package br.inatel.ailarica.Restaurantes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Prato {
    private int idPrato;        // Pode ser gerado automaticamente ou vindo do banco
    private String nome;
    private String descricao;
    private float preco;
    private boolean disponivel; // para controlar se o prato está ativo ou não
    private String foto;        // opcional — pode ser uma URL/base64

    public Prato(int idPrato, String nome, String descricao, float preco, boolean disponivel, String foto) {
        this.idPrato = idPrato;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
        this.foto = foto;
    }

    public Prato() {
        // construtor vazio
    }
}
