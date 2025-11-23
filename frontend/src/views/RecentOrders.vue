<template>
  <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 hover:shadow-xl transition-shadow duration-300">
    <div class="flex justify-between items-center mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Pedidos Recentes</h2>
        <p class="text-sm text-gray-500 mt-1">Últimas atividades</p>
      </div>
      <button 
        @click="$emit('viewAll')" 
        class="text-sm text-red-600 hover:text-red-800 font-semibold px-4 py-2 rounded-lg hover:bg-red-50 transition-colors duration-200"
      >
        Ver Todos →
      </button>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="space-y-4">
      <div v-for="i in 3" :key="i" class="animate-pulse">
        <div class="h-16 bg-gray-200 rounded-lg"></div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else-if="orders.length === 0" class="text-center py-8">
      <div class="text-4xl mb-2">📭</div>
      <p class="text-gray-500">Nenhum pedido recente</p>
    </div>

    <!-- Orders List -->
    <ul v-else class="space-y-3">
      <li 
        v-for="order in orders" 
        :key="order.id || order.idPedido" 
        class="flex justify-between items-start border-l-4 pl-4 py-3 rounded-r-lg hover:bg-gray-50 transition-colors duration-200"
        :class="getStatusBorderColor(order.status)"
      >
        <div class="flex-1">
          <div class="flex items-center gap-2">
            <p class="font-semibold text-gray-900">Pedido #{{ order.id || order.idPedido }}</p>
            <span :class="['text-xs font-medium px-2 py-1 rounded-full', statusClass(order.status)]">
              {{ statusText(order.status) }}
            </span>
          </div>
          <p class="text-sm text-gray-600 mt-1">
            <span class="font-medium">Cliente:</span> {{ order.customerName || getCustomerName(order.idUsuario) }}
          </p>
          <p class="text-xs text-gray-500 mt-1">
            {{ formatDateTime(order.time || order.dataHora) }}
          </p>
        </div>
        <div class="text-right ml-4">
          <p class="font-bold text-gray-900 text-lg">R$ {{ formatPrice(order.total || order.valorTotal) }}</p>
          <p class="text-xs text-gray-500 mt-1">{{ (order.items || order.itens)?.length || 0 }} item(ns)</p>
        </div>
      </li>
    </ul>

    <!-- Footer -->
    <div v-if="orders.length > 0" class="mt-6 pt-4 border-t border-gray-200">
      <p class="text-xs text-gray-500 text-center">
        Mostrando {{ Math.min(3, orders.length) }} de {{ totalOrders }} pedidos
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, ref, computed } from 'vue';

interface ItemPedido {
  id: number;
  nome: string;
  quantidade: number;
  preco: number;
}

interface Pedido {
  id?: string;
  idPedido?: number;
  status: string;
  total?: number;
  valorTotal?: number;
  dataHora?: string;
  time?: string;
  customerName?: string;
  idUsuario?: number;
  idRestaurante?: number;
  items?: { name: string; quantity: number }[];
  itens?: ItemPedido[];
}

const props = defineProps<{
  orders: Pedido[];
  loading?: boolean;
  totalOrders?: number;
}>();

defineEmits(['viewAll']);

const loading = ref(props.loading || false);
const totalOrders = computed(() => props.totalOrders || props.orders.length);

const statusClass = (status: string) => {
  const statusMap: Record<string, string> = {
    'pendente': 'bg-yellow-100 text-yellow-800',
    'preparando': 'bg-blue-100 text-blue-800',
    'em_entrega': 'bg-purple-100 text-purple-800',
    'entregue': 'bg-green-100 text-green-800',
    'cancelado': 'bg-red-100 text-red-800',
    'pending': 'bg-yellow-100 text-yellow-800',
    'preparing': 'bg-blue-100 text-blue-800',
    'delivering': 'bg-purple-100 text-purple-800',
    'delivered': 'bg-green-100 text-green-800',
    'cancelled': 'bg-red-100 text-red-800',
  };
  return statusMap[status.toLowerCase()] || 'bg-gray-100 text-gray-800';
};

const getStatusBorderColor = (status: string) => {
  const borderMap: Record<string, string> = {
    'pendente': 'border-yellow-400',
    'preparando': 'border-blue-400',
    'em_entrega': 'border-purple-400',
    'entregue': 'border-green-400',
    'cancelado': 'border-red-400',
    'pending': 'border-yellow-400',
    'preparing': 'border-blue-400',
    'delivering': 'border-purple-400',
    'delivered': 'border-green-400',
    'cancelled': 'border-red-400',
  };
  return borderMap[status.toLowerCase()] || 'border-gray-400';
};

const statusText = (status: string) => {
  const textMap: Record<string, string> = {
    'pendente': 'Pendente',
    'preparando': 'Preparando',
    'em_entrega': 'Em Entrega',
    'entregue': 'Entregue',
    'cancelado': 'Cancelado',
    'pending': 'Pendente',
    'preparing': 'Preparando',
    'delivering': 'Em Entrega',
    'delivered': 'Entregue',
    'cancelled': 'Cancelado',
  };
  return textMap[status.toLowerCase()] || 'Desconhecido';
};

const formatPrice = (price: number | undefined): string => {
  if (!price) return '0,00';
  return price.toFixed(2).replace('.', ',');
};

const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return 'Data desconhecida';
  try {
    const date = new Date(dateTime);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffMins < 1) return 'Agora';
    if (diffMins < 60) return `${diffMins}m atrás`;
    if (diffHours < 24) return `${diffHours}h atrás`;
    if (diffDays < 7) return `${diffDays}d atrás`;

    return date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return dateTime;
  }
};

const getCustomerName = (userId: number | undefined): string => {
  if (!userId) return 'Cliente desconhecido';
  return `Cliente #${userId}`;
};
</script>

<style scoped>
/* Animações suaves */
li {
  animation: slideInLeft 0.3s ease-out;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
