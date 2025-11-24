package br.inatel.ailarica.Restaurantes;

import java.util.List;

public class DashboardDTO {
    // Card 1: Quantidade de pedidos hoje
    private int pedidosHoje;

    // Card 2: Dinheiro que entrou hoje
    private double faturamentoHoje;

    // Card 3: Tempo que o dono definiu (ex: "40-50 min")
    private String tempoMedio;

    // Card 4: Nota do restaurante
    private double avaliacaoMedia;

    // O Gráfico de Barras (Lista de dias e totais)
    private List<VendaDiaria> vendasSemanais;

    // --- Getters e Setters ---
    public int getPedidosHoje() { return pedidosHoje; }
    public void setPedidosHoje(int pedidosHoje) { this.pedidosHoje = pedidosHoje; }

    public double getFaturamentoHoje() { return faturamentoHoje; }
    public void setFaturamentoHoje(double faturamentoHoje) { this.faturamentoHoje = faturamentoHoje; }

    public String getTempoMedio() { return tempoMedio; }
    public void setTempoMedio(String tempoMedio) { this.tempoMedio = tempoMedio; }

    public double getAvaliacaoMedia() { return avaliacaoMedia; }
    public void setAvaliacaoMedia(double avaliacaoMedia) { this.avaliacaoMedia = avaliacaoMedia; }

    public List<VendaDiaria> getVendasSemanais() { return vendasSemanais; }
    public void setVendasSemanais(List<VendaDiaria> vendasSemanais) { this.vendasSemanais = vendasSemanais; }

    // Classe interna para representar cada barra do gráfico
    public static class VendaDiaria {
        private String dia; // Ex: "2025-11-23"
        private double total; // Ex: 1500.50

        public VendaDiaria(String dia, double total) {
            this.dia = dia;
            this.total = total;
        }
        // Getters
        public String getDia() { return dia; }
        public double getTotal() { return total; }
    }
}