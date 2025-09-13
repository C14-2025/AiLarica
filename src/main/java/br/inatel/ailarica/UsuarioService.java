package br.inatel.ailarica;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

class UsuarioService {
    private final String ARQUIVO = "usuarios.txt";

    // Cadastro
    public boolean cadastrar(String nome, String email, String senha) {
        List<Usuario> usuarios = carregarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return false; // já existe
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            writer.write(new Usuario(nome, email, senha).toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Login
    public Usuario login(String email, String senha) {
        List<Usuario> usuarios = carregarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    // Atualizar Senha
    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) { //Sofia Groke
        List<Usuario> usuarios = carregarUsuarios();
        boolean atualizado = false;
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senhaAntiga)) {
                u.setSenha(novaSenha);
                atualizado = true;
                break;
            }
        }
        if (atualizado) {
            salvarUsuarios(usuarios);
        }
        return atualizado;
    }

    // Salvar lista de usuários no arquivo
    private void salvarUsuarios(List<Usuario> usuarios) { //Sofia Groke
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, false))) { // false para sobrescrever
            for (Usuario u : usuarios) {
                writer.write(u.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carregar lista de usuários do arquivo
    public List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Usuario u = Usuario.fromString(linha);
                if (u != null) {
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            // arquivo pode não existir ainda, ignorar
        }
        return usuarios;
    }
}