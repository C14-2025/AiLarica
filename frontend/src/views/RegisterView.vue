<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="max-w-md w-full bg-white p-8 rounded-xl shadow-2xl">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-extrabold text-gray-900">
          Crie sua conta no AiLarica
        </h1>
        <p class="mt-2 text-sm text-gray-600">
          Preencha os dados abaixo
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

      <form class="space-y-6" @submit.prevent="handleRegister">

        <div v-if="userType === 'user'">
          <label for="full-name" class="block text-sm font-medium text-gray-700">
            Nome Completo
          </label>
          <div class="mt-1">
            <input
              id="full-name"
              name="full-name"
              type="text"
              autocomplete="name"
              required
              v-model="fullName"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div v-if="userType === 'user'">
          <label for="cpf" class="block text-sm font-medium text-gray-700">
            CPF
          </label>
          <div class="mt-1">
            <input
              id="cpf"
              name="cpf"
              type="text"
              required
              v-model="cpf"
              placeholder="Ex: 000.000.000-00"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div v-if="userType === 'restaurant'">
          <label for="restaurant-name" class="block text-sm font-medium text-gray-700">
            Nome do Restaurante
          </label>
          <div class="mt-1">
            <input
              id="restaurant-name"
              name="restaurant-name"
              type="text"
              required
              v-model="restaurantName"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div v-if="userType === 'restaurant'">
          <label for="cnpj" class="block text-sm font-medium text-gray-700">
            CNPJ
          </label>
          <div class="mt-1">
            <input
              id="cnpj"
              name="cnpj"
              type="text"
              required
              v-model="cnpj"
              placeholder="Ex: 00.000.000/0001-00"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div>
          <label for="address" class="block text-sm font-medium text-gray-700">
            Endereço Completo
          </label>
          <div class="mt-1">
            <input
              id="address"
              name="address"
              type="text"
              required
              v-model="address"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

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
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
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
              autocomplete="new-password"
              required
              v-model="password"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div>
          <label for="confirm-password" class="block text-sm font-medium text-gray-700">
            Confirmar Senha
          </label>
          <div class="mt-1">
            <input
              id="confirm-password"
              name="confirm-password"
              type="password"
              required
              v-model="confirmPassword"
              class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-orange-500 focus:border-orange-500 sm:text-sm"
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-orange-600 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500 transition duration-150 ease-in-out"
          >
            Cadastrar como {{ userType === 'user' ? 'Usuário' : 'Restaurante' }}
          </button>
        </div>
      </form>

      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          Já tem uma conta?
          <a href="/login" class="font-medium text-orange-600 hover:text-orange-500">
            Faça login
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AuthService from '@/services/authService';
import { useRouter } from 'vue-router';

const router = useRouter();

// --- Variáveis Reativas para Coleta de Dados ---
const userType = ref<'user' | 'restaurant'>('user');
const fullName = ref('');
const restaurantName = ref('');
const cnpj = ref('');
const cpf = ref(''); // ✅ NOVO REF PARA CPF
const address = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');

const handleRegister = async () => {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = "As senhas não coincidem!";
    alert(errorMessage.value);
    return;
  }

  // Validação do CNPJ (Restaurante)
  if (userType.value === 'restaurant' && !cnpj.value) {
    errorMessage.value = "CNPJ é obrigatório para cadastro de restaurante.";
    alert(errorMessage.value);
    return;
  }

  // ✅ Validação do CPF (Usuário)
  if (userType.value === 'user' && !cpf.value) {
    errorMessage.value = "CPF é obrigatório para cadastro de usuário.";
    alert(errorMessage.value);
    return;
  }

  // Validação do Endereço (Comum)
  if (!address.value) {
    errorMessage.value = "Endereço é obrigatório!";
    alert(errorMessage.value);
    return;
  }

  errorMessage.value = '';

  try {
    const isRestaurant = userType.value === 'restaurant';

    const basePayload = {
      email: email.value,
      senha: password.value,
      endereco: address.value,
    };

    let finalPayload;

    if (isRestaurant) {
      // Payload do Restaurante
      finalPayload = {
        ...basePayload,
        nome: restaurantName.value,
        cnpj: cnpj.value,
        descricao: 'Novo restaurante AiLarica',
        telefone: '(11) 90000-0000',
        avaliacao: 5.0,
        fotoPerfil: 'url_default.jpg',
        // Horários Padrão (Obrigatório pelo DTO do Backend)
        horarios: {
          horariosAbertura: [1000, 1000, 1000, 1000, 1000, 1000, 1000],
          horariosFechamento: [2200, 2200, 2200, 2200, 2200, 2200, 2200]
        }
      };
    } else {
      // Payload do Usuário
      finalPayload = {
        ...basePayload,
        nome: fullName.value,
        cpf: cpf.value, // ✅ CPF ADICIONADO AQUI
      };
    }

    // O AuthService usará o campo 'tipo' para determinar o endpoint.
    const response = await AuthService.register({
      ...finalPayload,
      tipo: isRestaurant ? 'RESTAURANTE' : 'USUARIO'
    });

    console.log("Cadastro bem-sucedido!", response);

    // Assumimos que o backend envia o código de ativação por email,
    // mas o fluxo de confirmação é iniciado após esta resposta de sucesso.
    alert("Cadastro realizado! Você deve confirmar sua conta por email antes de fazer login.");

    router.push('/login');

  } catch (error) {
    errorMessage.value = 'Falha no cadastro. Verifique os dados.';
    alert(errorMessage.value);
    console.error("Erro no cadastro:", error);
  }
}
</script>

<style scoped>
/* Estilos específicos do componente, se necessário */
</style>
