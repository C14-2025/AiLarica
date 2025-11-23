<template>
  <div class="bg-white p-6 rounded-xl shadow-lg">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold text-gray-800">Visão Geral do Cardápio</h2>
      <button @click="$emit('viewAll')" class="text-sm text-blue-600 hover:text-blue-800 font-medium">
        Ver Cardápio Completo
      </button>
    </div>

    <ul class="space-y-4">
      <li v-for="item in items" :key="item.id" class="flex justify-between items-center border-b pb-2 last:border-b-0">
        <div>
          <p class="font-medium text-gray-900">{{ item.name }}</p>
          <p class="text-sm text-gray-500">{{ item.description }}</p>
        </div>
        <div class="text-right">
          <p class="font-semibold text-gray-700">R$ {{ item.price.toFixed(2) }}</p>
          <p class="text-xs" :class="item.available ? 'text-green-600' : 'text-red-600'">
            {{ item.available ? 'Disponível' : 'Indisponível' }}
          </p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  items: {
    id: string;
    name: string;
    description: string;
    price: number;
    available: boolean;
    salesCount: number;
  }[];
}>();

defineEmits(['viewAll']);
</script>
