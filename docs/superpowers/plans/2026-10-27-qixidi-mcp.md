# qixidi-mcp 模块实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在 qixidi-service 新增 `qixidi-mcp` 模块，为外部 agent（workbuddy 等）提供无状态 MCP 服务：API key 认证 + 文章/小记创作 tools + 平台/用户两级 skill 分发 + 网站管理接口。

**Architecture:** MCP server 内嵌主服务（Streamable HTTP `/mcp` 端点，无状态），API key 即 Sa-Token 自定义 tokenValue 的长效会话（Filter 换头复用现有认证/角色/属主体系），业务全部复用 qixidi-business 现有 Service，同时在 business 前台链路补上属主校验。

**Tech Stack:** Spring Boot 4.0.1 / Java 17 / spring-ai-starter-mcp-server-webmvc（BOM 2.0.1）/ Sa-Token 1.46 / MyBatis Plus 3.5.17（light-mybatis-plus 框架）

**Spec:** `docs/superpowers/specs/2026-10-27-qixidi-mcp-design.md`（先读 spec 再执行本计划）

## Global Constraints

- 整仓构建：父 pom `${revision}`，禁止 `mvn -pl`，必须 `mvn clean compile`（增量编译有 SUCCESS 假阳性），在 `qixidi-service/` 目录执行
- 新增任何文件后立即 `git add .`
- 后端 Service：`@RequiredArgsConstructor` + final mapper/service 构造注入（无 ServiceImpl/IService）；批量写用 `BaseMapperPlus.insertBatch`
- 实体：`@Data + @NoArgsConstructor`；Entity/BO/VO 分放 `domain/entity|bo|vo`
- 增删改接口加 `@Log(title = "...", businessType = BusinessType.XXX)`；业务异常用 `BusinessException`，禁 `RuntimeException`
- Redis 只用 `com.light.redission.utils.RedisUtils`；只允许动 `Authorization:*` key，禁动 `token:*`
- Jackson 3：实体字段一律小写驼峰；MP Mapper 三泛型 `<M, T, V>`
- 本机 mysql CLI 会挂起：SQL 验证一律用 `mvn clean compile` + 应用启动成功代替
- 为验证启动的后端实例，验证完立即关闭（确认端口释放）
- MCP tool 调用不走 Controller，`@Log` 不生效——写操作审计由 ApiKeyService 更新 `last_used_time` 承担

## Review Focus

1. **无效/停用/过期的 key 调 `/mcp`** → 必须返回 401 + JSON body（自写 Filter 只 return 不写响应会变 200 空 body，是本项目已知坑）
2. **用户 A 的 key 操作用户 B 的文章/小记**（get/update/delete）→ 必须被归属校验拒绝（Task 5）
3. **非创作者签发 key 或调用 create 类 tool** → 必须被角色门槛拒绝（NOT_CREATOR 链路）；签发入口本身也校验创作者身份（Task 9）
4. **吊销 key 后立即用旧 key 调用** → Redis 缓存与 Sa-Token 会话必须同步失效（Task 4 验证步骤覆盖）
5. **无状态模式下每请求独立 initialize** → 每个请求都先 initialize 再 tools/call 仍能拿到完整工具清单（Task 1/6 冒烟脚本覆盖）；`upload_image` 收到非图片或超大文件 → 明确报错而非写库异常

---

### Task 1: 模块骨架 + MCP starter Spike

验证地基：mcp-server-webmvc starter 在 Spring Boot 4 下可用、无状态端点工作。**若 starter 不可用（拉取失败/启动报错），停止并回报 spec 作者，改手写 Streamable HTTP 端点方案后再继续。**

**Files:**
- Create: `qixidi-service/qixidi-mcp/pom.xml`（仿 `qixidi-ai/pom.xml`：parent=qixidi-service，依赖 qixidi-common、light-web、`spring-ai-starter-mcp-server-webmvc`——版本由父 pom spring-ai-bom 2.0.1 管理，不写版本号）
- Modify: `qixidi-service/pom.xml`（`<modules>` 加 `<module>qixidi-mcp</module>`；`<dependencyManagement>` 加 qixidi-mcp 条目，照 qixidi-ai 的写法）
- Modify: `qixidi-service/qixidi-startup/pom.xml`（`<dependencies>` 加 qixidi-mcp）
- Create: `qixidi-service/qixidi-mcp/src/main/resources/application-mcp.yml` 或并入 startup 的 application.yml（**先看 spring-ai starter 的配置绑定方式再定**；本任务最小配置见 Step 3）
- Create: `qixidi-service/qixidi-mcp/src/main/java/com/qixidi/mcp/tools/PingTool.java`

**Interfaces:**
- Produces: MCP 端点 `POST /mcp`（无状态）；`PingTool.ping()` tool 供后续任务确认注册链路；模块坐标 `com.light:qixidi-mcp`

- [ ] **Step 1: 创建模块 pom 并接线（三处 pom）**

依赖：`qixidi-common`、`light-web`（Controller/Result 基建）、`spring-ai-starter-mcp-server-webmvc`。groupId 等照抄 qixidi-ai/pom.xml。

- [ ] **Step 2: 写 PingTool**

```java
@Component
@RequiredArgsConstructor
public class PingTool {
    @Tool(description = "连通性测试：原样返回输入文本，用于验证 qixidi MCP 连接是否正常")
    public String ping(String text) { return "pong: " + text; }
}
```
`@Tool` 来自 `org.springframework.ai.tool.annotation.Tool`（若包名不同以本地依赖为准）。

- [ ] **Step 3: 写 McpServerConfig + 配置**

```java
@Configuration
public class McpServerConfig {
    @Bean
    public ToolCallbackProvider tools(PingTool pingTool) {
        return MethodToolCallbackProvider.builder().toolObjects(pingTool).build();
    }
}
```
application.yml 增加：`spring.ai.mcp.server.stateless: true`、`spring.ai.mcp.server.name: qixidi`、端点为默认 `/mcp`（具体键名以 starter 自动配置类为准，用 IDE/反编译确认，**不凭记忆猜**）。

- [ ] **Step 4: 整仓编译**

Run: `cd qixidi-service && mvn clean compile`
Expected: BUILD SUCCESS（starter 拉取成功）

- [ ] **Step 5: 启动 + curl 冒烟**

启动 qixidi-startup（方式照常），执行：

```bash
curl -s -X POST http://127.0.0.1:9001/mcp -H 'Content-Type: application/json' \
  -H 'Accept: application/json, text/event-stream' \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-03-26","capabilities":{},"clientInfo":{"name":"curl","version":"0"}}}'
curl -s -X POST http://127.0.0.1:9001/mcp -H 'Content-Type: application/json' \
  -H 'Accept: application/json, text/event-stream' \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/list"}'
```
Expected: initialize 返回 serverInfo；tools/list 含 `ping`。**此时 /mcp 尚未鉴权（后续任务补），限流拦截器已覆盖它**。

- [ ] **Step 6: 关闭启动实例，回填 spec，提交**

确认端口释放。把 starter 实际配置键名回填 spec 的 Spike #1 结论。`git add . && git commit -m "feat(mcp): 新增 qixidi-mcp 模块骨架与无状态 MCP 端点 spike"`

---

### Task 2: API key 纯逻辑（生成 + 哈希）— TDD

**Files:**
- Create: `qixidi-service/qixidi-mcp/src/main/java/com/qixidi/mcp/utils/ApiKeyGenerator.java`
- Create: `qixidi-service/qixidi-mcp/src/test/java/com/qixidi/mcp/utils/ApiKeyGeneratorTest.java`
- Modify: `qixidi-service/qixidi-mcp/pom.xml`（test 依赖 `spring-boot-starter-test`，scope test）

**Interfaces:**
- Produces: `ApiKeyGenerator.generate() → String`（`qxmcp_` + 48 位随机字母数字，64 hex 或 base62 均可但须测试固定格式）；`ApiKeyGenerator.sha256(String raw) → String`（64 位 hex 小写）

- [ ] **Step 1: 写失败测试**

断言：generate() 以 `qxmcp_` 开头、总长 54；生成 100 次无重复；sha256("abc") 等于已知值 `ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad`；generate 不含 `/` `+` `=`（避免 URL/头传输坑）。

- [ ] **Step 2: 运行确认失败**

Run: `cd qixidi-service && mvn clean test -pl 禁止！用 mvn clean test -Dtest=ApiKeyGeneratorTest -am 整仓带依赖跑，或直接 mvn clean test`
Expected: 编译失败（类不存在）

- [ ] **Step 3: 实现 ApiKeyGenerator**

`SecureRandom` + 固定字母表采样；sha256 用 `MessageDigest` + hex 转小写。

- [ ] **Step 4: 测试通过后提交**

`git add . && git commit -m "feat(mcp): API key 生成与哈希工具"`

---

### Task 3: 两张表 + Entity/BO/VO/Mapper/Service

**Files:**
- Create: `qixidi-service/db/agent_api_key_and_skill.sql`（两张表 DDL + 官方 skill 种子行先留空，Task 9 补种子内容）
- Create: `qixidi-service/qixidi-mcp/src/main/java/com/qixidi/mcp/domain/entity/AgentApiKey.java`、`.../entity/AgentSkill.java`
- Create: `.../domain/bo/AgentApiKeyBo.java`、`.../domain/bo/AgentSkillBo.java`、`.../domain/vo/AgentApiKeyVo.java`（**不回显 keyHash**）、`.../domain/vo/AgentSkillVo.java`、`.../domain/vo/McpSkillBriefVo.java`（id/name/description）
- Create: `.../mapper/AgentApiKeyMapper.java`、`.../mapper/AgentSkillMapper.java`
- Create: `qixidi-service/qixidi-mcp/src/main/resources/mapper/AgentApiKeyMapper.xml`、`.../AgentSkillMapper.xml`（空映射文件，XML 存放位置先看 `framework/light-mybatis-plus/.../MybatisAutoConfiguration.java` 的 mapper-locations 与 @MapperScan 范围，不覆盖则此路径为准）
- Create: `.../service/ApiKeyService.java`、`.../service/AgentSkillService.java`

**Interfaces:**
- Consumes: `ApiKeyGenerator`（Task 2）
- Produces:
  - `ApiKeyService.createKey(TripartiteUser user, String keyName, Long timeoutSeconds) → String`（生成明文 key、写表、**创建 Sa-Token 长效会话**：`StpUtil.login(loginId, SaLoginModel)` 自定义 device="mcp"、tokenValue=明文 key、timeout=key 有效期，SaLoginModel 的具体方法名以 sa-token 1.46 实际 API 为准（反编译/IDE 确认，不凭记忆）；随后 `LoginHelper.setTripartiteUser(user)`；loginId/device 细节以 `LoginHelper.tripartiteLoginByDevice` 现有实现为准，允许复制其内部写法）
  - `ApiKeyService.listByUser(String uuid) → List<AgentApiKeyVo>`
  - `ApiKeyService.updateStatus(Long id, String uuid, Integer status)` / `delete(Long id, String uuid)`（吊销时删 Redis 缓存 + `StpUtil.logoutByTokenValue(明文key)`——明文不可逆，故表中同时存 `saToken` 列）
  - `ApiKeyService.validate(String rawKey) → TripartiteUser`（`RedisUtils.getCacheObject("mcp:apikey:" + sha256(rawKey))` 未命中 → 查库 keyHash 匹配 + status 启用 + 未过期 → 校验 `StpUtil.getLoginIdByTokenValue(rawKey)` 会话存在 → `RedisUtils.setCacheObject(key, user, 5, MINUTES)`；命中直接返回）
  - `AgentSkillService.listForAgent(String uuid) → List<McpSkillBriefVo>`（`user_id IS NULL` 全局行 + 本用户行，按 id 升序）
  - `AgentSkillService.getForAgent(Long id, String uuid) → AgentSkillVo`（全局行或本用户行，否则 BusinessException）
  - 用户 CRUD：`create/update/delete(String uuid, ...)` 只操作本用户行

表结构（DDL 按此写）：`b_agent_api_key(id bigint pk, user_id varchar(64), key_name varchar(64), key_hash char(64) unique, sa_token varchar(128), status tinyint default 1, expire_time datetime null, last_used_time datetime null, create_time datetime, update_time datetime)`；`b_agent_skill(id bigint pk, user_id varchar(64) null, name varchar(100), description varchar(500), content mediumtext, status tinyint default 1, create_time datetime, update_time datetime)`，索引：`b_agent_skill(user_id)`、`b_agent_api_key(user_id)`。

- [ ] **Step 1: 写 DDL + 实体 + BO/VO + Mapper + Service 空实现**（本任务无纯逻辑可单测，验证 = 编译 + 启动绑定）
- [ ] **Step 2: `mvn clean compile`** Expected: SUCCESS
- [ ] **Step 3: 在本机 MySQL 手工执行 DDL 建表**（用 Navicat/IDEA 执行 `db/agent_api_key_and_skill.sql`，不用 mysql CLI）
- [ ] **Step 4: 启动应用确认 MyBatis 绑定成功**（无 `Invalid bound statement`），关闭实例
- [ ] **Step 5: 提交** `git add . && git commit -m "feat(mcp): API key 与 skill 数据模型及服务层"`

---

### Task 4: 认证链路（ApiKeyFilter + 换头桥 + 放行）

**Files:**
- Create: `qixidi-service/qixidi-mcp/src/main/java/com/qixidi/mcp/auth/McpApiKeyFilter.java`
- Modify: `qixidi-service/qixidi-startup/src/main/resources/application.yml`（`security.excludes` 列表加 `- /mcp/**`，该列表现有 `/ai/**`、`/white/**` 先例）

**Interfaces:**
- Consumes: `ApiKeyService.validate`（Task 3）
- Produces: `/mcp/**` 全量鉴权；请求通过后 Sa-Token 上下文可用（后续 tool 内 `LoginHelper.getTripartiteUser()` 直接工作）

- [ ] **Step 1: 实现 McpApiKeyFilter（OncePerRequestFilter）**

逻辑：仅拦 `/mcp` 开头请求 → 无 `X-API-Key` 头或 `ApiKeyService.validate` 抛异常时：**显式写** `response.setStatus(401)` + `application/json` body `{"code":401,"msg":"invalid api key"}` 再 return（本项目规则：只 return 不写响应 = 200 空 body 事故）→ 通过则用 `HttpServletRequestWrapper` 包装请求，把 `Authorization` 头覆写为 `Bearer {rawKey}` 放进过滤器链（后续 Sa-Token 体系识别会话）。注册为 FilterRegistrationBean，URL pattern `/mcp/*`，order 排在 Sa-Token 过滤之前。
- [ ] **Step 2: `mvn clean compile` + 启动**
- [ ] **Step 3: curl 三连验证**

① 无 key → 期望 401 + JSON body；② 伪造 key → 401；③ 数据库手工插入一条测试 key（keyHash=sha256(测试串)、saToken=测试串、status=1）→ 带 `X-API-Key: 测试串` 请求 initialize 成功（Sa-Token 会话不存在时会 401——需先手工造会话或直接验证①②，③ 的完整链路放 Task 6 有真实签发后验证）。**实测通过后删除手工测试行。**
- [ ] **Step 4: 提交** `git add . && git commit -m "feat(mcp): /mcp 端点 API key 认证过滤器"`

---

### Task 5: business 归属校验（文章 + 小记，前台链路）

**Files:**
- Modify: `qixidi-service/qixidi-business/src/main/java/com/qixidi/business/service/impl/article/ArticleInformationServiceImpl.java`（`updateByBo:287`、`delete:605`、`getArticle:573`）
- Modify: `qixidi-service/qixidi-business/src/main/java/com/qixidi/business/service/impl/TimeNotesServiceImpl.java`（`update:63`、`delete:79`、`getInfo:141`）

**Interfaces:**
- Produces: 上述六个方法入口处统一校验（写成各文件内私有方法 `checkOwnership(目标uid)`）：`LoginHelper.getTripartiteUser()` 非 null（前台用户）且 `目标记录 uid ≠ 当前用户 uuid` → `BusinessException("无权操作他人内容")`；`getTripartiteUser()` 为 null（后台 sys_user 走自家 Controller 链路）放行。目标记录的 uid 字段名以实体实际字段为准（先读实体再写）。

- [ ] **Step 1: 读两个实体确认 uid 字段名，给六个方法加校验**（一处一处加，不动方法其他逻辑）
- [ ] **Step 2: `mvn clean compile` + 启动冒烟**：用两个前台用户 token 交叉 get/update 对方文章 → 期望 500/BusinessException msg；操作自己的 → 正常。**网站前端同步回归**：正常编辑自己文章不受影响。
- [ ] **Step 3: 提交** `git add . && git commit -m "fix(business): 前台文章与小记接口补充属主校验"`

---

### Task 6: 主链路 tools（get_publish_context + create_article）

**Files:**
- Create: `qixidi-service/qixidi-mcp/src/main/java/com/qixidi/mcp/tools/PublishContextTool.java`、`.../tools/ArticleTools.java`
- Create: `.../domain/vo/McpPublishContextVo.java`（labels: List<LabelInfoVo>、groupings: List<LabelGroupingInfoVo>、specials: List<SpecialInformationVo>、penName: String）
- Modify: `McpServerConfig`（toolObjects 加两个 tool）

**Interfaces:**
- Consumes: `ILabelInfoService.fdLabelList(LabelInfoBo)`、分组查询方法（从 `FdLabelInfoController.java:31` 起复制其注入的 Service 字段与方法）、`ISpecialInformationService`（从 `SpecialFrkController.java` 复制）、`IArticleInformationService.insertByBo / updateByBo`、`LoginHelper.getTripartiteUser()`、`ApiKeyService`（更新 lastUsedTime）
- Produces: tools `get_publish_context()`、`create_article(...)`

- [ ] **Step 1: PublishContextTool** — `get_publish_context()` 无参，聚合上述三组列表 + 用户笔名（字段名以 `TripartiteUser.java` 实际字段为准）。description 写清"写文章/小记前先调它拿可选标签、分组、专栏与笔名"。
- [ ] **Step 2: ArticleTools.create_article** — 入参（title、content、labelIds、specialId 等）与 `ArticleInformationBo` 字段对齐（**先读 BO 源码再定入参，禁止凭记忆**）；复刻 `UserArticleController.add` 的语义：`bo.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW)`、id 非空走 updateByBo；从 `LoginHelper.getTripartiteUser()` 取当前用户；返回 `{articleId, auditState}`。
- [ ] **Step 3: `mvn clean compile` + 启动端到端 curl**：网站调 `/agent/apiKey` 尚未存在（Task 9）——先用单测/临时 main 或直接在测试里调 `ApiKeyService.createKey` 拿真实 key，再走 initialize → tools/list（应含 2 个新 tool）→ tools/call get_publish_context → tools/call create_article 真实写库 → 前台文章列表可见该文章（审核中状态）。验证完关闭实例。
- [ ] **Step 4: 提交** `git add . && git commit -m "feat(mcp): 发布上下文与文章创建 tools"`

---

### Task 7: 其余文章 tools + upload_image

**Files:**
- Modify: `.../tools/ArticleTools.java`（补 list_my_articles、get_article、update_article、delete_article）
- Create: `.../tools/UploadImageTool.java`、`.../utils/ByteArrayMultipartFile.java`（实现 MultipartFile 的最小包装：name/originalFilename/contentType/bytes）
- Modify: `McpServerConfig`

**Interfaces:**
- Consumes: `IArticleInformationService.getArticleInfo(bo, pageQuery) / getArticle(id) / updateByBo / delete(id)`；`ISysOssService.upload(MultipartFile) → SysOss`（qixidi-system）
- Produces: 五个 tool；`upload_image(base64, filename) → {url}`（SysOss.getUrl()）

- [ ] **Step 1: 实现五个 tool**（list 强制按当前用户 uid 过滤；get/update/delete 依赖 Task 5 的 Service 层属主校验即可，tool 层不重复）
- [ ] **Step 2: ByteArrayMultipartFile + upload_image**（base64 解码 → 包装 → `iSysOssService.upload` → 返回 url；description 注明支持格式与用途"文章封面与正文配图"）
- [ ] **Step 3: `mvn clean compile` + 启动 curl 冒烟**（upload 用一张小图 base64 实测返回 OSS url；delete/get/update 走一遍）验证完关闭
- [ ] **Step 4: 提交** `git add . && git commit -m "feat(mcp): 文章管理 tools 与图片上传 tool"`

---

### Task 8: 小记 tools + skill tools

**Files:**
- Create: `.../tools/TimeNotesTool.java`、`.../tools/SkillTool.java`
- Modify: `McpServerConfig`

**Interfaces:**
- Consumes: `TimeNotesService.add(TimeNotesBo) / update(TimeNotesBo)`（入参字段以 `TimeNotesBo` 源码为准）；`AgentSkillService.listForAgent / getForAgent`
- Produces: tools `create_time_note`、`update_time_note`、`list_skills`、`get_skill`

- [ ] **Step 1: 实现四个 tool**（skill 两个的 description 写明"开始写作前先 list_skills 再 get_skill 读取用户写作偏好与平台规范"）
- [ ] **Step 2: `mvn clean compile` + 启动 curl 冒烟**（小记真实写库；list_skills 返回两级合并结果——先手工插入一条 user_id IS NULL 种子行验证）
- [ ] **Step 3: 提交** `git add . && git commit -m "feat(mcp): 小记与 skill 分发 tools"`

---

### Task 9: 网站管理接口 + 官方 skill 种子

**Files:**
- Create: `.../controller/AgentApiKeyController.java`（`/agent/apiKey`）、`.../controller/AgentSkillController.java`（`/agent/skill`）、`.../controller/AgentMcpInfoController.java`（`/agent/mcp-info`）
- Modify: `qixidi-service/db/agent_api_key_and_skill.sql`（追加官方 skill 种子 INSERT：栖息地写作规范、栏目结构、10 个 tool 的使用指引，Markdown 全文）

**Interfaces:**
- Consumes: `ApiKeyService`、`AgentSkillService`（Task 3）；创作者判定（roleId≥2 的实际判定方式以 `ArticleInformationServiceImpl` 内 NOT_CREATOR 门槛的现有写法为准，复制同款判据）
- Produces: `POST /agent/apiKey`（校验创作者身份；body: keyName、有效期天数；**响应 data 只此一次返回明文 key**）、`GET /agent/apiKey/list`、`PUT /agent/apiKey/{id}`（改名/启停）、`DELETE /agent/apiKey/{id}`；`/agent/skill` CRUD（只能操作 user_id=自己的行）；`GET /agent/mcp-info` → `{endpoint: "https://qixidi.top/mcp", headerName: "X-API-Key", snippets: {claudeCode: "...", codex: "...", generic: "..."}}`（片段内容用 spec 的示例）

- [ ] **Step 1: 三个 Controller**（增删改加 `@Log`；返回 `Result.ok`；校验 `@Validated` + AddGroup/EditGroup）
- [ ] **Step 2: 种子 SQL**（官方 skill 全文认真写：平台定位、文章 vs 小记的选择标准、标签/专栏使用建议、tool 调用顺序建议、审核规则提示）
- [ ] **Step 3: `mvn clean compile` + 启动全流程 curl**：登录拿 Sa-Token → 创建 key（断言明文只出现一次）→ 带新 key 走 initialize → get_publish_context → create_article → 网站前台可见 → 吊销 key → 再调 → 401（**Review Focus #4**）
- [ ] **Step 4: 提交** `git add . && git commit -m "feat(mcp): API key 与 skill 管理接口及官方 skill 种子"`

---

### Task 10: 端到端验收 + 交付收尾

**Files:**
- Create: `qixidi-service/db/nginx-mcp.conf`（交付给用户的 nginx 片段：`location /mcp { proxy_pass http://127.0.0.1:9001; proxy_buffering off; proxy_http_version 1.1; }`，注释说明与 /api/ 剥前缀写法的区别）
- Modify: spec 文档（Spike 结论、最终 tools 清单勾稽）、`CLAUDE.md` 模块表加 qixidi-mcp 一行

- [ ] **Step 1: 完整回归**（Review Focus 五项逐条过，含"无效 key 401 JSON"“越权拒绝"“非创作者拒绝"“吊销即失效"“无状态每请求可 initialize"）
- [ ] **Step 2: workbuddy 实测**：用户在 workbuddy 配 URL + key 跑真实写作流（此步需用户参与）
- [ ] **Step 3: 收尾提交 + 规则提炼自问**（默认不提炼；若"MCP 模块结构/会话桥"满足三门槛再入 rules/backend）
