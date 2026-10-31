# qixidi-web-admin

栖息地博客的**管理后台 2.0**，基于 Nuxt 3 重写（旧版 Vue 2 + Element UI 后台已下线删除，git 历史可溯）。

## 技术栈

Nuxt 3 + TypeScript + Naive UI + Pinia（persistedstate）+ Tailwind 4 + @vicons/tabler

与门户（qixidi-web-nuxt3）同栈，全 CSR 渲染（无 SEO 诉求），dev 通过 `/api` 代理到本地后端。

## 启动

```bash
pnpm install
pnpm dev        # http://localhost:3000，需后端 qixidi-service 运行在 127.0.0.1:9001
```

## 打包与发布

全 CSR 项目，`nuxt generate` 产出纯静态文件（`.output/public`），nginx 直接托管，与旧后台（web-admin-1.0）的 dist.zip 运维模式完全兼容。

### 1. 本地打包（开发机）

```powershell
# 🔴 先停掉本地 dev server——dev 与 generate 共用 .nuxt 目录，
#    dev 运行中打包会产出损坏构建物（部署后整站 500），强制规则
pnpm build
# = nuxt cleanup + nuxt generate + 压缩产物，dist.zip 输出在项目外 ../dist.zip
```

依赖有变更时先 `pnpm install`。手动等价：`pnpm generate` 产物在 `.output/public`，压缩时**压 public 目录内的内容**（不带 public 这一层），否则解压后多套一层目录、线上 JS/CSS 全 404。

### 2. 上传与服务器部署

上传 `dist.zip` 到服务器，执行（沿用旧后台运维脚本，部署目录按服务器实际为准）：

```bash
cd /data/qixidi/web-admin
unzip -o dist.zip
/data/app/nginx/sbin/nginx -t
/data/app/nginx/sbin/nginx -s reload
```

- `unzip -o` 覆盖解压；旧 hash 文件会残留但无害，追求干净可先清空目录再解压
- 纯静态更新**无需 reload nginx**，`-t` 只是校验配置语法

### 3. nginx 配置要求（首次部署必查）

两个硬性要求，缺一不可：

1. **SPA history 路由兜底**：Nuxt 走 history 路由，直接访问/刷新 `/dictum/comment` 等子路径没有 `try_files` 兜底就是 404
2. **`/api` 反代**：前端 API 统一走 `/api` 前缀（`apiBase: '/api'`），须反代到本机后端 9001

```nginx
server {
    listen 80;
    server_name admin.qixidi.top;    # 按实际域名/端口调整

    root /data/qixidi/web-admin;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;   # SPA 兜底（硬性）
    }

    location /api/ {
        proxy_pass http://127.0.0.1:9001/;  # 末尾 / 去掉 /api 前缀（与门户一致）
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

改动 nginx 配置后：`nginx -t` 通过再 `nginx -s reload`。

### 4. 发布后验证

- [ ] 打开后台入口能进登录页
- [ ] 登录成功，数据看板有数据
- [ ] 刷新任一子路径页面（如 `/dictum/comment`）不 404——验证 `try_files` 兜底
- [ ] 浏览器 Network 面板确认 `/api` 请求全部 200（无跨域、无 404）

### 常见问题

| 现象 | 原因与处理 |
|------|-----------|
| 部署后整站 500 / 白屏 | 打包时 dev server 未停，构建物损坏——停 dev 重新 `generate` |
| 刷新子路径 404 | nginx 缺 `try_files ... /index.html` 兜底 |
| 登录页正常但登录报网络错误 | `/api` 未反代，或 `proxy_pass` 末尾 `/` 缺失导致前缀没去掉 |
| 样式/JS 404 | dist.zip 压的是 `public` 目录本身而非其内容，多套了一层目录 |

## 开发导航

**需求与设计（唯一需求源）**：[docs/开发需求与设计.md](./docs/开发需求与设计.md)

- 当前状态：M0 骨架 + M1（登录对接 + 数据看板）已完成，M2 三个管理页（文章/评论/用户）为下一步
- 权限模型：登录即管理员（静态路由，无 RBAC），配套后端 sys 表瘦身方案见文档 §5
- 开发每个页面前，先到 `qixidi-service` grep 对应 Controller 确认接口字段，禁止凭记忆扩写
