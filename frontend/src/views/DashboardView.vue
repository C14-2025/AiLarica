<template>
  <div class="min-h-screen bg-gray-200 dark:bg-gray-200 custom-dashboard-bg">
    
    <header class="bg-red-600 shadow-xl shadow-red-600/30 sticky top-0 z-50 custom-slide-down">
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 sm:py-6 flex items-center justify-between">
        <div class="flex items-center space-x-4 sm:space-x-6">
          
          <div class="relative flex items-center justify-center custom-logo-pulse">
            <div class="absolute w-14 h-14 border-3 border-white/50 rounded-full custom-pulse-ring"></div>
            <div class="w-12 h-12 bg-white rounded-full flex items-center justify-center text-2xl shadow-md transition-transform duration-300 hover:rotate-12 hover:scale-105">
              🍽️
            </div>
          </div>
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Painel de Parceiros</h1>
        </div>

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
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
      
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
        <StatsCard
          title="Tempo Médio"
          :value="`${stats.avgTime} min`"
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
              :avgTime="stats.avgTime"
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
import axios from 'axios'; // Assumindo que axios está disponível ou será instalado

import { useRouter } from 'vue-router';
import StatsCard from '@/views/StatsCard.vue';
import RestaurantStatus from '@/views/RestaurantStatus.vue';
import SalesChart from '@/views/SalesChart.vue';
import MenuOverview from '@/views/MenuOverview.vue';
import RecentOrders from '@/views/RecentOrders.vue';

const router = useRouter();

const RESTAURANT_ID = 1; // ID do restaurante fixo para teste
const API_BASE_URL = 'http://localhost:8080'; // URL base do backend (ajustar conforme necessário)

const restaurantData = ref({
  name: 'Carregando...',
  isOpen: false
});


const stats = ref({
  ordersToday: 0,
  revenueToday: 0.00,
  avgTime: 0,
  rating: 0.0,
  acceptanceRate: 0
});


const salesData = ref<{ label: string; value: number }[]>([]);


const menuItems = ref([]);


const recentOrders = ref([]);


const toggleRestaurantStatus = async () => {
  try {
    const response = await axios.put(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/alternar`);
    console.log(response.data);
    // Atualiza o status localmente após sucesso
    restaurantData.value.isOpen = !restaurantData.value.isOpen;
  } catch (error) {
    console.error('Erro ao alternar status do restaurante:', error);
    alert('Erro ao alternar status do restaurante.');
  }
};

const goToPedidos = () => {
  router.push('/restaurante/pedidos');
};

const goToCardapio = () => {
  router.push('/restaurante/cardapio');
};

// --- Funções de Carregamento de Dados ---

const fetchRestaurantData = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}`);
    const data = response.data;
    restaurantData.value.name = data.nome;
    restaurantData.value.isOpen = data.ativo;
    // Adicionar lógica para carregar estatísticas reais se houver endpoint
    // Por enquanto, mantemos as estatísticas mockadas ou ajustamos para 0
  } catch (error) {
    console.error('Erro ao buscar dados do restaurante:', error);
  }
};

const fetchMenuItems = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pratos`);
    menuItems.value = response.data.map((prato: any) => ({
      id: prato.id.toString(),
      name: prato.nome,
      description: prato.descricao,
      price: prato.preco,
      available: prato.ativo,
      salesCount: 0 // Sem dados de vendas no backend, mantemos 0
    }));
  } catch (error) {
    console.error('Erro ao buscar itens do cardápio:', error);
  }
};

const fetchRecentOrders = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pedidos-ativos`);
    // Limitar a 3 pedidos recentes
    recentOrders.value = response.data.slice(0, 3).map((pedido: any) => ({
      id: pedido.idPedido?.toString() || pedido.id?.toString(),
      idPedido: pedido.idPedido || pedido.id,
      customerName: 'Cliente #' + pedido.idUsuario,
      idUsuario: pedido.idUsuario,
      total: pedido.valorTotal,
      valorTotal: pedido.valorTotal,
      time: pedido.dataHora,
      dataHora: pedido.dataHora,
      status: pedido.status || 'pendente',
      items: pedido.itens || [],
      itens: pedido.itens || []
    }));
  } catch (error) {
    console.error('Erro ao buscar pedidos recentes:', error);
    // Dados de fallback para testes
    recentOrders.value = [];
  }
};

// --- Chamada de Dados ao Montar o Componente ---

onMounted(() => {
  fetchRestaurantData();
  fetchMenuItems();
  fetchRecentOrders();
  // Manter dados de stats e salesData mockados por falta de endpoints específicos
  stats.value = {
    ordersToday: 23,
    revenueToday: 1247.50,
    avgTime: 42,
    rating: 4.8,
    acceptanceRate: 95
  };
  salesData.value = [
    { label: 'Seg', value: 450 },
    { label: 'Ter', value: 680 },
    { label: 'Qua', value: 520 },
    { label: 'Qui', value: 780 },
    { label: 'Sex', value: 920 },
    { label: 'Sáb', value: 1150 },
    { label: 'Dom', value: 890 }
  ];
});
</script>