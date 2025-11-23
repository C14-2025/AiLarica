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
    private final JwtTokenProvider jwtTokenProvider; // Injeção de Segurança

    public UsuarioController(UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Cadastro de Usuário.
     * ACESSO: PÚBLICO (Não precisa de token para criar conta)
     */
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody Usuario novoUsuario) {
        // O service já criptografa a senha e valida
        if (usuarioService.cadastrar(novoUsuario)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Email já cadastrado ou dados inválidos (senha fraca/email inválido).");
    }

    /**
     * Atualizar Senha.
     * ACESSO: RESTRITO (Exige Token)
     * Segurança: O email é extraído do token, impedindo alterar senha de outros.
     */
    @PutMapping("/senha")
    public ResponseEntity<String> atualizarSenha(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody SenhaUpdateRequest updateRequest) {

        // 1. Validar Token
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado.");
        }

        // 2. Extrair o email do dono do token
        String emailDoToken = jwtTokenProvider.getEmailFromToken(token);

        // 3. Tentar atualizar
        boolean atualizou = usuarioService.atualizarSenha(
                emailDoToken, // Usa o email do token (seguro), ignora o do JSON se houver
                updateRequest.getSenhaAntiga(),
                updateRequest.getNovaSenha()
        );

        if (atualizou) {
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Senha antiga incorreta ou nova senha não cumpre os requisitos.");
        }
    }

    /**
     * Obter meus dados (Perfil).
     * ACESSO: RESTRITO (Exige Token)
     * Novo endpoint útil para o Frontend pegar dados do usuário logado.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMeuPerfil(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }

        Integer id = jwtTokenProvider.getIdFromToken(token);
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);

        if (usuario.isPresent()) {
            // Remove a senha antes de devolver o JSON para segurança
            Usuario u = usuario.get();
            u.setSenha("PROTEGIDA");
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }

    // DTO para atualização de senha
    public static class SenhaUpdateRequest {
        private String senhaAntiga;
        private String novaSenha;

        // Getters e Setters
        public String getSenhaAntiga() { return senhaAntiga; }
        public void setSenhaAntiga(String senhaAntiga) { this.senhaAntiga = senhaAntiga; }
        public String getNovaSenha() { return novaSenha; }
        public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
    }
}