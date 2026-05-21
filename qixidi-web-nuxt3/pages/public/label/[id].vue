<template>
  <div class="label-detail-page">
    <!-- 错误状态 -->
    <div v-if="error || (!pending && !labelData)" class="lb-error-state">
      <p style="color: var(--color-ink-muted);">该标签可能已被删除或不存在</p>
      <n-button @click="navigateTo('/')">返回首页</n-button>
    </div>

    <!-- 正常内容 -->
    <template v-else-if="labelData">
      <!-- 标签头部信息 -->
      <header class="lb-header">
        <div class="lb-header-inner">
          <div class="lb-cover" v-if="labelData.labelCover" v-html="labelData.labelCover" />
          <div class="lb-cover lb-cover-placeholder" v-else>
            <n-icon size="36"><Tag /></n-icon>
          </div>
          <div class="lb-header-info">
            <h1 class="lb-name">{{ labelData.labelName }}</h1>
            <p class="lb-desc" v-if="labelData.labelDescribe">{{ labelData.labelDescribe }}</p>
            <div class="lb-meta">
              <div class="lb-stats">
                <span class="lb-stat-item">
                  <n-icon><FileText /></n-icon>
                  {{ labelData.articleNumber || 0 }} 篇文章
                </span>
                <span class="lb-stat-item">
                  <n-icon><Users /></n-icon>
                  {{ labelData.followNumber || 0 }} 关注
                </span>
                <span class="lb-stat-item" v-if="labelData.createTime" :title="getFullDateTime(labelData.createTime)">
                  <n-icon><Calendar /></n-icon>
                  {{ formatTime(labelData.createTime) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- 文章标题 + 搜索 -->
      <div class="lb-section-header">
        <h2 class="lb-section-title">相关文章</h2>
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索文章标题"
          clearable
          size="small"
          class="lb-search"
        >
          <template #prefix>
            <n-icon><Search /></n-icon>
          </template>
        </n-input>
      </div>

      <!-- 加载状态 -->
      <div v-if="articlePending" class="lb-loading">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!articleList.length" class="lb-empty">
        <CommonEmptyState :description="apiKeyword ? '没有找到相关文章' : '暂无文章'" />
      </div>

      <!-- 文章列表 -->
      <template v-else>
        <CommonArticleList
          ref="articleListRef"
          :article-list="articleList"
          @save-state="handleSaveState"
        />
      </template>

      <!-- 分页 -->
      <div v-if="articleTotal > pageSize" class="article-pagination">
        <n-pagination
          :page="currentPage"
          :item-count="articleTotal"
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
import { Tag, FileText, Users, Calendar, Search } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

definePageMeta({ layout: 'blank' })

const { siteName } = useRuntimeConfig().public

const route = useRoute()
const api = useApi()
const labelApi = useLabelApi()

const labelId = computed(() => Number(route.params.id))
const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)
const pageSize = 20

// --- 标签详情 ---
const { data: labelData, pending, error } = await useAsyncData(
  `label-detail-${labelId.value}`,
  () => labelApi.getLabelInfo(labelId.value),
  { server: true }
)

// --- 文章列表 ---
const currentPage = computed(() => Number(route.query.page) || 1)
const searchKeyword = ref((route.query.search as string) || '')
const apiKeyword = ref(searchKeyword.value)

// 防抖
let debounceTimer: ReturnType<typeof setTimeout> | null = null
watch(searchKeyword, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    apiKeyword.value = val
  }, 400)
})

const cacheKey = computed(() => `label-articles-${labelId.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: articleData, pending: articlePending } = await useAsyncData(
  cacheKey,
  () => api.getPage<any>('/white/article/list', {
    pageNum: currentPage.value,
    pageSize,
    labelId: String(labelId.value),
    auditState: 2,
    articleTitle: apiKeyword.value || undefined
  })
)

const articleList = computed(() => articleData.value?.rows || [])
const articleTotal = computed(() => articleData.value?.total || 0)

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

// 同步 URL search 参数
watch(() => route.query.search, (val) => {
  const keyword = (val as string) || ''
  searchKeyword.value = keyword
  apiKeyword.value = keyword
})

const handleSaveState = (scrollTop: number) => {
  if (import.meta.client) {
    history.replaceState({
      ...(window.history.state || {}),
      labelArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置
onMounted(() => {
  if (import.meta.client && window.history.state?.labelArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.labelArticleScroll)
    })
  }
})

// SEO
useHead(() => ({
  title: labelData.value?.labelName ? `标签详情-${labelData.value.labelName}` : '标签',
  meta: [
    { name: 'description', content: labelData.value?.labelDescribe || labelData.value?.labelName || '' }
  ]
}))
</script>

<style>
.label-detail-page {
  height: 100vh;
  width: 800px;
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.lb-error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex: 1;
}

/* --- 标签头部 --- */
.lb-header {
  flex-shrink: 0;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.lb-header-inner {
  padding: 20px 24px;
  display: flex;
  gap: 20px;
  align-items: center;
}

.lb-cover {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lb-cover :deep(svg) {
  width: 100%;
  height: 100%;
  display: block;
}

.lb-cover-placeholder {
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.lb-header-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.lb-name {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  line-height: var(--leading-tight);
}

.lb-desc {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: var(--leading-relaxed);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.lb-meta {
  margin-top: auto;
}

.lb-stats {
  display: flex;
  gap: 16px;
}

.lb-stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* --- 文章标题 + 搜索 --- */
.lb-section-header {
  flex-shrink: 0;
  padding: 12px 24px;
  background: var(--color-surface-warm);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.lb-section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  white-space: nowrap;
}

.lb-search {
  flex: 1;
  min-width: 0;
}

.lb-loading,
.lb-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* --- 分页 --- */
.article-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding: 12px 24px 16px;
  background: var(--color-surface-warm);
  border-top: 1px solid var(--color-border-light);
}

/* --- 响应式 --- */
@media (max-width: 640px) {
  .lb-header-inner {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .lb-stats {
    justify-content: center;
  }

  .lb-section-header {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
