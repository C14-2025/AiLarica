package br.inatel.ailarica.Pedidos;

import br.inatel.ailarica.Restaurantes.DashboardDTO;
import br.inatel.ailarica.Restaurantes.RestauranteService; // <--- Import Novo
import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/painel-restaurante")
public class RestaurantePedidoController {

    private final PedidoService pedidoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestauranteService restauranteService; // <--- 1. Nova Dependência

    // 2. Construtor Atualizado (Recebendo o RestauranteService)
    public RestaurantePedidoController(PedidoService pedidoService,
                                       JwtTokenProvider jwtTokenProvider,
                                       RestauranteService restauranteService) {
        this.pedidoService = pedidoService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restauranteService = restauranteService;
    }

    private Integer validarRestaurante(String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) return null;
        if (!"RESTAURANTE".equals(jwtTokenProvider.getTipoFromToken(token))) return null;
        return jwtTokenProvider.getIdFromToken(token);
    }

    @GetMapping("/pedidos-ativos")
    public ResponseEntity<?> getMeusPedidosAtivos(@RequestHeader("Authorization") String authHeader) {
        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        List<Pedido> pedidos = pedidoService.listarPedidosAtivosDoRestaurante(idRestaurante);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/historico")
    public ResponseEntity<?> getMeuHistorico(@RequestHeader("Authorization") String authHeader) {
        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        List<Pedido> pedidos = pedidoService.listarPedidosDoRestaurante(idRestaurante);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/pedidos/{idPedido}/status")
    public ResponseEntity<String> atualizarStatusPedido(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int idPedido,
            @RequestBody StatusUpdateRequest request) {

        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        boolean sucesso = pedidoService.atualizarStatus(idPedido, request.getNovoStatus());
        if (sucesso) {
            return ResponseEntity.ok("Status do pedido atualizado para " + request.getNovoStatus());
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Endpoint do Dashboard (Agora funciona!)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@RequestHeader("Authorization") String authHeader) {
        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        DashboardDTO dashboard = restauranteService.getDashboardDados(idRestaurante);

        return ResponseEntity.ok(dashboard);
    }

    private static class StatusUpdateRequest {
        private String novoStatus;
        public String getNovoStatus() { return novoStatus; }
        public void setNovoStatus(String novoStatus) { this.novoStatus = novoStatus; }
    }
}