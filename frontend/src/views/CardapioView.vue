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
          <h1 class="text-2xl sm:text-3xl font-bold text-white drop-shadow-md">Cardápio Completo</h1>
        </div>
        <div class="text-white text-sm">
          <span class="bg-red-700 px-3 py-1 rounded-full">{{ filteredItems.length }} itens</span>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
      <!-- Filtros e Busca -->
      <div class="bg-white rounded-xl shadow-lg p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- Busca por Nome -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Buscar por Nome</label>
            <input 
              v-model="searchQuery"
              type="text" 
              placeholder="Ex: Pizza, Hambúrguer..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          <!-- Filtro de Disponibilidade -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Filtrar por Status</label>
            <select 
              v-model="availabilityFilter"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            >
              <option value="">Todos</option>
              <option value="available">Disponíveis</option>
              <option value="unavailable">Indisponíveis</option>
            </select>
          </div>

          <!-- Ordenação -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Ordenar por</label>
            <select 
              v-model="sortBy"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            >
              <option value="name">Nome (A-Z)</option>
              <option value="price-low">Preço (Menor)</option>
              <option value="price-high">Preço (Maior)</option>
              <option value="sales">Mais Vendidos</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="i in 6" :key="i" class="bg-white rounded-xl shadow-lg p-6 animate-pulse">
          <div class="h-40 bg-gray-200 rounded-lg mb-4"></div>
          <div class="h-6 bg-gray-200 rounded mb-2"></div>
          <div class="h-4 bg-gray-200 rounded"></div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else-if="filteredItems.length === 0" class="bg-white rounded-xl shadow-lg p-12 text-center">
        <div class="mb-4">
          <svg class="w-12 h-12 mx-auto text-gray-400" viewBox="0 0 106.06 106.06" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img">
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
        <h3 class="text-xl font-semibold text-gray-800 mb-2">Nenhum item encontrado.</h3>
        <p class="text-gray-500">Tente ajustar seus filtros ou pesquisa.</p>
      </div>

      <!-- Items Grid -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
          v-for="item in filteredItems" 
          :key="item.id"
          class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden group"
          :class="{ 'opacity-60': !item.available }"
        >
          <!-- Imagem do Prato -->
          <div class="relative h-48 bg-linear-to-br from-red-100 to-red-50 flex items-center justify-center overflow-hidden">
            <div class="text-6xl group-hover:scale-110 transition-transform duration-300">🍽️</div>
            
            <!-- Badge de Disponibilidade -->
            <div class="absolute top-3 right-3">
              <span 
                v-if="item.available"
                class="bg-green-500 text-white px-3 py-1 rounded-full text-xs font-semibold"
              >
                Disponível
              </span>
              <span 
                v-else
                class="bg-red-500 text-white px-3 py-1 rounded-full text-xs font-semibold"
              >
                Indisponível
              </span>
            </div>

            <!-- Badge de Mais Vendido -->
            <div v-if="item.salesCount > 10" class="absolute top-3 left-3">
              <span class="bg-yellow-400 text-gray-900 px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1">
                ⭐ Top
              </span>
            </div>
          </div>

          <!-- Conteúdo -->
          <div class="p-6">
            <!-- Nome e Descrição -->
            <h3 class="text-lg font-bold text-gray-900 mb-2">{{ item.name }}</h3>
            <p class="text-sm text-gray-600 mb-4 line-clamp-2">{{ item.description }}</p>

            <!-- Preço e Vendas -->
            <div class="flex justify-between items-center mb-4 pb-4 border-b border-gray-200">
              <div>
                <p class="text-2xl font-bold text-red-600">R$ {{ formatPrice(item.price) }}</p>
              </div>
              <div class="text-right">
                <p class="text-xs text-gray-500">Vendas</p>
                <p class="text-lg font-semibold text-gray-900">{{ item.salesCount }}</p>
              </div>
            </div>

            <!-- Ações -->
            <div class="flex gap-3">
              <button 
                @click="editItem(item)"
                class="flex-1 px-4 py-2 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors duration-200"
              >
                ✏️ Editar
              </button>
              <button 
                @click="toggleAvailability(item)"
                :class="[
                  'flex-1 px-4 py-2 font-semibold rounded-lg transition-colors duration-200',
                  item.available 
                    ? 'bg-red-100 text-red-600 hover:bg-red-200' 
                    : 'bg-green-100 text-green-600 hover:bg-green-200'
                ]"
              >
                {{ item.available ? '🚫 Desativar' : '✓ Ativar' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal de Edição -->
      <div 
        v-if="showEditModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
        @click="closeEditModal"
      >
        <div 
          class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6"
          @click.stop
        >
          <h2 class="text-2xl font-bold text-gray-900 mb-4">Editar Item</h2>

          <div class="space-y-4">
            <!-- Nome -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nome</label>
              <input 
                v-model="editingItem.name"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Descrição -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea 
                v-model="editingItem.description"
                rows="3"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              ></textarea>
            </div>

            <!-- Preço -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Preço (R$)</label>
              <input 
                v-model.number="editingItem.price"
                type="number"
                step="0.01"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <!-- Disponibilidade -->
            <div class="flex items-center gap-3">
              <input 
                v-model="editingItem.available"
                type="checkbox"
                id="available"
                class="w-4 h-4 text-red-600 rounded focus:ring-2 focus:ring-red-500"
              />
              <label for="available" class="text-sm font-medium text-gray-700">Disponível</label>
            </div>
          </div>

          <!-- Botões -->
          <div class="flex gap-3 mt-6">
            <button 
              @click="closeEditModal"
              class="flex-1 px-4 py-2 bg-gray-200 text-gray-800 font-semibold rounded-lg hover:bg-gray-300 transition-colors duration-200"
            >
              Cancelar
            </button>
            <button 
              @click="saveEditedItem"
              :disabled="savingItem"
              class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ savingItem ? '⏳ Salvando...' : '💾 Salvar' }}
            </button>
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

interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
  available: boolean;
  salesCount: number;
}

const router = useRouter();
const RESTAURANT_ID = 1;
const API_BASE_URL = 'http://localhost:8080';

const items = ref<MenuItem[]>([]);
const loading = ref(false);
const searchQuery = ref('');
const availabilityFilter = ref('');
const sortBy = ref('name');
const showEditModal = ref(false);
const savingItem = ref(false);
const editingItem = ref<MenuItem>({
  id: '',
  name: '',
  description: '',
  price: 0,
  available: true,
  salesCount: 0
});

const goBack = () => {
  router.push('/restaurante/dashboard');
};

const fetchMenuItems = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pratos`);
    items.value = response.data.map((prato: any) => ({
      id: prato.id?.toString() || prato.idPrato?.toString(),
      name: prato.nome,
      description: prato.descricao,
      price: prato.preco,
      available: prato.ativo,
      salesCount: Math.floor(Math.random() * 50) // Simulando dados de vendas
    }));
  } catch (error) {
    console.error('Erro ao buscar itens do cardápio:', error);
    items.value = [];
  } finally {
    loading.value = false;
  }
};

const filteredItems = computed(() => {
  let result = [...items.value];

  // Filtrar por busca
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(item =>
      item.name.toLowerCase().includes(query) ||
      item.description.toLowerCase().includes(query)
    );
  }

  // Filtrar por disponibilidade
  if (availabilityFilter.value === 'available') {
    result = result.filter(item => item.available);
  } else if (availabilityFilter.value === 'unavailable') {
    result = result.filter(item => !item.available);
  }

  // Ordenar
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'price-low':
        return a.price - b.price;
      case 'price-high':
        return b.price - a.price;
      case 'sales':
        return b.salesCount - a.salesCount;
      case 'name':
      default:
        return a.name.localeCompare(b.name);
    }
  });

  return result;
});

const formatPrice = (price: number | undefined): string => {
  if (!price) return '0,00';
  return price.toFixed(2).replace('.', ',');
};

const editItem = (item: MenuItem) => {
  editingItem.value = { ...item };
  showEditModal.value = true;
};

const closeEditModal = () => {
  showEditModal.value = false;
  editingItem.value = {
    id: '',
    name: '',
    description: '',
    price: 0,
    available: true,
    salesCount: 0
  };
};

const saveEditedItem = async () => {
  savingItem.value = true;
  try {
    // Enviar atualização para o backend
    await axios.put(
      `${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pratos/${editingItem.value.id}`,
      {
        nome: editingItem.value.name,
        descricao: editingItem.value.description,
        preco: editingItem.value.price,
        ativo: editingItem.value.available
      }
    );

    // Atualizar item localmente
    const index = items.value.findIndex(item => item.id === editingItem.value.id);
    if (index !== -1) {
      items.value[index] = { ...editingItem.value };
    }

    closeEditModal();
    alert('Prato atualizado com sucesso!');
  } catch (error) {
    console.error('Erro ao atualizar prato:', error);
    alert('Erro ao atualizar o prato. Tente novamente.');
  } finally {
    savingItem.value = false;
  }
};

const toggleAvailability = async (item: MenuItem) => {
  try {
    const newStatus = !item.available;
    
    // Enviar atualização para o backend
    await axios.put(
      `${API_BASE_URL}/restaurantes/${RESTAURANT_ID}/pratos/${item.id}`,
      {
        nome: item.name,
        descricao: item.description,
        preco: item.price,
        ativo: newStatus
      }
    );

    // Atualizar item localmente
    item.available = newStatus;
  } catch (error) {
    console.error('Erro ao atualizar disponibilidade do prato:', error);
    alert('Erro ao atualizar a disponibilidade do prato.');
  }
};

onMounted(() => {
  fetchMenuItems();
});
</script>

<style scoped>

.custom-dashboard-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f5 0%, #fff9f0 50%, #f5fef8 100%);
  background-size: 200% 200%; /* Ajuste para a animação */
  animation: gradientShift 10s ease infinite alternate;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
