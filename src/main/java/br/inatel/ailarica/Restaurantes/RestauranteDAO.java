package br.inatel.ailarica.Restaurantes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestauranteDAO {

    // Configuração da conexão
    private static final String URL = "jdbc:mysql://localhost:3306/ailari_db";
    private static final String USER = "root";
    private static final String PASSWORD = "minecraft123321"; // use sua senha

    // Método auxiliar para obter a conexão
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE
    public void criar(Restaurante restaurante) {
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, avaliacao, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, restaurante.getNome());
            stmt.setString(2, restaurante.getDescricao());
            stmt.setString(3, restaurante.getEndereco());
            stmt.setString(4, restaurante.getTelefone());
            stmt.setFloat(5, restaurante.getAvaliacao());
            stmt.setBoolean(6, restaurante.isAtivo());
            stmt.executeUpdate();

            System.out.println("✅ Restaurante inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (listar todos)
    public List<Restaurante> listarTodos() {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = "SELECT * FROM restaurante";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Restaurante r = new Restaurante();
                r.setIdRestaurante(rs.getInt("idRestaurante"));
                r.setNome(rs.getString("nome"));
                r.setDescricao(rs.getString("descricao"));
                r.setEndereco(rs.getString("endereco"));
                r.setTelefone(rs.getString("telefone"));
                r.setAvaliacao(rs.getFloat("avaliacao"));
                r.setAtivo(rs.getBoolean("ativo"));

                restaurantes.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurantes;
    }

    // READ (buscar por ID)
    public Restaurante buscarPorId(int id) {
        String sql = "SELECT * FROM restaurante WHERE idRestaurante = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Restaurante r = new Restaurante();
                r.setIdRestaurante(rs.getInt("idRestaurante"));
                r.setNome(rs.getString("nome"));
                r.setDescricao(rs.getString("descricao"));
                r.setEndereco(rs.getString("endereco"));
                r.setTelefone(rs.getString("telefone"));
                r.setAvaliacao(rs.getFloat("avaliacao"));
                r.setAtivo(rs.getBoolean("ativo"));
                return r;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void atualizar(Restaurante restaurante) {
        String sql = "UPDATE restaurante SET nome=?, descricao=?, endereco=?, telefone=?, avaliacao=?, ativo=? WHERE idRestaurante=?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, restaurante.getNome());
            stmt.setString(2, restaurante.getDescricao());
            stmt.setString(3, restaurante.getEndereco());
            stmt.setString(4, restaurante.getTelefone());
            stmt.setFloat(5, restaurante.getAvaliacao());
            stmt.setBoolean(6, restaurante.isAtivo());
            stmt.setInt(7, restaurante.getIdRestaurante());

            stmt.executeUpdate();
            System.out.println("✅ Restaurante atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletar(int id) {
        String sql = "DELETE FROM restaurante WHERE idRestaurante = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("❌ Restaurante deletado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserir(Restaurante r) {
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, ativo, avaliacao, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getNome());
            ps.setString(2, r.getDescricao());
            ps.setString(3, r.getEndereco());
            ps.setString(4, r.getTelefone());
            ps.setBoolean(5, r.isAtivo());
            ps.setFloat(6, r.getAvaliacao());
            ps.setString(7, r.getFotoPerfil());

            ps.executeUpdate();
            System.out.println("✅ Restaurante inserido: " + r.getNome());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
