<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-200 custom-dashboard-bg">
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

        <div class="flex items-center gap-4">
          <button
            @click="openCreateModal"
            class="bg-white text-red-600 px-4 py-2 rounded-lg font-bold shadow-md hover:bg-gray-100 transition-all flex items-center gap-2"
          >
            <span>+</span> <span class="hidden sm:inline">Novo Prato</span>
          </button>

          <div class="text-white text-sm bg-red-700 px-3 py-1 rounded-full">
            {{ filteredItems.length }} itens
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
      <div class="bg-white rounded-xl shadow-lg p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Buscar por Nome</label>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Ex: Pizza, Hambúrguer..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none transition-all"
            />
          </div>

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

      <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="i in 6" :key="i" class="bg-white rounded-xl shadow-lg p-6 animate-pulse">
          <div class="h-40 bg-gray-200 rounded-lg mb-4"></div>
          <div class="h-6 bg-gray-200 rounded mb-2"></div>
          <div class="h-4 bg-gray-200 rounded"></div>
        </div>
      </div>

      <div v-else-if="filteredItems.length === 0" class="bg-white rounded-xl shadow-lg p-12 text-center">
        <div class="text-6xl mb-4">🍽️</div>
        <h3 class="text-xl font-semibold text-gray-800 mb-2">Nenhum prato encontrado</h3>
        <p class="text-gray-500">Tente ajustar seus filtros ou crie um novo prato.</p>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="item in filteredItems"
          :key="item.id"
          class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden group"
          :class="{ 'opacity-60': !item.available }"
        >
          <div class="relative h-48 bg-linear-to-br from-red-100 to-red-50 flex items-center justify-center overflow-hidden">
            <div class="text-6xl group-hover:scale-110 transition-transform duration-300">🍽️</div>

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

            <div v-if="item.salesCount > 10" class="absolute top-3 left-3">
              <span class="bg-yellow-400 text-gray-900 px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1">
                ⭐ Top
              </span>
            </div>
          </div>

          <div class="p-6">
            <h3 class="text-lg font-bold text-gray-900 mb-2">{{ item.name }}</h3>
            <p class="text-sm text-gray-600 mb-4 line-clamp-2">{{ item.description }}</p>

            <div class="flex justify-between items-center mb-4 pb-4 border-b border-gray-200">
              <div>
                <p class="text-2xl font-bold text-red-600">R$ {{ formatPrice(item.price) }}</p>
              </div>
              <div class="text-right">
                <p class="text-xs text-gray-500">Vendas</p>
                <p class="text-lg font-semibold text-gray-900">{{ item.salesCount }}</p>
              </div>
            </div>

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

      <div
        v-if="showEditModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
        @click="closeEditModal"
      >
        <div
          class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6"
          @click.stop
        >
          <h2 class="text-2xl font-bold text-gray-900 mb-4">
            {{ isCreating ? 'Novo Prato' : 'Editar Prato' }}
          </h2>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nome</label>
              <input
                v-model="editingItem.name"
                type="text"
                placeholder="Ex: Pizza Margherita"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea
                v-model="editingItem.description"
                rows="3"
                placeholder="Ex: Molho de tomate, queijo mussarela e manjericão."
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              ></textarea>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Preço (R$)</label>
              <input
                v-model.number="editingItem.price"
                type="number"
                step="0.01"
                placeholder="0.00"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent outline-none"
              />
            </div>

            <div class="flex items-center gap-3">
              <input
                v-model="editingItem.available"
                type="checkbox"
                id="available"
                class="w-4 h-4 text-red-600 rounded focus:ring-2 focus:ring-red-500"
              />
              <label for="available" class="text-sm font-medium text-gray-700">Disponível para venda</label>
            </div>
          </div>

          <div class="flex gap-3 mt-6">
            <button
              @click="closeEditModal"
              class="flex-1 px-4 py-2 bg-gray-200 text-gray-800 font-semibold rounded-lg hover:bg-gray-300 transition-colors duration-200"
            >
              Cancelar
            </button>
            <button
              @click="saveItem"
              :disabled="savingItem"
              class="flex-1 px-4 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ savingItem ? '⏳ Salvando...' : (isCreating ? 'Criar Prato' : 'Salvar Alterações') }}
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
import AuthService from '@/services/authService';

interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
  available: boolean;
  salesCount: number;
}

const router = useRouter();
const API_BASE_URL = 'http://localhost:8080';

// --- JWT/Token Setup ---
const token = AuthService.getToken();
const currentUser = AuthService.getCurrentUser();
const headers = { 'Authorization': `Bearer ${token}` };

if (!token || currentUser?.tipo !== 'RESTAURANTE') {
  AuthService.logout();
  if (!token) router.push('/login');
}
const RESTAURANT_ID_FROM_TOKEN = currentUser?.id || 0;

// --- Estado ---
const items = ref<MenuItem[]>([]);
const loading = ref(false);
const searchQuery = ref('');
const availabilityFilter = ref('');
const sortBy = ref('name');
const showEditModal = ref(false);
const savingItem = ref(false);
const isCreating = ref(false); // ✅ Novo estado: controla se é criação ou edição

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
  if (!token) { loading.value = false; return; }

  try {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${RESTAURANT_ID_FROM_TOKEN}/pratos`, { headers });
    items.value = response.data.map((prato: any) => ({
      id: prato.id?.toString() || prato.idPrato?.toString(),
      name: prato.nome,
      description: prato.descricao,
      price: prato.preco,
      available: prato.ativo,
      salesCount: Math.floor(Math.random() * 50)
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

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(item =>
      item.name.toLowerCase().includes(query) ||
      item.description.toLowerCase().includes(query)
    );
  }

  if (availabilityFilter.value === 'available') {
    result = result.filter(item => item.available);
  } else if (availabilityFilter.value === 'unavailable') {
    result = result.filter(item => !item.available);
  }

  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'price-low': return a.price - b.price;
      case 'price-high': return b.price - a.price;
      case 'sales': return b.salesCount - a.salesCount;
      case 'name': default: return a.name.localeCompare(b.name);
    }
  });
  return result;
});

const formatPrice = (price: number | undefined): string => {
  if (!price) return '0,00';
  return price.toFixed(2).replace('.', ',');
};

// ✅ Função para abrir modal em modo CRIAR
const openCreateModal = () => {
  isCreating.value = true;
  editingItem.value = {
    id: '',
    name: '',
    description: '',
    price: 0,
    available: true,
    salesCount: 0
  };
  showEditModal.value = true;
};

// ✅ Função para abrir modal em modo EDITAR
const editItem = (item: MenuItem) => {
  isCreating.value = false;
  editingItem.value = { ...item };
  showEditModal.value = true;
};

const closeEditModal = () => {
  showEditModal.value = false;
};

// ✅ Função unificada para SALVAR (Criação ou Edição)
const saveItem = async () => {
  // Validação básica
  if (!editingItem.value.name || !editingItem.value.price) {
    alert("Nome e Preço são obrigatórios!");
    return;
  }

  savingItem.value = true;
  if (!token) { savingItem.value = false; return; }

  try {
    if (isCreating.value) {
      // --- Lógica de CRIAÇÃO (POST) ---
      const response = await axios.post(
        `${API_BASE_URL}/painel-restaurante/pratos`,
        {
          nome: editingItem.value.name,
          descricao: editingItem.value.description,
          preco: editingItem.value.price,
          disponivel: editingItem.value.available,
          foto: null // Opcional
        },
        { headers }
      );

      // Adiciona o novo item à lista local
      const novoPrato = response.data;
      items.value.push({
        id: novoPrato.idPrato.toString(),
        name: novoPrato.nome,
        description: novoPrato.descricao,
        price: novoPrato.preco,
        available: novoPrato.disponivel || novoPrato.ativo, // Backend pode retornar um ou outro
        salesCount: 0
      });
      alert('Novo prato criado com sucesso!');

    } else {
      // --- Lógica de EDIÇÃO (PUT) ---
      await axios.put(
        `${API_BASE_URL}/painel-restaurante/pratos/${editingItem.value.id}`,
        {
          nome: editingItem.value.name,
          descricao: editingItem.value.description,
          preco: editingItem.value.price,
          disponivel: editingItem.value.available
        },
        { headers }
      );

      // Atualiza item localmente
      const index = items.value.findIndex(item => item.id === editingItem.value.id);
      if (index !== -1) {
        items.value[index] = { ...editingItem.value };
      }
      alert('Prato atualizado com sucesso!');
    }

    closeEditModal();

  } catch (error) {
    console.error('Erro ao salvar prato:', error);
    alert('Erro ao salvar. Verifique os dados.');
  } finally {
    savingItem.value = false;
  }
};

const toggleAvailability = async (item: MenuItem) => {
  if (!token) return;

  try {
    const newStatus = !item.available;
    await axios.put(
      `${API_BASE_URL}/painel-restaurante/pratos/${item.id}`,
      {
        nome: item.name,
        descricao: item.description,
        preco: item.price,
        disponivel: newStatus // Enviando 'disponivel' corretamente
      },
      { headers }
    );
    item.available = newStatus;
  } catch (error) {
    console.error('Erro ao atualizar disponibilidade:', error);
    alert('Erro de conexão.');
  }
};

onMounted(() => {
  if (token) fetchMenuItems();
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.custom-slide-down {
  animation: slideDown 0.5s ease;
}
@keyframes slideDown {
  from { transform: translateY(-100%); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>
