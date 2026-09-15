# 通用评论区组件 ThreadedCommentSection

业务无关的楼层式评论区组件：`components/ThreadedCommentSection.vue`（2026-09 随时光小记评论功能抽取，源头是随笔侧旧组件 `DictumCommentSection.vue`）。**三大业务已全部接入**：时光小记、阅读随笔（2026-09-15）、文章（2026-09-15，经 `article/CommentSection.vue` 适配壳）。

> ⚠️ **必须放在 `components/` 根目录**。Nuxt 自动导入默认带目录前缀：`components/comment/Xxx.vue` 注册名是 `CommentXxx`，模板里写 `<Xxx>` 会报 `Failed to resolve component`。曾踩过此坑（2026-09-15）。

---

## 已接入业务

| 业务 | 挂载位置 | 后端次级集合字段 |
|------|---------|----------------|
| 时光小记 | `pages/time-notes/index.vue`、`pages/time-notes/[id].vue` | `children`（直接用） |
| 阅读随笔 | `components/readingEssays/ReadingEssaysDetailContent.vue` | `dictumCommentVoList` → 在 `useDictumCommentApi.getCommentList` 内映射为 `children` |
| 文章 | `components/article/CommentSection.vue`（适配壳）→ 4 个页面共用 | `mountComment` → 壳组件内递归映射为 `children` |

## 使用方式

```vue
<ThreadedCommentSection
  :biz-id="noteId"
  :biz-uid="note.uid"
  :fetch-list="(page) => commentApi.getCommentList(noteId, page)"
  :submit="(payload) => commentApi.addComment({ timeNotesId: noteId, ...payload })"
  :remove="commentApi.deleteComment"
/>
```

- `fetchList(pageQuery)` / `submit(payload)` / `remove(id)`：三个函数 props，业务 id 由调用方闭包组装
- `showReplyAvatar`（可选，默认 false）：二级/三级回复是否显示头像。文章评论区开启，随笔/时光小记关闭
- `submit` 返回 `Promise<Partial<CommentItem> | void>`：**至少返回真实 id**（如 `{ id }`），组件用它原位落定乐观节点（2026-09-15 优化：提交后不再整列表刷新，避免 `loading` 置位导致列表闪烁；临时 id `Date.now()` 若不落定，会被后续"回复/删除刚发的评论"误用）。后端只返回 Long id 时，适配层包装成 `{ id }` 即可
- 提交载荷类型 `CommentSubmitPayload`（不含业务 id）；时光小记请求体 `TimeNotesCommentBo extends CommentSubmitPayload`
- 列表项类型 `CommentItem`，次级集合字段名是 **`children`**；后端返回其他字段名（`dictumCommentVoList` / `mountComment`）时需在 API 层或壳组件映射
- 组件内部已处理：登录校验（未登录弹 `authDialogStore` 登录框）、乐观更新、楼层组装、表情选择、Ctrl+Enter 提交、加载更多
- `defineExpose({ refresh })` 供父组件刷新

## 乐观更新契约（提交不闪烁的关键）

```
提交流程：乐观插入（临时 id）→ 后端返回新评论（真实 id）→ 原位落定 id → 仅一级评论时 total+1
失败流程：移除乐观节点 → loadComments() 兜底恢复
```

- 后端 add 接口返回插入后的 VO 或仅返回 id 均可（组件只取 `created.id`）
- 组件刷新（非 loadMore）请求 `pageNum:1, pageSize: 已加载页数 × pageSize`：拉回全部已加载内容，修复"加载 3 页后刷新只剩第 3 页"的分页 bug
- 全局响应包装（`ResponseAdvice`）会把 Controller 返回值裹成 `{code, msg, data}`，前端 `api.post<T>` 解包 `res.data` 即拿到 VO

## 适配壳模式（文章侧，2026-09-15）

后端接口与通用契约差异较大时，不硬改通用组件，而是保留原组件文件名与 props 签名、内部重写为薄壳：

`components/article/CommentSection.vue`（895 行 → ~100 行）：

- **4 个页面零改动**：`articles/[id]`、`friend-link`、`regarding/about-author`、`regarding/introduce` 的 `<ArticleCommentSection :article-id :article-user-id />` 不变
- **布局留在壳上**：`margin-left: 92px`（避开文章左侧操作栏）+ 卡片底色圆角，通用组件保持业务无关
- **字段映射**：`commentName/commentAvatar → nickname/avatar`、`mountComment → children`（递归）、`targetName → targetNickname`、`state → status`
- **全量接口包装分页契约**：后端 `/white/article/comment/list` 不分页（PageQuery 参数收了没用），适配函数包装 `{ total: 一级评论数, rows: 全量 }` → `hasMore` 恒为 false，"加载更多"永不出现，行为与旧组件一致
- **删除参数瘦身**：旧前端传 `{ id, articleId, uid, commentUid }`，后端 `deleteWithValidById` 只用 id，`useCommentApi.deleteComment` 已简化为单参数
- **targetId 语义差异**：旧文章组件三级回复的 targetId 沿用二级评论的 targetId（指向一级评论）；通用组件恒用被回复评论的 id。后端通知只用 targetUid、查询只用 parentId，删除联动 `parentId OR targetId` 反而更精确，迁移无副作用

## 新业务接入评论的步骤

1. 后端按 `skills/backend/comment/comment-module.md` 平行建一套
2. `types/index.ts` 定义 `XxxCommentBo extends CommentSubmitPayload`（补业务 id 字段）
3. `composables/useXxxCommentApi.ts` 写 getCommentList / addComment / deleteComment 三方法（字段名不同就在此层映射）
4. 详情页挂组件，传三个函数 props；后端契约差异大时参考上面的适配壳模式

## ⚠️ 命名注意事项

- **`CommentSection` 名字已被 `components/article/CommentSection.vue` 占用**（Nuxt 自动导入按文件名），通用组件只能叫 `ThreadedCommentSection`
- 组件类名统一 `tcs-` 前缀，禁止用 `.comment-item` 等通用类名（scoped CSS 注入顺序问题，见 rules/frontend/qixidi-web-nuxt3.md）
