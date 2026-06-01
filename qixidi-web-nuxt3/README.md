# 栖息地 - 知识社区博客

栖息地是一个基于 Nuxt 3 构建的知识社区博客系统，支持 SSR 渲染、深色主题切换、Markdown 编辑等功能。

## 技术栈

- **框架**：Nuxt 3.21 + Vue 3.5
- **UI 组件**：Naive UI
- **状态管理**：Pinia
- **样式**：Tailwind CSS 4 + CSS 变量
- **Markdown**：md-editor-v3（编辑）+ unified（渲染）
- **类型**：TypeScript（严格模式）

## 开发环境要求

- Node.js >= 22.12.0
- npm >= 10.0.0

## 快速开始

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

访问 http://localhost:9007/

## 项目结构

```
qixidi-web-nuxt3/
├── assets/          # 静态资源
│   ├── css/        # 样式文件（tailwind.css + main.css）
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
├── nuxt.config.ts   # Nuxt 配置文件
└── ecosystem.config.cjs  # PM2 生产部署配置
```

## 配置说明

### API 地址

开发环境默认配置（写在 `nuxt.config.ts` 的 runtimeConfig 中）：

| 配置项              | 默认值                     | 说明                |
|------------------|-------------------------|-------------------|
| `apiBase`        | `http://127.0.0.1:9001` | SSR 服务端直连后端       |
| `public.apiBase` | `/api`                  | 客户端通过 devProxy 代理 |
| `public.wsBase`  | `http://127.0.0.1:9001` | WebSocket 地址      |

开发模式下客户端请求 `/api/xxx` 由 Nitro devProxy 转发到后端。生产模式通过 `ecosystem.config.cjs` 环境变量覆盖。

### 端口配置

开发服务器默认运行在 **9007** 端口，可在 `nuxt.config.ts` 中修改。

## 渲染策略

| 页面类型    | 渲染方式 | 原因     |
|---------|------|--------|
| 文章列表/详情 | SSR  | SEO 友好 |
| 标签/分类页  | SSR  | SEO 友好 |
| 登录/注册   | CSR  | 客户端交互  |
| 后台管理    | CSR  | 复杂交互   |

## 开发规范

- **主题**：所有新增代码必须支持深色/浅色双主题，使用 CSS 变量，禁止硬编码颜色
- **SSR**：`useMessage()`、`useDialog()` 等不能在 SSR 阶段调用，放在事件处理函数内或使用 `.client.vue` 后缀
- **图标**：统一使用 Tabler Icons（`@vicons/tabler`）
- 详细规范见 `.claude/rules/frontend/` 目录

---

## 生产部署（PM2）

### 服务器要求

- Node.js >= 22.12.0
- PM2（全局安装）
- Nginx（反向代理，可选）

### 环境变量

生产环境通过 `ecosystem.config.cjs` 的 `env` 字段注入。

```js
module.exports = {
    apps: [{
        name: 'qixidi-web',
        script: '.output/server/index.mjs',
        env: {
            HOST: '0.0.0.0',
            PORT: 9007,
            NUXT_API_BASE: 'http://127.0.0.1:9001',
            NUXT_PUBLIC_API_BASE: 'http://127.0.0.1:9001',
            NUXT_PUBLIC_WS_BASE: 'http://127.0.0.1:9001'
        }
    }]
}
```

| 变量                     | 用途        | 说明          |
|------------------------|-----------|-------------|
| `NUXT_API_BASE`        | SSR 服务端   | 服务端请求后端的地址  |
| `NUXT_PUBLIC_API_BASE` | 客户端       | 浏览器请求后端的地址  |
| `NUXT_PUBLIC_WS_BASE`  | WebSocket | WebSocket 地址 |

`ecosystem.config.cjs` 只需第一次手动上传，后续一般不需要改动。

### 发版流程

构建后 `.output/` 已通过 Nitro `noExternals` 配置内联所有依赖，可直接打包：

```bash
# 1. 构建
npm run build

# 2. 打包为 zip
npm run pack

## 或者（打包加压缩）
npm run build && npm run pack
  
##  切换控制台到power
pwsh

# 3. 上传并重启
scp ../deployment.zip user@server:/path/to/app/
ssh user@server
cd /data/qixidi/web
unzip -o deployment.zip
pm2 restart qixidi-web --update-env
```

本地测试可使用 `npm run pack:dir`，将 `.output` 复制到 `../deployment/`。

### 首次部署

```bash
# 首次需要额外上传 ecosystem.config.cjs 并启动
scp ecosystem.config.cjs user@server:/path/to/app/
ssh user@server
cd /path/to/app/
unzip -o deployment.zip
pm2 start ecosystem.config.cjs
pm2 save
pm2 startup
```

### PM2 常用命令

```bash
pm2 start ecosystem.config.cjs       # 启动
pm2 restart qixidi-web --update-env  # 重启（刷新环境变量）
pm2 status                            # 查看状态
pm2 logs qixidi-web                  # 查看日志
pm2 monit                            # 监控面板
pm2 save                             # 保存进程列表
pm2 startup                          # 设置开机自启
```

### Nginx 反向代理参考

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://127.0.0.1:9007;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
```

### 服务器脚本
```shell
#!/bin/bash

cd /data/qixidi/web
unzip -o deployment.zip
pm2 restart qixidi-web --update-env
```
