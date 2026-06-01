# Markdown 渲染系统

文章和时光小记的内容以 Markdown 格式存储，需要转换为 HTML 展示。本文档详细说明渲染管线的架构设计。

---

## 架构总览

```
                           SSR 阶段                              客户端阶段
                    ┌──────────────────┐                  ┌──────────────────────┐
 API 返回           │                  │                  │                      │
 原始 Markdown ───► │  useMarkdown()   │ ──► HTML ──────► │  MarkdownRenderer    │
                    │  渲染管线         │     字符串        │  客户端 Hydrate       │
                    │                  │                  │                      │
                    └──────────────────┘                  └──────────────────────┘
```

**核心设计**：Markdown → HTML 的转换在 SSR 阶段完成，保证搜索引擎能抓取到完整的文章内容。客户端只做交互增强（图片预览、代码复制、视频嵌入等）。

### 涉及文件

| 文件 | 职责 |
|------|------|
| `composables/useMarkdown.ts` | 渲染管线：预处理 → Unified 插件链 → HTML 输出 |
| `components/MarkdownRenderer.vue` | 展示组件：接收 HTML，客户端做交互增强 |
| `pages/articles/[id].vue` | 文章详情页：SSR 阶段调用 renderMarkdown |
| `pages/time-notes/[id].vue` | 时光小记详情页：同上 |
| `components/article/ArticleDetail.vue` | 文章展示组件：传递 HTML 给 MarkdownRenderer |
| `components/timeNotes/TimeNotesDetailContent.vue` | 小记展示组件：同上 |
| `components/article/ArticleToc.client.vue` | 文章目录：从渲染后的 DOM 中提取标题 |
| `assets/css/main.css` | 全局样式：`.markdown-body` 下的所有排版样式 |

---

## 1. 渲染管线（useMarkdown.ts）

### 导出 API

```typescript
const { renderMarkdown } = useMarkdown()
const html: string = await renderMarkdown(markdownString)
```

`renderMarkdown` 是一个纯函数：输入 Markdown 字符串，输出 HTML 字符串。不依赖 DOM，可在 Node.js（SSR）和浏览器中运行。

### 数据流

```
原始 Markdown
    │
    ▼
preprocessSuperSub()     ← ^上标^ → <sup>，~下标~ → <sub>
    │
    ▼
preprocessAdmonition()   ← !!! type ... !!! 前后插入空行
    │
    ▼
unified() 管线（15 个插件，按顺序执行）
    │
    ▼
HTML 字符串
```

### 1.1 预处理阶段（字符串级别）

在 `remarkParse` 之前执行，处理 remark 无法直接支持的语法。

#### preprocessSuperSub

```typescript
// 输入
"H~2~O 是水的化学式，E=mc^2^ 是质能方程"

// 输出
"H<sub>2</sub>O 是水的化学式，E=mc<sup>2</sup> 是质能方程"
```

**为什么需要预处理**：`remarkGfm` 会把 `~text~` 解析为删除线（`<del>`）。在 remark 解析之前把 `~text~` 转为 `<sub>` HTML，可以绕过这个冲突。

- `^text^` → `<sup>text</sup>`
- `~text~`（单波浪）→ `<sub>text</sub>`，`~~text~~`（双波浪）不受影响
- 跳过代码块（` ``` ` 围栏）和行内代码（`` `code` ``）中的内容
- 内容经过 HTML 转义（`escapeHtml`）

#### preprocessAdmonition

```typescript
// 输入（!!! 前后可能没有空行）
"!!! note\n内容\n!!!"

// 输出（确保 !!! 是独立段落）
"\n!!! note\n\n内容\n\n!!!\n"
```

确保 `!!! type` 和闭合 `!!!` 前后有空行，使它们成为独立的 Markdown 段落，否则 remark 解析时会与其他内容混在一起。

### 1.2 Unified 管线（15 个插件）

```
remarkParse              ← Markdown → mdast（抽象语法树）
    │
remarkGfm                ← GFM 扩展：表格、删除线、任务列表、自动链接
    │
remarkMath               ← 数学公式标记：$x^2$、$$x^2$$
    │
remarkBlockquoteLineBreaks ← 引用块内软换行 → 硬换行 <br>
    │
remarkVideo              ← @[video](url) + 视频链接 → 占位符 <div>
    │
remarkDiagram            ← mermaid/echarts 代码块 → 占位符 <div>
    │
remarkRehype             ← mdast → hast（HTML 抽象语法树）
    │                       allowDangerousHtml: true（保留原始 HTML）
rehypeKatex              ← 数学公式 → KaTeX HTML
    │
rehypeSlug               ← 标题加 id 属性（TOC 锚点）
    │
rehypeRaw                ← 解析原始 HTML（<sup>、<sub>、视频占位符等）
    │
rehypeSanitize           ← 白名单过滤（防 XSS，保留自定义属性）
    │
rehypeAdmonition         ← !!! type ... !!! → <div class="admonition">
    │
rehypeLanguageAlias      ← vue→html、jsx→javascript 等语言映射
    │
rehypeHighlight          ← highlight.js 语法高亮（atom-one-dark 主题）
    │
rehypeStringify          ← hast → HTML 字符串
```

### 1.3 自定义插件详解

#### remarkVideo

识别两种视频语法：

**显式语法**：`@[video](url)` 或 `@[video title="标题" poster="封面"](url)`

**隐式语法**：独立一行的链接，匹配视频 URL

| 平台 | URL 模式 | 转换为 |
|------|---------|--------|
| 本地 | `.mp4/.webm/.ogg/.mov` | `<div class="video-embed" data-video-type="local" data-video-url="...">` |
| B站 | `bilibili.com/video/BVxxx` | `<div class="video-embed" data-video-type="bilibili" data-video-id="BVxxx">` |
| YouTube | `youtube.com/watch?v=xxx` | `<div class="video-embed" data-video-type="youtube" data-video-id="xxx">` |

渲染为占位符 `<div>`，客户端 hydrate 时替换为真实的 `<video>` 或 `<iframe>`。

#### remarkDiagram

将 ` ```mermaid ` 和 ` ```echarts ` 代码块替换为占位符：

```html
<!-- mermaid -->
<div class="mermaid-placeholder" data-mermaid-source="编码后的源码"></div>

<!-- echarts -->
<div class="echarts-placeholder" data-echarts-option="编码后的配置"></div>
```

这样做的目的是避免这些代码块被 `rehypeHighlight` 当作普通代码高亮。客户端 hydrate 时用 mermaid/echarts 库渲染。

#### remarkBlockquoteLineBreaks

解决引用块内软换行被忽略的问题。GFM 默认把引用块内的换行合并为一行，但用户期望每行独立显示：

```markdown
> 第一行
> 第二行
> 第三行
```

插件把段落内的 `\n` 转为 `<br>` break 节点。

#### rehypeAdmonition

处理 `!!! type "title" ... !!!` 提示框语法：

```markdown
!!! note "自定义标题"
内容段落
!!!
```

转换为：

```html
<div class="admonition admonition-note">
  <p class="admonition-title">自定义标题</p>
  <p>内容段落</p>
</div>
```

支持 16 种类型：

| 类型 | 中文默认标题 | 类型 | 中文默认标题 |
|------|-------------|------|-------------|
| note | 笔记 | tip | 提示 |
| abstract | 摘要 | success | 成功 |
| info | 信息 | question | 问题 |
| warning | 警告 | failure | 失败 |
| danger | 危险 | bug | Bug |
| example | 示例 | quote | 引用 |
| hint | 提示 | caution | 注意 |
| error | 错误 | attention | 注意 |

如果 `!!!` 后面没有跟标题，则不渲染 `<p class="admonition-title">`。

#### rehypeLanguageAlias + rehypeHighlight

`rehypeLanguageAlias` 把框架语言名映射到 highlight.js 支持的语言：

| 源语言 | 映射到 | 保留原始名 |
|--------|--------|-----------|
| vue | html | `data-language="vue"` |
| svelte | html | `data-language="svelte"` |
| jsx | javascript | `data-language="jsx"` |
| tsx | typescript | `data-language="tsx"` |
| astro | html | `data-language="astro"` |

`rehypeHighlight` 使用 atom-one-dark 主题做语法高亮。

#### rehypeSanitize（自定义白名单）

基于 `rehype-sanitize` 的白名单机制，防止 XSS。白名单包含：

**允许的标签**：`div, span, p, br, hr, h1-h6, ul, ol, li, blockquote, pre, code, strong, em, del, ins, a, img, input, table系列, video, source, iframe, figure, figcaption, u, sup, sub`

**允许的属性**（全局）：`className, class, style, id, data-video-*, data-mermaid-source, data-echarts-option`

**特殊标签属性**：
- `a`：`href, title, target, rel`
- `img`：`src, alt, title, width, height, loading`
- `input`：`type, disabled, checked`（任务列表复选框）
- `code`：`className, data-language`
- `iframe`：`src, title, allow, allowfullscreen` 等

---

## 2. SSR 渲染流程

### 文章详情页（pages/articles/[id].vue）

```typescript
const { renderMarkdown } = useMarkdown()

const { data: article } = await useAsyncData(
  `article-${articleId.value}`,
  async () => {
    // 1. 从 API 获取原始数据（articleContent 是 Markdown）
    const data = await articleApi.getArticleDetail(articleId.value)
    // 2. SSR 阶段渲染 Markdown → HTML
    if (data?.articleContent) {
      data._renderedHtml = await renderMarkdown(data.articleContent)
    }
    return data
  }
)

// 3. 计算属性提取 HTML
const articleHtml = computed(() => article.value?._renderedHtml || '')
```

传递给组件：

```html
<ArticleDetail :article="article" :article-html="articleHtml" />
```

### 时光小记详情页（pages/time-notes/[id].vue）

完全相同的模式，字段名从 `articleContent` 变为 `content`。

### 为什么在 useAsyncData 中渲染

`useAsyncData` 的回调函数在 SSR 和 CSR 两个阶段都会执行。把 `renderMarkdown` 放在回调里，保证：

1. **SSR 输出完整 HTML**：搜索引擎爬虫直接看到渲染后的内容
2. **CSR 水合时数据一致**：客户端拿到相同的 HTML，避免水合不匹配
3. **HTML 缓存在 payload 中**：Nuxt 会把 `useAsyncData` 的结果序列化到页面 payload，客户端不需要重新渲染

---

## 3. MarkdownRenderer 组件

### Props

```typescript
interface Props {
  html: string  // SSR 阶段渲染好的 HTML 字符串
}
```

### SSR 阶段

组件在 SSR 时直接通过 `v-html="html"` 输出 HTML。搜索引擎看到的是完整的文章内容。

### 客户端 Hydrate

`onMounted` 时执行 `hydrate()` 函数，依次调用 6 个增强函数：

#### bindLinkTargets()

给所有 `<a>` 标签添加 `target="_blank"` 和 `rel="noopener noreferrer"`，让外部链接在新标签页打开。

#### bindImageClickEvents()

给所有 `<img>` 添加点击事件，打开 `ImagePreview` 组件（全屏图片预览）。使用 `WeakMap` 防止重复绑定。

ImagePreview 功能：缩放（滚轮/双指捏合）、拖拽、旋转、双击重置、ESC 关闭、body 滚动锁定。

#### hydrateVideoEmbeds()

查找所有 `.video-embed` 占位符，替换为真实播放器：

| data-video-type | 替换为 |
|----------------|--------|
| `local` | `<video src="..." controls>` |
| `bilibili` | `<iframe src="player.bilibili.com/...">` |
| `youtube` | `<iframe src="youtube.com/embed/...">` |

跳过已 hydrate 的元素（检查是否有 `video, iframe` 子元素）。

#### enhanceCodeBlocks()

给每个 `<pre>` 代码块添加头部：

```
┌─────────────────────────────────────┐
│ ● ● ●                  javascript 复制 │  ← code-block-header
├─────────────────────────────────────┤
│ const x = 1                         │  ← 原始代码
│ console.log(x)                      │
└─────────────────────────────────────┘
```

- 三色圆点（macOS 风格：红/黄/绿）
- 语言标签（优先读取 `data-language`，其次 `language-*` class）
- 复制按钮（`navigator.clipboard.writeText`，2 秒内显示"已复制"）

#### hydrateMermaidDiagrams()

1. 查找 `.mermaid-placeholder` 元素，没有则直接返回（不加载 mermaid 库）
2. 动态 `import('mermaid')`（懒加载，~150KB gzip）
3. 初始化：`mermaid.initialize({ startOnLoad: false, securityLevel: 'strict' })`
4. 遍历每个占位符：解码 `data-mermaid-source` → `mermaid.render()` → 注入 SVG

#### hydrateEchartsDiagrams()

1. 查找 `.echarts-placeholder`，没有则返回
2. 动态 `import('echarts')`（懒加载）
3. 解码 `data-echarts-option` → `new Function('return (' + raw + ')')()` 解析配置
4. 创建 400px 高的容器 → `echarts.init()` → `chart.setOption(option)`
5. `ResizeObserver` 响应容器尺寸变化

### 事件通知

所有 hydrate 完成后派发自定义事件：

```typescript
window.dispatchEvent(new CustomEvent('markdown-rendered'))
```

`ArticleToc.client.vue` 监听此事件来更新目录。

### HTML 变化时重新 Hydrate

```typescript
watch(() => props.html, () => {
  if (import.meta.client) hydrate()
})
```

---

## 4. 文章目录（ArticleToc.client.vue）

> `.client.vue` 组件，只在客户端运行。

### Props

```typescript
interface Props {
  content?: string  // 渲染后的 HTML 字符串
}
```

### 标题提取

两种策略，按优先级尝试：

**1. 从 DOM 提取（首选）**

```typescript
const elements = container.querySelectorAll('h2[id], h3[id], h4[id]')
```

直接从 `.markdown-body` 容器中查询带 `id` 的标题元素。保证 ID 与实际渲染一致。

**2. 从 HTML 字符串解析（备选）**

```typescript
const regex = /<h([2-4])[^>]*id="([^"]*)"[^>]*>(.*?)<\/h\1>/g
```

当 DOM 还未渲染时使用（例如 SSR 输出到达但 Vue 尚未 hydrate）。

### 更新触发时机

| 触发方式 | 说明 |
|---------|------|
| `watch(props.content)` | HTML 内容变化时，`nextTick` 后更新 |
| `onMounted` | 组件挂载后立即更新 |
| `markdown-rendered` 事件 | MarkdownRenderer hydrate 完成后 |
| `scroll` 事件 | 持续更新当前高亮的标题 |

### 滚动跟踪

监听 `window.scroll` 事件，计算当前可见的标题（`offsetTop <= scrollTop + 100`），更新 `activeId`。

点击目录项时，平滑滚动到对应标题，预留 80px 导航栏偏移。

---

## 5. 样式系统

所有 Markdown 样式定义在 `assets/css/main.css` 中，以 `.markdown-body` 为根选择器。全局生效，非 scoped。

### 设计 Token

| 变量 | 浅色 | 深色 |
|------|------|------|
| `--md-code-bg` | `#eef0f2` | `#17171b` |
| `--md-code-border` | `#e2e4e8` | `#38383e` |
| `--md-inline-code-bg` | `rgba(61,90,128,0.08)` | `rgba(90,127,168,0.2)` |
| `--md-quote-border` | `#35B378` | `#35B378` |
| `--md-link-color` | `#3b82f6` | `#60a5fa` |

### 排版

| 元素 | 大小 | 特殊样式 |
|------|------|---------|
| h1 | 28px | 底部边框 |
| h2 | 22px | 底部边框 |
| h3 | 19px | — |
| h4 | 17px | — |
| p | — | `color: var(--color-ink)`，底部间距 16px |
| a | — | `color: var(--md-link-color)`，hover 下划线 |

### 代码块

```
┌─ code-block-header ──────────────────────────────┐
│ 背景 #21252b，底部边框                              │
│ ● ● ●  (红 #ff5f57 / 黄 #febc2e / 绿 #28c840)     │
│                                           javascript │
│                                              [复制]  │
├───────────────────────────────────────────────────┤
│ 背景 #282c34，文字 #abb2bf                          │
│ padding: 16px 20px，字号 13px，行高 1.6              │
└───────────────────────────────────────────────────┘
```

行内代码：`font-family: var(--font-mono)`，`background: var(--md-inline-code-bg)`，蓝色文字。

### 引用块

```css
.markdown-body blockquote {
  padding: 8px 16px;
  border-left: 4px solid var(--md-quote-border);  /* 绿色 */
  background: var(--color-surface-dim);
  border-radius: 0 4px 4px 0;
}
```

### 提示框（Admonition）

```css
.markdown-body .admonition {
  padding: 16px;
  margin: 16px 0;
  border-radius: 8px;
  border-left: 4px solid;  /* 颜色因类型而异 */
}
```

16 种类型的颜色映射（6 组）：

| 组 | 类型 | 左边框颜色 | 背景 |
|----|------|-----------|------|
| 蓝色 | note | `#448aff` | `rgba(68,138,255,0.08)` |
| 浅蓝 | abstract, info | `#00b0ff` / `#29b6f6` | 对应 8% 透明度 |
| 绿色 | tip, hint, success | `#00c853` / `#00e676` | 对应 8% 透明度 |
| 橙色 | question, warning, caution, attention | `#ff9100` | `rgba(255,145,0,0.08)` |
| 红色 | failure, bug, error, danger | `#ff5252` / `#ff1744` | 对应 8% 透明度 |
| 紫色 | example | `#7c4dff` | `rgba(124,77,255,0.08)` |
| 灰色 | quote | `#9e9e9e` | `rgba(158,158,158,0.08)` |

### 任务列表

- `.task-list-item`：`list-style-type: none !important`（去掉圆点）
- 复选框：`appearance: none`，16x16px
  - 未选中：灰色边框
  - 已选中：绿色背景 `#00c853`，SVG 勾选图标

### 图表占位符

- `.mermaid-placeholder`：居中，24px 内边距，`var(--color-surface-dim)` 背景
- `.echarts-placeholder`：无内边距，同上背景，`overflow: hidden`

### 表格

- `display: block`，`overflow-x: auto`（支持横向滚动）
- 表头：`font-weight: 600`，`background: var(--color-surface-dim)`
- 斑马纹：`tr:nth-child(2n)` 交替背景色
- 含图片的表格：去掉斑马纹，增加单元格内边距

### 图片

- `max-width: 100%`，`max-height: 800px`
- `display: block`，`margin: 16px auto`（居中）
- `border-radius: 8px`

---

## 6. 支持的特殊语法汇总

| 语法 | 效果 | 处理阶段 |
|------|------|---------|
| `**粗体**` | **粗体** | remarkGfm |
| `*斜体*` | *斜体* | remarkGfm |
| `~~删除线~~` | ~~删除线~~ | remarkGfm |
| `` `行内代码` `` | `行内代码` | remarkParse |
| `^上标^` | 上标 | 预处理 preprocessSuperSub |
| `~下标~` | 下标 | 预处理 preprocessSuperSub |
| `[链接](url)` | 超链接 | remarkParse |
| `![图片](url)` | 图片 | remarkParse |
| `> 引用` | 引用块 | remarkParse |
| `- [x] 任务` | 任务列表 | remarkGfm |
| `$x^2$` | 行内公式 | remarkMath + rehypeKatex |
| `$$x^2$$` | 块级公式 | remarkMath + rehypeKatex |
| `` ```mermaid `` | Mermaid 图表 | remarkDiagram + 客户端 hydrate |
| `` ```echarts `` | ECharts 图表 | remarkDiagram + 客户端 hydrate |
| `@[video](url)` | 视频嵌入 | remarkVideo + 客户端 hydrate |
| `!!! note "标题"` | 提示框 | 预处理 + rehypeAdmonition |

---

## 7. 新增语法/插件的步骤

以新增一个 remark 插件为例：

### 1. 在 useMarkdown.ts 中编写插件

```typescript
const remarkMyPlugin: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'code', (node: any) => {
      // 处理逻辑
    })
  }
}
```

### 2. 插入到管线中

注意插入位置：
- **remark 插件**（操作 mdast）：在 `remarkRehype` 之前
- **rehype 插件**（操作 hast）：在 `rehypeSanitize` 之后、`rehypeStringify` 之前

### 3. 更新 rehypeSanitize 白名单

如果插件引入了新的 HTML 标签或属性，需要在 `videoSanitizeSchema` 中添加。

### 4. 客户端 Hydrate（如果需要 DOM 操作）

在 `MarkdownRenderer.vue` 的 `hydrate()` 函数中添加增强逻辑。

### 5. 添加 CSS 样式

在 `assets/css/main.css` 的 `.markdown-body` 下添加样式。

---

## 8. 排错指南

| 问题 | 可能原因 | 排查方法 |
|------|---------|---------|
| 自定义语法不渲染 | rehypeSanitize 白名单缺少标签/属性 | 检查 `videoSanitizeSchema` |
| 提示框内容异常 | `!!!` 前后没有空行 | preprocessAdmonition 应该已处理 |
| 代码高亮不生效 | 语言名不在 highlight.js 支持范围 | 检查 rehypeLanguageAlias 映射 |
| Mermaid 图表空白 | mermaid 语法错误 | 查看控制台错误信息 |
| SSR 输出仍是 Markdown | 页面没有在 useAsyncData 中调用 renderMarkdown | 检查页面的数据获取逻辑 |
| TOC 标题缺失 | `rehypeSlug` 生成的 id 与实际不一致 | 检查 DOM 中 h2/h3 的 id 属性 |
| 任务列表有圆点 | `list-style-type: none !important` 未覆盖 | 检查 CSS 选择器优先级 |
