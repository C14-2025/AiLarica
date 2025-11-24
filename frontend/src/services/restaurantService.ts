import api from './api';

interface Restaurante {
  id: number;
  name: string;
  category: string;
  rating: number;
  reviews: number;
  deliveryTime: number;
  deliveryFee: number;
  image: string;
}

interface Prato {
  id: number;
  name: string;
  description: string;
  price: number;
  restauranteId: number;
}

export const restaurantService = {
  // Obter todos os restaurantes
  async getAllRestaurantes(): Promise<Restaurante[]> {
    try {
      const response = await api.get('/restaurantes');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar restaurantes:', error);
      throw error;
    }
  },

  // Obter restaurante por ID
  async getRestauranteById(id: number): Promise<Restaurante> {
    try {
      const response = await api.get(`/restaurantes/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Erro ao buscar restaurante ${id}:`, error);
      throw error;
    }
  },

  // Buscar restaurantes por nome
  async searchRestaurantes(query: string): Promise<Restaurante[]> {
    try {
      const response = await api.get('/restaurantes/search', {
        params: { q: query },
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar restaurantes:', error);
      throw error;
    }
  },

  // Obter pratos de um restaurante
  async getPratosByRestaurante(restauranteId: number): Promise<Prato[]> {
    try {
      const response = await api.get(`/restaurantes/${restauranteId}/pratos`);
      return response.data;
    } catch (error) {
      console.error(`Erro ao buscar pratos do restaurante ${restauranteId}:`, error);
      throw error;
    }
  },

  // Buscar pratos por nome
  async searchPratos(query: string): Promise<Prato[]> {
    try {
      const response = await api.get('/pratos/search', {
        params: { q: query },
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar pratos:', error);
      throw error;
    }
  },
};
