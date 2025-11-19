package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Serviço de autenticação para restaurantes.
 * Gerencia login e validações de restaurante.
 */
@Service
public class RestauranteAuthService {

    private final RestauranteDAO restauranteDAO;

    public RestauranteAuthService(RestauranteDAO restauranteDAO) {
        this.restauranteDAO = restauranteDAO;
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
     * @param senha Senha do restaurante
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

            // Validar senha
            if (restaurante.getSenha() != null && restaurante.getSenha().equals(senha)) {
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
     * @param senhaAntiga Senha anterior
     * @param novaSenha Nova senha
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

            if (restaurante.getSenha() != null && restaurante.getSenha().equals(senhaAntiga)) {
                restaurante.setSenha(novaSenha);
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
