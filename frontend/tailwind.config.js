/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'ailarica-red': '#FF5757',
        'ailarica-orange': '#FF9800',
        'ailarica-dark': '#2C2C2C',
        'ailarica-light': '#F5F5F5',
      },
      fontFamily: {
        sans: ['Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'sans-serif'],
      },
    },
  },
  plugins: [],
}