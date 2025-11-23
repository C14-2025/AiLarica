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

      <!-- Toggle entre Usuário e Restaurante -->
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
        <!-- Campos Comuns -->
        <div>
          <label for="name" class="block text-sm font-medium text-gray-700">
            Nome Completo
          </label>
          <div class="mt-1">
            <input
              id="name"
              name="name"
              type="text"
              autocomplete="name"
              required
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
import { ref } from 'vue'

const userType = ref<'user' | 'restaurant'>('user')

const handleRegister = () => {
  console.log(`Tentativa de cadastro como ${userType.value}`)
  // Lógica de cadastro (apenas frontend, então apenas log)
}
</script>

<style scoped>
/* Estilos específicos do componente, se necessário */
</style>
