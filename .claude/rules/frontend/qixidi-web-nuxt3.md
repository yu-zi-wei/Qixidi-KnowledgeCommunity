---
description: 仅在开发 qixidi-web-nuxt3 项目时遵循
globs: qixidi-web-nuxt3/**
---

# qixidi-web-nuxt3 开发规范

栖息地博客前端开发规范。

---

## API 响应格式

| 类型 | 格式 | 调用方法 |
|------|------|----------|
| 非分页 | `{ code, msg, data }` | `api.get/post/put/delete`（自动解包） |
| 分页 | `{ total, rows }` | `api.getPage` |

---

## 接口对接规范（重要）

**开发新接口前**：必须先去 `qixidi-service-plus` 项目查看 Controller 层的接口定义，确保：

### 1. 查看接口定义
**后端项目位置**：`D:\Project\qixidi\qixidi-service-plus`

**常见 Controller 位置**：
- 文件上传：`qixidi-system/src/main/java/com/qixidi/system/api/SysOssController.java`
- 文章管理：`qixidi-business/qixidi-article/api/` 下的 Controller
- 用户管理：`qixidi-system/src/main/java/com/qixidi/system/api/` 下的 Controller

**查找命令**：
```bash
# 在 qixidi-service-plus 根目录执行
grep -r "@PostMapping.*接口路径" --include="*.java"
```

### 2. 确认参数格式
| 注解 | 参数位置 | 示例 |
|------|----------|------|
| `@RequestParam` | URL 参数 | `?key=value` |
| `@RequestBody` | JSON Body | `{ "key": "value" }` |
| `@RequestPart("file")` | FormData | 文件上传（multipart/form-data） |

### 3. 确认返回格式
| 返回类型 | 格式 | 处理方式 |
|----------|------|----------|
| `Result.ok(data)` | `{ code, msg, data }` | `api.get/post` 自动解包 |
| `TableDataInfo` | `{ total, rows }` | `api.getPage` |
| `Map<String, String>` | 直接返回对象 | **不被 R 包装，需要特殊处理** |

**示例（文件上传接口）**：
```java
// 后端定义
@PostMapping("/upload")
public Map<String, String> upload(@RequestPart("file") MultipartFile file) {
    Map<String, String> map = new HashMap<>();
    map.put("url", oss.getUrl());
    return map;  // 注意：直接返回 Map，不是 R 包装
}
```

```typescript
// 前端处理
const uploadFile = async (file: File): Promise<string> => {
  const formData = new FormData()
  formData.append('file', file)  // 参数名必须匹配 @RequestPart("file")

  // 返回的是 Map，不是 { code, msg, data }
  const result = await fetch(url, { method: 'POST', body: formData })
  const data = await result.json()
  return data.url  // 直接访问 url 字段
}
```

---

## SSR 注意事项

- `useMessage()`、`useDialog()` 等需要 provider（已配置）
- 这些 composable **不能在 SSR 阶段的 setup 顶层调用**
- 解决方案：放在事件处理函数内，或使用 `.client.vue` 后缀
- 影响 CSS 布局的关键样式放全局 `main.css`，避免 scoped CSS 导致样式闪烁

---

## 主题切换（强制）

所有新增代码必须支持浅色/深色双主题：

| 禁止 | 允许 |
|------|------|
| 硬编码颜色值（`#fff`、`#333`） | CSS 变量（`--color-text`） |
| 单一主题配色 | `:root` 和 `.dark` 双变量 |

- Tailwind 使用 `dark:` variant：`bg-white dark:bg-gray-900`
- Naive UI 主题自动切换（已配置）
- 自测时必须切换主题检查视觉效果

---

## 样式开发（强制）

所有样式开发必须遵循统一的设计系统：

| 禁止 | 允许 |
|------|------|
| 硬编码颜色、字号、间距 | CSS 变量（`var(--text-xl)`） |
| 每个组件重复定义样式 | 复用全局样式 |
| 随意的动画时长 | `var(--transition-base)` |

**设计变量详见**：[设计系统规范](design-system.md)

**核心原则**：
- 使用设计 token，禁止硬编码
- 复用全局样式，避免重复
- 所有新增样式支持双主题

---

## 图标使用（强制）

**统一使用 Tabler Icons（通过 @vicons/tabler）**

### 安装的图标包
```bash
npm install @vicons/tabler
```

### 使用方式

**✅ 正确：从 @vicons 导入组件**
```vue
<script setup lang="ts">
import { Eye, Heart } from '@vicons/tabler'
</script>

<template>
  <Eye class="icon" />
  <Heart class="icon" />
</template>
```

**❌ 禁止：使用其他图标方式**
```vue
<!-- ❌ 禁止：使用 @nuxt/icon -->
<Icon name="mdi:eye-outline" />

<!-- ❌ 禁止：使用 Emoji 替代图标 -->
<span>👁</span>
```

### 常用图标

| 功能 | Tabler 图标 |
|------|-------------|
| 浏览 | `Eye` |
| 点赞/喜欢 | `Heart` |
| 评论 | `MessageCircle` |
| 收藏 | `Bookmark` |
| 收藏(实心) | `BookmarkFilled` |
| 分享 | `Share` |
| 搜索 | `Search` |
| 设置 | `Settings` |
| 用户 | `User` |
| 时钟 | `Clock` |
| 日历 | `Calendar` |
| 首页 | `Home` |
| 箭头 | `ArrowUp` / `ArrowDown` |
| 更多 | `Dots` |
| 关闭 | `X` |
| 加载中 | `Loader2` |
| 编辑 | `Edit` |
| 删除 | `Trash` |
| 过滤 | `Filter` |

### 图标样式

```css
.stat-icon {
  width: 15px;
  height: 15px;
  color: var(--color-ink-muted);
  stroke-width: 1.5;
  flex-shrink: 0;
}
```

### Tabler Icons 特色

- **命名简洁**：`Eye` 而非 `Eye`
- **风格统一**：所有图标 2px 描边圆角
- **数量丰富**：4000+ 图标
- **SVG 优化**：代码精简

图标查找：https://tabler-icons.io/

---

## 核心约定

| 约定 | 说明 |
|------|------|
| **API 模块** | 放 `composables/` 目录，命名 `useXxxApi.ts`（自动导入） |
| **数据获取** | 用 `useAsyncData` + computed key，详见 [数据获取规范](nuxt3-data-fetching.md) |
| **表单校验** | 用 Naive UI 内置，不用 VeeValidate/Zod |
| **Markdown** | 编辑器 client-only，渲染用 unified（SSR 兼容） |
| **组件抽取** | 复用 ≥ 2 次或逻辑复杂，不过度拆分 |
| **路由** | 后台管理用 Nuxt 嵌套路由 |
| **渲染** | 前台 SSR、后台/登录 CSR（routeRules 已配置） |
| **样式** | 简单用 Tailwind 工具类，复杂用 scoped CSS |

---

## 数据获取（重要）

**使用 `useAsyncData` 的强制规则**：

| 场景 | ✅ 正确 | ❌ 错误 |
|------|--------|--------|
| 路由参数变化 | computed key | 固定 key + 手动 watch |
| 初始加载状态 | 用 `pending` | 自定义 `isReady` |
| 阻止过早触发 | `pending` 控制 loadMore | `pageNum` 从 0 开始 |
| 分页管理 | `pageNum` 从 1 开始，数据到达重置为 1 | `pageNum` 从 0 开始或复杂逻辑 |

**核心原则**：利用框架能力，避免自定义状态管理。详见 [nuxt3-data-fetching.md](nuxt3-data-fetching.md)。

---

**核心原则**：所有新代码必须支持双主题，禁止硬编码颜色。
