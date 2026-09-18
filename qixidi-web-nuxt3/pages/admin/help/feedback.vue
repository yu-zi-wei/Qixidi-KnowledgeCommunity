<template>
  <div class="feedback-page">
    <!-- 提交反馈 -->
    <section class="feedback-form-section">
      <h3 class="section-title">提交反馈</h3>
      <div class="feedback-form">
        <n-input
          v-model:value="form.feedbackTitle"
          placeholder="反馈标题（必填）"
          maxlength="50"
          show-count
        />
        <div class="form-editor">
          <CommonMdEditorWithVideo
            v-model="form.feedbackContent"
            placeholder="详细描述你遇到的问题或建议…"
            :toolbars="editorToolbars"
          />
        </div>
        <div class="form-footer">
          <span class="form-hint">遇到问题或有好想法，欢迎告诉我们</span>
          <n-button type="primary" :loading="submitting" :disabled="!canSubmit" @click="handleSubmit">
            提交反馈
          </n-button>
        </div>
      </div>
    </section>

    <!-- 反馈列表 -->
    <section class="feedback-list-section">
      <div class="list-header">
        <n-tabs type="segment" size="small" :value="listMode" @update:value="switchMode">
          <n-tab name="all">反馈墙</n-tab>
          <n-tab name="mine">我的反馈</n-tab>
        </n-tabs>
        <div class="header-filters">
          <button
            v-for="item in sumItems"
            :key="item.label"
            type="button"
            class="filter-chip"
            :class="{ active: activeStatus === item.value }"
            @click="switchStatus(item.value)"
          >
            <span class="chip-count">{{ item.count }}</span>
            <span class="chip-label">{{ item.label }}</span>
          </button>
        </div>
        <n-button size="small" quaternary @click="loadData">
          <template #icon><n-icon><Refresh /></n-icon></template>
          刷新
        </n-button>
      </div>

      <div class="list-body">
        <div v-if="loading" class="list-loading">
          <n-skeleton v-for="i in 3" :key="i" :sharp="false" height="72px" />
        </div>

        <div v-else-if="feedbackList.length === 0" class="list-empty">
          <n-empty description="暂无反馈" />
        </div>

        <ul v-else class="feedback-list">
          <li
            v-for="item in feedbackList"
            :key="item.id"
            class="feedback-item"
            @click="openDetail(item)"
          >
            <div class="item-main">
              <span class="item-title">{{ item.feedbackTitle }}</span>
              <span class="item-status" :class="`status-${item.status}`">{{ statusText(item.status) }}</span>
            </div>
            <div class="item-meta">
              <span class="meta-author">{{ item.nickname }}</span>
              <span class="meta-dot">·</span>
              <time :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
            </div>
          </li>
        </ul>
      </div>

      <div class="list-pagination">
        <n-pagination
          v-if="total > pageSize"
          :page="pageNum"
          :page-size="pageSize"
          :item-count="total"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </section>

    <!-- 反馈详情 -->
    <AdminFeedbackDetail
      :show="detailVisible"
      :feedback="activeFeedback"
      @close="detailVisible = false"
      @updated="handleDetailUpdated"
      @deleted="handleDetailDeleted"
    />
  </div>
</template>

<script setup lang="ts">
import { Refresh } from '@vicons/tabler'
import { FeedbackStatus, FeedbackStatusOptions, type FeedbackStatusSumVo, type FeedbackVo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const authStore = useAuthStore()
const feedbackApi = useFeedbackApi()
const message = useMessage()

const myUid = computed(() => authStore.user?.uuid)

// 表单（内容为 Markdown）
const form = reactive({ feedbackTitle: '', feedbackContent: '' })
const submitting = ref(false)
const canSubmit = computed(() => form.feedbackTitle.trim().length > 0)

// 轻量编辑工具栏（反馈场景不需要表格/代码块/视频等重功能）
const editorToolbars = [
  'bold', 'underline', 'italic', 'strikeThrough', '-',
  'unorderedList', 'orderedList', 'quote', '-',
  'codeRow', 'link', 'image', '-',
  'revoke', 'next', '=', 'preview'
]

// 列表状态
const listMode = ref<'all' | 'mine'>('all')
const activeStatus = ref<number | null>(null)
const loading = ref(false)
const feedbackList = ref<FeedbackVo[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10

// 详情弹窗
const detailVisible = ref(false)
const activeFeedback = ref<FeedbackVo | null>(null)

// 状态汇总
const statusSum = ref<FeedbackStatusSumVo | null>(null)
const sumItems = computed(() => [
  { label: '全部', value: null as number | null, count: statusSum.value?.allData || 0 },
  { label: '待处理', value: FeedbackStatus.TO_BE_PROCESSED as number | null, count: statusSum.value?.toBeProcessed || 0 },
  { label: '进行中', value: FeedbackStatus.UNDER_WAY as number | null, count: statusSum.value?.underWay || 0 },
  { label: '已完成', value: FeedbackStatus.COMPLETED as number | null, count: statusSum.value?.completed || 0 },
  { label: '已关闭', value: FeedbackStatus.CLOSED as number | null, count: statusSum.value?.closed || 0 }
])

const statusText = (status: number) => FeedbackStatusOptions.find(o => o.value === status)?.label || '未知'

// 数据加载（列表 + 汇总）
const loadData = async () => {
  loading.value = true
  try {
    const params: Record<string, any> = {
      pageNum: pageNum.value,
      pageSize
    }
    if (listMode.value === 'mine' && myUid.value) params.uid = myUid.value
    if (activeStatus.value !== null) params.status = activeStatus.value
    const { rows, total: t } = await feedbackApi.getList(params)
    feedbackList.value = rows || []
    total.value = t || 0
    statusSum.value = await feedbackApi.getStatusSum()
  } finally {
    loading.value = false
  }
}

const switchMode = (mode: 'all' | 'mine') => {
  listMode.value = mode
  pageNum.value = 1
  loadData()
}

const switchStatus = (status: number | null) => {
  activeStatus.value = activeStatus.value === status ? null : status
  pageNum.value = 1
  loadData()
}

const handlePageChange = (page: number) => {
  pageNum.value = page
  loadData()
}

const handleSubmit = async () => {
  if (form.feedbackContent.length > 500) {
    message.warning('反馈内容不能超过 500 字')
    return
  }
  submitting.value = true
  try {
    await feedbackApi.addFeedback({
      feedbackTitle: form.feedbackTitle.trim(),
      feedbackContent: form.feedbackContent.trim()
    })
    message.success('反馈已提交，感谢你的输入')
    form.feedbackTitle = ''
    form.feedbackContent = ''
    pageNum.value = 1
    listMode.value = 'mine'
    loadData()
  } finally {
    submitting.value = false
  }
}

const openDetail = (item: FeedbackVo) => {
  activeFeedback.value = item
  detailVisible.value = true
}

// 详情内更新状态：同步列表项 + 刷新汇总
const handleDetailUpdated = (updated: FeedbackVo) => {
  const target = feedbackList.value.find(f => f.id === updated.id)
  if (target) target.status = updated.status
  feedbackApi.getStatusSum().then(sum => { statusSum.value = sum })
}

// 详情内删除：关闭弹窗 + 刷新列表
const handleDetailDeleted = (deleted: FeedbackVo) => {
  detailVisible.value = false
  activeFeedback.value = null
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
/* 自管滚动：外层 .admin-content 不滚，滚动收进列表区 */
.feedback-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow: hidden;
}

/* 表单区 */
.feedback-form-section {
  flex-shrink: 0;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0 0 4px;
}

.feedback-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-editor {
  height: 260px;
}

.form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.form-hint {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 列表区：吃掉剩余高度，内部 list-body 滚动，分页常驻底部 */
.feedback-list-section {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 20px 24px 16px;
  box-shadow: var(--shadow-sm);
}

/* 切换 tab + 状态筛选 + 刷新 同一行 */
.list-header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

/* segment tabs 缩小一号；n-tabs 默认占满整行，需收回让位给筛选 chips */
.list-header :deep(.n-tabs) {
  width: auto;
  flex-shrink: 0;
}

.list-header :deep(.n-tabs-tab) {
  padding: 3px 12px;
  font-size: var(--text-xs);
}

.header-filters {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 3px 12px;
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-full);
  background: var(--color-surface);
  cursor: pointer;
  transition: all var(--transition-base);
}

.filter-chip:hover {
  border-color: var(--color-primary);
}

.filter-chip.active {
  border-color: var(--color-primary);
  background: var(--color-primary-light, rgba(102, 126, 234, 0.08));
}

.chip-count {
  font-size: var(--text-sm);
  font-weight: 700;
  color: var(--color-ink);
}

.chip-label {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.filter-chip.active .chip-label {
  color: var(--color-ink);
}

/* 列表滚动容器：滚动条只出现在这里 */
.list-body {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.list-loading {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.list-empty {
  padding: 40px 0;
}

.feedback-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.feedback-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 10px 8px;
  border-bottom: 1px solid var(--color-border-light);
  cursor: pointer;
  transition: background var(--transition-base);
}

.feedback-item:last-child {
  border-bottom: none;
}

.feedback-item:hover {
  background: var(--color-surface-warm);
}

.item-main {
  display: flex;
  align-items: center;
  gap: 10px;
}

.item-title {
  flex: 1;
  min-width: 0;
  font-size: var(--text-base);
  font-weight: 500;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-status {
  flex-shrink: 0;
  font-size: var(--text-xs);
  padding: 1px 10px;
  border-radius: var(--radius-full);
}

.item-status.status-1 { color: #d97706; background: rgba(217, 119, 6, 0.1); }
.item-status.status-2 { color: #2563eb; background: rgba(37, 99, 235, 0.1); }
.item-status.status-3 { color: #059669; background: rgba(5, 150, 105, 0.1); }
.item-status.status-4 { color: var(--color-ink-muted); background: var(--color-surface-dim); }

.item-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.meta-author {
  color: var(--color-ink-muted);
}

/* 分页常驻列表区底部，无需滚到页面最底 */
.list-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  margin-top: 12px;
}
</style>
