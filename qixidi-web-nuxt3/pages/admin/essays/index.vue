<template>
  <div class="admin-essays">
    <!-- 顶部操作区 -->
    <div class="header-section">
      <div class="header-top">
        <div class="search-bar">
          <n-input
            v-model:value="searchKeyword"
            placeholder="搜索随笔内容..."
            clearable
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          >
            <template #prefix>
              <n-icon><Search /></n-icon>
            </template>
          </n-input>
        </div>
        <n-button type="primary" @click="essayDrawerStore.open()">
          <template #icon>
            <n-icon><Plus /></n-icon>
          </template>
          写随笔
        </n-button>
      </div>
      <!-- 状态 Tab -->
      <div class="status-tabs">
        <span
          v-for="tab in statusTabs"
          :key="tab.value"
          class="tab-item"
          :class="{ active: currentStatus === tab.value }"
          @click="handleTabChange(tab.value)"
        >
          {{ tab.label }}
        </span>
      </div>
    </div>

    <!-- 列表区域 -->
    <div class="list-section">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!essayList.length" class="empty-state">
        <CommonEmptyState description="暂无随笔">
          <template #extra>
            <n-button type="primary" @click="essayDrawerStore.open()">写一篇</n-button>
          </template>
        </CommonEmptyState>
      </div>

      <!-- 列表 -->
      <template v-else>
        <div class="list-meta">
          <span class="total-count">共 {{ total }} 篇随笔</span>
        </div>
        <div class="essay-list">
          <div
            v-for="essay in essayList"
            :key="essay.id"
            class="essay-item"
          >
            <!-- 随笔内容 -->
            <div class="essay-content" @click="handleShowDetail(essay)">
              {{ essay.content }}
            </div>

            <!-- 作者和作品 -->
            <div v-if="essay.author || essay.worksName" class="essay-source">
              <span class="source-sep">——</span>
              <span v-if="essay.author" class="source-author">{{ essay.author }}</span>
              <span v-if="essay.worksName" class="source-work">《{{ essay.worksName }}》</span>
            </div>

            <!-- 分类和标签 -->
            <div class="essay-meta">
              <span v-if="essay.groupName" class="meta-badge">{{ essay.groupName }}</span>
              <span
                class="state-badge"
                :class="essay.dictumState === 1 ? 'state-public' : 'state-private'"
              >
                {{ essay.dictumState === 1 ? '公开' : '私有' }}
              </span>
              <div v-if="essay.labelList && essay.labelList.length > 0" class="meta-labels">
                <span v-for="label in essay.labelList.slice(0, 3)" :key="label" class="label-tag">
                  # {{ label }}
                </span>
              </div>
            </div>

            <!-- 底部 -->
            <div class="essay-footer">
              <div class="footer-left">
                <span class="admin-stat">
                  <n-icon><MessageCircle /></n-icon>
                  {{ essay.commentSum || 0 }}
                </span>
                <span class="admin-stat" :title="getFullDateTime(essay.createTime)">
                  <n-icon><Calendar /></n-icon>
                  {{ formatTime(essay.createTime) }}
                </span>
              </div>
              <div class="footer-actions">
                <n-button text size="tiny" type="primary" @click="handleEdit(essay)">编辑</n-button>
                <n-popconfirm @positive-click="handleDelete(essay)">
                  <template #trigger>
                    <n-button text size="tiny" type="error">删除</n-button>
                  </template>
                  确定要删除这篇随笔吗？
                </n-popconfirm>
              </div>
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

    <!-- 详情抽屉 -->
    <n-drawer v-model:show="detailVisible" :width="680" placement="right">
      <n-drawer-content title="随笔详情" closable :native-scrollbar="false">
        <template #header>
          <div class="drawer-header">
            <span class="drawer-title">随笔详情</span>
            <div class="drawer-actions">
              <n-button v-if="selectedEssay" quaternary size="small" @click="handleEditFromDetail">
                <template #icon><n-icon><Edit /></n-icon></template>
              </n-button>
              <NuxtLink v-if="selectedEssay" :to="`/reading-essays/${selectedEssay.id}`" target="_blank" class="inline-flex">
                <n-button quaternary size="small">
                  <template #icon><n-icon><ExternalLink /></n-icon></template>
                </n-button>
              </NuxtLink>
              <n-button v-if="selectedEssay" quaternary size="small" @click="copyShareLink">
                <template #icon><n-icon><Share /></n-icon></template>
              </n-button>
            </div>
          </div>
        </template>
        <ReadingEssaysDetailContent
          v-if="selectedEssay"
          :essay="selectedEssay"
          @comment-added="handleCommentAdded"
        />
      </n-drawer-content>
    </n-drawer>

    <!-- 全局随笔编辑抽屉 -->
    <ReadingEssaysEssayDrawerGlobal />
  </div>
</template>

<script setup lang="ts">
import { Plus, Search, Calendar, MessageCircle, Edit, ExternalLink, Share } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const message = useMessage()
const essayDrawerStore = useEssayDrawerStore()
const dictumApi = useReadingEssaysApi()

// 状态
const loading = ref(false)
const essayList = ref<ReadingEssaysInfo[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const searchKeyword = ref('')
const currentStatus = ref<number | undefined>(undefined)

const statusTabs = [
  { label: '全部', value: undefined },
  { label: '公开', value: 1 },
  { label: '私有', value: 2 }
]

// 获取列表
const fetchList = async () => {
  loading.value = true
  try {
    const res = await dictumApi.getAdminList({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      dictumState: currentStatus.value,
      content: searchKeyword.value || undefined
    })
    essayList.value = res.rows || []
    total.value = res.total || 0
  } catch {
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchList()
}

// Tab 切换
const handleTabChange = (status: number | undefined) => {
  currentStatus.value = status
  currentPage.value = 1
  fetchList()
}

// 分页
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchList()
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchList()
}

// 详情抽屉
const detailVisible = ref(false)
const selectedEssay = ref<ReadingEssaysInfo | null>(null)

const handleShowDetail = (essay: ReadingEssaysInfo) => {
  selectedEssay.value = essay
  detailVisible.value = true
}

const handleEditFromDetail = () => {
  if (!selectedEssay.value) return
  detailVisible.value = false
  essayDrawerStore.openEdit(selectedEssay.value.id)
}

const copyShareLink = async () => {
  if (!selectedEssay.value) return
  const url = `${window.location.origin}/reading-essays/${selectedEssay.value.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}

const handleCommentAdded = () => {
  if (selectedEssay.value) {
    selectedEssay.value.commentSum = (selectedEssay.value.commentSum || 0) + 1
  }
}

// 编辑
const handleEdit = (essay: ReadingEssaysInfo) => {
  essayDrawerStore.openEdit(essay.id)
}

// 删除
const handleDelete = async (essay: ReadingEssaysInfo) => {
  try {
    await dictumApi.deleteDictum(essay.id, essay.groupId)
    message.success('删除成功')
    fetchList()
  } catch {
  }
}

// 监听编辑抽屉关闭后刷新列表
watch(() => essayDrawerStore.visible, (val) => {
  if (!val) fetchList()
})

onMounted(() => fetchList())
</script>

<style scoped>
.admin-essays {
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
  flex-direction: column;
  gap: var(--space-3);
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.header-top {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.search-bar {
  flex: 1;
}

/* 状态 Tab */
.status-tabs {
  display: flex;
  gap: var(--space-1);
}

.tab-item {
  padding: 4px 12px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}

.tab-item:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.tab-item.active {
  color: var(--color-primary);
  background: var(--color-primary-light);
  font-weight: 500;
}

/* 列表区域 */
.list-section {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-4);
  box-shadow: var(--shadow-sm);
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
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

.essay-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.essay-list::-webkit-scrollbar {
  width: 6px;
}

.essay-list::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.essay-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

/* 随笔卡片 */
.essay-item {
  padding: var(--space-4);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  transition: all var(--transition-base);
}

.essay-item:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-md);
}

/* 随笔内容 */
.essay-content {
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  color: var(--color-ink);
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.essay-content:hover {
  color: var(--color-primary);
}

/* 作者和作品 */
.essay-source {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  font-style: italic;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
}

.source-sep {
  color: var(--color-ink-faint);
}

.source-author {
  color: var(--color-ink-light);
}

.source-work {
  color: var(--color-ink-muted);
}

/* 分类和标签 */
.essay-meta {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  flex-wrap: wrap;
}

.meta-badge {
  font-size: var(--text-xs);
  padding: 2px 8px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-full);
  color: var(--color-ink-light);
}

.state-badge {
  padding: 2px 8px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
}

.state-public {
  background: rgba(34, 197, 94, 0.1);
  color: #22c55e;
}

.state-private {
  background: rgba(107, 114, 128, 0.1);
  color: #6b7280;
}

.meta-labels {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.label-tag {
  font-size: 11px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

/* 底部 */
.essay-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: var(--space-2);
  border-top: 1px solid var(--color-border-light);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.admin-stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.footer-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

/* 分页 */
.pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding-top: var(--space-4);
}

/* 详情抽屉头部 */
.drawer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.drawer-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
}

.drawer-actions {
  display: flex;
  gap: 4px;
}
</style>
