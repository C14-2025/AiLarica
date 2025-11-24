import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'
import authService from '../services/authService'

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
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresAuth: false }
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
      path: '/usuario/home',
      name: 'Home',
      component: () => import('../views/client/HomeView.vue'),
      meta: { requiresAuth: false, layout: 'ClientLayout' }
    },
    {
      path: '/restaurante/:id',
      name: 'RestauranteDetalhe',
      component: () => import('../views/client/RestaurantDetailView.vue'),
      meta: { requiresAuth: false, layout: 'ClientLayout' }
    },
    {
      path: '/carrinho',
      name: 'Carrinho',
      component: () => import('../views/client/CartView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    },
    {
      path: '/checkout',
      name: 'Checkout',
      component: () => import('../views/client/CheckoutView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    },
    {
      path: '/pedidos',
      name: 'MeusPedidos',
      component: () => import('../views/client/OrdersView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    },
    {
      path: '/perfil',
      name: 'Perfil',
      component: () => import('../views/client/ProfileView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    },
    {
      path: '/pagamentos',
      name: 'Pagamentos',
      component: () => import('../views/client/PaymentsView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    },
    {
      path: '/avaliacao/:pedidoId',
      name: 'Avaliacao',
      component: () => import('../views/client/RatingView.vue'),
      meta: { requiresAuth: true, layout: 'ClientLayout' }
    }
  ]
})

// Guard de navegação global
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = authService.isAuthenticated()

  if (requiresAuth && !isAuthenticated) {
    // Rota requer autenticação mas usuário não está autenticado
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    // Usuário já está autenticado tentando acessar login
    next('/restaurante/dashboard')
  } else {
    // Permite a navegação
    next()
  }
})

export default router
