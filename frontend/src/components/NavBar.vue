<template>
  <nav class="bg-ailarica-red text-white shadow-lg sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
      <router-link to="/" class="flex items-center space-x-2 hover:opacity-90 transition-opacity">
        <div class="w-10 h-10 bg-white rounded-full flex items-center justify-center text-ailarica-red font-bold text-lg">
          A
        </div>
        <span class="text-xl font-bold hidden sm:inline">AiLarica</span>
      </router-link>

      <div class="flex items-center space-x-2 sm:space-x-4">
        <router-link
          to="/carrinho"
          class="flex items-center space-x-1 hover:bg-red-600 px-3 py-2 rounded-lg transition-colors relative"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"></path></svg>
          <span class="hidden sm:inline">Carrinho</span>
          <span v-if="cartCount > 0" class="absolute top-0 right-0 bg-white text-ailarica-red rounded-full w-5 h-5 flex items-center justify-center text-xs font-bold">
            {{ cartCount }}
          </span>
        </router-link>

        <router-link
          to="/pedidos"
          class="flex items-center space-x-1 hover:bg-red-600 px-3 py-2 rounded-lg transition-colors"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>
          <span class="hidden sm:inline">Pedidos</span>
        </router-link>

        <router-link
          to="/perfil"
          class="flex items-center space-x-1 hover:bg-red-600 px-3 py-2 rounded-lg transition-colors"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
          <span class="hidden sm:inline">Perfil</span>
        </router-link>

        <button @click="logout" class="hover:bg-red-600 px-3 py-2 rounded-lg transition-colors" title="Sair">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const cartCount = ref(2); // Será substituído por store reativa

const logout = () => {
  if (confirm('Tem certeza que deseja sair?')) {
    localStorage.removeItem('token');
    localStorage.removeItem('userType');
    localStorage.removeItem('userEmail');
    router.push({ name: 'login' });
  }
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
