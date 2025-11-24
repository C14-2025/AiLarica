package br.inatel.ailarica.Cliente;

import br.inatel.ailarica.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    // --- VALIDAÇÕES ---

    private boolean isEmailValid(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isSenhaValida(String senha) {
        if (senha == null) return false;
        // Mínimo 8 chars, 1 número, 1 especial
        return senha.length() >= 8 &&
                senha.matches(".*\\d.*") &&
                senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    private boolean isEnderecoValido(String endereco) {
        return endereco != null && endereco.trim().length() >= 5;
    }

    // --- AÇÕES PRINCIPAIS ---

    public boolean cadastrar(Usuario usuario) {
        if (!isEmailValid(usuario.getEmail())) return false;
        if (!isSenhaValida(usuario.getSenha())) return false;
        if (usuarioDAO.buscarPorEmail(usuario.getEmail()).isPresent()) return false;

        if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
            usuario.setTipo("USUARIO");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // ✅ CORREÇÃO: Confirma o usuário imediatamente para o fluxo de testes.
        usuario.confirmar();

        usuarioDAO.criar(usuario);
        return true;
    }

    public boolean atualizarDados(int id, Usuario novosDados) {
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuarioExistente = usuarioOpt.get();

            // Atualiza apenas o que é permitido
            if (novosDados.getNome() != null) {
                usuarioExistente.setNome(novosDados.getNome());
            }
            if (novosDados.getEndereco() != null) {
                usuarioExistente.setEndereco(novosDados.getEndereco());
            }

            // O DAO.atualizar geralmente atualiza tudo, então garantimos
            // que senha e email continuam os mesmos do objeto existente
            usuarioDAO.atualizar(usuarioExistente);
            return true;
        }
        return false;
    }

    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) {
        if (!isSenhaValida(novaSenha)) return false;

        return usuarioDAO.buscarPorEmail(email)
                .filter(u -> passwordEncoder.matches(senhaAntiga, u.getSenha())) // Verifica senha antiga hash
                .map(u -> {
                    u.setSenha(passwordEncoder.encode(novaSenha)); // Salva nova hash
                    usuarioDAO.atualizar(u);
                    return true;
                })
                .orElse(false);
    }

    // Login Genérico (Usado pelo AuthController)
    public Usuario login(String email, String senha) {
        return usuarioDAO.buscarPorEmail(email)
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()) && u.isConfirmado())
                .orElse(null);
    }

    // Login Específico de Usuário (Com validação de Endereço)
    public Usuario loginUsuario(String email, String senha, String endereco) {
        if (!isEnderecoValido(endereco)) return null;

        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getSenha()) && usuario.isConfirmado()) {
                if ("USUARIO".equals(usuario.getTipo())) {
                    // Atualiza endereço se necessário
                    if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty() || !usuario.getEndereco().equals(endereco)) {
                        usuario.setEndereco(endereco);
                        usuarioDAO.atualizar(usuario);
                    }
                    return usuario;
                }
            }
        }
        return null;
    }

    public boolean confirmarEmail(String email) {
        return usuarioDAO.buscarPorEmail(email)
                .map(u -> {
                    u.confirmar();
                    usuarioDAO.atualizar(u);
                    return true;
                })
                .orElse(false);
    }

    // --- CONSULTAS ---

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public boolean deletar(int id) {
        return usuarioDAO.deletar(id);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }
}