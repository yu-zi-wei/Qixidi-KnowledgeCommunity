<template>
  <div class="article-page">
    <!-- 搜索栏 -->
    <div class="article-search">
      <n-input
        v-model:value="searchKeyword"
        placeholder="搜索文章标题"
        clearable
      >
        <template #prefix>
          <n-icon><Search /></n-icon>
        </template>
      </n-input>
    </div>

    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!articleList.length" class="empty-state">
      <CommonEmptyState :description="apiKeyword ? '没有找到相关文章' : '暂无文章'" />
    </div>

    <!-- 文章列表 -->
    <template v-else>
      <CommonArticleList
        ref="articleListRef"
        :article-list="articleList"
        @save-state="handleSaveState"
      />

      <!-- 分页 -->
      <div v-if="total > pageSize" class="article-pagination">
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
import { Search } from '@vicons/tabler'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const api = useApi()

const uid = computed(() => route.params.uid as string)
const pageSize = 20
const currentPage = computed(() => Number(route.query.page) || 1)
const searchKeyword = ref((route.query.search as string) || '')
const apiKeyword = ref(searchKeyword.value)
const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)

// 防抖：输入停止 400ms 后触发搜索
let debounceTimer: ReturnType<typeof setTimeout> | null = null
watch(searchKeyword, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    apiKeyword.value = val
  }, 400)
})

const cacheKey = computed(() => `user-home-article-${uid.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: pageData, pending } = useAsyncData(
  cacheKey,
  () => api.getPage<any>('/white/article/user/list', {
    pageNum: currentPage.value,
    pageSize,
    userId: uid.value,
    auditState: 2,
    articleTitle: apiKeyword.value || undefined
  })
)

const articleList = computed(() => pageData.value?.rows || [])
const total = computed(() => pageData.value?.total || 0)

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

// 同步 URL search 参数到本地
watch(() => route.query.search, (val) => {
  const keyword = (val as string) || ''
  searchKeyword.value = keyword
  apiKeyword.value = keyword
})

const handleSaveState = (scrollTop: number) => {
  if (import.meta.client) {
    history.replaceState({
      ...(window.history.state || {}),
      userArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置
onMounted(() => {
  if (import.meta.client && window.history.state?.userArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.userArticleScroll)
    })
  }
})
</script>

<style>
/* 非 scoped，确保 SSR 可用 */
.article-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.article-search {
  flex-shrink: 0;
  padding: 0 24px 12px;
}

.article-page .loading-state,
.article-page .empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.article-page .article-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding: 12px 0 16px;
  background: var(--color-surface-warm);
  border-top: 1px solid var(--color-border-light);
}
</style>
