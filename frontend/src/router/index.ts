import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DashboardView from '../views/DashboardView.vue' // Restaurante
import UserDashboardView from '../views/UserDashBoardView.vue' // Novo Usuário
import UserPedidosView from '../views/UserPedidosView.vue' // Novo Usuário
import authService from '../services/authService'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },

    // --- Rotas de Restaurante ---
    {
      path: '/restaurante/dashboard',
      name: 'restaurante-dashboard',
      component: DashboardView,
      meta: { requiresAuth: true, role: 'RESTAURANTE' }
    },
    {
      path: '/restaurante/pedidos',
      name: 'restaurante-pedidos',
      component: () => import('../views/PedidosView.vue'),
      meta: { requiresAuth: true, role: 'RESTAURANTE' }
    },
    {
      path: '/restaurante/cardapio',
      name: 'restaurante-cardapio',
      component: () => import('../views/CardapioView.vue'),
      meta: { requiresAuth: true, role: 'RESTAURANTE' }
    },
    {
      path: '/restaurante/settings',
      name: 'restaurante-settings',
      component: () => import('../views/SettingsView.vue'),
      meta: { requiresAuth: true, role: 'RESTAURANTE' }
    },

    // --- Rotas de Usuário (NOVAS) ---
    {
      path: '/usuario/dashboard',
      name: 'usuario-dashboard',
      component: UserDashboardView,
      meta: { requiresAuth: true, role: 'USUARIO' }
    },
    {
      path: '/usuario/perfil',
      name: 'usuario-perfil',
      component: () => import('../views/UserProfileView.vue'),
      meta: { requiresAuth: true, role: 'USUARIO' }
    },
    {
      path: '/usuario/pedidos',
      name: 'usuario-pedidos',
      component: UserPedidosView,
      meta: { requiresAuth: true, role: 'USUARIO' }
    }
  ]
})

// Guard de Navegação Inteligente
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = authService.isAuthenticated()
  const currentUser = authService.getCurrentUser(); // { tipo: 'USUARIO' | 'RESTAURANTE' }

  if (requiresAuth && !isAuthenticated) {
    // 1. Não logado tentando acessar área restrita
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    // 2. Já logado tentando ir pro login -> Manda pra home correta
    if (currentUser?.tipo === 'RESTAURANTE') next('/restaurante/dashboard')
    else next('/usuario/dashboard')
  } else if (requiresAuth && to.meta.role && to.meta.role !== currentUser?.tipo) {
    // 3. Logado, mas tentando acessar área errada (Ex: Usuário tentando ver painel de restaurante)
    if (currentUser?.tipo === 'RESTAURANTE') next('/restaurante/dashboard')
    else next('/usuario/dashboard')
  } else {
    // 4. Tudo certo
    next()
  }
})

export default router
