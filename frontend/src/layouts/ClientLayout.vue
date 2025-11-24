<template>
  <div class="min-h-screen bg-gray-50 flex flex-col">
    <!-- Header/NavBar -->
    <header class="bg-gradient-to-r from-red-600 to-red-500 text-white shadow-lg sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div class="flex justify-between items-center">
          <!-- Logo -->
          <router-link to="/usuario/home" class="flex items-center space-x-3 hover:opacity-80 transition-opacity">
            <div class="w-12 h-12 bg-white rounded-full flex items-center justify-center text-red-600 font-bold text-xl shadow-md">
              A
            </div>
            <span class="text-2xl font-bold hidden sm:inline">AiLarica</span>
          </router-link>

          <!-- Navigation Menu -->
          <nav class="flex items-center space-x-2 sm:space-x-4">
            <!-- Carrinho -->
            <router-link
              to="/carrinho"
              class="relative flex items-center space-x-1 hover:bg-red-700 px-3 py-2 rounded-lg transition-all duration-200 group"
              :class="{ 'bg-red-700': isActive('Carrinho') }"
            >
              <svg class="w-6 h-6 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"></path>
              </svg>
              <span class="hidden sm:inline text-sm font-medium">Carrinho</span>
              <span v-if="cartCount > 0" class="absolute top-1 right-1 bg-yellow-400 text-red-600 rounded-full w-5 h-5 flex items-center justify-center text-xs font-bold">
                {{ cartCount }}
              </span>
            </router-link>

            <!-- Pedidos -->
            <router-link
              to="/pedidos"
              class="flex items-center space-x-1 hover:bg-red-700 px-3 py-2 rounded-lg transition-all duration-200 group"
              :class="{ 'bg-red-700': isActive('MeusPedidos') }"
            >
              <svg class="w-6 h-6 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
              </svg>
              <span class="hidden sm:inline text-sm font-medium">Pedidos</span>
            </router-link>

            <!-- Pagamentos -->
            <router-link
              to="/pagamentos"
              class="flex items-center space-x-1 hover:bg-red-700 px-3 py-2 rounded-lg transition-all duration-200 group"
              :class="{ 'bg-red-700': isActive('Pagamentos') }"
            >
              <svg class="w-6 h-6 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M3 14h18m-9-4v8m-7 0h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
              </svg>
              <span class="hidden sm:inline text-sm font-medium">Pagamentos</span>
            </router-link>

            <!-- Perfil -->
            <router-link
              to="/perfil"
              class="flex items-center space-x-1 hover:bg-red-700 px-3 py-2 rounded-lg transition-all duration-200 group"
              :class="{ 'bg-red-700': isActive('Perfil') }"
            >
              <svg class="w-6 h-6 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
              </svg>
              <span class="hidden sm:inline text-sm font-medium">Perfil</span>
            </router-link>

            <!-- Logout Button -->
            <button
              @click="logout"
              class="flex items-center space-x-1 hover:bg-red-700 px-3 py-2 rounded-lg transition-all duration-200 group ml-2 border-l border-red-400"
            >
              <svg class="w-6 h-6 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
              </svg>
              <span class="hidden sm:inline text-sm font-medium">Sair</span>
            </button>
          </nav>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <slot></slot>
    </main>

    <!-- Footer -->
    <footer class="bg-gray-900 text-gray-300 mt-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">
          <!-- Sobre -->
          <div>
            <h3 class="text-white font-bold mb-4">Sobre AiLarica</h3>
            <p class="text-sm">Sua plataforma de delivery favorita, conectando você aos melhores restaurantes da região.</p>
          </div>
          <!-- Links Úteis -->
          <div>
            <h3 class="text-white font-bold mb-4">Links Úteis</h3>
            <ul class="text-sm space-y-2">
              <li><router-link to="/usuario/home" class="hover:text-white transition-colors">Home</router-link></li>
              <li><router-link to="/pedidos" class="hover:text-white transition-colors">Meus Pedidos</router-link></li>
              <li><router-link to="/perfil" class="hover:text-white transition-colors">Perfil</router-link></li>
            </ul>
          </div>
          <!-- Contato -->
          <div>
            <h3 class="text-white font-bold mb-4">Contato</h3>
            <p class="text-sm">Email: contato@ailarica.com</p>
            <p class="text-sm">Telefone: (11) 9999-9999</p>
          </div>
        </div>
        <div class="border-t border-gray-700 pt-8 text-center text-sm">
          <p>&copy; 2025 AiLarica. Todos os direitos reservados.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const cartCount = ref(2)

const isActive = (routeName: string) => {
  return route.name === routeName
}

const logout = () => {
  if (confirm('Tem certeza que deseja sair?')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userType')
    localStorage.removeItem('userEmail')
    router.push({ name: 'login' })
  }
}
</script>

<style scoped>
/* Estilos específicos */
</style>
