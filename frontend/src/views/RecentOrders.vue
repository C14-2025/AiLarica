<template>
  <div class="bg-white p-6 rounded-xl shadow-lg">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold text-gray-800">Pedidos Recentes</h2>
      <button @click="$emit('viewAll')" class="text-sm text-blue-600 hover:text-blue-800 font-medium">
        Ver Todos
      </button>
    </div>

    <ul class="space-y-4">
      <li v-for="order in orders" :key="order.id" class="flex justify-between items-center border-b pb-2 last:border-b-0">
        <div>
          <p class="font-medium text-gray-900">Pedido #{{ order.id }}</p>
          <p class="text-sm text-gray-500">Cliente: {{ order.customerName }}</p>
        </div>
        <div class="text-right">
          <p class="font-semibold text-gray-700">R$ {{ order.total.toFixed(2) }}</p>
          <span :class="['text-xs font-medium px-2 py-0.5 rounded-full', statusClass(order.status)]">
            {{ statusText(order.status) }}
          </span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  orders: {
    id: string;
    customerName: string;
    total: number;
    time: string;
    status: 'pending' | 'preparing' | 'delivering' | 'delivered' | 'cancelled';
    items: { name: string; quantity: number }[];
  }[];
}>();

defineEmits(['viewAll']);

const statusClass = (status: string) => {
  switch (status) {
    case 'pending':
      return 'bg-yellow-100 text-yellow-800';
    case 'preparing':
      return 'bg-blue-100 text-blue-800';
    case 'delivering':
      return 'bg-purple-100 text-purple-800';
    case 'delivered':
      return 'bg-green-100 text-green-800';
    case 'cancelled':
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
};

const statusText = (status: string) => {
  switch (status) {
    case 'pending':
      return 'Pendente';
    case 'preparing':
      return 'Preparando';
    case 'delivering':
      return 'Em Entrega';
    case 'delivered':
      return 'Entregue';
    case 'cancelled':
      return 'Cancelado';
    default:
      return 'Desconhecido';
  }
};
</script>
