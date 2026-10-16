# qixidi-web-nuxt3 开发规范

栖息地博客前端开发规范。

---

## 🔴🔴🔴 最高优先级规则

**当用户提供接口时，必须先去 `qixidi-service` 查看接口定义！绝对不能瞎编字段名！**

流程：接口路径 → Grep 后端 Controller → 查 VO/BO 字段定义 → 确认参数与返回格式 → 再写前端代码。

---

## API 调用规范

- 后端项目位置：`D:\Project\qixidi\qixidi-service`（`grep -rn "接口路径" --include="*.java"`）
- 响应格式：非分页 `{ code, msg, data }` 用 `api.get/post/put/delete`（自动解包）；分页 `{ total, rows }` 用 `api.getPage`
- 参数接收：`@RequestParam`→URL / `@RequestBody`→JSON / `@RequestPart`→FormData / `@PathVariable`→路径
- API 模块放 `composables/`，命名 `useXxxApi.ts`（自动导入）

### API 错误处理（强制）

**`useApi` 已统一拦截错误并弹出后端 `msg`，页面禁止重复写 `message.error`。**

```typescript
// ✅ 只在需要流程控制时 try/catch（关 loading 等）；catch 空逻辑可整个省略
try {
  await api.saveData(data)
  message.success('保存成功')  // 成功提示可以写
} catch {} finally { saving.value = false }

// ❌ 禁止：catch 里再弹错误（重复）、硬编码错误消息（后端 msg 更准）
```

例外（需自己处理）：剪贴板 `navigator.clipboard`、文件上传 `useOssApi`（XHR 不走 useApi）、前端表单校验 `message.warning`。

---

## SSR 规范

- `useMessage()`/`useDialog()` 等需要 provider 的 composable **不能在 SSR 阶段的 setup 顶层调用**——放事件处理函数内或用 `.client.vue`
- 影响布局的关键样式放全局 `main.css`（scoped CSS 有闪烁风险）

### useAsyncData 兜底模式（强制）

SSR 可能失败的 useAsyncData，**handler 失败必须返回 `null`，且禁止配 `default` 选项**：

```typescript
const { data } = await useAsyncData('key', async () => {
  try {
    const { rows } = await api.getList()
    return rows || null
  } catch (e) { return null }
})
const list = computed(() => data.value || [])  // 消费端兜底
```

机制：返回 `[]`（非 null）或配了 `default` 会让水合期把缓存当有效数据短路，客户端**永远不会重新请求**（静默失败）。返回 `null` + 无 default → 水合时自动重取。dev 模式返回 null 会打 may be duplicated 警告，是预期提示，勿"修复"。

- key 必须随路由参数变化（computed key），禁止固定 key + 手动 watch 补救
- 顺序 await 每个 useAsyncData，**禁止 `await Promise.all([...useAsyncData])`**（组合 await 破坏 onMounted 注册时序，兜底静默失效）
- 详细用法见 [nuxt3-data-fetching.md](nuxt3-data-fetching.md)

### HTML 嵌套禁忌

浏览器会自动"修复"非法嵌套导致 SSR 与客户端 DOM 不一致：

| 外层 | 内层禁止 | 替代 |
|------|---------|------|
| `<a>`/NuxtLink | `<a>`、`<button>` | 内层用 `<span @click.stop>` |
| `<button>` | `<a>`、`<button>` | 外层改 `<div>`，或拆并列 |

---

## 组件规范

### 组件自动导入前缀

`components/` 子目录的组件名带目录前缀：`common/MdEditorWithVideo.vue` → `<CommonMdEditorWithVideo>`（写错会**静默渲染失败**，先 grep 使用先例确认）。

### 图标（强制）

统一 Tabler Icons（`@vicons/tabler`），禁止 @nuxt/icon 或 Emoji。**图标名以官方名为准，不能凭记忆写**——不存在的导出会运行时整页崩。拿不准先查：`ls node_modules/@vicons/tabler/es/ | grep -i 关键词`。

### 页面跳转（强制）

**用户可能中键点击（新标签页）的跳转必须用 `<NuxtLink>`**（文章卡片、列表项、标签、通知图标等）；操作型跳转（返回、登录重定向、提交后跳转）用 `navigateTo`。NuxtLink 记得 `text-decoration: none`。

### 时间显示（强制）

统一用 `~/utils/formatTime.ts`：`formatTime`（相对时间）+ `getFullDateTime`（title 属性），禁止组件内自定义时间格式化函数。相对时间必须带 `:title`。

### 文章列表统计（强制）

浏览/点赞/评论数无论是否为 0 都必须展示（`{{ item.x || 0 }}`），保持图标布局一致。

### Scoped CSS 类名唯一（强制）

Nuxt SSR 下 scoped CSS 注入顺序不稳定，**不同组件共用同一类名会随机互相覆盖**。每个组件用带前缀的唯一类名（`article-stat` / `sidebar-stat` / `popover-stat`），禁止通用类名（`stat-item`、`card-item`、`meta-info`）。例外：`main.css` 全局类、Tailwind 工具类。

### Naive UI 组件行为差异

- **n-menu/n-tree 的 key 必须唯一**：折叠/展开按 key 增删，key 重复会联动。分组节点的 key 一律用唯一 id（`menu-${item.id}`），叶子节点可用 route（供 `:value` 高亮匹配）；`route || fallback` 兜不住非空占位串（如 `"-"`），见 `layouts/admin.vue` 的 `getMenuKey`
- **n-tabs 占满整行**：放进 flex 行内会独自占一行挤走其他元素，需 `width: auto; flex-shrink: 0`
- **Transfer 穿梭框**：面板标题用 `source-title`/`target-title`（不是 render 函数）；不支持 `#source`/`#target` slot；高度用固定像素，禁百分比/flex。开发顺序：先最简实现跑通 → 再加样式，一次只改一个问题
- **grid 双栏等宽**：并排卡片列写 `grid-template-columns: minmax(0, 1fr) minmax(0, 1fr)`，不能只写 `1fr 1fr`（子项 min-width:auto，nowrap 长文本会撑开挤压相邻列，内部 ellipsis 失效）

### md-editor-v3 编程式插入（强制）

禁止 `content.value += text`（永远追加末尾）。编辑器从未聚焦时 selectionStart=0 会插到**开头**——用 `@onFocus` 标记"放置过光标"，统一入口 `components/common/MdEditorWithVideo.vue` 的 `insertToEditor`。

---

## 样式与主题（强制）

- 所有新增样式支持浅色/深色双主题：颜色一律 CSS 变量（`--color-ink` 等），禁止硬编码色值；Tailwind 用 `dark:` variant
- 设计 token（字体/间距/圆角/阴影/动画）见 [design-system.md](design-system.md)，禁止硬编码字号间距
- 优先复用 `main.css` 全局样式（`.article-card`、`.tab-bar` 等），组件内只写特殊样式

---

## 布局规范

### admin 布局滚动模型（强制）

`layouts/admin.vue` 整页永不滚动，滚动责任在 `.admin-content`（overflow-y:auto）。新页面两种写法皆可：

| 模式 | 写法 | 滚动位置 |
|------|------|---------|
| 自管滚动 | 根节点 `height:100%` + 列表区 `flex:1;min-height:0;overflow-y:auto` | 页面内部列表区 |
| 文档流 | 无高度约束 | `.admin-content` 整体 |

自管滚动可把分页放滚动容器外常驻底部。**新增后台页面必测：长列表能滚到最底部（分页/末条可见）**。

### 移动端页面间距覆盖

布局默认移动端 `padding-top: 260px`，需要不同间距的页面：`useHead({ bodyAttrs: { class: 'page-xxx' } })` + 非 scoped 样式覆盖 `body.page-xxx .home-main { padding-top: ... !important }`。禁止 JS 直接操作父布局 DOM、禁止 scoped 覆盖（选不中父元素）。

已有间距：默认 260px（首页）| `page-reading-essays` 170px | `page-tool` 160px | `page-article-archive`/`page-friend-link`/`page-label`/`page-regarding` 70px

---

## Nuxt dev 与 build 互斥（强制）

**dev server 运行期间禁止执行 `nuxi build`**——共用 `.nuxt` 目录互相污染：dev 中跑 build 会产出混入 vite-node 代码的 `.output`（部署后整站 500）；build 后 dev 会 HMR 失效/新增组件不生效/`#app-manifest` 解析失败。测试实例用完必须立即关闭（详见 code-basics「测试实例管理」）。

同族坑：**先引用、后建组件文件** → Vite transform 缓存陈旧，组件静默渲染失败（点击无反应，仅控制台 resolve component 警告）。修复：`touch` 引用方强制重新 transform。预防：**先 Write 新组件文件，再 Edit 引用方**。

验证代码的正确姿势：开发期用 dev + 浏览器；需要 build 验证编译时**先停 dev** 再 build。

---

## UI 改动必须真实浏览器验证（强制）

**curl 只能验证 HTTP 200，验证不了布局/滚动/可见性。** 布局类改动必须无头浏览器量测 + 截图验证（脚本存 `%TEMP%\qixidi-pptr\` 可复用）：

1. **驱动**：`puppeteer-core` + 本机 Edge 无头（`C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe`）
2. **登录态构造**（admin 页必经）：
   - token 来源：Redis db1 `KEYS "Authorization:login:token:*"` 取 key 后缀（**只读，token 值不得进日志**）；需当前用户信息时用该 token 调 `/oauth/getInfo`（header 带 `Bearer` 前缀）
   - **注入方式必须是 `auth` cookie**：`document.cookie = 'auth=' + encodeURIComponent(JSON.stringify({token})) + '; path=/'`，注入后重载让 `auth-init.client.ts` 读到
   - 🔴 **不能往 localStorage 注入**：本项目 pinia persist 默认 storage 是 cookie（key=store id=`auth`）
3. **页面导航**：admin 页用客户端导航模拟真实路径（`__vue_app__.config.globalProperties.$router.push(path)`）；已登录态 URL goto（刷新场景）也要测
4. **断言**：量测（getBoundingClientRect、scrollHeight、getComputedStyle）+ 截图双确认；`page.on('pageerror')` 收集运行时错误
5. **登录态断言用 pinia 内存态**（`$pinia.state.value.auth.token/user`），禁止 `body.innerText.includes('用户名')`（同名文章假阳性）

---

## dev server 运维（Windows）

| 坑 | 规则 |
|----|------|
| PowerShell 后台管道任务会被宿主会话回收（exit 9、无日志） | 长驻 dev 用独立进程：`Start-Process cmd.exe /c "npx nuxi dev > %TEMP%\xxx.log 2>&1"` |
| dev 启动早于新页面文件创建 → 路由 watcher 失效，新页面 404 | 新建页面目录后 404，先比对 dev 启动时间 vs 文件创建时间，重启 dev |
| Nuxt dev 路由是 Vite 虚拟模块不落盘 | 权威判据：浏览器里 `__vue_app__...$router.getRoutes()` 读真实路由表 |
| 未登录直接 URL 访问 `/admin/**` 显示 404 错误页 | 属预期（路由守卫 abort），验证 admin 页须先构造登录态 |

---

## 站点刻意设计清单（勿当缺陷上报）

时光小记宽容器（1352px 无侧栏）而其他内容页 992px；用户主页无站点导航（独立 1200px 布局）；全站无传统 footer（页脚信息在首页侧边栏）；各页卡片圆角/阴影差异——均为刻意设计。UI 巡检先排除本清单；新发现的跨页差异先确认设计意图，判断标准是 bug 和功能障碍，不是风格差异。

---

**核心原则**：所有新代码必须支持双主题，禁止硬编码颜色，禁止瞎编接口字段名。
