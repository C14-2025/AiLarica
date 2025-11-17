package br.inatel.ailarica;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository // Agora é um repositório de DB real
public class UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper para traduzir o banco para o objeto Usuario
    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario u = new Usuario();
        u.setId(rs.getInt("idUsuario"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenha(rs.getString("senha"));
        u.setEndereco(rs.getString("endereco"));
        u.setConfirmado(rs.getInt("confirmado") == 1);
        u.setTipo(rs.getString("tipo"));
        return u;
    };

    // Criar
    public void criar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, endereco, confirmado, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getEndereco());
            ps.setBoolean(5, usuario.isConfirmado());
            ps.setString(6, usuario.getTipo() != null ? usuario.getTipo() : "USUARIO");
            return ps;
        }, keyHolder);

        usuario.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    // Buscar por Email
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, email);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty(); // Não encontrado
        }
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty(); // Não encontrado
        }
    }

    // Atualizar (Necessário para o service de "confirmarEmail" e "atualizarSenha")
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, endereco = ?, confirmado = ?, tipo = ? WHERE idUsuario = ?";
        jdbcTemplate.update(sql,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getEndereco(),
                usuario.isConfirmado(),
                usuario.getTipo() != null ? usuario.getTipo() : "USUARIO",
                usuario.getId());
    }

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }

    // Deletar usuário
    public boolean deletar(int id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
