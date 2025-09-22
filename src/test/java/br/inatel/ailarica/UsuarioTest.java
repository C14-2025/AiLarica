package br.inatel.ailarica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Resetar o arquivo de usuários para cada teste para garantir isolamento
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void CadastrarNovoUsuarioComSucesso() throws IOException {
        // Arrange
        Usuario novoUsuario = new Usuario("Teste", "teste@email.com", "senha123");

        // Limpa o arquivo antes do teste para garantir que o usuário não exista
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuarioService.ARQUIVO))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        boolean resultado = usuarioService.cadastrar(novoUsuario);

        // Assert
        assertTrue(resultado, "O cadastro de um novo usuário deveria retornar true.");

        // Verifica se o usuário foi realmente adicionado ao arquivo
        List<Usuario> usuariosCarregados = usuarioService.carregarUsuarios();
        assertTrue(usuariosCarregados.contains(novoUsuario), "O usuário cadastrado deveria estar na lista de usuários carregados.");
    }

    @Test
    void CadastrarUsuarioExistenteRetornaFalse() throws IOException {
        // Arrange
        Usuario usuarioExistente = new Usuario("Existente", "existente@email.com", "senha456");

        // Cadastra o usuário uma vez
        usuarioService.cadastrar(usuarioExistente);

        // Tenta cadastrar o mesmo usuário novamente
        boolean resultado = usuarioService.cadastrar(usuarioExistente);

        // Assert
        assertFalse(resultado, "O cadastro de um usuário existente deveria retornar false.");

        // Verifica se o usuário não foi duplicado no arquivo
        List<Usuario> usuariosCarregados = usuarioService.carregarUsuarios();
        long count = usuariosCarregados.stream().filter(u -> u.getEmail().equals(usuarioExistente.getEmail())).count();
        assertEquals(1, count, "O usuário existente não deveria ser duplicado.");
    }
