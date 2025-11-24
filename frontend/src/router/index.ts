import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/restaurante/dashboard',
      name: 'restaurante-dashboard',
      component: DashboardView,
      meta: { requiresAuth: true, role: 'restaurant' }
    },
    {
      path: '/restaurante/pedidos',
      name: 'restaurante-pedidos',
      component: () => import('../views/PedidosView.vue'),
      meta: { requiresAuth: true, role: 'restaurant' }
    },
    {
      path: '/restaurante/cardapio',
      name: 'restaurante-cardapio',
      component: () => import('../views/CardapioView.vue'),
      meta: { requiresAuth: true, role: 'restaurant' }
    },
    {
      path: '/restaurante/settings',
      name: 'restaurante-settings',
      component: () => import('../views/SettingsView.vue'),
      meta: { requiresAuth: true, role: 'restaurant' }
    }
  ]
})

export default router
