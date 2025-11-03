package br.inatel.ailarica.Restaurantes;

import java.util.Scanner;

public class RestauranteHorario {

    private int[] horariosAbertura = new int[7];   // Segunda (0) a Domingo (6)
    private int[] horariosFechamento = new int[7];

    private final String[] diasSemana = {
            "Segunda-feira", "Terça-feira", "Quarta-feira",
            "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"
    };

    // Construtor vazio
    public RestauranteHorario() {
        // Inicializa arrays com 0
    }

    // Construtor com arrays
    public RestauranteHorario(int[] horariosAbertura, int[] horariosFechamento) {
        if (horariosAbertura.length != 7 || horariosFechamento.length != 7) {
            throw new IllegalArgumentException("É necessário informar 7 horários para cada.");
        }
        this.horariosAbertura = horariosAbertura;
        this.horariosFechamento = horariosFechamento;
    }

    // Converte int HHmm em string HH:mm
    public static String intToString(int horario) {
        int horas = horario / 100;
        int minutos = horario % 100;

        if (horas < 0 || horas > 23 || minutos < 0 || minutos > 59) {
            throw new IllegalArgumentException("Horário inválido: " + horario);
        }

        return String.format("%02d:%02d", horas, minutos);
    }

    // Getters individuais
    public String getAbertura(int dia) {
        return intToString(horariosAbertura[dia]);
    }

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

    // Ler horários do usuário via Scanner
    public void setHorarios() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe os horários do restaurante no formato HHmm (ex: 1200):");

        for (int i = 0; i < 7; i++) {
            System.out.print("Hora de abertura na " + diasSemana[i] + ": ");
            horariosAbertura[i] = sc.nextInt();

            System.out.print("Hora de fechamento na " + diasSemana[i] + ": ");
            horariosFechamento[i] = sc.nextInt();
        }
    }

    // Imprimir todos os horários para conferência
    public void printHorarios() {
        System.out.println("Horários do restaurante:");
        for (int i = 0; i < 7; i++) {
            System.out.println(diasSemana[i] + ": " +
                    intToString(horariosAbertura[i]) + " - " +
                    intToString(horariosFechamento[i]));
        }
    }
}
