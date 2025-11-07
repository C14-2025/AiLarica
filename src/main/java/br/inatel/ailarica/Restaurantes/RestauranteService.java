package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO restauranteDAO = new RestauranteDAO();
    private final PratoDAO pratoDAO = new PratoDAO(); // ✅ novo DAO para lidar com pratos

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {
        restauranteDAO.criar(novo);

        // Se o restaurante tiver pratos, salva todos
        if (novo.getCardapio() != null) {
            for (Prato prato : novo.getCardapio()) {
                prato.setIdRestaurante(novo.getIdRestaurante());
                pratoDAO.criar(prato, novo.getIdRestaurante());
            }
        }

        return novo;
    }

    // Listar todos (incluindo pratos)
    public List<Restaurante> listarTodos() {
        List<Restaurante> restaurantes = restauranteDAO.listarTodos();

        // ✅ Para cada restaurante, busca o cardápio correspondente
        for (Restaurante r : restaurantes) {
            r.setCardapio(pratoDAO.listarPorRestaurante(r.getIdRestaurante()));
        }

        return restaurantes;
    }

    // Buscar por id
    public Optional<Restaurante> buscarPorId(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r != null) {
            r.setCardapio(pratoDAO.listarPorRestaurante(id));
        }
        return Optional.ofNullable(r);
    }

    // Atualizar
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            atualizado.setIdRestaurante(id);
            restauranteDAO.atualizar(atualizado);

            // Atualiza os pratos também
            pratoDAO.deletarPorRestaurante(id);
            if (atualizado.getCardapio() != null) {
                for (Prato p : atualizado.getCardapio()) {
                    p.setIdRestaurante(id);
                    pratoDAO.criar(p, atualizado.getIdRestaurante());
                }
            }

            return Optional.of(atualizado);
        }
        return Optional.empty();
    }

    // Deletar
    public boolean deletar(int id) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            pratoDAO.deletarPorRestaurante(id);
            restauranteDAO.deletar(id);
            return true;
        }
        return false;
    }

    // Ativar / Desativar / Alternar (sem mudanças)
    public boolean atualizarStatus(int id, boolean status) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        return restauranteDAO.atualizarStatus(id, status);
    }

    public boolean alternar(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        boolean novoStatus = !r.isAtivo();
        return restauranteDAO.atualizarStatus(id, novoStatus);
    }
}
