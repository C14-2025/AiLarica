package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PratoController {

    private final PratoService pratoService;
    private final JwtTokenProvider jwtTokenProvider;

    public PratoController(PratoService pratoService, JwtTokenProvider jwtTokenProvider) {
        this.pratoService = pratoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // --- ÁREA PÚBLICA ---

    @GetMapping("/restaurantes/{id}/pratos")
    public List<Prato> listarPratos(@PathVariable int id) {
        return pratoService.listarPratosDoRestaurante(id);
    }

    @GetMapping("/pratos/{idPrato}")
    public ResponseEntity<Prato> buscarPrato(@PathVariable int idPrato) {
        return pratoService.buscarPratoPorId(idPrato)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- ÁREA RESTRITA (Cozinha) ---

    private Integer validarRestaurante(String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) return null;
        if (!"RESTAURANTE".equals(jwtTokenProvider.getTipoFromToken(token))) return null;
        return jwtTokenProvider.getIdFromToken(token);
    }

    @PostMapping("/painel-restaurante/pratos")
    public ResponseEntity<?> criarPrato(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody Prato novoPrato) {
        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        Prato pratoCriado = pratoService.criarPrato(idRestaurante, novoPrato);
        if (pratoCriado != null) return ResponseEntity.status(HttpStatus.CREATED).body(pratoCriado);

        return ResponseEntity.badRequest().body("Erro ao criar prato.");
    }

    // ATUALIZAR (Agora passa o ID do token para validar)
    @PutMapping("/painel-restaurante/pratos/{idPrato}")
    public ResponseEntity<?> atualizarPrato(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable int idPrato,
                                            @RequestBody Prato pratoAtualizado) {

        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        // Passamos o idRestaurante para o Service fazer a checagem de dono
        return pratoService.atualizarPrato(idPrato, idRestaurante, pratoAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build()); // Retorna 403 se não for o dono
    }

    // DELETAR (Agora passa o ID do token para validar)
    @DeleteMapping("/painel-restaurante/pratos/{idPrato}")
    public ResponseEntity<?> deletarPrato(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable int idPrato) {

        Integer idRestaurante = validarRestaurante(authHeader);
        if (idRestaurante == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");

        // Passamos o idRestaurante para validar
        if (pratoService.deletarPrato(idPrato, idRestaurante)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden (Não é seu prato ou não existe)
    }
}