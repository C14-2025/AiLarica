package br.inatel.ailarica;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean confirmado; // novo campo
    private List<String> enderecos = new ArrayList<>();

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.confirmado = false; // por padrão, não confirmado
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) { // mantido de Sofia Groke
        this.senha = senha;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void confirmar() {
        this.confirmado = true;
    }

    public void adicionarEndereco(String endereco) {
        enderecos.add(endereco);
    }

    public List<String> getEnderecos() {
        return enderecos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}