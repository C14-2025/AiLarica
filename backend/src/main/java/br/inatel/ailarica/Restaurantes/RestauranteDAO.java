package br.inatel.ailarica.Restaurantes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RestauranteDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PratoDAO pratoDAO;
    private final ObjectMapper objectMapper;
    private final RowMapper<Restaurante> restauranteRowMapper;

    @Autowired
    public RestauranteDAO(JdbcTemplate jdbcTemplate, PratoDAO pratoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.pratoDAO = pratoDAO;
        this.objectMapper = new ObjectMapper();

        // Mapeamento do Banco para o Objeto
        this.restauranteRowMapper = (rs, rowNum) -> {
            Restaurante r = new Restaurante();
            r.setIdRestaurante(rs.getInt("idRestaurante"));
            r.setNome(rs.getString("nome"));
            r.setDescricao(rs.getString("descricao"));
            r.setEndereco(rs.getString("endereco"));
            r.setTelefone(rs.getString("telefone"));
            r.setAvaliacao(rs.getFloat("avaliacao"));
            r.setAtivo(rs.getBoolean("ativo"));
            r.setFotoPerfil(rs.getString("fotoPerfil"));
            r.setEmail(rs.getString("email"));
            r.setSenha(rs.getString("senha"));

            // ✅ LENDO A COLUNA NOVA
            r.setTempoMedioEntrega(rs.getString("tempoMedioEntrega"));

            try {
                String horariosJson = rs.getString("horarios_json");
                if (horariosJson != null && !horariosJson.isEmpty()) {
                    RestauranteHorario horarios = objectMapper.readValue(horariosJson, RestauranteHorario.class);
                    r.setHorarios(horarios);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            r.setCardapio(pratoDAO.listarPorRestaurante(r.getIdRestaurante()));
            return r;
        };
    }

    public Restaurante criar(Restaurante restaurante) {
        // ✅ SQL ATUALIZADO (Adicionado tempoMedioEntrega no final)
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, avaliacao, ativo, fotoPerfil, horarios_json, email, senha, tempoMedioEntrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            String horariosJson = objectMapper.writeValueAsString(restaurante.getHorarios());

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, restaurante.getNome());
                ps.setString(2, restaurante.getDescricao());
                ps.setString(3, restaurante.getEndereco());
                ps.setString(4, restaurante.getTelefone());
                ps.setFloat(5, restaurante.getAvaliacao());
                ps.setBoolean(6, restaurante.isAtivo());
                ps.setString(7, restaurante.getFotoPerfil());
                ps.setString(8, horariosJson);
                ps.setString(9, restaurante.getEmail());
                ps.setString(10, restaurante.getSenha());
                // ✅ SALVANDO A COLUNA NOVA
                ps.setString(11, restaurante.getTempoMedioEntrega());
                return ps;
            }, keyHolder);

            int idRestaurante = Objects.requireNonNull(keyHolder.getKey()).intValue();
            restaurante.setIdRestaurante(idRestaurante);

            if (restaurante.getCardapio() != null && !restaurante.getCardapio().isEmpty()) {
                for (Prato prato : restaurante.getCardapio()) {
                    pratoDAO.criar(prato, idRestaurante);
                }
            }
            return restaurante;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void atualizar(Restaurante restaurante) {
        // ✅ SQL ATUALIZADO
        String sql = "UPDATE restaurante SET nome=?, descricao=?, endereco=?, telefone=?, avaliacao=?, ativo=?, fotoPerfil=?, horarios_json=?, email=?, senha=?, tempoMedioEntrega=? WHERE idRestaurante=?";
        try {
            String horariosJson = objectMapper.writeValueAsString(restaurante.getHorarios());
            jdbcTemplate.update(sql,
                    restaurante.getNome(),
                    restaurante.getDescricao(),
                    restaurante.getEndereco(),
                    restaurante.getTelefone(),
                    restaurante.getAvaliacao(),
                    restaurante.isAtivo(),
                    restaurante.getFotoPerfil(),
                    horariosJson,
                    restaurante.getEmail(),
                    restaurante.getSenha(),
                    // ✅ ATUALIZANDO A COLUNA NOVA
                    restaurante.getTempoMedioEntrega(),
                    restaurante.getIdRestaurante());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // --- Outros métodos continuam iguais ---
    public List<Restaurante> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM restaurante", restauranteRowMapper);
    }

    public Restaurante buscarPorId(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM restaurante WHERE idRestaurante = ?", restauranteRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public java.util.Optional<Restaurante> buscarPorIdOptional(int id) {
        return java.util.Optional.ofNullable(buscarPorId(id));
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM restaurante WHERE idRestaurante = ?", id);
    }

    public boolean atualizarStatus(int id, boolean ativo) {
        return jdbcTemplate.update("UPDATE restaurante SET ativo = ? WHERE idRestaurante = ?", ativo, id) > 0;
    }

    public java.util.Optional<Restaurante> buscarPorEmail(String email) {
        try {
            return java.util.Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM restaurante WHERE email = ?", restauranteRowMapper, email));
        } catch (Exception e) {
            return java.util.Optional.empty();
        }
    }
}