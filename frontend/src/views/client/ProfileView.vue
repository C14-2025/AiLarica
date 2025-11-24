<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-6 border-b pb-2">Meu Perfil</h1>

    <div class="bg-white p-6 rounded-xl shadow-lg max-w-2xl mx-auto">
      <form @submit.prevent="updateProfile" class="space-y-4">
        <div class="flex items-center space-x-4 mb-6">
          <div class="w-20 h-20 rounded-full bg-red-500 flex items-center justify-center text-white text-3xl font-bold">
            {{ initials }}
          </div>
          <div>
            <h2 class="text-xl font-semibold">{{ user.name }}</h2>
            <p class="text-gray-500">{{ user.email }}</p>
          </div>
        </div>

        <div>
          <label for="name" class="block text-sm font-medium text-gray-700">Nome Completo</label>
          <input
            type="text"
            id="name"
            v-model="user.name"
            class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"
            required
          />
        </div>

        <div>
          <label for="phone" class="block text-sm font-medium text-gray-700">Telefone</label>
          <input
            type="tel"
            id="phone"
            v-model="user.phone"
            class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"
            required
          />
        </div>

        <div>
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <input
            type="email"
            id="email"
            v-model="user.email"
            class="mt-1 block w-full p-3 border border-gray-300 rounded-lg bg-gray-100 cursor-not-allowed"
            disabled
          />
        </div>

        <div class="pt-4">
          <button
            type="submit"
            class="w-full bg-red-500 text-white py-3 rounded-lg font-semibold hover:bg-red-600 transition-colors"
          >
            Salvar Alterações
          </button>
        </div>
      </form>

      <div class="mt-8 border-t pt-6">
        <h3 class="text-lg font-semibold mb-3">Endereços Cadastrados</h3>
        <div v-for="(address, index) in user.addresses" :key="index" class="border p-4 rounded-lg mb-3 flex justify-between items-center">
          <div>
            <p class="font-medium">{{ address.street }}, {{ address.number }}</p>
            <p class="text-sm text-gray-600">{{ address.neighborhood }} - {{ address.city }}/{{ address.state }}</p>
          </div>
          <button class="text-red-500 hover:text-red-700 text-sm">Editar</button>
        </div>
        <button class="text-red-500 hover:text-red-700 text-sm mt-2">+ Adicionar Novo Endereço</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { userService } from '../../services/userService';

const user = ref({
  name: 'João da Silva',
  email: 'joao.silva@email.com',
  phone: '(31) 99876-5432',
  addresses: [
    { street: 'Rua das Flores', number: '123', neighborhood: 'Centro', city: 'Belo Horizonte', state: 'MG' },
    { street: 'Avenida Principal', number: '456', neighborhood: 'Jardim', city: 'Contagem', state: 'MG' },
  ],
});

const initials = computed(() => {
  const parts = user.value.name.split(' ');
  if (parts.length >= 2) {
    return (parts[0]?.[0] ?? '') + (parts[parts.length - 1]?.[0] ?? '');
  }
  return parts[0]?.[0] ?? '';
});

// Removed assignment of initials to user object

const updateProfile = async () => {
  try {
    const updatedUser = await userService.updateUser({
      name: user.value.name,
      phone: user.value.phone,
    });
    user.value = updatedUser;
    alert('Perfil atualizado com sucesso!');
  } catch (error) {
    console.error('Erro ao atualizar perfil:', error);
    alert('Erro ao atualizar perfil. Tente novamente.');
  }
};

const loadUserData = async () => {
  try {
    const userData = await userService.getUser();
    user.value = userData;
  } catch (error) {
    console.error('Erro ao carregar dados do usuario:', error);
  }
};

onMounted(() => {
  loadUserData();
});
</script>

<style scoped>
/* Estilos específicos se necessário */
</style>
