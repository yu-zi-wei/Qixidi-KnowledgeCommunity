<template>
  <div class="collection-detail-page">
    <!-- 错误状态 -->
    <div v-if="error || (!pending && !collectionData)" class="cl-error-state">
      <p style="color: var(--color-ink-muted);">该收藏夹可能已被删除或不存在</p>
      <n-button @click="navigateTo('/')">返回首页</n-button>
    </div>

    <!-- 正常内容 -->
    <template v-else-if="collectionData">
      <!-- 收藏夹头部信息 - 固定顶部 -->
      <header class="cl-header">
        <div class="cl-header-inner">
          <div class="cl-header-info">
            <h1 class="cl-name">{{ collectionData.collectionName }}</h1>
            <p class="cl-introduce" v-if="collectionData.collectionIntroduce">
              {{ collectionData.collectionIntroduce }}
            </p>
            <div class="cl-meta">
              <div class="cl-author" v-if="userInfo">
                <img v-if="userInfo.avatar" :src="userInfo.avatar" class="cl-author-avatar" :alt="userInfo.nickname || userInfo.username" />
                <div v-else class="cl-author-avatar cl-author-avatar-placeholder">
                  <n-icon><User /></n-icon>
                </div>
                <span class="cl-author-name">{{ userInfo.nickname || userInfo.username }}</span>
                <span v-if="userInfo.remark" class="cl-author-remark">{{ userInfo.remark }}</span>
              </div>
              <div class="cl-stats">
                <span class="cl-stat-item">
                  <n-icon><FileText /></n-icon>
                  {{ collectionData.includedCount || 0 }} 篇文章
                </span>
                <span class="cl-stat-item" v-if="collectionData.createTime" :title="getFullDateTime(collectionData.createTime)">
                  <n-icon><Calendar /></n-icon>
                  {{ formatTime(collectionData.createTime) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- 收藏文章标题 + 搜索 -->
      <div class="cl-section-header">
        <h2 class="cl-section-title">收藏文章</h2>
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索文章标题"
          clearable
          size="small"
          class="cl-search"
        >
          <template #prefix>
            <n-icon><Search /></n-icon>
          </template>
        </n-input>
      </div>

      <!-- 文章列表 - 组件 -->
      <CommonArticleList
        ref="articleListRef"
        :article-list="articleList"
        @save-state="handleSaveState"
      />

      <!-- 分页 - 固定底部 -->
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
import { FileText, Calendar, User, Eye, Heart, MessageCircle, Search } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'
import type { UserInfoVo } from '~/composables/useSpecialDetailApi'

definePageMeta({ layout: 'blank' })

const route = useRoute()
const collectionId = computed(() => Number(route.params.id))
const collectionApi = useCollectionApi()
const specialDetailApi = useSpecialDetailApi()

const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)
const pageSize = 20

// --- 收藏夹详情 ---
const { data: collectionData, pending, error } = await useAsyncData(
  `collection-detail-${collectionId.value}`,
  () => collectionApi.getCollectionDetail(collectionId.value),
  { server: true }
)

// --- 用户信息 ---
const { data: userInfo } = await useAsyncData(
  `collection-user-${collectionId.value}`,
  () => {
    if (!collectionData.value?.uid) return null
    return specialDetailApi.getUserInfo(collectionData.value.uid) as Promise<UserInfoVo>
  },
  { server: true, watch: [() => collectionData.value?.uid] }
)

// --- 文章列表 ---
const currentPage = computed(() => Number(route.query.page) || 1)
const searchKeyword = ref((route.query.search as string) || '')
const apiKeyword = ref(searchKeyword.value)
const articleList = ref<any[]>([])
const articleTotal = ref(0)

// 防抖：输入停止 400ms 后触发搜索
let debounceTimer: ReturnType<typeof setTimeout> | null = null
watch(searchKeyword, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    apiKeyword.value = val
  }, 400)
})

const cacheKey = computed(() => `collection-articles-${collectionId.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: articleData } = await useAsyncData(
  cacheKey,
  () => collectionApi.getCollectionArticles({
    collectionId: collectionId.value,
    pageNum: currentPage.value,
    pageSize,
    articleTitle: apiKeyword.value || undefined
  })
)

watch(() => articleData.value, (data) => {
  if (data) {
    articleList.value = data.rows || []
    articleTotal.value = data.total || 0
  }
}, { immediate: true })

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
      collectionArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置
onMounted(() => {
  if (import.meta.client && window.history.state?.collectionArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.collectionArticleScroll)
    })
  }
})

// SEO
useHead(() => ({
  title: collectionData.value?.collectionName ? `${collectionData.value.collectionName} - 栖息地` : '收藏夹 - 栖息地',
  meta: [
    { name: 'description', content: collectionData.value?.collectionIntroduce || collectionData.value?.collectionName || '' }
  ]
}))
</script>

<style>
/* collection/[id].vue - 非 scoped，确保 SSR 可用 */
.collection-detail-page {
  height: 100vh;
  width: 800px;
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.cl-error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex: 1;
}

/* --- 收藏夹头部 - 固定不滚动 --- */
.cl-header {
  flex-shrink: 0;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.cl-header-inner {
  padding: 20px 24px;
  display: flex;
  gap: 20px;
}

.cl-header-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cl-name {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  line-height: var(--leading-tight);
}

.cl-introduce {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: var(--leading-relaxed);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.cl-meta {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cl-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cl-author-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.cl-author-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.cl-author-name {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.cl-author-remark {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.cl-stats {
  display: flex;
  gap: 16px;
}

.cl-stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* --- 文章列表标题 - 固定 --- */
.cl-section-header {
  flex-shrink: 0;
  padding: 12px 24px;
  background: var(--color-surface-warm);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.cl-section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  white-space: nowrap;
}

.cl-search {
  flex: 1;
  min-width: 0;
}

/* --- 分页 - 固定底部 --- */
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
  .cl-header-inner {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .cl-author {
    justify-content: center;
  }

  .cl-stats {
    justify-content: center;
  }

  .cl-section-header {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
