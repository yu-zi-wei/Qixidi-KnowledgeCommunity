# 栖息地 - 知识社区博客

栖息地是一个基于 Nuxt 3 构建的知识社区博客系统，支持 SSR 渲染、深色主题切换、Markdown 编辑等功能。

## 技术栈

- **框架**：Nuxt 3.17 + Vue 3.5
- **UI 组件**：Naive UI
- **状态管理**：Pinia
- **样式**：Tailwind CSS + CSS 变量
- **Markdown**：md-editor-v3（编辑）+ unified（渲染）
- **类型**：TypeScript（严格模式）

## 功能特性

- ✅ SSR/CSR 混合渲染（前台 SSR，后台 CSR）
- ✅ 深色/浅色主题切换
- ✅ Markdown 文章编辑与发布
- ✅ 分类与标签管理
- ✅ 评论与互动功能
- ✅ 用户权限管理
- ✅ SEO 优化

## 开发环境要求

- Node.js >= 18.17.0
- npm >= 9.0.0

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:9007/

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

### 生成静态站点

```bash
npm run generate
```

## 项目结构

```
qixidi-web-nuxt3/
├── assets/          # 静态资源
│   ├── css/        # 样式文件
│   └── images/     # 图片资源
├── components/      # 公共组件
├── composables/     # 组合式函数（自动导入）
├── layouts/         # 布局组件
├── middleware/      # 中间件（路由守卫）
├── pages/           # 页面（文件路由）
├── plugins/         # 插件
├── public/          # 公共静态资源
├── stores/          # Pinia 状态管理
├── types/           # TypeScript 类型定义
├── utils/           # 工具函数
├── app.vue          # 根组件
└── nuxt.config.ts   # Nuxt 配置文件
```

## 配置说明

### 端口配置

开发服务器默认运行在 **9007** 端口，可在 `nuxt.config.ts` 中修改。

### API 代理

开发环境下，`/api` 请求会被代理到后端服务器（默认 `http://116.198.203.132:9001`）。

### 环境变量

- `API_SERVER_URL`：服务端 API 地址（SSR 使用）
- `NUXT_PUBLIC_API_BASE`：客户端 API 基础路径
- `API_PROXY_TARGET`：开发代理目标地址

## 开发规范

### 主题开发

**所有新增代码必须支持深色/浅色双主题**：

- 颜色一律使用 CSS 变量
- 禁止硬编码颜色值
- Tailwind 使用 `dark:` variant
- 自测时必须切换主题检查

### SSR 注意事项

- `useMessage()`、`useDialog()` 等不能在 SSR 阶段调用
- 应放在事件处理函数内，或使用 `.client.vue` 后缀
- 关键样式放全局 `main.css`

## 渲染策略

| 页面类型 | 渲染方式 | 原因 |
|---------|---------|------|
| 文章列表/详情 | SSR | SEO 友好 |
| 标签/分类页 | SSR | SEO 友好 |
| 登录/注册 | CSR | 客户端交互 |
| 后台管理 | CSR | 复杂交互 |

## 常见问题

### Q: 为什么使用 Nuxt 3 而不是 Nuxt 4？

A: Nuxt 4 在 Windows 上存在原生绑定兼容性问题（`@oxc-parser`、`@oxc-transform` 等），导致项目无法启动。目前使用 Nuxt 3.17.7 稳定版本。等 Nuxt 4 修复 Windows 兼容性问题后，可以平滑升级。

### Q: 如何升级到 Nuxt 4？

A: Nuxt 3.17+ 已经为 Nuxt 4 做好了准备，升级会很平滑：

```bash
# 更新 Nuxt 到 4.x
npm install nuxt@latest

# 更新相关依赖
npm install @nuxt/devtools@latest
npm install @nuxtjs/color-mode@latest
```

Nuxt 3 → Nuxt 4 的升级成本非常低，不需要像 Vue 2 → Vue 3 那样重写代码。

---

**文档更新日期**：2026-03-07
