package br.inatel.ailarica.Pedidos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoDAO pedidoDAO;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void testListarPedidosAtivos_DeveFiltrarConcluidos() {
        // Cenário:
        // Pedido 1: PENDENTE (Deve aparecer)
        Pedido p1 = new Pedido();
        p1.setIdPedido(1);
        p1.setStatus("PENDENTE");

        // Pedido 2: ENTREGUE (Não deve aparecer)
        Pedido p2 = new Pedido();
        p2.setIdPedido(2);
        p2.setStatus("ENTREGUE");

        // Pedido 3: A_CAMINHO (Deve aparecer)
        Pedido p3 = new Pedido();
        p3.setIdPedido(3);
        p3.setStatus("A_CAMINHO");

        // Pedido 4: CANCELADO (Não deve aparecer)
        Pedido p4 = new Pedido();
        p4.setIdPedido(4);
        p4.setStatus("CANCELADO");

        // Mock: O DAO retorna TUDO (o "banco" tem todos os históricos)
        when(pedidoDAO.listarPorRestaurante(1)).thenReturn(Arrays.asList(p1, p2, p3, p4));

        // Execução
        List<Pedido> ativos = pedidoService.listarPedidosAtivosDoRestaurante(1);

        // Verificação
        assertEquals(2, ativos.size(), "Deveria retornar apenas 2 pedidos ativos");
        assertEquals("PENDENTE", ativos.get(0).getStatus());
        assertEquals("A_CAMINHO", ativos.get(1).getStatus());
    }
}