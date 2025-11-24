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

      <div class="flex mb-6 rounded-lg bg-gray-200 p-1">
        <button
          @click="userType = 'user'"
          :class="[
            'w-1/2 py-2 text-sm font-medium rounded-lg transition-colors duration-200',
            userType === 'user'
              ? 'bg-orange-500 text-white shadow-md'
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
              ? 'bg-orange-500 text-white shadow-md'
              : 'text-gray-700 hover:bg-gray-300'
          ]"
        >
          Restaurante
        </button>
      </div>

      <div v-if="errorMessage" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg">
        <p class="text-sm text-red-700">{{ errorMessage }}</p>
      </div>

      <form class="space-y-6" @submit.prevent="handleLogin">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700">
            Email
          </label>
          <div class="mt-1">
            <input
              id="email"
              name="email"
              type="email"
              autocomplete="email"
              required
              v-model="email"
              :disabled="loading"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
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
              name="password"
              type="password"
              autocomplete="current-password"
              required
              v-model="senha"
              :disabled="loading"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
            />
          </div>
        </div>

        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              class="h-4 w-4 text-orange-600 focus:ring-orange-500 border-gray-300 rounded"
            />
            <label for="remember-me" class="ml-2 block text-sm text-gray-900">
              Lembrar de mim
            </label>
          </div>

          <div class="text-sm">
            <a href="/register" class="font-medium text-orange-600 hover:text-orange-500">
              Esqueceu a senha?
            </a>
          </div>
        </div>

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-orange-600 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500 transition duration-150 ease-in-out disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ loading ? 'Entrando...' : `Entrar como ${userType === 'user' ? 'Usuário' : 'Restaurante'}` }}
          </button>
        </div>
      </form>

      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          Não tem uma conta?
          <a href="/register" class="font-medium text-orange-600 hover:text-orange-500">
            Cadastre-se
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import AuthService from '@/services/authService';

const router = useRouter();

// Estados do formulário
const userType = ref<'user' | 'restaurant'>('user');
const email = ref('');
const senha = ref('');

// Estados de UI
const loading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  // Limpar erro anterior
  errorMessage.value = '';

  if (!email.value || !senha.value) {
    errorMessage.value = 'Por favor, preencha todos os campos';
    return;
  }

  loading.value = true;

  try {
    const body = {
      email: email.value,
      senha: senha.value,
      tipo: userType.value === 'user' ? 'USUARIO' : 'RESTAURANTE',
      // Mantive a lógica do "Bottom" que é mais robusta
      endereco: userType.value === 'user' ? 'Endereço Não Requerido no Login' : 'N/A'
    };

    const response = await AuthService.login(body);

    console.log("Login bem-sucedido!", response);

    if (response.tipo === 'RESTAURANTE') {
      router.push('/restaurante/dashboard');
    } else {
      router.push('/usuario/dashboard');
    }

  } catch (error: any) {
    console.error("Erro no login:", error);
    // Recupera mensagem de erro do backend se existir, senão usa uma genérica
    errorMessage.value = error.response?.data?.mensagem || 'Erro ao fazer login. Verifique suas credenciais.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* Estilos específicos do componente, se necessário */
</style>
