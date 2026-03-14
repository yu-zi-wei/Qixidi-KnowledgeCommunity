/**
 * 认证中间件
 * 按照成熟网站的交互方式处理需要登录的路由
 *
 * 通用原则：
 * - 客户端路由切换：显示登录框，不跳转
 * - 直接访问/刷新：重定向到首页
 *
 * 特殊页面（如 /follow）：
 * - 允许页面正常渲染（导航栏、侧边栏立即显示）
 * - 内容区域显示登录提示
 * - 弹出登录框引导用户
 *
 * 使用方法：在页面组件中添加
 * definePageMeta({ middleware: 'auth' })
 */
export default defineNuxtRouteMiddleware((to, from) => {
  const authStore = useAuthStore()
  const authDialogStore = useAuthDialogStore()

  // 已登录，放行
  if (authStore.isLoggedIn) {
    return
  }

  // 未登录
  // 场景1：客户端路由切换（有 from 信息）
  // 行为：显示登录框，不跳转，阻止路由切换
  if (import.meta.client && from) {
    authDialogStore.showLoginDialog(to.fullPath)
    return abortNavigation()
  }

  // 场景2：直接访问或刷新（无 from 信息）
  // 默认行为：重定向到首页
  // 特殊页面（如 /follow）：页面内处理登录状态，显示登录提示
  authDialogStore.showLoginDialog(to.fullPath)

  // 检查是否是特殊页面（允许渲染但显示登录提示）
  // 目前只有 /follow 页面不走重定向逻辑
  const specialPages = ['/follow']
  if (specialPages.includes(to.path)) {
    return // 不重定向，让页面正常渲染
  }

  // 其他页面重定向到首页
  return navigateTo('/')
})
