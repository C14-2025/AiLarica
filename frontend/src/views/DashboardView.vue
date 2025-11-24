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
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Dashboard</h1>
        </div>

        <div class="flex items-center space-x-4 sm:space-x-6">
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
          :value="String(`R$ ${stats.revenueToday.toFixed(2)}`)"
          icon="currency"
          :trend="8"
          bgColor="bg-green-gradient"
        />

        <StatsCard
          title="Tempo Médio"
          :value="String(stats.avgTime)"
          icon="clock"
          bgColor="bg-yellow-gradient"
        />

        <StatsCard
          title="Avaliação"
          :value="String(stats.rating)"
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
/* estilo igual ao seu, removido por brevidade */
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

// --- Auth Check ---
const token = AuthService.getToken();
const currentUser = AuthService.getCurrentUser();

if (!token || currentUser?.tipo !== 'RESTAURANTE') {
  AuthService.logout();
}

const RESTAURANT_ID_FROM_TOKEN = currentUser?.id || 0;

const headers = { 'Authorization': `Bearer ${token}` };

const isLoading = ref(true);

const restaurantData = ref({
  name: currentUser?.nome || 'Carregando...',
  email: currentUser?.email || 'email@example.com',
  isOpen: false
});

const stats = ref({
  ordersToday: 0,
  revenueToday: 0.00,
  avgTime: '0 min',
  rating: 0.0,
  acceptanceRate: 95
});

const salesData = ref([]);
const menuItems = ref([]);
const recentOrders = ref([]);

// Navegação
const goToPedidos = () => router.push('/restaurante/pedidos');
const goToCardapio = () => router.push('/restaurante/cardapio');

// --- Carregar dados ---
const fetchMetricsAndData = async () => {
  isLoading.value = true;

  try {
    const dashResponse = await axios.get(`${API_BASE_URL}/painel-restaurante/dashboard`, { headers });
    const data = dashResponse.data;

    stats.value.ordersToday = data.pedidosHoje || 0;

    stats.value.revenueToday = Number(data.faturamentoHoje) || 0.00;
    stats.value.rating = Number(data.avaliacaoMedia) || 0.0;

    stats.value.avgTime = data.tempoMedio || '0 min';

    salesData.value = data.vendasSemanais?.map((item: any) => ({
      label: item.dia.substring(5),
      value: item.total
    })) || [];

    const restResponse = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID_FROM_TOKEN}`);
    restaurantData.value.name = restResponse.data.nome;
    restaurantData.value.isOpen = restResponse.data.ativo;

  } catch (error) {
    console.error('Erro ao buscar dashboard:', error);
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      AuthService.logout();
    }
  }

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

  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID_FROM_TOKEN}/pratos`);
    menuItems.value = response.data.slice(0, 5).map((prato: any) => ({
      id: prato.idPrato?.toString(),
      name: prato.nome,
      price: prato.preco,
    }));
  } catch (error) {
    console.error('Erro ao buscar cardápio:', error);
  }

  isLoading.value = false;
};

// Alternar status
const toggleRestaurantStatus = async () => {
  if (!token) return;

  try {
    await axios.put(`${API_BASE_URL}/restaurantes/me/funcionamento`, {}, { headers });

    restaurantData.value.isOpen = !restaurantData.value.isOpen;

    fetchMetricsAndData();
  } catch (error) {
    console.error('Erro ao alternar status:', error);
    alert('Erro ao alternar status do restaurante.');
  }
};

onMounted(() => {
  if (token) fetchMetricsAndData();
});
</script>
