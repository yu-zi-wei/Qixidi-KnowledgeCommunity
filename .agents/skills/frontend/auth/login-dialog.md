---
description: 【登录对话框】用户的登录、注册、重置密码弹窗。关键词：登录、注册、弹窗、Pinia Store、全局状态
---

# 登录对话框 (Login Dialog)

## 1 模块职责 (Module Responsibility)

### 负责范围
- 用户登录、注册、重置密码的 UI 展示和表单交互
- 登录框的显示/隐藏状态管理
- 登录成功后的页面跳转

### 不负责
- 用户认证状态的持久化（由 `stores/auth.ts` 负责）
- 登录 API 调用（由 `composables/useAuthApi.ts` 负责）
- 路由权限控制（由 `middleware/auth.ts` 负责）

---

## 2 代码结构 (Code Structure)

### 关键文件（前端）

| 类型 | 路径 | 职责 |
|------|------|------|
| Store | `stores/authDialog.ts` | 登录框全局状态管理 |
| 组件 | `components/AuthDialog.client.vue` | 登录框组件（客户端渲染） |
| 中间件 | `middleware/auth.ts` | 路由权限控制，触发登录框 |
| API | `composables/useAuthApi.ts` | 登录/注册 API 调用 |
| 认证Store | `stores/auth.ts` | 用户认证状态（token、用户信息） |

### 全局挂载
登录框组件在 `app.vue` 中全局挂载，无 props，内部通过 Store 管理状态。

---

## 3 核心数据结构 (Core Data Structures)

### AuthDialog Store（`stores/authDialog.ts`）

**说明**：管理登录框的全局状态，确保所有入口共享同一状态。

**核心状态**：
- `visible: Ref<boolean>` - 登录框显示状态
- `redirectAfterLogin: Ref<string>` - 登录成功后跳转的路径

**核心方法**：
- `showLoginDialog(redirect?: string)` - 显示登录框
- `hideLoginDialog()` - 隐藏登录框

---

## 4 业务规则 (Business Rules)

### 显示规则
- **导航栏登录按钮**：点击直接显示登录框
- **路由中间件（通用原则）**：
  - 客户端路由切换：显示登录框，不跳转，`abortNavigation()`
  - 直接访问/刷新：重定向到首页
- **路由中间件（特殊页面）**：
  - 允许页面正常渲染（导航栏、侧边栏立即显示）
  - 内容区域显示登录提示
  - 弹出登录框引导用户
  - 目前特殊页面：`/follow`
- **业务操作**：需要登录的操作（点赞、评论等）显示登录框

### 关闭规则
- 用户点击右上角 X 按钮
- 用户点击遮罩层
- 登录成功后自动关闭
- 注册成功后切换到登录视图

### 跳转规则
- 登录成功后，如果 `redirectAfterLogin` 有值，跳转到指定路径
- 跳转后清空 `redirectAfterLogin`

---

## 5 不变量 (Invariants / Never Break Rules)

### 状态管理
- **单一数据源**：所有入口必须使用 `authDialogStore`，禁止维护独立状态
- **禁止 useState**：全局 UI 组件禁止使用 `useState`，必须使用 Pinia Store
- **SSR 兼容**：登录框使用 `.client.vue` 后缀，避免 SSR 问题

### 显示逻辑
- **登录成功必关闭**：登录成功后必须关闭弹窗
- **跳转后清空**：跳转完成后必须清空 `redirectAfterLogin`

---

## 6 常见查询模式 (Query Patterns)

| 场景 | 实现方式 |
|------|----------|
| 导航栏登录 | `authDialogStore.showLoginDialog()` |
| 路由拦截 | `middleware/auth.ts` 中调用 `showLoginDialog(redirect)` |
| 业务操作拦截 | 检查登录状态，未登录调用 `showLoginDialog()` |
| 登录成功跳转 | 读取 `redirectAfterLogin`，跳转后清空 |

---

## 7 关联关系 (Cross Module Relations)

| 关联模块 | 关联内容 | 说明 |
|---------|---------|------|
| `stores/auth.ts` | 认证状态 | 登录成功后设置 token 和用户信息 |
| `composables/useAuthApi.ts` | API 调用 | 调用登录/注册/重置密码接口 |
| `middleware/auth.ts` | 路由守卫 | 未登录时显示登录框 |
| `components/AppHeader.vue` | 导航栏 | 点击登录按钮触发 |

---

## 8 AI 开发 Workflow (Development Workflow)

### 新增登录触发入口
1. 在目标组件中导入 `useAuthDialogStore`
2. 调用 `authDialogStore.showLoginDialog(redirect?)` 显示登录框
3. 如需登录后跳转，传入目标路径

### 修改登录框 UI
1. 定位 `components/AuthDialog.client.vue`
2. 修改表单布局、样式或文案
3. 确保三种视图（登录/注册/重置）都能正常切换

### 新增表单字段
1. 在对应的表单数据 ref 中添加字段
2. 在表单校验规则中添加校验逻辑
3. 在 API 调用时传递新字段

### 登录成功后处理
1. 调用 `authApi.login()` 获取 token
2. 调用 `authStore.setToken()` 设置认证状态
3. 读取 `redirectAfterLogin`（如有）
4. 直接设置 `visible.value = false` 关闭弹窗
5. 执行跳转并清空 `redirectAfterLogin`

---

## 核心注意事项

### 状态管理必须使用 Pinia Store
```typescript
// ✅ 正确
const authDialogStore = useAuthDialogStore()
authDialogStore.showLoginDialog()

// ❌ 错误：禁止使用本地状态
const showDialog = ref(false)
```

### 组件必须使用 storeToRefs
```typescript
// ✅ 正确
const { visible } = storeToRefs(authDialogStore)

// ❌ 错误：会丢失响应性
const { visible } = authDialogStore
```

### 登录成功必须直接设置本地状态
```typescript
// ✅ 正确
visible.value = false

// ⚠️ 可以但不推荐
authDialogStore.hideLoginDialog()
```

---

## 相关规则文件
- `rules/frontend/global-component-management.md` - 全局组件状态管理规范
- `rules/frontend/qixidi-web-nuxt3.md` - Nuxt 3 开发规范
