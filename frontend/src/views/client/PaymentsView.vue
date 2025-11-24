<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Meus Pagamentos</h1>

    <div class="bg-white p-6 rounded-xl shadow-lg max-w-2xl mx-auto">
      <h2 class="text-xl font-semibold mb-4 border-b pb-2">Cartões Cadastrados</h2>

      <div v-if="cards.length === 0" class="text-center py-5 text-gray-500">
        Nenhum cartão cadastrado.
      </div>

      <div v-else class="space-y-4">
        <div v-for="card in cards" :key="card.id" class="border p-4 rounded-lg flex justify-between items-center bg-gray-50">
          <div>
            <p class="font-medium">{{ card.brand }} **** **** **** {{ card.lastFour }}</p>
            <p class="text-sm text-gray-600">Vencimento: {{ card.expiry }}</p>
          </div>
          <button @click="removeCard(card.id)" class="text-red-500 hover:text-red-700 text-sm">Remover</button>
        </div>
      </div>

      <button @click="addCard" class="mt-6 w-full bg-red-500 text-white py-3 rounded-lg font-semibold hover:bg-red-600 transition-colors">
        + Adicionar Novo Cartão
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { userService } from '../../services/userService';

const cards = ref([
  { id: 1, brand: 'Visa', lastFour: '1234', expiry: '12/28' },
  { id: 2, brand: 'Mastercard', lastFour: '5678', expiry: '06/26' },
]);

const removeCard = (id: number) => {
  if (confirm('Tem certeza que deseja remover este cartão?')) {
    cards.value = cards.value.filter(card => card.id !== id);
    alert('Cartão removido com sucesso!');
  }
};

const addCard = () => {
  // Lógica para abrir modal ou redirecionar para formulário de adição de cartão
  alert('Funcionalidade de adicionar cartão em desenvolvimento.');
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>

const loadPaymentCards = async () => {
  try {
    const data = await userService.getPaymentCards();
    cards.value = data;
  } catch (error) {
    console.error('Erro ao carregar cartoes:', error);
  }
};

const removeCardAsync = async (id: number) => {
  if (confirm('Tem certeza que deseja remover este cartao?')) {
    try {
      await userService.deletePaymentCard(id);
      cards.value = cards.value.filter(card => card.id !== id);
      alert('Cartao removido com sucesso!');
    } catch (error) {
      console.error('Erro ao remover cartao:', error);
      alert('Erro ao remover cartao. Tente novamente.');
    }
  }
};

onMounted(() => {
  loadPaymentCards();
});
