import api from './api';

interface OrderItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

interface Order {
  id: number;
  restaurant: string;
  date: string;
  status: string;
  total: number;
  items: OrderItem[];
}

export const orderService = {
  // Obter todos os pedidos do usuário
  async getUserOrders(): Promise<Order[]> {
    try {
      const response = await api.get('/pedidos');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pedidos:', error);
      throw error;
    }
  },

  // Obter pedido por ID
  async getOrderById(id: number): Promise<Order> {
    try {
      const response = await api.get(`/pedidos/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Erro ao buscar pedido ${id}:`, error);
      throw error;
    }
  },

  // Criar novo pedido
  async createOrder(orderData: any): Promise<Order> {
    try {
      const response = await api.post('/pedidos', orderData);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar pedido:', error);
      throw error;
    }
  },

  // Atualizar status do pedido
  async updateOrderStatus(id: number, status: string): Promise<Order> {
    try {
      const response = await api.put(`/pedidos/${id}/status`, { status });
      return response.data;
    } catch (error) {
      console.error(`Erro ao atualizar status do pedido ${id}:`, error);
      throw error;
    }
  },

  // Cancelar pedido
  async cancelOrder(id: number): Promise<void> {
    try {
      await api.delete(`/pedidos/${id}`);
    } catch (error) {
      console.error(`Erro ao cancelar pedido ${id}:`, error);
      throw error;
    }
  },

  // Avaliar pedido
  async rateOrder(id: number, rating: number, comment: string): Promise<void> {
    try {
      await api.post(`/pedidos/${id}/avaliar`, { rating, comment });
    } catch (error) {
      console.error(`Erro ao avaliar pedido ${id}:`, error);
      throw error;
    }
  },
};
