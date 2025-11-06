package br.inatel.ailarica.Restaurantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import org.springframework.stereotype.Repository;
import java.sql.*;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService service;

    @GetMapping
    public List<Restaurante> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Restaurante criar(@RequestBody Restaurante novo) {
        return service.criar(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable int id, @RequestBody Restaurante atualizado) {
        return service.atualizar(id, atualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Autowired
    private RestauranteService restauranteService;

    // ✅ Ativar restaurante
    @PutMapping("/{id}/ativar")
    public ResponseEntity<String> ativarRestaurante(@PathVariable int id) {
        boolean sucesso = restauranteService.atualizarStatus(id, true);
        if (sucesso) {
            return ResponseEntity.ok("✅ Restaurante ativado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    // 🚫 Desativar restaurante
    @PutMapping("/{id}/desativar")
    public ResponseEntity<String> desativarRestaurante(@PathVariable int id) {
        boolean sucesso = restauranteService.atualizarStatus(id, false);
        if (sucesso) {
            return ResponseEntity.ok("🚫 Restaurante desativado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    // 🔁 Alternar status (toggle)
    @PutMapping("/{id}/alternar")
    public ResponseEntity<String> alternar(@PathVariable int id) {
        return service.alternar(id)
                ? ResponseEntity.ok("Status do restaurante alternado!")
                : ResponseEntity.status(404).body("Restaurante não encontrado!");
    }
}
