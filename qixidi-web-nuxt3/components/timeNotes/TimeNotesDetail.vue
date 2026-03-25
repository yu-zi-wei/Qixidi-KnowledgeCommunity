<template>
  <div class="time-notes-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <n-spin size="medium" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!detail" class="empty-state">
      <p>请选择一条记录查看</p>
    </div>

    <!-- 详情内容 -->
    <template v-else>
      <!-- 标题区域 -->
      <div class="detail-header">
        <!-- 第一行：时间 + 作者 + 编辑按钮 -->
        <div class="detail-meta">
          <div class="detail-date">
            <span class="date-day">{{ formatDay(detail.recordTime) }}</span>
            <span class="date-month">{{ formatMonth(detail.recordTime) }}</span>
            <span class="date-year">{{ formatYear(detail.recordTime) }}</span>
          </div>
          <template v-if="detail.createBy">
            <span class="divider">·</span>
            <span class="create-by">{{ detail.createBy }}</span>
          </template>

          <!-- 编辑按钮（仅作者可见） -->
          <n-button
            v-if="detail.isAuthor === 0"
            text
            size="small"
            class="edit-btn"
            @click="handleEdit"
          >
            <template #icon>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
              </svg>
            </template>
            编辑
          </n-button>
        </div>
        <!-- 第二行：标题 -->
        <h1 class="detail-title" v-if="detail.title">{{ detail.title }}</h1>
      </div>

      <!-- 内容 - Markdown 渲染 -->
      <div class="detail-content">
        <ClientOnly>
          <MarkdownRenderer :content="detail.content || ''" />
        </ClientOnly>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesInfo } from '~/types'

interface Props {
  detail: TimeNotesInfo | null
  loading: boolean
}

const props = defineProps<Props>()

const emit = defineEmits<{
  edit: [id: number]
}>()

// 编辑
const handleEdit = () => {
  if (props.detail?.id) {
    emit('edit', props.detail.id)
  }
}

// 格式化日期 - 日
const formatDay = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.getDate().toString().padStart(2, '0')
}

// 格式化日期 - 月
const formatMonth = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  return months[date.getMonth()]
}

// 格式化日期 - 年
const formatYear = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.getFullYear() + '年'
}
</script>

<style scoped>
.time-notes-detail {
  display: flex;
  flex-direction: column;
  min-height: 100%;
}

/* 加载和空状态 */
.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  min-height: 300px;
  color: var(--color-ink-muted);
}

/* 标题区域 */
.detail-header {
  padding: 24px 32px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

.dark .detail-header {
  border-bottom-color: rgba(255, 255, 255, 0.05);
}

/* 第一行：时间 + 作者 */
.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.detail-date {
  display: flex;
  align-items: center;
  gap: 6px;
}

.date-day {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1;
}

.date-month {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink);
  line-height: 24px;
}

.date-year {
  font-size: 13px;
  color: var(--color-ink-muted);
  line-height: 24px;
}

.divider {
  color: var(--color-ink-muted);
  line-height: 24px;
}

.create-by {
  font-size: 14px;
  color: var(--color-ink-muted);
  line-height: 24px;
}

/* 编辑按钮 */
.edit-btn {
  margin-left: auto;
  font-size: 13px;
  color: var(--color-ink-muted);
  transition: color 0.2s ease;
}

.edit-btn:hover {
  color: var(--color-primary);
}

.edit-btn svg {
  width: 14px;
  height: 14px;
}

/* 第二行：标题 */
.detail-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  line-height: 1.4;
}

/* 内容区域 */
.detail-content {
  padding: 24px 32px;
}
</style>
