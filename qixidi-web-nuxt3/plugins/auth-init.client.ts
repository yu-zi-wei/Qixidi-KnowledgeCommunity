export default defineNuxtPlugin(async () => {
  const authStore = useAuthStore()
  if (authStore.isLoggedIn) {
    await authStore.fetchUser()
  }
})
