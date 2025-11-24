<template>
  <div class="p-4">
    <button @click="router.back()" class="text-red-500 hover:text-red-700 mb-4 flex items-center">
      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>
      Voltar
    </button>

    <div v-if="restaurant" class="bg-white rounded-xl shadow-lg overflow-hidden">
      <img :src="restaurant.image" alt="Imagem do Restaurante" class="w-full h-64 object-cover" />
      <div class="p-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-2">{{ restaurant.name }}</h1>
        <div class="flex items-center mb-4">
          <span class="text-yellow-500 text-xl mr-2">★ {{ restaurant.rating }}</span>
          <span class="text-gray-500">({{ restaurant.reviews }} avaliações)</span>
          <span class="mx-3 text-gray-400">|</span>
          <span class="text-gray-600">{{ restaurant.category }}</span>
        </div>
        <p class="text-lg text-gray-700 mb-6">Entrega: {{ restaurant.deliveryTime }} min • Taxa: R$ {{ restaurant.deliveryFee }}</p>

        <h2 class="text-2xl font-semibold text-gray-800 mb-4 border-b pb-2">Cardápio</h2>

        <div class="space-y-6">
          <div v-for="dish in restaurant.menu" :key="dish.id" class="flex justify-between items-center border-b pb-4">
            <div>
              <h3 class="text-xl font-medium text-gray-800">{{ dish.name }}</h3>
              <p class="text-gray-500 text-sm">{{ dish.description }}</p>
              <p class="text-red-500 font-bold mt-1">R$ {{ dish.price.toFixed(2) }}</p>
            </div>
            <button class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-colors" @click="addToCart(dish)">
              Adicionar
            </button>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="text-center p-10">
      <p class="text-xl text-gray-500">Restaurante não encontrado.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { restaurantService } from '../../services/restaurantService';
import { useCartStore } from '../../stores/cartStore';

const route = useRoute();
const router = useRouter();
const cartStore = useCartStore();
const restaurant = ref(null as any);

// Mock de dados detalhados
const mockRestaurants = [
  {
    id: 1,
    name: 'Pizzaria do Chef',
    category: 'Pizzaria',
    rating: 4.5,
    reviews: 500,
    deliveryTime: 30,
    deliveryFee: 5.00,
    image: 'https://via.placeholder.com/800x400/FF5757/FFFFFF?text=Pizzaria+do+Chef',
    menu: [
      { id: 101, name: 'Pizza Calabresa', description: 'Molho de tomate, mussarela, calabresa e cebola.', price: 45.00 },
      { id: 102, name: 'Pizza Marguerita', description: 'Molho de tomate, mussarela, manjericão e tomate.', price: 48.00 },
      { id: 103, name: 'Pizza Portuguesa', description: 'Molho, mussarela, presunto, ovo, cebola e azeitona.', price: 52.00 },
    ],
  },
  {
    id: 2,
    name: 'Japa Food Express',
    category: 'Japonesa',
    rating: 4.8,
    reviews: 820,
    deliveryTime: 45,
    deliveryFee: 8.00,
    image: 'https://via.placeholder.com/800x400/FF5757/FFFFFF?text=Japa+Food+Express',
    menu: [
      { id: 201, name: 'Combinado Salmão (20 peças)', description: 'Sashimi, niguiri e uramaki de salmão.', price: 89.90 },
      { id: 202, name: 'Temaki Filadélfia', description: 'Cone de alga com arroz, salmão e cream cheese.', price: 25.00 },
    ],
  },
];

onMounted(async () => {
  const id = parseInt(route.params.id as string);
  try {
    restaurant.value = await restaurantService.getRestauranteById(id);
  } catch (error) {
    console.error('Erro ao carregar restaurante:', error);
    // Fallback para mock data
    restaurant.value = mockRestaurants.find(r => r.id === id);
  }
});

const addToCart = (dish: any) => {
  const placeholder = 'https://via.placeholder.com/64/FF5757/FFFFFF?text=Dish';
  cartStore.addItem({
    id: dish.id,
    name: dish.name,
    price: dish.price,
    quantity: 1,
    restaurant: restaurant.value.name,
    image: placeholder,
  });
  alert(`Adicionado ao carrinho: ${dish.name}`);
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
