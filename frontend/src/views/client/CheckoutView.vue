<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Finalizar Pedido</h1>

    <div class="flex flex-col lg:flex-row gap-6">
      <!-- Detalhes do Pedido e Endereço -->
      <div class="lg:w-2/3 space-y-6">
        <!-- Endereço de Entrega -->
        <div class="bg-white p-6 rounded-xl shadow-lg">
          <h2 class="text-xl font-semibold mb-4 border-b pb-2">1. Endereço de Entrega</h2>
          <div class="space-y-3">
            <p class="font-medium">Endereço Selecionado:</p>
            <p class="text-gray-700">Rua das Flores, 123 - Apto 401, Centro - Cidade/UF</p>
            <button class="text-red-500 hover:text-red-700 text-sm">Alterar Endereço</button>
          </div>
        </div>

        <!-- Forma de Pagamento -->
        <div class="bg-white p-6 rounded-xl shadow-lg">
          <h2 class="text-xl font-semibold mb-4 border-b pb-2">2. Forma de Pagamento</h2>
          <div class="space-y-3">
            <label class="flex items-center space-x-3 cursor-pointer">
              <input type="radio" name="payment" value="credit_card" v-model="paymentMethod" class="form-radio text-red-500 h-4 w-4" />
              <span class="text-gray-700">Cartão de Crédito/Débito</span>
            </label>
            <label class="flex items-center space-x-3 cursor-pointer">
              <input type="radio" name="payment" value="pix" v-model="paymentMethod" class="form-radio text-red-500 h-4 w-4" />
              <span class="text-gray-700">PIX</span>
            </label>
            <label class="flex items-center space-x-3 cursor-pointer">
              <input type="radio" name="payment" value="cash" v-model="paymentMethod" class="form-radio text-red-500 h-4 w-4" />
              <span class="text-gray-700">Dinheiro (Troco para R$ <input type="number" v-model="changeFor" class="border rounded-md w-20 text-center ml-2" placeholder="0.00" />)</span>
            </label>
          </div>
        </div>

        <!-- Observações -->
        <div class="bg-white p-6 rounded-xl shadow-lg">
          <h2 class="text-xl font-semibold mb-4 border-b pb-2">3. Observações</h2>
          <textarea v-model="notes" rows="3" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500" placeholder="Ex: Sem cebola, ponto da carne, etc."></textarea>
        </div>
      </div>

      <!-- Resumo do Pedido e Botão de Finalizar -->
      <div class="lg:w-1/3 bg-white p-6 rounded-xl shadow-lg h-fit">
        <h2 class="text-xl font-bold mb-4 border-b pb-2">Resumo do Pedido</h2>
        <div class="space-y-2 text-gray-700">
          <div v-for="item in cartItems" :key="item.id" class="flex justify-between text-sm">
            <span>{{ item.quantity }}x {{ item.name }}</span>
            <span>R$ {{ (item.price * item.quantity).toFixed(2) }}</span>
          </div>
          <div class="flex justify-between pt-2 border-t mt-2">
            <span>Subtotal:</span>
            <span>R$ {{ subtotal.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Taxa de Entrega:</span>
            <span>R$ {{ deliveryFee.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between font-bold text-lg pt-2 border-t mt-2">
            <span>Total a Pagar:</span>
            <span class="text-red-500">R$ {{ total.toFixed(2) }}</span>
          </div>
        </div>
        <button @click="placeOrder" class="mt-6 w-full block text-center bg-red-500 text-white py-3 rounded-lg font-semibold hover:bg-red-600 transition-colors disabled:bg-gray-400" :disabled="!paymentMethod">
          Confirmar Pedido (R$ {{ total.toFixed(2) }})
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '../../stores/cartStore';
import { orderService } from '../../services/orderService';

const router = useRouter();
const cartStore = useCartStore();
const cartItems = cartStore.cartItems;

const mockCartItems = [
  { id: 101, name: 'Pizza Calabresa', price: 45.00, quantity: 1 },
  { id: 201, name: 'Combinado Salmão', price: 89.90, quantity: 2 },
]);

const deliveryFee = cartStore.deliveryFee;
const paymentMethod = ref('');
const changeFor = ref(0);
const notes = ref('');

const subtotal = cartStore.subtotal;
const total = cartStore.total;

const placeOrder = async () => {
  if (!paymentMethod.value) {
    alert('Por favor, selecione uma forma de pagamento.');
    return;
  }
  try {
    const orderData = {
      items: cartItems.value,
      paymentMethod: paymentMethod.value,
      changeFor: paymentMethod.value === 'cash' ? changeFor.value : null,
      notes: notes.value,
      total: total.value,
    };
    await orderService.createOrder(orderData);
    cartStore.clearCart();
    alert('Pedido realizado com sucesso! Você será redirecionado para o acompanhamento.');
    router.push({ name: 'MeusPedidos' });
  } catch (error) {
    console.error('Erro ao criar pedido:', error);
    alert('Erro ao realizar pedido. Tente novamente.');
  }
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
