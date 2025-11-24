<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Avaliar Pedido #{{ pedidoId }}</h1>

    <div class="bg-white p-6 rounded-xl shadow-lg max-w-2xl mx-auto">
      <h2 class="text-xl font-semibold mb-4">Como foi sua experiência com o {{ restaurantName }}?</h2>

      <div class="space-y-6">
        <!-- Avaliação de Estrelas -->
        <div>
          <label class="block text-lg font-medium text-gray-700 mb-2">Qualidade Geral:</label>
          <div class="flex space-x-1">
            <button
              v-for="star in 5"
              :key="star"
              @click="rating = star"
              class="text-4xl transition-colors"
              :class="star <= rating ? 'text-yellow-500' : 'text-gray-300 hover:text-yellow-400'"
            >
              ★
            </button>
          </div>
          <p class="text-sm text-gray-500 mt-1">{{ ratingText }}</p>
        </div>

        <!-- Comentário -->
        <div>
          <label for="comment" class="block text-lg font-medium text-gray-700 mb-2">Comentário (Opcional):</label>
          <textarea
            id="comment"
            v-model="comment"
            rows="4"
            class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"
            placeholder="Conte-nos mais sobre sua experiência..."
          ></textarea>
        </div>

        <!-- Botão de Envio -->
        <button
          @click="submitRating"
          :disabled="rating === 0"
          class="w-full bg-red-500 text-white py-3 rounded-lg font-semibold hover:bg-red-600 transition-colors disabled:bg-gray-400"
        >
          Enviar Avaliação
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { orderService } from '../../services/orderService';

const route = useRoute();
const router = useRouter();
const pedidoId = route.params.pedidoId as string;

// Mock de dados do pedido para avaliação
const restaurantName = ref('Pizzaria do Chef');

const rating = ref(0);
const comment = ref('');

const ratingText = computed(() => {
  switch (rating.value) {
    case 1: return 'Péssimo';
    case 2: return 'Ruim';
    case 3: return 'Regular';
    case 4: return 'Bom';
    case 5: return 'Excelente';
    default: return 'Selecione uma nota';
  }
});

const submitRating = async () => {
  if (rating.value === 0) {
    alert('Por favor, selecione uma nota para avaliar.');
    return;
  }

  try {
    await orderService.rateOrder(parseInt(pedidoId), rating.value, comment.value);
    alert(`Avaliacao de ${rating.value} estrelas enviada com sucesso para o pedido #${pedidoId}!`);
    router.push({ name: 'MeusPedidos' });
  } catch (error) {
    console.error('Erro ao enviar avaliacao:', error);
    alert('Erro ao enviar avaliacao. Tente novamente.');
  }
};
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
