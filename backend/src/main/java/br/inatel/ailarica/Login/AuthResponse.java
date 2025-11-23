package br.inatel.ailarica.Login;
import br.inatel.ailarica.Cliente.Usuario;
import br.inatel.ailarica.Cliente.UsuarioService;

/**
 * Classe de resposta para requisições de autenticação.
 * Contém informações do usuário/restaurante autenticado.
 */
public class AuthResponse {
    private int id;
    private String nome;
    private String email;
    private String tipo; // "USUARIO" ou "RESTAURANTE"
    private String endereco; // Para usuários
    private String token; // Token JWT
    private String mensagem;
    private boolean sucesso;

    // Construtor padrão
    public AuthResponse() {
    }

    // Construtor para sucesso com token
    public AuthResponse(int id, String nome, String email, String tipo, String endereco, String token, String mensagem) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.endereco = endereco;
        this.token = token;
        this.mensagem = mensagem;
        this.sucesso = true;
    }

    // Construtor para erro
    public AuthResponse(String mensagem) {
        this.mensagem = mensagem;
        this.sucesso = false;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
