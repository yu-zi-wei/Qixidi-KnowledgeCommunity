/**
 * mcp 管理页专属门槛：创作者或管理员（roleId ≥ 2）
 * 与后端 AgentApiKeyController / AgentSkillController 的写操作校验对齐。
 * 已登录但非创作者 → 弹申请创作者弹窗引导（mcp 接入是创作者权益）
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

  const roleId = Number(authStore.user?.roleId ?? 1)
  if (roleId < 2) {
    if (import.meta.client && from) {
      const creatorApplyStore = useCreatorApplyStore()
      creatorApplyStore.show()
      return abortNavigation()
    }
    return navigateTo('/')
  }
})
