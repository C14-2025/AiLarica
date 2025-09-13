package br.inatel.ailarica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class UsuarioService {
    private final String ARQUIVO = "usuarios.txt";

    // Cadastro por campos (compatível com versões antigas)
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

    // Cadastro por objeto Usuario (novo)
    public boolean cadastrar(Usuario usuario) {
        List<Usuario> usuarios = carregarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                return false; // já existe
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            writer.write(usuario.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Atualizar senha (mantido de Sofia Groke)
    public boolean atualizarSenha(String email, String senhaAntiga, String novaSenha) {
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

    // Salvar usuários (mantido)
    private void salvarUsuarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, false))) { // sobrescreve
            for (Usuario u : usuarios) {
                writer.write(u.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Atualização genérica (ex: confirmar email ou adicionar endereço)
    public void atualizarUsuarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Usuario u : usuarios) {
                writer.write(u.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Login (só permite se confirmado)
    public Usuario login(String email, String senha) {
        List<Usuario> usuarios = carregarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                    u.getSenha().equals(senha) &&
                    u.isConfirmado()) {
                return u;
            }
        }
        return null;
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