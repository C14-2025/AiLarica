<template>
  <div class="min-h-screen bg-gray-50">

    <header class="bg-orange-500 shadow-lg sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">

        <div class="flex items-center gap-3">
          <div class="text-3xl">🍔</div>
          <h1 class="text-2xl font-bold text-white">AiLarica <span class="text-sm font-normal opacity-90">Delivery</span></h1>
        </div>

        <div class="flex items-center gap-6">
          <router-link to="/usuario/pedidos" class="text-white font-medium hover:text-orange-100 transition flex items-center gap-2">
            📄 Meus Pedidos
          </router-link>

          <div class="relative">
            <button
              @click="toggleDropdown"
              class="flex items-center gap-3 pl-6 border-l border-orange-400 focus:outline-none"
            >
              <div class="text-right hidden sm:block">
                <span class="block text-white font-bold text-sm">{{ userName }}</span>
                <span class="block text-orange-200 text-xs">Cliente VIP</span>
              </div>
              <div class="w-10 h-10 bg-white rounded-full flex items-center justify-center text-orange-500 font-bold text-lg shadow-sm border-2 border-orange-300">
                {{ userName.charAt(0).toUpperCase() }}
              </div>
              <span class="text-white text-xs">▼</span>
            </button>

            <div
              v-if="showDropdown"
              class="absolute right-0 mt-2 w-48 bg-white rounded-xl shadow-xl py-2 z-50 border border-gray-100 transform origin-top-right transition-all duration-200"
              @mouseleave="showDropdown = false"
            >
              <div class="px-4 py-2 border-b border-gray-100">
                <p class="text-xs text-gray-500">Logado como</p>
                <p class="text-sm font-bold text-gray-900 truncate">{{ userEmail }}</p>
              </div>

              <router-link
                to="/usuario/perfil"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-orange-50 hover:text-orange-600 transition-colors"
              >
                👤 Meu Perfil
              </router-link>

              <router-link
                to="/usuario/pedidos"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-orange-50 hover:text-orange-600 transition-colors"
              >
                🛍️ Meus Pedidos
              </router-link>

              <div class="border-t border-gray-100 mt-1 pt-1">
                <button
                  @click="logout"
                  class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
                >
                  🚪 Sair
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h2 class="text-2xl font-bold text-gray-800 mb-6">Restaurantes Disponíveis</h2>

      <div v-if="loading" class="text-center py-20">
        <div class="animate-spin h-10 w-10 border-4 border-orange-500 border-t-transparent rounded-full mx-auto"></div>
        <p class="mt-4 text-gray-500">Buscando os melhores sabores...</p>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="restaurante in restaurantes"
          :key="restaurante.idRestaurante"
          class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 overflow-hidden cursor-pointer group"
          @click="verCardapio(restaurante.idRestaurante)"
        >
          <div class="h-40 bg-gray-200 relative">
            <img
              v-if="restaurante.fotoPerfil && restaurante.fotoPerfil !== 'url_default.jpg'"
              :src="restaurante.fotoPerfil"
              class="w-full h-full object-cover"
              alt="Foto Restaurante"
            />
            <div v-else class="w-full h-full flex items-center justify-center bg-orange-100 text-4xl">
              🏪
            </div>
            <div class="absolute top-3 right-3 bg-white px-2 py-1 rounded-lg shadow-sm flex items-center gap-1 text-xs font-bold text-gray-800">
              ⭐ {{ restaurante.avaliacao }}
            </div>
          </div>

          <div class="p-5">
            <div class="flex justify-between items-start mb-2">
              <h3 class="text-xl font-bold text-gray-900 group-hover:text-orange-600 transition-colors">
                {{ restaurante.nome }}
              </h3>
              <span
                :class="[
                  'px-2 py-0.5 rounded text-xs font-bold uppercase',
                  restaurante.ativo ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                ]"
              >
                {{ restaurante.ativo ? 'Aberto' : 'Fechado' }}
              </span>
            </div>
            <p class="text-gray-600 text-sm mb-4 line-clamp-2 h-10">
              {{ restaurante.descricao }}
            </p>
            <div class="flex items-center text-xs text-gray-500 gap-4 border-t border-gray-100 pt-3">
              <span class="flex items-center gap-1">
                🕒 {{ restaurante.tempoMedioEntrega || '40-50 min' }}
              </span>
              <span class="flex items-center gap-1">
                🛵 Entrega Grátis
              </span>
            </div>
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
import { useRouter } from 'vue-router';

// Interfaces
interface Restaurante {
  idRestaurante: number;
  nome: string;
  descricao: string;
  avaliacao: number;
  ativo: boolean;
  fotoPerfil: string;
  tempoMedioEntrega: string;
}

const router = useRouter();
const API_BASE_URL = 'http://localhost:8080';

const restaurantes = ref<Restaurante[]>([]);
const loading = ref(true);
const userName = ref('');
const userEmail = ref('');
const showDropdown = ref(false); // Controle do Menu

// --- Auth Check ---
const currentUser = AuthService.getCurrentUser();
if (currentUser && currentUser.tipo === 'USUARIO') {
  userName.value = currentUser.nome;
  userEmail.value = currentUser.email;
} else {
  AuthService.logout();
}

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const fetchRestaurantes = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes`);
    restaurantes.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar restaurantes:', error);
  } finally {
    loading.value = false;
  }
};

// ✅ CORREÇÃO AQUI: Navegação direta sem alerta
const verCardapio = (id: number) => {
  router.push(`/usuario/restaurante/${id}`);
};

const logout = () => {
  AuthService.logout();
};

onMounted(() => {
  fetchRestaurantes();
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
