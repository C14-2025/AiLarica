package br.inatel.ailarica.Restaurantes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PratoDAO {

    // O Spring injeta o JdbcTemplate configurado (do application.properties)
    private final JdbcTemplate jdbcTemplate;

    // O Spring gerencia a injeção do JdbcTemplate
    public PratoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper para traduzir uma linha do ResultSet para um objeto Prato
    private final RowMapper<Prato> pratoRowMapper = (rs, rowNum) -> {
        Prato p = new Prato();
        p.setIdPrato(rs.getInt("idPrato"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getFloat("preco"));
        p.setDisponivel(rs.getBoolean("disponivel"));
        p.setFoto(rs.getString("foto"));
        p.setIdRestaurante(rs.getInt("idRestaurante"));
        return p;
    };

    // ✅ Criar prato (mesmo que inserir)
    public void criar(Prato prato, int idRestaurante) {
        String sql = "INSERT INTO prato (nome, descricao, preco, disponivel, foto, idRestaurante) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.isDisponivel(),
                prato.getFoto(),
                idRestaurante);
    }

    // ✅ Listar pratos de um restaurante
    public List<Prato> listarPorRestaurante(int idRestaurante) {
        String sql = "SELECT * FROM prato WHERE idRestaurante = ?";
        // Usa o rowMapper para converter cada linha em um objeto Prato
        return jdbcTemplate.query(sql, pratoRowMapper, idRestaurante);
    }

    // ✅ Deletar todos os pratos de um restaurante
    // (Será desnecessário se o Schema.sql tiver "ON DELETE CASCADE")
    public void deletarPorRestaurante(int idRestaurante) {
        String sql = "DELETE FROM prato WHERE idRestaurante = ?";
        jdbcTemplate.update(sql, idRestaurante);
    }

    // ✅ (Opcional) Deletar prato individualmente
    public void deletarPorId(int idPrato) {
        String sql = "DELETE FROM prato WHERE idPrato = ?";
        jdbcTemplate.update(sql, idPrato);
    }
}
