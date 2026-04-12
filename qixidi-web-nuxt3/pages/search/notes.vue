<template>
  <div>
    <div v-if="searching" class="search-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!timeNotesGroups.length" class="search-empty">
      <n-empty :description="keyword ? '未找到相关小记' : '请输入搜索关键词'" />
    </div>
    <div v-else>
      <div v-for="group in timeNotesGroups" :key="group.recordTime" class="sn-month-group">
        <div class="sn-month-header">
          <span>{{ formatMonthTitle(group.recordTime) }}</span>
          <span class="sn-month-count">{{ group.list?.length || 0 }} 篇</span>
        </div>
        <div
          v-for="note in group.list"
          :key="note.id"
          class="sn-note-item"
          @click="handleSelect(note.id)"
        >
          <span class="sn-note-date">{{ formatNoteDate(note.recordTime) }}</span>
          <span class="sn-note-title">{{ note.title }}
              <svg v-if="note.isContent" class="sn-content-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" title="有详情内容">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
            </span>
        </div>
      </div>
    </div>

    <!-- 抽屉弹窗 -->
    <n-drawer v-model:show="showDrawer" :width="800" placement="right">
      <n-drawer-content closable>
        <TimeNotesDetail
          :detail="selectedNote"
          :loading="detailLoading"
          @edit="handleEdit"
        />
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesVo, TimeNotesInfo } from '~/types'

definePageMeta({ showTabBar: false })

const route = useRoute()
const router = useRouter()
const searchApi = useSearchApi()
const timeNotesApi = useTimeNotesApi()

const keyword = computed(() => (route.query.q as string || '').trim())

const searching = ref(false)
const timeNotesGroups = ref<TimeNotesVo[]>([])
const showDrawer = ref(false)
const selectedNote = ref<TimeNotesInfo | null>(null)
const detailLoading = ref(false)

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

const handleSelect = async (id: number) => {
  showDrawer.value = true
  detailLoading.value = true
  selectedNote.value = null
  try {
    selectedNote.value = await timeNotesApi.getTimeNotesDetail(id)
  } finally {
    detailLoading.value = false
  }
}

const handleEdit = (id: number) => {
  showDrawer.value = false
  router.push(`/write/note/${id}`)
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
.sn-month-group {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  overflow: hidden;
  margin-bottom: 12px;
}

.sn-month-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.sn-month-count {
  font-weight: 400;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.sn-note-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background var(--transition-fast);
}

.sn-note-item:hover {
  background: var(--color-surface-dim);
}

.sn-note-date {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  flex-shrink: 0;
  min-width: 44px;
}

.sn-note-title {
  font-size: var(--text-sm);
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 6px;
}

.sn-content-icon {
  width: 14px;
  height: 14px;
  color: var(--color-ink-muted);
  flex-shrink: 0;
}
</style>
