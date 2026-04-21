<template>
  <div class="time-notes-page">
    <div class="tn-search">
      <n-input v-model:value="searchKeyword" placeholder="搜索小记标题" clearable>
        <template #prefix>
          <n-icon><Search /></n-icon>
        </template>
      </n-input>
    </div>

    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <div v-else-if="!noteGroups.length" class="empty-state">
      <CommonEmptyState :description="apiKeyword ? '没有找到相关小记' : '暂无小记'" />
    </div>

    <template v-else>
      <div ref="notesListRef" class="notes-list">
        <div
          v-for="group in noteGroups"
          :key="group.recordTime"
          class="tn-month-group"
        >
          <div class="tn-month-header">
            <span>{{ formatMonthTitle(group.recordTime) }}</span>
            <span class="tn-month-count">「{{ group.list?.length || 0 }}篇」</span>
          </div>
          <div
            v-for="note in group.list"
            :key="note.id"
            class="tn-note-item"
          >
            <span class="tn-note-date">{{ formatNoteDate(note.recordTime) }}</span>
            <NuxtLink :to="`/time-notes/${note.id}`" class="tn-note-title" @click="handleSaveScroll">{{ note.title || '无标题' }}</NuxtLink>
            <n-icon v-if="note.isContent" size="14" class="tn-content-icon" title="有详情内容"><FileText /></n-icon>
          </div>
        </div>
      </div>

      <div v-if="total > pageSize" class="tn-pagination">
        <n-pagination
          :page="currentPage"
          :item-count="total"
          :page-size="pageSize"
          :page-slot="5"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { FileText, Search } from '@vicons/tabler'
import type { TimeNotesVo } from '~/types'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const timeNotesApi = useTimeNotesApi()

const uid = computed(() => route.params.uid as string)
const pageSize = 20
const currentPage = computed(() => Number(route.query.page) || 1)
const notesListRef = ref<HTMLElement | null>(null)

const searchKeyword = ref('')
const apiKeyword = ref('')

let debounceTimer: ReturnType<typeof setTimeout> | null = null
watch(searchKeyword, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    apiKeyword.value = val
  }, 400)
})

const cacheKey = computed(() => `user-home-time-notes-${uid.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: pageData, pending } = await useAsyncData(
  cacheKey,
  () => timeNotesApi.getTimeNotesList({
    pageNum: currentPage.value,
    pageSize,
    uid: Number(uid.value),
    title: apiKeyword.value || undefined
  })
)

const noteGroups = computed(() => pageData.value?.rows || [])
const total = computed(() => pageData.value?.total || 0)

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

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

const handleSaveScroll = () => {
  if (import.meta.client && notesListRef.value) {
    history.replaceState({
      ...(window.history.state || {}),
      timeNotesScroll: notesListRef.value.scrollTop
    }, '')
  }
}

onMounted(() => {
  if (import.meta.client && window.history.state?.timeNotesScroll) {
    nextTick(() => {
      if (notesListRef.value) {
        notesListRef.value.scrollTop = window.history.state.timeNotesScroll
      }
    })
  }
})
</script>

<style>
.time-notes-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.tn-search {
  flex-shrink: 0;
  padding: 0 24px 12px;
}

.time-notes-page .loading-state,
.time-notes-page .empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.notes-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 0 24px;
}

.notes-list::-webkit-scrollbar {
  width: 6px;
}

.notes-list::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.notes-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.tn-month-group {
  margin-bottom: 8px;
}

.tn-month-header {
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

.tn-month-count {
  font-size: var(--text-sm);
  font-weight: 400;
  color: var(--color-ink-muted);
}

.tn-note-item {
  display: flex;
  align-items: baseline;
  gap: 12px;
  padding: 10px 16px;
  border-radius: var(--radius-sm);
}

.tn-note-date {
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
}

.tn-note-title {
  font-size: var(--text-base);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.tn-note-title:hover {
  color: var(--color-primary);
}

.tn-content-icon {
  flex-shrink: 0;
  color: var(--color-ink-faint);
}

.time-notes-page .tn-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding: 12px 0 16px;
  background: var(--color-surface-warm);
  border-top: 1px solid var(--color-border-light);
}
</style>
