package br.inatel.ailarica.Restaurantes; // (ou seu pacote)

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Endpoint para a Funcionalidade 4: Restaurante vê seus pedidos ATIVOS
     * (O "Dogão do Jairo" vai chamar este endpoint para mostrar os "3 Pedidos")
     */
    @GetMapping("/restaurantes/{id}/pedidos-ativos")
    public List<Pedido> getPedidosAtivosRestaurante(@PathVariable int id) {
        return pedidoService.listarPedidosAtivosDoRestaurante(id);
    }

    /**
     * Endpoint para a Funcionalidade 5: Restaurante atualiza o status do pedido
     * (O "Dogão do Jairo" chama isso ao clicar em "saiu para entrega")
     */
    @PutMapping("/pedidos/{id}/status")
    public ResponseEntity<String> atualizarStatusPedido(
            @PathVariable int id,
            @RequestBody StatusUpdateRequest request) {

        boolean sucesso = pedidoService.atualizarStatus(id, request.getNovoStatus());
        if (sucesso) {
            return ResponseEntity.ok("Status do pedido atualizado para " + request.getNovoStatus());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para o CLIENTE criar um novo pedido
     */
    @PostMapping("/pedidos")
    public Pedido criarPedido(@RequestBody Pedido novoPedido) {
        // O novoPedido vem com idUsuario, idRestaurante e a List<ItemPedido>
        return pedidoService.criarPedido(novoPedido);
    }

    // --- Classe auxiliar para o corpo do JSON de atualização de status ---
    private static class StatusUpdateRequest {
        private String novoStatus;
        public String getNovoStatus() { return novoStatus; }
    }
}