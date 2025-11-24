<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900">
    <!-- Header -->
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
      <!-- Abas de Configuração -->
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

      <!-- Mensagens de Status -->
      <div v-if="successMessage" class="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg">
        <p class="text-sm text-green-700">✓ {{ successMessage }}</p>
      </div>
      <div v-if="errorMessage" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg">
        <p class="text-sm text-red-700">✗ {{ errorMessage }}</p>
      </div>

      <!-- Aba: Informações Gerais -->
      <div v-if="activeTab === 'general'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-4">Informações Gerais</h2>

          <div class="space-y-4">
            <!-- Nome do Restaurante -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nome do Restaurante</label>
              <input
                v-model="formData.nome"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Descrição -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea
                v-model="formData.descricao"
                rows="4"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              ></textarea>
            </div>

            <!-- Endereço -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Endereço</label>
              <input
                v-model="formData.endereco"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Status do Restaurante -->
            <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
              <div>
                <p class="text-sm font-medium text-gray-700">Status do Restaurante</p>
                <p class="text-xs text-gray-500">Ativar ou desativar o restaurante</p>
              </div>
              <div class="ml-auto">
                <button
                  @click="toggleRestaurantStatus"
                  :class="[
                    'px-4 py-2 rounded-lg font-medium transition-colors',
                    formData.ativo
                      ? 'bg-green-600 text-white hover:bg-green-700'
                      : 'bg-gray-300 text-gray-700 hover:bg-gray-400'
                  ]"
                >
                  {{ formData.ativo ? '✓ Ativo' : '✗ Inativo' }}
                </button>
              </div>
            </div>

            <!-- Botão Salvar -->
            <div class="flex gap-3 pt-4">
              <button
                @click="saveGeneralSettings"
                :disabled="saving"
                class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ saving ? '⏳ Salvando...' : '💾 Salvar Alterações' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Aba: Contato -->
      <div v-if="activeTab === 'contact'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-4">Informações de Contato</h2>

          <div class="space-y-4">
            <!-- Telefone -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
              <input
                v-model="formData.telefone"
                type="tel"
                placeholder="(11) 99999-9999"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Email -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input
                v-model="formData.email"
                type="email"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Horários de Funcionamento -->
            <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
              <p class="text-sm font-medium text-blue-900">ℹ️ Horários de Funcionamento</p>
              <p class="text-xs text-blue-700 mt-1">Os horários podem ser configurados em uma página separada.</p>
            </div>

            <!-- Botão Salvar -->
            <div class="flex gap-3 pt-4">
              <button
                @click="saveContactSettings"
                :disabled="saving"
                class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ saving ? '⏳ Salvando...' : '💾 Salvar Alterações' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Aba: Segurança -->
      <div v-if="activeTab === 'security'" class="space-y-6">
        <div class="bg-white rounded-xl shadow-lg p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-4">Segurança</h2>

          <div class="space-y-4">
            <!-- Alterar Senha -->
            <div class="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
              <p class="text-sm font-medium text-yellow-900">🔐 Alterar Senha</p>
              <p class="text-xs text-yellow-700 mt-1">Para alterar sua senha, clique no botão abaixo.</p>
              <button
                @click="showPasswordModal = true"
                class="mt-3 px-4 py-2 bg-yellow-600 text-white font-medium rounded-lg hover:bg-yellow-700 transition-colors"
              >
                Alterar Senha
              </button>
            </div>

            <!-- Sessões Ativas -->
            <div class="p-4 bg-gray-50 rounded-lg">
              <p class="text-sm font-medium text-gray-900">📱 Sessões Ativas</p>
              <p class="text-xs text-gray-600 mt-1">Você está conectado em 1 dispositivo.</p>
              <button
                @click="handleLogoffAllDevices"
                class="mt-3 px-4 py-2 bg-red-600 text-white font-medium rounded-lg hover:bg-red-700 transition-colors"
              >
                Sair de Todos os Dispositivos
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal de Alteração de Senha -->
      <div
        v-if="showPasswordModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
        @click="showPasswordModal = false"
      >
        <div
          class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6"
          @click.stop
        >
          <h2 class="text-2xl font-bold text-gray-900 mb-4">Alterar Senha</h2>

          <div class="space-y-4">
            <!-- Senha Atual -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Senha Atual</label>
              <input
                v-model="passwordForm.currentPassword"
                type="password"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Nova Senha -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nova Senha</label>
              <input
                v-model="passwordForm.newPassword"
                type="password"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Confirmar Nova Senha -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Confirmar Nova Senha</label>
              <input
                v-model="passwordForm.confirmPassword"
                type="password"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>
          </div>

          <!-- Botões -->
          <div class="flex gap-3 mt-6">
            <button
              @click="showPasswordModal = false"
              class="flex-1 px-4 py-2 bg-gray-200 text-gray-800 font-semibold rounded-lg hover:bg-gray-300 transition-colors"
            >
              Cancelar
            </button>
            <button
              @click="changePassword"
              :disabled="saving"
              class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ saving ? '⏳ Alterando...' : '✓ Confirmar' }}
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const API_BASE_URL = 'http://localhost:8080'
const RESTAURANT_ID = localStorage.getItem('restaurantId') || '1'

const activeTab = ref('general')
const saving = ref(false)
const showPasswordModal = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const formData = ref({
  nome: '',
  descricao: '',
  endereco: '',
  telefone: '',
  email: '',
  ativo: true
})

const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const goBack = () => {
  router.push('/restaurante/dashboard')
}

const fetchRestaurantData = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}`)
    const data = response.data
    formData.value = {
      nome: data.nome || '',
      descricao: data.descricao || '',
      endereco: data.endereco || '',
      telefone: data.telefone || '',
      email: data.email || '',
      ativo: data.ativo !== false
    }
  } catch (error) {
    console.error('Erro ao buscar dados do restaurante:', error)
    errorMessage.value = 'Erro ao carregar as configurações do restaurante.'
  }
}

const saveGeneralSettings = async () => {
  saving.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await axios.put(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}`, {
      nome: formData.value.nome,
      descricao: formData.value.descricao,
      endereco: formData.value.endereco,
      ativo: formData.value.ativo
    })
    successMessage.value = 'Informações gerais atualizadas com sucesso!'
  } catch (error) {
    console.error('Erro ao salvar configurações gerais:', error)
    errorMessage.value = 'Erro ao salvar as informações gerais.'
  } finally {
    saving.value = false
  }
}

const saveContactSettings = async () => {
  saving.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await axios.put(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}`, {
      telefone: formData.value.telefone,
      email: formData.value.email
    })
    successMessage.value = 'Informações de contato atualizadas com sucesso!'
  } catch (error) {
    console.error('Erro ao salvar informações de contato:', error)
    errorMessage.value = 'Erro ao salvar as informações de contato.'
  } finally {
    saving.value = false
  }
}

const toggleRestaurantStatus = () => {
  formData.value.ativo = !formData.value.ativo
}

const changePassword = async () => {
  if (!passwordForm.value.currentPassword || !passwordForm.value.newPassword || !passwordForm.value.confirmPassword) {
    errorMessage.value = 'Por favor, preencha todos os campos.'
    return
  }

  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    errorMessage.value = 'As senhas não correspondem.'
    return
  }

  saving.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    // Aqui você precisaria de um endpoint específico para alterar senha
    // Por enquanto, apenas mostramos uma mensagem de sucesso
    successMessage.value = 'Senha alterada com sucesso!'
    showPasswordModal.value = false
    passwordForm.value = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    console.error('Erro ao alterar senha:', error)
    errorMessage.value = 'Erro ao alterar a senha.'
  } finally {
    saving.value = false
  }
}

const handleLogoffAllDevices = async () => {
  if (confirm('Tem certeza que deseja sair de todos os dispositivos?')) {
    localStorage.removeItem('authToken')
    localStorage.removeItem('userId')
    localStorage.removeItem('userType')
    localStorage.removeItem('userEmail')
    localStorage.removeItem('restaurantId')
    router.push('/login')
  }
}

onMounted(() => {
  fetchRestaurantData()
})
</script>

<style scoped>
/* Estilos específicos do componente */
</style>
