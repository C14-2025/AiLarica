package br.inatel.ailarica.Pedidos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestaurantePedidoController {

    private final PedidoService pedidoService;

    public RestaurantePedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Endpoint para a Funcionalidade 4: Restaurante vê seus pedidos ATIVOS
     * (O "Dogão do Jairo" vai chamar este endpoint para mostrar os "3 Pedidos")
     */
    @GetMapping("/{id}/pedidos-ativos")
    public List<Pedido> getPedidosAtivosRestaurante(@PathVariable int id) {
        return pedidoService.listarPedidosAtivosDoRestaurante(id);
    }

    /**
     * Endpoint para o RESTAURANTE ver todos os seus pedidos
     */
    @GetMapping("/{id}/pedidos")
    public List<Pedido> getPedidosRestaurante(@PathVariable int id) {
        return pedidoService.listarPedidosDoRestaurante(id);
    }

    /**
     * Endpoint para a Funcionalidade 5: Restaurante atualiza o status do pedido
     * (O "Dogão do Jairo" chama isso ao clicar em "saiu para entrega")
     */
    @PutMapping("/{idRestaurante}/pedidos/{idPedido}/status")
    public ResponseEntity<String> atualizarStatusPedido(
            @PathVariable int idRestaurante,
            @PathVariable int idPedido,
            @RequestBody StatusUpdateRequest request) {

        boolean sucesso = pedidoService.atualizarStatus(idPedido, request.getNovoStatus());
        if (sucesso) {
            return ResponseEntity.ok("Status do pedido atualizado para " + request.getNovoStatus());
        }
        return ResponseEntity.notFound().build();
    }

    // --- Classe auxiliar para o corpo do JSON de atualização de status ---
    private static class StatusUpdateRequest {
        private String novoStatus;
        public String getNovoStatus() { return novoStatus; }
    }
}
