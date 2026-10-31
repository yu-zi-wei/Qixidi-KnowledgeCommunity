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
- **会话失效返回 code 401**（2026-10-31 M3 改造：SaTokenConfig 去掉包装，NotLoginException 走 GlobalExceptionHandler 401 映射；此前是 code 500 + "用户登录失效"），useApi 401 分支已激活，msg 含"登录失效"兜底仍在
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
        ├─ #toolbar-left（批量操作）/ #toolbar-right（新增等入口按钮）
        ├─ n-data-table（flex-height，表体内部滚、表头固定）
        └─ footer：「共 N 条」靠右、紧邻 n-pagination（show-size-picker）
```

- **菜单/面包屑单一数据源** `composables/useAdminMenu.ts`：`ADMIN_MENU` 树（叶子 key=路由路径）+ `useAdminBreadcrumbs()`；**新增菜单项改这里一处**，侧栏 n-menu 与面包屑同源自动更新；**侧栏一级菜单必须带子菜单（禁止纯一级叶子）**；数据看板无菜单入口——首页 `/` 即数据看板页面，面包屑「首页」直达
- **AdminSearchField**：`{key, label, type: 'input'|'select'|'multiple', options?, placeholder?, defaultValue?}`；所有字段统一「label 前置 + 控件」结构；multiple 渲染多选下拉（maxTagCount responsive 折叠 tag）；**控件宽度一律走默认 200px，禁止配 width 单独覆盖**（页面各自配宽会破坏全站搜索控件等宽）；key 直接对应后端 BO 字段（GET 数组 `key=a&key=b` 自动绑 `List<Integer>`）
- **查询快照机制**：点「查询」才取条件快照，翻页沿用快照——输入框实时变化不影响已出结果；页面无需自建任何分页/条件状态
- **🔴 列宽双轨制**（2026-10-31 定版）：值长度不可预估的内容列（标题/名称/账号/邮箱/URL/摘要/模块/地点/浏览器/角色等）一律 `minWidth`（自适应拉伸填满容器，无留白）；值域窄而稳定的列（状态/类型/方式/时间/统计数字/IP/序号/操作按钮）才 `width` 固定。**scroll-x 会把表格总宽钉死在列宽合计**——含 minWidth 列时 AdminListPage 自动不传 scroll-x（小屏保护由列 minWidth + 容器横滚承担，勿手动传 scrollX 覆盖）
- **分页默认 50/页**，pageSizes `[50, 100, 200]`（AdminListPage 默认值，页面不传）
- **🔴 详情弹出一律调详情接口**（用户定版）：禁止拿列表行数据展示详情——接口失败显示空态，不回退列表数据；抽屉打开先清空旧内容防残留上一篇
- **外层零滚动**：`.admin-list-page{height:calc(100vh - 88px)}`（56 header + 布局 padding 16×2，改布局 padding 必须同步此 calc）+ flex 链（列表区透明容器 `flex:1;min-height:0` + 表格 `flex:1;min-height:0`）——纯 CSS 无需 JS 实时计算；768px 以下降级 height:auto（移动端允许外滚）
- **ref 方法**：`listRef.refresh()`（保留条件刷当前页）/ `reload()`（重置第 1 页）
- **操作列用纯文字链接**（NButton text + .cell-btns，按钮间隙 10px）：带图标时 3 按钮宽超列宽会被截断（实证溢出 22px）
- **🔴 列加 `fixed: 'right'` 时表格底色必须实色**（app.vue 全局 thColor/tdColor 已设纸片色 #ffffff/#1a1a1e，勿改回 transparent）：fixed 列 sticky 定位下透明背景会透出横向滚动中的下层内容
- **行内主标题即详情入口**：标题 `is-link` 主色可点击开详情抽屉，不放独立"详情"按钮
- **文字浓度/字重定版**：正文列（标题/摘要/统计/时间）与「共 N 条」一律正常浓度（opacity 1）不做浅色弱化（`.cell-dim` 仅旧页空值占位遗留）；表头 600、行内主标题 400（已固化在 AdminListPage/admin-table.scss，页面不再写）
- **统计轻查询**：`{pageNum:1, pageSize:1, 筛选条件}` 取 `total`，不拉全量
- **行内操作 loading** 用 `operatingId === row.id` 匹配单行
- **🔴 操作列所有按钮执行前一律二次确认**（用户定版，含通过/拒绝等状态操作）：正向操作 `dialog.info`、破坏性操作（拒绝/删除/冻结）`dialog.warning`，文案写明后果（如"通过后文章将对外可见""删除后不可恢复"）
- **状态终审（审核/冻结）走专用接口**只传 ids+结果（如 `PUT /system/article/information/audit`），不走编辑 PUT；真正的整体编辑受 EditGroup 必填约束，列表行数据缺字段（如 articleContent）先调详情接口补全再提交；TripartiteUser 无 EditGroup 约束，冻结只传 `{uuid, state}`
- teleport 组件（n-drawer/n-modal）宽度只能 inline `:style`，scoped 尺寸类选不中（同门户规则）
- **视觉体系（2026-10-31 极简书签风定版，取代 10-30 高级风）**：浅色默认（仅显式 dark 才深色，app.vue）；暖纸底色系——内容区 #FAF9F6 / 侧栏 #F3F1EA，区块是白纸片卡片（`--panel-bg` 白 + `--hairline` #E8E4D9 细边框 + `--panel-shadow` 轻投影浮起感，圆角 10px）；墨绿（纸墨意象，冷暖互补）只做交互点缀（浅 `#3D7A5C` / 深 `#6FBF9A`，非 Naive 上下文用布局根注入的 `--cell-link` 变量，与 app.vue primaryColor 同源）；全局 6px 暖灰细滚动条（`--scrollbar-thumb`，样式在 admin-table.scss）；深色模式保留黑白灰骨架（纸片 #1a1a1e），投影/滚动条变量随主题切换
- **前台地址展示**：文章前台 URL = `https://qixidi.top/articles/{id}`（门户路由 pages/articles/[id].vue）

## 组件库（components/）

| 组件 | 用途 | 关键 props / slots |
|------|------|-----------|
| **AdminListPage** | **通用列表页骨架（搜索+表格+分页+零滚动）** | `{searchFields, columns, fetch, rowKey?, seq?, selectable?, scrollX?}`（scrollX 缺省自动按列宽合计，小屏表格内横向滚动）+ `#toolbar-left/#toolbar-right`，defineExpose `refresh/reload` |
| **AdminBreadcrumb** | 顶栏面包屑（自动按路由生成） | 无 props，数据源 useAdminMenu |
| AdminStateTag | 状态标签（圆点+文字） | `{value, map: Record<状态值, {label, type}>}` |
| AdminAvatar | 头像/封面（裂图兜底首字） | `{src?, name?, size: number|[w,h], shape?: 'circle'|'square'}` |

AdminListPage 双 script 块（普通块 export 类型 + setup 块实现），页面 import 类型用 `import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue'`。

时间展示统一 `utils/format.ts` 的 `getFullDateTime`（完整时间直出，不用相对时间）。

## 无头验证

- 同门户方式（puppeteer-core + Edge 无头，脚本存 `%TEMP%\qixidi-pptr\`）
- **nuxi dev 只监听 ::1**（IPv6 localhost）：无头脚本访问 dev 页面必须用 `http://localhost:端口`，写 `127.0.0.1` 必拒连（Get-NetTCPConnection 可见监听地址为 `::1`）
- **admin 页登录用 UI 表单**（本地 dev 凭据 ziwei/ziwei123，验证码开关默认关）；也可 localStorage 注入 `{token}`（key=`admin`）后刷新
- **无 `window.__vue_app__`（那是门户内嵌 admin 页的通道）**：登录态断言读 `JSON.parse(localStorage.admin).token`，页面导航一律 URL goto
- 全 CSR 应用 URL goto 与客户端导航渲染等价，验证不必模拟菜单点击
- 布局/视觉改动跑量测断言（getBoundingClientRect/裂图 naturalWidth/横向溢出），不要只截图

---

**核心原则**：列表页一律 AdminListPage 表格模式（配置驱动，外层零滚动、列表内滚）；列 render 样式只能进内聚组件或全局类；新页面只提供 searchFields + columns + fetch，不手写分页与搜索状态。
