import apiClient from './api.ts';
import { isAxiosError } from 'axios'; // Importa a função de verificação do Axios

/**
 * Payload enviado pelo login
 */
export interface LoginPayload {
  email: string;
  senha: string;
  tipo: string;      // 'USUARIO' ou 'RESTAURANTE'
  // Nota: O backend exige 'endereco' para Usuario no cadastro, mas não no login após a última correção.
}

/**
 * Resposta REAL do backend no login
 */
export interface AuthResponse {
  id: number;
  nome: string;
  email: string;
  tipo: string;
  endereco?: string; // Opcional, pois pode não vir para Restaurantes
  token: string;
  mensagem: string;
  sucesso: boolean;
}

/**
 * Serviço de autenticação
 */
class AuthService {

  /**
   * LOGIN — Compatível com /auth/login
   */
  async login(payload: LoginPayload): Promise<AuthResponse> {
    try {
      const response = await apiClient.post('/auth/login', payload);

      const data: AuthResponse = response.data;

      // Salva o token
      localStorage.setItem('authToken', data.token);

      // Remove o token do usuário antes de salvar
      const { token, ...user } = data;
      localStorage.setItem('user', JSON.stringify(user));

      return data;

    } catch (error) {
      // ✅ CORREÇÃO: Usar o isAxiosError para garantir o tipo e evitar o 'unknown'
      if (isAxiosError(error) && error.response && error.response.status === 400) {
        // Loga a mensagem exata do AuthController (Ex: "Email é obrigatório!")
        // Verificamos se data e mensagem existem antes de tentar logar
        const serverMessage = error.response.data?.mensagem;
        console.error("Mensagem do Servidor (400):", serverMessage || "Erro de validação desconhecido.");
      } else {
        console.error("Erro desconhecido ou de rede:", error);
      }
      throw error; // Relança o erro para que a View possa tratar o feedback
    }
  }

  /**
   * REGISTRO — Compatível com /usuarios/cadastro
   */
  async register(data: any): Promise<any> {
    try {
      const response = await apiClient.post('/auth/cadastro/usuario', data); // Corrigi a rota para usar a de cadastro de USUARIO
      return response.data;

    } catch (error) {
      if (isAxiosError(error) && error.response) {
        const serverMessage = error.response.data || "Erro de rede/servidor.";
        console.error('Erro no registro:', serverMessage);
        throw error.response.data; // Lança o corpo do erro para o frontend
      }
      console.error('Erro no registro (desconhecido):', error);
      throw error;
    }
  }

  /**
   * LOGOUT
   */
  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    window.location.href = '/login';
  }

  /**
   * VERIFICA LOGIN
   */
  isAuthenticated(): boolean {
    return !!localStorage.getItem('authToken');
  }

  /**
   * PEGA USUÁRIO
   */
  getCurrentUser(): any {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  /**
   * PEGA TOKEN
   */
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }
}

export default new AuthService();
