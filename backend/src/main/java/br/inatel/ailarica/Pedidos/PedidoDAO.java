package br.inatel.ailarica.Pedidos;

import br.inatel.ailarica.Restaurantes.DashboardDTO; // Import novo
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate; // Import novo para data
import java.util.List;
import java.util.Objects;

@Repository
public class PedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ItemPedidoDAO itemPedidoDAO;

    public PedidoDAO(JdbcTemplate jdbcTemplate, ItemPedidoDAO itemPedidoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemPedidoDAO = itemPedidoDAO;
    }

    private final RowMapper<Pedido> pedidoRowMapper = (rs, rowNum) -> {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("idPedido"));
        p.setStatus(rs.getString("status"));
        p.setValorTotal(rs.getFloat("valorTotal"));
        p.setDataHora(rs.getString("dataHora"));
        p.setIdUsuario(rs.getInt("idUsuario"));
        p.setIdRestaurante(rs.getInt("idRestaurante"));
        return p;
    };

    public Pedido criar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (status, valorTotal, dataHora, idUsuario, idRestaurante) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getStatus());
            ps.setFloat(2, pedido.getValorTotal());
            ps.setString(3, pedido.getDataHora());
            ps.setInt(4, pedido.getIdUsuario());
            ps.setInt(5, pedido.getIdRestaurante());
            return ps;
        }, keyHolder);

        int idPedido = Objects.requireNonNull(keyHolder.getKey()).intValue();
        pedido.setIdPedido(idPedido);

        for (ItemPedido item : pedido.getItens()) {
            item.setIdPedido(idPedido);
            itemPedidoDAO.criar(item);
        }

        return pedido;
    }

    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE idPedido = ?";
        try {
            Pedido pedido = jdbcTemplate.queryForObject(sql, pedidoRowMapper, idPedido);
            if (pedido != null) {
                List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(idPedido);
                pedido.setItens(itens);
            }
            return pedido;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Pedido> listarPorRestaurante(int idRestaurante) {
        String sql = "SELECT * FROM pedido WHERE idRestaurante = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, idRestaurante);
        for (Pedido p : pedidos) {
            p.setItens(itemPedidoDAO.listarPorPedido(p.getIdPedido()));
        }
        return pedidos;
    }

    public List<Pedido> listarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM pedido WHERE idUsuario = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, idUsuario);
        for (Pedido p : pedidos) {
            p.setItens(itemPedidoDAO.listarPorPedido(p.getIdPedido()));
        }
        return pedidos;
    }

    public void atualizarStatus(int idPedido, String novoStatus) {
        String sql = "UPDATE pedido SET status = ? WHERE idPedido = ?";
        jdbcTemplate.update(sql, novoStatus, idPedido);
    }

    // ==========================================
    // 📊 MÉTODOS NOVOS PARA O DASHBOARD 📊
    // ==========================================

    // 1. Contar pedidos de HOJE
    public int contarPedidosHoje(int idRestaurante) {
        String hoje = LocalDate.now().toString(); // Ex: "2025-11-23"

        // O LIKE '2025-11-23%' garante que pegamos qualquer hora do dia
        String sql = "SELECT COUNT(*) FROM pedido WHERE idRestaurante = ? AND dataHora LIKE ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idRestaurante, hoje + "%");
        return count != null ? count : 0;
    }

    // 2. Somar Faturamento de HOJE (Ignora Cancelados)
    public Double somarFaturamentoHoje(int idRestaurante) {
        String hoje = LocalDate.now().toString();

        String sql = "SELECT SUM(valorTotal) FROM pedido WHERE idRestaurante = ? AND status != 'CANCELADO' AND dataHora LIKE ?";

        Double total = jdbcTemplate.queryForObject(sql, Double.class, idRestaurante, hoje + "%");
        return (total != null) ? total : 0.0;
    }

    // 3. Dados para o Gráfico (Últimos 7 dias)
    public List<DashboardDTO.VendaDiaria> buscarVendasUltimos7Dias(int idRestaurante) {
        // SQLITE: substr(dataHora, 1, 10) pega só a parte da data (YYYY-MM-DD)
        // Agrupa por dia e soma o total
        String sql = """
            SELECT substr(dataHora, 1, 10) as dia, SUM(valorTotal) as total
            FROM pedido
            WHERE idRestaurante = ? 
            AND status != 'CANCELADO'
            GROUP BY dia
            ORDER BY dia DESC
            LIMIT 7
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new DashboardDTO.VendaDiaria(
                rs.getString("dia"),
                rs.getDouble("total")
        ), idRestaurante);
    }
}