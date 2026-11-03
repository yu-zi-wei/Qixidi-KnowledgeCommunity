# 前端设计系统规范

栖息地博客的前端设计系统和样式开发规范。

---

## 核心原则

**设计优先**：所有样式开发必须遵循统一的设计系统，使用设计 token，禁止硬编码样式值。

## 版面语言：一张纸（2026-11-02 定版；同日色温由宣纸暖调改为冷灰）

**整页一张冷灰纸面（`--color-page-bg` #f4f5f7 / 深色 #17171b），大面积内容容器一律透明**——不铺底色、不画边框/圆角/投影，半透明 blur 大面板已废弃（time-notes 首个迁移，其余页面照此）。层次只来自两处：

- **排版**：字号/字重对比（衬线大序号是视觉锚点，Playfair Display 自托管仅 0.1MB）、冷墨四阶浓淡（ink → ink-light → ink-muted → ink-faint）、细分隔线（hairline 是纸上的排版线，不是盒子轮廓）
- **小面积点缀**：页签方块 / 标签 pill / hover 墨色浸染（`color-mix(in srgb, var(--color-ink) 2~4%, transparent)`，双主题同式；主色描边按钮类 hover 用 `color-mix(primary 16%)`）/ 输入控件纸片底

---

## CSS 变量系统

### 颜色系统

所有颜色必须使用 CSS 变量，禁止硬编码颜色值。

```css
/* 文字颜色 */
--color-ink           /* 主要文字 */
--color-ink-light     /* 次要文字 */
--color-ink-muted     /* 辅助文字 */
--color-ink-faint     /* 占位/禁用文字 */

/* 背景颜色（中性冷灰三级纸面：纸片最浅 → 纸页居中 → 纸桌最深；warm 是历史命名，实为冷灰） */
--color-page-bg       /* 整页纸面底（浅 #f4f5f7 / 深 #17171b） */
--color-surface       /* 纸片（最浅一级） */
--color-surface-warm  /* 纸页（居中一级，名遗留 warm 实为冷灰） */
--color-surface-dim   /* 纸桌（最深一级） */
--color-paper         /* 上浮白纸片（弹层/卡片叠纸） */
--color-sunken        /* 凹陷面（输入控件底；hover 其上沾墨） */

/* 边框颜色 */
--color-border        /* 主边框 */
--color-border-light  /* 浅色边框 */

/* 功能颜色 */
--color-primary       /* 品牌色 */
--color-primary-hover /* 品牌色悬停 */
--color-primary-light /* 浅色品牌色 */
--color-accent        /* 强调色 */
--color-danger        /* 危险色 */
```

### 字体系统

```css
/* 字体族 */
--font-display        /* 标题字体（衬线；英文数字可叠 'Playfair Display'，自托管 italic 字面 ~0.1MB） */
--font-body           /* 正文字体（无衬线） */
--font-mono           /* 等宽字体 */
--font-logo           /* Logo 楷风字体（LXGW WenKai / 楷体回落） */

/* 字体大小 */
--text-2xs: 12px
--text-xs: 13px
--text-sm: 15px
--text-base: 17px
--text-lg: 18px
--text-xl: 20px
--text-2xl: 23px
--text-3xl: 29px

/* 行高 */
--leading-tight: 1.4
--leading-normal: 1.6
--leading-relaxed: 1.75
```

### 间距系统

```css
--space-1: 4px
--space-2: 8px
--space-3: 12px
--space-4: 16px
--space-5: 20px
--space-6: 24px
--space-8: 32px
```

### 圆角系统

```css
--radius-sm: 4px
--radius-md: 8px
--radius-lg: 12px
--radius-full: 9999px
```

### 阴影系统

```css
--shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.04)
--shadow-md: 0 4px 12px rgba(0, 0, 0, 0.06)
--shadow-lg: 0 8px 30px rgba(0, 0, 0, 0.08)
--shadow-float: 0 12px 40px rgba(0, 0, 0, 0.12)
```

### 动画系统

```css
--transition-fast: 0.15s ease
--transition-base: 0.2s ease
--transition-slow: 0.3s ease
```

---

## 组件样式规范

### 文章卡片

```css
/* 使用全局样式，无需重复定义 */
.article-card { /* 已在 main.css 定义 */ }

/* 组件内只需写特殊样式 */
<style scoped>
.article-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4); /* 使用间距变量 */
}

/* 特殊样式才在这里写 */
.article-card:hover {
  /* 特殊悬停效果 */
}
</style>
```

### 标签导航

```css
/* 使用全局样式 */
.tab-bar { /* 已在 main.css 定义 */ }

/* 组件内只需写特殊布局 */
<style scoped>
.tab-wrapper {
  /* 特殊布局样式 */
}
</style>
```

---

## 开发规范

### ✅ 正确做法

```css
/* 使用设计变量 */
.article-title {
  font-size: var(--text-xl);
  line-height: var(--leading-tight);
  color: var(--color-ink);
  padding: var(--space-3);
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
}
```

### ❌ 错误做法

```css
/* 硬编码值 */
.article-title {
  font-size: 18px;       /* ❌ 使用 var(--text-xl) */
  line-height: 1.4;      /* ❌ 使用 var(--leading-tight) */
  color: #1a1a1a;        /* ❌ 使用 var(--color-ink) */
  padding: 12px;         /* ❌ 使用 var(--space-3) */
  border-radius: 8px;    /* ❌ 使用 var(--radius-md) */
  transition: all 0.2s;  /* ❌ 使用 var(--transition-base) */
}
```

---

## 样式复用原则

### 1. 全局样式优先

常用组件样式已定义在 `main.css`，直接复用：
- `.article-card` - 文章卡片
- `.article-title` - 文章标题
- `.article-excerpt` - 文章摘要
- `.article-meta` - 文章元数据
- `.article-stats` - 互动数据
- `.tab-bar` - 标签导航
- `.tab-item` - 标签项
- `.bm-tab` - 页签（激活 = 浅蓝纸底 + 墨蓝字的圆角方块）
- `.bm-card` - 条目卡片（纸片底圆角方块，hover 只做墨水反应；标题加 `.bm-card-name` 联动变色）

### 2. 组件内只写特殊样式

```vue
<style scoped>
/* 只写组件特有的布局或特殊效果 */
.article-list {
  gap: var(--space-4); /* 特殊间距 */
}

/* 需要覆盖全局样式时 */
.article-card {
  /* 特殊效果 */
}
</style>
```

### 3. 避免重复定义

```vue
<!-- ❌ 错误：每个组件都定义一遍 -->
<style scoped>
.tab-item {
  padding: 6px 14px;
  font-size: 13.5px;
  /* ... */
}
</style>

<!-- ✅ 正确：使用全局样式，组件内只需特殊样式 -->
<style scoped>
.special-tab {
  /* 特殊样式 */
}
</style>
```

---

## 响应式设计

### 断点系统

使用 Tailwind 的断点：
- `sm`: 640px
- `md`: 768px
- `lg`: 1024px
- `xl`: 1280px

### 移动优先

```css
/* 默认移动端样式 */
.article-card {
  padding: var(--space-4);
}

/* 平板及以上 */
@media (min-width: 768px) {
  .article-card {
    padding: var(--space-5);
  }
}
```

---

## 双主题支持

所有新增样式必须支持浅色/深色主题：

```css
/* ✅ 正确：使用颜色变量 */
.element {
  background: var(--color-surface);
  color: var(--color-ink);
  border: 1px solid var(--color-border);
}

/* ❌ 错误：硬编码颜色 */
.element {
  background: #ffffff;
  color: #1a1a1a;
  border: 1px solid #e8e4de;
}
```

---

## 代码质量检查清单

开发新组件时，确保：

- [ ] 是否使用了设计变量（颜色、字体、间距等）？
- [ ] 是否避免了硬编码值？
- [ ] 是否复用了全局样式？
- [ ] 是否支持双主题？
- [ ] 是否使用了响应式设计？
- [ ] 是否使用了统一的动画时长？
- [ ] 是否避免了重复定义样式？

---

## 修改设计系统

如果需要修改设计系统：

1. **评估影响范围**：确认修改会影响哪些组件
2. **修改变量定义**：在 `main.css` 中修改 CSS 变量
3. **全局测试**：测试所有使用该变量的组件
4. **更新文档**：同步更新本文档

**禁止**：在组件内硬编码样式来绕过设计系统。

---

**核心原则**：统一的设计变量 + 复用的全局样式 + 最少的组件定制 = 易维护的设计系统。
