<template>
  <div class="relative">
    <!-- Botão do Menu -->
    <div
      @click="toggleMenu"
      class="flex items-center space-x-3 bg-white/15 p-2 sm:px-4 sm:py-2 rounded-full backdrop-blur-sm cursor-pointer transition-all duration-300 hover:bg-white/25 hover:-translate-y-0.5"
    >
      <div class="w-11 h-11 bg-gradient-to-br from-yellow-300 to-yellow-500 rounded-full flex items-center justify-center font-extrabold text-red-600 text-base border-2 border-white shadow-lg">
        <span>{{ restaurantInitials }}</span>
      </div>
      <div class="hidden sm:flex flex-col text-sm">
        <span class="text-white font-bold">{{ restaurantName }}</span>
        <span class="text-white/90 text-xs font-medium">● Online</span>
      </div>
      <svg
        v-if="isOpen"
        class="w-4 h-4 text-white transition-transform"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7-7m0 0L5 14m7-7v12" />
      </svg>
      <svg
        v-else
        class="w-4 h-4 text-white transition-transform"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7 7 7-7" />
      </svg>
    </div>

    <!-- Menu Dropdown -->
    <transition
      enter-active-class="transition ease-out duration-100"
      enter-from-class="transform opacity-0 scale-95"
      enter-to-class="transform opacity-100 scale-100"
      leave-active-class="transition ease-in duration-75"
      leave-from-class="transform opacity-100 scale-100"
      leave-to-class="transform opacity-0 scale-95"
    >
      <div
        v-if="isOpen"
        class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-xl z-50 overflow-hidden"
      >
        <!-- Cabeçalho do Menu -->
        <div class="bg-red-600 text-white px-4 py-3">
          <p class="font-semibold text-sm">{{ restaurantName }}</p>
          <p class="text-xs text-red-100">{{ restaurantEmail }}</p>
        </div>

        <!-- Opções do Menu -->
        <div class="py-2">
          <!-- Configurações -->
          <button
            @click="goToSettings"
            class="w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-150 flex items-center gap-3"
          >
            <span class="text-lg">⚙️</span>
            <span>Configurações</span>
          </button>

          <!-- Perfil -->
          <button
            @click="goToProfile"
            class="w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-150 flex items-center gap-3"
          >
            <span class="text-lg">👤</span>
            <span>Meu Perfil</span>
          </button>

          <!-- Divisor -->
          <div class="my-2 border-t border-gray-200"></div>

          <!-- Logoff -->
          <button
            @click="handleLogoff"
            class="w-full px-4 py-2 text-left text-sm text-red-600 hover:bg-red-50 transition-colors duration-150 flex items-center gap-3 font-medium"
          >
            <span class="text-lg">🚪</span>
            <span>Sair</span>
          </button>
        </div>
      </div>
    </transition>

    <!-- Overlay para fechar o menu ao clicar fora -->
    <div
      v-if="isOpen"
      @click="isOpen = false"
      class="fixed inset-0 z-40"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

interface Props {
  restaurantName: string
  restaurantEmail: string
}

const props = withDefaults(defineProps<Props>(), {
  restaurantName: 'Restaurante',
  restaurantEmail: 'email@example.com'
})

const router = useRouter()
const isOpen = ref(false)

const restaurantInitials = computed(() => {
  return props.restaurantName.substring(0, 2).toUpperCase()
})

const toggleMenu = () => {
  isOpen.value = !isOpen.value
}

const goToSettings = () => {
  isOpen.value = false
  router.push('/restaurante/settings')
}

const goToProfile = () => {
  isOpen.value = false
  router.push('/restaurante/profile')
}

const goToSupport = () => {
  isOpen.value = false
  // Você pode abrir um link de suporte ou uma página de suporte
  window.open('https://support.ailarica.com', '_blank')
}

const handleLogoff = async () => {
  isOpen.value = false
  
  // Limpar dados do localStorage
  localStorage.removeItem('authToken')
  localStorage.removeItem('userId')
  localStorage.removeItem('userType')
  localStorage.removeItem('userEmail')
  localStorage.removeItem('restaurantId')
  
  // Redirecionar para o login
  router.push('/login')
}
</script>

<style scoped>
/* Estilos específicos do componente */
</style>
