<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Meus Pedidos</h1>

    <div v-if="orders.length === 0" class="text-center py-10 bg-white rounded-xl shadow-lg">
      <p class="text-xl text-gray-500">Você ainda não fez nenhum pedido.</p>
      <router-link to="/" class="text-red-500 hover:text-red-700 mt-4 inline-block">
        Começar a Pedir
      </router-link>
    </div>

    <div v-else class="space-y-6">
      <div
        v-for="order in orders"
        :key="order.id"
        class="bg-white p-6 rounded-xl shadow-lg border-l-4"
        :class="statusColor(order.status)"
      >
        <div class="flex justify-between items-start mb-3">
          <div>
            <h2 class="text-xl font-bold text-gray-800">Pedido #{{ order.id }}</h2>
            <p class="text-sm text-gray-500">Restaurante: {{ order.restaurant }}</p>
            <p class="text-sm text-gray-500">Data: {{ order.date }}</p>
          </div>
          <span class="px-3 py-1 text-sm font-semibold rounded-full" :class="statusBadge(order.status)">
            {{ order.status }}
          </span>
        </div>

        <div class="border-t pt-3 mt-3">
          <p class="font-medium mb-2">Itens:</p>
          <ul class="list-disc list-inside text-sm text-gray-700 space-y-1">
            <li v-for="item in order.items" :key="item.name">
              {{ item.quantity }}x {{ item.name }} (R$ {{ item.price.toFixed(2) }})
            </li>
          </ul>
        </div>

        <div class="flex justify-between items-center border-t pt-3 mt-3">
          <p class="text-lg font-bold text-red-500">Total: R$ {{ order.total.toFixed(2) }}</p>
          <router-link
            v-if="order.status === 'Entregue' && !order.rated"
            :to="{ name: 'Avaliacao', params: { pedidoId: order.id } }"
            class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-colors text-sm"
          >
            Avaliar Pedido
          </router-link>
          <button
            v-else-if="order.status === 'Pendente' || order.status === 'Em Preparo'"
            class="bg-gray-500 text-white px-4 py-2 rounded-lg text-sm cursor-not-allowed"
            disabled
          >
            Acompanhar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { orderService } from '../../services/orderService';

const orders = ref([
  {
    id: 1001,
    restaurant: 'Pizzaria do Chef',
    date: '23/11/2025 19:30',
    status: 'Em Preparo',
    total: 55.00,
    rated: false,
    items: [{ name: 'Pizza Calabresa', quantity: 1, price: 45.00 }, { name: 'Refrigerante', quantity: 1, price: 10.00 }],
  },
  {
    id: 1000,
    restaurant: 'Japa Food Express',
    date: '22/11/2025 20:15',
    status: 'Entregue',
    total: 189.80,
    rated: false,
    items: [{ name: 'Combinado Salmão', quantity: 2, price: 89.90 }, { name: 'Molho Extra', quantity: 1, price: 10.00 }],
  },
  {
    id: 999,
    restaurant: 'Hamburgueria Gourmet',
    date: '21/11/2025 12:45',
    status: 'Entregue',
    total: 35.50,
    rated: true,
    items: [{ name: 'X-Bacon', quantity: 1, price: 25.00 }, { name: 'Batata Frita', quantity: 1, price: 10.50 }],
  },
]);

const statusColor = (status: string) => {
  switch (status) {
    case 'Entregue':
      return 'border-l-green-500';
    case 'Em Preparo':
      return 'border-l-yellow-500';
    case 'Pendente':
      return 'border-l-blue-500';
    default:
      return 'border-l-gray-500';
  }
};

const statusBadge = (status: string) => {
  switch (status) {
    case 'Entregue':
      return 'bg-green-100 text-green-800';
    case 'Em Preparo':
      return 'bg-yellow-100 text-yellow-800';
    case 'Pendente':
      return 'bg-blue-100 text-blue-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>

const loadOrders = async () => {
  try {
    const data = await orderService.getUserOrders();
    orders.value = data;
  } catch (error) {
    console.error('Erro ao carregar pedidos:', error);
  }
};

onMounted(() => {
  loadOrders();
});
