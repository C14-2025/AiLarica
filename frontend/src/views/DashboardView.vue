<script setup lang="ts"></script>

<template>
  <div class="container">
    <h1>Dogão do Jairo (Dashboard)</h1>
    <button>🟢 Ativo/Inativo</button>
    <hr>
    <h3>CARDÁPIO</h3>
    <ul>
      <li>Dogão 18cm</li>
      <li>Dogão 30cm</li>
    </ul>
    <button @click="$router.push('/pedidos')">Ver 3 Pedidos</button>
    <br><br>
    <small>(Wireframe: Tela da Esquerda)</small>
  </div>
</template>

<style scoped></style>
