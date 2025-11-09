package br.inatel.ailarica.Restaurantes;

import java.sql.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PratoDAO {

    // Configuração do banco
    private static final String URL = "jdbc:mysql://localhost:3306/ailarica_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "minecraft123321";

    // Método auxiliar para conectar
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ✅ Criar prato (mesmo que inserir)
    public void criar(Prato prato, int idRestaurante) {
        inserirPrato(prato, idRestaurante);
    }

    // ✅ Inserir prato no banco
    public void inserirPrato(Prato prato, int idRestaurante) {
        String sql = "INSERT INTO prato (nome, descricao, preco, disponivel, foto, idRestaurante) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prato.getNome());
            stmt.setString(2, prato.getDescricao());
            stmt.setFloat(3, prato.getPreco());
            stmt.setBoolean(4, prato.isDisponivel());
            stmt.setString(5, prato.getFoto());
            stmt.setInt(6, idRestaurante);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Listar pratos de um restaurante
    public List<Prato> listarPorRestaurante(int idRestaurante) {
        List<Prato> pratos = new ArrayList<>();
        String sql = "SELECT * FROM prato WHERE idRestaurante = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRestaurante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prato p = new Prato();
                p.setIdPrato(rs.getInt("idPrato"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setPreco(rs.getFloat("preco"));
                p.setDisponivel(rs.getBoolean("disponivel"));
                p.setFoto(rs.getString("foto"));
                p.setIdRestaurante(rs.getInt("idRestaurante")); // ✅ campo novo
                pratos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pratos;
    }

    // ✅ Deletar todos os pratos de um restaurante
    public void deletarPorRestaurante(int idRestaurante) {
        String sql = "DELETE FROM prato WHERE idRestaurante = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRestaurante);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ (Opcional) Deletar prato individualmente
    public void deletarPorId(int idPrato) {
        String sql = "DELETE FROM prato WHERE idPrato = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrato);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}