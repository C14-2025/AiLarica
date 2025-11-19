package br.inatel.ailarica.Pedidos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ClientePedidoController {

    private final PedidoService pedidoService;

    public ClientePedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Endpoint para o CLIENTE criar um novo pedido
     */
    @PostMapping("/pedidos")
    public Pedido criarPedido(@RequestBody Pedido novoPedido) {
        // O novoPedido vem com idUsuario, idRestaurante e a List<ItemPedido>
        return pedidoService.criarPedido(novoPedido);
    }

    /**
     * Endpoint para o CLIENTE ver todos os seus pedidos
     */
    @GetMapping("/{id}/pedidos")
    public List<Pedido> getPedidosUsuario(@PathVariable int id) {
        return pedidoService.listarPedidosDoUsuario(id);
    }

    /**
     * Endpoint para o CLIENTE ver seus pedidos ATIVOS
     */
    @GetMapping("/{id}/pedidos-ativos")
    public List<Pedido> getPedidosAtivosUsuario(@PathVariable int id) {
        return pedidoService.listarPedidosAtivosDoUsuario(id);
    }
}
