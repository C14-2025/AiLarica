package br.inatel.ailarica.Restaurantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RestauranteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mapeia uma linha do banco em um objeto Restaurante
    private final RowMapper<Restaurante> restauranteMapper = new RowMapper<>() {
        @Override
        public Restaurante mapRow(ResultSet rs, int rowNum) throws SQLException {
            Restaurante r = new Restaurante(
                    rs.getInt("idRestaurante"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    null,
                    rs.getString("endereco"),
                    rs.getString("telefone"),
                    rs.getBoolean("ativo"),
                    rs.getString("fotoPerfil"),
                    null
            );
            r.setAvaliacao(rs.getFloat("avaliacao"));
            return r;
        }
    };

    public List<Restaurante> listarTodos() {
        String sql = "SELECT * FROM restaurante";
        return jdbcTemplate.query(sql, restauranteMapper);
    }

    public Optional<Restaurante> buscarPorId(int id) {
        String sql = "SELECT * FROM restaurante WHERE idRestaurante = ?";
        List<Restaurante> resultados = jdbcTemplate.query(sql, restauranteMapper, id);
        return resultados.stream().findFirst();
    }

    public void salvar(Restaurante restaurante) {
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, avaliacao, ativo, fotoPerfil) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                restaurante.getNome(),
                restaurante.getDescricao(),
                restaurante.getEndereco(),
                restaurante.getTelefone(),
                restaurante.getAvaliacao(),
                restaurante.isAtivo(),
                restaurante.getFotoPerfil());
    }

    public void atualizar(int id, Restaurante r) {
        String sql = "UPDATE restaurante SET nome=?, descricao=?, endereco=?, telefone=?, avaliacao=?, ativo=?, fotoPerfil=? WHERE idRestaurante=?";
        jdbcTemplate.update(sql,
                r.getNome(),
                r.getDescricao(),
                r.getEndereco(),
                r.getTelefone(),
                r.getAvaliacao(),
                r.isAtivo(),
                r.getFotoPerfil(),
                id);
    }

    public void deletar(int id) {
        String sql = "DELETE FROM restaurante WHERE idRestaurante = ?";
        jdbcTemplate.update(sql, id);
    }
}
