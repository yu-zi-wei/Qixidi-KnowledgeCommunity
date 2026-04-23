<template>
  <div class="essay-detail-content">
    <!-- 操作按钮插槽 -->
    <div v-if="$slots.actions" class="essay-actions">
      <slot name="actions" />
    </div>

    <!-- 随笔内容 -->
    <div class="essay-content">
      {{ essay.content }}
    </div>

    <!-- 作者和作品 -->
    <div v-if="essay.author || essay.worksName" class="essay-source">
      <span class="source-sep">——</span>
      <span v-if="essay.author" class="author-name">{{ essay.author }}</span>
      <span v-if="essay.worksName" class="work-name">《{{ essay.worksName }}》</span>
    </div>

    <!-- 分割线 -->
    <div class="divider"></div>

    <!-- 元信息区 -->
    <div class="essay-meta-section">
      <!-- 左侧：分类和专辑 -->
      <div class="meta-left">
        <div v-if="essay.groupName" class="meta-row">
          <span class="meta-label">分类</span>
          <span class="meta-value">{{ essay.groupName }}</span>
        </div>
        <div v-if="essay.albumName" class="meta-row">
          <span class="meta-label">专辑</span>
          <span class="meta-value">{{ essay.albumName }}</span>
        </div>
        <div class="meta-row">
          <span class="meta-label">时间</span>
          <span class="meta-value meta-time">{{ getFullDateCN(essay.createTime) }}</span>
        </div>
      </div>

      <!-- 右侧：标签 -->
      <div v-if="essay.labelList && essay.labelList.length > 0" class="meta-right">
        <div class="label-list">
          <span v-for="label in essay.labelList" :key="label" class="label-tag">
            # {{ label }}
          </span>
        </div>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="essay-stats">
      <span class="essay-detail-stat">
        <MessageCircle class="stat-icon" />
        {{ essay.commentSum || 0 }} 评论
      </span>
      <span class="essay-detail-stat">
        <ThumbUp class="stat-icon" />
        {{ essay.helpSum || 0 }} 赞
      </span>
    </div>

    <!-- 评论区域 -->
    <DictumCommentSection
      :key="essay.id"
      :dictum-id="essay.id"
      :dictum-uid="essay.uid"
      @comment-added="handleCommentAdded"
    />
  </div>
</template>

<script setup lang="ts">
import { MessageCircle, ThumbUp } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'
import { getFullDateCN } from '~/utils/formatTime'

import DictumCommentSection from './DictumCommentSection.vue'

interface Props {
  essay: ReadingEssaysInfo
}

defineProps<Props>()
const emit = defineEmits<{
  commentAdded: []
}>()

const handleCommentAdded = () => {
  emit('commentAdded')
}
</script>

<style scoped>
.essay-detail-content {
  padding: 24px 32px;
  position: relative;
}

.essay-actions {
  position: absolute;
  top: 16px;
  right: 16px;
  display: flex;
  gap: 4px;
  z-index: 10;
}

.essay-content {
  font-size: 17px;
  line-height: 1.9;
  color: var(--color-ink);
  margin-bottom: 28px;
  padding-top: 28px;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
  text-decoration: underline;
  text-decoration-style: dashed;
  text-underline-offset: 6px;
  text-decoration-color: var(--color-border);
}

.essay-source {
  font-size: var(--text-base);
  color: var(--color-ink-muted);
  font-style: italic;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
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

.divider {
  height: 1px;
  background: var(--color-border-light);
  margin-bottom: 20px;
}

/* 元信息区 */
.essay-meta-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 24px;
}

.meta-left {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.meta-label {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  width: 36px;
  flex-shrink: 0;
}

.meta-value {
  font-size: var(--text-sm);
  color: var(--color-ink);
}

.meta-time {
  color: var(--color-ink-light);
}

.meta-right {
  flex-shrink: 0;
}

.label-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: flex-end;
}

.label-tag {
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 4px 12px;
  border-radius: var(--radius-full);
}

/* 统计信息 */
.essay-stats {
  display: flex;
  gap: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--color-border-light);
}

.essay-detail-stat {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 16px;
  height: 16px;
}

/* 响应式 */
@media (max-width: 640px) {
  .essay-detail-content {
    padding: 20px 16px;
  }

  .essay-meta-section {
    flex-direction: column;
    gap: 16px;
  }

  .meta-right {
    width: 100%;
  }

  .label-list {
    justify-content: flex-start;
  }
}
</style>
