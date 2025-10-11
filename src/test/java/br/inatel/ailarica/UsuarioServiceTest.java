package br.inatel.ailarica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuarioService.ARQUIVO))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void CadastrarNovoUsuarioComSucesso() throws IOException {
        Usuario novoUsuario = new Usuario("Teste", "teste@email.com", "Senha123!");

        boolean resultado = usuarioService.cadastrar(novoUsuario);

        assertTrue(resultado, "O cadastro de um novo usuário deveria retornar true.");

        // Verifica se o usuário foi realmente adicionado ao arquivo
        List<Usuario> usuariosCarregados = usuarioService.carregarUsuarios();
        assertTrue(
                usuariosCarregados.stream().anyMatch(u -> u.getEmail().equals(novoUsuario.getEmail())),
                "O usuário cadastrado deveria estar na lista de usuários carregados."
        );
    }

    @Test
    void CadastrarUsuarioExistenteRetornaFalse() throws IOException {
        Usuario usuarioExistente = new Usuario("Existente", "existente@email.com", "Senha123!");

        usuarioService.cadastrar(usuarioExistente);
        boolean resultado = usuarioService.cadastrar(usuarioExistente);

        assertFalse(resultado, "O cadastro de um usuário existente deveria retornar false.");

        List<Usuario> usuariosCarregados = usuarioService.carregarUsuarios();
        long count = usuariosCarregados.stream()
                .filter(u -> u.getEmail().equals(usuarioExistente.getEmail()))
                .count();
        assertEquals(1, count, "O usuário existente não deveria ser duplicado.");
    }

    @Test
    void testLoginComUsuarioConfirmadoComSucesso() throws IOException {
        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuarioConfirmado = new Usuario("João", "joao@email.com", "Senha123!");
        usuarioConfirmado.confirmar();
        usuariosMock.add(usuarioConfirmado);

        UsuarioService spyUsuarioService = spy(usuarioService);
        doReturn(usuariosMock).when(spyUsuarioService).carregarUsuarios();

        Usuario resultado = spyUsuarioService.login("joao@email.com", "Senha123!");

        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertTrue(resultado.isConfirmado());
    }

    @Test
    void testLoginComCredenciaisInvalidas() throws IOException {
        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario = new Usuario("Pedro", "pedro@email.com", "Senha123!");
        usuario.confirmar();
        usuariosMock.add(usuario);

        UsuarioService spyUsuarioService = spy(usuarioService);
        doReturn(usuariosMock).when(spyUsuarioService).carregarUsuarios();

        Usuario resultado = spyUsuarioService.login("pedro@email.com", "SenhaErrada1!");

        assertNull(resultado, "Login deve retornar null para credenciais inválidas");
    }

    @Test
    void testCadastroComEmailInvalido() {
        Usuario usuario = new Usuario("Teste", "emailinvalido", "Senha123!");
        assertFalse(usuarioService.cadastrar(usuario));
    }

    @Test
    void testCadastroComSenhaInvalida() {
        // Falta caractere especial e número
        Usuario usuario = new Usuario("Teste", "teste@email.com", "senha");
        assertFalse(usuarioService.cadastrar(usuario));
    }
}
