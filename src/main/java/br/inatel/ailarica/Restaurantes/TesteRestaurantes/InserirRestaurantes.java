package br.inatel.ailarica.Restaurantes.TesteRestaurantes;

import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteDAO;
import br.inatel.ailarica.Restaurantes.RestauranteHorario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class InserirRestaurantes {
    public static void main(String[] args) {

        RestauranteDAO dao = new RestauranteDAO();

        RestauranteHorario horarioPadrao = new RestauranteHorario(
                new int[]{800, 800, 800, 800, 800, 900, 900},
                new int[]{2200, 2200, 2200, 2200, 2300, 2300, 2200}
        );

        Restaurante r1 = new Restaurante(
                1,
                "Lanchonete do Goku",
                "Melhor lanche de Ki da cidade",
                horarioPadrao,
                "Rua Kame House, 123",
                "12345-6789",
                true,
                "goku.jpg",
                Arrays.asList("Hambúrguer de energia", "Refrigerante de nuvem voadora")
        );

        Restaurante r2 = new Restaurante(
                2,
                "Churrascaria Vegeta",
                "Carne assada no poder máximo do orgulho Saiyajin",
                horarioPadrao,
                "Av. Capsule Corp, 456",
                "98765-4321",
                true,
                "vegeta.jpg",
                Arrays.asList("Picanha Saiyajin", "Costela Galáctica")
        );

        Restaurante r3 = new Restaurante(
                3,
                "Sorveteria Bulma",
                "Sabores tecnológicos que desafiam a física!",
                horarioPadrao,
                "Rua do Laboratório, 999",
                "11223-4455",
                true,
                "bulma.jpg",
                Arrays.asList("Sorvete de gravidade zero", "Milkshake elétrico")
        );

        dao.criar(r1);
        dao.criar(r2);
        dao.criar(r3);

        System.out.println("✅ Restaurantes inseridos com sucesso!");
    }
}
