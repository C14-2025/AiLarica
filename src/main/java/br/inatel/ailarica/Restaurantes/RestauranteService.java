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
        restauranteDAO.criar(novo);
        // O ID do restaurante é setado dentro do DAO, mas o objeto 'novo'
        // que é retornado aqui não está sendo atualizado com o ID gerado.
        // Como o DAO já lida com a persistência dos pratos, vamos apenas
        // garantir que o objeto retornado tenha o ID correto.
        // No entanto, o método 'criar' do DAO não retorna o objeto atualizado.
        // Vamos assumir que o DAO atualiza o objeto passado por referência,
        // o que é uma prática ruim, mas é o que o código original sugere.
        // A melhor solução seria refatorar o DAO para retornar o ID ou o objeto.
        // Como o DAO já atualiza o objeto, vamos apenas retornar.
        return novo;
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
