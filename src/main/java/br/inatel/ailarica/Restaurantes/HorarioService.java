package br.inatel.ailarica.Restaurantes; // (ou seu pacote)

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HorarioService {

    private final RestauranteDAO restauranteDAO;

    public HorarioService(RestauranteDAO restauranteDAO) {
        this.restauranteDAO = restauranteDAO;
    }

    /**
     * Roda a cada minuto para checar os horários.
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checarHorariosAutomaticamente() {
        System.out.println("--- 🤖 Rodando agendador de horários (Versão Simples) ---");

        // Pega a hora e dia atuais
        LocalDateTime agora = LocalDateTime.now();
        int diaIndex = agora.getDayOfWeek().getValue() - 1; // 0=Segunda, 6=Domingo
        int horaAtual = agora.getHour() * 100 + agora.getMinute(); // Ex: 1830

        // 1. Busca os restaurantes (USANDO O MÉTODO QUE JÁ TEMOS)
        List<Restaurante> restaurantes = restauranteDAO.listarTodos();

        // 2. Loop para checar cada um
        for (Restaurante r : restaurantes) {
            if (r.getHorarios() == null) continue; // Pula se não tiver horário

            int horaAbrir = r.getHorarios().getHorariosAbertura()[diaIndex];
            int horaFechar = r.getHorarios().getHorariosFechamento()[diaIndex];

            // 3. A LÓGICA INTELIGENTE (que lida com "virar a noite")
            boolean deveriaEstarAberto;

            if (horaFechar < horaAbrir) {
                // "Vira a noite" (ex: 18:00 - 02:00)
                deveriaEstarAberto = (horaAtual >= horaAbrir || horaAtual < horaFechar);
            } else {
                // Normal (ex: 09:00 - 17:00)
                deveriaEstarAberto = (horaAtual >= horaAbrir && horaAtual < horaFechar);
            }

            // 4. Se o status atual estiver errado, atualiza o banco
            if (r.isAtivo() != deveriaEstarAberto) {
                System.out.println("Atualizando status do " + r.getNome() + " para: " + (deveriaEstarAberto ? "ABERTO" : "FECHADO"));
                restauranteDAO.atualizarStatus(r.getIdRestaurante(), deveriaEstarAberto);
            }
        }
    }
}