/**
 * 登录权限中间件
 * 只需登录即可访问后台管理和写作页面
 */
export default defineNuxtRouteMiddleware((to, from) => {
  const authStore = useAuthStore()

  if (!authStore.isLoggedIn) {
    if (import.meta.client && from) {
      const authDialogStore = useAuthDialogStore()
      authDialogStore.showLoginDialog(to.fullPath)
      return abortNavigation()
    }
    return navigateTo('/')
  }
})
