<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-orange-600 shadow-md">
      <div class="max-w-3xl mx-auto px-4 py-4 flex items-center gap-4">
        <button @click="$router.push('/usuario/dashboard')" class="text-white hover:bg-orange-700 p-2 rounded-lg transition">
          ← Voltar
        </button>
        <h1 class="text-xl font-bold text-white">Meu Perfil</h1>
      </div>
    </header>

    <main class="max-w-3xl mx-auto px-4 py-8">

      <div v-if="successMessage" class="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg flex items-center gap-2">
        <span>✅</span> <p class="text-sm text-green-700">{{ successMessage }}</p>
      </div>
      <div v-if="errorMessage" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg flex items-center gap-2">
        <span>❌</span> <p class="text-sm text-red-700">{{ errorMessage }}</p>
      </div>

      <div class="bg-white rounded-xl shadow p-6 mb-6">
        <h2 class="text-lg font-bold text-gray-800 mb-4 border-b pb-2">Dados Pessoais</h2>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Nome Completo</label>
            <input v-model="form.nome" type="text" :class="inputClass" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Endereço de Entrega</label>
            <input v-model="form.endereco" type="text" :class="inputClass" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input v-model="form.email" type="email" disabled :class="inputClassDisabled" />
          </div>
          <div class="pt-2 text-right">
            <button
              @click="saveProfile"
              :disabled="saving"
              class="bg-orange-600 text-white px-6 py-2 rounded-lg font-bold hover:bg-orange-700 transition shadow-sm disabled:opacity-50"
            >
              {{ saving ? 'Salvando...' : 'Salvar Alterações' }}
            </button>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow p-6">
        <h2 class="text-lg font-bold text-gray-800 mb-4 border-b pb-2">Segurança</h2>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Senha Atual</label>
            <input v-model="security.oldPassword" type="password" :class="inputClass" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Nova Senha</label>
            <input v-model="security.newPassword" type="password" :class="inputClass" />
          </div>

          <div class="pt-2 text-right">
            <button
              @click="changePassword"
              class="bg-gray-800 text-white px-6 py-2 rounded-lg font-bold hover:bg-gray-900 transition shadow-sm"
            >
              Alterar Senha
            </button>
          </div>
        </div>
      </div>

    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import AuthService from '@/services/authService';

const API_BASE_URL = 'http://localhost:8080';
const token = AuthService.getToken();
const headers = { 'Authorization': `Bearer ${token}` };

// Classes Tailwind para inputs (para evitar o erro @apply)
const inputClass = 'w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none transition-all';
const inputClassDisabled = `${inputClass} bg-gray-100 cursor-not-allowed`;

const saving = ref(false);
const successMessage = ref('');
const errorMessage = ref('');

// ✅ form.cpf REMOVIDO
const form = ref({
  nome: '',
  endereco: '',
  email: '',
  id: 0,
});

const security = ref({
  oldPassword: '',
  newPassword: ''
});

const fetchUserData = async () => {
  try {
    // GET /usuarios/me
    const response = await axios.get(`${API_BASE_URL}/usuarios/me`, { headers });
    const data = response.data;

    // Mapeia os dados para o formulário
    form.value = {
      id: data.id,
      nome: data.nome,
      endereco: data.endereco,
      email: data.email,
      // data.cpf não é mapeado aqui
    };
  } catch (error) {
    console.error('Erro ao carregar perfil', error);
  }
};

const saveProfile = async () => {
  saving.value = true;
  successMessage.value = '';
  errorMessage.value = '';

  try {
    // PUT /usuarios/me
    await axios.put(`${API_BASE_URL}/usuarios/me`, form.value, { headers });

    // Atualiza o nome no cache local (Header)
    const currentUser = AuthService.getCurrentUser();
    if (currentUser) {
      currentUser.nome = form.value.nome;
      localStorage.setItem('user', JSON.stringify(currentUser));
    }

    successMessage.value = 'Perfil atualizado com sucesso!';
    errorMessage.value = '';
  } catch (error) {
    console.error('Erro ao atualizar perfil', error);
    errorMessage.value = 'Erro ao atualizar perfil. Verifique se o Backend suporta a atualização.';
  } finally {
    saving.value = false;
  }
};

const changePassword = async () => {
  if (!security.value.oldPassword || !security.value.newPassword) {
    errorMessage.value = 'Por favor, preencha a senha atual e a nova senha.';
    return;
  }
  if (security.value.newPassword.length < 8 || !security.value.newPassword.match(/[\d]/) || !security.value.newPassword.match(/[^a-zA-Z0-9\s]/)) {
    errorMessage.value = 'A nova senha deve ter no mínimo 8 caracteres, número e símbolo.';
    return;
  }

  saving.value = true;
  successMessage.value = '';
  errorMessage.value = '';

  try {
    // PUT /usuarios/senha
    await axios.put(`${API_BASE_URL}/usuarios/senha`, {
      senhaAntiga: security.value.oldPassword,
      novaSenha: security.value.newPassword
    }, { headers });

    successMessage.value = 'Senha alterada com sucesso!';
    security.value = { oldPassword: '', newPassword: '', confirmPassword: '' } as any; // Limpa formulário
  } catch (error) {
    console.error('Erro ao alterar senha:', error);
    errorMessage.value = 'Erro ao alterar senha. Verifique sua senha atual.';
  } finally {
    saving.value = false;
  }
};

const handleLogoffAllDevices = () => {
  if(confirm("Tem certeza que deseja sair de todos os dispositivos?")) {
    AuthService.logout();
  }
};

onMounted(() => {
  if (token) fetchUserData();
});
</script>

<style scoped>
/* Estilos limpos, sem @apply, pois ele causa erros se o Tailwind não estiver configurado para processar CSS dentro do Vue */
</style>
