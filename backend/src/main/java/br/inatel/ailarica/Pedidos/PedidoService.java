package br.inatel.ailarica.Pedidos;

import br.inatel.ailarica.Restaurantes.Restaurante;
import br.inatel.ailarica.Restaurantes.RestauranteService; // ✅ NOVO IMPORT
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional; // Para lidar com o retorno do Service
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final RestauranteService restauranteService; // ✅ 1. Dependência Injetada

    public PedidoService(PedidoDAO pedidoDAO, RestauranteService restauranteService) {
        this.pedidoDAO = pedidoDAO;
        this.restauranteService = restauranteService;
    }

    public List<Pedido> listarPedidosDoRestaurante(int idRestaurante) {
        return pedidoDAO.listarPorRestaurante(idRestaurante);
    }
    // ... (Outros métodos de listagem omitidos para brevidade)

    public List<Pedido> listarPedidosAtivosDoRestaurante(int idRestaurante) {
        List<Pedido> todosPedidos = pedidoDAO.listarPorRestaurante(idRestaurante);

        return todosPedidos.stream()
                .filter(p -> !"ENTREGUE".equals(p.getStatus()) && !"CANCELADO".equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Pedido> listarPedidosDoUsuario(int idUsuario) {
        return pedidoDAO.listarPorUsuario(idUsuario);
    }

    public List<Pedido> listarPedidosAtivosDoUsuario(int idUsuario) {
        List<Pedido> todosPedidos = pedidoDAO.listarPorUsuario(idUsuario);

        return todosPedidos.stream()
                .filter(p -> !"ENTREGUE".equals(p.getStatus()) && !"CANCELADO".equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public boolean atualizarStatus(int idPedido, String novoStatus) {
        Pedido existente = pedidoDAO.buscarPorId(idPedido);
        if (existente == null) {
            return false;
        }

        pedidoDAO.atualizarStatus(idPedido, novoStatus);
        return true;
    }


    public Pedido criarPedido(Pedido pedido) {
        // ✅ VERIFICAÇÃO DE STATUS CRÍTICA DE NEGÓCIO
        Optional<Restaurante> restauranteOptional = restauranteService
                .buscarPorId(pedido.getIdRestaurante());

        if (restauranteOptional.isEmpty() || !restauranteOptional.get().isAtivo()) {
            // Se não encontrar o restaurante OU se ele estiver inativo/fechado,
            // retorna null, bloqueando a criação.
            return null;
        }
        // ---------------------------------------------

        // Define os valores padrão antes de salvar
        pedido.setStatus("PENDENTE"); // Todo pedido começa como pendente
        pedido.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return pedidoDAO.criar(pedido);
    }
}