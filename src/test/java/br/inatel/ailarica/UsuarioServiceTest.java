package br.inatel.ailarica;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioServiceTest {

    @Test //Vitor
    public void testarCadastroTrue(){
        UsuarioService service = new UsuarioService();
        
        //rodar esse teste antes do seguinte
        
        String nome = "Teste";
        String email = "teste@gmail.com";
        String senha = "123456";

        assertTrue(service.cadastrar(nome, email, senha));
    }

    @Test //Vitor
    public void testarCadastroFalse(){
        UsuarioService service = new UsuarioService();

        //assumindo que esse usuario ja foi cadastrado no teste passado

        String nome = "Teste";
        String email = "teste@gmail.com";
        String senha = "123456";

        assertTrue(service.cadastrar(nome, email, senha));
    }

    @Test //Vitor
    public void carregarUsuarioFalse(){
        UsuarioService service = new UsuarioService();
        List<Usuario> lista = service.carregarUsuarios();
        assertFalse(lista.isEmpty());
    }
}