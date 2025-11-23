<template>
  <div :class="['p-4 rounded-xl shadow-lg flex items-center justify-between', bgColor]">
    <div>
      <p class="text-sm font-medium text-gray-500">{{ title }}</p>
      <p class="text-2xl font-bold text-gray-900 mt-1">{{ value }}</p>
    </div>
    <div class="flex flex-col items-end">
      <div :class="['p-2 rounded-full', iconBgColor]">
        <svg v-if="icon === 'package'" class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path></svg>
        <svg v-else-if="icon === 'currency'" class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V3m0 13v5m-3-2h6m-6-6h6"></path></svg>
        <svg v-else-if="icon === 'clock'" class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <svg v-else-if="icon === 'star'" class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.519 4.674c.3.921-.755 1.688-1.539 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.784.57-1.838-.197-1.539-1.118l1.519-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.382-1.81.588-1.81h4.915a1 1 0 00.95-.69l1.519-4.674z"></path></svg>
      </div>
      <div v-if="trend" class="mt-2 text-xs font-medium" :class="trend > 0 ? 'text-green-600' : 'text-red-600'">
        {{ trend > 0 ? '+' : '' }}{{ trend }}%
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  title: string;
  value: string | number;
  icon: 'package' | 'currency' | 'clock' | 'star';
  trend?: number;
  bgColor: string;
}>();

const iconBgColor = computed(() => {
  if (props.icon === 'package') return 'bg-blue-200';
  if (props.icon === 'currency') return 'bg-green-200';
  if (props.icon === 'clock') return 'bg-yellow-200';
  if (props.icon === 'star') return 'bg-purple-200';
  return 'bg-gray-200';
});
</script>
