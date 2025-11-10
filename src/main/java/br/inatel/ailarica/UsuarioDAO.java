package br.inatel.ailarica;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO mockado para a entidade Usuario.
 * A persistência é feita em memória (List<Usuario>) para focar
 * na implementação do CRUD de Restaurante, conforme solicitado.
 */
@Repository
public class UsuarioDAO {

    private final List<Usuario> usuarios = new ArrayList<>();
    private int nextId = 1;

    public UsuarioDAO() {
        // Mock de alguns usuários para testes
        Usuario u1 = new Usuario("Mock User 1", "mock1@email.com", "Senha@123");
        u1.confirmar();
        u1.setId(nextId++);
        usuarios.add(u1);

        Usuario u2 = new Usuario("Mock User 2", "mock2@email.com", "Senha@456");
        u2.setId(nextId++);
        usuarios.add(u2);
    }

    public void criar(@NotNull Usuario usuario) {
        usuario.setId(nextId++);
        usuarios.add(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public void atualizar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                return;
            }
        }
    }

    public boolean deletar(int id) {
        return usuarios.removeIf(u -> u.getId() == id);
    }
}
