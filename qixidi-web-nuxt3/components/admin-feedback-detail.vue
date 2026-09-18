<template>
  <n-modal
    :show="show"
    preset="card"
    class="feedback-detail-modal"
    :bordered="false"
    @update:show="(v: boolean) => !v && emit('close')"
  >
    <template #header>
      <div class="detail-header">
        <span class="detail-title">{{ feedback?.feedbackTitle || '反馈详情' }}</span>
        <span v-if="feedback" class="detail-status" :class="`status-${feedback.status}`">
          {{ statusText(feedback.status) }}
        </span>
      </div>
    </template>

    <template #default>
      <div v-if="feedback" class="detail-body">
        <div class="detail-meta">
          <span class="detail-nickname">{{ feedback.nickname || '匿名用户' }}</span>
          <time class="detail-time" :title="getFullDateTime(feedback.createTime)">
            {{ formatTime(feedback.createTime) }}
          </time>
          <span v-if="feedback.updateName" class="detail-handler">
            {{ feedback.updateName }} 处理
          </span>
        </div>

        <div class="detail-content">
          <div v-if="rendering" class="detail-rendering">
            <n-skeleton :sharp="false" height="18px" />
            <n-skeleton :sharp="false" height="18px" width="70%" />
          </div>
          <MarkdownRenderer v-else :html="renderedHtml" />
        </div>
      </div>
    </template>

    <template v-if="feedback" #action>
      <div class="detail-actions">
        <n-popconfirm v-if="isMine" @positive-click="handleDelete">
          <template #trigger>
            <n-button size="small" quaternary type="error">删除</n-button>
          </template>
          确定删除这条反馈吗？
        </n-popconfirm>

        <n-dropdown
          v-if="isAdmin"
          trigger="click"
          :options="statusOptions"
          @select="(status: number) => handleStatusChange(status)"
        >
          <n-button size="small" type="primary" ghost :loading="changing">
            更改状态
          </n-button>
        </n-dropdown>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { FeedbackStatusOptions, type FeedbackVo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const props = defineProps<{
  show: boolean
  feedback: FeedbackVo | null
}>()

const emit = defineEmits<{
  close: []
  updated: [feedback: FeedbackVo]
  deleted: [feedback: FeedbackVo]
}>()

const authStore = useAuthStore()
const feedbackApi = useFeedbackApi()
const message = useMessage()

const isAdmin = computed(() => authStore.user?.roleId === 3)
const isMine = computed(() => !!authStore.user?.uuid && props.feedback?.uid === authStore.user.uuid)

const statusOptions = FeedbackStatusOptions.map(o => ({ label: o.label, key: o.value }))
const statusText = (status: number) => FeedbackStatusOptions.find(o => o.value === status)?.label || '未知'

// Markdown 渲染（unified 管道含 sanitize）
const { renderMarkdown } = useMarkdown()
const renderedHtml = ref('')
const rendering = ref(false)

watch(() => props.show, async (visible) => {
  if (!visible) return
  rendering.value = true
  renderedHtml.value = ''
  try {
    renderedHtml.value = await renderMarkdown(props.feedback?.feedbackContent || '')
  } finally {
    rendering.value = false
  }
})

const changing = ref(false)

const handleStatusChange = async (status: number) => {
  if (!props.feedback || status === props.feedback.status) return
  changing.value = true
  try {
    await feedbackApi.updateStatus(props.feedback.id, status)
    message.success(`已标记为「${statusText(status)}」`)
    emit('updated', { ...props.feedback, status })
  } finally {
    changing.value = false
  }
}

const handleDelete = async () => {
  if (!props.feedback) return
  await feedbackApi.deleteFeedback(props.feedback.id)
  message.success('已删除')
  emit('deleted', props.feedback)
}
</script>

<style scoped>
.detail-header {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.detail-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.detail-status {
  flex-shrink: 0;
  font-size: var(--text-xs);
  padding: 1px 10px;
  border-radius: var(--radius-full);
}

.detail-status.status-1 { color: #d97706; background: rgba(217, 119, 6, 0.1); }
.detail-status.status-2 { color: #2563eb; background: rgba(37, 99, 235, 0.1); }
.detail-status.status-3 { color: #059669; background: rgba(5, 150, 105, 0.1); }
.detail-status.status-4 { color: var(--color-ink-muted); background: var(--color-surface-dim); }

.detail-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.detail-nickname {
  color: var(--color-ink-muted);
}

.detail-handler {
  color: var(--color-primary);
}

.detail-content {
  min-height: 80px;
  font-size: var(--text-base);
  color: var(--color-ink);
  line-height: var(--leading-relaxed);
  word-break: break-word;
}

.detail-rendering {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 8px 0;
}

/* 操作按钮统一收在弹窗右下角 */
.detail-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
}
</style>

<style>
.feedback-detail-modal {
  width: 90vw;
  max-width: 720px;
}
</style>
