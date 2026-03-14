# 全局组件状态管理规范

适用于弹窗、抽屉、通知等全局 UI 组件的状态管理。

---

## 核心原则

**单一数据源**：全局 UI 组件必须有唯一的全局状态，所有入口共享该状态。

---

## 状态管理选择

| 组件类型 | 状态管理方式 | 原因 |
|---------|------------|------|
| **局部组件** | 组件内 `ref` | 仅组件内部使用 |
| **跨组件共享** | **Pinia Store** | SSR 兼容、响应式、状态同步 |
| **全局单例** | **Pinia Store** | 确保唯一性、避免重复实例 |

---

## ❌ 禁止的模式

### 1. 禁止使用 useState 管理全局 UI 状态

```typescript
// ❌ 错误：useState 在客户端组件中可能导致状态不同步
const visible = useState<boolean>('dialog-visible', () => false)
```

**原因**：
- `useState` 的 key 冲突可能导致状态不同步
- SSR/客户端状态可能不一致
- 难以追踪状态变化

### 2. 禁止多套独立状态

```vue
<!-- ❌ 错误：每个组件维护自己的状态 -->
<script setup>
const showDialog = ref(false)
</script>

<template>
  <MyDialog v-model:show="showDialog" />
</template>
```

**问题**：
- 组件 A 打开弹窗，组件 B 无法感知
- 状态不一致，行为不可预测

### 3. 禁止过度复杂的双向同步

```typescript
// ❌ 错误：双向 watch 容易出错
watch(() => store.visible, (val) => { local.value = val })
watch(local, (val) => { store.visible = val })
```

**问题**：
- 循环更新风险
- 调试困难
- 性能问题

---

## ✅ 正确的模式

### 1. Pinia Store 管理全局状态

```typescript
// stores/authDialog.ts
export const useAuthDialogStore = defineStore('authDialog', () => {
  const visible = ref(false)

  const show = () => { visible.value = true }
  const hide = () => { visible.value = false }

  return { visible, show, hide }
})
```

### 2. 组件直接绑定 Store 状态

```vue
<!-- components/AuthDialog.client.vue -->
<script setup>
import { storeToRefs } from 'pinia'

const authDialogStore = useAuthDialogStore()
const { visible } = storeToRefs(authDialogStore)
</script>

<template>
  <n-modal v-model:show="visible">
    <!-- 内容 -->
  </n-modal>
</template>
```

### 3. 所有入口使用同一 Store

```vue
<!-- AppHeader.vue -->
<script setup>
const authDialogStore = useAuthDialogStore()
const handleLogin = () => authDialogStore.show()
</script>

<!-- middleware/auth.ts -->
export default defineNuxtRouteMiddleware(() => {
  const authDialogStore = useAuthDialogStore()
  authDialogStore.show()
  return abortNavigation()
})
```

---

## 实现检查清单

开发全局 UI 组件时，必须确认：

- [ ] 是否有多个触发入口？（导航栏、路由、按钮等）
- [ ] 是否使用 Pinia Store 管理状态？
- [ ] 所有入口是否使用同一个 Store？
- [ ] 是否避免了 useState 管理全局状态？
- [ ] 是否避免了双向 watch 等复杂同步？
- [ ] 组件是否直接绑定 Store 状态（使用 storeToRefs）？

---

## 常见场景

### 场景 1：登录弹窗

**入口**：导航栏按钮、路由中间件、文章详情页

**实现**：`stores/authDialog.ts` + 全局 AuthDialog 组件

### 场景 2：全局通知

**入口**：API 错误、操作成功、后台推送

**实现**：`stores/notification.ts` + 全局 Notification 组件

### 场景 3：确认对话框

**入口**：删除操作、退出登录、提交表单

**实现**：`stores/confirmDialog.ts` + 全局 ConfirmDialog 组件

---

## 与 Nuxt 3 SSR 的兼容性

**为什么选择 Pinia Store 而不是 useState**：

1. **SSR 安全**：Pinia 自动处理 SSR/客户端状态同步
2. **类型安全**：完整的 TypeScript 支持
3. **DevTools**：状态变化可追踪
4. **单一数据源**：确保状态唯一性

---

**核心原则**：全局 UI 组件 = Pinia Store + 单一实例 + 所有入口共享状态。

禁止多套独立状态，禁止复杂同步逻辑。
