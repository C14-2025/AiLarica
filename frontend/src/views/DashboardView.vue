<template>
<<<<<<< HEAD
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 custom-dashboard-bg">
    
=======
  <div class="min-h-screen bg-gray-200 dark:bg-gray-200 custom-dashboard-bg">

>>>>>>> 6e3a98edcfe924e3c01aca0d6e4dcadd39ed48f5
    <header class="bg-red-600 shadow-xl shadow-red-600/30 sticky top-0 z-50 custom-slide-down">

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 sm:py-6 flex items-center justify-between">
        <div class="flex items-center space-x-4 sm:space-x-6">

          <div class="relative flex items-center justify-center custom-logo-pulse">
            <div class="absolute w-14 h-14 border-3 border-white/50 rounded-full custom-pulse-ring"></div>
            <div class="w-12 h-12 bg-white rounded-full flex items-center justify-center text-2xl shadow-md transition-transform duration-300 hover:rotate-12 hover:scale-105">
              🍽️
            </div>
          </div>
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Dashboard</h1>
        </div>

<<<<<<< HEAD
          <div class="flex items-center space-x-4 sm:space-x-6">
            
            <div class="relative text-3xl text-white cursor-pointer transition-transform duration-200 hover:scale-110 custom-shake">
              <span class="drop-shadow-md">🔔</span>
              <span class="absolute -top-1 -right-1 bg-yellow-400 text-red-700 text-xs font-extrabold w-5 h-5 rounded-full flex items-center justify-center border-2 border-white custom-bounce">3</span>
=======
        <div class="flex items-center space-x-4 sm:space-x-6">

          <div class="relative text-3xl text-white cursor-pointer transition-transform duration-200 hover:scale-110 custom-shake">
            <span class="drop-shadow-md">🔔</span>
            <span class="absolute -top-1 -right-1 bg-yellow-400 text-red-700 text-xs font-extrabold w-5 h-5 rounded-full flex items-center justify-center border-2 border-white custom-bounce">3</span>
          </div>

          <div class="flex items-center space-x-3 bg-white/15 p-2 sm:px-4 sm:py-2 rounded-full backdrop-blur-sm cursor-pointer transition-all duration-300 hover:bg-white/25 hover:-translate-y-0.5">
            <div class="w-11 h-11 bg-linear-to-br from-yellow-300 to-yellow-500 rounded-full flex items-center justify-center font-extrabold text-red-600 text-base border-2 border-white shadow-lg">
              <span>{{ restaurantData.name.substring(0, 2).toUpperCase() }}</span>
            </div>
            <div class="hidden sm:flex flex-col text-sm">
              <span class="text-white font-bold">{{ restaurantData.name }}</span>
              <span class="text-white/90 text-xs font-medium">● Online</span>
>>>>>>> 6e3a98edcfe924e3c01aca0d6e4dcadd39ed48f5
            </div>
            
            <UserMenu
              :restaurantName="restaurantData.name"
              :restaurantEmail="restaurantData.email"
            />
          </div>
      </div>
    </header>

    <div v-if="isLoading" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
      <svg class="animate-spin h-10 w-10 text-red-600 mx-auto" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <p class="mt-4 text-gray-700 font-semibold">Carregando dados do Dashboard...</p>
    </div>

    <main v-else class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-8">
        <StatsCard
          title="Pedidos Hoje"
          :value="stats.ordersToday"
          icon="package"
          :trend="15"
          bgColor="bg-red-gradient"
        />
        <StatsCard
          title="Faturamento"
          :value="`R$ ${stats.revenueToday.toFixed(2)}`"
          icon="currency"
          :trend="8"
          bgColor="bg-green-gradient"
        />
        <!-- ✅ CORREÇÃO 1: StatsCard - Usando String() na prop value -->
        <StatsCard
          title="Tempo Médio"
          :value="String(stats.avgTime)"
          icon="clock"
          bgColor="bg-yellow-gradient"
        />
        <StatsCard
          title="Avaliação"
          :value="stats.rating"
          icon="star"
          bgColor="bg-purple-gradient"
        />
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 lg:gap-8 xl:grid-cols-12">
        <div class="flex flex-col space-y-6 lg:col-span-2 xl:col-span-8">
          <div class="custom-card-animated" style="--animation-delay: 0.1s;">
            <RestaurantStatus
              :restaurantName="restaurantData.name"
              :isOpen="restaurantData.isOpen"
              :avgTime="String(stats.avgTime)"
            :acceptanceRate="stats.acceptanceRate"
            @toggleStatus="toggleRestaurantStatus"
            />
          </div>

          <div class="custom-card-animated" style="--animation-delay: 0.2s;">
            <SalesChart :data="salesData" />
          </div>

          <div class="custom-card-animated" style="--animation-delay: 0.3s;">
            <MenuOverview
              :items="menuItems"
              @viewAll="goToCardapio"
            />
          </div>
        </div>

        <div class="flex flex-col space-y-6 mt-6 lg:mt-0 lg:col-span-1 xl:col-span-4">
          <div class="custom-card-animated" style="--animation-delay: 0.4s;">
            <RecentOrders
              :orders="recentOrders"
              @viewAll="goToPedidos"
            />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
/*
  Requer que você configure 'custom-dashboard-bg', 'custom-slide-down',
  'custom-logo-pulse', 'custom-pulse-ring', 'custom-shake', 'custom-bounce',
  'custom-card-animated' e classes como 'bg-red-gradient' no seu tailwind.config.js
  ou via CSS padrão, pois o Tailwind não suporta nativamente todas essas animações e gradientes complexos.
*/

/* 1. Fundo Animado do Container */
.custom-dashboard-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f5 0%, #fff9f0 50%, #f5fef8 100%);
  background-size: 200% 200%; /* Ajuste para a animação */
  animation: gradientShift 10s ease infinite alternate;
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  100% { background-position: 100% 50%; }
}

/* 2. Animação de Entrada do Header */
.custom-slide-down {
  animation: slideDown 0.5s ease;
}

@keyframes slideDown {
  from { transform: translateY(-100%); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

/* 3. Animações de Logo, Notificação e Card */
/* Animação do logo pulse */
.custom-pulse-ring {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0; }
}

/* Animação de shake da notificação */
.custom-shake:hover {
  animation: shake 0.5s ease;
}

@keyframes shake {
  0%, 100% { transform: rotate(0deg) scale(1.1); }
  25% { transform: rotate(-8deg) scale(1.1); }
  75% { transform: rotate(8deg) scale(1.1); }
}

/* Animação de bounce da badge count */
.custom-bounce {
  animation: bounce 2s ease infinite;
}

@keyframes bounce {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.2); }
}

/* Animação de entrada dos cards */
.custom-card-animated {
  animation: fadeInUp 0.6s ease backwards;
  animation-delay: var(--animation-delay, 0s); /* Usa variável CSS para delay */
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

</style>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import AuthService from '@/services/authService';
import StatsCard from '@/views/StatsCard.vue';
import RestaurantStatus from '@/views/RestaurantStatus.vue';
import SalesChart from '@/views/SalesChart.vue';
import MenuOverview from '@/views/MenuOverview.vue';
import RecentOrders from '@/views/RecentOrders.vue';
import UserMenu from '@/views/UserMenu.vue';

const router = useRouter();

const API_BASE_URL = 'http://localhost:8080';

// --- Auth Check e Dados do Usuário ---
const token = AuthService.getToken();
const currentUser = AuthService.getCurrentUser();

// Verifica autenticação e tipo de usuário
if (!token || currentUser?.tipo !== 'RESTAURANTE') {
  AuthService.logout();
  // Se a autenticação falhar, saia imediatamente e evite processar o restante do script
}

const RESTAURANT_ID_FROM_TOKEN = currentUser?.id || 0;
const headers = { 'Authorization': `Bearer ${token}` };

// --- Variáveis de Estado ---
const isLoading = ref(true); // ✅ NOVO: Estado de Carregamento

const restaurantData = ref({
<<<<<<< HEAD
  name: 'Carregando...',
  email: 'email@example.com',
=======
  name: currentUser?.nome || 'Carregando...',
>>>>>>> 6e3a98edcfe924e3c01aca0d6e4dcadd39ed48f5
  isOpen: false
});

const stats = ref({
  ordersToday: 0,
  revenueToday: 0.00,
  avgTime: '0 min', // Formato de string do DTO
  rating: 0.0,
  acceptanceRate: 95
});

const salesData = ref<{ label: string; value: number }[]>([]);
const menuItems = ref<any[]>([]);
const recentOrders = ref<any[]>([]);


// --- Funções de Navegação ---
const goToPedidos = () => {
  router.push('/restaurante/pedidos');
};

const goToCardapio = () => {
  router.push('/restaurante/cardapio');
};


// --- Funções de Carregamento de Dados ---
const fetchMetricsAndData = async () => {
  isLoading.value = true; // Inicia o carregamento

  // 1. Fetch DTO CENTRALIZADO e Dados do Restaurante
  try {
<<<<<<< HEAD
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}`);
    const data = response.data;
    restaurantData.value.name = data.nome;
    restaurantData.value.email = data.email || 'email@example.com';
    restaurantData.value.isOpen = data.ativo;
    localStorage.setItem('restaurantId', data.idRestaurante?.toString() || RESTAURANT_ID.toString());
    // Adicionar lógica para carregar estatísticas reais se houver endpoint
    // Por enquanto, mantemos as estatísticas mockadas ou ajustamos para 0
=======
    const dashResponse = await axios.get(`${API_BASE_URL}/painel-restaurante/dashboard`, { headers });
    const data = dashResponse.data;

    // Atualiza Stats Card
    stats.value.ordersToday = data.pedidosHoje || 0;
    stats.value.revenueToday = data.faturamentoHoje || 0.00;
    stats.value.rating = data.avaliacaoMedia || 0.0;
    stats.value.avgTime = data.tempoMedio || '0 min';

    // Atualiza Gráfico
    salesData.value = data.vendasSemanais?.map((item: any) => ({
      label: item.dia.substring(5),
      value: item.total
    })) || []; // Garante array vazio se for nulo

    // O nome e status ativo vêm de outra rota (mais pública, mas necessária)
    const restResponse = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID_FROM_TOKEN}`);
    restaurantData.value.name = restResponse.data.nome;
    restaurantData.value.isOpen = restResponse.data.ativo;

>>>>>>> 6e3a98edcfe924e3c01aca0d6e4dcadd39ed48f5
  } catch (error) {
    console.error('Erro ao buscar dados do dashboard:', error);
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      AuthService.logout();
    }
  }

  // 2. Fetch Pedidos Recentes
  try {
    const response = await axios.get(`${API_BASE_URL}/painel-restaurante/pedidos-ativos`, { headers });
    recentOrders.value = response.data.slice(0, 3).map((pedido: any) => ({
      id: pedido.idPedido?.toString(),
      customerName: 'Cliente #' + pedido.idUsuario,
      total: pedido.valorTotal,
      time: pedido.dataHora.split('T')[1].substring(0, 5),
      status: pedido.status || 'pendente',
    }));
  } catch (error) {
    console.error('Erro ao buscar pedidos recentes:', error);
  }

  // 3. Fetch Itens do Cardápio
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID_FROM_TOKEN}/pratos`);
    menuItems.value = response.data.slice(0, 5).map((prato: any) => ({
      id: prato.idPrato?.toString(),
      name: prato.nome,
      price: prato.preco,
    }));
  } catch (error) {
    console.error('Erro ao buscar itens do cardápio:', error);
  }

  isLoading.value = false; // Finaliza o carregamento, garantindo a renderização
};

const toggleRestaurantStatus = async () => {
  if (!token) return;

  try {
    await axios.put(`${API_BASE_URL}/restaurantes/me/funcionamento`, {}, { headers });

    restaurantData.value.isOpen = !restaurantData.value.isOpen;

    // Recarrega o dashboard para atualizar o status global
    fetchMetricsAndData();

  } catch (error) {
    console.error('Erro ao alternar status do restaurante:', error);
    alert('Erro ao alternar status do restaurante.');
  }
};


// --- Chamada de Dados ao Montar o Componente ---
onMounted(() => {
  if (token) {
    fetchMetricsAndData();
  }
});
</script>
