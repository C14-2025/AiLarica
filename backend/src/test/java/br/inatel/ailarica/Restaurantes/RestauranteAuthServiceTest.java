package br.inatel.ailarica.Restaurantes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para RestauranteAuthService.
 * Testa funcionalidades de autenticação de restaurante.
 */
@DisplayName("Testes do Serviço de Autenticação de Restaurante")
class RestauranteAuthServiceTest {

    @Mock
    private RestauranteDAO restauranteDAO;

    private RestauranteAuthService restauranteAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restauranteAuthService = new RestauranteAuthService(restauranteDAO);
    }

    // ============ TESTES DE LOGIN DE RESTAURANTE ============

    @Test
    @DisplayName("Deve fazer login de restaurante com sucesso")
    void testLoginRestauranteComSucesso() {
        // Arrange
        String email = "restaurante@example.com";
        String senha = "Senha@123";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senha);
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("Restaurante Delícia", resultado.getNome());
        assertTrue(resultado.isAtivo());
        verify(restauranteDAO, times(1)).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando restaurante está inativo")
    void testLoginRestauranteInativo() {
        // Arrange
        String email = "restaurante@example.com";
        String senha = "Senha@123";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senha);
        restaurante.setAtivo(false); // Inativo

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando senha está incorreta")
    void testLoginRestauranteComSenhaIncorreta() {
        // Arrange
        String email = "restaurante@example.com";
        String senhaCorreta = "Senha@123";
        String senhaIncorreta = "SenhaErrada@123";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senhaCorreta);
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senhaIncorreta);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando restaurante não existe")
    void testLoginRestauranteNaoExiste() {
        // Arrange
        String email = "inexistente@example.com";
        String senha = "Senha@123";

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando email é inválido")
    void testLoginRestauranteComEmailInvalido() {
        // Arrange
        String email = "email_invalido";
        String senha = "Senha@123";

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNull(resultado);
        verify(restauranteDAO, never()).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando email é nulo")
    void testLoginRestauranteComEmailNulo() {
        // Arrange
        String email = null;
        String senha = "Senha@123";

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNull(resultado);
        verify(restauranteDAO, never()).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando restaurante não tem senha definida")
    void testLoginRestauranteSemSenha() {
        // Arrange
        String email = "restaurante@example.com";
        String senha = "Senha@123";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(null); // Sem senha
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        Restaurante resultado = restauranteAuthService.loginRestaurante(email, senha);

        // Assert
        assertNull(resultado);
    }

    // ============ TESTES DE ATUALIZAÇÃO DE SENHA ============

    @Test
    @DisplayName("Deve atualizar senha de restaurante com sucesso")
    void testAtualizarSenhaRestauranteComSucesso() {
        // Arrange
        String email = "restaurante@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String novaSenha = "NovaSenha@456";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senhaAntiga);
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));
        doNothing().when(restauranteDAO).atualizar(any(Restaurante.class));

        // Act
        boolean resultado = restauranteAuthService.atualizarSenha(email, senhaAntiga, novaSenha);

        // Assert
        assertTrue(resultado);
        verify(restauranteDAO, times(1)).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve falhar na atualização quando senha antiga está incorreta")
    void testAtualizarSenhaRestauranteComSenhaAntigaIncorreta() {
        // Arrange
        String email = "restaurante@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String senhaAntigaIncorreta = "SenhaErrada@123";
        String novaSenha = "NovaSenha@456";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senhaAntiga);
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        boolean resultado = restauranteAuthService.atualizarSenha(email, senhaAntigaIncorreta, novaSenha);

        // Assert
        assertFalse(resultado);
        verify(restauranteDAO, never()).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve falhar na atualização quando nova senha é inválida")
    void testAtualizarSenhaRestauranteComNovaSenhaInvalida() {
        // Arrange
        String email = "restaurante@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String novaSenhaInvalida = "Abc"; // Muito curta

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setNome("Restaurante Delícia");
        restaurante.setEmail(email);
        restaurante.setSenha(senhaAntiga);
        restaurante.setAtivo(true);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        boolean resultado = restauranteAuthService.atualizarSenha(email, senhaAntiga, novaSenhaInvalida);

        // Assert
        assertFalse(resultado);
        verify(restauranteDAO, never()).atualizar(any(Restaurante.class));
    }

    // ============ TESTES DE BUSCA ============

    @Test
    @DisplayName("Deve buscar restaurante por email com sucesso")
    void testBuscarRestaurantePorEmailComSucesso() {
        // Arrange
        String email = "restaurante@example.com";
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1);
        restaurante.setEmail(email);

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteAuthService.buscarPorEmail(email);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(email, resultado.get().getEmail());
    }

    @Test
    @DisplayName("Deve retornar vazio quando restaurante não existe por email")
    void testBuscarRestaurantePorEmailNaoExiste() {
        // Arrange
        String email = "inexistente@example.com";

        when(restauranteDAO.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteAuthService.buscarPorEmail(email);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void testBuscarRestaurantePorIdComSucesso() {
        // Arrange
        int id = 1;
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(id);
        restaurante.setNome("Restaurante Delícia");

        when(restauranteDAO.buscarPorIdOptional(id)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteAuthService.buscarPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdRestaurante());
    }

    @Test
    @DisplayName("Deve retornar vazio quando restaurante não existe por ID")
    void testBuscarRestaurantePorIdNaoExiste() {
        // Arrange
        int id = 999;

        when(restauranteDAO.buscarPorIdOptional(id)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteAuthService.buscarPorId(id);

        // Assert
        assertFalse(resultado.isPresent());
    }
}
