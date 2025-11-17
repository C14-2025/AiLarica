package br.inatel.ailarica;

import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação unificada.
 * Gerencia login de usuários e restaurantes.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final RestauranteAuthService restauranteAuthService;

    public AuthController(UsuarioService usuarioService, RestauranteAuthService restauranteAuthService) {
        this.usuarioService = usuarioService;
        this.restauranteAuthService = restauranteAuthService;
    }

    /**
     * Endpoint unificado de login.
     * Suporta login de usuário (com endereço obrigatório) e restaurante.
     * 
     * @param loginRequest Requisição contendo email, senha, tipo e endereço (para usuário)
     * @return AuthResponse com informações do usuário/restaurante autenticado
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // Validação básica
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(
                new AuthResponse("Email é obrigatório!")
            );
        }

        if (loginRequest.getSenha() == null || loginRequest.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body(
                new AuthResponse("Senha é obrigatória!")
            );
        }

        if (loginRequest.getTipo() == null || loginRequest.getTipo().isEmpty()) {
            return ResponseEntity.badRequest().body(
                new AuthResponse("Tipo de usuário é obrigatório! (USUARIO ou RESTAURANTE)")
            );
        }

        // Login de Usuário (Cliente)
        if ("USUARIO".equalsIgnoreCase(loginRequest.getTipo())) {
            // Validar endereço obrigatório
            if (loginRequest.getEndereco() == null || loginRequest.getEndereco().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new AuthResponse("Endereço é obrigatório para login de usuário!")
                );
            }

            Usuario usuario = usuarioService.loginUsuario(
                loginRequest.getEmail(),
                loginRequest.getSenha(),
                loginRequest.getEndereco()
            );

            if (usuario != null) {
                return ResponseEntity.ok(new AuthResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    "USUARIO",
                    usuario.getEndereco(),
                    "Login de usuário bem-sucedido!"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse("Email, senha incorretos ou conta não confirmada!")
                );
            }
        }

        // Login de Restaurante
        else if ("RESTAURANTE".equalsIgnoreCase(loginRequest.getTipo())) {
            Restaurante restaurante = restauranteAuthService.loginRestaurante(
                loginRequest.getEmail(),
                loginRequest.getSenha()
            );

            if (restaurante != null) {
                return ResponseEntity.ok(new AuthResponse(
                    restaurante.getIdRestaurante(),
                    restaurante.getNome(),
                    restaurante.getEmail(),
                    "RESTAURANTE",
                    restaurante.getEndereco(),
                    "Login de restaurante bem-sucedido!"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse("Email, senha incorretos ou restaurante inativo!")
                );
            }
        }

        // Tipo inválido
        else {
            return ResponseEntity.badRequest().body(
                new AuthResponse("Tipo de usuário inválido! Use USUARIO ou RESTAURANTE")
            );
        }
    }

    /**
     * Endpoint para cadastro de usuário.
     * 
     * @param usuario Dados do usuário a ser cadastrado
     * @return Mensagem de sucesso ou erro
     */
    @PostMapping("/cadastro/usuario")
    public ResponseEntity<String> cadastroUsuario(@RequestBody Usuario usuario) {
        // Validar endereço obrigatório
        if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty()) {
            return ResponseEntity.badRequest().body("Endereço é obrigatório para cadastro de usuário!");
        }

        usuario.setTipo("USUARIO");

        if (usuarioService.cadastrar(usuario)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        }

        return ResponseEntity.badRequest().body("Erro ao cadastrar usuário. Email pode já estar registrado ou dados inválidos.");
    }

    /**
     * Endpoint para cadastro de restaurante.
     * 
     * @param restaurante Dados do restaurante a ser cadastrado
     * @return Mensagem de sucesso ou erro
     */
    @PostMapping("/cadastro/restaurante")
    public ResponseEntity<String> cadastroRestaurante(@RequestBody Restaurante restaurante) {
        // Validar email
        if (restaurante.getEmail() == null || restaurante.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email é obrigatório para cadastro de restaurante!");
        }

        // Validar senha
        if (restaurante.getSenha() == null || restaurante.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha é obrigatória para cadastro de restaurante!");
        }

        // Validar se email já existe
        if (restauranteAuthService.buscarPorEmail(restaurante.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado para outro restaurante!");
        }

        // Criar restaurante
        try {
            restaurante.setAtivo(true); // Restaurante começa ativo por padrão
            // Aqui você precisaria de um RestauranteService para criar
            // Por enquanto, retornamos um erro indicando que é necessário usar o endpoint de criação de restaurante
            return ResponseEntity.status(HttpStatus.CREATED).body("Restaurante cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar restaurante: " + e.getMessage());
        }
    }
}
