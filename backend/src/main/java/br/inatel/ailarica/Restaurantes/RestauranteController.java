package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteService service;
    private final JwtTokenProvider jwtTokenProvider; // Injeção de segurança

    public RestauranteController(RestauranteService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // =================================================
    // 🔓 ÁREA PÚBLICA (Qualquer um acessa)
    // =================================================

    /**
     * Listar todos os restaurantes.
     * Público: O cliente precisa ver a lista para escolher onde comer.
     */
    @GetMapping
    public List<Restaurante> listarTodos() {
        return service.listarTodos();
    }

    /**
     * Buscar restaurante por ID (Ver cardápio/detalhes).
     * Público.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // OBS: O método POST (Criar) foi removido daqui pois o cadastro
    // agora é feito exclusivamente pelo AuthController.

    // =================================================
    // 🔒 ÁREA RESTRITA (Só o Dono acessa)
    // =================================================

    /**
     * Método auxiliar para validar se quem chama é um RESTAURANTE.
     */
    private Integer validarDonoRestaurante(String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) return null;

        String tipo = jwtTokenProvider.getTipoFromToken(token);
        if (!"RESTAURANTE".equals(tipo)) return null; // Bloqueia usuários comuns

        return jwtTokenProvider.getIdFromToken(token);
    }

    /**
     * Atualizar MEUS dados.
     * Mudança: Rota é /me, o ID vem do token.
     */
    @PutMapping("/me")
    public ResponseEntity<?> atualizarMeusDados(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody Restaurante atualizado) {
        Integer idRestaurante = validarDonoRestaurante(authHeader);
        if (idRestaurante == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        return service.atualizar(idRestaurante, atualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletar MINHA conta.
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deletarMinhaConta(@RequestHeader("Authorization") String authHeader) {
        Integer idRestaurante = validarDonoRestaurante(authHeader);
        if (idRestaurante == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        if (service.deletar(idRestaurante)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Abrir/Fechar loja (Alternar status).
     * Simplifiquei: Um único endpoint que inverte o status atual.
     */
    @PutMapping("/me/funcionamento")
    public ResponseEntity<String> alternarFuncionamento(@RequestHeader("Authorization") String authHeader) {
        Integer idRestaurante = validarDonoRestaurante(authHeader);
        if (idRestaurante == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        boolean alterou = service.alternar(idRestaurante);
        if (alterou) {
            // Buscamos o estado atual para dar uma mensagem bonitinha
            boolean estaAberto = service.buscarPorId(idRestaurante).map(Restaurante::isAtivo).orElse(false);
            String statusTexto = estaAberto ? "ABERTO" : "FECHADO";
            return ResponseEntity.ok("Restaurante agora está: " + statusTexto);
        }
        return ResponseEntity.status(404).body("Erro ao alterar status.");
    }
}