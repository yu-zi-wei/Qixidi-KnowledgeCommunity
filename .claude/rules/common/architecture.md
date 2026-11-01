# 系统架构设计

栖息地博客项目的系统架构和设计原则。

---

## 整体架构

**前后端分离架构**：

```
用户 → 前端（Nuxt 3） → API 网关 → 后端（Spring Boot） → 数据库
```

### 技术选型

| 层级 | 技术选型 |
|------|----------|
| 门户前端 | qixidi-web-nuxt3 · Nuxt 3 + Vue 3 + Naive UI（SSR） |
| 管理后台前端 | qixidi-web-admin · Nuxt 3 + TS + Naive UI（全 CSR） |
| 状态管理 | Pinia |
| 后端 | Spring Boot 4.0 + Java 17 |
| ORM | MyBatis Plus |
| 认证 | Sa-Token |
| 缓存 | Redis |
| AI | Spring AI 2.0（qixidi-ai）+ MCP 端点（qixidi-mcp） |
| 存储 | MySQL + OSS |

---

## 前端架构

### 双前端应用

| 应用 | 渲染 | 职责 |
|------|------|------|
| qixidi-web-nuxt3 | SSR 为主（交互页按需 CSR） | 门户 + 用户后台 |
| qixidi-web-admin | 全 CSR（routeRules 关闭 SSR） | 管理后台（八大功能域，AdminListPage 骨架） |

### 门户目录结构

```
qixidi-web-nuxt3/
├── assets/          # 静态资源
├── components/      # 公共组件
├── composables/     # 组合式函数（自动导入）
├── layouts/         # 布局组件
├── middleware/      # 中间件
├── pages/           # 页面（文件路由）
├── stores/          # Pinia 状态管理
├── types/           # TypeScript 类型
└── utils/           # 工具函数
```

### 渲染策略

| 页面类型 | 渲染方式 | 原因 |
|---------|---------|------|
| 文章列表/详情 | SSR | SEO 友好 |
| 标签/分类页 | SSR | SEO 友好 |
| 搜索页 | CSR | 实时交互 |
| 登录/注册 | CSR | 客户端交互 |
| 后台管理 | CSR | 复杂交互 |

### 组件设计

- **抽取原则**：复用 ≥ 2 次或逻辑复杂
- **通信方式**：props/emit、provide/inject、Pinia

---

## 后端架构

### 模块划分

```
qixidi-service/
├── qixidi-startup    # 启动模块
├── framework         # 框架层
├── qixidi-system     # 系统模块（含后台管理 Controller，URL 统一 /system/*）
├── qixidi-auth      # 认证模块
├── qixidi-mcp       # MCP 服务模块（无状态 /mcp 端点 + API key 会话桥）
├── qixidi-ai        # AI 模块（Spring AI，业务接 AI 的唯一入口）
├── qixidi-business  # 业务模块（system 依赖 business）
│   ├── api/         # 前台 Controller（按业务子目录平铺）
│   ├── domain/      # entity/bo/vo/enums
│   ├── service/     # Service 层
│   ├── mapper/      # Mapper 层
│   └── task/        # 定时任务
└── qixidi-common    # 公共模块
```

### MVC 三层架构

```
Controller 层（api）
    ↓
Service 层（service）
    ↓
Mapper 层（mapper）
```

| 调用方向 | ✅ 允许 | ❌ 禁止 |
|---------|--------|--------|
| Controller → Service | ✅ | - |
| Service → Mapper | ✅ | - |
| Controller → Mapper | - | ❌ |
| 下层 → 上层 | - | ❌ |

---

## 数据流设计

### 前端

```
用户操作 → 组件事件 → Composable（useXxxApi） → $fetch → 后端 API
```

### 后端

```
Controller（接收请求） → Service（业务逻辑） → Mapper（数据访问） → Database
```

---

## 安全设计

- **认证**：Sa-Token JWT
- **授权**：RBAC
- **会话**：Redis 存储
- **密码**：BCrypt 加密
- **SQL 注入防护**：MyBatis 参数绑定
- **XSS 防护**：前端过滤 + 后端转义
- **接口鉴权**：Token 验证
- **限流**：IP 计数防刷拦截器（超频拉黑，黑名单仅内存）

---

## 性能优化

### 前端
- SSR 渲染（首屏直出）
- 代码分割（路由懒加载）
- 资源优化（压缩、CDN）
- 缓存策略（HTTP + Redis）

### 后端
- 数据库优化（索引、查询优化）
- 缓存策略（Redis 多级缓存）
- 异步处理（定时任务 task/）
- 连接池（数据库、Redis）

---

**核心原则**：高内聚、低耦合、职责清晰、易于扩展。
