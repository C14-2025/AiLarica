import api from "./api";

export default {
  /**
   * POST /usuarios/cadastro
   * Cadastro público, sem autenticação.
   */
  cadastrarUsuario(dados: {
    nome: string;
    email: string;
    senha: string;
  }) {
    return api.post("/usuarios/cadastro", dados);
  },

  /**
   * PUT /usuarios/senha
   * Atualizar senha — exige token no header.
   */
  atualizarSenha(token: string, payload: {
    senhaAntiga: string;
    novaSenha: string;
  }) {
    return api.put("/usuarios/senha", payload, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  },

  /**
   * GET /usuarios/me
   * Retorna os dados do usuário autenticado
   */
  getMeuPerfil(token: string) {
    return api.get("/usuarios/me", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }
};
