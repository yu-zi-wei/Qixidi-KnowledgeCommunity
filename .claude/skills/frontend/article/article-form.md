# 文章编辑发布模块

栖息地博客文章编辑/发布模块的开发规范和常见问题。

---

## 接口信息

### 后端 Controller
- **文件位置**：`qixidi-service-plus/qixidi-business/src/main/java/com/qixidi/business/api/frontDesk/article/UserArticleController.java`

### 主要接口

#### 1. 获取文章详情（编辑用）
```java
@GetMapping("/user/get/article/{id}")
public Result<ArticleInformation> getArticle(@PathVariable Long id)
```

**返回数据结构**：
```json
{
  "id": 78,
  "articleTitle": "文章标题",
  "articleContent": "Markdown 内容",
  "articleCover": "封面图URL",
  "articleAbstract": "摘要",
  "type": 1,  // 1:原创 2:转载 3:翻译
  "reprintUrl": "转载地址",
  "groupingId": 1,
  "labelId": "1,2,3",  // 注意：逗号分隔的字符串！
  "specialId": 1,
  "abstractSelect": true  // AI生成摘要
}
```

#### 2. 新增/更新文章
```java
@PostMapping("/user/insert/article")
public Result<Void> insertArticle(@RequestBody ArticleInformationBo bo)
```

**请求数据结构（BO）**：
```java
// ArticleInformationBo
private String articleTitle;      // 必填
private String articleContent;     // 必填
private String articleCover;
private String articleAbstract;
private Integer type;              // 必填：1原创 2转载 3翻译
private String reprintUrl;
private Long groupingId;           // 必填
private String labelId;            // 必填：逗号分隔的字符串，如 "1,2,3"
private Long specialId;
private Boolean abstractSelect;
```

#### 3. 保存草稿
```java
@PutMapping("/user/save/draft")
public Result<Void> saveDraft(@RequestBody ArticleInformationBo bo)
```

---

## 字段映射表（⚠️ 重要）

| 前端字段 | 后端字段 | 前端类型 | 后端类型 | 转换规则 |
|---------|---------|---------|---------|---------|
| `labelIds` | `labelId` | `number[]` | `String` | 数组转逗号分隔字符串 |
| `groupingId` | `groupingId` | `number` | `Long` | 直接映射 |
| `specialId` | `specialId` | `number` | `Long` | 直接映射 |
| `abstractSelect` | `abstractSelect` | `boolean` | `Boolean` | 直接映射 |
| `type` | `type` | `1\|2\|3` | `Integer` | 直接映射 |

**关键转换逻辑**：
```typescript
// 提交时：数组 → 字符串
labelIds: [1, 2, 3] → labelId: "1,2,3"

// 获取时：字符串 → 数组
labelId: "1,2,3" → labelIds: [1, 2, 3]
```

---

## 前端实现

### API 层转换（composables/useArticleApi.ts）

```typescript
export const useArticleApi = () => {
  const api = useApi()

  // 获取文章详情
  const getDetail = async (id: string | number): Promise<ArticleInfo> => {
    const data = await api.get<any>(`/user/get/article/${id}`)
    // 后端的 labelId（字符串）→ 前端的 labelIds（数组）
    if (data && data.labelId) {
      data.labelIds = data.labelId.split(',').map(Number)
      delete data.labelId
    }
    return data
  }

  // 提交时的数据转换
  const transformFormData = (data: any) => {
    const transformed = { ...data }
    // 前端的 labelIds（数组）→ 后端的 labelId（字符串）
    if (transformed.labelIds && Array.isArray(transformed.labelIds)) {
      transformed.labelId = transformed.labelIds.join(',')
      delete transformed.labelIds
    }
    return transformed
  }

  // 新增/更新文章
  const insertArticle = (data: any): Promise<ArticleInfo> => {
    const transformedData = transformFormData(data)
    return api.post<ArticleInfo>('/user/insert/article', transformedData)
  }

  // 保存草稿
  const saveDraft = (data: any): Promise<ArticleInfo> => {
    const transformedData = transformFormData(data)
    return api.put<ArticleInfo>('/user/save/draft', transformedData)
  }

  return { getDetail, insertArticle, saveDraft, ... }
}
```

---

## 组件架构

### 页面组件
```
pages/write.vue          → 新建文章
pages/write/[id].vue     → 编辑文章（传递 articleId prop）
```

### 共享组件
```
components/article/WritePageContent.vue
```

**Props**：
```typescript
interface Props {
  articleId?: string      // 编辑模式的文章 ID
  groupings: GroupingInfo[]
  labels: LabelInfo[]
  specials: SpecialInfo[]
}
```

**数据获取**：
```typescript
// 组件内部根据 articleId 决定是否加载文章数据
const currentArticleId = computed(() => props.articleId || route.params.id)

const { data: articleDetail } = await useAsyncData(
  computed(() => currentArticleId.value ? `article-${currentArticleId.value}` : 'article-new'),
  () => currentArticleId.value ? articleApi.getDetail(currentArticleId.value) : Promise.resolve(null)
)
```

### 编辑器组件
```
components/article/ArticleEditor.vue
```

**内部数据结构**（用于 UI 交互）：
```typescript
const form = ref<any>({
  id: undefined,
  articleTitle: '',
  articleContent: '',
  articleCover: '',
  articleAbstract: '',
  abstractSelect: true,
  type: 1,
  reprintUrl: '',
  groupingId: undefined,
  labelIds: [],              // 数组形式，方便 UI 绑定
  specialId: undefined,
  status: 1
})
```

**暴露给父组件**：
```typescript
defineExpose({
  form,                    // 表单数据引用
  isSubmitting,
  hasChanges: () => { ... },
  loadArticle: (article: any) => {
    // 加载文章数据到表单
    form.value.id = article.id
    form.value.articleTitle = article.articleTitle || ''
    // ...
    form.value.labelIds = article.labelIds || []
    form.value.abstractSelect = article.abstractSelect !== undefined ? article.abstractSelect : true
  }
})
```

---

## 常见问题

### 问题 1：标签不回显
**原因**：后端返回 `labelId: "1,2,3"`（字符串），前端 UI 绑定需要 `labelIds: [1, 2, 3]`（数组）

**解决**：在 `getDetail` 中转换
```typescript
if (data.labelId) {
  data.labelIds = data.labelId.split(',').map(Number)
}
```

### 问题 2：提交时提示"请填写标签"
**原因**：前端提交 `labelIds: [1, 2, 3]`（数组），后端期望 `labelId: "1,2,3"`（字符串）

**解决**：在 `insertArticle`/`saveDraft` 中转换
```typescript
if (transformed.labelIds && Array.isArray(transformed.labelIds)) {
  transformed.labelId = transformed.labelIds.join(',')
  delete transformed.labelIds
}
```

### 问题 3：文章类型验证失败
**原因**：后端期望 `type: 1`（整数），前端发送 `type: "1"`（字符串）或其他字段名

**解决**：
1. 前端类型定义：`type?: 1 | 2 | 3`
2. UI 按钮绑定：`:type="localArticleType === 1 ? 'primary' : 'default'"`
3. 点击事件：`@click="localArticleType = 1"`

### 问题 4：AI 摘要开关字段不匹配
**原因**：后端字段是 `abstractSelect`，前端用的是 `aiGenerateAbstract`

**解决**：统一使用 `abstractSelect`（后端字段名）

### 问题 5：切换文章时数据不更新
**原因**：Nuxt 组件复用，`useAsyncData` 的 key 是静态字符串，不会响应路由参数变化

**解决**：
```typescript
// ❌ 错误：静态 key
const { data } = await useAsyncData('article-detail', () => ...)

// ✅ 正确：computed key
const { data } = await useAsyncData(
  computed(() => `article-${articleId.value}`),
  () => articleApi.getDetail(articleId.value)
)
```

---

## 开发检查清单

开发文章相关功能时，必须检查：

- [ ] 前端使用 `labelIds`（数组），提交时转换为 `labelId`（字符串）
- [ ] 前端使用 `type: 1|2|3`（整数），不要使用字符串
- [ ] 前端使用 `abstractSelect`（boolean），不要使用 `aiGenerateAbstract`
- [ ] `useAsyncData` 的 key 必须是 computed，响应路由参数变化
- [ ] 获取详情时将 `labelId` 转换为 `labelIds`
- [ ] 提交时将 `labelIds` 转换为 `labelId`
- [ ] 文章类型按钮绑定 `=== 1`、`=== 2`、`=== 3`
- [ ] AI 摘要开关绑定 `v-model:value="formData.abstractSelect"`

---

## 核心教训

1. **字段名必须一致**：前端字段名应该和后端保持一致（除了需要转换的如 labelIds）
2. **数据类型必须匹配**：后端是 Integer，前端不要用字符串
3. **数组↔字符串转换必须在 API 层处理**：保持组件内部使用数组，API 层负责转换
4. **路由参数变化必须响应**：使用 computed key 或 watch 监听路由变化
5. **接口定义必须先查看后端**：使用 Grep 搜索后端 Controller，确认字段名、类型、注解
