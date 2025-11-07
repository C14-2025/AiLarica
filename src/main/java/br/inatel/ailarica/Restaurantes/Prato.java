package br.inatel.ailarica.Restaurantes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Prato {

    private int idPrato;          // ID do prato
    private String nome;          // Nome do prato
    private String descricao;     // Descrição do prato
    private float preco;          // Preço do prato
    private boolean disponivel;   // Indica se o prato está disponível
    private String foto;          // Caminho ou URL da imagem do prato
    private int idRestaurante;    // ✅ Novo campo: referência ao restaurante dono do prato

    public Prato(int idPrato, String nome, String descricao, float preco, boolean disponivel, String foto, int idRestaurante) {
        this.idPrato = idPrato;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
        this.foto = foto;
        this.idRestaurante = idRestaurante;
    }

    public Prato() {
        // Construtor vazio
    }
}
