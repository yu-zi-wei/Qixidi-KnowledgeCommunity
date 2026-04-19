<template>
  <div class="admin-time-notes">
    <!-- 顶部操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索标题..."
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <n-icon><Search /></n-icon>
          </template>
        </n-input>
        <n-button @click="handleSearch">搜索</n-button>
      </div>
      <n-button type="primary" @click="handleCreate">
        <template #icon>
          <n-icon><Plus /></n-icon>
        </template>
        新建小记
      </n-button>
    </div>

    <!-- 列表 -->
    <div class="list-section">
      <div v-if="loading && !noteList.length" class="loading-state">
        <n-spin size="large" />
      </div>

      <div v-else-if="!noteList.length" class="empty-state">
        <n-empty description="暂无时光小记">
          <template #extra>
            <n-button type="primary" @click="handleCreate">立即创建</n-button>
          </template>
        </n-empty>
      </div>

      <template v-else>
        <div class="list-meta">
          <span class="total-count">共 {{ total }} 篇</span>
        </div>
        <div class="note-list">
          <div
            v-for="note in noteList"
            :key="note.id"
            class="note-item"
          >
            <div class="note-info">
              <span class="note-date" :title="getFullDateTime(note.recordTime)">
                {{ formatNoteDate(note.recordTime) }}
              </span>
              <span class="note-title" @click="handleViewDetail(note)">{{ note.title || '无标题' }}</span>
              <n-icon v-if="note.isContent" size="14" class="content-icon" title="有详情内容"><FileText /></n-icon>
            </div>
            <div class="note-actions">
              <n-button text size="tiny" type="primary" @click="handleEdit(note)">编辑</n-button>
              <n-popconfirm @positive-click="handleDelete(note.id)">
                <template #trigger>
                  <n-button text size="tiny" type="error">删除</n-button>
                </template>
                确定要删除这条小记吗？
              </n-popconfirm>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > pageSize" class="pagination">
          <n-pagination
            v-model:page="currentPage"
            v-model:page-size="pageSize"
            :item-count="total"
            show-size-picker
            :page-sizes="[20, 50]"
            :page-slot="5"
            @update:page="handlePageChange"
            @update:page-size="handlePageSizeChange"
          />
        </div>
      </template>
    </div>

    <!-- 右侧抽屉：详情 -->
    <n-drawer v-model:show="drawerVisible" :width="760" placement="right">
      <n-drawer-content title="小记详情" :closable="true" :native-scrollbar="false">
        <div class="drawer-detail-wrapper">
          <div v-if="detailLoading" class="drawer-loading">
            <n-spin size="medium" />
          </div>
          <TimeNotesDetail v-else-if="currentDetail" :detail="currentDetail" @edit="handleEditById" />
          <div v-else class="drawer-loading">加载失败</div>
        </div>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { Plus, FileText, Search } from '@vicons/tabler'
import type { TimeNotesInfo } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const message = useMessage()
const route = useRoute()
const router = useRouter()
const timeNotesApi = useTimeNotesAdminApi()

// 列表 - 从 URL 参数恢复状态
const noteList = ref<any[]>([])
const total = ref(0)
const currentPage = ref(Number(route.query.page) || 1)
const pageSize = ref(Number(route.query.pageSize) || 20)
const searchKeyword = ref(String(route.query.keyword || ''))
const loading = ref(false)

// 抽屉
const drawerVisible = ref(false)
const currentDetail = ref<TimeNotesInfo | null>(null)
const detailLoading = ref(false)

// 获取列表
const fetchList = async () => {
  loading.value = true
  try {
    const result = await timeNotesApi.getTimeNotesList({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      title: searchKeyword.value || undefined
    })
    noteList.value = result.rows || []
    total.value = result.total || 0
  } catch {
  } finally {
    loading.value = false
  }
}

// 同步状态到 URL（不产生历史记录）
const syncPageToUrl = () => {
  const query: Record<string, any> = {
    page: currentPage.value,
    pageSize: pageSize.value
  }
  if (searchKeyword.value) {
    query.keyword = searchKeyword.value
  }
  router.replace({ query })
}

// 分页切换
const handlePageChange = (page: number) => {
  currentPage.value = page
  syncPageToUrl()
  fetchList()
}

// 每页条数切换
const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  syncPageToUrl()
  fetchList()
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  syncPageToUrl()
  fetchList()
}

// 格式化日期
const formatNoteDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

// 查看详情
const handleViewDetail = async (note: any) => {
  currentDetail.value = null
  drawerVisible.value = true

  detailLoading.value = true
  try {
    currentDetail.value = await timeNotesApi.getTimeNotesDetail(note.id)
  } catch {
  } finally {
    detailLoading.value = false
  }
}

// 新建
const handleCreate = () => {
  router.push('/write/note')
}

// 编辑（列表按钮）
const handleEdit = (note: any) => {
  router.push(`/write/note/${note.id}`)
}

// 编辑（详情组件内触发）
const handleEditById = (id: number) => {
  drawerVisible.value = false
  router.push(`/write/note/${id}`)
}

// 删除
const handleDelete = async (id: number) => {
  try {
    await timeNotesApi.deleteTimeNotes(id)
    message.success('删除成功')
    if (drawerVisible.value && currentDetail.value?.id === id) {
      drawerVisible.value = false
    }
    fetchList()
  } catch {
  }
}

onMounted(() => fetchList())
</script>

<style scoped>
.admin-time-notes {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
  overflow: hidden;
}

/* 顶部操作区 */
.header-section {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.search-bar {
  display: flex;
  gap: var(--space-3);
  flex: 1;
}

.search-bar .n-input {
  flex: 1;
}

.list-meta {
  flex-shrink: 0;
  padding-bottom: var(--space-3);
  margin-bottom: var(--space-3);
  border-bottom: 1px solid var(--color-border-light);
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 列表区域 */
.list-section {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-5);
  box-shadow: var(--shadow-sm);
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.note-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.note-list::-webkit-scrollbar {
  width: 6px;
}

.note-list::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.note-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

/* 列表项 */
.note-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  border-left: 3px solid transparent;
}

.note-item + .note-item {
  border-top: 1px solid var(--color-border-light);
}

.note-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  min-width: 0;
  flex: 1;
}

.note-date {
  flex-shrink: 0;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  font-variant-numeric: tabular-nums;
  min-width: 90px;
}

.note-title {
  font-size: var(--text-base);
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.note-title:hover {
  color: var(--color-primary);
}

.content-icon {
  flex-shrink: 0;
  color: var(--color-ink-faint);
}

.note-actions {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: var(--space-2);
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.note-item:hover .note-actions {
  opacity: 1;
}

/* 分页 */
.pagination {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-top: var(--space-4);
  margin-top: var(--space-3);
  border-top: 1px solid var(--color-border-light);
}

/* 抽屉 */
.drawer-detail-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.drawer-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

@media (max-width: 768px) {
  .note-actions {
    opacity: 1;
  }
}
</style>
