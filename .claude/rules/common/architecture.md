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
| 前端 | Nuxt 3 + Vue 3 + Naive UI |
| 状态管理 | Pinia |
| 后端 | Spring Boot 3.3 + Java 17 |
| ORM | MyBatis Plus |
| 认证 | Sa-Token |
| 缓存 | Redis |
| 存储 | MySQL + OSS |

---

## 前端架构

### 目录结构

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
qixidi-service-plus/
├── qixidi-startup    # 启动模块
├── framework         # 框架层
├── qixidi-system     # 系统模块
├── qixidi-auth      # 认证模块
├── qixidi-business  # 业务模块
│   ├── api/         # Controller（backstage/frontDesk）
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
- **限流**：Redis + 令牌桶

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
- 异步处理（消息队列）
- 连接池（数据库、Redis）

---

**核心原则**：高内聚、低耦合、职责清晰、易于扩展。
