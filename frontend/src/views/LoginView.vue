<script setup lang="ts"></script>

<template>
  <div class="container">
    <h1>AI LARICA - Login</h1>
    <p>Email: <input type="text" placeholder="email@email.com"></p>
    <p>Senha: <input type="password"></p>
    <button @click="$router.push('/dashboard')">Entrar (Simulação)</button>
    <br><br>
    <small>(Wireframe: Tela de Cima)</small>
  </div>
</template>

<style scoped></style>
