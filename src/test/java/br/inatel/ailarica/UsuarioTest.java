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