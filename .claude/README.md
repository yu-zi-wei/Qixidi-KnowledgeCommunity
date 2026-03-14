# Claude Code 配置目录

栖息地博客项目的 AI 开发导航入口。

---

## 目录结构

```
.claude/
├── CLAUDE.md              # 项目主配置（AI 优先读取）
├── README.md              # 本文件
├── memory/                # 项目记忆（经验教训）
│   └── MEMORY.md
├── rules/                 # 规则文件（通用规范）
│   ├── common/            # 通用规则（所有项目）
│   ├── frontend/          # 前端规则
│   └── backend/           # 后端规则
└── skills/                # 业务技能（模块特定）
    ├── common/            # 通用技能
    ├── frontend/          # 前端技能
    └── backend/           # 后端技能（暂无）
```

---

## 快速导航

- **[CLAUDE.md](../CLAUDE.md)** - 项目主配置文件
- **[Rules 索引](rules/common/README.md)** - 规范和约定
- **[Skills 索引](skills/common/INDEX.md)** - 业务模块知识

---

## 规则文件说明

### 通用规则（rules/common/）

- **[architecture.md](rules/common/architecture.md)** - 系统架构设计原则
- **[code-basics.md](rules/common/code-basics.md)** - 通用编码规范

### 前端规则（rules/frontend/）

- **[qixidi-web-nuxt3.md](rules/frontend/qixidi-web-nuxt3.md)** - Nuxt 3 开发规范
- **[global-component-management.md](rules/frontend/global-component-management.md)** - 全局组件状态管理

### 后端规则（rules/backend/）

- **[qixidi-service-plus.md](rules/backend/qixidi-service-plus.md)** - Spring Boot 开发规范

---

## 技能文件说明

### 前端技能（skills/frontend/）

- **[登录对话框](skills/frontend/auth/login-dialog.md)** - 登录/注册/重置密码弹窗实现

### 通用技能（skills/common/）

- **[规则管理](skills/common/rules-management.md)** - 如何创建和维护规则
- **[技能模板](skills/common/TEMPLATE.md)** - 新技能文件模板

---

## AI 工作流程

```
1. 读取 CLAUDE.md（项目配置）
   ↓
2. 查询 skills/common/INDEX.md（业务索引）
   ↓
3. 加载相关 Rules（规范文件）
   ↓
4. 阅读 Skill 文件（业务知识）
   ↓
5. 实现功能
   ↓
6. ⭐ 验证：检查是否违反 Rules
   ↓
7. ⭐⭐⭐ 规则提炼：更新 Rules 和 Skills
```

---

## 核心原则

**一次错误，终身受益。同样的问题不能出现两次。**

每次被纠正后，必须：
1. 分析根本原因
2. 提取通用规则
3. 创建/更新规则文件
4. 更新记忆文档
