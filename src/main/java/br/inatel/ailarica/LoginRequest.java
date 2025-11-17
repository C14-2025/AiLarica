package br.inatel.ailarica;

/**
 * Classe de requisição para login.
 * Suporta login de usuário (com endereço obrigatório) e restaurante.
 */
public class LoginRequest {
    private String email;
    private String senha;
    private String tipo; // "USUARIO" ou "RESTAURANTE"
    private String endereco; // Obrigatório para USUARIO

    // Construtor padrão
    public LoginRequest() {
    }

    // Construtor completo
    public LoginRequest(String email, String senha, String tipo, String endereco) {
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.endereco = endereco;
    }

    // Getters e Setters
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
