package br.inatel.ailarica;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioServiceTest {

    @Test //Vitor
    public void testarCadastroTrue(){
        UsuarioService service = new UsuarioService();
        service.carregarUsuarios().clear(); //garantir que a lista ta vazia
        
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

    @Test
    @DisplayName("Cadastrar novo usuário deve funcionar (mocked)")
    void testCadastrarNovoUsuarioComMock() {

        List<Usuario> listaMock = new ArrayList<>();

        UsuarioService service = new UsuarioService() {
            @Override
            public List<Usuario> carregarUsuarios() {
                return listaMock; //retorna o mock
            }

            @Override
            public void atualizarUsuarios(List<Usuario> usuarios) {
                //faz nada, pra n mexer no arquivo real
            }
        };

        boolean sucesso = service.cadastrar("Novo User", "novo@example.com", "senhaNova");

        assertTrue(sucesso, "O cadastro deve ser bem-sucedido");
    }
}