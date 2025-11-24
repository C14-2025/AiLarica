<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900">
    <header class="bg-red-600 shadow-xl shadow-red-600/30 sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 sm:py-6 flex items-center justify-between">
        <div class="flex items-center space-x-4">
          <button
            @click="goBack"
            class="text-white hover:bg-red-700 p-2 rounded-lg transition-colors duration-200"
            title="Voltar ao Dashboard"
          >
            ← Voltar
          </button>
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Configurações do Restaurante</h1>
        </div>
      </div>
    </header>

    <main class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
      <div class="flex gap-2 mb-6 border-b border-gray-200">
        <button
          @click="activeTab = 'general'"
          :class="[
            'px-4 py-2 font-medium border-b-2 transition-colors',
            activeTab === 'general'
              ? 'border-red-600 text-red-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          ]"
        >
          📋 Informações Gerais
        </button>
        <button
          @click="activeTab = 'contact'"
          :class="[
            'px-4 py-2 font-medium border-b-2 transition-colors',
            activeTab === 'contact'
              ? 'border-red-600 text-red-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          ]"
        >
          📞 Contato
        </button>
        <button
          @click="activeTab = 'security'"
          :class="[
            'px-4 py-2 font-medium border-b-2 transition-colors',
            activeTab === 'security'
              ? 'border-red-600 text-red-600'
              : 'border-transparent text-gray-600 hover:text-gray-900'
          ]"
        >
          🔐 Segurança
        </button>
      </div>

      <div v-if="successMessage" class="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg flex items-center gap-2">
        <span>✅</span> <p class="text-sm text-green-700">{{ successMessage }}</p>
      </div>
      <div v-if="errorMessage" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg flex items-center gap-2">
        <span>❌</span> <p class="text-sm text-red-700">{{ errorMessage }}</p>
      </div>

      <div v-if="activeTab === 'general'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-6">Dados Básicos</h2>

          <div class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nome do Restaurante</label>
              <input
                v-model="formData.nome"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea
                v-model="formData.descricao"
                rows="3"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
              ></textarea>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Endereço Completo</label>
              <input
                v-model="formData.endereco"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
              />
            </div>

            <hr class="border-gray-200 my-6">

            <div>
              <h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                🕒 Horários de Funcionamento
              </h3>

              <div class="bg-gray-50 rounded-xl border border-gray-200 overflow-hidden">
                <div class="grid grid-cols-3 bg-gray-100 p-3 text-xs font-bold text-gray-500 uppercase tracking-wider text-center">
                  <div class="text-left pl-4">Dia</div>
                  <div>Abertura</div>
                  <div>Fechamento</div>
                </div>

                <div
                  v-for="(day, index) in daysOfWeek"
                  :key="index"
                  class="grid grid-cols-3 items-center p-3 border-t border-gray-200 hover:bg-white transition-colors"
                >
                  <div class="font-medium text-gray-700 pl-4">{{ day }}</div>

                  <div class="flex justify-center">
                    <input
                      type="time"
                      v-model="scheduleForm.abertura[index]"
                      class="px-3 py-1.5 border border-gray-300 rounded-md text-sm text-gray-700 focus:ring-2 focus:ring-red-500 outline-none cursor-pointer bg-white hover:border-red-300 transition-colors"
                    >
                  </div>

                  <div class="flex justify-center">
                    <input
                      type="time"
                      v-model="scheduleForm.fechamento[index]"
                      class="px-3 py-1.5 border border-gray-300 rounded-md text-sm text-gray-700 focus:ring-2 focus:ring-red-500 outline-none cursor-pointer bg-white hover:border-red-300 transition-colors"
                    >
                  </div>
                </div>
              </div>
            </div>

            <div class="pt-4">
              <button
                @click="saveGeneralSettings"
                :disabled="saving"
                class="w-full py-3 bg-red-600 text-white font-bold rounded-xl hover:bg-red-700 transition-all shadow-md hover:shadow-lg disabled:opacity-70 disabled:cursor-not-allowed flex justify-center items-center gap-2"
              >
                <span v-if="saving" class="animate-spin">⏳</span>
                {{ saving ? 'Salvando...' : 'Salvar Alterações' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'contact'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-4">Contato</h2>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
              <input
                v-model="formData.telefone"
                type="tel"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input
                v-model="formData.email"
                type="email"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
              />
            </div>
            <div class="pt-4">
              <button
                @click="saveContactSettings"
                :disabled="saving"
                class="w-full py-3 bg-red-600 text-white font-bold rounded-xl hover:bg-red-700 transition-all shadow-md hover:shadow-lg disabled:opacity-70 disabled:cursor-not-allowed"
              >
                {{ saving ? 'Salvando...' : 'Salvar Contato' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'security'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-4">Segurança</h2>
          <div class="p-4 bg-yellow-50 border border-yellow-200 rounded-lg text-center">
            <p class="text-yellow-800">Funcionalidade de alterar senha será implementada em breve.</p>
          </div>
          <div class="mt-4 pt-4 border-t">
            <button @click="handleLogoffAllDevices" class="w-full py-2 bg-gray-200 text-gray-700 font-semibold rounded-lg hover:bg-gray-300 transition-colors">
              Sair
            </button>
          </div>
        </div>
      </div>

    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import AuthService from '@/services/authService';

const router = useRouter();
const API_BASE_URL = 'http://localhost:8080';

// Auth Setup
const token = AuthService.getToken();
const currentUser = AuthService.getCurrentUser();
const headers = { 'Authorization': `Bearer ${token}` };

if (!token || currentUser?.tipo !== 'RESTAURANTE') {
  AuthService.logout();
  router.push('/login');
}

// Estado
const activeTab = ref('general');
const saving = ref(false);
const successMessage = ref('');
const errorMessage = ref('');
const fullRestaurantData = ref<any>({});

// Dados Gerais
const formData = ref({
  nome: '',
  descricao: '',
  endereco: '',
  telefone: '',
  email: '',
  ativo: true
});

// Dados de Horário
const daysOfWeek = ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'];
const scheduleForm = ref({
  abertura: Array(7).fill('10:00'),
  fechamento: Array(7).fill('22:00')
});

// --- Funções Auxiliares ---
const intToTimeStr = (val: number): string => {
  if (val === undefined || val === null) return '00:00';
  const str = val.toString().padStart(4, '0');
  return `${str.slice(0, 2)}:${str.slice(2)}`;
};

const timeStrToInt = (val: string): number => {
  if (!val) return 0;
  return parseInt(val.replace(':', ''), 10);
};

const goBack = () => {
  router.push('/restaurante/dashboard');
};

// --- 1. Buscar Dados ---
const fetchRestaurantData = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${currentUser.id}`, { headers });
    const data = response.data;
    fullRestaurantData.value = data;

    formData.value = {
      nome: data.nome || '',
      descricao: data.descricao || '',
      endereco: data.endereco || '',
      telefone: data.telefone || '',
      email: data.email || '',
      ativo: data.ativo
    };

    if (data.horarios) {
      scheduleForm.value.abertura = data.horarios.horariosAbertura.map(intToTimeStr);
      scheduleForm.value.fechamento = data.horarios.horariosFechamento.map(intToTimeStr);
    }

  } catch (error) {
    console.error('Erro ao buscar dados:', error);
    errorMessage.value = 'Erro ao carregar dados.';
  }
};

// --- 2. Salvar Configurações ---
const saveGeneralSettings = async () => {
  saving.value = true;
  successMessage.value = '';
  errorMessage.value = '';

  try {
    const horariosInteiros = {
      horariosAbertura: scheduleForm.value.abertura.map(timeStrToInt),
      horariosFechamento: scheduleForm.value.fechamento.map(timeStrToInt)
    };

    const payload = {
      ...fullRestaurantData.value,
      nome: formData.value.nome,
      descricao: formData.value.descricao,
      endereco: formData.value.endereco,
      ativo: formData.value.ativo,
      horarios: horariosInteiros
    };

    await axios.put(`${API_BASE_URL}/restaurantes/me`, payload, { headers });

    fullRestaurantData.value = payload;
    successMessage.value = 'Configurações e horários salvos com sucesso!';

    if (payload.nome !== currentUser.nome) {
      const updatedUser = { ...currentUser, nome: payload.nome };
      localStorage.setItem('user', JSON.stringify(updatedUser));
    }

  } catch (error) {
    console.error('Erro ao salvar:', error);
    errorMessage.value = 'Erro ao salvar as alterações.';
  } finally {
    saving.value = false;
  }
};

const saveContactSettings = async () => {
  saving.value = true;
  successMessage.value = '';
  errorMessage.value = '';
  try {
    const payload = {
      ...fullRestaurantData.value,
      telefone: formData.value.telefone,
      email: formData.value.email
    };
    await axios.put(`${API_BASE_URL}/restaurantes/me`, payload, { headers });
    fullRestaurantData.value = payload;
    successMessage.value = 'Contato atualizado!';
  } catch(e) {
    errorMessage.value = 'Erro ao salvar contato.';
  } finally {
    saving.value = false;
  }
};

const handleLogoffAllDevices = () => {
  if(confirm("Sair do sistema?")) AuthService.logout();
};

onMounted(() => {
  if (token) fetchRestaurantData();
});
</script>

<style scoped>
/* Estilos limpos, sem @apply, pois ele causa erros se o Tailwind não estiver configurado para processar CSS dentro do Vue */
</style>
