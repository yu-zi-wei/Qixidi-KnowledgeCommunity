# qixidi-service 开发规范

---

## 项目位置与技术栈

所有后端代码位于 `qixidi-service` 文件夹，用 `grep` 在其中搜索接口路径。

- Spring Boot 4.0.1 + Java 17 / Jackson 3（`tools.jackson`）/ MyBatis Plus 3.5.17
- MySQL 8.0 + Redis（Redisson）/ Sa-Token 1.46.0 / Spring AI 2.0.1

---

## AI 开发规范（qixidi-ai）

AI 唯一入口是 `qixidi-ai` 模块（Spring AI），禁止再引入直调大模型 HTTP API 的实现。

| 场景 | 入口 |
|------|------|
| 业务文本生成（摘要/总结等） | `AiGenerationService.generateContent(prompt)`（同步无记忆，失败返回 null 不影响主流程） |
| 多轮对话（记忆/图片） | `ChatStrategyMap.executeStrategy*` |

- 多个 ChatClient bean 注入必须 `@Resource(name = "...")`（@Primary 是 deepSeekChatClient，按类型注入会错拿）
- 场景化 ChatClient 装配集中在 `AIChatClientConfig`：`openAiChatClient`（图片解析+记忆）/ `deepSeekChatClient`（@Primary，对话）/ `generationChatClient`（无状态纯文本）
- AI 返回内容清洗统一用 `AiTextUtils.stripMarkdownCodeBlock`（qixidi-ai/utils）
- AI 配置走 `config.ai.*`（applicationConfig/{profile}/config.properties，yml 占位符引用）

---

## MCP Tool 开发规范（qixidi-mcp）

- 注册一律 `@Tool`（spring-ai-model）走现有 ToolCallbackProvider 装配；不用 `@McpTool`（纯 MCP 注解装配，工具无法同时复用给 qixidi-ai ChatClient）
- 工具名 snake_case 动词开头（create_xxx / update_xxx / list_xxx / get_xxx / delete_xxx）；description 三要素：干什么 + 入参取值来源（来自哪个工具的返回值）+ 副作用/状态流转（如"提交后进入审核 UNDER_REVIEW"）
- 入参与 POJO 字段一律 `@ToolParam(description, required)`：可选参数必须显式 `required = false`——POJO 字段默认 required（SpringAiSchemaModule requiredByDefault=true），不标 required 数组会全量误导调用方；必填性随场景变化（create 必填/update 可选）时标 false、场景写进 description（参考 McpArticleBo）
- description 写明真实约束：枚举值（type：1 原创 2 转载）、长度（摘要 ≤200 字）、格式（yyyy-MM-dd）、大小（图片 ≤10MB）；无依据的约束不编造
- ≥4 字段的大对象入参 POJO 化放 `domain/bo/{业务}/`（schema 会按参数名嵌套一层对象）；返回用专用 MCP VO（`domain/vo/{业务}/`）只含核心字段，禁直接返回业务 VO，时间格式化为字符串
- 返回结构语义必须写进 tool description：AI 读不到 VO 的 javadoc，@Tool 链也不生成 outputSchema（decompile 实证那是 @McpTool.generateOutputSchema + @JsonPropertyDescription 体系）；枚举值/时间格式/字段用途只能靠 description 传达，VO 字段 javadoc 只给人读
- tool 直调 Service 不经过 Controller 校验：BO 的 @NotBlank 等校验组不生效，必填项在 tool 方法内自行校验（参考 ArticleTools.checkTitleAndContent）
- 每个 tool 入口 `LoginHelper.getTripartiteUser()` 判空；属主 uid 由 Service 层从登录态强制取，tool 层不信任入参 uid
- 注入业务 Service 的 tool 构造器加 `@Lazy` 断 Spring AI 自动装配环（见 ArticleTools 构造器注释）

---

## 框架行为差异（写代码时必须知道）

### Jackson 3

- **属性名取字段原名**：字段首字母大写的坏命名会原样输出（Jackson 2 会兜底成小写驼峰）→ 实体字段一律小写驼峰；升级/排查时全局扫 `private\s+[\w.<>\[\],\s]+\s+[A-Z]\w*\s*;` 排雷（static/final 除外）。Lombok getter/setter 名不随字段首字母变化，改名对 Java 调用点零影响；MP 驼峰转列名 `i>0` 才加下划线，Entity 改名安全
- `@JsonSerialize` 等 databind 注解用 `tools.jackson.databind.annotation`；`@JsonFormat/@JsonIgnore/@JsonInclude` 留在 `com.fasterxml.jackson.annotation`（两代共用，不要改）
- 定制入口 `JsonMapperBuilderCustomizer`；自定义序列化器 `ValueSerializer<T>` / `SerializationContext`；入口类 `JsonMapper`
- 读写不再抛受检异常，不要写 `@SneakyThrows`

### MyBatis Plus 3.5.17

- `ServiceImpl`/`IService` 已删除：Service 一律 `@RequiredArgsConstructor` + final mapper 构造注入；批量写用 `BaseMapperPlus.insertBatch`（配合 @Transactional）
- **无 Sa-Token 请求上下文的线程（Filter 阶段/定时任务/异步线程）禁止实体写（insert/updateById）**：实体写触发 MetaObjectHandler 自动填充，填充器调 StpUtil 会抛「SaTokenContext 上下文尚未初始化」（Tomcat 线程复用时偶发）。这类场景一律 `update(null, Wrappers.lambdaUpdate().set(...))`——wrapper set 不走填充

### Spring Boot 4

- `spring-boot-starter-data-redis` 不携带 lettuce-core，新模块需要时显式引入
- Redisson 4.7 适配层不兼容 `Expiration.keepTtl()`（报 invalid expire time）→ application.yml 已排除 `RedissonAutoConfigurationV4`，spring-data-redis 走原生 Lettuce；RedissonClient 是手动 @Bean 不受影响，**这个 exclude 勿删**
- Spring AI 2.0 openai 模块 base-url 必须带 `/v1`（底层 SDK 直接拼路径，少了报 404）

### 构建

- 父 pom 用 `${revision}`：单独 `-pl 模块` 构建必失败，必须整仓 `mvn package`
- 验证编译结果必须 `mvn clean compile`：增量编译会复用旧 target class 造成 SUCCESS 假阳性

---

## 认证体系事实（Sa-Token）

- loginId 格式 `{userType}:{userId}`（`LoginHelper.JOIN_CODE`），前台 userType=`tripartite_user`；依赖 `sa-token.allow-login-id-colon: true`
- 前端请求头：`Authorization: Bearer {token}`
- Redis key 前缀 = token-name（`Authorization:`）；本机 Redis db1 与 guanmai 项目共用，**只能动 `Authorization:*`，绝不能动 `token:*`**
- dao 是自定义 `SaTokenDaoRedisCompat`（兼容低版本 Redis 的 KEEPTTL），官方 dao 装配已 exclude，**勿删**
- **新增存入 session 的自定义类型必须同步登记** `qixidi-auth/src/main/resources/META-INF/satoken/sa-json-type.list`（jackson3 反序列化类型白名单，每行一个类名）；登记时把**嵌套字段类型一并登记**（含集合元素类型，如 LoginUser.roles 的 RoleDTO——只登顶层类型，登录链路回读 session 时才炸 500）

---

## 防刷限流（CurrentLimitingInterceptor）

- `ResourcesConfig` 全局注册，按 IP 计数 8 秒 ≥250 次拉黑，黑名单仅存内存（重启即清）
- `SKIP_LIMIT_IPS` 回环白名单（127.0.0.1 等）是为了让 Nuxt SSR 的服务端请求免计数，**勿删**
- 自写拦截器 preHandle 拦截时必须显式写状态码 + JSON body：只 return false 不写响应会变成 200 + 空 body，极难排查

---

## MVC 三层架构

### 分层职责

| 层级 | 职责 | 禁止 |
|------|------|------|
| **Controller** | HTTP 请求响应、参数校验、权限控制 | 直接调用 Mapper、包含复杂业务逻辑 |
| **Service** | 业务逻辑处理、事务控制 | 直接操作 HTTP 请求/响应 |
| **Mapper** | 数据访问、SQL 操作 | - |

目录：后台管理 Controller 在 `qixidi-system/api`（URL 统一 `/system/*`，system 依赖 business 的 Service 层）；前台 Controller 在 `qixidi-business/api`（按业务子目录平铺，无 backstage/frontDesk 层级）

**绝对禁止**：Controller 直接调 Mapper、Controller 写 `lambdaQuery()`、复杂业务逻辑放 Controller。

---

## 实体类规范

- Lombok 必须同时 `@Data` + `@NoArgsConstructor`
- Entity（数据库实体）/ BO（接收参数）/ VO（返回数据）严格分放 `domain/entity|bo|vo`

---

## 数据库操作规范

### 禁止在循环中调用数据库

```java
// ❌ for (Long id : ids) { mapper.selectById(id); }
// ✅ mapper.selectBatchIds(ids) 或 in 查询
```

### Mapper 继承

三泛型 `<M, T, V>`：M=Mapper 自身、T=实体、V=默认 VO：

```java
@Mapper
public interface DataMapper extends BaseMapperPlus<DataMapper, DataEntity, DataVo> { }
```

### 数据模型事实（统计/聚合/排序前必读）

- **`b_private_user.update_time` 是"会话最新消息时间"冗余字段**：发私信时双向两行一起刷新——私信会话列表排序直接 `ORDER BY update_time DESC`，勿关联消息表算 MAX（消息表单边存储，双向归组很重）
- **b_browsing_history 按（uid+target）去重合并**（`ON DUPLICATE KEY UPDATE update_time`）：按天做浏览趋势必须聚合 `update_time`（聚合 create_time 会漏掉回访）
- **详情接口（queryById）禁止用 `selectVoById` 单表查询取含 join 展示字段的 VO**：nickname/groupingName/labelNameList 等是列表 join SQL 的产物，单表查全 null；须走列表同款 `selectIndex` + `wrapper.eq("别名.id", id)` 查询（参考 ArticleInformationServiceImpl.queryById）
- **用户统计走 `UserCountQueryHelper`**：新增统计字段三处同步——`CountUserWebsiteVo` 加字段 → Mapper 加 `selectXxxCountByUserIds`（XML）→ helper 加方法**并在 `allCounts()` 里 merge**
- **`CensusVo` 是通用时间序列货币**（dateTimes/censusSum/title），趋势类接口直接复用
- **本机 `mysql.exe` CLI 会挂起**：验证 SQL 用"`mvn clean compile` + 启动成功"代替（MyBatis 启动期绑定 XML，`Invalid bound statement` 起不来就是绑定失败）

---

## 日志与异常

- 新增/更新/删除必须加 `@Log(module = "...", businessType = BusinessType.INSERT)` 
- 异常用 `BusinessException("...")`；禁止返回 null 表示错误、禁止抛 `RuntimeException`
- 参数校验：`@Validated @RequestBody DataBo` + 校验分组（AddGroup/EditGroup）

---

## 返回值与 Controller 规范

**Controller 一律返回裸数据（含 void），禁止返回 R**——`ResponseAdvice` 对 com.qixidi 全局统一包装，直接返回 R 会双层嵌套（`{"code":200,"data":{"code":200,...}}`）。String 返回值会被 JSON 序列化包装。

| 类型 | 写法 | 前端调用 |
|------|------|----------|
| 非分页 | 返回 VO/List/Map 或 void → `{ code, msg, data }` | `api.post/put/delete`（自动解包） |
| 分页 | 返回 `TableDataInfo` → `{ total, rows }`（Advice 放行不包装） | `api.postPage` |

### 🔴 查询接口一律 POST + @RequestBody（2026-10-30 全量改造定版）

- 查询入参是对象（BO/实体/VO）一律 `@PostMapping` + `@RequestBody` 单参数；`@Validated(QueryGroup.class)` 等校验注解保留在前
- **分页随 BO 走**：`BaseEntity extends PageQuery`（BO 继承链自带分页），Controller 禁止单独声明 `PageQuery pageQuery` 参数；Service 调用双传 `service.list(bo, bo)`
- 查询 BO 无分页父类时补 `extends PageQuery`（勿 extends BaseEntity——日志类表无 create_time 等填充列，fill 字段会污染 INSERT）
- 保持 GET 的只有：单简单参数接口、`@PathVariable` id/路径类接口
- 分页字段安全机制：PageQuery 字段 `@TableField(exist=false)`（不参与列映射）+ `@JsonProperty(WRITE_ONLY)`（序列化只进不出，不污染响应）

- 权限：`@SaCheckPermission("data:query")`；防重复提交：`@RepeatSubmit`

---

## 接口对接（前端开发前必做）

用户提到接口路径时，先 grep `qixidi-service` 查 Controller 定义，确认：

1. 请求方式与参数接收：`@RequestParam`→URL / `@RequestBody`→JSON / `@RequestPart`→FormData / `@PathVariable`→路径
2. 返回格式：`Result.ok` 包装 / `TableDataInfo` 分页 / 直接返回对象（不被包装）
3. 确认后再写前端调用，**禁止猜测字段名**

---

## 代码质量检查清单

- [ ] 循环里调数据库了吗？
- [ ] Controller 直接调 Mapper / 写 lambdaQuery / 含业务逻辑了吗？
- [ ] 实体类 @Data + @NoArgsConstructor 了吗？
- [ ] 增删改加 @Log 了吗？
- [ ] Entity/BO/VO 混用了吗？

---

**核心原则**：严格遵守 MVC 三层架构，保持代码简洁，优先复用现有方法。
