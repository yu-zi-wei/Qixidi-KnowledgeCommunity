# 路由认证交互规范

受保护路由的登录交互方式和用户体验设计。

---

## 核心原则

**通用原则**（适用于大多数页面）：
- **客户端路由切换**：显示登录框，不跳转，`abortNavigation()`
- **直接访问/刷新**：重定向到首页

**特殊页面**（需要更友好的用户体验）：
- 允许页面正常渲染（SSR）
- 导航栏、侧边栏立即显示
- 内容区域显示登录提示
- 弹出登录框引导用户

---

## 适用场景

### 通用原则适用场景

**后台管理**、**个人中心**等完全需要登录的页面：
- `/admin/**` - 后台管理
- `/user/**` - 个人中心
- 其他所有需要登录的功能页面

**实现方式**：中间件直接重定向到首页

### 特殊页面适用场景

**内容展示页面**，但内容需要登录：
- `/follow` - 关注页（导航栏、侧边栏有价值，内容区域提示登录）

**实现方式**：
1. 中间件配置特殊页面白名单
2. 页面内检查登录状态，渲染不同内容
3. SSR 模式确保导航栏和侧边栏立即显示

---

## 实现规范

### 中间件实现

**文件**：`middleware/auth.ts`

```typescript
export default defineNuxtRouteMiddleware((to, from) => {
  const authStore = useAuthStore()
  const authDialogStore = useAuthDialogStore()

  // 已登录，放行
  if (authStore.isLoggedIn) {
    return
  }

  // 未登录
  // 场景1：客户端路由切换
  if (import.meta.client && from) {
    authDialogStore.showLoginDialog(to.fullPath)
    return abortNavigation()
  }

  // 场景2：直接访问或刷新
  authDialogStore.showLoginDialog(to.fullPath)

  // 特殊页面白名单（允许渲染但显示登录提示）
  const specialPages = ['/follow']
  if (specialPages.includes(to.path)) {
    return // 不重定向
  }

  // 其他页面重定向到首页
  return navigateTo('/')
})
```

### 页面实现（特殊页面）

```vue
<template>
  <div>
    <!-- 未登录：显示登录提示 -->
    <div v-if="!authStore.isLoggedIn" class="login-prompt">
      <h3>需要登录</h3>
      <p>登录后查看更多内容</p>
      <button @click="authDialogStore.showLoginDialog('/follow')">
        立即登录
      </button>
    </div>

    <!-- 已登录：显示正常内容 -->
    <template v-else>
      <!-- 正常页面内容 -->
    </template>
  </div>
</template>

<script setup>
definePageMeta({
  middleware: 'auth'
})

const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
</script>
```

---

## Nuxt 配置

### 渲染策略配置

**文件**：`nuxt.config.ts`

```typescript
routeRules: {
  // 特殊页面需要 SSR（确保导航栏、侧边栏立即显示）
  '/follow': {ssr: true},

  // 通用页面可以 CSR 或 SSR，根据重定向行为决定
  '/admin/**': {ssr: false},
}
```

---

## 用户体验对比

### 通用原则（重定向到首页）

| 操作 | 行为 | 用户体验 |
|------|------|----------|
| 刷新受保护页面 | 白屏 → 显示首页 | 简单但可能突兀 |
| 点击导航栏菜单 | 显示登录框 | 友好 |

### 特殊页面（显示登录提示）

| 操作 | 行为 | 用户体验 |
|------|------|----------|
| 刷新受保护页面 | 导航栏立即显示 + 内容提示登录 | 更友好，用户知道在哪里 |
| 点击导航栏菜单 | 显示登录框 | 友好 |

---

## 决策树

```
需要登录的页面
    ↓
是否是内容展示页面？
    ↓
是 → 特殊页面（SSR + 登录提示）
    ↓
否 → 通用原则（重定向到首页）
```

---

## 检查清单

添加新的受保护路由时，确认：

- [ ] 是否需要添加到 `middleware/auth.ts` 的 `definePageMeta`
- [ ] 是否符合"通用原则"还是"特殊页面"？
- [ ] 如果是特殊页面，是否添加到 `specialPages` 白名单？
- [ ] 如果是特殊页面，页面内是否实现了登录状态检查？
- [ ] 渲染策略（SSR/CSR）是否正确配置？
- [ ] 是否测试了刷新、直接访问、路由切换三种场景？

---

**核心原则**：大多数页面重定向到首页，个别内容页面友好提示登录。
