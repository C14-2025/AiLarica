package br.inatel.ailarica;

// (Não precisamos de List ou ArrayList aqui)

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean confirmado;
    private String endereco; // Perfeito

    // 1. ADIÇÃO: Construtor vazio (obrigatório para o RowMapper do DAO)
    public Usuario() {
    }

    // 2. ALTERAÇÃO: Construtor principal (agora inclui 'endereco')
    public Usuario(String nome, String email, String senha, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco; // Agora o endereço é salvo
        this.confirmado = false; // por padrão, não confirmado
    }

    // --- Seus Getters e Setters (Estão ótimos!) ---

    public String getNome() {
        return nome;
    }

    // Adicionando o setNome que o DAO vai precisar
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    // Adicionando o setEmail que o DAO vai precisar
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    // Adicionando o setConfirmado que o DAO vai precisar
    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public void confirmar() {
        this.confirmado = true;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}