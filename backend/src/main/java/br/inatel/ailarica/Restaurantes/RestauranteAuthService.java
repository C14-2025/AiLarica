package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RestauranteAuthService {

    private final RestauranteDAO restauranteDAO;
    private final PasswordEncoder passwordEncoder;

    public RestauranteAuthService(RestauranteDAO restauranteDAO, PasswordEncoder passwordEncoder) {
        this.restauranteDAO = restauranteDAO;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean isEmailValid(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isSenhaValida(String senha) {
        // Validações de senha (mantive simplificado aqui, mas mantenha a sua lógica completa)
        return senha != null && senha.length() >= 8;
    }

    /**
     * LOGIN CORRIGIDO:
     * Agora o dono pode logar mesmo se a loja estiver FECHADA (ativo = false).
     */
    public Restaurante loginRestaurante(String email, String senha) {
        // Validar email
        if (!isEmailValid(email)) {
            return null;
        }

        // Buscar restaurante
        Optional<Restaurante> restauranteOpt = restauranteDAO.buscarPorEmail(email);

        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();

            // 1. Verifica a senha
            if (restaurante.getSenha() != null && passwordEncoder.matches(senha, restaurante.getSenha())) {

                // --- MUDANÇA AQUI ---
                // Removemos o 'if (restaurante.isAtivo())'.
                // O login é permitido sempre que a senha estiver correta.
                // O 'ativo' agora serve apenas para dizer se a loja está aberta para clientes.

                return restaurante;
            }
        }
        return null;
    }

    // Atualizar Senha e Buscar por Email (Mantidos iguais)
    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) {
        // ... (Mantenha sua lógica de atualização de senha aqui)
        // Se precisar eu mando completo, mas acho que você já tem essa parte.
        Optional<Restaurante> restauranteOpt = restauranteDAO.buscarPorEmail(email);
        if (restauranteOpt.isPresent()) {
            Restaurante r = restauranteOpt.get();
            if (passwordEncoder.matches(senhaAntiga, r.getSenha())) {
                r.setSenha(passwordEncoder.encode(novaSenha));
                restauranteDAO.atualizar(r);
                return true;
            }
        }
        return false;
    }

    public Optional<Restaurante> buscarPorEmail(String email) {
        return restauranteDAO.buscarPorEmail(email);
    }
}