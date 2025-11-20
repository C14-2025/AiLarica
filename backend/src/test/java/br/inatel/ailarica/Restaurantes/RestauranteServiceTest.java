package br.inatel.ailarica.Restaurantes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {

    @Mock
    private RestauranteDAO restauranteDAO;

    @InjectMocks
    private RestauranteService restauranteService;

    @Test
    void testCriarRestaurante() {
        // Cenário
        Restaurante novo = new Restaurante();
        novo.setNome("Teste Burger");

        // Mock: Quando o DAO criar, ele retorna o objeto (simulando o comportamento ajustado)
        when(restauranteDAO.criar(any(Restaurante.class))).thenReturn(novo);

        // Execução
        Restaurante criado = restauranteService.criar(novo);

        // Verificação
        assertNotNull(criado);
        assertEquals("Teste Burger", criado.getNome());
        verify(restauranteDAO, times(1)).criar(novo);
    }

    @Test
    void testListarTodos() {
        // Cenário
        List<Restaurante> listaMock = Arrays.asList(new Restaurante(), new Restaurante());
        when(restauranteDAO.listarTodos()).thenReturn(listaMock);

        // Execução
        List<Restaurante> resultado = restauranteService.listarTodos();

        // Verificação
        assertEquals(2, resultado.size());
        verify(restauranteDAO, times(1)).listarTodos();
    }

    @Test
    void testAlternarStatus_DeveMudarDeAtivoParaInativo() {
        // Cenário: Restaurante existe e está ATIVO (true)
        Restaurante r = new Restaurante();
        r.setIdRestaurante(1);
        r.setAtivo(true);

        when(restauranteDAO.buscarPorId(1)).thenReturn(r);
        when(restauranteDAO.atualizarStatus(1, false)).thenReturn(true);

        // Execução
        boolean sucesso = restauranteService.alternar(1);

        // Verificação
        assertTrue(sucesso);
        // Verifica se o DAO foi chamado com o inverso (false)
        verify(restauranteDAO).atualizarStatus(1, false);
    }

    @Test
    void testAlternarStatus_RestauranteNaoEncontrado() {
        // Cenário: Restaurante não existe
        when(restauranteDAO.buscarPorId(99)).thenReturn(null);

        // Execução
        boolean sucesso = restauranteService.alternar(99);

        // Verificação
        assertFalse(sucesso);
        // Garante que NUNCA tentou atualizar nada
        verify(restauranteDAO, never()).atualizarStatus(anyInt(), anyBoolean());
    }
}