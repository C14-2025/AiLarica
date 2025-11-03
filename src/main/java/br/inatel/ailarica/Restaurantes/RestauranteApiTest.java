package br.inatel.ailarica.Restaurantes;

import io.javalin.Javalin;
import java.util.List;

public class RestauranteApiTest {
    public static void main(String[] args) {
        RestauranteService service = new RestauranteService();

        Javalin app = Javalin.create().start(7000);

        // Listar todos
        app.get("/restaurantes", ctx -> ctx.json(service.listarRestaurantes()));

        // Buscar por id (USAR {id} em vez de :id)
        app.get("/restaurantes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var r = service.buscarPorId(id);
            if (r != null) ctx.json(r);
            else ctx.status(404).result("Restaurante não encontrado");
        });

        // Criar restaurante (body = JSON)
        app.post("/restaurantes", ctx -> {
            Restaurante r = ctx.bodyAsClass(Restaurante.class);
            service.cadastrarRestaurante(r);
            ctx.status(201).json(r);
        });

        // Atualizar (simples)
        app.put("/restaurantes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Restaurante r = ctx.bodyAsClass(Restaurante.class);
            service.atualizarInfo(id, r.getNome(), r.getDescricao());
            ctx.status(200).result("Atualizado!");
        });

        // Adicionar prato
        app.post("/restaurantes/{id}/cardapio", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            String prato = ctx.body();
            service.adicionarPrato(id, prato);
            ctx.status(200).result("Prato adicionado: " + prato);
        });

        System.out.println("API rodando em http://localhost:7000");
    }
}
