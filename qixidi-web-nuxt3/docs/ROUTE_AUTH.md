# 路由权限配置文档

本项目提供统一的路由权限配置系统，用于保护需要登录才能访问的页面。

---

## 快速使用

### 需要登录的路由

在页面组件中添加 `middleware: 'auth'` 配置：

```typescript
definePageMeta({
  middleware: 'auth' // 需要登录才能访问
})
```

**示例**：

```typescript
// pages/follow.vue - 关注页面
definePageMeta({
  layout: 'home',
  middleware: 'auth' // 需要登录
})
```

---

## 工作原理

### 1. 认证中间件

**文件**：`middleware/auth.ts`

当用户访问配置了 `middleware: 'auth'` 的页面时：
- ✅ 已登录：正常访问
- ❌ 未登录：弹出登录框，不跳转页面

### 2. 全局登录框管理

**文件**：`composables/useAuthDialog.ts`

提供全局方法：
- `showLoginDialog(redirect)` - 显示登录框
- `hideLoginDialog()` - 隐藏登录框
- `handleLoginSuccess()` - 登录成功后跳转

### 3. 登录框组件

**文件**：`components/AuthDialog.client.vue`

已在 `app.vue` 中全局注册，无需手动引入。

---

## 使用场景

### 场景 1：点击菜单弹出登录框

```vue
<template>
  <NuxtLink to="/follow" @click.prevent="handleClick">
    关注
  </NuxtLink>
</template>

<script setup lang="ts">
const authStore = useAuthStore()
const { showLoginDialog } = useAuthDialog()

const handleClick = () => {
  if (!authStore.isLoggedIn) {
    showLoginDialog('/follow') // 未登录显示登录框
    return
  }
  navigateTo('/follow')
}
</script>
```

### 场景 2：保护整个页面

```typescript
// pages/user/[uuid].vue - 用户主页
definePageMeta({
  middleware: 'auth' // 需要登录才能访问
})
```

### 场景 3：保护特定操作

```typescript
const handleLike = async (articleId: string) => {
  const authStore = useAuthStore()
  const { showLoginDialog } = useAuthDialog()

  if (!authStore.isLoggedIn) {
    showLoginDialog() // 弹出登录框
    return
  }

  // 执行点赞操作
  await articleApi.like(articleId)
}
```

---

## 已配置的路由

| 路由 | 页面 | 需要登录 |
|------|------|----------|
| `/follow` | 关注 | ✅ |
| `/user/[uuid]` | 用户主页 | ✅ |

---

## 扩展新路由

如需添加新的需要登录的路由，只需在页面组件中添加：

```typescript
definePageMeta({
  middleware: 'auth'
})
```

---

## 技术实现

### 文件结构

```
composables/
└── useAuthDialog.ts        # 全局登录框管理

middleware/
└── auth.ts                 # 认证中间件

components/
└── AuthDialog.client.vue   # 登录框组件（全局注册）

stores/
└── auth.ts                 # 用户状态管理

app.vue                     # 已添加 AuthDialog 组件
```

### 核心流程

```
用户访问受保护页面
    ↓
中间件检查登录状态
    ↓
未登录 → 显示登录框 → 用户登录 → 跳转到目标页面
已登录 → 正常访问
```

---

## 注意事项

1. **客户端组件**：AuthDialog 使用 `.client.vue` 后缀，只在客户端渲染
2. **SSR 兼容**：中间件在服务端和客户端都会执行
3. **状态持久化**：登录状态通过 Pinia persist 持久化到 localStorage
4. **跳转保持**：登录成功后会自动跳转到原目标页面
