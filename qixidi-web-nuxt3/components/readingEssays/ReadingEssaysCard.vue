<template>
  <div class="reading-essays-card">
    <!-- 随笔内容 -->
    <div class="reading-essays-content">
      {{ readingEssay.content }}
    </div>

    <!-- 作者和作品 -->
    <div v-if="readingEssay.author || readingEssay.worksName" class="reading-essays-source">
      <span class="source-sep">——</span>
      <span v-if="readingEssay.author" class="author-name">{{ readingEssay.author }}</span>
      <span v-if="readingEssay.worksName" class="work-name">《{{ readingEssay.worksName }}》</span>
    </div>

    <!-- 分类和标签 -->
    <div class="reading-essays-meta">
      <span v-if="readingEssay.groupName" class="meta-badge">{{ readingEssay.groupName }}</span>
      <div v-if="readingEssay.labelList && readingEssay.labelList.length > 0" class="meta-labels">
        <span v-for="label in readingEssay.labelList.slice(0, 3)" :key="label" class="label-tag">
          # {{ label }}
        </span>
      </div>
    </div>

    <!-- 底部栏 -->
    <div class="reading-essays-footer">
      <div class="footer-left">
        <span v-if="readingEssay.commentSum !== undefined" class="stat-item" title="评论">
          <MessageCircle class="stat-icon" />
          {{ readingEssay.commentSum }}
        </span>
        <span v-if="readingEssay.createTime" class="time-text" :title="getFullDateTime(readingEssay.createTime)">
          {{ formatTime(readingEssay.createTime) }}
        </span>
      </div>
      <button class="detail-btn" @click.stop="handleClick">
        详情
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { MessageCircle, ThumbUp, Bookmark } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

interface Props {
  readingEssay: ReadingEssaysInfo
}

const props = defineProps<Props>()

const emit = defineEmits<{
  collect: []
  showDetail: []
}>()

const handleClick = () => {
  emit('showDetail')
}
</script>

<style scoped>
.reading-essays-card {
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 14px;
  padding: 20px;
  transition: all 0.25s ease;
  break-inside: avoid;
  margin-bottom: 20px;
  display: inline-block;
  width: 100%;
  box-sizing: border-box;
}

.reading-essays-card:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.dark .reading-essays-card {
  background: rgba(30, 32, 35, 0.75);
  border-color: rgba(255, 255, 255, 0.06);
}

.dark .reading-essays-card:hover {
  background: rgba(40, 42, 45, 0.9);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
}

/* 随笔内容 */
.reading-essays-content {
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  color: var(--color-ink);
  margin-bottom: 16px;
  max-height: 448px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 16;
  -webkit-box-orient: vertical;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
}

/* 作者和作品 */
.reading-essays-source {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  font-style: italic;
  margin-bottom: 12px;
  margin-top: -6px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
}

.source-sep {
  color: var(--color-ink-faint);
}

.author-name {
  color: var(--color-ink-light);
}

.work-name {
  color: var(--color-ink-muted);
}

/* 分类和标签 */
.reading-essays-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.meta-badge {
  font-size: var(--text-xs);
  padding: 4px 10px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  color: var(--color-ink-light);
}

.dark .meta-badge {
  background: rgba(255, 255, 255, 0.08);
}

.meta-labels {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.label-tag {
  font-size: 12px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 4px 10px;
  border-radius: 12px;
}

/* 底部栏 */
.reading-essays-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .reading-essays-footer {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 时间文本 */
.time-text {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 统计项 */
.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 14px;
  height: 14px;
}

/* 详情按钮 */
.detail-btn {
  padding: 4px 12px;
  font-size: var(--text-xs);
  color: var(--color-ink-light);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.detail-btn:hover {
  color: var(--color-primary);
}

.author-name {
  color: var(--color-ink-light);
}

.work-name {
  color: var(--color-ink-muted);
}

/* ==================== 移动端适配 ==================== */
@media (max-width: 768px) {
  .reading-essays-card {
    /* 移动端列表样式 */
    break-inside: auto;
    margin-bottom: 16px;
    padding: 16px;
  }

  /* 底部栏调整 */
  .reading-essays-footer {
    padding-top: 10px;
  }

  .stat-icon {
    width: 13px;
    height: 13px;
  }

  /* 标签缩小 */
  .meta-badge,
  .label-tag {
    font-size: var(--text-xs);
    padding: 2px 6px;
  }
}
</style>
