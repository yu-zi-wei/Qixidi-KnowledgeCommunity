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
import { ref, computed, watch, onMounted, onUnmounted, nextTick } from 'vue'
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

// 用于跟踪已经分配过的项（通过 id）
const allocatedIds = ref<Set<number>>(new Set())

// 计算每个列的近似高度
const getApproximateHeight = (items: ReadingEssaysInfo[]) => {
  return items.reduce((total, item) => {
    return total + estimateHeight(item)
  }, 0)
}

const estimateHeight = (item: ReadingEssaysInfo) => {
  const baseHeight = 220
  const contentLength = item.summary?.length || item.content?.length || 0
  const imageCount = (item.images?.length || 0)
  return baseHeight + Math.min(contentLength / 50, 120) + imageCount * 100
}

// 总项目数
const totalItems = computed(() => column1.value.length + column2.value.length + column3.value.length)

// 移动端使用原始数据顺序
const allItems = computed(() => props.readingEssays)

// 分配新数据到最短列
const distributeNewItems = (newItems: ReadingEssaysInfo[]) => {
  newItems.forEach(item => {
    if (allocatedIds.value.has(item.id)) return

    allocatedIds.value.add(item.id)

    // 找出最短的列
    const h1 = getApproximateHeight(column1.value)
    const h2 = getApproximateHeight(column2.value)
    const h3 = getApproximateHeight(column3.value)

    if (h1 <= h2 && h1 <= h3) {
      column1.value.push(item)
    } else if (h2 <= h3) {
      column2.value.push(item)
    } else {
      column3.value.push(item)
    }
  })
}

// 监听数据变化，只添加新元素
watch(
  () => props.readingEssays,
  (newData, oldData) => {
    if (!newData || newData.length === 0) return

    // 找出新增的项（id 不在已分配集合中的）
    const newItems = newData.filter(item => !allocatedIds.value.has(item.id))

    if (newItems.length > 0) {
      distributeNewItems(newItems)
    }
  },
  { deep: true }
)

// 清空重置（当筛选条件变化时，父组件可以通过设置空数组来触发）
watch(
  () => props.readingEssays.length,
  (newLength, oldLength) => {
    // 当数据减少时（筛选变化），重置所有列
    if (newLength < oldLength) {
      allocatedIds.value.clear()
      column1.value = []
      column2.value = []
      column3.value = []
      distributeNewItems(props.readingEssays)
    }
  }
)

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
