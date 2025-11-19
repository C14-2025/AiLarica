package br.inatel.ailarica.Cliente;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint mockado para Cadastro
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody Usuario novoUsuario) {
        if (usuarioService.cadastrar(novoUsuario)) {
            return ResponseEntity.ok("Usuário cadastrado com sucesso! (Mockado em memória)");
        }
        return ResponseEntity.badRequest().body("Email já cadastrado ou dados inválidos. (Mockado em memória)");
    }

    // Endpoint mockado para Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());
        if (usuario != null) {
            return ResponseEntity.ok("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "! (Mockado em memória)");
        }
        return ResponseEntity.status(401).body("Email, senha incorretos ou conta não confirmada. (Mockado em memória)");
    }

    // Endpoint mockado para Atualizar Senha
    @PutMapping("/senha")
    public ResponseEntity<String> atualizarSenha(@RequestBody SenhaUpdateRequest updateRequest) {
        if (usuarioService.atualizarSenha(updateRequest.getEmail(), updateRequest.getSenhaAntiga(), updateRequest.getNovaSenha())) {
            return ResponseEntity.ok("Senha atualizada com sucesso! (Mockado em memória)");
        }
        return ResponseEntity.status(400).body("Email ou senha antiga incorretos. (Mockado em memória)");
    }

    // Classe auxiliar para o corpo da requisição de Login
    private static class LoginRequest {
        private String email;
        private String senha;

        public String getEmail() { return email; }
        public String getSenha() { return senha; }
    }

    // Classe auxiliar para o corpo da requisição de Atualização de Senha
    private static class SenhaUpdateRequest {
        private String email;
        private String senhaAntiga;
        private String novaSenha;

        public String getEmail() { return email; }
        public String getSenhaAntiga() { return senhaAntiga; }
        public String getNovaSenha() { return novaSenha; }
    }
}
