<template>
  <div class="min-h-screen bg-gray-50 pb-24">

    <div class="relative h-64 bg-gray-800">
      <img
        v-if="restaurant.fotoPerfil && restaurant.fotoPerfil !== 'url_default.jpg'"
        :src="restaurant.fotoPerfil"
        class="w-full h-full object-cover opacity-60"
      />
      <div v-else class="w-full h-full flex items-center justify-center text-6xl opacity-50">🏪</div>

      <div class="absolute inset-0 bg-linear-to-t from-black/80 to-transparent"></div>

      <div class="absolute bottom-0 left-0 w-full p-6 text-white">
        <div class="max-w-4xl mx-auto">
          <button @click="$router.push('/usuario/dashboard')" class="text-sm text-gray-300 hover:text-white mb-2">← Voltar</button>
          <div class="flex justify-between items-end">
            <div>
              <h1 class="text-3xl font-bold mb-1">{{ restaurant.nome }}</h1>
              <p class="text-gray-300 text-sm mb-2">{{ restaurant.descricao }}</p>
              <div class="flex gap-4 text-sm font-medium">
                <span class="text-yellow-400">⭐ {{ restaurant.avaliacao }}</span>
                <span>🕒 {{ restaurant.tempoMedioEntrega || '40-50 min' }}</span>
                <span :class="restaurant.ativo ? 'text-green-400' : 'text-red-400'">
                  {{ restaurant.ativo ? '● Aberto' : '● Fechado' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <main class="max-w-4xl mx-auto px-4 py-8">
      <h2 class="text-xl font-bold text-gray-800 mb-6 border-b pb-2">Cardápio</h2>

      <div v-if="loading" class="py-10 text-center text-gray-500">Carregando cardápio...</div>

      <div v-else class="grid gap-4">
        <div
          v-for="prato in menu"
          :key="prato.idPrato"
          class="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex justify-between items-center hover:shadow-md transition-shadow"
          :class="{ 'opacity-50 pointer-events-none': !prato.disponivel || !restaurant.ativo }"
        >
          <div class="flex-1 pr-4">
            <h3 class="font-bold text-gray-900">{{ prato.nome }}</h3>
            <p class="text-sm text-gray-500 line-clamp-2 mb-2">{{ prato.descricao }}</p>
            <p class="font-bold text-green-600">R$ {{ prato.preco.toFixed(2).replace('.', ',') }}</p>
          </div>

          <button
            @click="openItemModal(prato)"
            class="bg-orange-100 text-orange-600 px-4 py-2 rounded-lg font-bold hover:bg-orange-200 transition"
            :disabled="!prato.disponivel || !restaurant.ativo"
          >
            Adicionar
          </button>
        </div>
      </div>
    </main>

    <div v-if="cart.length > 0" class="fixed bottom-0 left-0 w-full bg-white border-t border-gray-200 shadow-2xl p-4 z-50 animate-slide-up">
      <div class="max-w-4xl mx-auto flex justify-between items-center">
        <div>
          <p class="text-sm text-gray-500">Total do Pedido</p>
          <p class="text-xl font-bold text-gray-900">R$ {{ cartTotal.toFixed(2).replace('.', ',') }}</p>
          <p class="text-xs text-gray-400">{{ cart.length }} itens</p>
        </div>
        <button
          @click="finalizeOrder"
          :disabled="sendingOrder"
          class="bg-green-600 text-white px-8 py-3 rounded-xl font-bold text-lg hover:bg-green-700 transition shadow-lg disabled:opacity-50 flex items-center gap-2"
        >
          <span v-if="sendingOrder">Enviando...</span>
          <span v-else>Concluir Pedido ✅</span>
        </button>
      </div>
    </div>

    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4" @click="showModal = false">
      <div class="bg-white rounded-xl p-6 w-full max-w-sm text-center" @click.stop>
        <h3 class="text-lg font-bold mb-2">{{ selectedItem.nome }}</h3>
        <p class="text-gray-500 mb-6">{{ selectedItem.descricao }}</p>

        <div class="flex justify-center items-center gap-6 mb-8">
          <button @click="quantity > 1 ? quantity-- : null" class="w-10 h-10 rounded-full bg-gray-100 text-xl font-bold hover:bg-gray-200">-</button>
          <span class="text-2xl font-bold w-8">{{ quantity }}</span>
          <button @click="quantity++" class="w-10 h-10 rounded-full bg-orange-100 text-orange-600 text-xl font-bold hover:bg-orange-200">+</button>
        </div>

        <button
          @click="addToCart"
          class="w-full bg-orange-600 text-white py-3 rounded-xl font-bold hover:bg-orange-700"
        >
          Adicionar R$ {{ (selectedItem.preco * quantity).toFixed(2) }}
        </button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import AuthService from '@/services/authService';

const route = useRoute();
const router = useRouter();
const API_BASE_URL = 'http://localhost:8080';
const restaurantId = route.params.id;

// --- Estado ---
const restaurant = ref<any>({});
const menu = ref<any[]>([]);
const cart = ref<any[]>([]);
const loading = ref(true);
const sendingOrder = ref(false);

// Modal Estado
const showModal = ref(false);
const selectedItem = ref<any>({});
const quantity = ref(1);

// --- Computados ---
const cartTotal = computed(() => {
  return cart.value.reduce((acc, item) => acc + (item.preco * item.quantidade), 0);
});

// --- Funções de Busca ---
const fetchData = async () => {
  loading.value = true;
  try {
    // 1. Dados do Restaurante (Público)
    const resInfo = await axios.get(`${API_BASE_URL}/restaurantes/${restaurantId}`);
    restaurant.value = resInfo.data;

    // 2. Cardápio (Público)
    const resMenu = await axios.get(`${API_BASE_URL}/restaurantes/${restaurantId}/pratos`);
    menu.value = resMenu.data;
  } catch (error) {
    console.error('Erro ao carregar restaurante:', error);
    alert('Restaurante não encontrado.');
    router.push('/usuario/dashboard');
  } finally {
    loading.value = false;
  }
};

// --- Funções do Carrinho ---
const openItemModal = (item: any) => {
  selectedItem.value = item;
  quantity.value = 1;
  showModal.value = true;
};

const addToCart = () => {
  const existing = cart.value.find(i => i.idPrato === selectedItem.value.idPrato);

  if (existing) {
    existing.quantidade += quantity.value;
  } else {
    cart.value.push({
      idPrato: selectedItem.value.idPrato,
      nome: selectedItem.value.nome,
      preco: selectedItem.value.preco,
      quantidade: quantity.value
    });
  }
  showModal.value = false;
};

// --- FINALIZAR PEDIDO (Checkout) ---
const finalizeOrder = async () => {
  if (!confirm(`Confirmar pedido de R$ ${cartTotal.value.toFixed(2)}?`)) return;

  sendingOrder.value = true;
  const token = AuthService.getToken();

  // Montar Payload conforme o Backend espera
  // { idRestaurante: X, valorTotal: Y, itens: [...] }
  const payload = {
    idRestaurante: parseInt(restaurantId as string),
    valorTotal: cartTotal.value,
    itens: cart.value.map(item => ({
      idPrato: item.idPrato,
      quantidade: item.quantidade,
      preco: item.preco // O backend recalcula, mas enviamos por histórico
    }))
  };

  try {
    await axios.post(`${API_BASE_URL}/usuarios`, payload, {
      headers: { 'Authorization': `Bearer ${token}` }
    });

    alert('Pedido realizado com sucesso! 🛵');
    router.push('/usuario/pedidos'); // Manda para a tela de histórico
  } catch (error) {
    console.error('Erro ao finalizar:', error);
    if (axios.isAxiosError(error) && error.response?.status === 400) {
      alert(`Erro: ${error.response.data}`); // Ex: Restaurante fechado
    } else {
      alert('Erro ao realizar pedido. Tente novamente.');
    }
  } finally {
    sendingOrder.value = false;
  }
};

onMounted(fetchData);
</script>

<style scoped>
.animate-slide-up {
  animation: slideUp 0.3s ease-out;
}
@keyframes slideUp {
  from { transform: translateY(100%); }
  to { transform: translateY(0); }
}
</style>
