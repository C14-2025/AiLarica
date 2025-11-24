import api from './api';

interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
  addresses: Address[];
}

interface Address {
  id: number;
  street: string;
  number: string;
  neighborhood: string;
  city: string;
  state: string;
}

interface PaymentCard {
  id: number;
  brand: string;
  lastFour: string;
  expiry: string;
}

export const userService = {
  // Obter dados do usuário
  async getUser(): Promise<User> {
    try {
      const response = await api.get('/usuarios/perfil');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar dados do usuário:', error);
      throw error;
    }
  },

  // Atualizar dados do usuário
  async updateUser(userData: Partial<User>): Promise<User> {
    try {
      const response = await api.put('/usuarios/perfil', userData);
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar dados do usuário:', error);
      throw error;
    }
  },

  // Obter endereços do usuário
  async getUserAddresses(): Promise<Address[]> {
    try {
      const response = await api.get('/usuarios/enderecos');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar endereços:', error);
      throw error;
    }
  },

  // Adicionar novo endereço
  async addAddress(address: Omit<Address, 'id'>): Promise<Address> {
    try {
      const response = await api.post('/usuarios/enderecos', address);
      return response.data;
    } catch (error) {
      console.error('Erro ao adicionar endereço:', error);
      throw error;
    }
  },

  // Atualizar endereço
  async updateAddress(id: number, address: Omit<Address, 'id'>): Promise<Address> {
    try {
      const response = await api.put(`/usuarios/enderecos/${id}`, address);
      return response.data;
    } catch (error) {
      console.error(`Erro ao atualizar endereço ${id}:`, error);
      throw error;
    }
  },

  // Deletar endereço
  async deleteAddress(id: number): Promise<void> {
    try {
      await api.delete(`/usuarios/enderecos/${id}`);
    } catch (error) {
      console.error(`Erro ao deletar endereço ${id}:`, error);
      throw error;
    }
  },

  // Obter cartões de pagamento
  async getPaymentCards(): Promise<PaymentCard[]> {
    try {
      const response = await api.get('/usuarios/cartoes');
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar cartões de pagamento:', error);
      throw error;
    }
  },

  // Adicionar novo cartão
  async addPaymentCard(cardData: any): Promise<PaymentCard> {
    try {
      const response = await api.post('/usuarios/cartoes', cardData);
      return response.data;
    } catch (error) {
      console.error('Erro ao adicionar cartão de pagamento:', error);
      throw error;
    }
  },

  // Deletar cartão de pagamento
  async deletePaymentCard(id: number): Promise<void> {
    try {
      await api.delete(`/usuarios/cartoes/${id}`);
    } catch (error) {
      console.error(`Erro ao deletar cartão ${id}:`, error);
      throw error;
    }
  },
};
