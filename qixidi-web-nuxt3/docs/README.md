# 栖息地前台 — 项目文档

> 技术栈：Nuxt 3 + Vue 3 + Naive UI + Pinia + Tailwind CSS v4

---

## 文档索引

| 文档 | 说明 |
|------|------|
| [Markdown 渲染系统](./markdown-rendering.md) | 渲染管线、预处理、Unified 插件、客户端 Hydrate、SSR 渲染、样式系统 |
| [布局与路由](./layouts-and-routing.md) | 6 种布局、侧边栏切换机制、路由权限、SSR/CSR 策略 |
| [API 与状态管理](./api-and-state.md) | useApi 封装、Pinia Stores、WebSocket、无限滚动 |
| [路由权限](./ROUTE_AUTH.md) | 认证中间件、登录框交互、页面保护 |

---

## 项目概览

栖息地是一个知识社区博客系统，前台项目（`qixidi-web-nuxt3`）负责所有用户可见的内容展示。

### 核心业务模块

- **文章系统**：列表、详情、分类、标签、专栏、收藏、评论、点赞
- **阅读随笔**：以专辑分组的短文，支持瀑布流展示
- **时光小记**：按日期分组的简短记录
- **社交互动**：关注、收藏、评论、私信、通知
- **后台管理**：文章/随笔/时光小记的 CRUD、数据统计
- **用户系统**：注册/登录、个人主页、创作者申请

### 开发环境

```bash
pnpm install    # 安装依赖
pnpm dev        # 启动开发服务器（端口 9007）
pnpm build      # 构建生产包
pnpm preview    # 本地预览生产包
```

后端 API 通过 Nitro devProxy 代理：前端 `/api/*` → 后端 `http://127.0.0.1:9001/*`

### 目录结构

```
qixidi-web-nuxt3/
├── assets/css/          # 全局样式（设计系统 + Tailwind）
├── components/          # Vue 组件
│   ├── article/         # 文章相关（详情、TOC、收藏、评论）
│   ├── common/          # 通用组件（空状态、编辑器包装）
│   ├── readingEssays/   # 阅读随笔（抽屉、卡片、瀑布流、侧边栏）
│   └── timeNotes/       # 时光小记（列表、详情、编辑器）
├── composables/         # 组合式函数（自动导入）
│   ├── useApi.ts        # HTTP 客户端封装
│   ├── useMarkdown.ts   # Markdown 渲染管线
│   ├── useInfiniteScroll.ts  # 无限滚动 + 滚动位置恢复
│   ├── useWebSocket.ts  # WebSocket 单例
│   └── use*Api.ts       # 各业务域 API 封装（30+ 个）
├── layouts/             # 布局组件（6 个）
├── middleware/          # 路由中间件（auth, creator）
├── pages/               # 页面（文件路由，~50 个）
├── stores/              # Pinia 状态管理（6 个）
└── utils/               # 工具函数（formatTime, emoji）
```

### 部署

```bash
pnpm build                # 输出到 .output/ 目录
pm2 start ecosystem.config.cjs  # PM2 管理
```

环境变量：

| 变量 | 说明 |
|------|------|
| `NUXT_PRIVATE_API_BASE` | 后端 API 地址（SSR 服务端直连） |
| `NUXT_PUBLIC_API_BASE` | 前端 API 地址（CSR，通过反向代理） |
| `NUXT_PUBLIC_WS_BASE` | WebSocket 地址 |

### 注意事项

1. **接口对接**：涉及后端接口时，必须先在 `qixidi-service` 项目中查看 Controller 定义
2. **CSS 变量**：禁止硬编码颜色值，统一使用 `var(--xxx)` 变量
3. **双主题**：所有新增样式必须支持浅色/深色主题
4. **图标**：统一使用 `@vicons/tabler`
5. **页面跳转**：用户可能中键点击的位置必须用 `<NuxtLink>`
6. **API 错误**：`useApi` 已统一拦截，页面禁止重复 `message.error`
7. **Scoped CSS 类名**：必须带组件前缀，避免 SSR 注入顺序导致覆盖
8. **SSR 兼容**：`useMessage()` 等需要 provider 的 composable 不能在 SSR setup 顶层调用
