package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO restauranteDAO = new RestauranteDAO(); // ✅ instância manual

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {
        restauranteDAO.criar(novo);
        return novo;
    }

    // Listar todos
    public List<Restaurante> listarTodos() {
        return restauranteDAO.listarTodos();
    }

    // Buscar por id
    public Optional<Restaurante> buscarPorId(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        return Optional.ofNullable(r);
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
            restauranteDAO.deletar(id);
            return true;
        }
        return false;
    }

    // Ativar
    public boolean ativar(int id) {
        return restauranteDAO.atualizarStatus(id, true);
    }

    // Desativar
    public boolean desativar(int id) {
        return restauranteDAO.atualizarStatus(id, false);
    }

    // Alternar
    public boolean alternar(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        boolean novoStatus = !r.isAtivo();
        return restauranteDAO.atualizarStatus(id, novoStatus);
    }

    public boolean atualizarStatus(int id, boolean status) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        return restauranteDAO.atualizarStatus(id, status);
    }
}
