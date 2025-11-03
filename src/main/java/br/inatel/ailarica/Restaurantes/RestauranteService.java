package br.inatel.ailarica.Restaurantes;

import java.util.ArrayList;
import java.util.List;

public class RestauranteService {

    private List<Restaurante> restaurantes = new ArrayList<>();

    // Cadastrar novo restaurante
    public void cadastrarRestaurante(Restaurante r) {
        restaurantes.add(r);
        System.out.println("Restaurante cadastrado: " + r.getNome());
    }

    // Consultar restaurante pelo ID
    public Restaurante buscarPorId(int id) {
        return restaurantes.stream()
                .filter(r -> r.getIdRestaurante() == id)
                .findFirst()
                .orElse(null);
    }

    // Atualizar nome e descrição
    public void atualizarInfo(int id, String nome, String descricao) {
        Restaurante r = buscarPorId(id);
        if (r != null) {
            r.setNome(nome);
            r.setDescricao(descricao);
            System.out.println("Informações atualizadas!");
        }
    }

    // Adicionar prato ao cardápio
    public void adicionarPrato(int id, String prato) {
        Restaurante r = buscarPorId(id);
        if (r != null) {
            r.getCardapio().add(prato);
            System.out.println("Prato adicionado: " + prato);
        }
    }

    // Listar todos restaurantes
    public List<Restaurante> listarRestaurantes() {
        return restaurantes;
    }
}
