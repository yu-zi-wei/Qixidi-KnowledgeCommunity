# qixidi-web-admin 开发规范

管理后台 2.0（Nuxt 3 + TS + Naive UI，全 CSR）。需求源：`qixidi-web-admin/docs/开发需求与设计.md`。

---

## 技术底座事实

- **全 CSR**（`routeRules '/**': {ssr:false}`）：无水合问题，`localStorage`/`window` 可直用；但 useAsyncData 兜底、SSR 兼容类规则不适用本项目
- **auto-import 仅 4 个 composable**（nuxt.config 显式清单）：useDialog/useMessage/useNotification/useLoadingBar；**darkTheme/NIcon/zhCN/NTooltip 等组件与对象必须手动 import**（忘写不报编译错，静默崩）
- **主题唯一数据源在 app.vue**：provide `theme`/`switchTheme`，localStorage key=`admin-theme`；业务组件只 inject 派生，禁止自建主题状态
- **pinia persist 是 localStorage**（key=`admin`），与门户（cookie）不同
- 图标同门户规范：Tabler Icons，名字必须查 `node_modules/@vicons/tabler/es/`
- **Tailwind 工具类从未编译生效**（tailwind.scss 是 v3 `@tailwind` 语法 + postcss 配的是 v4 `@tailwindcss/postcss` 插件，样式表不生成任何工具类，且无报错）：布局样式一律 scoped CSS 手写，禁止依赖 `px-*`/`flex` 等 Tailwind 类

## API 约定

- 后台接口统一 `/system/*` 前缀（Controller 在 qixidi-system 模块）；`useApi` 语义同门户：分页 `api.postPage`（POST body 携带 BO + 分页字段，`{total,rows}`），其余 `api.post/put/delete` 自动解包——**查询对象接口一律 POST**（2026-10-30 后端定版），`api.get` 仅用于单简单参数/路径参数接口；错误 useApi 统一弹，页面 catch 留空
- **批量删除是路径参数逗号分隔**：`DELETE /system/article/comment/{id1,id2}`，用 `api.request` 拼路径，不是 query/body
- **会话失效返回 code 500 + "用户登录失效"**（SaTokenConfig 包装所致，非 401），useApi 已双判定处理
- API 模块放 `composables/useXxxAdminApi.ts`，行类型 + 状态映射（`XXX_STATE_MAP`）与请求同文件导出

## 🔴 n-data-table 列 render 元素 scoped CSS 选不中

columns render 函数在 NDataTable 渲染上下文执行，**页面 scoped hash 打不上 render 里的元素**（实证：封面 img 4080px、头像 0px、行高 2342px 全部失控，无任何报错）。列内样式只有三个去处：

1. **内聚组件**（首选）：AdminAvatar（头像/封面，外链裂图自动兜底首字占位）、AdminStateTag（状态标签）
2. **共享类** `assets/css/admin-table.scss`：`.cell-dim` / `.cell-actions` / `.cell-author` / `.cell-ellipsis` / `.cell-clamp-2`
3. **页面非 scoped 块**：页面专属类，命名带页面语义前缀（`article-xxx` / `comment-xxx` / `user-xxx`）防跨页覆盖

模板里写的元素（弹窗/抽屉内容）scoped 正常，不受此限。**列样式三首选顺序：内聚组件 → admin-table.scss 共享类 → 页面非 scoped 块。**

## 🔴 页面骨架模式（AdminListPage 表格版，新列表页一律照抄）

用户定版（M3）：**列表页 = 顶栏面包屑 + 多条件搜索卡片 + 表格卡片，外层零滚动条**。

```
layouts/default.vue：header 左 <AdminBreadcrumb />（数据源 useAdminMenu）
页面：<AdminListPage :search-fields :columns :fetch row-key seq?>
        ├─ 搜索卡片：字段配置驱动（input/select/checkbox 多选），右端 重置+查询 按钮
        ├─ #toolbar-left / #toolbar-right（可选工具栏，放统计标签/刷新按钮）
        ├─ n-data-table（flex-height，表体内部滚、表头固定）
        └─ footer：共 N 条(+已选) + n-pagination（show-size-picker）
```

- **菜单/面包屑单一数据源** `composables/useAdminMenu.ts`：`ADMIN_MENU` 树（叶子 key=路由路径）+ `useAdminBreadcrumbs()`；**新增菜单项改这里一处**，侧栏 n-menu 与面包屑同源自动更新
- **AdminSearchField**：`{key, label, type: 'input'|'select'|'multiple', options?, placeholder?, width?}`；所有字段统一「label 前置 + 控件」结构；multiple 渲染多选下拉（maxTagCount responsive 折叠 tag）；控件默认等宽 200px（不配 width，需差异化才覆盖）；key 直接对应后端 BO 字段（GET 数组 `key=a&key=b` 自动绑 `List<Integer>`）
- **查询快照机制**：点「查询」才取条件快照，翻页沿用快照——输入框实时变化不影响已出结果；页面无需自建任何分页/条件状态
- **分页默认 50/页**，pageSizes `[50, 100, 200]`（AdminListPage 默认值，页面不传）
- **🔴 详情弹出一律调详情接口**（用户定版）：禁止拿列表行数据展示详情——接口失败显示空态，不回退列表数据；抽屉打开先清空旧内容防残留上一篇
- **外层零滚动**：`.admin-list-page{height:calc(100vh - 80px)}`（56 header + 布局 padding 12×2，改布局 padding 必须同步此 calc）+ flex 链（列表卡片 `flex:1;min-height:0` + n-card content-style 立 flex column + 表格 `flex:1;min-height:0`）——纯 CSS 无需 JS 实时计算；768px 以下降级 height:auto（移动端允许外滚）
- **ref 方法**：`listRef.refresh()`（保留条件刷当前页）/ `reload()`（重置第 1 页）
- **操作列用纯文字链接**（NButton text + .cell-btns）：带图标时 3 按钮宽超列宽会被截断（实证溢出 22px）
- **统计轻查询**：`{pageNum:1, pageSize:1, 筛选条件}` 取 `total`，不拉全量
- **行内操作 loading** 用 `operatingId === row.id` 匹配单行
- **危险操作统一 `dialog.warning` 确认**（删除/冻结/解冻），确认文案写明级联影响
- **审核等编辑操作受 EditGroup 必填约束**：PUT payload 必填字段要齐全，列表行数据缺字段（如 articleContent）先 `getArticle` 补全再提交；TripartiteUser 无 EditGroup 约束，冻结只传 `{uuid, state}`
- teleport 组件（n-drawer/n-modal）宽度只能 inline `:style`，scoped 尺寸类选不中（同门户规则）
- **布局底色**：内容区浅灰（light `#f5f6f8` / dark `#101014`，layouts/default.vue 绑定）衬白卡片；AdminListPage 卡片白底带默认边框
- **前台地址展示**：文章前台 URL = `https://qixidi.top/articles/{id}`（门户路由 pages/articles/[id].vue）
- 评论/用户页仍是旧行卡片模式（AdminPageHeader+AdminRowCard），**待迁移到 AdminListPage 后两个旧组件即废弃**

## 组件库（components/）

| 组件 | 用途 | 关键 props / slots |
|------|------|-----------|
| **AdminListPage** | **通用列表页骨架（搜索+表格+分页+零滚动）** | `{searchFields, columns, fetch, rowKey?, seq?, selectable?, scrollX?}` + `#toolbar-left/#toolbar-right`，defineExpose `refresh/reload` |
| **AdminBreadcrumb** | 顶栏面包屑（自动按路由生成） | 无 props，数据源 useAdminMenu |
| AdminStateTag | 状态标签（圆点+文字） | `{value, map: Record<状态值, {label, type}>}` |
| AdminAvatar | 头像/封面（裂图兜底首字） | `{src?, name?, size: number|[w,h], shape?: 'circle'|'square'}` |
| AdminPageHeader / AdminRowCard | ⚠️ 旧行卡片模式遗留，仅评论/用户页引用，迁移后删除 | — |

AdminListPage 双 script 块（普通块 export 类型 + setup 块实现），页面 import 类型用 `import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue'`。

时间展示统一 `utils/format.ts` 的 `getFullDateTime`（完整时间直出，不用相对时间）。

## 无头验证

- 同门户方式（puppeteer-core + Edge 无头，脚本存 `%TEMP%\qixidi-pptr\`）
- **admin 页登录用 UI 表单**（本地 dev 凭据 ziwei/ziwei123，验证码开关默认关）；也可 localStorage 注入 `{token}`（key=`admin`）后刷新
- 全 CSR 应用 URL goto 与客户端导航渲染等价，验证不必模拟菜单点击
- 布局/视觉改动跑量测断言（getBoundingClientRect/裂图 naturalWidth/横向溢出），不要只截图

---

**核心原则**：列表页一律 AdminListPage 表格模式（配置驱动，外层零滚动、列表内滚）；列 render 样式只能进内聚组件或全局类；新页面只提供 searchFields + columns + fetch，不手写分页与搜索状态。
