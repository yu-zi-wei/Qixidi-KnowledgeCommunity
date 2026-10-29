# qixidi-web-admin 开发规范

管理后台 2.0（Nuxt 3 + TS + Naive UI，全 CSR）。需求源：`qixidi-web-admin/docs/开发需求与设计.md`。

---

## 技术底座事实

- **全 CSR**（`routeRules '/**': {ssr:false}`）：无水合问题，`localStorage`/`window` 可直用；但 useAsyncData 兜底、SSR 兼容类规则不适用本项目
- **auto-import 仅 4 个 composable**（nuxt.config 显式清单）：useDialog/useMessage/useNotification/useLoadingBar；**darkTheme/NIcon/zhCN/NTooltip 等组件与对象必须手动 import**（忘写不报编译错，静默崩）
- **主题唯一数据源在 app.vue**：provide `theme`/`switchTheme`，localStorage key=`admin-theme`；业务组件只 inject 派生，禁止自建主题状态
- **pinia persist 是 localStorage**（key=`admin`），与门户（cookie）不同
- 图标同门户规范：Tabler Icons，名字必须查 `node_modules/@vicons/tabler/es/`

## API 约定

- 后台接口统一 `/system/*` 前缀（Controller 在 qixidi-system 模块）；`useApi` 语义同门户：分页 `api.getPage`（`{total,rows}`），其余 `api.get/post/put/delete` 自动解包；错误 useApi 统一弹，页面 catch 留空
- **批量删除是路径参数逗号分隔**：`DELETE /system/article/comment/{id1,id2}`，用 `api.request` 拼路径，不是 query/body
- **会话失效返回 code 500 + "用户登录失效"**（SaTokenConfig 包装所致，非 401），useApi 已双判定处理
- API 模块放 `composables/useXxxAdminApi.ts`，行类型 + 状态映射（`XXX_STATE_MAP`）与请求同文件导出

## 🔴 n-data-table 列 render 元素 scoped CSS 选不中

columns render 函数在 NDataTable 渲染上下文执行，**页面 scoped hash 打不上 render 里的元素**（实证：封面 img 4080px、头像 0px、行高 2342px 全部失控，无任何报错）。列内样式只有三个去处：

1. **内聚组件**（首选）：AdminAvatar（头像/封面，外链裂图自动兜底首字占位）、AdminTimeText（相对时间 + hover 完整时间）、AdminStateTag（状态标签）
2. **共享类** `assets/css/admin-table.scss`：`.cell-dim` / `.cell-actions` / `.cell-author` / `.cell-ellipsis`
3. **页面非 scoped 块**：页面专属类，命名带页面语义前缀（`article-xxx` / `comment-xxx` / `user-xxx`）防跨页覆盖

模板里写的元素（弹窗/抽屉内容）scoped 正常，不受此限。

## 页面骨架模式（三个管理页已定型，新页面照抄）

```
AdminPageHeader（title/description/stats 统计胶囊/actions 刷新按钮）
└─ n-card（无边框 10px 圆角）
   ├─ toolbar：搜索框（enter 触发）+ n-radio-group segment 筛选
   └─ n-data-table：remote + striped + size=small + 分页 computed
```

- **统计胶囊轻查询**：`{pageNum:1, pageSize:1, 筛选条件}` 取 `total`，不拉全量
- **行内操作 loading** 用 `operatingId === row.id` 匹配单行
- **危险操作统一 `dialog.warning` 确认**（删除/冻结/解冻），确认文案写明级联影响
- **审核等编辑操作受 EditGroup 必填约束**：PUT payload 必填字段要齐全，列表行数据缺字段（如 articleContent）先 `getArticle` 补全再提交；TripartiteUser 无 EditGroup 约束，冻结只传 `{uuid, state}`
- teleport 组件（n-drawer/n-modal）宽度只能 inline `:style`，scoped 尺寸类选不中（同门户规则）

## 组件库（components/，全部已产出可直接复用）

| 组件 | 用途 | 关键 props |
|------|------|-----------|
| AdminPageHeader | 页头 + 统计胶囊 | `{title, description?, stats?: [{label, value?, accent?}]}` + #actions |
| AdminStateTag | 状态标签（圆点+文字） | `{value, map: Record<状态值, {label, type}>}` |
| AdminAvatar | 头像/封面（裂图兜底首字） | `{src?, name?, size: number|[w,h], shape?: 'circle'|'square'}` |
| AdminTimeText | 表格时间列 | `{time}` |

## 无头验证

- 同门户方式（puppeteer-core + Edge 无头，脚本存 `%TEMP%\qixidi-pptr\`）
- **admin 页登录用 UI 表单**（本地 dev 凭据 ziwei/ziwei123，验证码开关默认关）；也可 localStorage 注入 `{token}`（key=`admin`）后刷新
- 全 CSR 应用 URL goto 与客户端导航渲染等价，验证不必模拟菜单点击
- 布局/视觉改动跑量测断言（getBoundingClientRect/裂图 naturalWidth/横向溢出），不要只截图

---

**核心原则**：列 render 里的样式要么进组件要么进全局，页面 scoped 只管模板元素；新页面照抄三页骨架，先组件化再写页面。
