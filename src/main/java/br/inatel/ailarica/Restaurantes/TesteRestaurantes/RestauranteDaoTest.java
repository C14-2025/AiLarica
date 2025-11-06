package br.inatel.ailarica.Restaurantes.TesteRestaurantes;

import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteDAO;
import br.inatel.ailarica.Restaurantes.RestauranteHorario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class RestauranteDaoTest {

    private static final String URL = "jdbc:mysql://localhost:3306/ailarica_db"; // nome do seu BD
    private static final String USER = "root";
    private static final String PASSWORD = "minecraft123321";

    public static void main(String[] args) {
        try {
            // Testa conexão
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexão com o banco estabelecida!");

            // Cria tabela se não existir
            String createTable = """
                CREATE TABLE IF NOT EXISTS restaurante (
                    idRestaurante INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100),
                    descricao TEXT,
                    endereco VARCHAR(200),
                    telefone VARCHAR(50),
                    ativo BOOLEAN,
                    avaliacao FLOAT,
                    fotoPerfil VARCHAR(255)
                );
            """;
            conn.createStatement().execute(createTable);

            // Insere um restaurante
            String insert = "INSERT INTO restaurante (nome, descricao, endereco, telefone, ativo, avaliacao, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, "Pizzaria Larica");
            ps.setString(2, "Melhor pizza de Kanto");
            ps.setString(3, "Rua Pallet 42");
            ps.setString(4, "9999-8888");
            ps.setBoolean(5, true);
            ps.setFloat(6, 4.8f);
            ps.setString(7, "img.png");
            ps.executeUpdate();
            System.out.println("🍕 Restaurante inserido!");

            // Lista todos os restaurantes
            String select = "SELECT * FROM restaurante";
            ResultSet rs = conn.createStatement().executeQuery(select);

            List<Restaurante> restaurantes = new ArrayList<>();
            while (rs.next()) {
                Restaurante r = new Restaurante();
                r.setIdRestaurante(rs.getInt("idRestaurante"));
                r.setNome(rs.getString("nome"));
                r.setDescricao(rs.getString("descricao"));
                r.setEndereco(rs.getString("endereco"));
                r.setTelefone(rs.getString("telefone"));
                r.setAtivo(rs.getBoolean("ativo"));
                r.setAvaliacao(rs.getFloat("avaliacao"));
                r.setFotoPerfil(rs.getString("fotoPerfil"));
                restaurantes.add(r);
            }

            System.out.println("📋 Restaurantes cadastrados:");
            for (Restaurante r : restaurantes) {
                System.out.println(r);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
