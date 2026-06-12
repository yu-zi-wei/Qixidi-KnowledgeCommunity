export default defineNuxtPlugin(async () => {
  const authStore = useAuthStore()
  if (authStore.token) {
    await authStore.fetchUser()
  }
})
