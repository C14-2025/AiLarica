// PratoService.java
package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PratoService {

    // 1. Você declara os CAMPOS (objetos) que você precisa
    private final PratoDAO pratoDAO;
    private final RestauranteDAO restauranteDAO;

    // 2. Você pede ao Spring para injetá-los no CONSTRUTOR
    public PratoService(PratoDAO pratoDAO, RestauranteDAO restauranteDAO) {
        this.pratoDAO = pratoDAO; // O Spring te dá o objeto, você salva
        this.restauranteDAO = restauranteDAO;
    }

    // ... (outros métodos) ...

    // 3. Você usa o CAMPO (o objeto) para chamar o método
    public Optional<Prato> buscarPratoPorId(int idPrato) {
        //   👇
        return Optional.ofNullable(pratoDAO.buscarPorId(idPrato)); // CORRETO (usando o objeto)
        //   👇
        // return Optional.ofNullable(PratoDAO.buscarPorId(idPrato)); // ERRADO (chamada estática)
    }

    public Prato criarPrato(int idRestaurante, Prato novoPrato) {
        if (restauranteDAO.buscarPorId(idRestaurante) == null) {
            return null;
        }
        //   👇
        return pratoDAO.criar(novoPrato, idRestaurante); // CORRETO (usando o objeto)
    }

    public Optional<Prato> atualizarPrato(int idPrato, Prato pratoAtualizado) {
        //   👇
        if (pratoDAO.buscarPorId(idPrato) == null) { // CORRETO
            return Optional.empty();
        }
        pratoAtualizado.setIdPrato(idPrato);
        //   👇
        pratoDAO.atualizar(pratoAtualizado); // CORRETO
        return Optional.of(pratoAtualizado);
    }

    public boolean deletarPrato(int idPrato) {
        //   👇
        if (pratoDAO.buscarPorId(idPrato) == null) { // CORRETO
            return false;
        }
        //   👇
        pratoDAO.deletarPorId(idPrato); // CORRETO
        return true;
    }

    // ... (método listarPratosDoRestaurante) ...
    public List<Prato> listarPratosDoRestaurante(int idRestaurante) {
        //   👇
        return pratoDAO.listarPorRestaurante(idRestaurante); // CORRETO
    }
}