# Rules 规范索引

栖息地项目的编码规范和架构原则。

---

## 目录

### 通用规则（所有项目）

| 规则 | 说明 |
|------|------|
| [architecture.md](architecture.md) | 系统架构设计、前后端分离、MVC 三层架构 |
| [code-basics.md](code-basics.md) | 命名规范、注释规范、错误处理、类型安全 |

### 前端规则（Nuxt 3）

| 规则 | 说明 |
|------|------|
| [qixidi-web-nuxt3.md](../frontend/qixidi-web-nuxt3.md) | Nuxt 3、SSR/CSR、主题切换、API 调用 |
| [design-system.md](../frontend/design-system.md) | 设计系统、CSS 变量、样式开发规范 |
| [nuxt3-data-fetching.md](../frontend/nuxt3-data-fetching.md) | useAsyncData、无限滚动、分页状态管理 |
| [global-component-management.md](../frontend/global-component-management.md) | 全局组件状态管理（弹窗、对话框） |
| [route-auth-interaction.md](../frontend/route-auth-interaction.md) | 路由认证交互方式（登录框、重定向） |
| [deployment.md](../frontend/deployment.md) | 部署配置：SSR 用 127.0.0.1、PM2 重启规范、nginx 反代 |

### 后端规则（Spring Boot）

| 规则 | 说明 |
|------|------|
| [qixidi-service.md](../backend/qixidi-service) | Spring Boot、MyBatis Plus、MVC 分层、事务管理 |

---

## 使用指南

### AI 开发前必读

1. 根据项目类型加载对应规则
2. 通用规则（`architecture、code-basics）适用于所有项目
3. 前端开发额外加载 frontend/ 目录规则
4. 后端开发额外加载 backend/ 目录规则

### 规则优先级

1. **项目特定规则**（frontend/*/ backend/*） > 通用规则（common/*）
2. **架构原则** > 编码规范
3. **强制规则**（❌ 禁止） > 推荐实践（✅ 正确）

---

**核心原则**：Rules 是"地图"，告诉 AI "在哪里"和"注意什么"，而不是罗列所有细节。
`
