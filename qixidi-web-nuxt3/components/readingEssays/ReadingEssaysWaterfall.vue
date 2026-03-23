<template>
  <div class="reading-essays-waterfall">
    <ReadingEssaysCard
      v-for="readingEssay in readingEssays"
      :key="readingEssay.id"
      :reading-essay="readingEssay"
      @collect="$emit('collect', readingEssay.id)"
    />

    <!-- 加载更多 -->
    <div v-if="hasMore" ref="loadTrigger" class="load-more">
      <span v-if="loading">加载中...</span>
      <span v-else>下拉加载更多</span>
    </div>

    <!-- 空状态 -->
    <div v-if="readingEssays.length === 0 && !loading" class="empty-state">
      <p>暂无随笔</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import type { ReadingEssaysInfo } from '~/types'

interface Props {
  readingEssays: ReadingEssaysInfo[]
  loading: boolean
  hasMore: boolean
}

defineProps<Props>()

defineEmits<{
  collect: [id: number]
}>()

const loadTrigger = ref<HTMLElement | null>(null)

let observer: IntersectionObserver | null = null

onMounted(() => {
  if (!loadTrigger.value) return

  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        // 触发加载更多
        // 这里我们使用 emit 让父组件处理
      }
    },
    { threshold: 0.1 }
  )

  observer.observe(loadTrigger.value)
})

onUnmounted(() => {
  observer?.disconnect()
})
</script>

<style scoped>
.reading-essays-waterfall {
  column-count: 3;
  column-gap: 20px;
  flex: 1;
}

/* 加载更多 */
.load-more,
.no-more {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px 20px;
  color: var(--color-ink-muted);
  font-size: var(--text-sm);
  break-inside: avoid;
}

/* 空状态 */
.empty-state {
  grid-column: 1 / -1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: var(--color-ink-muted);
}

/* 响应式 */
@media (max-width: 1200px) {
  .reading-essays-waterfall {
    column-count: 2;
  }
}

@media (max-width: 768px) {
  .reading-essays-waterfall {
    /* 移动端改为列表布局 */
    column-count: 1;
    column-gap: 0;
    display: flex;
    flex-direction: column;
  }
}
</style>
