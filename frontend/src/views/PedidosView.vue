<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-200 custom-dashboard-bg">
    <!-- Header -->
    <header class="bg-red-600 shadow-xl shadow-red-600/30 sticky top-0 z-50 custom-slide-down">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 sm:py-6 flex items-center justify-between">
        <div class="flex items-center space-x-4">
          <button 
            @click="goBack"
            class="text-white hover:bg-red-700 p-2 rounded-lg transition-colors duration-200"
            title="Voltar ao Dashboard"
          >
            ← Voltar
          </button>
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Gerenciamento de Pedidos</h1>
        </div>
        <div class="text-white text-sm">
          <span class="bg-red-700 px-3 py-1 rounded-full">{{ filteredOrders.length }} pedidos</span>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
      <!-- Filtros e Busca -->
      <div class="bg-white rounded-xl shadow-lg p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- Busca por ID -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Buscar por ID</label>
            <input 
              v-model="searchQuery"
              type="text" 
              placeholder="Ex: #123"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          <!-- Filtro de Status -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Filtrar por Status</label>
            <select 
              v-model="selectedStatus"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            >
              <option value="">Todos os Status</option>
              <option value="pendente">Pendente</option>
              <option value="preparando">Preparando</option>
              <option value="em_entrega">Em Entrega</option>
              <option value="entregue">Entregue</option>
              <option value="cancelado">Cancelado</option>
            </select>
          </div>

          <!-- Ordenação -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Ordenar por</label>
            <select 
              v-model="sortBy"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            >
              <option value="recent">Mais Recentes</option>
              <option value="oldest">Mais Antigos</option>
              <option value="highest">Maior Valor</option>
              <option value="lowest">Menor Valor</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="space-y-4">
        <div v-for="i in 5" :key="i" class="bg-white rounded-xl shadow-lg p-6 animate-pulse">
          <div class="h-24 bg-gray-200 rounded-lg"></div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else-if="filteredOrders.length === 0" class="bg-white rounded-xl shadow-lg p-12 text-center">
        <div class="text-6xl mb-4"><svg class="w-12 h-12 mx-auto text-gray-400" viewBox="0 0 106.06 106.06" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img">
            <g fill="currentColor">
              <path d="M53.029,0.001c-13.587,0-27.173,5.17-37.515,15.512C-5.173,36.2-5.171,69.858,15.516,90.546
        c10.341,10.343,23.927,15.513,37.513,15.513s27.172-5.172,37.517-15.519c20.686-20.684,20.684-54.339,0.002-75.022
        C80.202,5.173,66.615,0.001,53.029,0.001z M84.758,84.757C76.01,93.505,64.52,97.878,53.029,97.88
        c-11.49,0-22.98-4.373-31.728-13.119c-2.188-2.188-4.101-4.547-5.741-7.033C4.078,60.317,5.993,36.609,21.301,21.3
        c8.748-8.747,20.238-13.12,31.728-13.12s22.98,4.373,31.729,13.121C102.254,38.796,102.252,67.264,84.758,84.757z M24.688,52.313
        c-1.212-1.133-1.274-3.033-0.142-4.246c1.132-1.213,3.018-1.291,4.247-0.143c3.251,3.053,6.589,0.242,6.959-0.088
        c1.105-0.99,2.741-1.012,3.867-0.119c0.133,0.104,0.259,0.223,0.376,0.354c1.106,1.236,1.001,3.135-0.235,4.242
        C37.096,54.698,30.552,57.798,24.688,52.313z M81.502,48.036c1.105,1.236,1.001,3.135-0.235,4.242
        c-2.664,2.385-9.208,5.484-15.072,0c-1.212-1.133-1.273-3.033-0.142-4.246s3.018-1.291,4.247-0.143
        c3.251,3.053,6.589,0.242,6.959-0.088c1.104-0.99,2.741-1.012,3.867-0.119C81.259,47.786,81.384,47.905,81.502,48.036z
         M77.017,79.333c0.658,1.521-0.041,3.287-1.563,3.945c-1.52,0.66-3.284-0.041-3.942-1.563c-2.895-6.688-9.731-11.013-17.422-11.013
        c-7.867,0-14.746,4.32-17.523,11.007c-0.479,1.151-1.596,1.85-2.771,1.85c-0.383,0-0.773-0.073-1.149-0.229
        c-1.53-0.637-2.255-2.393-1.62-3.922c3.711-8.933,12.764-14.703,23.064-14.703C64.175,64.704,73.175,70.446,77.017,79.333z"/>
            </g>
          </svg>
        </div>
        <h3 class="text-xl font-semibold text-gray-800 mb-2">Nenhum pedido encontrado</h3>
        <p class="text-gray-500">Tente ajustar seus filtros ou aguarde novos pedidos.</p>
      </div>

      <!-- Orders Grid -->
      <div v-else class="space-y-4">
        <div 
          v-for="order in filteredOrders" 
          :key="resolveOrderId(order)"
          class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 overflow-hidden"
        >
          <div class="p-6">
            <!-- Header do Pedido -->
            <div class="flex flex-col md:flex-row md:items-center md:justify-between mb-4 pb-4 border-b border-gray-200">
              <div>
                <h3 class="text-lg font-bold text-gray-900">Pedido #{{ resolveOrderId(order) }}</h3>
                <p class="text-sm text-gray-500 mt-1">
                  {{ formatDateTime(order.time || order.dataHora) }}
                </p>
              </div>
              <div class="mt-3 md:mt-0 flex items-center gap-3">
                <span :class="['px-4 py-2 rounded-full font-semibold text-sm', getStatusBadgeClass(order.status)]">
                  {{ statusText(order.status) }}
                </span>
                <span class="text-2xl font-bold text-red-600">
                  R$ {{ formatPrice(order.total || order.valorTotal) }}
                </span>
              </div>
            </div>

            <!-- Informações do Cliente -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div class="bg-gray-50 p-4 rounded-lg">
                <p class="text-xs font-semibold text-gray-600 uppercase mb-2">Cliente</p>
                <p class="text-lg font-semibold text-gray-900">
                  {{ order.customerName || `Cliente #${order.idUsuario}` }}
                </p>
              </div>
              <div class="bg-gray-50 p-4 rounded-lg">
                <p class="text-xs font-semibold text-gray-600 uppercase mb-2">Itens do Pedido</p>
                <p class="text-lg font-semibold text-gray-900">
                  {{ ((order.items ?? order.itens) || []).length }} item(ns)
                </p>
              </div>
            </div>

            <!-- Itens do Pedido -->
            <div v-if="((order.items ?? order.itens) || []).length > 0" class="mb-6 bg-gray-50 rounded-lg p-4">
              <p class="text-sm font-semibold text-gray-700 mb-3">Detalhes dos Itens:</p>
              <ul class="space-y-2">
                <li 
                  v-for="(item, index) in (order.items ?? order.itens ?? [])" 
                  :key="index"
                  class="flex justify-between items-center text-sm text-gray-700"
                >
                  <span>{{ item.name || item.nome }} x{{ (item.quantity ?? item.quantidade) ?? 0 }}</span>
                  <span class="font-medium">R$ {{ formatPrice(((item.price ?? item.preco) ?? 0) * ((item.quantity ?? item.quantidade) ?? 0)) }}</span>
                </li>
              </ul>
            </div>

            <!-- Ações -->
            <div class="flex flex-col sm:flex-row gap-3 pt-4 border-t border-gray-200">
              <button 
                v-if="canUpdateStatus(order.status)"
                @click="updateOrderStatus(resolveOrderId(order), getNextStatus(order.status))"
                :disabled="updatingOrderId === resolveOrderId(order)"
                class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="updatingOrderId === resolveOrderId(order)" class="inline-block mr-2">⏳</span>
                {{ getNextStatusText(order.status) }}
              </button>
              
              <button 
                @click="toggleOrderDetails(resolveOrderId(order))"
                class="flex-1 px-4 py-2 bg-gray-200 text-gray-800 font-semibold rounded-lg hover:bg-gray-300 transition-colors duration-200"
              >
                {{ expandedOrderId === resolveOrderId(order) ? '▼ Ocultar' : '▶ Detalhes' }}
              </button>

              <button 
                v-if="order.status !== 'cancelado' && order.status !== 'entregue' && order.status !== 'cancelled' && order.status !== 'delivered'"
                @click="cancelOrder(resolveOrderId(order))"
                class="flex-1 px-4 py-2 bg-red-100 text-red-600 font-semibold rounded-lg hover:bg-red-200 transition-colors duration-200"
              >
                ✕ Cancelar
              </button>
            </div>

            <!-- Detalhes Expandidos -->
            <div 
              v-if="expandedOrderId === resolveOrderId(order)"
              class="mt-4 pt-4 border-t border-gray-200"
            >
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                <div>
                  <p class="text-gray-600">ID do Restaurante</p>
                  <p class="font-semibold text-gray-900">{{ order.idRestaurante || 'N/A' }}</p>
                </div>
                <div>
                  <p class="text-gray-600">ID do Usuário</p>
                  <p class="font-semibold text-gray-900">{{ order.idUsuario || 'N/A' }}</p>
                </div>
                <div>
                  <p class="text-gray-600">Data/Hora do Pedido</p>
                  <p class="font-semibold text-gray-900">{{ order.time || order.dataHora }}</p>
                </div>
                <div>
                  <p class="text-gray-600">Status Atual</p>
                  <p class="font-semibold text-gray-900">{{ statusText(order.status) }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

interface ItemPedido {
  id?: number;
  nome?: string;
  name?: string;
  quantidade?: number;
  quantity?: number;
  preco?: number;
  price?: number;
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
  items?: ItemPedido[];
  itens?: ItemPedido[];
}

const router = useRouter();
const RESTAURANT_ID = 1;
const API_BASE_URL = 'http://localhost:8080';

const orders = ref<Pedido[]>([]);
const loading = ref(false);
const searchQuery = ref('');
const selectedStatus = ref('');
const sortBy = ref('recent');
const expandedOrderId = ref<string | number | null>(null);
const updatingOrderId = ref<string | number | null>(null);

const goBack = () => {
  router.push('/restaurante/dashboard');
};

const fetchOrders = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pedidos`);
    orders.value = response.data.map((pedido: any) => ({
      id: pedido.idPedido?.toString() || pedido.id?.toString(),
      idPedido: pedido.idPedido || pedido.id,
      customerName: `Cliente #${pedido.idUsuario}`,
      idUsuario: pedido.idUsuario,
      total: pedido.valorTotal,
      valorTotal: pedido.valorTotal,
      time: pedido.dataHora,
      dataHora: pedido.dataHora,
      status: pedido.status || 'pendente',
      items: pedido.itens || [],
      itens: pedido.itens || [],
      idRestaurante: pedido.idRestaurante
    }));
  } catch (error) {
    console.error('Erro ao buscar pedidos:', error);
    orders.value = [];
  } finally {
    loading.value = false;
  }
};

const resolveOrderId = (order: Pedido): string | number => {
  return order.id ?? order.idPedido ?? '';
};

const filteredOrders = computed(() => {
  let result = [...orders.value];

  // Filtrar por busca
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase().replace('#', '');
    result = result.filter(order => 
      (((order.id ?? order.idPedido) ?? '') as string).toString().toLowerCase().includes(query)
    );
  }

  // Filtrar por status
  if (selectedStatus.value) {
    result = result.filter(order => 
      order.status.toLowerCase() === selectedStatus.value.toLowerCase()
    );
  }

  // Ordenar
  result.sort((a, b) => {
    const aTime = a.time || a.dataHora || '';
    const bTime = b.time || b.dataHora || '';
    const aTotal = a.total || a.valorTotal || 0;
    const bTotal = b.total || b.valorTotal || 0;
    
    switch (sortBy.value) {
      case 'oldest':
        return new Date(aTime).getTime() - new Date(bTime).getTime();
      case 'highest':
        return bTotal - aTotal;
      case 'lowest':
        return aTotal - bTotal;
      case 'recent':
      default:
        return new Date(bTime).getTime() - new Date(aTime).getTime();
    }
  });

  return result;
});

const statusText = (status: string): string => {
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

const getStatusBadgeClass = (status: string): string => {
  const classMap: Record<string, string> = {
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
  return classMap[status.toLowerCase()] || 'bg-gray-100 text-gray-800';
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

const toggleOrderDetails = (orderId: string | number) => {
  expandedOrderId.value = expandedOrderId.value === orderId ? null : orderId;
};

const canUpdateStatus = (status: string): boolean => {
  const finalStatuses = ['entregue', 'cancelado', 'delivered', 'cancelled'];
  return !finalStatuses.includes(status.toLowerCase());
};

const getNextStatus = (status: string): string => {
  const statusFlow: Record<string, string> = {
    'pendente': 'preparando',
    'preparando': 'em_entrega',
    'em_entrega': 'entregue',
    'pending': 'preparing',
    'preparing': 'delivering',
    'delivering': 'delivered'
  };
  return statusFlow[status.toLowerCase()] || status;
};

const getNextStatusText = (status: string): string => {
  const textMap: Record<string, string> = {
    'pendente': '👨‍🍳 Começar Preparação',
    'preparando': '🛵 Enviar para Entrega',
    'em_entrega': '✓ Marcar Entregue',
    'pending': '👨‍🍳 Começar Preparação',
    'preparing': '🛵 Enviar para Entrega',
    'delivering': '✓ Marcar Entregue'
  };
  return textMap[status.toLowerCase()] || 'Próximo Passo';
};

const updateOrderStatus = async (orderId: string | number, newStatus: string) => {
  if (!orderId) {
    alert('ID do pedido inválido.');
    return;
  }
  updatingOrderId.value = orderId;
  try {
    await axios.put(
      `${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pedidos/${orderId}/status`,
      { novoStatus: newStatus }
    );
    
    // Atualizar o status localmente
    const order = orders.value.find(o => resolveOrderId(o) === orderId);
    if (order) {
      order.status = newStatus;
    }
  } catch (error) {
    console.error('Erro ao atualizar status do pedido:', error);
    alert('Erro ao atualizar o status do pedido.');
  } finally {
    updatingOrderId.value = null;
  }
};

const cancelOrder = async (orderId: string | number) => {
  if (!orderId) {
    alert('ID do pedido inválido.');
    return;
  }

  if (!confirm('Tem certeza que deseja cancelar este pedido?')) {
    return;
  }

  updatingOrderId.value = orderId;
  try {
    await axios.put(
      `${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pedidos/${orderId}/status`,
      { novoStatus: 'cancelado' }
    );
    
    // Atualizar o status localmente
    const order = orders.value.find(o => resolveOrderId(o) === orderId);
    if (order) {
      order.status = 'cancelado';
    }
  } catch (error) {
    console.error('Erro ao cancelar pedido:', error);
    alert('Erro ao cancelar o pedido.');
  } finally {
    updatingOrderId.value = null;
  }
};

onMounted(() => {
  fetchOrders();
  // Atualizar pedidos a cada 30 segundos
  const interval = setInterval(fetchOrders, 30000);
  
  // Limpar intervalo ao desmontar o componente
  return () => clearInterval(interval);
});
</script>

<style scoped>
/* Animações suaves */

.custom-dashboard-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f5 0%, #fff9f0 50%, #f5fef8 100%);
  background-size: 200% 200%; /* Ajuste para a animação */
  animation: gradientShift 10s ease infinite alternate;
}

@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 100% 50%;
  }
}

.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

/* Apenas animação de entrada do header (slide down) */
.custom-slide-down {
  animation: slideDown 0.5s ease;
}

@keyframes slideDown {
  from { transform: translateY(-100%); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>
