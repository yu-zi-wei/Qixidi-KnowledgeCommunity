---
description: 仅在开发 qixidi-service 项目时遵循
globs: qixidi-service/**, qixidi-service\**
---

# qixidi-service 开发规范

---

## ⚠️ 重要变更

| 项目 | 状态 | 说明 |
|------|------|------|
| **qixidi-service** | ✅ 当前使用 | 所有后端代码已迁移至此 |

**查找接口**：使用 `grep` 在 `qixidi-service` 文件夹中搜索接口路径

---

## 技术栈

- **框架**：Spring Boot 4.0.1 + Java 17（2026-09-16 从 3.3.2 升级）
- **JSON**：Jackson 3（`tools.jackson`，Boot 4 默认）
- **ORM**：MyBatis Plus 3.5.17（`mybatis-plus-spring-boot4-starter` + `mybatis-plus-jsqlparser`）
- **数据库**：MySQL 8.0（druid-spring-boot-4-starter + dynamic-datasource-spring-boot4-starter）
- **缓存**：Redis + Redisson 4.7（`redisson-spring-boot-starter` + 显式 `redisson-spring-cache`）
- **认证**：Sa-Token 1.46.0（`sa-token-spring-boot4-starter`）
- **AI**：Spring AI 2.0.1（spring-ai-bom）

---

## AI 开发规范（qixidi-ai）

AI 唯一入口是 `qixidi-ai` 模块（Spring AI），禁止再引入直调大模型 HTTP API 的实现。

| 场景 | 入口 |
|------|------|
| 业务文本生成（文章摘要/总结等） | `AiGenerationService.generateContent(prompt)`（同步无记忆，失败返回 null 不影响主流程） |
| 多轮对话（记忆/图片） | `ChatStrategyMap.executeStrategy*` |

- **多个 ChatClient bean 注入必须 `@Resource(name = "...")`**：`@Primary` 是 deepSeekChatClient，按类型注入会错拿
- 场景化 ChatClient 装配集中在 `AIChatClientConfig`：`openAiChatClient`（图片解析+记忆）/ `deepSeekChatClient`（@Primary，对话）/ `generationChatClient`（无状态纯文本，无 advisor）
- AI 返回内容清洗统一用 `AiTextUtils.stripMarkdownCodeBlock`（qixidi-ai/utils）
- AI 外部化配置走项目惯例：`config.ai.*` 定义在 `applicationConfig/{profile}/config.properties`，yml 用 `${config.ai.xxx}` 占位符引用（与 redis/minio 同模式）

---

## Spring Boot 4 / Jackson 3 升级要点（2026-09-16 沉淀）

### Jackson 3（tools.jackson）

| 变更点 | 说明 |
|--------|------|
| 定制入口 | `org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer`（`customize(JsonMapper.Builder)`），`spring.jackson.*` 仍走 `JacksonProperties`（同包） |
| 实体注解 | `@JsonSerialize` 等 databind 注解 → `tools.jackson.databind.annotation`；`@JsonFormat/@JsonIgnore/@JsonInclude` 等**纯注解留在 `com.fasterxml.jackson.annotation`**（jackson-annotations 2.20，Jackson 2/3 共用，不要改） |
| 自定义序列化器 | `JsonSerializer` → `ValueSerializer<T>`；`SerializerProvider` → `SerializationContext`；`NumberSerializer` 在 `tools.jackson.databind.ser.jdk` |
| LocalDateTime | 内置于 `tools.jackson.databind.ext.javatime.ser/deser` |
| 异常 | 读写**不再抛受检异常**，`JsonProcessingException` catch 和 `@SneakyThrows` 全部删除 |
| 入口类 | `ObjectMapper` → `JsonMapper`（`JsonMapper.builder().build()`） |
| **属性名推导变了**：字段首字母大写（如 `LevelList`/`FeedbackContent`，历史遗留坏命名）时，Jackson 2 按 getter 推导输出小写驼峰 `levelList`，**Jackson 3 直接用字段原名输出 `LevelList`** → 前端读小写属性拿 undefined（导航栏下拉消失的根因，2026-09-16 生产实例）。机制（实验证实）：getter 推导规则两代相同；变的是**字段↔访问器链接**——Jackson 2 弱链接不合并大小写不一致的 private 字段（等于替坏命名兜底），Jackson 3 按忽略大小写合并且属性名取字段名 | 实体字段一律小写驼峰；升级时全局扫 `private\s+[\w.<>\[\],\s]+\s+[A-Z]\w*\s*;` 排雷（static/final 常量除外）。Lombok getter/setter 名不随字段首字母大小写变化，改名对 Java 调用点零影响；MP 驼峰转列名 `i>0` 才加下划线，`FeedbackContent`/`feedbackContent` 同映射 `feedback_content`，Entity 改名安全 |

### MyBatis Plus 3.5.7 → 3.5.17

- **`ServiceImpl`/`IService` 已删除**：Service 一律 `@RequiredArgsConstructor` + final mapper 构造注入（本来就是项目规范）；批量写用 `BaseMapperPlus.insertBatch`
- **`SqlHelper.executeBatch/saveOrUpdateBatch` 回调从 `BiConsumer` 变 `BiFunction`**：lambda 必须 `return sqlSession.update(...)` 返回影响行数
- jsqlparser 拆为独立构件 `mybatis-plus-jsqlparser`，必须显式引入（分页插件依赖）

### Boot 4 / 组件版本坑

| 坑 | 修复 |
|----|------|
| `spring-boot-starter-aop` 已移除 | 直接依赖 `aspectjweaver`（Boot 4 parent 不管版本，自 pin 1.9.25） |
| `RedisProperties` 改名 `DataRedisProperties`（`org.springframework.boot.data.redis.autoconfigure`） | 改 import 即可，getter 不变 |
| `CachingConfigurerSupport` 已移除（Spring 7） | 删除 extends |
| Redisson 4.x 把 `CacheConfig/RedissonSpringCacheManager` 拆到 `redisson-spring-cache`，且 starter 里是 **optional** 不传递 | 显式加依赖 |
| Sa-Token 1.46 `SaTokenListener`：`doLogin` 第 4 参 `SaLoginModel`→`SaLoginParameter`（`stp.parameter` 包）；`doRenewTimeout` 新增 tokenValue 参数 | 按接口新签名 override |
| 启动器坐标全部换 boot4 版 | sa-token-spring-boot4-starter / mybatis-plus-spring-boot4-starter / druid-spring-boot-4-starter / dynamic-datasource-spring-boot4-starter |
| **Boot 4 的 `spring-boot-starter-data-redis` 不再携带 lettuce-core** | starter 只剩 spring-boot-starter + spring-boot-data-redis 两构件；需要 Lettuce 客户端必须显式加 `io.lettuce:lettuce-core`（版本 Boot parent 管理） |
| **Redisson 4.7.0 适配层不兼容 spring-data-redis 4.x 的 `Expiration.keepTtl()`** | 适配层把 KeepTtl 哨兵翻译成 `PX -2000` → `ERR invalid expire time in set`；已在 application.yml 排除 `RedissonAutoConfigurationV4`，spring-data-redis（Sa-Token dao、CacheController）走 Boot 原生 Lettuce 装配；RedissonClient 是手动 @Bean，RedisUtils/缓存/锁不受影响 |
| logback 1.5.2x：`SizeAndTimeBasedFNATP` 废弃告警 | `logback.xml` 用 `SizeAndTimeBasedRollingPolicy` 合并时间+大小滚动（`FileNamePattern` 必须含 `%i`） |
| Redisson 4.x：server config 上 `setPassword` 废弃告警 | 统一在 `Config.setPassword(...)` 设置 |
| **父 pom 用 `${revision}`（CI-friendly）**：单独 `-pl 模块` 构建会解析不了兄弟模块的 `${revision}` 父版本（Bad Request 400） | 必须整仓构建：`mvn package`（不带 `-pl`） |
| **Spring AI 2.0 openai 模块 base-url 必须带 `/v1`**：底层 OpenAI 官方 SDK 直接拼 `/chat/completions` 不自动补版本路径，少 `/v1` 报 `404: Unknown`（路径 404，非鉴权问题） | base-url 写全版本路径（如 `https://dashscope.aliyuncs.com/compatible-mode/v1`）；curl 带/不带 `/v1` 二分验证 |

---

## Sa-Token 1.37 → 1.46 认证体系（2026-09-16 登录故障全案）

### 版本变化坑（升级必踩清单）

| # | 坑 | 修复 |
|---|-----|------|
| 1 | loginId 冒号校验：1.46 `checkLoginArgs` 默认禁止冒号，项目 loginId 为 `userType:userId` | `sa-token.allow-login-id-colon: true`（application.yml） |
| 2 | `sa-token-redis-jackson` 变空壳聚合包（0 class） | 换坐标 `sa-token-redis-template`（dao）+ starter 自带 jackson3（序列化 SPI，先装者胜） |
| 3 | SaSession 结构变化（tokenSignList→terminalList、TokenSign→SaTerminalInfo），1.37 写入的旧 JSON 带 `@class: SaSessionForJacksonCustomized`（类已删）→ `InvalidTypeIdException`（登录也炸：login 需读 Account-Session 挂终端） | **旧登录态不可恢复**，升级后清空 Redis 中 `Authorization:*` key（token-name 为前缀），全员重新登录；**每套环境的 Redis 独立清**（本地清了≠生产清了，发版后立即执行，无需重启应用） |
| 4 | 官方 dao `setStringAndKeepTTL` 用 `SET KEEPTTL XX`：Redisson 适配层报 invalid expire time（见上表），Redis < 6.0 报 syntax error（本地 3.2） | 自定义 `SaTokenDaoRedisCompat`（qixidi-auth/config）：读 TTL → 正数 PX 写入，兼容所有版本；配套 exclude `cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate`（官方装配无 @ConditionalOnMissingBean 守卫，不排除则 bean 歧义） |
| 5 | jackson3 序列化器带**反序列化类型白名单**：SaSession dataMap 里的自定义类型读回报"无法反序列化的类型…注册到 JSON 全局类型白名单" | `qixidi-auth/src/main/resources/META-INF/satoken/sa-json-type.list` 列出类名（每行一个，# 注释；`SaJsonStrategy` 初始化前经 ClassLoader 读取）。新增存入 session 的类型必须同步登记 |

### 认证链路关键位置

- loginId 格式：`{userType}:{userId}`（`LoginHelper.JOIN_CODE`），前台 userType=`tripartite_user`
- 登录：POST `/oauth/frontDesk/login`（username/password Base64）→ SM3 摘要比对 → `LoginHelper.tripartiteLoginByDevice` → `StpUtil.login`
- session 自定义类型入口：`LoginHelper.setLoginUser/setTripartiteUser`（往 token-session 塞 LoginUser/TripartiteUser）
- Redis key 前缀 = token-name（`Authorization:`）；本机 Redis db1 与 guanmai 项目共用，**只能动 `Authorization:*`，绝不能动 `token:*`**

### 诊断方法论（本案沉淀）

- 三层故障逐层显形：冒号校验（配置）→ 旧数据反序列化（清库）→ KEEPTTL 兼容（环境）——**每修一层，错误位置会后移一层，别把新报错当回归**
- Redis 报错先看命令真身：`invalid expire time`（PX 负数）≠ `syntax error`（语法不支持）≠ 反序列化异常（白名单/类型缺失），三者根因完全不同
- 用 `javap -c` 反汇编依赖 jar 确认装配条件（如官方 dao 无 @ConditionalOnMissingBean）与命令构造（KEEPTTL/XX），不猜

### 升级方法论

1. **用 `javap`/`unzip -l` 查本地仓库 jar 确认 API 签名**，不猜（如 `javap -cp xxx.jar 类名`），一遍过
2. **最终验证必须 `mvn clean compile`**：增量编译会复用旧 target class 造成"SUCCESS 假阳性"（light-redission 曾带旧包名编译"通过"）
3. 报错信息里的"候选方法"列表就是权威签名来源
4. 冒烟验证链路：`/white/site/info`（MySQL+Jackson3 格式化）、`/white/site/friend-link`（MP 分页）、未登录访问受保护接口（Sa-Token 拦截）

---

## MVC 三层架构

### 分层职责

| 层级 | 职责 | 禁止 |
|------|------|------|
| **Controller** | HTTP 请求响应、参数校验、权限控制 | 直接调用 Mapper、包含复杂业务逻辑 |
| **Service** | 业务逻辑处理、事务控制 | 直接操作 HTTP 请求/响应 |
| **Mapper** | 数据访问、SQL 操作 | - |

### 目录结构

**⚠️ 路径变更**：所有后端代码位于 `qixidi-service` 文件夹

```
qixidi-service/qixidi-business/
├── api/
│   ├── backstage/    # 后台接口
│   └── frontDesk/    # 前台接口
├── service/          # Service 层
├── mapper/           # Mapper 层
└── domain/
    ├── entity/       # 数据库实体
    ├── bo/           # Business Object（接收参数）
    ├── vo/           # View Object（返回数据）
    └── enums/        # 枚举
```

### 绝对禁止的跨层调用

| 调用层级 | ❌ 禁止 | ✅ 正确 |
|---------|--------|--------|
| Controller | 直接调用 Mapper | 调用 Service，由 Service 调用 Mapper |
| Controller | 写 `lambdaQuery()` | 在 Service 或 Mapper 中实现 |
| Controller | 包含复杂业务逻辑 | 业务逻辑放在 Service 层 |

---

## 实体类规范

### Lombok 注解

必须同时使用 `@Data` 和 `@NoArgsConstructor`：

```java
@Data
@NoArgsConstructor
public class DataEntity {
    private Long id;
    private String title;
}
```

### Entity、BO、VO 区分

| 类型 | 用途 | 位置 |
|------|------|------|
| **Entity** | 数据库实体 | domain/entity |
| **BO** | 接收参数 | domain/bo |
| **VO** | 返回数据 | domain/vo |

---

## 数据库操作规范

### 禁止在循环中调用数据库

```java
// ❌ 错误
for (Long id : ids) {
    Entity entity = mapper.selectById(id);
}

// ✅ 正确
List<Entity> entities = mapper.selectBatchIds(ids);
```

### MyBatis Plus 使用

```java
// lambdaQuery
List<Entity> list = mapper.lambdaQuery()
    .eq(Entity::getStatus, 1)
    .like(Entity::getTitle, keyword)
    .list();

// lambdaUpdate
mapper.lambdaUpdate()
    .eq(Entity::getId, id)
    .set(Entity::getStatus, 2)
    .update();
```

### Mapper 继承

三泛型 `<M, T, V>`：M=Mapper 自身、T=实体、V=默认 VO（MP 3.5.17 起 `ServiceImpl/IService` 已删除，勿再引入）：

```java
@Mapper
public interface DataMapper extends BaseMapperPlus<DataMapper, DataEntity, DataVo> {
    // 自定义方法
}
```

### 数据模型坑（统计/聚合前必读）

- **b_browsing_history 按（uid+target）去重合并**：`insertOrUpdates` 用 `ON DUPLICATE KEY UPDATE update_time`，重复浏览不新增行、只刷新 update_time。**按天做浏览趋势必须聚合 `update_time`**（聚合 create_time 会漏掉所有回访）；代价是"同一访问者同一天多次浏览同一内容只计 1 次"
- **用户统计走 `UserCountQueryHelper`**（service/count）：新增统计字段时三处同步——`CountUserWebsiteVo` 加字段 → 对应 Mapper 加 `selectXxxCountByUserIds`（XML，resultType=CountUserWebsiteVo）→ helper 加方法**并在 `allCounts()` 里 merge**（fabulousCount 曾漏 merge，前端永远显示 0）
- **`CensusVo` 是通用时间序列货币**（dateTimes/censusSum/title），趋势类接口直接复用，title 填序列名（如 fabulous/comment）
- **本机 `mysql.exe` CLI 会挂起**（无论 -p 还是 MYSQL_PWD，均卡到超时）：验证 SQL 用"`mvn clean compile` + 启动成功"代替——MyBatis 在启动期绑定 XML 方法签名，`Invalid bound statement` 会启动即炸，起得来就是绑定通过；SQL 列名用现有 XML/entity 交叉核对

### 批量插入

**不继承 `ServiceImpl` 的 Service（`@RequiredArgsConstructor` + final mapper 风格）没有 `saveBatch`**，用 `BaseMapperPlus` 自带的 `insertBatch`：

```java
// ✅ 批量插入（BaseMapperPlus 自带，配合 @Transactional 全成功或全回滚）
mapper.insertBatch(entityList);

// ❌ 禁止在循环中逐条 insert
for (Entity e : list) { mapper.insert(e); }
```

---

## 日志规范

### 关键操作加日志

新增/更新/删除操作必须加 `@Log` 注解：

```java
@Log(module = "数据管理", businessType = BusinessType.INSERT)
public void createData(DataBo bo) { }
```

### 日志级别

| 级别 | 用途 |
|------|------|
| ERROR | 错误日志，需要立即处理 |
| WARN | 警告日志，需要关注 |
| INFO | 关键业务日志 |
| DEBUG | 调试日志 |

---

## 异常处理

```java
// ✅ 使用业务异常
throw new BusinessException("数据不存在");

// ❌ 禁止返回 null
return null;

// ❌ 禁止随意抛出通用异常
throw new RuntimeException("数据不存在");
```

---

## 参数校验

### Validation 注解

```java
@PostMapping("/data")
public Result<Void> create(@Validated @RequestBody DataBo bo) { }
```

### 校验分组

```java
public class DataBo {
    @NotNull(groups = {EditGroup.class})
    private Long id;

    @NotBlank(groups = {AddGroup.class, EditGroup.class})
    private String title;
}
```

---

## 返回值规范

| 类型 | 格式 | 方法 |
|------|------|------|
| 非分页 | `Result.ok(data)` | `api.get/post/put/delete` |
| 分页 | `TableDataInfo` | `api.getPage` |

---

## Controller 规范

| 场景 | 注解 |
|------|------|
| 权限控制 | `@SaCheckPermission("data:query")` |
| 防重复提交 | `@RepeatSubmit` |

---

## 接口对接规范（重要）

**前端开发时**：当用户提到服务端接口路径时，必须先去 `qixidi-service` 项目查看接口定义，确保：

1. **查看 Controller 层**：确认参数格式、返回格式
2. **参数接收方式**：
   - `@RequestParam` → URL 参数
   - `@RequestBody` → JSON Body
   - `@RequestPart` → FormData（文件上传）
3. **返回格式**：
   - `Result.ok(data)` → `{ code, msg, data }` 格式
   - `TableDataInfo` → 分页格式 `{ total, rows }`
   - 直接返回对象（如 `Map<String, String>`）→ 不被 R 包装

**常见 Controller 位置**：
- 文件上传：`qixidi-system/src/main/java/com/qixidi/system/api/SysOssController.java`
- 文章管理：`qixidi-business/qixidi-article/api/` 下的 Controller
- 用户管理：`qixidi-system/src/main/java/com/qixidi/system/api/` 下的 Controller

**查找接口命令**：
```bash
# 在 qixidi-service 根目录执行
grep -r "@PostMapping.*接口路径" --include="*.java"
```

---

## 代码质量检查清单

- [ ] 是否在循环中调用数据库？
- [ ] Controller 是否直接调用了 Mapper？
- [ ] Controller 是否包含复杂业务逻辑？
- [ ] 实体类是否同时使用了 @Data 和 @NoArgsConstructor？
- [ ] 新增/更新/删除是否加了 @Log 注解？
- [ ] 是否混淆了 Entity、BO、VO？

---

**核心原则**：严格遵守 MVC 三层架构，保持代码简洁，优先复用现有方法。
