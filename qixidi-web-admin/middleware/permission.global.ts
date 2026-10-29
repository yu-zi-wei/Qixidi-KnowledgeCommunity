/**
 * 全局登录守卫：管理后台除 /login 外一律需要管理员会话
 * （权限模型走"登录即管理员"的简化方案，详见 docs/开发需求与设计.md）
 */
export default defineNuxtRouteMiddleware((to) => {
  const adminStore = useAdminStore();
  const isLoggedIn = !!adminStore.token;

  if (to.path === '/login') {
    if (isLoggedIn) return navigateTo('/');
    return;
  }
  if (!isLoggedIn) return navigateTo('/login');
});
