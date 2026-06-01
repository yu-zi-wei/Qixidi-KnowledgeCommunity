# 布局与路由

---

## 布局系统

6 种布局组件，通过 `definePageMeta({ layout: 'xxx' })` 选择。

| 布局 | 文件 | 使用场景 | 特点 |
|------|------|---------|------|
| `default` | `default.vue` | 文章、分类等通用页面 | 导航栏 + 可选 TabBar + 动态侧边栏 |
| `home` | `home.vue` | 首页 | 导航栏 + TabBar + HomeSidebar |
| `admin` | `admin.vue` | 创作者后台 | 左侧菜单 + 用户卡片 |
| `user-home` | `user-home.vue` | 个人主页 | 用户头部卡片 + Tab 菜单 + 统计面板 |
| `editor` | `editor.vue` | 文章编辑 | 最小化布局，只有 `<slot />` |
| `blank` | `blank.vue` | 登录/注册、独立页面 | 纯净布局，居中内容 |

### default 布局（主要布局）

**结构**：

```
┌──────────────────────────────────────────────────┐
│ HomeNavBar（粘性导航栏）                            │
├──────────────────────────────────────────────────┤
│ ContentTabBar（可选，通过 showTabBar 控制）         │
├──────────────────────────────┬───────────────────┤
│                              │                   │
│       <slot />               │   Sidebar         │
│       页面内容                │   （动态组件）      │
│                              │                   │
└──────────────────────────────┴───────────────────┘
```

### 侧边栏切换机制

**页面设置元数据**：

```typescript
// pages/articles/[id].vue
definePageMeta({
  showTabBar: false,
  sidebar: 'article'
})
```

**布局读取并动态渲染**：

```typescript
// layouts/default.vue
const pageMeta = computed(() => ({
  showTabBar: route.meta.showTabBar ?? false,
  sidebar: route.meta.sidebar ?? 'home'
}))

const sidebarComponents = {
  home: defineAsyncComponent(() => import('~/components/HomeSidebar.vue')),
  article: defineAsyncComponent(() => import('~/components/ArticleSidebar.vue')),
  readingEssays: defineAsyncComponent(() => import('~/components/readingEssays/ReadingEssaysSidebar.vue')),
}
```

**侧边栏映射**：

| 页面 | `sidebar` 值 | 侧边栏组件 |
|------|-------------|-----------|
| 首页、最新、精选、关注 | `'home'`（默认） | HomeSidebar |
| 文章详情、关于页 | `'article'` | ArticleSidebar |
| 阅读随笔 | `'readingEssays'` | ReadingEssaysSidebar |
| 时光小记 | `false` | 无 |

**移动端**：`<768px` 时侧边栏通过 `v-show="!isMobile"` 隐藏（DOM 保留但不可见）。

### ArticleSidebar 数据传递

使用 `useState('article-sidebar-data')` 在页面和侧边栏之间共享数据：

```typescript
// 页面中写入
const sidebarData = useState('article-sidebar-data', () => ({
  author: null,
  articleContent: '',  // 渲染后的 HTML
  showToc: true
}))

sidebarData.value = {
  author: { userId, nickname, avatar, ... },
  articleContent: renderedHtml,
  showToc: true
}

// ArticleSidebar 中读取
const sidebarData = useState('article-sidebar-data')
```

侧边栏渲染内容：
1. **作者卡片**：头像、昵称、角色、统计（文章/粉丝/获赞）、关注按钮
2. **平台介绍卡片**（关于页使用）：站点 logo、简介、仓库链接
3. **文章目录**（`ArticleToc` 组件）

---

## 导航栏（HomeNavBar.vue）

### 三栏网格布局

```
┌──────────────────────────────────────────────────┐
│  Logo  │   搜索 + 导航菜单    │  主题 创作 通知 头像 │
│ (left) │      (center)        │     (right)        │
└──────────────────────────────────────────────────┘
```

### 功能模块

**搜索框**：
- 桌面端（>1400px）：始终显示完整搜索框
- 中等屏幕（769-1400px）：默认只显示图标，点击展开
- 移动端（<768px）：始终显示，全宽
- 回车跳转 `/search/article?q=...`
- 聚焦时加载搜索历史（下拉标签）

**导航菜单**：
- 从 API 获取导航列表
- `isList === 0`：普通链接
- `isList === 1`：下拉菜单（n-dropdown），包含子项

**用户区域**：
- 主题切换（日/夜 SVG 图标，切换 `colorMode.preference`）
- 创作按钮（创作者可见）：下拉菜单（写文章、写随笔、时光小记）
- 通知铃铛：下拉显示 5 类未读通知，数据来自 WebSocket
- 用户头像弹出框：个人信息、统计数据、快捷菜单、退出登录
- 登录按钮（未登录时显示）

**隐藏预加载链接**：

```html
<!-- 预加载写作页面的 JS chunk，避免首次点击卡顿 -->
<NuxtLink prefetch to="/write/article" class="hidden-prefetch" />
<NuxtLink prefetch to="/write/note" class="hidden-prefetch" />
```

### 移动端

- 固定顶部栏，56px 高
- Logo 和导航菜单隐藏
- 搜索框全宽显示
- 汉堡菜单按钮打开侧滑抽屉（220px 宽），包含完整导航
- 主题切换和创作按钮隐藏

### 粘性行为

`position: sticky; top: 0`，滚动时 `.is-sticky` 触发样式变化：圆角从 32px 缩小到 6px，阴影增强。

---

## 路由系统

### 渲染策略（SSR / CSR）

在 `nuxt.config.ts` 的 `routeRules` 中配置：

| 页面类型 | 渲染方式 | 原因 |
|---------|---------|------|
| 首页、文章列表/详情、分类、标签 | SSR | SEO 友好 |
| 个人主页、关于页 | SSR | SEO 友好 |
| 搜索页 | SSR | URL 可分享 |
| 写作页面（/write/**） | CSR + prefetch | 需登录、重依赖（md-editor） |
| 后台管理（/admin/**） | CSR | 纯交互 |
| 登录/注册 | CSR | 客户端交互 |
| 通知/消息 | CSR | 需登录 |

### 写作页面预加载

写作页面使用 CSR（需要 md-editor-v3 等重型依赖），首次点击时加载慢。通过隐藏的 `<NuxtLink prefetch>` 提前下载 JS chunk：

```typescript
// nuxt.config.ts
routeRules: {
  '/write/**': { ssr: false, prefetch: true }
}
```

### 公开页面（SSR）

| 路由 | 说明 | 布局 | 侧边栏 |
|------|------|------|--------|
| `/` | 首页（无限滚动文章流） | home | home |
| `/latest` | 最新文章 | default | home |
| `/featured` | 精选内容 | default | home |
| `/follow` | 关注（允许未登录渲染） | default | home |
| `/articles/[id]` | 文章详情 | default | article |
| `/category/[id]` | 分类详情 | default | home |
| `/tags/**` | 标签页 | default | home |
| `/search/*` | 搜索 | default | home |
| `/reading-essays` | 阅读随笔列表 | default | readingEssays |
| `/reading-essays/[id]` | 随笔详情 | default | — |
| `/time-notes` | 时光小记列表 | default | false |
| `/time-notes/[id]` | 小记详情 | false（无布局） | — |
| `/user-home/[menu]/[uid]` | 个人主页 | user-home | — |
| `/about` | 关于 | default | article |

### 认证页面（CSR）

| 路由 | 说明 |
|------|------|
| `/write/article/[[id]]` | 写文章（新建/编辑） |
| `/write/note/[[id]]` | 写时光小记 |
| `/admin/**` | 创作者后台 |
| `/news` | 通知消息 |
| `/settings` | 个人设置 |

---

## admin 布局

### 结构

```
┌──────────────┬──────────────────────────────────────┐
│              │                                      │
│   左侧菜单    │            <slot />                   │
│   （220px）   │            页面内容                    │
│              │                                      │
│  ──────────  │                                      │
│  返回首页     │                                      │
│  用户卡片     │                                      │
│  菜单树       │                                      │
│  申请创作者   │                                      │
│              │                                      │
└──────────────┴──────────────────────────────────────┘
```

**权限**：`middleware: 'creator'`，需要创作者角色。未登录重定向到首页。

**菜单**：从 `sidebarApi.getList(2, 0)` 获取，递归构建 Naive UI `n-menu`。父级菜单自动展开。

---

## user-home 布局

### 结构

```
┌──────────────────────────────────────────────────────┐
│  头部卡片：头像、昵称、角色徽章、简介、关注/私信按钮      │
│  统计行：文章 | 随笔 | 小记 | 专栏 | 关注 | 粉丝        │
├──────────────────────────────────────────────────────┤
│  Tab 菜单（从 API 获取）                               │
├──────────────────────────────────┬───────────────────┤
│                                  │                   │
│         <slot />                 │   统计面板         │
│         页面内容                  │   （3列网格）      │
│                                  │                   │
└──────────────────────────────────┴───────────────────┘
```

**数据获取**：`specialDetailApi.getUserInfo(uid)` + `navigationApi.getList(2, 0)`

**Tab 菜单**：根据权限过滤（公开、仅自己、需登录、VIP）。

**移动端**：右侧统计面板隐藏，统计行显示在头部卡片内。
