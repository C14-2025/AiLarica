package br.inatel.ailarica.Restaurantes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedido {
    private int idItemPedido;
    private int quantidade;
    private float precoNoMomento; // Preço do prato na hora da compra

    // IDs das chaves estrangeiras
    private int idPedido;
    private int idPrato;

    public ItemPedido() {
    }
}