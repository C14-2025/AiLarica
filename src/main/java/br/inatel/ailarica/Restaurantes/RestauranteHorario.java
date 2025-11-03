package br.inatel.ailarica.Restaurantes;

public class RestauranteHorario {
    private int[] horariosAbertura = new int[7]; // ha1..ha7
    private int[] horariosFechamento = new int[7]; // hf1..hf7

    public RestauranteHorario(int[] horariosAbertura, int[] horariosFechamento) {
        if (horariosAbertura.length != 7 || horariosFechamento.length != 7) {
            throw new IllegalArgumentException("É necessário informar 7 horários para cada.");
        }
        this.horariosAbertura = horariosAbertura;
        this.horariosFechamento = horariosFechamento;
    }

    public static String intToString(int horario) {
        int horas = horario / 100;
        int minutos = horario % 100;
        return String.format("%02d:%02d", horas, minutos);
    }

    // Pega horário de abertura de um dia (0 = domingo, 6 = sábado)
    public String getAbertura(int dia) {
        return intToString(horariosAbertura[dia]);
    }

    // Pega horário de fechamento de um dia (0 = domingo, 6 = sábado)
    public String getFechamento(int dia) {
        return intToString(horariosFechamento[dia]);
    }

    // Setters individuais
    public void setAbertura(int dia, int horario) {
        horariosAbertura[dia] = horario;
    }

    public void setFechamento(int dia, int horario) {
        horariosFechamento[dia] = horario;
    }
}
