package br.inatel.ailarica;

import java.util.ArrayList;
import java.util.List;

public class Restaurante {
    private String nome;
    private List<String> cardapio = new ArrayList<>();

    public Restaurante(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getCardapio() {
        return cardapio;
    }

    public void adicionarPrato(String prato) {
        cardapio.add(prato);
    }

    @Override
    public String toString() {
        return nome + ";" + String.join(",", cardapio);
    }

    public static Restaurante fromString(String linha) {
        String[] partes = linha.split(";");
        Restaurante r = new Restaurante(partes[0]);
        if (partes.length > 1 && !partes[1].isEmpty()) {
            String[] pratos = partes[1].split(",");
            for (String p : pratos) r.adicionarPrato(p);
        }
        return r;
    }
}
