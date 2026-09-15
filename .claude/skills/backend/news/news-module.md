# 消息通知模块（b_news_user_record）接入指南

适用于：给新业务内容（文章/随笔/小记/…）的互动行为（评论/点赞/关注）接入站内消息通知。

---

## 双表分工（关键认知）

| 表 | 作用 | 何时写入 |
|----|------|---------|
| `b_news_user_record` | **互动通知主表**，评论/点赞/关注通知全在这 | 业务动作发生时（Service 内异步插入） |
| `b_news_user_info` | 后台手动发消息用（NewsUserInfo CRUD），**评论通知链路完全不参与** | 仅后台管理 |

> 新业务接入通知只写 `b_news_user_record`，不要动 `b_news_user_info`。

`NewsUserRecord` 字段约定：`uid`=被通知人、`newsId`=触发通知的记录 id（评论 id）、`targetUid`=操作人（评论人）、`type`=`NewsType` code、`beenRead`=0/1、`createTime`。

## NewsType 枚举（domain/enums/news/NewsType.java）

```java
COMMENT_NEWS(1, "评论"), FABULOUS_NEWS(2, "点赞"), FOLLOW_NEWS(3, "关注"),
PRIVATE_LETTER(4, "私信"), SYSTEM_NEWS(5, "系统消息"),
DICTUM_COMMENT_NEWS(6, "随笔评论"), TIME_NOTES_COMMENT_NEWS(7, "小记评论"),
```

**子类型规则**（6/7 模式，2026-09-15 引入）：同一大类的新内容类型（如"评论"下新增随笔/小记）追加独立 code，但：
- `listSums`/`listInfo` **跳过子类型不输出独立项**（`isCommentSubType()` 判断）
- `listSums` 统计时**先取出各分项数量**，再把子类型未读记录合并进大类型总数；type=1 条目挂 `subList` 分项明细（文章1/小记7/随笔6 各自未读数），供前端二级 tab 红点
- 路由与父类型相同（都是 `/news/comment`），前端在 `/news` 页内用二级 tab 区分

**前端未读状态的两个 Map**（useWebSocket）：`unreadMap`（一级 5 类型，合并总数，侧边栏/导航栏红点，**不能本地归零合并数**——子类型只清部分）+ `subUnreadMap`（评论分项，二级 tab 红点）。标已读后必须**强制重拉汇总**（force 参数绕过 wsConnected 守卫），不要本地推算。

## 写入模板（业务 Service.add 内，照搬 ArticleCommentServiceImpl）

```java
//通知被评论人（一级评论通知内容作者、回复通知被回复人），自己评论自己不发
if (!comment.getCommentUid().equals(bo.getTargetUid())) {
    executorService.execute(() -> {
        NewsUserRecord newsUserRecord = new NewsUserRecord();
        newsUserRecord.setUid(bo.getTargetUid());          //被通知人
        newsUserRecord.setNewsId(comment.getId());          //评论 id（插入后主键已回填）
        newsUserRecord.setTargetUid(comment.getCommentUid());
        newsUserRecord.setType(NewsType.XXX.getCode());
        newsUserRecord.setCreateTime(new Date());
        newsUserRecordMapper.insert(newsUserRecord);
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid()); //实时推送未读数
    });
}
```

- 线程池注入：`@Resource(name = "threadPoolInstance") private ExecutorService executorService;`（与 @Autowired 字段风格可混用）
- `targetUid` 必须是 BO 校验过的非空字段（@NotBlank），前端评论组件总是传
- WebSocket 推送会让在线用户的消息汇总实时刷新，无需前端轮询

## 删除评论联动清理通知（三个业务均已接入，2026-09-15）

评论删除（文章物理删/小记随笔软删）时**同步物理删除**对应通知记录，否则未读统计（不 join 评论表）与通知列表（join 后过滤软删）数字对不上：

```java
newsUserRecordMapper.delete(new LambdaQueryWrapper<NewsUserRecord>()
        .eq(NewsUserRecord::getType, NewsType.XXX_COMMENT_NEWS.getCode())
        .eq(NewsUserRecord::getNewsId, commentId));   //文章侧是 .in(collect)，连带子评论
```

**🔴 防误删铁律：必须 `type + news_id` 双条件。** `news_id` 存的是各业务评论表的 id，三表独立自增必然撞车——只按 news_id 删会把其他业务同 id 评论的通知一起删掉。文章删除是连同子评论批量删，用 `.in(评论id集合)`。

## 查询模板（NewsUserInfoMapper.xml，join 模式）

```sql
SELECT nur.id as newsId, 评论表字段..., 来源内容表.标题 as xxxTitle,
       nur.been_read as beenRead, nur.create_time as createTime,
       um.nickname AS commentName, um.avatar AS commentAvatar
FROM b_news_user_record AS nur
    LEFT JOIN 业务评论表 ON nur.news_id = 评论表.id
    LEFT JOIN 业务内容表 ON 内容表.id = 评论表.业务id
    LEFT JOIN b_user_main AS um ON 评论表.comment_uid = um.uuid
where 评论表.status = 0 and nur.type = #{type} and nur.uid = #{uid}
ORDER BY nur.create_time DESC
```

- 软删过滤字段：文章评论表是 `state`，dictum/timeNotes 评论表是 `status`（各表不统一，接入前确认）
- 平行新建 `XxxCommentNewsVo`（对照 `ArticleCommentNewsVo`）；来源无标题时带内容字段（dictum 用 `worksContent`，前端截断展示）
- 链路：Mapper → `INewsUserInfoService`/Impl 新增 `xxxCommentList(PageQuery)` → `FdNewsController` 新增 `GET /frontDesk/news/comment/xxx/list`

## 已读机制（天然支持子类型，无需改）

`newsRead` 按 `type + uid` 全量 update `beenRead`——前端二级 tab 传子类型 code（6/7）即只标对应子类。后端 update 后会 WebSocket 推送最新汇总。

前端注意：未读**触发判断**用父类型（子类型未读已合并进父类型总数）；子类型标记后**不要本地乐观归零**父类型未读数（可能还有兄弟子类型未读），交给 WebSocket 推送刷新。

## 前端 /news 页二级 tab 模式（pages/news/index.vue）

- 子 tab 配置：`commentSubs = [{ key, type, label }]`，key 写入 `route.query.sub`（默认 article 不带 query）
- `cacheKey`、`fetchNewsList` 分发、空状态文案均纳入 sub
- 渲染三块：`v-if="currentType === 1 && currentSub === 'xxx'"` 平行模板
- 跳转：小记 `/time-notes/{id}`、随笔 `/reading-essays/{id}`、文章 `/articles/{id}`；用户主页 `/user-home/article/{uid}`
- 样式类名用 `news-sub-*` 前缀（scoped CSS 类名冲突规则）

## 后端 Long 精度（全局兜底）

`framework/light-core` 的 `JacksonConfig` 注册了 `BigNumberSerializer`：超出 JS 安全整数的 Long 自动序列化为 String，安全范围内仍是 number——前端类型一律写 `number | string`，拼 URL 用模板字符串。
