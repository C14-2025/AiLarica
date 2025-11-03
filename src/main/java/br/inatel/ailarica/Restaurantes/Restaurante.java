package br.inatel.ailarica.Restaurantes;

import java.util.ArrayList;
import java.util.List;

public class Restaurante {
    private int idRestaurante;
    private String nome;
    private String descricao;
    private RestauranteHorario horarios; // Nova forma de armazenar horários
    private String endereco;
    private String telefone;
    private float avaliacao;
    private boolean ativo;
    private String fotoPerfil;
    private List<String> cardapio;

    // Construtor
    public Restaurante(int idRestaurante, String nome, String descricao,
                       RestauranteHorario horarios, String endereco, String telefone,
                       boolean ativo, String fotoPerfil, List<String> cardapio) {
        this.idRestaurante = idRestaurante;
        this.nome = nome;
        this.descricao = descricao;
        this.horarios = horarios;
        this.endereco = endereco;
        this.telefone = telefone;
        this.ativo = ativo;
        this.fotoPerfil = fotoPerfil;
        this.avaliacao = 0.0f; // Inicializando avaliação como zero
        this.cardapio = (cardapio != null) ? cardapio : new ArrayList<>();
    }

    // Getters e Setters básicos
    public String getNome() {
        return nome;
    }

    public List<String> getCardapio() {
        return cardapio;
    }

    public RestauranteHorario getHorarios() {
        return horarios;
    }

    public void setHorarios(RestauranteHorario horarios) {
        this.horarios = horarios;
    }

    public void adicionarPrato(String prato) {
        cardapio.add(prato);
    }

    public void removerPrato(String prato) {
        cardapio.remove(prato);
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    // Para exibir informações resumidas
    @Override
    public String toString() {
        return nome + ";" + String.join(",", cardapio);
    }
}

