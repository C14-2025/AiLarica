package br.inatel.ailarica.Login;

import br.inatel.ailarica.Cliente.Usuario;
import br.inatel.ailarica.Cliente.UsuarioService;
import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteAuthService;
import br.inatel.ailarica.Restaurantes.RestauranteService; // Import necessário
import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Controller de autenticação unificada.
 * Gerencia login de usuários e restaurantes com geração de tokens JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final RestauranteAuthService restauranteAuthService;
    private final RestauranteService restauranteService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UsuarioService usuarioService,
                          RestauranteAuthService restauranteAuthService,
                          RestauranteService restauranteService, // Injeção
                          JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.restauranteAuthService = restauranteAuthService;
        this.restauranteService = restauranteService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        // Validações básicas
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Email é obrigatório!"));
        }
        if (loginRequest.getSenha() == null || loginRequest.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Senha é obrigatória!"));
        }
        if (loginRequest.getTipo() == null || loginRequest.getTipo().isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Tipo de usuário é obrigatório! (USUARIO ou RESTAURANTE)"));
        }

        // ---------------------------------------------
        // LOGIN USUÁRIO — CORREÇÃO: Usa o método login de 2 parâmetros
        // ---------------------------------------------
        if ("USUARIO".equalsIgnoreCase(loginRequest.getTipo())) {

            Usuario usuario = usuarioService.login( // <-- AGORA ESTÁ CORRETO!
                    loginRequest.getEmail(),
                    loginRequest.getSenha()
            );

            if (usuario != null) {
                String token = jwtTokenProvider.generateToken(
                        usuario.getId(),
                        usuario.getEmail(),
                        "USUARIO"
                );

                return ResponseEntity.ok(new AuthResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        "USUARIO",
                        usuario.getEndereco(),
                        token,
                        "Login de usuário bem-sucedido!"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Email ou senha incorretos ou conta não confirmada!"));
            }
        }

        // ---------------------------------------------
        // LOGIN RESTAURANTE
        // ---------------------------------------------
        else if ("RESTAURANTE".equalsIgnoreCase(loginRequest.getTipo())) {

            Restaurante restaurante = restauranteAuthService.loginRestaurante(
                    loginRequest.getEmail(),
                    loginRequest.getSenha()
            );

            if (restaurante != null) {
                String token = jwtTokenProvider.generateToken(
                        restaurante.getIdRestaurante(),
                        restaurante.getEmail(),
                        "RESTAURANTE"
                );

                return ResponseEntity.ok(new AuthResponse(
                        restaurante.getIdRestaurante(),
                        restaurante.getNome(),
                        restaurante.getEmail(),
                        "RESTAURANTE",
                        restaurante.getEndereco(),
                        token,
                        "Login de restaurante bem-sucedido!"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Email, senha incorretos ou restaurante inativo!"));
            }
        }

        return ResponseEntity.badRequest().body(
                new AuthResponse("Tipo de usuário inválido! Use USUARIO ou RESTAURANTE")
        );
    }

    @PostMapping("/cadastro/usuario")
    public ResponseEntity<String> cadastroUsuario(@RequestBody Usuario usuario) {
        if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty()) {
            return ResponseEntity.badRequest().body("Endereço é obrigatório para cadastro de usuário!");
        }
        usuario.setTipo("USUARIO");

        if (usuarioService.cadastrar(usuario)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Erro ao cadastrar usuário. Email pode já estar registrado ou dados inválidos.");
    }

    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmarConta(@RequestParam String email) {
        if (usuarioService.confirmarEmail(email)) {
            return ResponseEntity.ok("Conta confirmada com sucesso! Agora você pode logar.");
        }
        return ResponseEntity.badRequest().body("Email não encontrado para confirmação.");
    }

    @PostMapping("/cadastro/restaurante")
    public ResponseEntity<String> cadastroRestaurante(@RequestBody Restaurante restaurante) {
        if (restaurante.getEmail() == null || restaurante.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email é obrigatório para cadastro de restaurante!");
        }
        if (restaurante.getSenha() == null || restaurante.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha é obrigatória para cadastro de restaurante!");
        }

        if (restauranteAuthService.buscarPorEmail(restaurante.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado para outro restaurante!");
        }

        try {
            restaurante.setAtivo(true);

            restauranteService.criar(restaurante); // SALVAMENTO REAL (Correção de Bug)

            return ResponseEntity.status(HttpStatus.CREATED).body("Restaurante cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar restaurante: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return ResponseEntity.badRequest().body("Header Authorization é obrigatório!");
        }

        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null) {
            return ResponseEntity.badRequest().body("Formato inválido! Use: Bearer <token>");
        }

        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            Integer id = jwtTokenProvider.getIdFromToken(token);
            String tipo = jwtTokenProvider.getTipoFromToken(token);

            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("valido", true);
                put("id", id);
                put("email", email);
                put("tipo", tipo);
                put("mensagem", "Token válido!");
            }});
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado!");
        }
    }
}