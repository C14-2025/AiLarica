package br.inatel.ailarica.Restaurantes; // (ou seu pacote)

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoDAO pedidoDAO;
    // (Poderíamos injetar PratoDAO e UsuarioDAO aqui para validações,
    //  mas vamos manter simples por enquanto)

    public PedidoService(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    public List<Pedido> listarPedidosDoRestaurante(int idRestaurante) {
        return pedidoDAO.listarPorRestaurante(idRestaurante);
    }

    public List<Pedido> listarPedidosAtivosDoRestaurante(int idRestaurante) {
        List<Pedido> todosPedidos = pedidoDAO.listarPorRestaurante(idRestaurante);

        return todosPedidos.stream()
                .filter(p -> !"ENTREGUE".equals(p.getStatus()) && !"CANCELADO".equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public boolean atualizarStatus(int idPedido, String novoStatus) {
        // (Aqui poderíamos ter uma lógica de quais status são permitidos,
        //  por exemplo, não pode voltar de "A_CAMINHO" para "PENDENTE")
        Pedido existente = pedidoDAO.buscarPorId(idPedido);
        if (existente == null) {
            return false; // Pedido não encontrado
        }

        pedidoDAO.atualizarStatus(idPedido, novoStatus);
        return true;
    }


    public Pedido criarPedido(Pedido pedido) {
        // Define os valores padrão antes de salvar
        pedido.setStatus("PENDENTE"); // Todo pedido começa como pendente
        pedido.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // (Aqui deveríamos recalcular o valorTotal com base nos itens e preços do PratoDAO
        //  para evitar fraude, mas por enquanto confiamos no frontend)

        return pedidoDAO.criar(pedido);
    }
}