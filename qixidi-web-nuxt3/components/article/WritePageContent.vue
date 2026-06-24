<template>
  <div class="write-page">
    <!-- 主内容区：分栏布局 -->
    <div class="main-content">
      <!-- 左侧：编辑器 -->
      <div class="editor-area">
        <ArticleEditor
          ref="editorRef"
          :initial-data="initialArticle"
          :groupings="groupings"
          :labels="labels"
          :specials="specials"
        />
      </div>

      <!-- 右侧：配置面板 -->
      <div class="config-panel">
        <!-- 配置内容（可滚动） -->
        <div class="config-content">
          <div class="config-header">
            <h3 class="config-title">发布设置</h3>
            <n-button text @click="handleBack" class="back-link">
              <template #icon>
                <ArrowLeft class="icon-small" />
              </template>
              返回
            </n-button>
          </div>

          <!-- 最近文章（可折叠） -->
          <div class="config-section">
            <div class="section-header-clickable" @click="showRecentArticles = !showRecentArticles">
              <div class="section-header-left">
                <label class="section-label">最近文章</label>
                <!-- 状态筛选按钮 -->
                <div class="status-filters">
                  <n-button
                    size="small"
                    :type="articleStatusFilter === 4 ? 'primary' : 'default'"
                    @click.stop="handleStatusFilter(4)"
                  >
                    草稿
                  </n-button>
                  <n-button
                    size="small"
                    :type="articleStatusFilter === 2 ? 'primary' : 'default'"
                    @click.stop="handleStatusFilter(2)"
                  >
                    已发布
                  </n-button>
                  <n-button
                    size="small"
                    :type="articleStatusFilter === 1 ? 'primary' : 'default'"
                    @click.stop="handleStatusFilter(1)"
                  >
                    审核中
                  </n-button>
                  <n-button
                    size="small"
                    :type="articleStatusFilter === 3 ? 'primary' : 'default'"
                    @click.stop="handleStatusFilter(3)"
                  >
                    未通过
                  </n-button>
                </div>
              </div>
              <n-icon size="16" :style="{ transform: showRecentArticles ? 'rotate(180deg)' : 'rotate(0deg)' }">
                <ChevronDown />
              </n-icon>
            </div>
            <n-collapse-transition>
              <div v-show="showRecentArticles" class="recent-articles">
                <div
                  v-for="article in recentArticles"
                  :key="article.id"
                  class="recent-article-item"
                  @click="handleEditArticle(article.id)"
                >
                  <div class="article-title-text">{{ article.articleTitle }}</div>
                  <div class="article-meta">
                    <span>{{ formatDate(article.createTime) }}</span>
                    <span>{{ article.viewCount || 0 }} 阅读</span>
                  </div>
                </div>
                <n-button v-if="recentArticles.length === 0" text disabled style="width: 100%; justify-content: center;">
                  暂无文章
                </n-button>
              </div>
            </n-collapse-transition>
          </div>

          <!-- 封面上传 -->
          <div class="config-section">
            <label class="section-label">封面图</label>
            <ImageUpload
              v-model="formData.articleCover"
              :max-size="5 * 1024 * 1024"
            />
            <p class="section-hint">推荐尺寸 16:9，最大 5MB</p>
          </div>

          <!-- 分类选择（卡片式） -->
          <div class="config-section">
            <label class="section-label">分类 <span class="required">*</span></label>
            <div class="category-grid">
              <div
                v-for="category in groupings"
                :key="category.id"
                :class="['category-card', { active: formData.groupingId === category.id }]"
                @click="handleSelectCategory(category.id)"
              >
                {{ category.groupingName }}
              </div>
            </div>
          </div>

          <!-- 摘要 + AI生成 -->
          <div class="config-section">
            <div class="section-label-row">
              <label class="section-label">摘要</label>
              <n-switch
                v-model:value="formData.abstractSelect"
                size="small"
              >
                <template #checked>AI</template>
                <template #unchecked>手动</template>
              </n-switch>
            </div>
            <n-input
              v-if="!formData.abstractSelect"
              v-model:value="formData.articleAbstract"
              type="textarea"
              placeholder="简要描述文章内容..."
              :rows="5"
              :maxlength="200"
              show-count
            />
            <div v-else class="ai-hint-text">
              AI 将根据文章内容自动生成摘要
            </div>
          </div>

          <!-- 标签选择 -->
          <div class="config-section">
            <label class="section-label">标签 <span class="required">*</span></label>
            <n-select
              v-model:value="selectedLabelIds"
              :options="labelOptions"
              placeholder="选择标签（最多3个）"
              multiple
              :max-tag-count="3"
              filterable
              @update:value="handleLabelChange"
            />
          </div>

          <!-- 专栏选择 -->
          <div class="config-section">
            <label class="section-label">专栏</label>
            <n-select
              v-model:value="formData.specialId"
              :options="specialOptions"
              placeholder="选择专栏（可选）"
              clearable
              filterable
            />
          </div>

          <!-- 文章类型 -->
          <div class="config-section">
            <label class="section-label">类型</label>
            <n-button-group>
              <n-button
                :type="localArticleType === 1 ? 'primary' : 'default'"
                @click="localArticleType = 1"
              >
                原创
              </n-button>
              <n-button
                :type="localArticleType === 2 ? 'primary' : 'default'"
                @click="localArticleType = 2"
              >
                转载
              </n-button>
              <n-button
                :type="localArticleType === 3 ? 'primary' : 'default'"
                @click="localArticleType = 3"
              >
                翻译
              </n-button>
            </n-button-group>

            <!-- 转载/翻译地址 -->
            <n-input
              v-if="localArticleType === 2 || localArticleType === 3"
              v-model:value="formData.reprintUrl"
              type="text"
              placeholder="请输入原文链接"
              style="margin-top: 8px"
            />
          </div>
        </div>

        <!-- 底部操作按钮（固定） -->
        <div class="config-footer">
          <n-button @click="handleQuickSaveDraft" :disabled="isSubmitting" size="medium">
            保存草稿
          </n-button>
          <n-button
            type="primary"
            @click="handlePublish"
            :disabled="isSubmitting"
            :loading="isSubmitting"
            size="medium"
          >
            {{ buttonText }}
          </n-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { ArrowLeft, ChevronDown } from '@vicons/tabler'
import type { ArticleForm, GroupingInfo, LabelInfo, SpecialInfo } from '~/types'

interface Props {
  articleId?: string  // 新增：文章 ID（用于编辑模式）
  groupings: GroupingInfo[]
  labels: LabelInfo[]
  specials: SpecialInfo[]
}

const props = withDefaults(defineProps<Props>(), {
  articleId: undefined,
  groupings: () => [],
  labels: () => [],
  specials: () => []
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const articleApi = useArticleApi()

const editorRef = ref<InstanceType<typeof ArticleEditor>>()
const isSubmitting = ref(false)

// 表单数据（引用编辑器的 form）
const formData = computed(() => editorRef.value?.form || ({} as ArticleForm))

// 最近文章
const showRecentArticles = ref(false)
const recentArticles = ref<any[]>([])

// 最近文章状态筛选（默认草稿）
const articleStatusFilter = ref<number>(4) // 1=审核中, 2=审核通过, 3=审核不通过, 4=草稿

// 文章类型本地状态（用于类型切换）
const localArticleType = ref<1 | 2 | 3>(1)

// 同步 type 到 form
watch(() => formData.value.type, (newVal) => {
  if (newVal) {
    localArticleType.value = newVal
  }
}, { immediate: true })

// 同步 localArticleType 到 form（确保 form 存在）
watch(localArticleType, (newVal) => {
  const form = formData.value
  if (form && Object.keys(form).length > 0) {
    form.type = newVal
  }
}, { immediate: true })

// 确保编辑器加载后 type 被正确设置
onMounted(() => {
  nextTick(() => {
    const form = formData.value
    if (form && !form.type) {
      form.type = localArticleType.value
    }
  })
})

// 从路由获取文章 ID（优先使用 prop，其次从 route 获取）
const currentArticleId = computed(() => props.articleId || route.params.id as string | undefined)

// 加载文章数据（如果提供了 ID）
const { data: articleDetail } = await useAsyncData(
  computed(() => currentArticleId.value ? `article-edit-${currentArticleId.value}` : 'article-edit-new'),
  () => currentArticleId.value ? articleApi.getDetail(currentArticleId.value) : Promise.resolve(null)
)

// 准备初始文章数据
const initialArticle = computed(() => {
  if (!articleDetail.value) return {}
  const data = articleDetail.value
  return {
    id: data.id,
    articleTitle: data.articleTitle || '',
    articleContent: data.articleContent || '',
    articleCover: data.articleCover || '',
    articleAbstract: data.articleAbstract || '',
    type: data.type || 1,
    reprintUrl: data.reprintUrl || '',
    groupingId: data.groupingId,
    labelIds: data.labelIds || [],
    specialId: data.specialId,
    abstractSelect: data.abstractSelect !== undefined
      ? data.abstractSelect
      : !(data.articleAbstract && data.articleAbstract.trim()) // 摘要有内容则不打开 AI，为空才默认打开
  }
})

// 标签选择（直接从已加载的 initialArticle 初始化，不依赖 watch）
const selectedLabelIds = ref<number[]>(initialArticle.value?.labelIds || [])

// 分类选项
const groupingOptions = computed(() =>
  props.groupings.map(item => ({
    label: item.groupingName,
    value: item.id
  }))
)

// 标签选项
const labelOptions = computed(() =>
  props.labels.map(item => ({
    label: item.labelName,
    value: item.id
  }))
)

// 专栏选项
const specialOptions = computed(() =>
  props.specials.map(item => ({
    label: item.specialName,
    value: item.id
  }))
)

// 是否为编辑模式
const isEdit = computed(() => !!currentArticleId.value)

// 按钮文本
const buttonText = computed(() => isEdit.value ? '更新文章' : '发布文章')

// 获取最近文章
const fetchRecentArticles = async () => {
  try {
    const params: any = {
      pageNum: 1,
      pageSize: 5
    }
    // 只有选择了特定状态时才传 auditState
    if (articleStatusFilter.value !== undefined) {
      params.auditState = articleStatusFilter.value
    }
    const result = await articleApi.getLatelyArticleList(params)
    recentArticles.value = result.rows || []
  } catch (error) {
    console.error('获取最近文章失败:', error)
  }
}

// 格式化日期
const formatDate = (timestamp: number) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days === 2) return '前天'
  if (days < 7) return `${days}天前`

  return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

// 编辑文章（导航到编辑路由）
const handleEditArticle = (id: number) => {
  navigateTo(`/write/article/${id}`)
}

// 处理状态筛选
const handleStatusFilter = (status: number) => {
  articleStatusFilter.value = status
  fetchRecentArticles()
}

// 组件挂载时获取最近文章
onMounted(() => {
  fetchRecentArticles()
})

// 选择分类
const handleSelectCategory = (categoryId: number) => {
  const form = formData.value
  if (form?.groupingId === categoryId) {
    form.groupingId = undefined
  } else {
    form.groupingId = categoryId
  }
}

// 标签选择限制（最多3个）
const handleLabelChange = (value: number[]) => {
  if (value.length > 3) {
    selectedLabelIds.value = value.slice(0, 3)
    message.warning('最多只能选择3个标签')
  } else {
    selectedLabelIds.value = value
  }
}

// 快速保存草稿（不打开抽屉）
const handleQuickSaveDraft = async () => {
  const form = formData.value
  if (!form?.articleTitle?.trim()) {
    message.warning('请先输入文章标题')
    return
  }

  try {
    form.status = 0
    form.labelIds = selectedLabelIds.value
    const result = await articleApi.saveDraft(form)

    // 保存返回的 ID 到表单，后续操作都是更新而非新增
    if (result?.id) {
      form.id = result.id
    }

    message.success('草稿保存成功')
  } catch {
  }
}

// 表单验证
const validateForm = (): boolean => {
  const form = formData.value
  if (!form?.articleTitle?.trim()) {
    message.error('请输入文章标题')
    return false
  }
  if (!form?.articleContent?.trim()) {
    message.error('请输入文章内容')
    return false
  }
  if (!form?.groupingId) {
    message.error('请选择分类')
    return false
  }
  if (!selectedLabelIds.value || selectedLabelIds.value.length === 0) {
    message.error('请选择至少一个标签')
    return false
  }
  return true
}

// 发布文章
const handlePublish = async () => {
  // 发布前强制同步 type（确保在验证前同步）
  const form = formData.value
  if (form && localArticleType.value) {
    form.type = localArticleType.value
  }

  if (!validateForm()) return

  dialog.success({
    title: '确认发布',
    content: isEdit.value ? '确定要更新这篇文章吗？' : '确定要发布这篇文章吗？发布后将对所有用户可见。',
    positiveText: isEdit.value ? '更新' : '发布',
    negativeText: '继续编辑',
    onPositiveClick: async () => {
      isSubmitting.value = true
      try {
        const form = formData.value
        form.status = 1
        form.labelIds = selectedLabelIds.value

        await articleApi.insertArticle(form)
        message.success(isEdit.value ? '文章更新成功！' : '文章发布成功！')
        setTimeout(() => {
          if (isEdit.value && form.id) {
            navigateTo(`/articles/${form.id}`)
          } else {
            navigateTo('/')
          }
        }, 500)
      } catch {
      } finally {
        isSubmitting.value = false
      }
    }
  })
}

// 返回
const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.write-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  overflow: hidden;
}

/* 主内容区：分栏布局 */
.main-content {
  flex: 1;
  display: flex;
  overflow: hidden;
  min-height: 0;
}

/* 左侧编辑器 */
.editor-area {
  flex: 7;
  min-width: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 右侧配置面板 */
.config-panel {
  flex: 3;
  min-width: 320px;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
  overflow: hidden;
}

.config-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 配置面板头部 */
.config-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border-light);
}

.config-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.back-link {
  font-size: 13px;
  color: var(--color-ink-light);
}

.back-link:hover {
  color: var(--color-primary);
}

.icon-small {
  width: 16px;
  height: 16px;
  stroke-width: 1.5;
}

/* 配置区块 */
.config-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.section-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink);
  margin-bottom: 0;
}

.section-hint {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin: 0;
  line-height: 1.4;
}

.required {
  color: var(--color-danger);
  margin-left: 2px;
}

/* 可点击的区块头部 */
.section-header-clickable {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  user-select: none;
}

.section-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.section-header-clickable .section-label {
  cursor: pointer;
  margin-bottom: 0;
}

/* 状态筛选按钮 */
.status-filters {
  display: flex;
  gap: 6px;
}

.status-filters .n-button {
  padding: 6px 12px;
  font-size: 13px;
  height: 26px;
}

/* 最近文章列表 */
.recent-articles {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 8px;
}

.recent-article-item {
  padding: 10px 12px;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-base);
}

.recent-article-item:hover {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.article-title-text {
  font-size: 13px;
  color: var(--color-ink);
  line-height: 1.4;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: var(--color-ink-muted);
}

.recent-article-item:hover .article-title-text {
  color: var(--color-primary);
}

/* AI 提示文字 */
.ai-hint-text {
  font-size: 13px;
  color: var(--color-primary);
  padding: 12px;
  background: var(--color-primary-light);
  border-radius: var(--radius-sm);
  line-height: 1.5;
}

/* 摘要区域头部 */
.section-label-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-label-row .section-label {
  margin-bottom: 0;
}

/* 文章类型按钮组 */
:deep(.n-button-group .n-button) {
  flex: 1;
  border-radius: 0 !important;
}

:deep(.n-button-group .n-button:first-child) {
  border-top-left-radius: var(--radius-sm) !important;
  border-bottom-left-radius: var(--radius-sm) !important;
}

:deep(.n-button-group .n-button:last-child) {
  border-top-right-radius: var(--radius-sm) !important;
  border-bottom-right-radius: var(--radius-sm) !important;
}

:deep(.n-button-group .n-button:not(:last-child)) {
  border-right: none;
}

/* 分类卡片网格 - 3列 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.category-card {
  padding: 12px 8px;
  text-align: center;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-base);
  font-size: 13px;
  color: var(--color-ink);
  user-select: none;
}

.category-card:hover {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.category-card.active {
  border-color: var(--color-primary);
  background: var(--color-primary);
  color: var(--color-surface);
  font-weight: 500;
}

/* 底部操作按钮（固定） */
.config-footer {
  display: flex;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid var(--color-border-light);
  background: var(--color-surface);
  flex-shrink: 0;
}

.config-footer .n-button {
  flex: 1;
}

/* 响应式 */
@media (max-width: 1024px) {
  .config-panel {
    display: none;
  }

  .editor-area {
    flex: 1;
  }
}
</style>
