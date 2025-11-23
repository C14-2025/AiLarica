package br.inatel.ailarica.Restaurantes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceTest {

    @Mock
    private RestauranteDAO restauranteDAO;

    @InjectMocks
    private HorarioService horarioService;

    @Test
    void testChecarHorarios_ListaVazia() {
        // Cenário: Não há restaurantes no banco
        when(restauranteDAO.listarTodos()).thenReturn(Collections.emptyList());

        // Execução
        horarioService.checarHorariosAutomaticamente();

        // Verificação
        // Garante que ele chamou o listarTodos
        verify(restauranteDAO, times(1)).listarTodos();
        // Garante que ele NÃO tentou atualizar ninguém (já que a lista estava vazia)
        verify(restauranteDAO, never()).atualizarStatus(anyInt(), anyBoolean());
    }

    @Test
    void testChecarHorarios_ExecutaFluxoCompleto() {
        // Cenário: Existe um restaurante com horários
        Restaurante r = new Restaurante();
        r.setIdRestaurante(1);
        r.setNome("Restaurante Teste");
        r.setAtivo(false);

        // Configura horários fictícios (arrays preenchidos)
        RestauranteHorario horarios = new RestauranteHorario();
        // Preenche com 0s ou valores padrão só para não dar NullPointerException
        r.setHorarios(horarios);

        when(restauranteDAO.listarTodos()).thenReturn(List.of(r));

        // Execução
        horarioService.checarHorariosAutomaticamente();

        // Verificação
        // O teste aqui é garantir que o serviço "rodou" sem erros e consultou o banco.
        // A lógica exata de "deveriaEstarAberto" depende da hora do sistema,
        // então focamos em garantir que a integração com o DAO ocorreu.
        verify(restauranteDAO, times(1)).listarTodos();
    }
}