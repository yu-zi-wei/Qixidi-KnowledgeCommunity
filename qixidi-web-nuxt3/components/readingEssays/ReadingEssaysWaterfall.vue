<template>
  <div class="reading-essays-waterfall">
    <!-- 桌面端三列布局 -->
    <div class="waterfall-column desktop-only">
      <ReadingEssaysCard
        v-for="item in column1"
        :key="item.id"
        :reading-essay="item"
        @collect="$emit('collect', item.id)"
        @show-detail="$emit('showDetail', item)"
      />
    </div>
    <div class="waterfall-column desktop-only">
      <ReadingEssaysCard
        v-for="item in column2"
        :key="item.id"
        :reading-essay="item"
        @collect="$emit('collect', item.id)"
        @show-detail="$emit('showDetail', item)"
      />
    </div>
    <div class="waterfall-column desktop-only">
      <ReadingEssaysCard
        v-for="item in column3"
        :key="item.id"
        :reading-essay="item"
        @collect="$emit('collect', item.id)"
        @show-detail="$emit('showDetail', item)"
      />
    </div>

    <!-- 移动端单列布局 -->
    <div class="mobile-list">
      <ReadingEssaysCard
        v-for="item in allItems"
        :key="item.id"
        :reading-essay="item"
        @collect="$emit('collect', item.id)"
        @show-detail="$emit('showDetail', item)"
      />
    </div>

    <!-- 加载更多 -->
    <div v-if="hasMore" ref="loadTrigger" class="load-more">
      <span v-if="loading">加载中...</span>
      <span v-else>下拉加载更多</span>
    </div>

    <!-- 空状态 -->
    <div v-if="totalItems === 0 && !loading" class="empty-state">
      <p>暂无随笔</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import type { ReadingEssaysInfo } from '~/types'

interface Props {
  readingEssays: ReadingEssaysInfo[]
  loading: boolean
  hasMore: boolean
}

const props = defineProps<Props>()
defineEmits<{
  collect: [id: number]
  showDetail: [item: ReadingEssaysInfo]
}>()

const loadTrigger = ref<HTMLElement | null>(null)

// 三列数据
const column1 = ref<ReadingEssaysInfo[]>([])
const column2 = ref<ReadingEssaysInfo[]>([])
const column3 = ref<ReadingEssaysInfo[]>([])

// 估算卡片高度
const estimateHeight = (item: ReadingEssaysInfo) => {
  const baseHeight = 220
  const contentLength = item.summary?.length || item.content?.length || 0
  const imageCount = (item.images?.length || 0)
  return baseHeight + Math.min(contentLength / 50, 120) + imageCount * 100
}

// 计算列高度
const getColumnHeight = (items: ReadingEssaysInfo[]) => {
  return items.reduce((total, item) => total + estimateHeight(item), 0)
}

// 分配数据到三列（瀑布流算法）
const distributeToColumns = (items: ReadingEssaysInfo[]) => {
  const col1: ReadingEssaysInfo[] = []
  const col2: ReadingEssaysInfo[] = []
  const col3: ReadingEssaysInfo[] = []

  items.forEach(item => {
    const h1 = getColumnHeight(col1)
    const h2 = getColumnHeight(col2)
    const h3 = getColumnHeight(col3)

    if (h1 <= h2 && h1 <= h3) {
      col1.push(item)
    } else if (h2 <= h3) {
      col2.push(item)
    } else {
      col3.push(item)
    }
  })

  column1.value = col1
  column2.value = col2
  column3.value = col3
}

// 监听数据变化，直接重新分配
watch(
  () => props.readingEssays,
  (newData) => {
    distributeToColumns(newData || [])
  },
  { immediate: true }
)

// 总项目数
const totalItems = computed(() => props.readingEssays.length)

// 移动端使用原始数据
const allItems = computed(() => props.readingEssays)

// 无限滚动观察器
let observer: IntersectionObserver | null = null

onMounted(() => {
  if (!loadTrigger.value) return

  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        // 触发加载更多（由父组件处理滚动逻辑）
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
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.waterfall-column {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

/* 移动端列表 */
.mobile-list {
  display: none;
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
    grid-template-columns: repeat(2, 1fr);
  }

  .waterfall-column:last-child {
    display: none;
  }
}

@media (max-width: 768px) {
  .reading-essays-waterfall {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .waterfall-column {
    display: none;
  }

  .mobile-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
}
</style>
