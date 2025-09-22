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

    // Mocks para as dependências que o UsuarioService usa internamente
    // Como FileWriter e FileReader são criados dentro dos métodos, precisamos de uma abordagem diferente para mocká-los.
    // Para este cenário, vamos mockar o comportamento de leitura e escrita de arquivo de forma mais abstrata, se possível,
    // ou refatorar UsuarioService para permitir injeção de dependência de BufferedReader/BufferedWriter.
    // No entanto, para o propósito de um teste rápido com mock, vamos simular o comportamento do arquivo.

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

        // Para mockar a leitura e escrita de arquivo sem refatorar a classe UsuarioService,
        // podemos simular o ambiente de arquivo ou usar PowerMock/ByteBuddy para mockar construtores.
        // No entanto, o Mockito padrão não permite mockar construtores ou métodos estáticos/finais diretamente.
        // Uma alternativa é testar o comportamento do arquivo real, mas isso não é um teste unitário puro com mock.
        // Para seguir a instrução de 'teste com mock', vamos assumir que o método carregarUsuarios()
        // e a escrita no arquivo podem ser controlados de alguma forma, mesmo que a implementação atual
        // do UsuarioService dificulte isso sem refatoração.

        // Simulação de que o arquivo está vazio (nenhum usuário existente)
        // Isso exigiria uma refatoração em UsuarioService para injetar BufferedReader/Writer
        // ou para que carregarUsuarios/cadastrar recebam um caminho de arquivo mockado.
        // Como não podemos refatorar a classe original, vamos criar um teste que interage com um arquivo temporário
        // e limpa ele após cada teste, o que é uma forma de teste de integração/sistema, não unitário puro com mock.
        // No entanto, a requisição é para 'teste com mock', então vamos simular o que seria feito se fosse possível mockar o FileWriter.

        // O teste original tentou mockar BufferedWriter, mas como ele é instanciado dentro do método,
        // o @Mock e @InjectMocks não funcionam para ele diretamente.
        // A melhor abordagem para este cenário seria refatorar UsuarioService para aceitar um 'FileWriterFactory' ou similar.
        // Sem refatoração, o teste com mock para FileWriter/BufferedReader é complexo.

        // Vamos reescrever o teste para focar no comportamento do método 'cadastrar' com base no estado do 'arquivo'.
        // Isso ainda não é um mock puro do FileWriter, mas é o mais próximo que podemos chegar sem refatorar a classe.

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
