package br.inatel.ailarica.Restaurantes;

// Removemos as importações do Lombok
import java.util.ArrayList;
import java.util.List;

// Não usamos mais @Getter, @Setter, @ToString
public class Restaurante {
    private int idRestaurante;
    private String nome;
    private String descricao;
    private RestauranteHorario horarios;
    private String endereco;
    private String telefone;
    private float avaliacao;
    private boolean ativo;
    private String fotoPerfil;
    private List<Prato> cardapio = new ArrayList<>();
    private String email; // NOVO: Email para autenticação
    private String senha; // NOVO: Senha para autenticação

    // Construtor padrão (que o Jackson usa)
    public Restaurante() {
        // construtor padrão vazio
    }

    // Construtor completo (que o código antigo usava)
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

    // --- Métodos antigos ---
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

    // --- GETTERS E SETTERS MANUAIS (O que vai corrigir o erro 400) ---

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public RestauranteHorario getHorarios() {
        return horarios;
    }

    public void setHorarios(RestauranteHorario horarios) {
        this.horarios = horarios;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<Prato> getCardapio() {
        return cardapio;
    }

    public void setCardapio(List<Prato> cardapio) {
        this.cardapio = cardapio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}