package br.inatel.ailarica.Restaurantes;

import br.inatel.ailarica.Pedidos.PedidoDAO; // Import necessário
import br.inatel.ailarica.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteDAO restauranteDAO;
    private final PasswordEncoder passwordEncoder;
    private final PedidoDAO pedidoDAO; // ✅ 1. Declarar o PedidoDAO

    // ✅ 2. Atualizar o construtor para receber o PedidoDAO
    public RestauranteService(RestauranteDAO restauranteDAO,
                              PasswordEncoder passwordEncoder,
                              PedidoDAO pedidoDAO) {
        this.restauranteDAO = restauranteDAO;
        this.passwordEncoder = passwordEncoder;
        this.pedidoDAO = pedidoDAO; // Salvar o objeto injetado
    }

    // Criar novo restaurante
    public Restaurante criar(Restaurante novo) {
        String senhaPura = novo.getSenha();
        String senhaHash = passwordEncoder.encode(senhaPura);
        novo.setSenha(senhaHash);

        // Define um valor padrão se vier nulo
        if (novo.getTempoMedioEntrega() == null) {
            novo.setTempoMedioEntrega("40-50 min");
        }

        return restauranteDAO.criar(novo);
    }

    // Listar todos
    public List<Restaurante> listarTodos() {
        return restauranteDAO.listarTodos();
    }

    // Buscar por id
    public Optional<Restaurante> buscarPorId(int id) {
        return Optional.ofNullable(restauranteDAO.buscarPorId(id));
    }

    // Atualizar
    public Optional<Restaurante> atualizar(int id, Restaurante atualizado) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            atualizado.setIdRestaurante(id);
            restauranteDAO.atualizar(atualizado);
            return Optional.of(atualizado);
        }
        return Optional.empty();
    }

    // Deletar
    public boolean deletar(int id) {
        Restaurante existente = restauranteDAO.buscarPorId(id);
        if (existente != null) {
            restauranteDAO.deletar(id);
            return true;
        }
        return false;
    }

    // Ativar / Desativar
    public boolean atualizarStatus(int id, boolean status) {
        return restauranteDAO.atualizarStatus(id, status);
    }

    public boolean alternar(int id) {
        Restaurante r = restauranteDAO.buscarPorId(id);
        if (r == null) return false;
        boolean novoStatus = !r.isAtivo();
        return restauranteDAO.atualizarStatus(id, novoStatus);
    }

    // ✅ Método do Dashboard (Agora vai funcionar)
    public DashboardDTO getDashboardDados(int idRestaurante) {
        DashboardDTO dash = new DashboardDTO();

        // 1. Pedidos Hoje
        dash.setPedidosHoje(pedidoDAO.contarPedidosHoje(idRestaurante));

        // 2. Faturamento
        dash.setFaturamentoHoje(pedidoDAO.somarFaturamentoHoje(idRestaurante));

        // 3. Tempo Médio e Avaliação
        Restaurante r = restauranteDAO.buscarPorId(idRestaurante);
        if (r != null) {
            // Agora o método getTempoMedioEntrega() existe na classe Restaurante!
            dash.setTempoMedio(r.getTempoMedioEntrega() != null ? r.getTempoMedioEntrega() : "40-50 min");
            dash.setAvaliacaoMedia(r.getAvaliacao());
        }

        // 4. Gráfico Semanal
        dash.setVendasSemanais(pedidoDAO.buscarVendasUltimos7Dias(idRestaurante));

        return dash;
    }
}