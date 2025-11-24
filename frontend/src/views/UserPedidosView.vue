<template>
  <div class="min-h-screen bg-gray-50 p-8">
    <div class="max-w-4xl mx-auto">
      <div class="flex items-center justify-between mb-8">
        <h1 class="text-2xl font-bold text-gray-800">Meus Pedidos</h1>
        <button @click="$router.push('/usuario/dashboard')" class="text-orange-600 font-bold hover:underline">← Voltar para Restaurantes</button>
      </div>

      <div class="bg-white rounded-xl shadow p-6" v-if="pedidos.length === 0">
        <p class="text-center text-gray-500">Você ainda não fez nenhum pedido.</p>
      </div>

      <div v-else class="space-y-4">
        <div v-for="pedido in pedidos" :key="pedido.idPedido" class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div class="flex justify-between items-center mb-4">
            <div>
              <p class="text-sm text-gray-500">Pedido #{{ pedido.idPedido }}</p>
              <p class="font-bold text-lg">Data: {{ new Date(pedido.dataHora).toLocaleDateString() }}</p>
            </div>
            <span class="px-3 py-1 rounded-full text-sm font-bold bg-gray-100 text-gray-700">
              {{ pedido.status }}
            </span>
          </div>
          <div class="flex justify-between items-end border-t pt-4">
            <p class="text-gray-600">{{ pedido.itens.length }} itens</p>
            <p class="text-xl font-bold text-orange-600">R$ {{ pedido.valorTotal.toFixed(2) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import AuthService from '@/services/authService';

const pedidos = ref([]);
const token = AuthService.getToken();

const fetchMeusPedidos = async () => {
  try {
    const response = await axios.get('http://localhost:8080/usuarios/ativos', {
      headers: { Authorization: `Bearer ${token}` }
    });
    pedidos.value = response.data;
  } catch (error) {
    console.error(error);
  }
};

onMounted(fetchMeusPedidos);
</script>
