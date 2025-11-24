import apiClient from './api';

export interface Restaurante {
  id?: number;
  nome: string;
  endereco: string;
  telefone: string;
  horarioAbertura?: string;
  horarioFechamento?: string;
  ativo?: boolean;
}

class RestauranteService {
  /**
   * Busca todos os restaurantes
   */
  async getAll(): Promise<Restaurante[]> {
    try {
      const response = await apiClient.get('/restaurantes');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar restaurantes:', error);
      throw error;
    }
  }

  /**
   * Busca um restaurante por ID
   */
  async getById(id: number): Promise<Restaurante> {
    try {
      const response = await apiClient.get(`/restaurantes/${id}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar restaurante:', error);
      throw error;
    }
  }

  /**
   * Cria um novo restaurante
   */
  async create(restaurante: Restaurante): Promise<Restaurante> {
    try {
      const response = await apiClient.post('/restaurantes', restaurante);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar restaurante:', error);
      throw error;
    }
  }

  /**
   * Atualiza um restaurante existente
   */
  async update(id: number, restaurante: Restaurante): Promise<Restaurante> {
    try {
      const response = await apiClient.put(`/restaurantes/${id}`, restaurante);
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar restaurante:', error);
      throw error;
    }
  }

  /**
   * Remove um restaurante
   */
  async delete(id: number): Promise<void> {
    try {
      await apiClient.delete(`/restaurantes/${id}`);
    } catch (error) {
      console.error('Erro ao remover restaurante:', error);
      throw error;
    }
  }

  /**
   * Busca o dashboard do restaurante
   */
  async getDashboard(id: number): Promise<any> {
    try {
      const response = await apiClient.get(`/restaurantes/${id}/dashboard`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar dashboard:', error);
      throw error;
    }
  }
}

export default new RestauranteService();
