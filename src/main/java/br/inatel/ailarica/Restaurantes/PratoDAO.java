package br.inatel.ailarica.Restaurantes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PratoDAO {

    private final JdbcTemplate jdbcTemplate;

    public PratoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    // --- MÉTODO 'CRIAR' ATUALIZADO ---
    // (Agora retorna o Prato com o ID gerado)
    public Prato criar(Prato prato, int idRestaurante) {
        String sql = "INSERT INTO prato (nome, descricao, preco, disponivel, foto, idRestaurante) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Para pegar o ID gerado

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, prato.getNome());
            ps.setString(2, prato.getDescricao());
            ps.setFloat(3, prato.getPreco());
            ps.setBoolean(4, prato.isDisponivel());
            ps.setString(5, prato.getFoto());
            ps.setInt(6, idRestaurante);
            return ps;
        }, keyHolder);

        // Pega o ID gerado e seta no objeto
        int idPrato = Objects.requireNonNull(keyHolder.getKey()).intValue();
        prato.setIdPrato(idPrato);
        prato.setIdRestaurante(idRestaurante); // Garante que o ID do restaurante também está setado

        return prato; // Retorna o objeto completo
    }

    // --- NOVO MÉTODO ---
    // ✅ UPDATE (Atualizar prato)
    public void atualizar(Prato prato) {
        String sql = "UPDATE prato SET nome=?, descricao=?, preco=?, disponivel=?, foto=? WHERE idPrato=?";
        jdbcTemplate.update(sql,
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.isDisponivel(),
                prato.getFoto(),
                prato.getIdPrato());
    }

    // --- MÉTODOS QUE VOCÊ JÁ TINHA ---

    public List<Prato> listarPorRestaurante(int idRestaurante) {
        String sql = "SELECT * FROM prato WHERE idRestaurante = ?";
        return jdbcTemplate.query(sql, pratoRowMapper, idRestaurante);
    }

    public void deletarPorRestaurante(int idRestaurante) {
        String sql = "DELETE FROM prato WHERE idRestaurante = ?";
        jdbcTemplate.update(sql, idRestaurante);
    }

    public void deletarPorId(int idPrato) {
        String sql = "DELETE FROM prato WHERE idPrato = ?";
        jdbcTemplate.update(sql, idPrato);
    }

    public Prato buscarPorId(int idPrato) {
        String sql = "SELECT * FROM prato WHERE idPrato = ?";
        try {
            return jdbcTemplate.queryForObject(sql, pratoRowMapper, idPrato);
        } catch (Exception e) {
            return null; // Retorna null se não encontrar
        }
    }
}