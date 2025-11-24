import { ref, computed } from 'vue';

interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
  restaurant: string;
  image: string;
}

const cartItems = ref<CartItem[]>([]);
const deliveryFee = ref(10.00);

export const useCartStore = () => {
  const subtotal = computed(() => {
    return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0);
  });

  const total = computed(() => {
    return subtotal.value + deliveryFee.value;
  });

  const addItem = (item: CartItem) => {
    const existingItem = cartItems.value.find(i => i.id === item.id);
    if (existingItem) {
      existingItem.quantity += item.quantity;
    } else {
      cartItems.value.push(item);
    }
  };

  const removeItem = (id: number) => {
    cartItems.value = cartItems.value.filter(i => i.id !== id);
  };

  const updateQuantity = (id: number, quantity: number) => {
    const item = cartItems.value.find(i => i.id === id);
    if (item && quantity > 0) {
      item.quantity = quantity;
    }
  };

  const clearCart = () => {
    cartItems.value = [];
  };

  return {
    cartItems,
    subtotal,
    total,
    deliveryFee,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
  };
};
