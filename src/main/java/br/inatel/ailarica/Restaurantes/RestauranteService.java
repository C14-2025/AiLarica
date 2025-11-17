package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO restauranteDAO;

    public RestauranteService(RestauranteDAO restauranteDAO) {
        this.restauranteDAO = restauranteDAO;
    }

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {
        // 1. O método 'restauranteDAO.criar()' não é mais 'void'.
        //    Ele retorna o objeto 'Restaurante' atualizado (com o ID).
        //    Nós precisamos capturar esse retorno.
        Restaurante restauranteCriado = restauranteDAO.criar(novo);

        // 2. Retorna o objeto que o DAO acabou de criar e atualizar.
        return restauranteCriado;
    }

    // Listar todos (incluindo pratos)
    public List<Restaurante> listarTodos() {
        return restauranteDAO.listarTodos();
    }

    // Buscar por id
    public Optional<Restaurante> buscarPorId(int id) {
        return Optional.ofNullable(restauranteDAO.buscarPorId(id));
    }

    // Atualizar
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            atualizado.setIdRestaurante(id);
            restauranteDAO.atualizar(atualizado);
            return Optional.of(atualizado);
        }
        return Optional.empty();
    }

    // Deletar
    public boolean deletar(int id) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            restauranteDAO.deletar(id); // O DAO já lida com a exclusão dos pratos
            return true;
        }
        return false;
    }

    // Ativar / Desativar / Alternar (sem mudanças)
    public boolean atualizarStatus(int id, boolean status) {
        return restauranteDAO.atualizarStatus(id, status);
    }

    public boolean alternar(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        boolean novoStatus = !r.isAtivo();
        return restauranteDAO.atualizarStatus(id, novoStatus);
    }
}
