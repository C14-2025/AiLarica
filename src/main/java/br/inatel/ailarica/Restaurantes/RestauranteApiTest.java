package br.inatel.ailarica.Restaurantes;

import io.javalin.Javalin;
import java.util.List;

public class RestauranteApiTest {

    public static void main(String[] args) {
        RestauranteService service = new RestauranteService();

        // Criando o servidor Javalin
        Javalin app = Javalin.create().start(7000);

        // GET: listar todos os restaurantes
        app.get("/restaurantes", ctx -> {
            List<Restaurante> todos = service.listarRestaurantes();
            ctx.json(todos); // retorna JSON
        });

        // GET: buscar restaurante por ID
        app.get("/restaurantes/:id", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurante r = service.buscarPorId(id);
            if (r != null) ctx.json(r);
            else ctx.status(404).result("Restaurante não encontrado");
        });

        // POST: cadastrar novo restaurante
        app.post("/restaurantes", ctx -> {
            Restaurante r = ctx.bodyAsClass(Restaurante.class); // pega JSON do body
            service.cadastrarRestaurante(r);
            ctx.status(201).json(r);
        });

        // PUT: atualizar nome e descrição
        app.put("/restaurantes/:id", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurante r = ctx.bodyAsClass(Restaurante.class);
            service.atualizarInfo(id, r.getNome(), r.getDescricao());
            ctx.status(200).result("Atualizado!");
        });

        // POST: adicionar prato ao cardápio
        app.post("/restaurantes/:id/cardapio", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            String prato = ctx.body(); // corpo é o nome do prato
            service.adicionarPrato(id, prato);
            ctx.status(200).result("Prato adicionado: " + prato);
        });
    }
}
