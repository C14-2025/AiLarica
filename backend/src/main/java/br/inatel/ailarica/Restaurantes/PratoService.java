package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PratoService {

    private final PratoDAO pratoDAO;
    private final RestauranteDAO restauranteDAO;

    public PratoService(PratoDAO pratoDAO, RestauranteDAO restauranteDAO) {
        this.pratoDAO = pratoDAO;
        this.restauranteDAO = restauranteDAO;
    }

    // --- MÉTODOS DE LEITURA (Públicos) ---

    public Optional<Prato> buscarPratoPorId(int idPrato) {
        return Optional.ofNullable(pratoDAO.buscarPorId(idPrato));
    }

    public List<Prato> listarPratosDoRestaurante(int idRestaurante) {
        return pratoDAO.listarPorRestaurante(idRestaurante);
    }

    // --- MÉTODOS DE ESCRITA (Restritos) ---

    public Prato criarPrato(int idRestaurante, Prato novoPrato) {
        if (restauranteDAO.buscarPorId(idRestaurante) == null) {
            return null;
        }
        return pratoDAO.criar(novoPrato, idRestaurante);
    }

    // 🔒 ATUALIZAÇÃO SEGURA
    public Optional<Prato> atualizarPrato(int idPrato, int idRestauranteLogado, Prato pratoAtualizado) {
        Prato existente = pratoDAO.buscarPorId(idPrato);

        if (existente == null) {
            return Optional.empty(); // Prato não existe
        }

        // --- A BLINDAGEM CONTRA IDOR ---
        if (existente.getIdRestaurante() != idRestauranteLogado) {
            // Tentativa de alterar prato de outro restaurante!
            return Optional.empty(); // Retorna vazio como se não existisse (ou lança erro)
        }
        // -------------------------------

        pratoAtualizado.setIdPrato(idPrato);
        pratoAtualizado.setIdRestaurante(idRestauranteLogado); // Garante a propriedade
        pratoDAO.atualizar(pratoAtualizado);
        return Optional.of(pratoAtualizado);
    }

    // 🔒 DELEÇÃO SEGURA
    public boolean deletarPrato(int idPrato, int idRestauranteLogado) {
        Prato existente = pratoDAO.buscarPorId(idPrato);

        if (existente == null) {
            return false;
        }

        // --- A BLINDAGEM CONTRA IDOR ---
        if (existente.getIdRestaurante() != idRestauranteLogado) {
            // Tentativa de deletar prato de outro restaurante!
            return false;
        }
        // -------------------------------

        pratoDAO.deletarPorId(idPrato);
        return true;
    }
}