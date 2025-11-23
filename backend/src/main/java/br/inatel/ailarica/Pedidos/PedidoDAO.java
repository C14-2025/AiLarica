package br.inatel.ailarica.Pedidos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ItemPedidoDAO itemPedidoDAO; // Injeta o outro DAO

    public PedidoDAO(JdbcTemplate jdbcTemplate, ItemPedidoDAO itemPedidoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemPedidoDAO = itemPedidoDAO;
    }

    // RowMapper para o Pedido (NÃO busca os itens, só o cabeçalho)
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

    // CREATE: Salva o Pedido (cabeçalho) e depois salva os Itens (corpo)
    public Pedido criar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (status, valorTotal, dataHora, idUsuario, idRestaurante) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Salva o "cabeçalho" do pedido
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getStatus());
            ps.setFloat(2, pedido.getValorTotal());
            ps.setString(3, pedido.getDataHora());
            ps.setInt(4, pedido.getIdUsuario());
            ps.setInt(5, pedido.getIdRestaurante());
            return ps;
        }, keyHolder);

        // 2. Pega o ID gerado para o pedido
        int idPedido = Objects.requireNonNull(keyHolder.getKey()).intValue();
        pedido.setIdPedido(idPedido);

        // 3. Salva os "itens" do pedido (o corpo)
        for (ItemPedido item : pedido.getItens()) {
            item.setIdPedido(idPedido); // Seta o ID do pedido no item
            itemPedidoDAO.criar(item); // Salva o item
        }

        return pedido; // Retorna o pedido completo com o ID
    }

    // READ: Busca um pedido pelo ID
    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE idPedido = ?";
        try {
            // 1. Busca o "cabeçalho"
            Pedido pedido = jdbcTemplate.queryForObject(sql, pedidoRowMapper, idPedido);

            if (pedido != null) {
                // 2. Busca os "itens" e anexa ao objeto
                List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(idPedido);
                pedido.setItens(itens);
            }
            return pedido;
        } catch (Exception e) {
            return null; // Não encontrado
        }
    }

    // READ: Listar pedidos de um RESTAURANTE (para o seu wireframe)
    public List<Pedido> listarPorRestaurante(int idRestaurante) {
        String sql = "SELECT * FROM pedido WHERE idRestaurante = ?";
        // 1. Busca todos os "cabeçalhos" de pedido
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, idRestaurante);

        // 2. Para cada pedido, busca seus itens
        for (Pedido p : pedidos) {
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(p.getIdPedido());
            p.setItens(itens);
        }
        return pedidos;
    }

    // READ: Listar pedidos de um USUARIO
    public List<Pedido> listarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM pedido WHERE idUsuario = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, idUsuario);

        for (Pedido p : pedidos) {
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(p.getIdPedido());
            p.setItens(itens);
        }
        return pedidos;
    }

    // UPDATE: Atualizar o STATUS de um pedido (Funcionalidade 5)
    public void atualizarStatus(int idPedido, String novoStatus) {
        String sql = "UPDATE pedido SET status = ? WHERE idPedido = ?";
        jdbcTemplate.update(sql, novoStatus, idPedido);
    }
}
