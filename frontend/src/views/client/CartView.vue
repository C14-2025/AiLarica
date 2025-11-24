<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Meu Carrinho</h1>

    <div v-if="cartItems.length === 0" class="text-center py-10 bg-white rounded-xl shadow-lg">
      <p class="text-xl text-gray-500">Seu carrinho está vazio.</p>
      <router-link to="/" class="text-red-500 hover:text-red-700 mt-4 inline-block">
        Voltar para a Home
      </router-link>
    </div>

    <div v-else class="flex flex-col lg:flex-row gap-6">
      <!-- Itens do Carrinho -->
      <div class="lg:w-2/3 space-y-4">
        <div
          v-for="item in cartItems"
          :key="item.id"
          class="flex items-center bg-white p-4 rounded-xl shadow-md"
        >
          <img :src="item.image" alt="Prato" class="w-16 h-16 object-cover rounded-lg mr-4" />
          <div class="flex-grow">
            <h2 class="text-lg font-semibold text-gray-800">{{ item.name }}</h2>
            <p class="text-sm text-gray-500">{{ item.restaurant }}</p>
            <p class="text-red-500 font-bold mt-1">R$ {{ item.price.toFixed(2) }}</p>
          </div>
          <div class="flex items-center space-x-3">
            <button @click="decreaseQuantity(item.id)" class="text-gray-500 hover:text-red-500">-</button>
            <span class="font-medium">{{ item.quantity }}</span>
            <button @click="increaseQuantity(item.id)" class="text-gray-500 hover:text-red-500">+</button>
            <button @click="removeItem(item.id)" class="text-red-500 hover:text-red-700 ml-4">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Resumo do Pedido -->
      <div class="lg:w-1/3 bg-white p-6 rounded-xl shadow-lg h-fit">
        <h2 class="text-xl font-bold mb-4 border-b pb-2">Resumo</h2>
        <div class="space-y-2 text-gray-700">
          <div class="flex justify-between">
            <span>Subtotal:</span>
            <span>R$ {{ subtotal.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Taxa de Entrega:</span>
            <span>R$ {{ deliveryFee.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between font-bold text-lg pt-2 border-t mt-2">
            <span>Total:</span>
            <span class="text-red-500">R$ {{ total.toFixed(2) }}</span>
          </div>
        </div>
        <router-link to="/checkout" class="mt-6 w-full block text-center bg-red-500 text-white py-3 rounded-lg font-semibold hover:bg-red-600 transition-colors">
          Finalizar Pedido
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useCartStore } from '../../stores/cartStore';

const cartStore = useCartStore();
const cartItems = cartStore.cartItems;

const mockCartItems = [
  { id: 101, name: 'Pizza Calabresa', restaurant: 'Pizzaria do Chef', price: 45.00, quantity: 1, image: 'https://via.placeholder.com/64/FF5757/FFFFFF?text=P1' },
  { id: 201, name: 'Combinado Salmão', restaurant: 'Japa Food Express', price: 89.90, quantity: 2, image: 'https://via.placeholder.com/64/FF5757/FFFFFF?text=P2' },
]);

const deliveryFee = cartStore.deliveryFee;

const subtotal = cartStore.subtotal;
const total = cartStore.total;

const increaseQuantity = (id: number) => {
  cartStore.updateQuantity(id, (cartStore.cartItems.find(i => i.id === id)?.quantity || 0) + 1);
};

const decreaseQuantity = (id: number) => {
  const item = cartStore.cartItems.find(i => i.id === id);
  if (item && item.quantity > 1) {
    cartStore.updateQuantity(id, item.quantity - 1);
  }
};

const removeItem = (id: number) => {
  cartStore.removeItem(id);
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
