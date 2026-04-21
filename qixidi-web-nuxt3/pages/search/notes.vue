<template>
  <div class="notes-search-page">
    <div v-if="searching" class="notes-search-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!timeNotesGroups.length" class="notes-search-empty">
      <CommonEmptyState :description="keyword ? '未找到相关小记' : '请输入搜索关键词'" />
    </div>
    <template v-else>
      <div
        v-for="group in timeNotesGroups"
        :key="group.recordTime"
        class="ns-month-group"
      >
        <div class="ns-month-header">
          <span>{{ formatMonthTitle(group.recordTime) }}</span>
          <span class="ns-month-count">「{{ group.list?.length || 0 }}篇」</span>
        </div>
        <div
          v-for="note in group.list"
          :key="note.id"
          class="ns-note-item"
        >
          <span class="ns-note-date">{{ formatNoteDate(note.recordTime) }}</span>
          <NuxtLink :to="`/time-notes/${note.id}`" class="ns-note-title">{{ note.title }}</NuxtLink>
          <svg v-if="note.isContent" class="ns-content-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" title="有详情内容">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14 2 14 8 20 8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
          </svg>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesVo } from '~/types'

definePageMeta({ showTabBar: false })

const route = useRoute()
const searchApi = useSearchApi()

const keyword = computed(() => (route.query.q as string || '').trim())

const searching = ref(false)
const timeNotesGroups = ref<TimeNotesVo[]>([])

const searchTimeNotes = async () => {
  if (!keyword.value) return
  searching.value = true
  try {
    const res = await searchApi.searchTimeNotes({ pageNum: 1, pageSize: 40, title: keyword.value })
    timeNotesGroups.value = res.rows || []
  } finally {
    searching.value = false
  }
}

const formatMonthTitle = (monthStr: string) => {
  if (!monthStr) return ''
  const [year, month] = monthStr.split('-')
  return `${year}年${parseInt(month)}月`
}

const formatNoteDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const mm = (d.getMonth() + 1).toString().padStart(2, '0')
  const dd = d.getDate().toString().padStart(2, '0')
  return `${mm}-${dd}`
}

watch(keyword, () => searchTimeNotes())
onMounted(() => searchTimeNotes())
</script>

<style scoped>
.notes-search-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-top: 8px;
}

.notes-search-loading,
.notes-search-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.ns-month-group {
  margin-bottom: 8px;
}

.ns-month-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 4px;
}

.ns-month-count {
  font-size: var(--text-sm);
  font-weight: 400;
  color: var(--color-ink-muted);
}

.ns-note-item {
  display: flex;
  align-items: baseline;
  gap: 12px;
  padding: 10px 16px;
  border-radius: var(--radius-sm);
}

.ns-note-date {
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
}

.ns-note-title {
  font-size: var(--text-base);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.ns-note-title:hover {
  color: var(--color-primary);
}

.ns-content-icon {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  color: var(--color-ink-faint);
}
</style>
