<template>
  <div class="time-notes-list">
    <!-- 标题 -->
    <div class="list-header">
      <h2 class="list-title">时光小记</h2>
      <span class="list-count">共 {{ total }} 篇</span>
    </div>

    <!-- 时间线列表 -->
    <div class="list-content" ref="listRef">
      <div v-if="loading && timeNotesGroups.length === 0" class="loading-state">
        <n-spin size="medium" />
      </div>

      <div v-else-if="timeNotesGroups.length === 0" class="empty-state">
        <p>暂无记录</p>
      </div>

      <template v-else>
        <!-- 按月份分组显示 -->
        <div
          v-for="group in timeNotesGroups"
          :key="group.recordTime"
          class="month-group"
        >
          <!-- 月份标题 + 条数 -->
          <div class="month-header">
            <span>{{ formatMonthTitle(group.recordTime) }}</span>
            <span class="month-count">「{{ group.list?.length || 0 }}篇」</span>
          </div>

          <!-- 该月份下的小记列表 -->
          <div
            v-for="note in group.list"
            :key="note.id"
            class="note-item"
            :class="{ active: selectedId === note.id }"
            @click="$emit('select', note.id)"
          >
            <div class="note-date">{{ formatNoteDate(note.recordTime) }}</div>
            <div class="note-title" v-if="note.title">
              <span class="note-title-text">{{ note.title }}</span>
              <svg v-if="note.isContent" class="has-content-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" title="有详情内容">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
            </div>
          </div>
        </div>

        <!-- 底部加载检测器 -->
        <div ref="loadMoreTrigger" class="load-more-trigger">
          <n-spin v-if="loading && hasMore" size="small" />
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import type { TimeNotesVo } from '~/types'

interface Props {
  timeNotesGroups: TimeNotesVo[]
  selectedId: number | null
  loading: boolean
  hasMore: boolean
  total?: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  select: [id: number]
  loadMore: []
}>()

const listRef = ref<HTMLElement | null>(null)
const loadMoreTrigger = ref<HTMLElement | null>(null)
let observer: IntersectionObserver | null = null

// 设置 IntersectionObserver 自动加载
onMounted(() => {
  if (loadMoreTrigger.value) {
    observer = new IntersectionObserver(
      (entries) => {
        // 当触发器可见且有更多数据且不在加载中时，触发加载
        if (entries[0].isIntersecting && props.hasMore && !props.loading) {
          emit('loadMore')
        }
      },
      {
        root: listRef.value,
        rootMargin: '100px',
        threshold: 0
      }
    )
    observer.observe(loadMoreTrigger.value)
  }
})

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
  }
})

// 格式化月份标题 (yyyy-MM -> 2025年3月)
const formatMonthTitle = (monthStr: string) => {
  if (!monthStr) return ''
  const [year, month] = monthStr.split('-')
  return `${year}年${parseInt(month)}月`
}

// 格式化日期 (yyyy-MM-dd -> 03-26)
const formatNoteDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${month}-${day}`
}
</script>

<style scoped>
.time-notes-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

/* 标题 */
.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

.dark .list-header {
  border-bottom-color: rgba(255, 255, 255, 0.05);
}

.list-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.list-count {
  font-size: 12px;
  color: var(--color-ink-muted);
}

/* 列表内容 */
.list-content {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

/* 自定义滚动条 - 调窄 */
.list-content::-webkit-scrollbar {
  width: 4px;
}

.list-content::-webkit-scrollbar-track {
  background: transparent;
}

.list-content::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.15);
  border-radius: 2px;
}

.list-content::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.25);
}

.dark .list-content::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.15);
}

.dark .list-content::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.25);
}

/* 空状态和加载状态 */
.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: var(--color-ink-muted);
}

/* 月份分组 */
.month-group {
  margin-bottom: 12px;
}

.month-header {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 12px 20px 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-primary);
}

.month-count {
  font-size: 12px;
  font-weight: 400;
  color: var(--color-ink-muted);
}

/* 笔记项 */
.note-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.note-item:hover {
  background: rgba(61, 90, 128, 0.05);
}

.note-item.active {
  background: rgba(61, 90, 128, 0.08);
  border-left-color: var(--color-primary);
}

.dark .note-item:hover {
  background: rgba(90, 127, 168, 0.08);
}

.dark .note-item.active {
  background: rgba(90, 127, 168, 0.12);
}

.note-date {
  font-size: 13px;
  color: var(--color-ink-muted);
  flex-shrink: 0;
  min-width: 56px;
}

.note-title {
  font-size: 14px;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 6px;
}

.note-title-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 0;
}

.has-content-icon {
  width: 14px;
  height: 14px;
  color: var(--color-ink-muted);
  flex-shrink: 0;
}

.note-item.active .note-title {
  color: var(--color-primary);
}

/* 底部加载触发器 */
.load-more-trigger {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
