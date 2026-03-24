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
        <!-- 第一行：时间 + 作者 -->
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

defineProps<Props>()

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
