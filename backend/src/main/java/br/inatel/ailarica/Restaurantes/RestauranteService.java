package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.security.PasswordEncoder; // <--- NOVO IMPORT
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO restauranteDAO;
    private final PasswordEncoder passwordEncoder; // <--- NOVO CAMPO

    // Construtor atualizado para receber o PasswordEncoder
    public RestauranteService(RestauranteDAO restauranteDAO, PasswordEncoder passwordEncoder) {
        this.restauranteDAO = restauranteDAO;
        this.passwordEncoder = passwordEncoder; // <--- SALVANDO O OBJETO
    }

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {

        // --- CORREÇÃO DE SEGURANÇA AQUI ---
        String senhaPura = novo.getSenha();
        String senhaHash = passwordEncoder.encode(senhaPura); // 1. Criptografa
        novo.setSenha(senhaHash); // 2. Substitui a senha pura pelo hash
        // ----------------------------------

        Restaurante restauranteCriado = restauranteDAO.criar(novo);
        return restauranteCriado;
    }

    // Listar todos (sem alteração)
    public List<Restaurante> listarTodos() {
        return restauranteDAO.listarTodos();
    }

    // Buscar por id (sem alteração)
    public Optional<Restaurante> buscarPorId(int id) {
        return Optional.ofNullable(restauranteDAO.buscarPorId(id));
    }

    // Atualizar (sem alteração)
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {

            // ATENÇÃO: Se o frontend mandar a senha pura aqui, ela deve ser tratada
            // Mas, por enquanto, vamos assumir que a atualização de senha é feita
            // pelo RestauranteAuthService.atualizarSenha() para segurança.

            atualizado.setIdRestaurante(id);
            restauranteDAO.atualizar(atualizado);
            return Optional.of(atualizado);
        }
        return Optional.empty();
    }

    // ... (restante do código omitido por ser igual)
    // Deletar
    public boolean deletar(int id) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            restauranteDAO.deletar(id);
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