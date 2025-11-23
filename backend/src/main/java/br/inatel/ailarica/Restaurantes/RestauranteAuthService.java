package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Serviço de autenticação para restaurantes.
 * Gerencia login e validações de restaurante com criptografia de senhas.
 */
@Service
public class RestauranteAuthService {

    private final RestauranteDAO restauranteDAO;
    private final PasswordEncoder passwordEncoder;

    public RestauranteAuthService(RestauranteDAO restauranteDAO, PasswordEncoder passwordEncoder) {
        this.restauranteDAO = restauranteDAO;
        this.passwordEncoder = passwordEncoder;
    }

    // Validação de email
    private boolean isEmailValid(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Validação de senha
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

    /**
     * Realiza login de restaurante com email e senha.
     * @param email Email do restaurante
     * @param senha Senha do restaurante (em texto plano)
     * @return Restaurante autenticado ou null se falhar
     */
    public Restaurante loginRestaurante(String email, String senha) {
        // Validar email
        if (!isEmailValid(email)) {
            System.out.println("Email inválido!");
            return null;
        }

        // Buscar restaurante por email
        Optional<Restaurante> restauranteOpt = restauranteDAO.buscarPorEmail(email);

        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();

            // Validar senha (comparar com hash)
            if (restaurante.getSenha() != null && passwordEncoder.matches(senha, restaurante.getSenha())) {
                // Verificar se está ativo
                if (restaurante.isAtivo()) {
                    return restaurante;
                } else {
                    System.out.println("Restaurante inativo!");
                    return null;
                }
            } else {
                System.out.println("Senha incorreta!");
                return null;
            }
        }

        System.out.println("Restaurante não encontrado!");
        return null;
    }

    /**
     * Atualiza a senha de um restaurante.
     * @param email Email do restaurante
     * @param senhaAntiga Senha anterior (em texto plano)
     * @param novaSenha Nova senha (em texto plano)
     * @return true se atualizado com sucesso
     */
    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) {
        if (!isSenhaValida(novaSenha)) {
            System.out.println("Nova senha não atende aos requisitos!");
            return false;
        }

        Optional<Restaurante> restauranteOpt = restauranteDAO.buscarPorEmail(email);

        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();

            // Validar senha anterior (comparar com hash)
            if (restaurante.getSenha() != null && passwordEncoder.matches(senhaAntiga, restaurante.getSenha())) {
                // Criptografar nova senha
                String novaSenhaHash = passwordEncoder.encode(novaSenha);
                restaurante.setSenha(novaSenhaHash);
                restauranteDAO.atualizar(restaurante);
                return true;
            }
        }

        return false;
    }

    /**
     * Busca restaurante por email.
     * @param email Email do restaurante
     * @return Restaurante encontrado ou vazio
     */
    public Optional<Restaurante> buscarPorEmail(String email) {
        return restauranteDAO.buscarPorEmail(email);
    }

    /**
     * Busca restaurante por ID.
     * @param id ID do restaurante
     * @return Restaurante encontrado ou vazio
     */
    public Optional<Restaurante> buscarPorId(int id) {
        return restauranteDAO.buscarPorIdOptional(id);
    }
}
