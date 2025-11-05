package br.inatel.ailarica.Restaurantes;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final List<Restaurante> restaurantes = new ArrayList<>();
    private int proximoId = 1;

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {
        novo.setIdRestaurante(proximoId++);
        restaurantes.add(novo);
        return novo;
    }

    // Listar todos
    public List<Restaurante> listarTodos() {
        return restaurantes;
    }

    // Buscar por id
    public Optional<Restaurante> buscarPorId(int id) {
        return restaurantes.stream()
                .filter(r -> r.getIdRestaurante() == id)
                .findFirst();
    }

    // Atualizar
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        return buscarPorId(id).map(r -> {
            r.setNome(atualizado.getNome());
            r.setDescricao(atualizado.getDescricao());
            r.setEndereco(atualizado.getEndereco());
            r.setTelefone(atualizado.getTelefone());
            r.setAtivo(atualizado.isAtivo());
            r.setAvaliacao(atualizado.getAvaliacao());
            r.setFotoPerfil(atualizado.getFotoPerfil());
            r.setCardapio(atualizado.getCardapio());
            r.setHorarios(atualizado.getHorarios());
            return r;
        });
    }

    // Deletar
    public boolean deletar(int id) {
        return restaurantes.removeIf(r -> r.getIdRestaurante() == id);
    }
}
