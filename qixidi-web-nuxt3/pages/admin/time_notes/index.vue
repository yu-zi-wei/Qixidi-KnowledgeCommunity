<template>
  <div class="time-notes-admin">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">时光小记管理</h1>
        <span class="total-count">共 {{ total }} 篇</span>
      </div>
      <div class="header-right">
        <n-button type="primary" @click="handleAdd">
          <template #icon>
            <Plus class="icon" />
          </template>
          写时光小记
        </n-button>
      </div>
    </div>

    <div class="page-content">
      <!-- 列表 -->
      <div class="notes-list" v-if="timeNotesGroups.length > 0">
        <div
          v-for="group in timeNotesGroups"
          :key="group.recordTime"
          class="month-group"
        >
          <div class="month-header">
            <span class="month-title">{{ formatMonthTitle(group.recordTime) }}</span>
            <span class="month-count">{{ group.list?.length || 0 }} 篇</span>
          </div>

          <div
            v-for="note in group.list"
            :key="note.id"
            class="note-item"
          >
            <div class="note-info">
              <span class="note-date">{{ formatDate(note.recordTime) }}</span>
              <span class="note-title" v-if="note.title">{{ note.title }}</span>
            </div>
            <div class="note-actions">
              <n-button text size="small" @click="handleEdit(note.id)">
                <template #icon>
                  <Edit class="action-icon" />
                </template>
              </n-button>
              <n-button text size="small" @click="handleDelete(note.id)">
                <template #icon>
                  <Trash class="action-icon delete" />
                </template>
              </n-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <p>还没有时光小记，快来记录第一篇吧！</p>
        <n-button type="primary" @click="handleAdd">开始记录</n-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus, Edit, Trash } from '@vicons/tabler'
import type { TimeNotesVo } from '~/types'

definePageMeta({
  layout: 'admin',
  middleware: 'auth'
})

const router = useRouter()
const message = useMessage()
const dialog = useDialog()

const timeNotesApi = useTimeNotesAdminApi()

const timeNotesGroups = ref<TimeNotesVo[]>([])
const total = ref(0)
const loading = ref(false)

// 加载列表
const loadTimeNotes = async () => {
  loading.value = true
  try {
    const result = await timeNotesApi.getTimeNotesList({
      pageNum: 1,
      pageSize: 100
    })
    timeNotesGroups.value = result.rows || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载失败:', error)
    message.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

// 格式化月份标题
const formatMonthTitle = (monthStr: string) => {
  if (!monthStr) return ''
  const [year, month] = monthStr.split('-')
  return `${year}年${parseInt(month)}月`
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${month}-${day}`
}

// 新增
const handleAdd = () => {
  router.push('/admin/time_notes/edit')
}

// 编辑
const handleEdit = (id: number) => {
  router.push(`/admin/time_notes/edit/${id}`)
}

// 删除
const handleDelete = (id: number) => {
  dialog.warning({
    title: '确认删除',
    content: '删除后无法恢复，确定要删除吗？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await timeNotesApi.deleteTimeNotes(id)
        message.success('删除成功')
        loadTimeNotes()
      } catch (error) {
        console.error('删除失败:', error)
        message.error('删除失败，请重试')
      }
    }
  })
}

// 初始化
await loadTimeNotes()
</script>

<style scoped>
.time-notes-admin {
  min-height: 100vh;
  background: var(--color-surface);
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-5) var(--space-6);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.page-title {
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.icon {
  width: 18px;
  height: 18px;
  stroke-width: 1.5;
}

.page-content {
  padding: var(--space-6);
  max-width: 800px;
  margin: 0 auto;
}

.notes-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.month-group {
  background: var(--color-surface);
  border-radius: 12px;
  border: 1px solid var(--color-border-light);
  overflow: hidden;
}

.month-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4) var(--space-5);
  background: var(--color-surface-dim);
  border-bottom: 1px solid var(--color-border-light);
}

.month-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-primary);
}

.month-count {
  font-size: 13px;
  color: var(--color-ink-muted);
}

.note-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4) var(--space-5);
  border-bottom: 1px solid var(--color-border-light);
  transition: background 0.2s ease;
}

.note-item:last-child {
  border-bottom: none;
}

.note-item:hover {
  background: var(--color-surface-dim);
}

.note-info {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  flex: 1;
  min-width: 0;
}

.note-date {
  font-size: 14px;
  color: var(--color-ink-muted);
  flex-shrink: 0;
  min-width: 50px;
}

.note-title {
  font-size: 14px;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.note-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  opacity: 0;
  transition: opacity 0.2s ease;
}

.note-item:hover .note-actions {
  opacity: 1;
}

.action-icon {
  width: 16px;
  height: 16px;
  stroke-width: 1.5;
  color: var(--color-ink-muted);
}

.action-icon.delete:hover {
  color: var(--color-danger);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: var(--space-4);
  color: var(--color-ink-muted);
}

@media (max-width: 768px) {
  .page-header {
    padding: var(--space-4);
  }

  .page-content {
    padding: var(--space-4);
  }

  .note-actions {
    opacity: 1;
  }
}
</style>
