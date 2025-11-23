package br.inatel.ailarica;

import br.inatel.ailarica.Cliente.UsuarioService;
import br.inatel.ailarica.Cliente.Usuario;
import br.inatel.ailarica.Cliente.UsuarioDAO;
import br.inatel.ailarica.security.PasswordEncoder;
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
 * Testa funcionalidades de login, cadastro e validações com criptografia de senhas.
 */
@DisplayName("Testes do Serviço de Usuário")
class UsuarioServiceTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioDAO, passwordEncoder);
    }

    // ============ TESTES DE LOGIN DE USUÁRIO ============

    @Test
    @DisplayName("Deve fazer login com sucesso quando endereço é válido e usuário confirmado")
    void testLoginUsuarioComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String senhaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senhaHash, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(true);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, senhaHash)).thenReturn(true);

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("João Silva", resultado.getNome());
        assertEquals(endereco, resultado.getEndereco());
        verify(passwordEncoder, times(1)).matches(senha, senhaHash);
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
    }

    @Test
    @DisplayName("Deve falhar no login quando endereço é muito curto")
    void testLoginUsuarioComEnderecoMuitoCurto() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String endereco = "Rua";

        // Act
        Usuario resultado = usuarioService.loginUsuario(email, senha, endereco);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve falhar no login quando usuário não está confirmado")
    void testLoginUsuarioNaoConfirmado() {
        // Arrange
        String email = "usuario@example.com";
        String senha = "Senha@123";
        String senhaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senhaHash, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(false);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, senhaHash)).thenReturn(true);

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
        String senhaIncorreta = "SenhaErrada@123";
        String senhaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";
        String endereco = "Rua Principal, 123";

        Usuario usuario = new Usuario("João Silva", email, senhaHash, endereco, "USUARIO");
        usuario.setId(1);
        usuario.setConfirmado(true);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaIncorreta, senhaHash)).thenReturn(false);

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
        String senhaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";

        when(usuarioDAO.buscarPorEmail("maria@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Senha@123")).thenReturn(senhaHash);
        doNothing().when(usuarioDAO).criar(any(Usuario.class));

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertTrue(resultado);
        verify(passwordEncoder, times(1)).encode("Senha@123");
        verify(usuarioDAO, times(1)).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando email já existe")
    void testCadastroUsuarioComEmailDuplicado() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@123", "Avenida Brasil, 456", "USUARIO");
        Usuario usuarioExistente = new Usuario("João Silva", "maria@example.com", "Senha@123", "Rua Principal, 123", "USUARIO");

        when(usuarioDAO.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(usuarioExistente));

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
        verify(usuarioDAO, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando email é inválido")
    void testCadastroUsuarioComEmailInvalido() {
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
    void testCadastroUsuarioComSenhaMuitoCurta() {
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
    void testCadastroUsuarioComSenhaSemNumero() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha@Abc", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando senha não tem caractere especial")
    void testCadastroUsuarioComSenhaSemEspecial() {
        // Arrange
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "Senha123Abc", "Avenida Brasil, 456", "USUARIO");

        // Act
        boolean resultado = usuarioService.cadastrar(usuario);

        // Assert
        assertFalse(resultado);
    }

    // ============ TESTES DE ATUALIZAÇÃO DE SENHA ============

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void testAtualizarSenhaComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        String senhaAntiga = "SenhaAntiga@123";
        String novaSenha = "NovaSenha@456";
        String senhaAntigaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";
        String novaSenhaHash = "$2a$12$zyxwvutsrqponmlkjihgfedcba";

        Usuario usuario = new Usuario("João Silva", email, senhaAntigaHash, "Rua Principal, 123", "USUARIO");
        usuario.setId(1);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaAntiga, senhaAntigaHash)).thenReturn(true);
        when(passwordEncoder.encode(novaSenha)).thenReturn(novaSenhaHash);
        doNothing().when(usuarioDAO).atualizar(any(Usuario.class));

        // Act
        boolean resultado = usuarioService.atualizarSenha(email, senhaAntiga, novaSenha);

        // Assert
        assertTrue(resultado);
        verify(passwordEncoder, times(1)).matches(senhaAntiga, senhaAntigaHash);
        verify(passwordEncoder, times(1)).encode(novaSenha);
        verify(usuarioDAO, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar na atualização quando senha antiga está incorreta")
    void testAtualizarSenhaComSenhaAntigaIncorreta() {
        // Arrange
        String email = "usuario@example.com";
        String senhaAntigaIncorreta = "SenhaErrada@123";
        String novaSenha = "NovaSenha@456";
        String senhaAntigaHash = "$2a$12$abcdefghijklmnopqrstuvwxyz";

        Usuario usuario = new Usuario("João Silva", email, senhaAntigaHash, "Rua Principal, 123", "USUARIO");
        usuario.setId(1);

        when(usuarioDAO.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaAntigaIncorreta, senhaAntigaHash)).thenReturn(false);

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
        verify(usuarioDAO, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve falhar ao confirmar email de usuário inexistente")
    void testConfirmarEmailUsuarioInexistente() {
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
