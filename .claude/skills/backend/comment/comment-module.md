# 评论模块后端设计（通用模式）

适用于：文章、随笔（dictum）、时光小记（timeNotes）等内容的评论功能。

---

## 表设计（扁平楼层模式）

以 `b_time_notes_comment` 为例（`b_dictum_comment` 同构）：

| 字段 | 说明 |
|------|------|
| `time_notes_id` / `uid` | 被评论内容 id / 内容作者 id |
| `parent_id` | 父级评论 id，**一级评论 = 内容 id（不是 0！）** |
| `comment_grade` | 1 一级 / 2 二级 / 3 三级及以下 |
| `target_id` / `target_uid` | 回复目标评论 id / 目标用户 |
| `comment_uid` / `content` | 评论人 / 内容 |
| `type` | 1 = 评论内容本体，2 = 回复评论 |
| `status` | 0 正常 / 1 已删除（**软删**） |

索引：`idx_内容id`、`idx_parent_id`、`idx_comment_uid`。

**⚠️ collation 必须对齐库内老表（`utf8mb4_cs_0900_ai_ci`）**：建表语句若写常见的 `utf8mb4_general_ci`，单表查询正常，但与老表（如 `b_user_main`）join 做字符串比较时报 `Illegal mix of collations`（消息通知 join 查询 2026-09-15 踩坑）。新表接入跨表查询前先核对 `information_schema.TABLES.TABLE_COLLATION`。

## 代码结构（平行复制模式）

每个业务域独立一套，位于 `qixidi-business`：

```
domain/entity/timeNotes/TimeNotesComment.java
domain/bo/timeNotes/TimeNotesCommentBo.java          # @NotNull/@NotBlank + AddGroup/EditGroup
domain/vo/timeNotes/TimeNotesCommentVo.java          # 冗余用户字段 + children 次级集合
mapper/timeNotes/TimeNotesCommentMapper.java         # BaseMapperPlus，无 xml
service/timeNotes/TimeNotesCommentService(+Impl)
api/frontDesk/xxx/TimeNotesCommentController.java    # 需登录：POST /add、DELETE /delete/{id}
api/frontDesk/xxx/TimeNotesWhiteController.java      # 免登录：GET /comment/list/{id}
```

**关键约定**：
- 列表接口放 White Controller，`@PathVariable String id` 接收（防 JS Long 精度丢失），`Long.parseLong` 转换
- `commentUid` 从 `LoginHelper.getTripartiteUuid()` 取，不信任前端
- **add 接口返回插入后的 VO**（timeNotes 与 dictum 均已实现，2026-09-15）：MyBatis Plus 插入后主键自动回填 entity，`BeanUtil.copyProperties` 直接返回，零额外查库；用户昵称头像由前端登录态填充。前端用返回的真实 id 原位落定乐观节点，避免提交后整列表刷新闪烁
- commentList：分页查一级（type=1, 倒序）→ 批量查二级（`parentId in` 一级 ids，升序）→ 批量查 TripartiteUser 填充 → 组装楼层（共 3 次查询，无循环查库）

## ⚠️ 历史坑记录（b_dictum_comment 原版存在，已于 2026-09-15 全部回补修复）

1. **二级评论 commentUid 未收集导致 NPE**：uids 收集时只收了一级的 commentUid + 二级的 targetUid，**漏了二级的 commentUid**。只发过二级回复的用户会 NPE。
2. **用户注销后 userMap.get 返回 null**：填充用户信息需判空（用 `fillUserInfo` 私有方法防护）。
3. **删除评论本人校验缺失**：原版任何登录用户可删任何评论；需加"评论不存在校验 + `commentUid == 当前登录人`校验"。

> timeNotes 新建时即带全部修复；dictum 侧三个坑已于 2026-09-15 随随笔评论组件迁移一并回补。
