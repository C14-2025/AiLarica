package br.inatel.ailarica;

import java.io.*;
import java.util.*;

class Usuario {
    private String nome;
    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

    public void setSenha(String senha) { //Sofia Groke
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nome + ";" + email + ";" + senha;
    }

    public static Usuario fromString(String linha) {
        String[] partes = linha.split(";");
        if (partes.length == 3) {
            return new Usuario(partes[0], partes[1], partes[2]);
        }
        return null;
    }
}