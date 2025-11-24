package br.inatel.ailarica.Restaurantes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PratoServiceTest {

    @Mock
    private PratoDAO pratoDAO;

    @Mock
    private RestauranteDAO restauranteDAO; // Precisamos mockar este pois o Service usa

    @InjectMocks
    private PratoService pratoService;

    @Test
    void testCriarPrato_RestauranteExiste() {
        // Cenário
        int idRestaurante = 1;
        Prato novoPrato = new Prato();
        novoPrato.setNome("X-Bacon");

        // Mock: O restaurante EXISTE
        when(restauranteDAO.buscarPorId(idRestaurante)).thenReturn(new Restaurante());
        // Mock: O PratoDAO cria e retorna o prato
        when(pratoDAO.criar(novoPrato, idRestaurante)).thenReturn(novoPrato);

        // Execução
        Prato resultado = pratoService.criarPrato(idRestaurante, novoPrato);

        // Verificação
        assertNotNull(resultado);
        verify(pratoDAO).criar(novoPrato, idRestaurante);
    }

    @Test
    void testCriarPrato_RestauranteNaoExiste() {
        // Cenário
        int idRestaurante = 99;
        Prato novoPrato = new Prato();

        // Mock: O restaurante NÃO EXISTE (null)
        when(restauranteDAO.buscarPorId(idRestaurante)).thenReturn(null);

        // Execução
        Prato resultado = pratoService.criarPrato(idRestaurante, novoPrato);

        // Verificação
        assertNull(resultado); // Deve retornar null
        // Importante: Garante que o PratoDAO NUNCA foi chamado
        verify(pratoDAO, never()).criar(any(), anyInt());
    }

    @Test
    void testAtualizarPrato_PratoNaoExiste() {
        // Cenário
        int idRestauranteDono = 1; // <--- NOVO ID SIMULADO
        int idPrato = 50;
        Prato pratoAtualizado = new Prato();

        when(pratoDAO.buscarPorId(idPrato)).thenReturn(null);

        // Execução
        // Passando o novo ID do Restaurante, conforme a nova assinatura do Service
        Optional<Prato> resultado = pratoService.atualizarPrato(idPrato, idRestauranteDono, pratoAtualizado);

        // Verificação
        assertTrue(resultado.isEmpty());
        verify(pratoDAO, never()).atualizar(any());
    }
}