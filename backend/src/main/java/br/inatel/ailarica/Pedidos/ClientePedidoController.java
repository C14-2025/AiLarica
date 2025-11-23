package br.inatel.ailarica.Pedidos;

import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios") // Mudei a rota para fazer mais sentido
public class ClientePedidoController {

    private final PedidoService pedidoService;
    private final JwtTokenProvider jwtTokenProvider; // 1. Injetar o porteiro

    public ClientePedidoController(PedidoService pedidoService, JwtTokenProvider jwtTokenProvider) {
        this.pedidoService = pedidoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Método auxiliar para validar token e extrair ID
     * Retorna null se o token for inválido
     */
    private Integer validarEObterId(String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return null; // Token inválido ou ausente
        }

        // Só aceita se for do tipo USUARIO
        String tipo = jwtTokenProvider.getTipoFromToken(token);
        if (!"USUARIO".equals(tipo)) {
            return null; // Restaurante tentando agir como usuário
        }

        return jwtTokenProvider.getIdFromToken(token);
    }

    /**
     * Endpoint para o CLIENTE criar um novo pedido
     * AGORA SEGURO: Pega o ID do usuário direto do token
     */
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Pedido novoPedido) {

        Integer idUsuarioLogado = validarEObterId(authHeader);

        if (idUsuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado: Token inválido.");
        }

        // FORÇA o ID do usuário ser o do token (impede fraude)
        novoPedido.setIdUsuario(idUsuarioLogado);

        Pedido pedidoCriado = pedidoService.criarPedido(novoPedido);
        return ResponseEntity.ok(pedidoCriado);
    }

    /**
     * Endpoint para ver pedidos
     * MUDANÇA: Removemos o {id} da URL. O sistema sabe quem é pelo token.
     */
    @GetMapping
    public ResponseEntity<?> getMeusPedidos(@RequestHeader("Authorization") String authHeader) {

        Integer idUsuarioLogado = validarEObterId(authHeader);

        if (idUsuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        List<Pedido> pedidos = pedidoService.listarPedidosDoUsuario(idUsuarioLogado);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Endpoint para ver pedidos ativos
     */
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