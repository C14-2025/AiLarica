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

    private final PratoDAO pratoDAO; // Injeta o PratoDAO
    private final ObjectMapper objectMapper; // Para serializar/desserializar horários
    private final RowMapper<Restaurante> restauranteRowMapper; // RowMapper completo para Restaurante

    @Autowired
    public RestauranteDAO(JdbcTemplate jdbcTemplate, PratoDAO pratoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.pratoDAO = pratoDAO;
        this.objectMapper = new ObjectMapper(); // Instancia o conversor JSON

        // Inicializa o RowMapper aqui, onde objectMapper e pratoDAO já estão inicializados
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

            // 1. Desserializa os horários a partir da coluna JSON
            try {
                String horariosJson = rs.getString("horarios_json");
                if (horariosJson != null && !horariosJson.isEmpty()) {
                    RestauranteHorario horarios = objectMapper.readValue(horariosJson, RestauranteHorario.class);
                    r.setHorarios(horarios);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Tratar erro de desserialização
            }

            // 2. Busca o cardápio usando o PratoDAO
            r.setCardapio(pratoDAO.listarPorRestaurante(r.getIdRestaurante()));

            return r;
        };
    } // Fecha o construtor

    // CREATE — cria restaurante e adiciona seus pratos
    public void criar(Restaurante restaurante) {
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, avaliacao, ativo, fotoPerfil, horarios_json) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // Para pegar o ID gerado

        try {
            // Converte o objeto de horários para uma string JSON
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
                return ps;
            }, keyHolder);

            // Pega o ID gerado e seta no objeto
            int idRestaurante = Objects.requireNonNull(keyHolder.getKey()).intValue();
            restaurante.setIdRestaurante(idRestaurante);

            // Insere pratos (se houver) usando o PratoDAO
            if (restaurante.getCardapio() != null && !restaurante.getCardapio().isEmpty()) {
                for (Prato prato : restaurante.getCardapio()) {
                    pratoDAO.criar(prato, idRestaurante);
                }
            }
            System.out.println("✅ Restaurante e cardápio inseridos com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ — lista todos os restaurantes com cardápio
    public List<Restaurante> listarTodos() {
        String sql = "SELECT * FROM restaurante";
        return jdbcTemplate.query(sql, restauranteRowMapper);
    }

    // READ — busca restaurante por ID com cardápio
    public Restaurante buscarPorId(int id) {
        String sql = "SELECT * FROM restaurante WHERE idRestaurante = ?";
        try {
            return jdbcTemplate.queryForObject(sql, restauranteRowMapper, id);
        } catch (Exception e) {
            // Retorna null se não encontrar (EmptyResultDataAccessException)
            return null;
        }
    }

    // UPDATE — atualiza dados do restaurante (não mexe nos pratos)
    public void atualizar(Restaurante restaurante) {
        String sql = "UPDATE restaurante SET nome=?, descricao=?, endereco=?, telefone=?, avaliacao=?, ativo=?, fotoPerfil=?, horarios_json=? WHERE idRestaurante=?";
        try {
            // Converte horários para JSON
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
                    restaurante.getIdRestaurante());

            System.out.println("✅ Restaurante atualizado com sucesso!");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // DELETE — remove restaurante.
    // (Seu Schema.sql deve ter "ON DELETE CASCADE" na tabela 'prato'
    // para que isso funcione e delete os pratos juntos)
    public void deletar(int id) {
        String sqlRestaurante = "DELETE FROM restaurante WHERE idRestaurante = ?";

        // Se o "ON DELETE CASCADE" estiver no Schema.sql, você só precisa disso:
        jdbcTemplate.update(sqlRestaurante, id);

        System.out.println("❌ Restaurante deletado com sucesso (e pratos em cascata)!");
    }

    // ALTERAR STATUS — ativa ou desativa um restaurante
    public boolean atualizarStatus(int id, boolean ativo) {
        String sql = "UPDATE restaurante SET ativo = ? WHERE idRestaurante = ?";
        int rowsAffected = jdbcTemplate.update(sql, ativo, id);
        return rowsAffected > 0;
    }
}