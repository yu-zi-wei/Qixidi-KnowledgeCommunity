/**
 * 登录对话框全局状态管理
 * 使用 Pinia 确保 SSR 兼容和状态共享
 */
export const useAuthDialogStore = defineStore('authDialog', () => {
  const visible = ref(false)
  const redirectAfterLogin = ref('')

  const showLoginDialog = (redirect = '') => {
    redirectAfterLogin.value = redirect
    visible.value = true
  }

  const hideLoginDialog = () => {
    visible.value = false
  }

  const handleLoginSuccess = () => {
    const redirect = redirectAfterLogin.value
    hideLoginDialog()
    if (redirect) {
      navigateTo(redirect)
      redirectAfterLogin.value = ''
    }
  }

  return {
    visible,
    redirectAfterLogin,
    showLoginDialog,
    hideLoginDialog,
    handleLoginSuccess
  }
})
