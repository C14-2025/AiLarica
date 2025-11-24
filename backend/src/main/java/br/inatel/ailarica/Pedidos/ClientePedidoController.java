package br.inatel.ailarica.Pedidos;

import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ClientePedidoController {

    private final PedidoService pedidoService;
    private final JwtTokenProvider jwtTokenProvider;

    public ClientePedidoController(PedidoService pedidoService, JwtTokenProvider jwtTokenProvider) {
        this.pedidoService = pedidoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Método auxiliar para validar token e extrair ID
     */
    private Integer validarEObterId(String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return null;
        }

        String tipo = jwtTokenProvider.getTipoFromToken(token);
        if (!"USUARIO".equals(tipo)) {
            return null;
        }

        return jwtTokenProvider.getIdFromToken(token);
    }

    /**
     * Endpoint para o CLIENTE criar um novo pedido
     */
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Pedido novoPedido) {

        Integer idUsuarioLogado = validarEObterId(authHeader);

        if (idUsuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado: Token inválido.");
        }

        novoPedido.setIdUsuario(idUsuarioLogado);

        Pedido pedidoCriado = pedidoService.criarPedido(novoPedido);

        // ✅ TRATAMENTO DO RETORNO NULL (Restaurante Fechado)
        if (pedidoCriado == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Não foi possível criar o pedido: O restaurante está fechado ou não existe.");
        }

        return ResponseEntity.ok(pedidoCriado);
    }

    // ... (Demais métodos GET)
    @GetMapping
    public ResponseEntity<?> getMeusPedidos(@RequestHeader("Authorization") String authHeader) {

        Integer idUsuarioLogado = validarEObterId(authHeader);

        if (idUsuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        List<Pedido> pedidos = pedidoService.listarPedidosDoUsuario(idUsuarioLogado);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> getMeusPedidosAtivos(@RequestHeader("Authorization") String authHeader) {

        Integer idUsuarioLogado = validarEObterId(authHeader);

        if (idUsuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        List<Pedido> pedidos = pedidoService.listarPedidosAtivosDoUsuario(idUsuarioLogado);
        return ResponseEntity.ok(pedidos);
    }
}