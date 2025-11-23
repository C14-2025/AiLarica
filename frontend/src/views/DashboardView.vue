<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center gap-4">
            <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
          </div>
          
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
              <span class="text-sm font-semibold text-gray-700">DJ</span>
            </div>
            <span class="text-sm font-medium text-gray-700">{{ restaurantData.name }}</span>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatsCard
          title="Pedidos Hoje"
          :value="stats.ordersToday"
          icon="package"
          :trend="15"
          bgColor="bg-blue-50"
        />
        <StatsCard
          title="Faturamento"
          :value="`R$ ${stats.revenueToday.toFixed(2)}`"
          icon="currency"
          :trend="8"
          bgColor="bg-green-50"
        />
        <StatsCard
          title="Tempo Médio"
          :value="`${stats.avgTime} min`"
          icon="clock"
          bgColor="bg-yellow-50"
        />
        <StatsCard
          title="Avaliação"
          :value="stats.rating"
          icon="star"
          bgColor="bg-purple-50"
        />
      </div>

      <!-- Main Grid -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-2 space-y-8">
          <RestaurantStatus
            :restaurantName="restaurantData.name"
            :isOpen="restaurantData.isOpen"
            :avgTime="stats.avgTime"
            :acceptanceRate="stats.acceptanceRate"
            @toggleStatus="toggleRestaurantStatus"
          />
          <SalesChart :data="salesData" />
          <MenuOverview
            :items="menuItems"
            @viewAll="goToPedidos"
          />
        </div>

        <div>
          <RecentOrders
            :orders="recentOrders"
            @viewAll="goToPedidos"
          />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';


const router = useRouter();

const restaurantData = ref({
  name: 'Dogão do Jairo',
  isOpen: true
});

const stats = ref({
  ordersToday: 23,
  revenueToday: 1247.50,
  avgTime: 42,
  rating: 4.8,
  acceptanceRate: 95
});

const salesData = ref([
  { label: 'Seg', value: 450 },
  { label: 'Ter', value: 680 },
  { label: 'Qua', value: 520 },
  { label: 'Qui', value: 780 },
  { label: 'Sex', value: 920 },
  { label: 'Sáb', value: 1150 },
  { label: 'Dom', value: 890 }
]);

const menuItems = ref([
  {
    id: '1',
    name: 'Dogão 18cm',
    description: 'Hot dog tradicional',
    price: 12.50,
    available: true,
    salesCount: 15
  },
  {
    id: '2',
    name: 'Dogão 30cm',
    description: 'Hot dog gigante',
    price: 22.00,
    available: true,
    salesCount: 8
  }
]);

const recentOrders = ref([
  {
    id: '1234',
    customerName: 'JP',
    total: 22.00,
    time: new Date().toISOString(),
    status: 'pending',
    items: [{ name: 'Dogão 18cm', quantity: 1 }]
  }
]);

const toggleRestaurantStatus = () => {
  restaurantData.value.isOpen = !restaurantData.value.isOpen;
};

const goToPedidos = () => {
  router.push('/pedidos');
};
</script>