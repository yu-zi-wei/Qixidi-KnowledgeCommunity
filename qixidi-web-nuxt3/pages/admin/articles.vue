<template>
  <div class="admin-articles">
    <!-- 顶部区域：搜索框 + Tab 切换 -->
    <div class="header-section">
      <!-- 搜索框 -->
      <div class="search-bar">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索文章标题..."
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <n-icon><Search /></n-icon>
          </template>
        </n-input>
        <n-button @click="handleSearch">搜索</n-button>
        <n-button v-if="authStore.isCreator" type="primary" @click="handleCreate">
          <template #icon>
            <n-icon><Plus /></n-icon>
          </template>
          写文章
        </n-button>
      </div>

      <!-- 状态切换 Tab - 下划线样式 -->
      <div class="status-tabs">
        <div
          v-for="tab in statusTabs"
          :key="String(tab.value)"
          :class="['admin-article-tab', { active: currentStatus === tab.value }]"
          @click="handleTabChange(tab.value)"
        >
          {{ tab.label }}
        </div>
      </div>
    </div>

    <!-- 文章列表区域 -->
    <div class="list-section">
      <!-- 加载状态 -->
      <div v-if="pending" class="loading-state">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!articleList.length" class="empty-state">
        <CommonEmptyState description="暂无文章" />
      </div>

      <!-- 文章列表 -->
      <template v-else>
        <div class="list-header">
          <span class="total-count">共 {{ total }} 篇文章</span>
        </div>
        <div class="article-list">
          <div
            v-for="article in articleList"
            :key="article.id"
            class="article-item"
          >
            <!-- 封面图 -->
            <div class="article-cover" v-if="article.articleCover">
              <img :src="article.articleCover" :alt="article.articleTitle" />
            </div>
            <div class="article-cover cover-placeholder" v-else>
              <n-icon size="28"><FileText /></n-icon>
            </div>

            <!-- 文章信息 -->
            <div class="article-content">
              <h3 class="article-title">
                <NuxtLink :to="`/articles/${article.id}`" target="_blank" class="article-title-link">
                  {{ article.articleTitle }}
                </NuxtLink>
              </h3>
              <p v-if="article.articleAbstract" class="article-abstract">{{ article.articleAbstract }}</p>

              <!-- 统计数据 -->
              <div class="article-stats">
                <span class="admin-stat">
                  <n-icon><Eye /></n-icon>
                  {{ article.numberTimes || 0 }}
                </span>
                <span class="admin-stat">
                  <n-icon><ThumbUp /></n-icon>
                  {{ article.likeTimes || 0 }}
                </span>
                <span class="admin-stat">
                  <n-icon><MessageCircle /></n-icon>
                  {{ article.commentTimes || 0 }}
                </span>
              </div>

              <!-- 底部：时间和状态 + 操作按钮 -->
              <div class="article-footer">
                <span class="meta-time">
                  <n-icon><Clock /></n-icon>
                  {{ formatTime(article.createTime) }}
                </span>
                <span class="status-badge" :class="getStatusClass(article.auditState)">
                  {{ getStatusText(article.auditState) }}
                </span>
                <span class="footer-spacer"></span>
                <n-button text size="tiny" type="primary" @click="handleEdit(article.id)">
                  编辑
                </n-button>
                <n-popconfirm @positive-click="handleDelete(article.id)">
                  <template #trigger>
                    <n-button text size="tiny" type="error">删除</n-button>
                  </template>
                  确定要删除这篇文章吗？
                </n-popconfirm>
              </div>
            </div>
          </div>
        </div>
      </template>

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
        <span class="pagination-total">共 {{ total }} 条</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Search, FileText, Eye, ThumbUp, MessageCircle, Clock, Plus } from '@vicons/tabler'
import type { AdminArticleItem } from '~/types'
import { ArticleAuditState } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const route = useRoute()
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
const articleApi = useAdminArticleApi()

// 状态 Tab 配置
const statusTabs = [
  { label: '全部', value: undefined },
  { label: '已发布', value: ArticleAuditState.PUBLISHED },
  { label: '审核中', value: ArticleAuditState.REVIEWING },
  { label: '草稿', value: ArticleAuditState.DRAFT }
]

// 从 URL 恢复状态
const searchKeyword = ref(String(route.query.keyword || ''))
const currentStatus = ref(route.query.status ? Number(route.query.status) : undefined)
const currentPage = ref(Number(route.query.page) || 1)
const pageSize = ref(Number(route.query.pageSize) || 20)

// 文章列表数据
const articleList = ref<AdminArticleItem[]>([])
const total = ref(0)
const pending = ref(false)

// 同步状态到 URL
const syncToUrl = () => {
  const query: Record<string, any> = {
    page: currentPage.value,
    pageSize: pageSize.value
  }
  if (searchKeyword.value) query.keyword = searchKeyword.value
  if (currentStatus.value !== undefined) query.status = currentStatus.value
  router.replace({ query })
}

// 获取文章列表
const fetchArticles = async () => {
  pending.value = true
  try {
    const result = await articleApi.getArticleList({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      articleTitle: searchKeyword.value || undefined,
      auditState: currentStatus.value
    })
    articleList.value = result.rows || []
    total.value = result.total || 0
  } catch {
  } finally {
    pending.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  syncToUrl()
  fetchArticles()
}

// Tab 切换
const handleTabChange = (status: number | undefined) => {
  currentStatus.value = status
  currentPage.value = 1
  syncToUrl()
  fetchArticles()
}

// 分页切换
const handlePageChange = (page: number) => {
  currentPage.value = page
  syncToUrl()
  fetchArticles()
}

// 每页条数切换
const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  syncToUrl()
  fetchArticles()
}

// 新建文章
const handleCreate = () => {
  router.push('/write/article')
}

// 编辑文章
const handleEdit = (id: number) => {
  router.push(`/write/article/${id}`)
}

// 删除文章
const handleDelete = async (id: number) => {
  try {
    await articleApi.deleteArticle(id)
    message.success('删除成功')
    fetchArticles()
  } catch {
  }
}

// 获取状态文本
const getStatusText = (state: number): string => {
  switch (state) {
    case ArticleAuditState.REVIEWING: return '审核中'
    case ArticleAuditState.PUBLISHED: return '已发布'
    case ArticleAuditState.REJECTED: return '审核不通过'
    case ArticleAuditState.DRAFT: return '草稿'
    default: return '未知'
  }
}

// 获取状态样式类
const getStatusClass = (state: number): string => {
  switch (state) {
    case ArticleAuditState.REVIEWING: return 'status-reviewing'
    case ArticleAuditState.PUBLISHED: return 'status-published'
    case ArticleAuditState.REJECTED: return 'status-rejected'
    case ArticleAuditState.DRAFT: return 'status-draft'
    default: return ''
  }
}

// 初始化
onMounted(() => fetchArticles())
</script>

<style scoped>
.admin-articles {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow: hidden;
}

/* 顶部区域 */
.header-section {
  flex-shrink: 0;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 16px;
  box-shadow: var(--shadow-sm);
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.search-bar .n-input {
  flex: 1;
}

/* Tab 下划线样式 - 覆盖全局样式 */
.status-tabs {
  display: flex;
  gap: 24px;
  border-bottom: 1px solid var(--color-border-light);
}

.status-tabs .admin-article-tab {
  position: relative;
  padding: 8px 4px 12px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  background: transparent;
  border-radius: 0;
  cursor: pointer;
  transition: color var(--transition-base);
}

.status-tabs .admin-article-tab:hover {
  background: transparent;
  color: var(--color-ink-light);
}

.status-tabs .admin-article-tab.active {
  background: transparent;
  color: var(--color-primary);
  font-weight: 500;
}

.status-tabs .admin-article-tab.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--color-primary);
  border-radius: 1px;
}

/* 列表区域 */
.list-section {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 16px;
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-header {
  flex-shrink: 0;
  padding-bottom: 12px;
  margin-bottom: 12px;
  border-bottom: 1px solid var(--color-border-light);
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.article-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-right: 8px;
  margin-right: -8px;
}

.article-list::-webkit-scrollbar {
  width: 6px;
}

.article-list::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.article-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

/* 文章卡片 */
.article-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  transition: all var(--transition-base);
}

.article-item:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-md);
}

/* 封面图 */
.article-cover {
  width: 120px;
  height: 80px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
  background: var(--color-surface-dim);
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
}

.article-item:hover .article-cover img {
  transform: scale(1.05);
}

.cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

/* 文章内容 */
.article-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.article-title {
  font-size: var(--text-base);
  font-weight: 600;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-title-link {
  color: var(--color-ink);
  text-decoration: none;
  transition: color var(--transition-base);
}

.article-title-link:hover {
  color: var(--color-primary);
}

.article-abstract {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 4px 0 0;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 统计数据 */
.article-stats {
  display: flex;
  align-items: center;
  gap: 16px;
}

.admin-stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.admin-stat .n-icon {
  font-size: 14px;
}

/* 底部行：元信息 + 操作按钮 */
.article-footer {
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer-spacer {
  flex: 1;
}

.meta-time {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.meta-time .n-icon {
  font-size: 13px;
}

/* 状态徽章 */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
  white-space: nowrap;
}

.status-published {
  background: rgba(34, 197, 94, 0.1);
  color: #22c55e;
}

.status-reviewing {
  background: rgba(234, 179, 8, 0.1);
  color: #eab308;
}

.status-rejected {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.status-draft {
  background: rgba(107, 114, 128, 0.1);
  color: #6b7280;
}

/* 操作按钮 */
.article-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.article-actions .n-button {
  font-size: 12px;
}

/* 分页 */
.pagination {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding-top: 16px;
  margin-top: 12px;
  border-top: 1px solid var(--color-border-light);
}

.pagination-total {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 响应式 */
@media (max-width: 768px) {
  .article-item {
    flex-wrap: wrap;
    padding: 12px;
  }

  .article-cover {
    width: 48px;
    height: 36px;
  }

  .article-info {
    width: calc(100% - 62px);
  }

  .article-actions {
    width: 100%;
    justify-content: flex-end;
    margin-top: 8px;
    padding-top: 8px;
    border-top: 1px solid var(--color-border-light);
  }

  .status-tabs {
    gap: 16px;
    overflow-x: auto;
  }
}
</style>
