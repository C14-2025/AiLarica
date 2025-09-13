package br.inatel.ailarica;

import java.util.ArrayList;
import java.util.List;

class Usuario {
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

    @Override
    public String toString() {
        return nome + ";" + email + ";" + senha + ";" + confirmado + ";" + String.join(",", enderecos);
    }

    public static Usuario fromString(String linha) {
        String[] partes = linha.split(";");
        if (partes.length >= 4) {
            Usuario u = new Usuario(partes[0], partes[1], partes[2]);
            u.confirmado = Boolean.parseBoolean(partes[3]);

            if (partes.length == 5 && !partes[4].isEmpty()) {
                String[] enderecos = partes[4].split(",");
                for (String e : enderecos) {
                    u.adicionarEndereco(e);
                }
            }
            return u;
        }
        return null;
    }
}