<template>
  <div class="time-notes-detail-content">
    <!-- 标题区域 -->
    <div class="detail-header">
      <!-- 第一行：时间 + 作者 -->
      <div class="detail-meta">
        <div class="detail-date">
          <span class="date-day">{{ formatDay(note.recordTime) }}</span>
          <span class="date-month">{{ formatMonth(note.recordTime) }}</span>
          <span class="date-year">{{ formatYear(note.recordTime) }}</span>
        </div>
        <template v-if="note.createBy">
          <span class="divider">·</span>
          <span class="create-by">{{ note.createBy }}</span>
        </template>
      </div>

      <!-- 第二行：标题 -->
      <h2 v-if="note.title" class="detail-title">
        {{ note.title }}
      </h2>
    </div>

    <!-- 内容区域 -->
    <div class="detail-content">
      <MarkdownRenderer :html="renderedHtml || ''" />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesInfo } from '~/types'
import { formatDay, formatMonth, formatYear } from '~/utils/formatTime'

interface Props {
  note: TimeNotesInfo
}

const props = defineProps<Props>()

// 内部渲染 Markdown → HTML（SSR 输出完整 HTML）
const { renderMarkdown } = useMarkdown()
const { data: renderedHtml } = await useAsyncData(
  () => `note-html-${props.note.id}`,
  () => renderMarkdown(props.note.content || '')
)
</script>

<style scoped>
.time-notes-detail-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 标题区域 */
.detail-header {
  padding: 24px 32px;
  border-bottom: 1px solid var(--color-border-light);
}

.detail-meta {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 8px;
}

.detail-date {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.date-day {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1;
}

.date-month {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink-muted);
  text-transform: uppercase;
}

.date-year {
  font-size: 12px;
  font-weight: 400;
  color: var(--color-ink-muted);
}

.divider {
  color: var(--color-ink-muted);
}

.create-by {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink-muted);
}

.detail-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-ink);
  line-height: 1.4;
  margin: 0;
}

/* 内容区域 */
.detail-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px 32px;
}

/* 响应式 */
@media (max-width: 768px) {
  .detail-header {
    padding: 16px;
  }

  .detail-content {
    padding: 16px;
  }

  .date-day {
    font-size: 22px;
  }

  .detail-title {
    font-size: 20px;
  }
}
</style>
