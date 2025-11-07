//

 package br.inatel.ailarica.Restaurantes.TesteRestaurantes;

import br.inatel.ailarica.Restaurantes.Prato;
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
                Arrays.asList(
                        new Prato(1, "Hambúrguer de Energia", "Um lanche com poder de Super Saiyajin", 25.0f, true, "hamburguer.jpg"),
                        new Prato(2, "Refrigerante de Nuvem Voadora", "Bebida leve e refrescante", 10.0f, true, "refrigerante.jpg")
                )
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
                Arrays.asList(
                        new Prato(3, "Picanha Galáctica", "Corte nobre selado com energia Super Saiyajin Blue.", 89.90f, true, "picanha.jpg"),
                        new Prato(4, "Costela do Orgulho", "Assada lentamente no Ki do Príncipe dos Saiyajins.", 74.50f, true, "costela.jpg"),
                        new Prato(5, "Espetinho Final Flash", "Combinação explosiva de sabores intensos.", 29.90f, true, "espetinho.jpg")
                )
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
                Arrays.asList(
                        new Prato(6, "Sorvete de Gravidade Zero", "Derrete para cima e muda de cor conforme o sabor!", 19.90f, true, "sorvete_gravidade.jpg"),
                        new Prato(7, "Milkshake Elétrico", "Energia líquida desenvolvida pela Capsule Corp.", 24.50f, true, "milkshake_eletrico.jpg"),
                        new Prato(8, "Casquinha Cibernética", "Sorvete com chips de sabor programável.", 14.00f, true, "casquinha_cibernetica.jpg")
                )
        );

        dao.criar(r1);
        dao.criar(r2);
        dao.criar(r3);

        System.out.println("✅ Restaurantes inseridos com sucesso!");
    }
}
//