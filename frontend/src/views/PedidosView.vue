<script setup lang="ts"></script>

<template>
  <div class="container">
    <button @click="$router.push('/dashboard')">Voltar</button>
    <h1>Pedidos Ativos</h1>

    <div style="border: 1px solid black; margin: 10px; padding: 10px;">
      <h3>Dogão 18cm - para JP</h3>
      <p>Rua arrombada do norte</p>
      <button>Chat</button>
      <button>Mandar entregar 🛵</button>
    </div>

    <div style="border: 1px solid black; margin: 10px; padding: 10px;">
      <h3>Dogão 30cm - para Edu</h3>
      <p>Rua da silva</p>
      <button>Chat</button>
      <button>Mandar entregar 🛵</button>
    </div>

    <small>(Wireframe: Tela da Direita)</small>
  </div>
</template>

<style scoped></style>
