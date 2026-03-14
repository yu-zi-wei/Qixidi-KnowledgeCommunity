/**
 * 全局登录框管理（Pinia Store 的别名）
 * 提供统一的登录框显示/隐藏控制
 */
export const useAuthDialog = () => {
  const authDialogStore = useAuthDialogStore()

  return {
    visible: computed(() => authDialogStore.visible),
    redirectAfterLogin: computed(() => authDialogStore.redirectAfterLogin),
    showLoginDialog: authDialogStore.showLoginDialog,
    hideLoginDialog: authDialogStore.hideLoginDialog,
    setVisible: (val: boolean) => { authDialogStore.visible = val },
    handleLoginSuccess: authDialogStore.handleLoginSuccess
  }
}
