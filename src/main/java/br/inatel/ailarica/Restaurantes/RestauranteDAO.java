package br.inatel.ailarica.Restaurantes;

import java.sql.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestauranteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/ailarica_db";
    private static final String USER = "root";
    private static final String PASSWORD = "minecraft123321";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE — cria restaurante e adiciona seus pratos
    public void criar(Restaurante restaurante) {
        String sql = "INSERT INTO restaurante (nome, descricao, endereco, telefone, avaliacao, ativo, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, restaurante.getNome());
            stmt.setString(2, restaurante.getDescricao());
            stmt.setString(3, restaurante.getEndereco());
            stmt.setString(4, restaurante.getTelefone());
            stmt.setFloat(5, restaurante.getAvaliacao());
            stmt.setBoolean(6, restaurante.isAtivo());
            stmt.setString(7, restaurante.getFotoPerfil());
            stmt.executeUpdate();

            // Pega o ID gerado automaticamente
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int idRestaurante = 0;
            if (generatedKeys.next()) {
                idRestaurante = generatedKeys.getInt(1);
                restaurante.setIdRestaurante(idRestaurante); // ✅ Atualiza o ID no objeto
            }

            // Insere pratos (se houver)
            if (restaurante.getCardapio() != null && !restaurante.getCardapio().isEmpty()) {
                PratoDAO pratoDAO = new PratoDAO();
                for (Prato prato : restaurante.getCardapio()) {
                    pratoDAO.inserirPrato(prato, idRestaurante);
                }
            }

            System.out.println("✅ Restaurante e cardápio inseridos com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ — lista todos os restaurantes com cardápio
    public List<Restaurante> listarTodos() {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = "SELECT * FROM restaurante";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            PratoDAO pratoDAO = new PratoDAO();

            while (rs.next()) {
                Restaurante r = new Restaurante();
                r.setIdRestaurante(rs.getInt("idRestaurante"));
                r.setNome(rs.getString("nome"));
                r.setDescricao(rs.getString("descricao"));
                r.setEndereco(rs.getString("endereco"));
                r.setTelefone(rs.getString("telefone"));
                r.setAvaliacao(rs.getFloat("avaliacao"));
                r.setAtivo(rs.getBoolean("ativo"));
                r.setFotoPerfil(rs.getString("fotoPerfil"));

                // Carrega cardápio do restaurante
                r.setCardapio(pratoDAO.listarPorRestaurante(r.getIdRestaurante()));

                restaurantes.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurantes;
    }

    // READ — busca restaurante por ID com cardápio
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
                r.setFotoPerfil(rs.getString("fotoPerfil"));

                // Carrega pratos do restaurante
                PratoDAO pratoDAO = new PratoDAO();
                r.setCardapio(pratoDAO.listarPorRestaurante(r.getIdRestaurante()));

                return r;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE — atualiza dados do restaurante (não mexe nos pratos)
    public void atualizar(Restaurante restaurante) {
        String sql = "UPDATE restaurante SET nome=?, descricao=?, endereco=?, telefone=?, avaliacao=?, ativo=?, fotoPerfil=? WHERE idRestaurante=?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, restaurante.getNome());
            stmt.setString(2, restaurante.getDescricao());
            stmt.setString(3, restaurante.getEndereco());
            stmt.setString(4, restaurante.getTelefone());
            stmt.setFloat(5, restaurante.getAvaliacao());
            stmt.setBoolean(6, restaurante.isAtivo());
            stmt.setString(7, restaurante.getFotoPerfil());
            stmt.setInt(8, restaurante.getIdRestaurante());

            stmt.executeUpdate();
            System.out.println("✅ Restaurante atualizado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE — remove restaurante e seus pratos associados
    public void deletar(int id) {
        try (Connection conn = conectar()) {
            conn.setAutoCommit(false); // Inicia transação

            // 1. Deleta pratos do restaurante
            String sqlPratos = "DELETE FROM prato WHERE idRestaurante = ?";
            try (PreparedStatement stmtPratos = conn.prepareStatement(sqlPratos)) {
                stmtPratos.setInt(1, id);
                stmtPratos.executeUpdate();
            }

            // 2. Deleta o restaurante
            String sqlRestaurante = "DELETE FROM restaurante WHERE idRestaurante = ?";
            try (PreparedStatement stmtRest = conn.prepareStatement(sqlRestaurante)) {
                stmtRest.setInt(1, id);
                stmtRest.executeUpdate();
            }

            conn.commit(); // Confirma as duas operações
            System.out.println("❌ Restaurante e seus pratos deletados com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ALTERAR STATUS — ativa ou desativa um restaurante
    public boolean atualizarStatus(int id, boolean ativo) {
        String sql = "UPDATE restaurante SET ativo = ? WHERE idRestaurante = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
