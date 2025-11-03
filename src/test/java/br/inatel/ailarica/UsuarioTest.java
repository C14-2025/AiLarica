package br.inatel.ailarica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class UsuarioTest {

    @Test
    void testToString() {
        Usuario usuario = new Usuario("Teste Nome", "teste@email.com", "Senha123!");
        // confirmando que o usuário ainda não foi confirmado e sem endereços
        assertEquals("Teste Nome;teste@email.com;Senha123!;false;", usuario.toString());
    }

    @Test
    void testFromString() {
        String linha = "Teste Nome;teste@email.com;Senha123!;false;";
        Usuario usuario = Usuario.fromString(linha);
        assertNotNull(usuario);
        assertEquals("Teste Nome", usuario.getNome());
        assertEquals("teste@email.com", usuario.getEmail());
        assertEquals("Senha123!", usuario.getSenha());
        assertFalse(usuario.isConfirmado());
        assertTrue(usuario.getEnderecos().isEmpty());
    }

    @Test
    void testFromStringComEnderecos() {
        String linha = "Nome Teste;teste@email.com;Senha123!;true;RuaA123,RuaB456";
        Usuario usuario = Usuario.fromString(linha);
        assertNotNull(usuario);
        assertEquals("Nome Teste", usuario.getNome());
        assertEquals("teste@email.com", usuario.getEmail());
        assertEquals("Senha123!", usuario.getSenha());
        assertTrue(usuario.isConfirmado());
        List<String> enderecos = usuario.getEnderecos();
        assertEquals(2, enderecos.size());
        assertTrue(enderecos.contains("RuaA123"));
        assertTrue(enderecos.contains("RuaB456"));
    }

    @Test
    void testFromStringComDadosInvalidos() {
        String linha = "Teste Nome;teste@email.com;Senha123!";
        Usuario usuario = Usuario.fromString(linha);
        assertNull(usuario, "Deve retornar null se a string não tiver pelo menos 4 partes");
    }

    @Test
    void testAdicionarEGetEnderecos() {
        Usuario usuario = new Usuario("Nome Teste", "email@teste.com", "senha");
        usuario.adicionarEndereco("Rua A, 123");
        usuario.adicionarEndereco("Rua B, 456");

        List<String> enderecos = usuario.getEnderecos();
        assertNotNull(enderecos);
        assertEquals(2, enderecos.size());
        assertTrue(enderecos.contains("Rua A, 123"));
        assertTrue(enderecos.contains("Rua B, 456"));
    }
}
