package br.inatel.ailarica.Restaurantes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString

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
        this.horarios = new RestauranteHorario();
        this.endereco = endereco;
        this.telefone = telefone;
        this.ativo = ativo;
        this.fotoPerfil = fotoPerfil;
        this.avaliacao = 0.0f; // Inicializando avaliação como zero
        this.cardapio = (cardapio != null) ? cardapio : new ArrayList<>();
    }


    public void setHora() {
        this.horarios.setHorarios(); // apenas chama o método
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

}

