---
description: 仅在开发 qixidi-web-nuxt3 项目时遵循
globs: qixidi-web-nuxt3/**
---

# qixidi-web-nuxt3 开发规范

栖息地博客前端开发规范。

---

## 🔴🔴🔴 最高优先级规则

**当用户提供接口时，必须先去 `qixidi-service` 查看接口定义！绝对不能自己瞎编字段名！**

**正确流程**：
1. 用户给出接口路径
2. **立即** 去 `qixidi-service` 用 Grep 搜索该接口
3. 查看完整的 Controller 方法定义
4. 查看 VO/BO 实体类的字段定义
5. 确认请求参数（字段名、类型）
6. 确认返回值（字段名、类型）
7. 然后再编写前端代码

**错误示例**：
- ❌ 用户给了一个接口，我直接写代码
- ❌ 接口返回 `includedCount`，我写成 `articleNumber`
- ❌ 凭空猜测字段名

**正确示例**：
- ✅ 用户给接口 → 先 Grep 搜索后端 Controller → 查看 VO 字段定义 → 再写代码

---

## API 响应格式

| 类型 | 格式 | 调用方法 |
|------|------|----------|
| 非分页 | `{ code, msg, data }` | `api.get/post/put/delete`（自动解包） |
| 分页 | `{ total, rows }` | `api.getPage` |

---

## 接口对接规范

**后端项目位置**：`D:\Project\qixidi\qixidi-service`

**查找命令**：
```bash
# 搜索接口路径
grep -rn "接口路径" D:/Project/qixidi/qixidi-service --include="*.java"

# 查找 VO 文件
find D:/Project/qixidi/qixidi-service -name "*Vo.java"
```

### 参数接收方式

| 注解 | 参数位置 | 示例 |
|------|----------|------|
| `@RequestParam` | URL 参数 | `?key=value` |
| `@RequestBody` | JSON Body | `{ "key": "value" }` |
| `@RequestPart("file")` | FormData | 文件上传（multipart/form-data） |
| `@PathVariable` | 路径参数 | `/api/user/{id}` |

---

## SSR 注意事项

- `useMessage()`、`useDialog()` 等需要 provider（已配置）
- 这些 composable **不能在 SSR 阶段的 setup 顶层调用**
- 解决方案：放在事件处理函数内，或使用 `.client.vue` 后缀
- 影响 CSS 布局的关键样式放全局 `main.css`，避免 scoped CSS 导致样式闪烁

---

## HTML 嵌套禁忌（SSR 必读）

**浏览器会自动"修复"非法嵌套的 DOM，导致 SSR 输出与客户端不一致，引发布局错位。**

### 禁止嵌套的交互元素

| 外层 | 内层禁止 | 替代方案 |
|------|---------|---------|
| `<a>`（NuxtLink） | `<a>`（NuxtLink） | 内层用 `<span @click.stop="navigateTo(...)">` |
| `<a>`（NuxtLink） | `<button>` | 用 `<span>` 模拟按钮样式 |
| `<button>` | `<a>`（NuxtLink） | 外层改用 `<div>` |
| `<button>` | `<button>` | 拆分为并列元素 |
| `<form>` | `<form>` | 不可嵌套 |
| `<label>` | `<label>` | 不可嵌套 |

### 检测方法

刷新页面时如果出现布局错位，检查是否有嵌套的 `<a>` 标签（F12 查看 DOM 结构是否被浏览器拆开）。

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

---

## 图标使用（强制）

**统一使用 Tabler Icons（通过 @vicons/tabler）**

```vue
<script setup lang="ts">
import { Eye, Heart } from '@vicons/tabler'
</script>

<template>
  <Eye class="icon" />
  <Heart class="icon" />
</template>
```

**❌ 禁止**：使用 @nuxt/icon 或 Emoji 替代图标

---

## 核心约定

| 约定 | 说明 |
|------|------|
| **API 模块** | 放 `composables/` 目录，命名 `useXxxApi.ts`（自动导入） |
| **数据获取** | 用 `useAsyncData` + computed key |
| **表单校验** | 用 Naive UI 内置，不用 VeeValidate/Zod |
| **Markdown** | 编辑器 client-only，渲染用 unified（SSR 兼容） |
| **组件抽取** | 复用 ≥ 2 次或逻辑复杂，不过度拆分 |
| **样式** | 简单用 Tailwind 工具类，复杂用 scoped CSS |

---

## 页面跳转规范（强制）

**所有用户可能中键点击（打开新标签页）的跳转，必须使用 `<NuxtLink>` 而非 `@click + navigateTo`。**

### 原因

`@click="navigateTo(...)"` 只能左键跳转，无法通过鼠标中键在新标签页打开，用户体验差。

### 适用场景

| 场景 | 用 `<NuxtLink>` | 用 `navigateTo` |
|------|-----------------|-----------------|
| 文章卡片点击 | ✅ | ❌ |
| 列表项跳转 | ✅ | ❌ |
| 搜索历史标签 | ✅ | ❌ |
| 侧边栏筛选标签 | ✅ | ❌ |
| 通知图标 | ✅ | ❌ |
| 按钮操作（返回、编辑） | ❌ | ✅ |
| 登录后重定向 | ❌ | ✅ |
| 表单提交后跳转 | ❌ | ✅ |
| 条件逻辑跳转 | ❌ | ✅ |

### 正确示例

```vue
<!-- ✅ 文章卡片：用 NuxtLink -->
<NuxtLink :to="`/articles/${article.id}`" class="article-card">
  {{ article.title }}
</NuxtLink>

<!-- ✅ 侧边栏标签：用 NuxtLink -->
<NuxtLink :to="{ path: '/reading-essays', query: { albumId: album.id } }" class="tag-item">
  {{ album.name }}
</NuxtLink>
```

### 错误示例

```vue
<!-- ❌ 无法中键打开新标签页 -->
<div class="article-card" @click="navigateTo(`/articles/${article.id}`)">
  {{ article.title }}
</div>
```

### 注意事项

- `<NuxtLink>` 渲染为 `<a>` 标签，需要加 `text-decoration: none`
- 如需保存状态（如滚动位置），可在 `@click` 中用 `history.replaceState`，跳转交给 `NuxtLink`

---

## 时间显示规范（强制）

### 1. 统一使用公共方法

**禁止**在组件中重复定义时间格式化函数，必须使用 `~/utils/formatTime.ts` 中的公共方法：

| 方法 | 用途 |
|------|------|
| `formatTime(dateStr)` | 相对时间显示（"刚刚"、"3分钟前"、"2天前"） |
| `getFullDateTime(dateStr)` | 完整时间格式（"2024-04-08 14:30:25"），用于 title 属性 |

### 2. 相对时间必须添加 title 属性

```vue
<!-- ✅ 正确 -->
<time :title="getFullDateTime(comment.createTime)">
  {{ formatTime(comment.createTime) }}
</time>
```

---

## 文章列表统计展示（强制）

文章卡片中的浏览、点赞、评论等统计数据，**无论是否为 0 都必须展示**，保持图标和布局一致。

```vue
<!-- ✅ 正确：始终展示图标和数值 -->
<span>
  <n-icon><Eye /></n-icon>
  {{ article.numberTimes || 0 }}
</span>

<!-- ❌ 错误：为 0 时隐藏，导致布局不协调 -->
<span v-if="article.numberTimes">
  <n-icon><Eye /></n-icon>
  {{ article.numberTimes }}
</span>
```

---

## Naive UI Transfer（穿梭框）组件使用规范

### 1. 属性使用

| 属性 | 用途 | 错误用法 |
|------|------|----------|
| `source-title` | 左侧面板标题 | ❌ `render-source-label` |
| `target-title` | 右侧面板标题 | ❌ `render-target-label` |
| `source-filterable` | 左侧可搜索 | - |
| `target-filterable` | 右侧可搜索 | - |

**错误示例**：
```vue
<!-- ❌ 错误：render-source-label 用于渲染每个选项，不是面板标题 -->
<n-transfer
  :render-source-label="() => `我的文章（54篇）`"
/>
```

**正确示例**：
```vue
<!-- ✅ 正确：使用 source-title 设置面板标题 -->
<n-transfer
  v-model:value="selectedIds"
  :options="options"
  source-filterable
  target-filterable
/>
```

### 2. 自定义渲染

**禁止**使用自定义 slot（如 `#source`、`#target`），Naive UI Transfer 不支持。

**正确做法**：
- 选项使用简单的 `{ label: string; value: number | string }` 格式
- 如需自定义渲染，使用 `render-source-label` / `render-target-label` 函数（接收 option 参数）

### 3. 高度设置

**禁止**使用百分比或 flex 高度，可能导致内容不显示。

**正确做法**：使用固定像素高度
```css
/* ✅ 正确 */
.transfer-wrapper :deep(.n-transfer) {
  height: 800px;
}

/* ❌ 错误 - 可能导致内容不显示 */
.transfer-wrapper :deep(.n-transfer) {
  height: 100%;
  flex: 1;
}
```

### 4. 开发流程

**正确流程**：
1. 先用最简代码实现功能（不加自定义样式）
2. 确认功能正常后，再逐步添加样式
3. 一次只改一个问题

**错误流程**：
- ❌ 一开始就写复杂 CSS + 多个功能
- ❌ 功能不正常时就加样式补丁

---

## API 错误处理规范（强制）

**`useApi` 已统一拦截错误并弹出后端 `msg`，页面禁止重复写 `message.error`。**

### ✅ 正确做法

```typescript
// 只需要 try/catch 做流程控制（关 loading 等）
const saving = ref(false)
const handleSave = async () => {
  saving.value = true
  try {
    await api.saveData(data)
    message.success('保存成功')  // 成功提示可以写
  } catch {} finally {
    saving.value = false
  }
}

// 如果 catch 里没有任何逻辑，整个 try/catch 可以省略
const handleDelete = async () => {
  await api.deleteItem(id)  // 失败时 useApi 自动弹错误
  message.success('删除成功')
  await refresh()
}
```

### ❌ 错误做法

```typescript
// ❌ 禁止：重复弹出错误（useApi 已经弹了）
} catch (e: any) {
  message.error(e.statusMessage || e.message || '保存失败')
}

// ❌ 禁止：硬编码错误消息（后端 msg 更准确）
} catch {
  message.error('获取列表失败')
}
```

### 例外（需要自己处理错误）

| 场景 | 原因 |
|------|------|
| 剪贴板操作 `navigator.clipboard` | 不走 useApi |
| 文件上传 `useOssApi`（XMLHttpRequest） | 不走 useApi |
| 前端表单校验 `message.warning` | 不是 API 错误 |

---

## 移动端页面间距覆盖规范（强制）

**布局层默认移动端 `padding-top: 260px`**（为首页轮播图+tab栏预留），部分页面需要不同间距。

### 覆盖方式：body class + CSS

```typescript
// 1. 用 useHead 给 body 加页面专属 class
useHead({
  bodyAttrs: {
    class: 'page-xxx'
  }
})
```

```css
/* 2. 非 scoped 样式中覆盖布局 padding */
@media (max-width: 768px) {
  body.page-xxx .home-main {
    padding-top: 170px !important;
  }
}
```

### 已有页面间距

| 页面 | body class | padding-top | 说明 |
|------|-----------|-------------|------|
| 首页 | 无（默认） | 260px | 有轮播图+tab栏 |
| 阅读随笔 | `page-reading-essays` | 170px | 有分类导航栏 |
| 工具 | `page-tool` | 160px | 有二级分类菜单 |
| 文章归档 | `page-article-archive` | 70px | 无额外顶部元素 |
| 友链 | `page-friend-link` | 70px | 无额外顶部元素 |
| 标签 | `page-label` | 70px | 无额外顶部元素 |
| 关于/关于作者 | `page-regarding` | 70px | 无额外顶部元素 |

### 禁止

- ❌ 用 JS 直接操作父布局 DOM（`document.querySelector('.home-main').style.paddingTop`）
- ❌ 在 scoped 样式中尝试覆盖（无法选中父元素）

---

## Scoped CSS 类名冲突（强制）

**Nuxt SSR 下 scoped CSS 的注入顺序不稳定，不同组件使用相同类名会互相覆盖。**

### 根因

Vue scoped CSS 通过给元素添加 `data-v-xxx` 属性实现隔离，但 Nuxt SSR 在构建时合并所有组件的 CSS，注入顺序取决于组件加载顺序（路由、动态导入等）。当两个组件使用相同类名但样式不同时，**后注入的会覆盖先注入的**，导致样式随机错乱。

### 禁止

❌ 多个组件共用一个通用类名（如 `stat-item`、`card-item`、`meta-info`），各自定义不同样式：

```vue
<!-- ❌ 组件 A：水平排列 -->
<style scoped>
.stat-item { display: flex; align-items: center; }
</style>

<!-- ❌ 组件 B：垂直排列 -->
<style scoped>
.stat-item { display: flex; flex-direction: column; }
</style>
```

两个组件的 `.stat-item` 会互相覆盖，哪个生效取决于路由和加载顺序。

### 正确做法

✅ 每个组件使用**带组件前缀的唯一类名**：

```vue
<!-- ✅ 组件 A -->
<span class="article-stat">
  <n-icon><Eye /></n-icon>
  {{ article.views }}
</span>

<!-- ✅ 组件 B -->
<div class="popover-stat">
  <span class="stat-num">{{ count }}</span>
  <span class="stat-label">文章</span>
</div>

<!-- ✅ 组件 C -->
<div class="sidebar-stat">
  <span class="stat-num">{{ count }}</span>
</div>
```

### 命名规范

| 模式 | 格式 | 示例 |
|------|------|------|
| 组件名 + 用途 | `{component}-{purpose}` | `article-stat`、`sidebar-stat` |
| 页面名 + 用途 | `{page}-{purpose}` | `admin-stat`、`profile-stat` |
| 弹窗/浮层 + 用途 | `{context}-{purpose}` | `popover-stat` |

### 例外

以下情况可以安全使用同名类：

1. **全局样式**：`main.css` 中定义的类，所有组件共用同一套样式（如 `.article-stats`）
2. **Tailwind 工具类**：如 `flex`、`gap-4`，本身就是单一样式

### 检查清单

新增组件或修改样式时：

- [ ] 是否使用了其他组件已有的类名？（用 Grep 搜索确认）
- [ ] 样式定义是否和全局 `main.css` 冲突？
- [ ] 类名是否带组件/页面前缀？
- [ ] 刷新页面后样式是否正常？（SSR 注入顺序可能和客户端路由不同）

---

**核心原则**：所有新代码必须支持双主题，禁止硬编码颜色，禁止瞎编接口字段名。
