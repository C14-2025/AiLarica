<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="max-w-md w-full bg-white p-8 rounded-xl shadow-2xl">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-extrabold text-gray-900">
          Bem-vindo ao AiLarica
        </h1>
        <p class="mt-2 text-sm text-gray-600">
          Faça login para continuar
        </p>
      </div>

      <!-- Toggle entre Usuário e Restaurante -->
      <div class="flex mb-6 rounded-lg bg-gray-200 p-1">
        <button
          @click="userType = 'user'"
          :class="[
            'w-1/2 py-2 text-sm font-medium rounded-lg transition-colors duration-200',
            userType === 'user'
              ? 'bg-ailarica-orange text-white shadow-md'
              : 'text-gray-700 hover:bg-gray-300'
          ]"
        >
          Usuário
        </button>
        <button
          @click="userType = 'restaurant'"
          :class="[
            'w-1/2 py-2 text-sm font-medium rounded-lg transition-colors duration-200',
            userType === 'restaurant'
              ? 'bg-ailarica-orange text-white shadow-md'
              : 'text-gray-700 hover:bg-gray-300'
          ]"
        >
          Restaurante
        </button>
      </div>

      <!-- Mensagem de erro -->
      <div v-if="errorMessage" class="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg text-sm">
        {{ errorMessage }}
      </div>

      <!-- Credenciais de teste -->
      <div class="mb-4 p-3 bg-blue-50 border border-blue-200 rounded-lg text-xs text-blue-800">
        <p class="font-semibold mb-1">Credenciais de Teste:</p>
        <p v-if="userType === 'user'">Email: <strong>cliente@teste.com</strong></p>
        <p v-if="userType === 'user'">Senha: <strong>senha123</strong></p>
        <p v-if="userType === 'restaurant'">Email: <strong>admin@teste</strong></p>
        <p v-if="userType === 'restaurant'">Senha: <strong>admin123</strong></p>
      </div>

      <form class="space-y-6" @submit.prevent="handleLogin">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700">
            Email
          </label>
          <div class="mt-1">
            <input
              id="email"
              v-model="email"
              name="email"
              type="email"
              autocomplete="email"
              required
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ailarica-orange focus:border-ailarica-orange sm:text-sm"
            />
          </div>
        </div>

        <div>
          <label for="password" class="block text-sm font-medium text-gray-700">
            Senha
          </label>
          <div class="mt-1">
            <input
              id="password"
              v-model="password"
              name="password"
              type="password"
              autocomplete="current-password"
              required
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ailarica-orange focus:border-ailarica-orange sm:text-sm"
            />
          </div>
        </div>

        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              class="h-4 w-4 text-ailarica-orange focus:ring-ailarica-orange border-gray-300 rounded"
            />
            <label for="remember-me" class="ml-2 block text-sm text-gray-900">
              Lembrar de mim
            </label>
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ailarica-red hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ailarica-red transition duration-150 ease-in-out"
          >
            Entrar como {{ userType === 'user' ? 'Usuário' : 'Restaurante' }}
          </button>
        </div>
      </form>

      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          Não tem uma conta?
          <a href="/register" class="font-medium text-ailarica-red hover:text-red-600">
            Cadastre-se
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const userType = ref<'user' | 'restaurant'>('user')
const email = ref('')
const password = ref('')
const router = useRouter()
const errorMessage = ref('')

// Credenciais de teste
const TEST_CREDENTIALS = {
  user: {
    email: 'cliente@teste.com',
    password: 'senha123'
  },
  restaurant: {
    email: 'admin@teste',
    password: 'admin123'
  }
}

const handleLogin = () => {
  errorMessage.value = ''
  
  const testCreds = TEST_CREDENTIALS[userType.value]
  
  // Validação de credenciais de teste
  if (email.value === testCreds.email && password.value === testCreds.password) {
    // Armazenar token mock no localStorage
    const mockToken = `mock_token_${userType.value}_${Date.now()}`
    localStorage.setItem('token', mockToken)
    localStorage.setItem('userType', userType.value)
    localStorage.setItem('userEmail', email.value)
    
    // Redirecionar conforme o tipo de usuário
    if (userType.value === 'restaurant') {
      router.push('/restaurante/dashboard')
    } else {
      router.push('/usuario/home')
    }
  } else {
    errorMessage.value = `Credenciais inválidas. Use: ${testCreds.email} / ${testCreds.password}`
  }
}
</script>

<style scoped>
/* Estilos específicos do componente, se necessário */
</style>
