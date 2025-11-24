package br.inatel.ailarica.Cliente;

import br.inatel.ailarica.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;

    public UsuarioController(UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody Usuario novoUsuario) {
        if (usuarioService.cadastrar(novoUsuario)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Email já cadastrado ou dados inválidos.");
    }

    @PutMapping("/senha")
    public ResponseEntity<String> atualizarSenha(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody SenhaUpdateRequest updateRequest) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }

        String emailDoToken = jwtTokenProvider.getEmailFromToken(token);

        boolean atualizou = usuarioService.atualizarSenha(
                emailDoToken,
                updateRequest.getSenhaAntiga(),
                updateRequest.getNovaSenha()
        );

        if (atualizou) {
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Senha antiga incorreta ou nova senha inválida.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMeuPerfil(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }

        Integer id = jwtTokenProvider.getIdFromToken(token);
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);

        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            u.setSenha("PROTEGIDA");
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ NOVO ENDPOINT: Atualizar dados cadastrais (Nome, Endereço)
    @PutMapping("/me")
    public ResponseEntity<String> atualizarMeusDados(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody Usuario dadosAtualizados) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }

        Integer id = jwtTokenProvider.getIdFromToken(token);

        // Chama o novo método do Service
        if (usuarioService.atualizarDados(id, dadosAtualizados)) {
            return ResponseEntity.ok("Dados atualizados com sucesso!");
        }

        return ResponseEntity.badRequest().body("Erro ao atualizar dados.");
    }

    public static class SenhaUpdateRequest {
        private String senhaAntiga;
        private String novaSenha;
        public String getSenhaAntiga() { return senhaAntiga; }
        public void setSenhaAntiga(String senhaAntiga) { this.senhaAntiga = senhaAntiga; }
        public String getNovaSenha() { return novaSenha; }
        public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
    }
}