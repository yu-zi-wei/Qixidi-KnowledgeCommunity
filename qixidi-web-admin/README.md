# qixidi-web-admin

栖息地博客的**管理后台 2.0**，基于 Nuxt 3 重写（旧版 Vue 2 + Element UI 后台已归档至 `qixidi-web-admin-1.0`，仅存档不再开发）。

## 技术栈

Nuxt 3 + TypeScript + Naive UI + Pinia（persistedstate）+ Tailwind 4 + @vicons/tabler

与门户（qixidi-web-nuxt3）同栈，全 CSR 渲染（无 SEO 诉求），dev 通过 `/api` 代理到本地后端。

## 启动

```bash
pnpm install
pnpm dev        # http://localhost:3000，需后端 qixidi-service 运行在 127.0.0.1:9001
```

## 开发导航

**需求与设计（唯一需求源）**：[docs/开发需求与设计.md](./docs/开发需求与设计.md)

- 当前状态：M0 骨架 + M1（登录对接 + 数据看板）已完成，M2 三个管理页（文章/评论/用户）为下一步
- 权限模型：登录即管理员（静态路由，无 RBAC），配套后端 sys 表瘦身方案见文档 §5
- 开发每个页面前，先到 `qixidi-service` grep 对应 Controller 确认接口字段，禁止凭记忆扩写
