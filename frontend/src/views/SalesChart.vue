<template>
  <div class="bg-white p-6 rounded-xl shadow-lg">
    <h2 class="text-xl font-semibold text-gray-800 mb-4">Vendas Semanais (R$)</h2>
    <div class="h-64">
      <!-- Simulação de um gráfico simples com barras -->
      <div class="flex h-full items-end justify-between">
        <div v-for="item in data" :key="item.label" class="flex flex-col items-center justify-end h-full w-1/7 px-1">
          <div
            :style="{ height: `${(item.value / maxSales) * 100}%` }"
            class="w-full bg-orange-500 rounded-t-lg transition-all duration-500 ease-out"
          ></div>
          <span class="text-xs text-gray-500 mt-1">{{ item.label }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  data: { label: string; value: number }[];
}>();

const maxSales = computed(() => {
  return Math.max(...props.data.map(item => item.value));
});
</script>
