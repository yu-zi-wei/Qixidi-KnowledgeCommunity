# qixidi-mcp 模块设计

日期：2026-10-27
状态：已与用户对齐设计，待实施

---

## 意图与背景

博客作者（项目 owner 及未来全站用户）写文章太累、经常不知道怎么写。实际工作流是：用 agent 客户端（如 workbuddy）帮忙起草和提交内容。目前缺少一条让外部 agent 以用户身份操作栖息地的标准通道。

**最终形态**：网站提供"AI 工作台"——用户创建自己的 API key 和 skill，agent 通过固定的 MCP 接口能力进行创作。MCP tools 是平台固定的，skill 是用户自定义的写作指令。

## 目标

1. qixidi-service 新增 Maven 模块 `qixidi-mcp`，内嵌 MCP server，挂主服务同端口（`/mcp` 端点），Streamable HTTP 传输，**无状态模式**
2. 固定 tools：文章/小记的创建、编辑、提交；标签、分组、专栏等发布上下文查询；skill 拉取
3. API key 认证（PAT 模式）：用户在网站签发、吊销，agent 持 key 以该用户身份操作
4. skill 存储（Markdown 存库）+ 通过 MCP tools 自动分发给 agent
5. 给网站的管理 REST 接口（API key / skill CRUD + MCP 接入说明），走现有 Sa-Token 认证

## 非目标

- 不做开放平台：API key 仅用户签给自己用，不签发给第三方
- 不做 skill 市场 / 分享 / 推荐
- 前端 AI 工作台页面放二期（本期只出后端接口）
- 不做服务器推送通知、资源订阅（无状态模式天然放弃，不需要）
- 不引入第二种对外协议（不做 OpenAPI tool、webhook 等）

## 反目标（做成了也算失败）

- **MCP 提交绕过现有管控**：agent 发的内容必须和人工发的走同一条链路（审核、敏感词、统计），绝不另起炉灶
- **API key 与 Sa-Token 抢认证头**：MCP 不走 `Authorization: Bearer`，必须独立头
- **tools 里写业务逻辑**：tool 只做参数装配，业务全部复用 qixidi-business 的 Service

## 用户已拍板的决策

| 决策点 | 结论 |
|--------|------|
| 模块名 | `qixidi-mcp` |
| 部署形态 | 挂主服务，非独立进程 |
| 传输模式 | Streamable HTTP + 无状态（`spring.ai.mcp.server.stateless=true`） |
| 认证头 | `X-API-Key`（与 Sa-Token 的 Authorization 彻底隔离） |
| skill 分发 | agent 通过 `list_skills` / `get_skill` tools 自动拉取；管理页配复制按钮兜底 |
| 首期范围 | 后端模块 + 打通 workbuddy 主链路；前端管理页二期 |
| "专辑"语义 | 专栏 |
| 越权边界 | 直接修 business：前台链路的文章/小记 get/update/delete 加 uid 属主校验（非仅 MCP 层过滤） |
| 来源标记 | 一期不打标；platform_type 另有用途，本期不动 |
| 图片能力 | 一期含 `upload_image` tool（复用后端 OSS 上传能力） |
| key 签发资格 | 仅创作者可签（与现有 NOT_CREATOR 门槛一致） |
| 平台 skill | 要：全局官方 skill + 用户自建 skill 两级合并 |
| nginx | 由用户自行变更服务器配置；MCP 端点统一以 `/mcp` 打头 |

## 架构

```
workbuddy / Claude Code / Codex 等外部 agent
    │  Streamable HTTP POST /mcp + X-API-Key 头
    ▼
qixidi-service:9001 （nginx 反代 /mcp/ → 127.0.0.1:9001，proxy_buffering off）
    │
    ▼
qixidi-mcp（新模块）
    ├── tools/        固定 MCP tools（@Tool 注解），只做参数装配
    ├── auth/         ApiKeyFilter + McpUserContext（不进 Sa-Token）
    ├── controller/   网站管理接口（frontDesk，走 Sa-Token）
    ├── domain/       entity / bo / vo
    ├── mapper/       b_agent_api_key、b_agent_skill
    ├── service/      ApiKeyService、AgentSkillService
    └── config/       McpServerConfig（tool 注册）、模块装配
    ▼
qixidi-business（复用现有 Service：文章/小记/标签/专栏）
```

依赖：`qixidi-business`、`qixidi-common`、`spring-ai-starter-mcp-server-webmvc`（版本由父 pom spring-ai-bom 管理）。启动模块 `qixidi-startup` 需加上此依赖。

## 认证设计（API key）

参照 GitHub PAT：

- 表 `b_agent_api_key`：`id, user_id(uuid 字符串，前台用户主键为 uuid 非数字 id), key_name(备注名), key_hash(SHA-256), status(启用/停用), expire_time(可空), create_time, update_time, last_used_time`
- **签发资格**：仅创作者（roleId≥2）可创建 API key，创建入口校验角色
- key 明文格式：`qxmcp_` + 48 位随机串；明文仅创建时返回一次，库中只存 SHA-256
- 校验链：`ApiKeyFilter` 拦 `/mcp/**` → SHA-256(key) 查 Redis（TTL 5 分钟）→ 未命中查库 → 构建 `McpUserContext`（**缓存并携带完整 `TripartiteUser`**——业务 Service 的角色门槛需要 roleId，裸 userId 不够）→ 后续 tool 执行时使用；吊销/停用时删除缓存即时生效
- **Sa-Token 放行**：`qixidi-auth/config/SaTokenConfig.java` 中把 `/mcp/**` 加入排除路径
- 防刷限流（CurrentLimitingInterceptor）保持覆盖 `/mcp`：一轮 agent 对话约 5~10 次调用，远低于阈值，且保留防滥用能力

## 越权边界（安全口径）

Builder check 核实的存量弱点：前台文章/小记的 get/update/delete Service **不校验属主**（任何登录用户理论上可改删他人内容）。本期直接修 business 层：

- 前台用户链路（UserArticleController、TimeNotesController 对应 Service 方法）的 update / delete / get 增加归属校验：目标记录 uid ≠ 当前用户则拒绝
- 后台管理接口（backstage，@SaCheckPermission 管理员场景）不受影响
- MCP 与网站共用该 Service，天然继承校验——API key 只能操作其绑定用户自己的内容
- 存量其他接口的越权面不在本期范围，另案排查

## 固定 tools 清单（初版）

| tool | 入参 → 出参 | 复用的 Service 对接点 |
|------|------------|---------------------|
| `get_publish_context` | 无 → 标签列表、分组列表、我的专栏列表、笔名 | LabelInfoService（`/white/label/list`、`/white/label/grouping/list`）、SpecialInformationService（`/white/special/list`） |
| `list_my_articles` | page → 分页列表 | UserArticleController 对应的 article/list Service |
| `get_article` | id → 详情（含草稿） | get/article/{id} Service |
| `create_article` | title, content(md), labelIds, specialId?, … → articleId | insert/article Service（AddGroup 校验） |
| `update_article` | id, 可变字段 → vo | update/article 或 save/draft Service |
| `delete_article` | id → void | delete/article/{id} Service |
| `upload_image` | 图片数据 + filename → 图片 URL | 复用后端 OSS 上传 Service（实施时定位现有入口，API key 认证链路内可用） |
| `create_time_note` | 小记字段 → id | TimeNotesService.add（TimeNotesBo） |
| `update_time_note` | id, 字段 → void | TimeNotesService.update |
| `list_skills` | 无 → [{id, name, description}] | AgentSkillService |
| `get_skill` | id → content(Markdown) | AgentSkillService |

要求：
- **description 面向 LLM 撰写**：这是模型决定何时、如何调用的唯一依据，逐个精心写（中文，含使用时机、参数语义、返回结构说明）
- 文章提交/审核状态流转沿用现有语义（草稿 → 提交），MCP 不新增状态机
- 属主校验在 business Service 层统一做（见"越权边界"），tools 不重复实现、也不得绕过

## skill 数据模型

表 `b_agent_skill`：`id, user_id(平台官方 skill 为 NULL), name, description, content(Markdown), status, create_time, update_time`。

**两级合并**：`list_skills` / `get_skill` 返回 平台官方 skill（`user_id IS NULL`）+ 该用户自建 skill。官方 skill 承载栖息地写作规范、栏目结构、tool 使用指引——是 agent 首次接入、用户尚未自建时的默认指引，初版内容以种子数据提供。网站管理接口仅能增删改**自己的**行。

## 网站管理接口（Sa-Token 认证，frontDesk）

- `POST /agent/apiKey` 创建（返回明文一次）、`GET /agent/apiKey/list`、`PUT /agent/apiKey/{id}`（改名/启停）、`DELETE /agent/apiKey/{id}`
- `/agent/skill` CRUD 同构
- `GET /agent/mcp-info` → MCP 端点地址 + 各家客户端（Claude Code / Codex / 通用）的配置片段，前端直接渲染

增删改按规范加 `@Log`。

## 实施风险与必须先验证的点（Spike 范围）

1. **`spring-ai-starter-mcp-server-webmvc` 与 Spring Boot 4 兼容性**：✅ 已验证（2026-10-27 spike）——starter 2.0.1 可用，无状态端点工作。实证配置键：`spring.ai.mcp.server.protocol: STATELESS`（枚举 SSE/STREAMABLE/STATELESS）、`spring.ai.mcp.server.name`、`spring.ai.mcp.server.streamable-http.mcpEndpoint: /mcp`；`@Tool` 在 `org.springframework.ai.tool.annotation`，注册用 `MethodToolCallbackProvider.builder().toolObjects(...)`。注意：`/mcp/**` 必须加入 `security.excludes`（Sa-Token 全局拦截），否则返回"用户登录失效"
2. **LoginHelper 上下文打通**（最高风险）：现有 frontDesk Service 内部很可能通过 `LoginHelper`（Sa-Token 上下文）取 userId，MCP 线程没有会话。需 grep Service 调用点确认，二选一：
   - BO/方法签名已带 uid → MCP 直接传
   - Service 内部取上下文 → MCP 侧构造临时用户上下文（不可用 StpUtil.login 污染会话）
3. nginx 反代由**用户自行变更**（时机：开发完成、上生产前）。注意与 `/api/` 的剥前缀写法**不同**，此处必须保留原路径：`location /mcp { proxy_pass http://127.0.0.1:9001; proxy_buffering off; proxy_http_version 1.1; }`（proxy_pass 不带 URI 即不重写路径；勿用尾斜杠 location）。生产公网地址 `https://qixidi.top/mcp`，由 `/agent/mcp-info` 返回

## 落地顺序

1. **Spike**：模块骨架 + MCP starter 兼容性 + LoginHelper 上下文方案（验证完保留骨架，验证结论回填本文档）
2. 两张表 + ApiKey 认证链 + `get_publish_context` + `create_article` —— 打通 workbuddy 写文章主链路
3. 其余文章 tools + 小记 tools + skill tools
4. 网站管理接口 + 接入说明 API
5. 二期：前端 AI 工作台（API key 管理 + skill 编辑器 + 接入引导）

## 留给建造者的

- tools 的 description 措辞（按"面向 LLM"标准自行打磨）
- 表 DDL 细节（索引：key_hash 唯一、user_id + status 组合）
- Redis key 前缀沿用模块约定（如 `mcp:apikey:{hash}`）
- Sa-Token 上下文注入的具体实现方式（按 Spike 结论）
- 工具入参 BO 与现有 ArticleInformationBo / TimeNotesBo 的字段映射（实施时以实际 BO 字段为准，禁止凭记忆编造）
