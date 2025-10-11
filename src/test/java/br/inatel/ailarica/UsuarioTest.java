package br.inatel.ailarica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test //João Pedro Escobar
    void testToString() {
        Usuario usuario = new Usuario("Teste Nome", "teste@email.com", "senha123");
        assertEquals("Teste Nome;teste@email.com;senha123", usuario.toString());
    }

    @Test //João Pedro Escobar
    void testFromString() {
        String linha = "Teste Nome;teste@email.com;senha123";
        Usuario usuario = Usuario.fromString(linha);
        assertNotNull(usuario);
        assertEquals("Teste Nome", usuario.getNome());
        assertEquals("teste@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
    }

    @Test //João Pedro Escobar
    void testFromStringComDadosInvalidos() {
        String linha = "Teste Nome;teste@email.com";
        Usuario usuario = Usuario.fromString(linha);
        assertNull(usuario);
    }
}
@Test // João Pedro Escobar
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
