package br.inatel.ailarica;

import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteAuthService;
import br.inatel.ailarica.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthController.
 * Testa endpoints de autenticação unificada com geração de tokens JWT.
 */
@DisplayName("Testes do Controller de Autenticação")
class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RestauranteAuthService restauranteAuthService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(usuarioService, restauranteAuthService, jwtTokenProvider);
    }

    // ============ TESTES DE LOGIN DE USUÁRIO ============

    @Test
    @DisplayName("Deve fazer login de usuário com sucesso e retornar token JWT")
    void testLoginUsuarioComSucesso() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "Senha@123",
            "USUARIO",
            "Rua Principal, 123"
        );

        Usuario usuario = new Usuario("João Silva", "usuario@example.com", "Senha@123", "Rua Principal, 123", "USUARIO");
        usuario.setId(1);
        String token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...";

        when(usuarioService.loginUsuario(
            loginRequest.getEmail(),
            loginRequest.getSenha(),
            loginRequest.getEndereco()
        )).thenReturn(usuario);
        when(jwtTokenProvider.generateToken(1, "usuario@example.com", "USUARIO")).thenReturn(token);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
        assertEquals("USUARIO", response.getBody().getTipo());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals(token, response.getBody().getToken());
        verify(jwtTokenProvider, times(1)).generateToken(1, "usuario@example.com", "USUARIO");
    }

    @Test
    @DisplayName("Deve falhar no login de usuário quando endereço é vazio")
    void testLoginUsuarioComEnderecoVazio() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "Senha@123",
            "USUARIO",
            ""
        );

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
        assertTrue(response.getBody().getMensagem().contains("Endereço"));
    }

    @Test
    @DisplayName("Deve falhar no login quando email é vazio")
    void testLoginComEmailVazio() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "",
            "Senha@123",
            "USUARIO",
            "Rua Principal, 123"
        );

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
    }

    @Test
    @DisplayName("Deve falhar no login quando senha é vazia")
    void testLoginComSenhaVazia() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "",
            "USUARIO",
            "Rua Principal, 123"
        );

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
    }

    @Test
    @DisplayName("Deve falhar no login quando tipo é vazio")
    void testLoginComTipoVazio() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "Senha@123",
            "",
            "Rua Principal, 123"
        );

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
    }

    @Test
    @DisplayName("Deve falhar no login de usuário quando credenciais são inválidas")
    void testLoginUsuarioComCredenciaisInvalidas() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "SenhaErrada@123",
            "USUARIO",
            "Rua Principal, 123"
        );

        when(usuarioService.loginUsuario(
            loginRequest.getEmail(),
            loginRequest.getSenha(),
            loginRequest.getEndereco()
        )).thenReturn(null);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
    }

    // ============ TESTES DE LOGIN DE RESTAURANTE ============

    @Test
    @DisplayName("Deve fazer login de restaurante com sucesso e retornar token JWT")
    void testLoginRestauranteComSucesso() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "restaurante@example.com",
            "Senha@123",
            "RESTAURANTE",
            null
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail("restaurante@example.com");
        restaurante.setEndereco("Avenida Brasil, 456");
        restaurante.setAtivo(true);

        String token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...";

        when(restauranteAuthService.loginRestaurante(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        )).thenReturn(restaurante);
        when(jwtTokenProvider.generateToken(1, "restaurante@example.com", "RESTAURANTE")).thenReturn(token);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
        assertEquals("RESTAURANTE", response.getBody().getTipo());
        assertEquals("Restaurante Delícia", response.getBody().getNome());
        assertEquals(token, response.getBody().getToken());
        verify(jwtTokenProvider, times(1)).generateToken(1, "restaurante@example.com", "RESTAURANTE");
    }

    @Test
    @DisplayName("Deve falhar no login de restaurante quando credenciais são inválidas")
    void testLoginRestauranteComCredenciaisInvalidas() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "restaurante@example.com",
            "SenhaErrada@123",
            "RESTAURANTE",
            null
        );

        when(restauranteAuthService.loginRestaurante(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        )).thenReturn(null);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
    }

    @Test
    @DisplayName("Deve falhar no login com tipo inválido")
    void testLoginComTipoInvalido() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(
            "usuario@example.com",
            "Senha@123",
            "TIPO_INVALIDO",
            "Rua Principal, 123"
        );

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSucesso());
        assertTrue(response.getBody().getMensagem().contains("inválido"));
    }

    // ============ TESTES DE CADASTRO DE USUÁRIO ============

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void testCadastroUsuarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "Avenida Brasil, 456", "USUARIO");

        when(usuarioService.cadastrar(any(Usuario.class))).thenReturn(true);

        // Act
        ResponseEntity<String> response = authController.cadastroUsuario(usuario);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("sucesso"));
    }

    @Test
    @DisplayName("Deve falhar no cadastro de usuário quando endereço é vazio")
    void testCadastroUsuarioComEnderecoVazio() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "", "USUARIO");

        // Act
        ResponseEntity<String> response = authController.cadastroUsuario(usuario);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Endereço"));
    }

    @Test
    @DisplayName("Deve falhar no cadastro de usuário quando email já existe")
    void testCadastroUsuarioComEmailJaExistente() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "Avenida Brasil, 456", "USUARIO");

        when(usuarioService.cadastrar(any(Usuario.class))).thenReturn(false);

        // Act
        ResponseEntity<String> response = authController.cadastroUsuario(usuario);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Erro"));
    }

    // ============ TESTES DE CADASTRO DE RESTAURANTE ============

    @Test
    @DisplayName("Deve falhar no cadastro de restaurante quando email é vazio")
    void testCadastroRestauranteComEmailVazio() {
        // Arrange
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail("");
        restaurante.setSenha("Senha@123");

        // Act
        ResponseEntity<String> response = authController.cadastroRestaurante(restaurante);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Email"));
    }

    @Test
    @DisplayName("Deve falhar no cadastro de restaurante quando senha é vazia")
    void testCadastroRestauranteComSenhaVazia() {
        // Arrange
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail("restaurante@example.com");
        restaurante.setSenha("");

        // Act
        ResponseEntity<String> response = authController.cadastroRestaurante(restaurante);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Senha"));
    }

    @Test
    @DisplayName("Deve falhar no cadastro de restaurante quando email já existe")
    void testCadastroRestauranteComEmailJaExistente() {
        // Arrange
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail("restaurante@example.com");
        restaurante.setSenha("Senha@123");

        Restaurante restauranteExistente = new Restaurante();
        restauranteExistente.setEmail("restaurante@example.com");

        when(restauranteAuthService.buscarPorEmail("restaurante@example.com"))
            .thenReturn(Optional.of(restauranteExistente));

        // Act
        ResponseEntity<String> response = authController.cadastroRestaurante(restaurante);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Email já cadastrado"));
    }
}
