package br.inatel.ailarica;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Validações de email e senha
    private boolean isEmailValid(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isSenhaValida(String senha) {
        if (senha == null) {
            System.out.println("A senha não pode ser nula.");
            return false;
        }
        boolean valido = true;

        if (senha.length() < 8) {
            System.out.println("A senha deve ter pelo menos 8 caracteres.");
            valido = false;
        }

        if (!senha.matches(".*\\d.*")) {
            System.out.println("A senha deve conter pelo menos um número.");
            valido = false;
        }

        if (!senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            System.out.println("A senha deve conter pelo menos um caractere especial.");
            valido = false;
        }

        return valido;
    }

    // Validação de endereço
    private boolean isEnderecoValido(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            System.out.println("O endereço não pode ser vazio.");
            return false;
        }
        return endereco.trim().length() >= 5;
    }

    // Cadastro por campos (compatível com versões antigas)
    public boolean cadastrar(String nome, String email, String senha) {
        if (!isEmailValid(email)) {
            System.out.println("Email inválido!");
            return false;
        }

        if (!isSenhaValida(senha)) {
            System.out.println("A senha deve ter pelo menos 6 caracteres!");
            return false;
        }

        if (usuarioDAO.buscarPorEmail(email).isPresent()) {
            return false; // já existe
        }

        usuarioDAO.criar(new Usuario(nome, email, senha, null, "USUARIO"));
        return true;
    }

    // Cadastro por objeto Usuario
    public boolean cadastrar(Usuario usuario) {
        if (!isEmailValid(usuario.getEmail())) {
            System.out.println("Email inválido!");
            return false;
        }

        if (!isSenhaValida(usuario.getSenha())) {
            System.out.println("A senha deve ter pelo menos 6 caracteres!");
            return false;
        }

        if (usuarioDAO.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return false; // já existe
        }

        // Definir tipo padrão se não estiver definido
        if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
            usuario.setTipo("USUARIO");
        }

        usuarioDAO.criar(usuario);
        return true;
    }

    // Atualizar senha
    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) {
        if (!isSenhaValida(novaSenha)) {
            return false; // bloqueia atualização se não cumprir regras
        }

        return usuarioDAO.buscarPorEmail(email)
                .filter(u -> u.getSenha().equals(senhaAntiga))
                .map(u -> {
                    u.setSenha(novaSenha);
                    usuarioDAO.atualizar(u);
                    return true;
                })
                .orElse(false);
    }

    // Login (só permite se confirmado)
    public Usuario login(String email, String senha) {
        return usuarioDAO.buscarPorEmail(email)
                .filter(u -> u.getSenha().equals(senha) && u.isConfirmado())
                .orElse(null);
    }

    // Login para usuário com validação de endereço
    public Usuario loginUsuario(String email, String senha, String endereco) {
        // Validar endereço
        if (!isEnderecoValido(endereco)) {
            System.out.println("Endereço inválido ou vazio!");
            return null;
        }

        // Buscar usuário
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Validar senha e confirmação
            if (usuario.getSenha().equals(senha) && usuario.isConfirmado()) {
                // Validar que é do tipo USUARIO
                if ("USUARIO".equals(usuario.getTipo())) {
                    // Validar que o endereço corresponde (ou atualizar se necessário)
                    if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty()) {
                        usuario.setEndereco(endereco);
                        usuarioDAO.atualizar(usuario);
                    }
                    return usuario;
                }
            }
        }

        return null;
    }

    // Método auxiliar para confirmar email (necessário para o fluxo de cadastro)
    public boolean confirmarEmail(String email) {
        return usuarioDAO.buscarPorEmail(email)
                .map(u -> {
                    u.confirmar();
                    usuarioDAO.atualizar(u);
                    return true;
                })
                .orElse(false);
    }

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // Deletar usuário
    public boolean deletar(int id) {
        return usuarioDAO.deletar(id);
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }
}
