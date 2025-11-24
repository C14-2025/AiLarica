package br.inatel.ailarica.Restaurantes;

import java.util.ArrayList;
import java.util.List;

public class Restaurante {

    private int idRestaurante;
    private String nome;
    private String descricao;
    private String endereco;
    private String telefone;
    private float avaliacao;
    private boolean ativo;
    private String fotoPerfil;
    private String email;
    private String senha;

    // ✅ CAMPO NOVO
    private String tempoMedioEntrega;

    // Objeto complexo de horários
    private RestauranteHorario horarios;

    // Lista de pratos (não vai pro banco na tabela restaurante, é apenas lógico)
    private List<Prato> cardapio = new ArrayList<>();

    public Restaurante() {
    }

    // --- GETTERS E SETTERS MANUAIS (Para garantir que funcione sem Lombok) ---

    public String getTempoMedioEntrega() {
        return tempoMedioEntrega;
    }

    public void setTempoMedioEntrega(String tempoMedioEntrega) {
        this.tempoMedioEntrega = tempoMedioEntrega;
    }

    public int getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(int idRestaurante) { this.idRestaurante = idRestaurante; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public float getAvaliacao() { return avaliacao; }
    public void setAvaliacao(float avaliacao) { this.avaliacao = avaliacao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public RestauranteHorario getHorarios() { return horarios; }
    public void setHorarios(RestauranteHorario horarios) { this.horarios = horarios; }

    public List<Prato> getCardapio() { return cardapio; }
    public void setCardapio(List<Prato> cardapio) { this.cardapio = cardapio; }
}