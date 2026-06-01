# API 与状态管理

---

## API 层

### 架构

```
组件 → useXxxApi() → useApi() → $fetch → 后端 API
```

### useApi.ts 核心

#### SSR / CSR 双模式

```typescript
const baseURL = import.meta.server
  ? config.apiBase          // SSR: http://127.0.0.1:9001（服务端直连后端）
  : config.public.apiBase   // CSR: /api（通过 Nitro devProxy 或反向代理）
```

#### 请求头

- 自动注入 `Authorization: Bearer {token}`
- 默认 `Content-Type: application/json`（可覆盖）

#### 错误处理

| 场景 | 处理 |
|------|------|
| 后端返回 `code !== 200` | 抛出 Nuxt error（statusCode + statusMessage） |
| 客户端 | 自动弹出 `discreteMessage.error(statusMessage)` |
| 401 状态码 | 自动 `logout()` + 重定向到首页 |

**重要**：`useApi` 已统一拦截错误并弹窗，页面中**禁止重复**写 `message.error`。

#### 请求方法

| 方法 | 返回值 | 用途 |
|------|--------|------|
| `get<T>(url, params)` | `T` | GET 请求，自动解包 `data` |
| `post<T>(url, data)` | `T` | POST 请求，自动解包 `data` |
| `put<T>(url, data)` | `T` | PUT 请求 |
| `delete<T>(url, params)` | `T` | DELETE 请求 |
| `getPage<T>(url, params)` | `{ total, rows }` | GET 分页请求 |
| `postPage<T>(url, data)` | `{ total, rows }` | POST 分页请求 |
| `request<T>(url, options)` | 原始响应 | 底层方法 |

#### 后端响应格式

```
非分页：{ code: 200, msg: "success", data: T }
分页：  { total: number, rows: T[] }
```

#### API 命名规范

每个业务域一个文件 `composables/useXxxApi.ts`：

```typescript
export const useArticleApi = () => {
  const api = useApi()
  return {
    getDetail: (id: number) => api.get<ArticleInfo>(`/white/article/details/${id}`),
    // ...
  }
}
```

---

## Pinia 状态管理

### Store 列表

| Store | 文件 | 持久化 | 职责 |
|-------|------|--------|------|
| `auth` | `auth.ts` | localStorage | Token、用户信息、登录状态 |
| `authDialog` | `authDialog.ts` | 无 | 登录框显隐、登录后重定向路径 |
| `essayDrawer` | `essayDrawer.ts` | 无 | 随笔编辑抽屉的显隐和编辑 ID |
| `articles` | `articles.ts` | 无（内存） | 文章列表分页缓存（按 key 隔离） |
| `label` | `label.ts` | 无 | 标签列表缓存（单例） |
| `creatorApply` | `creatorApply.ts` | 无 | 创作者申请弹窗 |

### auth Store

```typescript
// 状态
token: Ref<string | null>
user: Ref<UserInfo | null>

// 计算属性
isLoggedIn: boolean  // !!token && !!user
isCreator: boolean   // roleId === 2 || roleId === 3

// 方法
setToken(val)
setUser(val)
logout()          // 清空 token 和 user
fetchUser()       // 从 API 获取用户信息，失败则 logout
```

### authDialog Store

```typescript
visible: Ref<boolean>
redirectAfterLogin: Ref<string>

showLoginDialog(redirect?: string)  // 打开登录框，记录重定向路径
hideLoginDialog()
handleLoginSuccess()               // 关闭弹窗 + navigateTo(redirect)
```

全局组件 `AuthDialog.client.vue` 在 `app.vue` 中注册，任何地方调用 `useAuthDialogStore().showLoginDialog()` 即可弹出。

### articles Store

按 key 隔离的分页缓存，支持无限滚动场景：

```typescript
// 状态结构
pages: Record<string, {
  articles: ArticleInfo[]
  pageNum: number
  total: number
  scrollPosition: number
  hasMore: boolean
}>

// 核心方法
initPage(key, articles, total)      // 初始化（仅在空数据时填充，不覆盖 SSR 数据）
replacePage(key, articles, total)   // 强制替换（用于刷新）
appendArticles(key, new, total)     // 追加（用于加载更多）
saveScrollPosition(key, position)   // 保存滚动位置
resetPage(key)                      // 清除指定 key
clearAll()                          // 清除全部（登出时使用）
```

### 全局组件状态管理规范

> 全局 UI 组件（弹窗、抽屉）**必须**用 Pinia Store，禁止 `useState` 或多套独立 `ref`。

---

## 无限滚动（useInfiniteScroll）

### 接口

```typescript
const {
  articles,     // ComputedRef<ArticleInfo[]>
  pageNum,      // ComputedRef<number>
  total,        // ComputedRef<number>
  loadingMore,  // Ref<boolean>
  noMore,       // ComputedRef<boolean>
  loadMore,     // () => Promise<void>
  initArticles, // () => Promise<void>
} = useInfiniteScroll({
  fetchFn,    // (page: number) => Promise<{ rows, total }>
  storeKey,   // string | Ref<string> | ComputedRef<string>
})
```

### 滚动位置保存与恢复

**保存**：
- `onMounted` 时添加 scroll 事件监听（100ms 防抖）
- 写入 `localStorage` 键 `scroll-${storeKey}`
- `onBeforeRouteLeave` 时强制保存一次

**恢复**：
- `initArticles()` 检查模块级 `Set<string> preserveCacheKeys`
- 如果当前 key 在集合中（从详情页返回），跳过数据获取，恢复滚动位置
- 使用 `behavior: 'instant'` 避免滚动动画

**缓存保留策略**：

| 导航类型 | 缓存行为 | 原因 |
|---------|---------|------|
| Tab 切换（首页→最新→关注） | 不保留 | 同级页面切换，需要新数据 |
| 详情页跳转（列表→文章） | 保留 | 返回时恢复位置 |

### 生命周期

- `onMounted`：添加 scroll 监听 + 调用 `initArticles()`
- `onBeforeUnmount`：移除 scroll 监听，清除防抖定时器
- `onBeforeRouteLeave`：保存滚动位置，判断是否保留缓存

---

## WebSocket 实时通知

### 架构

模块级单例（不是 composable 内部变量），确保整个应用只有一个 WebSocket 连接。

### 连接生命周期

```
登录 → watch(isLoggedIn) → 建立 WebSocket 连接
                           → ws://host/websocket/{userId}
登出 → 断开连接
```

**URL 构建**：
```typescript
// 读取 NUXT_PUBLIC_WS_BASE，替换 http→ws / https→wss
wsBase.replace('https://', 'wss://').replace('http://', 'ws://')
// 最终：ws://host/websocket/{userId}
```

**页面可见性**：`visibilitychange` 事件触发重连（标签页切回前台时）。

### 消息类型

| type | 数据 | 用途 |
|------|------|------|
| 1 | `NewsUserSumVo[]` | 通知摘要（评论、点赞、关注等未读数） |
| 2 | `{ rows: PrivateUserVo[] }` | 私信联系人列表 + 未读数 |

### 接口

```typescript
const {
  connected,       // Ref<boolean>
  unreadMap,       // Ref<Record<number, number>>  未读数按类型
  totalUnread,     // ComputedRef<number>
  privateUserList, // Ref<PrivateUserVo[]>          私信联系人
  getUnread,       // (type: number) => number
  onPrivateMessage,  // (cb) => void  注册私信回调
  offPrivateMessage, // () => void    注销回调
} = useWebSocket()
```

### 断线重连

指数退避策略：1s → 2s → 4s → 8s → 16s，最多 5 次。

重置条件：成功连接（`onopen`）、用户登录、标签页变为可见。

### SSR 兼容

服务端直接返回空值 stub，不创建 WebSocket。

---

## useAuthDialog 封装

`composables/useAuthDialog.ts` 是对 `authDialog` Store 的薄封装：

```typescript
const {
  visible,            // ComputedRef<boolean>（只读）
  redirectAfterLogin, // ComputedRef<string>（只读）
  showLoginDialog,    // (redirect?: string) => void
  hideLoginDialog,    // () => void
  handleLoginSuccess, // () => void  关闭弹窗 + navigateTo
} = useAuthDialog()
```

与直接使用 Store 的区别：`visible` 和 `redirectAfterLogin` 被包装为 `computed()`，从消费者角度变为只读。
