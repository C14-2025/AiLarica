package br.inatel.ailarica.Pedidos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemPedidoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ItemPedidoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper para traduzir a linha do DB para o objeto ItemPedido
    private final RowMapper<ItemPedido> itemPedidoRowMapper = (rs, rowNum) -> {
        ItemPedido item = new ItemPedido();
        item.setIdItemPedido(rs.getInt("idItemPedido"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setPrecoNoMomento(rs.getFloat("precoNoMomento"));
        item.setIdPedido(rs.getInt("idPedido"));
        item.setIdPrato(rs.getInt("idPrato"));
        return item;
    };

    // Criar um item dentro de um pedido
    public void criar(ItemPedido item) {
        String sql = "INSERT INTO item_pedido (quantidade, precoNoMomento, idPedido, idPrato) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                item.getQuantidade(),
                item.getPrecoNoMomento(),
                item.getIdPedido(),
                item.getIdPrato());
    }

    // Listar todos os itens de um pedido específico
    public List<ItemPedido> listarPorPedido(int idPedido) {
        String sql = "SELECT * FROM item_pedido WHERE idPedido = ?";
        return jdbcTemplate.query(sql, itemPedidoRowMapper, idPedido);
    }
}
