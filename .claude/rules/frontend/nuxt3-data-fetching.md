# Nuxt 3 数据获取规范

栖息地博客 Nuxt 3 数据获取的最佳实践和常见陷阱。

---

## 核心原则

**简单优于复杂**：优先使用框架内置能力，避免自定义状态管理。

---

## useAsyncData 正确用法

### ✅ 正确：使用 computed key

```typescript
// key 随路由参数变化，自动重新获取
const cacheKey = computed(() => `label-${route.params.id}-${route.query.sort || 'latest'}`)
const { data, pending } = await useAsyncData(
  cacheKey,
  () => fetchArticles(1)
)
```

### ❌ 错误：固定 key + 手动 watch

```typescript
// 会导致重复调用或数据不刷新
const { data } = await useAsyncData('fixed-key', () => fetchArticles(1))
watch([() => route.params.id, () => route.query.sort], () => {
  // 手动刷新 - 容易出错
})
```

### ❌ 错误：使用 watch 选项

```typescript
// useAsyncData 的 watch 选项与手动 watch 冲突
const { data } = await useAsyncData(
  'key',
  () => fetchArticles(1),
  { watch: [() => route.query.sort] } // 不要这样用
)
```

---

## 状态管理规范

### ✅ 最少状态原则

**只需要的状态**：
```typescript
const articleList = ref<T[]>([])      // 数据列表
const pageNum = ref(1)                // 当前页码（从 1 开始）
const loadingMore = ref(false)        // 加载更多状态
```

**框架提供的状态**：
```typescript
const { data, pending } = await useAsyncData(...) // pending 用于初始加载
```

### ❌ 禁止的冗余状态

```typescript
// ❌ 不要这些
const fetchingGroupingId = ref<number | null>(null)
const lastFetchKey = ref('')
const isReady = ref(false)
const pageNum = ref(0) // 从 0 开始会导致混乱
```

**原因**：
- 增加复杂度
- 状态同步困难
- 容易出现不一致

---

## 无限滚动 + 初始加载的正确实现

### 问题场景

```
用户切换分类
    ↓
useAsyncData 开始获取第 1 页（pending = true）
    ↓
组件渲染，ArticleList 挂载
    ↓
IntersectionObserver 立即触发 @load-more
    ↓
pageNum++（从 1 变成 2）
    ↓
第 1 页数据到达，被覆盖或混乱
```

### ✅ 解决方案：使用 pending 阻止

```typescript
const { data, pending } = await useAsyncData(...)
const isInitialLoading = ref(false)

// 监听 pending 状态
watch(() => pending.value, (val) => {
  isInitialLoading.value = val
}, { immediate: true })

// loadMore 中检查
const loadMore = async () => {
  if (loadingMore.value || noMore.value || isInitialLoading.value) {
    return // 初始加载期间不响应
  }
  // ...
}
```

### ❌ 错误方案： pageNum 从 0 开始

```typescript
const pageNum = ref(0) // ❌ 不要这样

// 数据到达后设置为 1
if (newData?.rows?.length > 0) {
  pageNum.value = 1 // ❌ 时序问题
}
```

**问题**：
- `loadMore` 检查 `pageNum === 0` 会失败
- `watch` 可能在 `loadMore` 之后执行
- 状态不一致

---

## 分页状态管理

### ✅ 正确：每次数据到达重置为 1

```typescript
watch(() => data.value, (newData) => {
  if (newData) {
    articleList.value = newData.rows || []
    total.value = newData.total || 0
    pageNum.value = 1 // 重置为第 1 页
  }
}, { immediate: true })

const loadMore = async () => {
  pageNum.value++ // 从 1 增加到 2
  // ...
}
```

### ✅ 正确：空数据也设置为 1

```typescript
watch(() => data.value, (newData) => {
  articleList.value = newData?.rows || []
  total.value = newData?.total || 0
  pageNum.value = 1 // 无论是否有数据，都重置为 1
}, { immediate: true })
```

---

## 调试流程规范

### 开发前必须做的事

1. **画时序图**：理解每一步的执行顺序
2. **列出所有状态**：明确每个状态的作用和生命周期
3. **列出边界情况**：空数据、快速切换、网络错误

### 开发中必须做的事

1. **一次只改一个问题**：不要同时修改多个地方
2. **验证框架行为**：用 console.log 确认执行顺序
3. **测试所有场景**：不只是"能跑"

### 常见问题排查

| 症状 | 可能原因 | 排查方法 |
|------|----------|----------|
| 重复调用接口 | useAsyncData key 不变或重复 watch | 检查 key 是否 computed，是否有多处 watch |
| 数据不刷新 | watch 监听引用而不是值 | 检查 `watch(data.value)` 而不是 `watch(data)` |
| 自动请求下一页 | IntersectionObserver 过早触发 | 检查 `isInitialLoading` 是否阻止 |
| 状态混乱 | 多个 ref 管理同一状态 | 简化状态，利用框架的 `pending` |

---

## 代码质量检查清单

- [ ] `useAsyncData` 的 key 是否随路由参数变化？
- [ ] 是否使用了 computed key？
- [ ] 是否避免了冗余状态（fetchingXxx、lastXxx）？
- [ ] `pageNum` 是否从 1 开始？
- [ ] 是否用 `pending` 阻止初始加载期间的 `loadMore`？
- [ ] watch 是否监听 `.value` 而不是 ref 本身？
- [ ] 数据到达时是否重置 `pageNum` 为 1？
- [ ] 是否测试了空数据场景？
- [ ] 是否测试了快速切换场景？

---

## 列表页状态持久化规范

**场景**：分页列表页点击某条数据跳转详情页，返回时需要恢复分页和滚动位置。

### 根据列表加载方式选择持久化策略

| 列表加载方式 | 页码持久化 | 滚动位置持久化 |
|-------------|-----------|--------------|
| **分页组件**（n-pagination） | URL query 参数 `?page=N` | `history.replaceState` |
| **自动触底加载**（infinite scroll） | `history.replaceState` | `history.replaceState` |

### 为什么分页用 URL 参数

- 任何刷新都存活（包括 Ctrl+F5 强刷）
- 可分享、可收藏指定页
- 浏览器前进后退天然支持
- SSR 能读取页码

### 为什么无限滚动用 history.state

- 没有明确的"页码"概念，是累计加载
- URL 频繁变化体验差
- 只需在跳转前保存，返回时恢复

### 分页组件模式（URL query）

```typescript
// 页码从 URL 读取
const currentPage = computed(() => Number(route.query.page) || 1)

// computed key 自动响应页码变化
const cacheKey = computed(() => `articles-${id}-${currentPage.value}`)
const { data } = await useAsyncData(cacheKey, () => fetchAPI(currentPage.value))

// 切页时更新 URL
const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

// 跳转详情前保存滚动位置
const handleSaveState = (scrollTop: number) => {
  history.replaceState({ ...(window.history.state || {}), scrollKey: scrollTop }, '')
}

// 返回时恢复滚动
onMounted(() => {
  if (window.history.state?.scrollKey) {
    nextTick(() => listRef.value?.restoreScroll(window.history.state.scrollKey))
  }
})
```

### 无限滚动模式（history.state）

```typescript
// 跳转前保存页码 + 滚动位置
const handleSaveState = () => {
  history.replaceState({
    ...(window.history.state || {}),
    listPage: pageNum.value,
    listScroll: scrollEl.scrollTop
  }, '')
}

// 返回时恢复
if (import.meta.client) {
  const saved = window.history.state?.listPage
  if (saved) pageNum.value = saved
}

onMounted(() => {
  if (window.history.state?.listScroll) {
    nextTick(() => { scrollEl.scrollTop = window.history.state.listScroll })
  }
})
```

---

## 核心教训

1. **框架能力优先**：优先使用 `useAsyncData` 的内置特性
2. **状态越少越好**：每个状态都有明确作用，不冗余
3. **理解时序**：画图理解执行顺序，不要猜测
4. **完整测试**：不只是"能跑"，要测试所有场景

---

**核心原则**：简单、清晰、可维护。不要用复杂的补丁修复简单的问题。
