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
    private List<Prato> cardapio = new ArrayList<>();


    public Restaurante(int idRestaurante, String nome, String descricao,
                       RestauranteHorario horarios, String endereco,
                       String telefone, boolean ativo, String fotoPerfil,
                       List<Prato> cardapio) {
        this.idRestaurante = idRestaurante;
        this.nome = nome;
        this.descricao = descricao;
        this.horarios = horarios;
        this.endereco = endereco;
        this.telefone = telefone;
        this.ativo = ativo;
        this.fotoPerfil = fotoPerfil;
        this.cardapio = cardapio;
        this.avaliacao = 0.0f; // valor padrão
    }

    // Construtor
    public Restaurante() {
        // construtor padrão vazio
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

    public void adicionarPrato(Prato prato) {
        cardapio.add(prato);
    }

    public void removerPrato(int idPrato) {
        cardapio.removeIf(p -> p.getIdPrato() == idPrato);
    }

    public Prato buscarPratoPorNome(String nome) {
        for (Prato p : cardapio) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

}

