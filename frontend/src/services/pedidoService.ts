import apiClient from './api';

export interface Pedido {
  id?: number;
  clienteId: number;
  restauranteId: number;
  itens: ItemPedido[];
  status?: string; // 'PENDENTE', 'A_CAMINHO', 'ENTREGUE'
  total?: number;
  dataPedido?: string;
}

export interface ItemPedido {
  pratoId: number;
  quantidade: number;
  preco?: number;
}

class PedidoService {
  /**
   * Busca todos os pedidos
   */
  async getAll(): Promise<Pedido[]> {
    try {
      const response = await apiClient.get('/pedidos');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pedidos:', error);
      throw error;
    }
  }

  /**
   * Busca pedidos por restaurante
   */
  async getByRestaurante(restauranteId: number): Promise<Pedido[]> {
    try {
      const response = await apiClient.get(`/pedidos/restaurante/${restauranteId}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pedidos do restaurante:', error);
      throw error;
    }
  }

  /**
   * Busca pedidos por cliente
   */
  async getByCliente(clienteId: number): Promise<Pedido[]> {
    try {
      const response = await apiClient.get(`/pedidos/cliente/${clienteId}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pedidos do cliente:', error);
      throw error;
    }
  }

  /**
   * Busca um pedido por ID
   */
  async getById(id: number): Promise<Pedido> {
    try {
      const response = await apiClient.get(`/pedidos/${id}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pedido:', error);
      throw error;
    }
  }

  /**
   * Cria um novo pedido
   */
  async create(pedido: Pedido): Promise<Pedido> {
    try {
      const response = await apiClient.post('/pedidos', pedido);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar pedido:', error);
      throw error;
    }
  }

  /**
   * Atualiza o status de um pedido
   */
  async updateStatus(id: number, status: string): Promise<Pedido> {
    try {
      const response = await apiClient.patch(`/pedidos/${id}/status`, { status });
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar status do pedido:', error);
      throw error;
    }
  }

  /**
   * Cancela um pedido
   */
  async cancel(id: number): Promise<void> {
    try {
      await apiClient.delete(`/pedidos/${id}`);
    } catch (error) {
      console.error('Erro ao cancelar pedido:', error);
      throw error;
    }
  }
}

export default new PedidoService();
