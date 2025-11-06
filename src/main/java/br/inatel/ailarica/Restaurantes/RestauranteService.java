package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO dao = new RestauranteDAO();

    // Criar novo restaurante (salva no banco)
    public Restaurante criar(Restaurante novo) {
        dao.criar(novo);
        return novo;
    }

    // Listar todos (do banco)
    public List<Restaurante> listarTodos() {
        return dao.listarTodos();
    }

    // Buscar por ID (do banco)
    public Optional<Restaurante> buscarPorId(int id) {
        Restaurante r = dao.buscarPorId(id);
        return Optional.ofNullable(r);
    }

    // Atualizar (no banco)
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        Restaurante existente = dao.buscarPorId(id);
        if (existente != null) {
            atualizado.setIdRestaurante(id);
            dao.atualizar(atualizado);
            return Optional.of(atualizado);
        }
        return Optional.empty();
    }

    // Deletar (no banco)
    public boolean deletar(int id) {
        Restaurante existente = dao.buscarPorId(id);
        if (existente != null) {
            dao.deletar(id);
            return true;
        }
        return false;
    }
}
