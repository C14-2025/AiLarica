package br.inatel.ailarica;

import br.inatel.ailarica.Cliente.Usuario;
import br.inatel.ailarica.Cliente.UsuarioService;
import br.inatel.ailarica.Cliente.UsuarioDAO;
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
 * Testes unitários para UsuarioService.
 * Testa funcionalidades de login, cadastro e validações.
 */
@DisplayName("Testes do Serviço de Usuário")
class UsuarioServiceTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioDAO);
    }

    // ============ TESTES DE LOGIN DE USUÁRIO ============

    @Test
    @DisplayName("Deve fazer login com sucesso quando endereço é válido e usuário confirmado")
    void testLoginUsuarioComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senha, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(true);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("João Silva", resultado.getNome());
        assertEquals(endereco, resultado.getEndereco());
        verify(usuarioDAO, times(1)).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando endereço é vazio")
    void testLoginUsuarioComEnderecoVazio() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String endereco = "";

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNull(resultado);
        verify(usuarioDAO, never()).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando endereço é nulo")
    void testLoginUsuarioComEnderecoNulo() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, null);

        // Assert
        assertNull(resultado);
        verify(usuarioDAO, never()).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando endereço é muito curto")
    void testLoginUsuarioComEnderecoMuitoCurto() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String endereco = "Rua"; // Menos de 5 caracteres

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNull(resultado);
        verify(usuarioDAO, never()).buscarPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar no login quando usuário não está confirmado")
    void testLoginUsuarioNaoConfirmado() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senha, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(false); // Não confirmado

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando senha está incorreta")
    void testLoginUsuarioComSenhaIncorreta() {
        // Arrange
        String email = "usuario@example.com";
        String senhaCorreta = "Senha@123";
        String senhaIncorreta = "SenhaErrada@123";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senhaCorreta, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(true);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senhaIncorreta, endereco);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando usuário não existe")
    void testLoginUsuarioNaoExiste() {
        // Arrange
        String email = "inexistente@example.com";
        String senha = "Senha@123";
        String endereco = "Rua Principal, 123";

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNull(resultado);
    }

    // ============ TESTES DE CADASTRO ============

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void testCadastroUsuarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "Avenida Brasil, 456", "USUARIO");

        when(usuarioDAO.buscarPorEmail("maria@example.com")).thenReturn(Optional.empty());
        doNothing().when(usuarioDAO).criar(any(Usuario.class));

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertTrue(resultado);
        verify(usuarioDAO, times(1)).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando email já existe")
    void testCadastroUsuarioEmailJaExiste() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "Avenida Brasil, 456", "USUARIO");
        Usuario usuarioExistente = new Usuario("Outro Usuário", "maria@example.com", "OutraSenha@123", "Outro Endereço", "USUARIO");

        when(usuarioDAO.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(usuarioExistente));

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando email é inválido")
    void testCadastroUsuarioEmailInvalido() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "email_invalido", "Senha@123", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando senha é muito curta")
    void testCadastroUsuarioSenhaMuitoCurta() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Abc@1", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando senha não tem número")
    void testCadastroUsuarioSenhaSemNumero() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "SenhaAbcd@", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando senha não tem caractere especial")
    void testCadastroUsuarioSenhaSemCaractereEspecial() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "SenhaAbcd123", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    // ============ TESTES DE ATUALIZAÇÃO DE SENHA ============

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void testAtualizarSenhaComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String novaSenha = "NovaSenha@456";

        Usuario usuario = new Usuario("João Silva", email, senhaAntiga, "Rua Principal, 123", "USUARIO");
        usuario.setId(1);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioDAO).atualizar(any(Usuario.class));

        // Act
        boolean resultado = usuarioService.atualizarSenha(email, senhaAntiga, novaSenha);

        // Assert
        assertTrue(resultado);
        verify(usuarioDAO, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar na atualização quando senha antiga está incorreta")
    void testAtualizarSenhaComSenhaAntigaIncorreta() {
        // Arrange
        String email = "usuario@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String senhaAntigaIncorreta = "SenhaErrada@123";
        String novaSenha = "NovaSenha@456";

        Usuario usuario = new Usuario("João Silva", email, senhaAntiga, "Rua Principal, 123", "USUARIO");
        usuario.setId(1);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        boolean resultado = usuarioService.atualizarSenha(email, senhaAntigaIncorreta, novaSenha);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).atualizar(any(Usuario.class));
    }

    // ============ TESTES DE CONFIRMAÇÃO DE EMAIL ============

    @Test
    @DisplayName("Deve confirmar email com sucesso")
    void testConfirmarEmailComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        Usuario usuario = new Usuario("João Silva", email, "Senha@123", "Rua Principal, 123", "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(false);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioDAO).atualizar(any(Usuario.class));

        // Act
        boolean resultado = usuarioService.confirmarEmail(email);

        // Assert
        assertTrue(resultado);
        assertTrue(usuario.isConfirmado());
        verify(usuarioDAO, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar na confirmação quando email não existe")
    void testConfirmarEmailNaoExiste() {
        // Arrange
        String email = "inexistente@example.com";

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean resultado = usuarioService.confirmarEmail(email);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).atualizar(any(Usuario.class));
    }
}
