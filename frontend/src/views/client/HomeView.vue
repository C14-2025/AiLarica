<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Restaurantes Próximos</h1>
    <!-- Componente de Busca de Restaurantes -->
    <div class="mb-6">
      <input
        type="text"
        v-model="searchQuery"
        @input="handleSearch"
        placeholder="Buscar restaurantes ou pratos..."
        class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"
      />
    </div>

    <!-- Lista de Restaurantes (Mock) -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="restaurant in restaurants"
        :key="restaurant.id"
        class="bg-white rounded-xl shadow-lg overflow-hidden cursor-pointer hover:shadow-xl transition-shadow duration-300"
        @click="goToRestaurant(restaurant.id)"
      >
        <img :src="restaurant.image" alt="Imagem do Restaurante" class="w-full h-40 object-cover" />
        <div class="p-4">
          <h2 class="text-xl font-semibold text-gray-800">{{ restaurant.name }}</h2>
          <p class="text-sm text-gray-500 mt-1">{{ restaurant.category }}</p>
          <div class="flex items-center mt-2">
            <span class="text-yellow-500 mr-2">★ {{ restaurant.rating }}</span>
            <span class="text-sm text-gray-500">({{ restaurant.reviews }} avaliações)</span>
          </div>
          <p class="text-sm text-gray-600 mt-2">Entrega: {{ restaurant.deliveryTime }} min • R$ {{ restaurant.deliveryFee }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { restaurantService } from '../../services/restaurantService';

const router = useRouter();
const searchQuery = ref('');
const restaurants = ref([
  { id: 1, name: 'Pizzaria do Chef', category: 'Pizzaria', rating: 4.5, reviews: 500, deliveryTime: 30, deliveryFee: 5.00, image: 'https://via.placeholder.com/400x200/FF5757/FFFFFF?text=Pizzaria' },
  { id: 2, name: 'Japa Food Express', category: 'Japonesa', rating: 4.8, reviews: 820, deliveryTime: 45, deliveryFee: 8.00, image: 'https://via.placeholder.com/400x200/FF5757/FFFFFF?text=Japonesa' },
  { id: 3, name: 'Hamburgueria Gourmet', category: 'Lanches', rating: 4.2, reviews: 310, deliveryTime: 25, deliveryFee: 3.50, image: 'https://via.placeholder.com/400x200/FF5757/FFFFFF?text=Hamburgueria' },
  { id: 4, name: 'Sabor Nordestino', category: 'Brasileira', rating: 4.6, reviews: 650, deliveryTime: 50, deliveryFee: 6.00, image: 'https://via.placeholder.com/400x200/FF5757/FFFFFF?text=Nordestino' },
]);

const goToRestaurant = (id: number) => {
  router.push({ name: 'RestauranteDetalhe', params: { id } });
};

const handleSearch = async () => {
  if (searchQuery.value.trim() === '') {
    await loadRestaurants();
  } else {
    try {
      const results = await restaurantService.searchRestaurantes(searchQuery.value);
      restaurants.value = results;
    } catch (error) {
      console.error('Erro ao buscar:', error);
    }
  }
};

const loadRestaurants = async () => {
  try {
    const data = await restaurantService.getAllRestaurantes();
    restaurants.value = data;
  } catch (error) {
    console.error('Erro ao carregar restaurantes:', error);
  }
};

onMounted(() => {
  loadRestaurants();
});
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
