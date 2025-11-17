package br.inatel.ailarica.Restaurantes; // (ou o seu pacote)

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Pedido {
    private int idPedido;
    private String status;
    private float valorTotal;
    private String dataHora; // (Vamos usar String, ex: "2025-11-17T14:30:00")

    // IDs das chaves estrangeiras
    private int idUsuario;
    private int idRestaurante;

    // O "corpo" do pedido
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }
}