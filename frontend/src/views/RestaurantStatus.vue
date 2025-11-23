<template>
  <div class="bg-white p-6 rounded-xl shadow-lg">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold text-gray-800">Status do Restaurante</h2>
      <span :class="['px-3 py-1 rounded-full text-sm font-medium', isOpen ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']">
        {{ isOpen ? 'Aberto' : 'Fechado' }}
      </span>
    </div>

    <div class="flex items-center justify-between">
      <p class="text-sm text-gray-600">
        O restaurante está atualmente <span class="font-bold">{{ isOpen ? 'recebendo' : 'fechado para' }}</span> novos pedidos.
      </p>
      <button
        @click="$emit('toggleStatus')"
        :class="['px-4 py-2 rounded-lg text-white font-medium transition duration-150 ease-in-out', isOpen ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600']"
      >
        {{ isOpen ? 'Fechar Agora' : 'Abrir Agora' }}
      </button>
    </div>

    <div class="mt-4 pt-4 border-t border-gray-200 grid grid-cols-2 gap-4 text-sm text-gray-600">
      <div>
        <p class="font-medium text-gray-800">Tempo Médio de Entrega:</p>
        <p>{{ avgTime }} minutos</p>
      </div>
      <div>
        <p class="font-medium text-gray-800">Taxa de Aceitação de Pedidos:</p>
        <p>{{ acceptanceRate }}%</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  restaurantName: string;
  isOpen: boolean;
  avgTime: number;
  acceptanceRate: number;
}>();

defineEmits(['toggleStatus']);
</script>
