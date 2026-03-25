<template>
  <div class="essay-detail-content">
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

    <!-- 分类信息 -->
    <div class="essay-meta">
      <span v-if="essay.groupName" class="meta-badge">{{ essay.groupName }}</span>
      <span v-if="essay.albumName" class="meta-badge">{{ essay.albumName }}</span>
      <span class="meta-time">{{ getFullDateCN(essay.createTime) }}</span>
    </div>

    <!-- 标签 -->
    <div v-if="essay.labelList && essay.labelList.length > 0" class="essay-labels">
      <span v-for="label in essay.labelList" :key="label" class="label-tag">
        # {{ label }}
      </span>
    </div>

    <!-- 统计信息 -->
    <div class="essay-stats">
      <span class="stat-item">
        <MessageCircle class="stat-icon" />
        {{ essay.commentSum || 0 }} 评论
      </span>
      <span class="stat-item">
        <ThumbUp class="stat-icon" />
        {{ essay.helpSum || 0 }} 赞
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { MessageCircle, ThumbUp } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'
import { getFullDateCN } from '~/utils/formatTime'

interface Props {
  essay: ReadingEssaysInfo
}

defineProps<Props>()
</script>

<style scoped>
.essay-detail-content {
  padding: 24px 32px;
}

.essay-content {
  font-size: 17px;
  line-height: 1.9;
  color: var(--color-ink);
  margin-bottom: 28px;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
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

.essay-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 24px;
}

.meta-badge {
  font-size: var(--text-sm);
  padding: 5px 12px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: var(--radius-sm);
  color: var(--color-ink-light);
}

:global(.dark) .meta-badge {
  background: rgba(255, 255, 255, 0.08);
}

.meta-time {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.essay-labels {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 28px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--color-border-light);
}

.label-tag {
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 6px 14px;
  border-radius: var(--radius-sm);
}

.essay-stats {
  display: flex;
  gap: 24px;
  padding-top: 8px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: var(--text-base);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 18px;
  height: 18px;
}
</style>
