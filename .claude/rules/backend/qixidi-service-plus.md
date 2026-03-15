---
description: 仅在开发 qixidi-service 项目时遵循（qixidi-service-plus 已弃用）
globs: qixidi-service/**, qixidi-service\**
---

# qixidi-service 开发规范

栖息地博客服务端开发规范（qixidi-service-plus 已弃用，所有后端代码已迁移至 qixidi-service）。

---

## ⚠️ 重要变更

| 项目 | 状态 | 说明 |
|------|------|------|
| **qixidi-service-plus** | ❌ 已弃用 | 旧版后端代码，已停止维护 |
| **qixidi-service** | ✅ 当前使用 | 所有后端代码已迁移至此 |

**查找接口**：使用 `grep` 在 `qixidi-service` 文件夹中搜索接口路径

---

## 技术栈

- **框架**：Spring Boot 3.3.2 + Java 17
- **ORM**：MyBatis Plus 3.5.7
- **数据库**：MySQL 8.0
- **缓存**：Redis + Redisson
- **认证**：Sa-Token 1.37.0

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

```java
@Mapper
public interface DataMapper extends BaseMapperPlus<DataEntity> {
    // 自定义方法
}
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

**⚠️ 后端代码位置变更**：后端代码已迁移至 `qixidi-service` 文件夹，`qixidi-service-plus` 已弃用。

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
