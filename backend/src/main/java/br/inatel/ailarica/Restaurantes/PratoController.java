// PratoController.java
package br.inatel.ailarica.Restaurantes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PratoController {

    private final PratoService pratoService;

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    // GET (O que você pediu): Listar todos os pratos de um restaurante
    @GetMapping("/restaurantes/{id}/pratos")
    public List<Prato> listarPratos(@PathVariable int id) {
        return pratoService.listarPratosDoRestaurante(id);
    }

    // GET: Buscar um prato específico pelo ID dele
    @GetMapping("/pratos/{idPrato}")
    public ResponseEntity<Prato> buscarPrato(@PathVariable int idPrato) {
        return pratoService.buscarPratoPorId(idPrato)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Criar um novo prato para um restaurante
    @PostMapping("/restaurantes/{id}/pratos")
    public Prato criarPrato(@PathVariable int id, @RequestBody Prato novoPrato) {
        return pratoService.criarPrato(id, novoPrato);
    }

    // PUT: Atualizar um prato
    @PutMapping("/pratos/{idPrato}")
    public ResponseEntity<Prato> atualizarPrato(@PathVariable int idPrato, @RequestBody Prato pratoAtualizado) {
        return pratoService.atualizarPrato(idPrato, pratoAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Deletar um prato
    @DeleteMapping("/pratos/{idPrato}")
    public ResponseEntity<Void> deletarPrato(@PathVariable int idPrato) {
        if (pratoService.deletarPrato(idPrato)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}