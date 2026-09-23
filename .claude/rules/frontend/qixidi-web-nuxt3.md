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

## useAsyncData SSR 失败兜底的缓存陷阱（强制，2026-09-20 生产导航丢失事故）

**SSR 可能失败的 useAsyncData，handler 失败必须返回 `null`，且禁止配 `default` 选项。**

### 事故链（Nuxt 3.21.2 源码验证）

| 步骤 | 发生了什么 |
|------|-----------|
| 1 | SSR fetch 失败被 handler 内 try/catch 吞掉，返回 `[]`（空数组非 null）→ `payload.data[key] = []` |
| 2 | 客户端水合期间（isHydrating），`execute` 先查缓存：默认 `getCachedData` 无条件返回 `payload.data[key]`；`[] != null` 为真 → 短路返回"成功"，handler 永不执行 |
| 3 | onMounted 里的 `refresh()` 兜底走同一条 execute 路径 → 同样被拦 → **静默失效，零报错** |
| 4 | 症状：刷新后导航/菜单缺失永不自愈；从其他页面客户端路由切过来（内存数据还在）正常，再刷新又丢 |

### 修复模式（layouts/default.vue、layouts/user-home.vue）

```typescript
// ✅ 正确：失败返回 null + 无 default 选项
const { data } = await useAsyncData('key', async () => {
  try {
    const { rows } = await api.getList()
    return rows || null
  } catch (e) {
    return null
  }
})
const list = computed(() => data.value || [])  // 消费端兜底渲染

// ❌ 错误：返回 [] 会被当有效缓存拦截客户端重取
// ❌ 错误：配 default: () => [] —— default 会让 data.value 非 null，
//    连 null 路径也走不进"无缓存"分支（源码 line 114 的 data.value != null 判断）
```

### 机制要点

- `null` + 无 `default` → 水合时 data.value 为 `undefined`（`undefined != null` 为 false）→ 不拦截 → **onBeforeMount 自动重新请求**，比 onMounted 兜底更早
- SSR 成功有真数据时零额外请求（payload 有数据直接用缓存），正常路径不受影响
- dev 模式 SSR 返回 null 会打 `must return a value...may be duplicated on the client side` 警告——**这是预期提示**（客户端会重取），不是错误，勿"修复"掉

### 排查方法论（本次沉淀）

- **payload 里某 key 是 `[]` 而非真数据** = "SSR 失败被 try/catch 吞掉"的签名（生产取证：view-source 搜 key 名）
- 判定页面自身是否发过某请求用 `performance.getEntriesByType('resource')`；但 **pending 中的请求不进 resource timing**——"timing 里没有"要区分"没发"（缓存拦截）和"挂着"（代理/后端死了），二者根因不同
- 网络监听器数组（`page.on('response')` 收集的）是引用，延迟打印会混入之后 evaluate 里的手动 fetch，不可作判据

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

**图标名以 Tabler 官方名为准，不能凭记忆写**（2026-09-18 实例：写了 antd 风格的 `CircleFilled`，@vicons/tabler 无此导出，运行时整页崩 `does not provide an export named`）。实心圆点用 `Point`。拿不准先查：`ls node_modules/@vicons/tabler/es/ | grep -i 关键词`

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
| **组件自动导入前缀** | `components/` 子目录的组件名带目录前缀：`common/MdEditorWithVideo.vue` → `<CommonMdEditorWithVideo>`（写 `<MdEditorWithVideo>` 会静默渲染失败，先 grep 使用先例确认组件名） |

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

## Naive UI n-tabs 行内占位（2026-09-18）

`n-tabs` 根元素默认占满整行（width:100%），放进 `flex-wrap: wrap` 的 flex 行内会**独自占满一行、把其余元素全部挤到第二行**（症状：同行元素 top 值差 ~46px，看似"莫名换行"）。与其他元素并排时必须收回：

```css
.list-header :deep(.n-tabs) {
  width: auto;
  flex-shrink: 0;
}
```

`size="small"` 只缩小 tab 内部尺寸，解决不了占满整行的问题，需配合上面的 width 修复。

---

## grid 双栏等宽 + 内容省略号（强制）

并排卡片列一律写 `grid-template-columns: minmax(0, 1fr) minmax(0, 1fr)`，**不能只写 `1fr 1fr`**：grid 子项默认 `min-width: auto`，且 `text-overflow: ellipsis` 不参与 min-content 计算——子项里 nowrap 长文本会把本列撑开、挤压遮盖相邻列（即使组件内部省略号样式全对也拦不住）。`minmax(0, 1fr)` 让列可收缩，内部 ellipsis 才能生效；截断的文本补 `:title` 展示完整内容。

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

## Nuxt dev 与 build 互斥（强制）

**dev server 运行期间，禁止执行 `nuxi build`。**

两者共用 `.nuxt` 目录，同时运行会互相污染：

| 方向 | 后果 |
|------|------|
| build 重写 `.nuxt` 生成文件 | dev server 内存模块图与磁盘不一致 → HMR 失效，**新增组件不被识别**（页面看不到新组件，也无报错） |
| build 重写 `.nuxt` 生成文件 | dev 启动报 `Pre-transform error: Failed to resolve import "#app-manifest"`（死代码分支的虚拟模块解析失败，2026-09-23 实例） |
| dev 占用中的 `.nuxt` 被 build 读取 | 产出被污染的 `.output`（混入 vite-node 代码）→ 启动报 `Vite Node IPC socket path not configured` |

**症状识别**：
- dev 运行中新组件/新页面始终不生效 → 先确认是否在 dev 运行时跑过 build
- build 产物启动报 Vite Node IPC 错误 → 产物已被污染，必须重新 build

**修复**：停 dev → 删除 `.nuxt` 和 `.output` → 重启 dev（或重新 build）

**最常见触发方式：测试完不关 dev**（长驻实例挂着，之后打包必污染）——测试实例用完必须立即关闭，见 code-basics「测试实例管理」。2026-09-23 生产整站 500 实例：dev 挂着运行 32 分钟后执行 build，`.output/server/chunks/build/server.mjs` 混入 `NUXT_VITE_NODE_OPTIONS`，部署后 Nitro 启动即挂。

**同族坑：先改引用、后建组件文件 → transform 缓存陈旧（2026-09-15）**

在已有组件中引用一个**尚不存在**的新组件（Edit 引用 → 再 Write 新组件文件），Vite 会在文件还不存在时立即 transform 引用方，组件名无法解析时回退为 `_resolveComponent` 运行时解析。Nuxt 3.2x **没有运行时全局注册表**，解析必然静默失败——症状是「点击按钮无反应」（ref 正常变化，但组件渲染成未知元素，仅控制台有 Failed to resolve component 警告）。之后即使组件被扫描注册（components.d.ts 已更新），引用方内容未变就不会重新 transform，坏缓存一直被 serve。

- **诊断**：`curl http://127.0.0.1:9007/_nuxt/components/xxx/引用方.vue | grep "新组件"` —— 若是 `_resolveComponent("Xxx")` 而非 `import ... from ".../Xxx.vue?t=..."`，即命中此坑
- **修复**：`touch` 引用方文件强制重新 transform（无需重启 dev）
- **预防**：**先 Write 新组件文件，再 Edit 引用方**，顺序不要反

**验证代码的正确姿势**：
- 开发期验证：dev server + 浏览器（改动热更新即可见）
- 需要 build 验证编译：**先停 dev**，再 build，验证完重启 dev

---

## md-editor-v3 插入内容到光标位置（强制）

编辑器内编程式插入内容（图片/视频/表格等），禁止 `content.value += text`（永远追加末尾）。

```ts
// md-editor-v3 的 insert 依赖 textarea selection，从未聚焦时 selectionStart = 0，会错误地插到【开头】
// 必须先用 @onFocus 标记"放置过光标"，未聚焦过才追加末尾
const editorFocusedOnce = ref(false)  // 模板绑定 @onFocus="editorFocusedOnce = true"

const insertToEditor = (text: string) => {
  if (mdEditorRef.value && editorFocusedOnce.value) {
    mdEditorRef.value.insert(() => ({ targetValue: text, select: true }))
  } else {
    content.value += text
  }
}
```

统一入口是 `components/common/MdEditorWithVideo.vue` 的 `insertToEditor`。

---

## admin 布局滚动模型（强制，2026-09-18 分页不可见事故沉淀）

`layouts/admin.vue` 整页永不滚动（`.layout-admin{height:100vh;overflow:hidden}`），滚动责任在 `.admin-content`（现为 `overflow-x:hidden; overflow-y:auto`）。**新页面两种写法皆可，都天然正确**：

| 模式 | 写法 | 滚动位置 |
|------|------|---------|
| 自管滚动（文章管理、反馈中心等） | 根节点 `height:100%` + 列表区 `flex:1;min-height:0;overflow-y:auto` | 页面内部列表区 |
| 文档流（工作台） | 无高度约束，正常文档流 | `.admin-content` 整体 |

自管滚动的额外红利：分页放在滚动容器**外面**（列表区 flex 尾部 `flex-shrink:0`）即可常驻底部，用户不用滚到页面最底才能翻页。

**事故根因**：曾把 `.admin-content` 写成 `overflow:hidden`，文档流页面（内容超出视口）被直接裁掉——分页在页面最底部够不到、页面滚不动。一处布局级修复（`overflow-y:auto`）覆盖两页；自管滚动页面内容恰好 100% 高，不触发外层滚动条，零影响。

**新增后台页面后的必测项**：内容超出视口时（长列表）能否滚到最底部（分页/末条可见）。

---

## UI 改动必须真实浏览器验证（强制）

**curl 只能验证 HTTP 200，验证不了布局/滚动/可见性。** 布局类改动（滚动、溢出、吸顶吸底、弹窗）必须用无头浏览器做量测 + 截图验证，标准套路（脚本存 `%TEMP%\qixidi-pptr\verify.js` 可直接改造复用）：

1. **驱动**：`puppeteer-core` + 本机 Edge 无头（`C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe`）
2. **登录态构造**（admin 页必经）：
   - token 来源：Redis db1 `KEYS "Authorization:login:token:*"` 取 TTL>120 的 key 后缀（**只读，token 值不得打进日志**）
   - **注入方式必须是 `auth` cookie**：`document.cookie = 'auth=' + encodeURIComponent(JSON.stringify({token})) + '; path=/'`，注入后重载一次让 `plugins/auth-init.client.ts` 读到
   - 🔴 **不能往 localStorage 注入**：本项目 `pinia-plugin-persistedstate/nuxt`（v4）默认 storage 是 **cookie**（key=store id=`auth`），localStorage 注入永远到不了 pinia state
3. **页面导航**：admin 页用客户端导航模拟用户真实路径——`document.querySelector('#__nuxt').__vue_app__.config.globalProperties.$router.push(path)`（等价点 NuxtLink）；已登录态下直接 URL goto（刷新场景）也要测
4. **断言**：量测（`getBoundingClientRect` 在视口内、`scrollHeight > clientHeight`、`getComputedStyle().overflowY`）+ 截图双确认；`page.on('pageerror')` 收集运行时错误

**登录态断言用 pinia 内存态**（`$pinia.state.value.auth.token/user`），**禁止 `body.innerText.includes('用户名')`**——作者名会命中同名文章造成假阳性。

---

## dev server 运维要点（Windows/PowerShell）

| 坑 | 规则 |
|----|------|
| PowerShell `run_in_background` 管道式任务（`npx nuxi dev \| Out-File`）会被宿主会话回收（exit code 9、无任何日志） | 拉长驻 dev server 必须独立进程：`Start-Process cmd.exe /c "npx nuxi dev > %TEMP%\xxx.log 2>&1"` |
| **dev server 启动早于新页面文件创建**（尤其新建子目录 `pages/admin/help/`）→ 路由 watcher 失效，新页面 404 | 新建页面目录后 404，先比对 dev 启动时间 vs 文件创建时间，重启 dev server 解决 |
| Nuxt dev 路由是 Vite 虚拟模块**不落盘**，grep `.nuxt` 找路由是无效诊断 | 权威判据：浏览器里 `__vue_app__...$router.getRoutes()` 读真实路由表 |
| 未登录直接 URL 访问 `/admin/**`（ssr:false）会因路由守卫 abort 显示 404 错误页 | 属预期行为（未登录不让进）；验证 admin 页须先构造登录态 |

---

## 站点刻意设计清单（勿当缺陷上报，2026-09-18 用户确认）

| 现象 | 定性 |
|------|------|
| 时光小记用宽容器（1352px 无侧栏），其他内容页 992px | 刻意设计 |
| 用户主页无站点导航，独立布局（1200px） | 刻意设计 |
| 全站无传统 footer，页脚信息放首页侧边栏 | 刻意设计 |
| 各页卡片圆角/阴影存在差异 | 风格选择 |

UI 巡检/一致性审查先排除本清单；新发现的跨页差异先确认设计意图再下结论，判断标准是 bug 和功能障碍，不是风格差异。

---

**核心原则**：所有新代码必须支持双主题，禁止硬编码颜色，禁止瞎编接口字段名。
