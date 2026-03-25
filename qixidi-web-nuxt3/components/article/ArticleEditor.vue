<template>
  <div class="article-editor">
    <!-- 主编辑区 -->
    <div class="editor-main">
      <!-- 标题输入 -->
      <n-input
        v-model:value="form.articleTitle"
        placeholder="请输入文章标题..."
        size="large"
        class="title-input"
        :maxlength="100"
        show-count
        borderless
      />

      <!-- Markdown 编辑器（支持视频上传） -->
      <ClientOnly>
        <CommonMdEditorWithVideo
          ref="editorRef"
          v-model="form.articleContent"
          :toolbars="fullToolbars"
          class="markdown-editor"
          placeholder="开始撰写你的文章..."
        />
        <template #fallback>
          <div class="editor-loading">编辑器加载中...</div>
        </template>
      </ClientOnly>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { ArticleForm, GroupingInfo, LabelInfo, SpecialInfo } from '~/types'

interface Props {
  articleId?: string
  initialData?: Partial<ArticleForm>
  groupings?: GroupingInfo[]
  labels?: LabelInfo[]
  specials?: SpecialInfo[]
}

const props = withDefaults(defineProps<Props>(), {
  groupings: () => [],
  labels: () => [],
  specials: () => []
})

const emit = defineEmits<{
  publish: [data: ArticleForm]
  'save-draft': [data: ArticleForm]
}>()

// 表单数据
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
  labelIds: [],
  specialId: undefined,
  status: 1
})

// 提交状态
const isSubmitting = ref(false)

// 初始化表单数据
if (props.initialData) {
  Object.assign(form.value, props.initialData)
}

// 监听 initialData 变化，用于在同一页面内切换文章时重新加载数据
watch(() => props.initialData, (newData, oldData) => {
  // 检查是否真的有变化（避免重复更新）
  if (!newData) return
  if (newData.id === oldData?.id && newData.id === form.value.id) return

  // 有新数据，更新表单
  if (newData.id) {
    // 编辑模式：加载文章数据
    form.value.id = newData.id
    form.value.articleTitle = newData.articleTitle || ''
    form.value.articleContent = newData.articleContent || ''
    form.value.articleCover = newData.articleCover || ''
    form.value.articleAbstract = newData.articleAbstract || ''
    form.value.type = newData.type || 1
    form.value.reprintUrl = newData.reprintUrl || ''
    form.value.groupingId = newData.groupingId
    form.value.labelIds = newData.labelIds || []
    form.value.specialId = newData.specialId
    form.value.status = newData.status
    form.value.abstractSelect = newData.abstractSelect !== undefined ? newData.abstractSelect : true
  }
}, { deep: true })

// 完整工具栏配置（文章编辑使用）
const fullToolbars = [
  'bold',
  'underline',
  'italic',
  'strikeThrough',
  '-',
  'title',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  '-',
  'codeRow',
  'code',
  'link',
  'image',
  'table',
  '-',
  'revoke',
  'next',
  '=',
  'pageFullscreen',
  'fullscreen',
  'preview',
  'htmlPreview',
  'catalog'
]

// 编辑器引用
const editorRef = ref<InstanceType<typeof CommonMdEditorWithVideo>>()

// 暴露表单数据（用于父组件访问）
defineExpose({
  form,
  isSubmitting,
  hasChanges: () => {
    return !!(form.value.articleTitle || form.value.articleContent)
  },
  loadArticle: (article: any) => {
    // 加载文章数据到表单
    form.value.id = article.id
    form.value.articleTitle = article.articleTitle || ''
    form.value.articleContent = article.articleContent || ''
    form.value.articleCover = article.articleCover || ''
    form.value.articleAbstract = article.articleAbstract || ''
    form.value.groupingId = article.groupingId
    form.value.labelIds = article.labelIds || []
    form.value.specialId = article.specialId
    form.value.type = article.type || 1
    form.value.reprintUrl = article.reprintUrl || ''
    form.value.status = article.status
    // abstractSelect 如果有值就用，没有值默认 true
    form.value.abstractSelect = article.abstractSelect !== undefined ? article.abstractSelect : true
  }
})
</script>

<style scoped>
.article-editor {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 主编辑区 */
.editor-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
  overflow: hidden; /* 防止内容溢出 */
}

/* 标题输入 - 适中字号 */
.title-input {
  font-size: 28px;
  font-weight: 600;
  line-height: 1.4;
  flex-shrink: 0; /* 不收缩 */
}

.title-input :deep(.n-input__input) {
  font-size: 28px;
  font-weight: 600;
  line-height: 1.4;
  padding: var(--space-3) 0 !important;
}

.title-input :deep(.n-input__border) {
  display: none !important;
}

.title-input :deep(.n-input__state-border) {
  display: none !important;
}

.markdown-editor {
  flex: 1;
  min-height: 0; /* 允许 flex 子元素收缩 */
}

.editor-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
}

/* 响应式 */
@media (max-width: 768px) {
  .title-input,
  .title-input :deep(.n-input__input) {
    font-size: 24px;
  }
}
</style>
