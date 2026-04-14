<template>
  <div class="special-detail-page">
    <!-- 错误状态 -->
    <div v-if="error || (!pending && !specialData)" class="sp-error-state">
      <p style="color: var(--color-ink-muted);">该专栏可能已被删除或不存在</p>
      <n-button @click="navigateTo('/')">返回首页</n-button>
    </div>

    <!-- 正常内容 -->
    <template v-else-if="specialData">
      <!-- 专栏头部信息 - 固定顶部 -->
      <header class="sp-header">
        <div class="sp-header-inner">
          <div class="sp-cover" v-if="specialData.cover">
            <img :src="specialData.cover" :alt="specialData.specialName" />
          </div>
          <div class="sp-cover sp-cover-placeholder" v-else>
            <n-icon size="36"><Folder /></n-icon>
          </div>
          <div class="sp-header-info">
            <h1 class="sp-name">{{ specialData.specialName }}</h1>
            <p class="sp-introduce" v-if="specialData.specialIntroduce">
              {{ specialData.specialIntroduce }}
            </p>
            <div class="sp-meta">
              <div class="sp-author" v-if="userInfo">
                <img v-if="userInfo.avatar" :src="userInfo.avatar" class="sp-author-avatar" :alt="userInfo.nickname || userInfo.username" />
                <div v-else class="sp-author-avatar sp-author-avatar-placeholder">
                  <n-icon><User /></n-icon>
                </div>
                <span class="sp-author-name">{{ userInfo.nickname || userInfo.username }}</span>
                <span v-if="userInfo.remark" class="sp-author-remark">{{ userInfo.remark }}</span>
              </div>
              <div class="sp-stats">
                <span class="sp-stat-item">
                  <n-icon><FileText /></n-icon>
                  {{ specialData.includedCount || 0 }} 篇文章
                </span>
                <span class="sp-stat-item" v-if="specialData.createTime" :title="getFullDateTime(specialData.createTime)">
                  <n-icon><Calendar /></n-icon>
                  {{ formatTime(specialData.createTime) }}
                </span>
              </div>
            </div>
          </div>
          <div class="sp-actions" v-if="isOwner">
            <n-button size="small" @click="navigateTo(`/admin/column-content/${specialData.id}?name=${encodeURIComponent(specialData.specialName)}`)">
              内容管理
            </n-button>
          </div>
        </div>
      </header>

      <!-- 专栏文章标题 + 搜索 -->
      <div class="sp-section-header">
        <h2 class="sp-section-title">专栏文章</h2>
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索文章标题"
          clearable
          size="small"
          class="sp-search"
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
import { Folder, FileText, Calendar, User, Eye, Heart, MessageCircle, Search } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

definePageMeta({ layout: 'blank' })

const { siteName } = useRuntimeConfig().public

const route = useRoute()
const specialId = computed(() => Number(route.params.id))
const specialDetailApi = useSpecialDetailApi()

const authStore = useAuthStore()
const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)
const pageSize = 20

// --- 专栏详情 ---
const { data: specialData, pending, error } = await useAsyncData(
  `special-detail-${specialId.value}`,
  () => specialDetailApi.getSpecialDetail(specialId.value),
  { server: true }
)

// 是否是专栏拥有者
const isOwner = computed(() => authStore.user?.uuid && authStore.user.uuid === specialData.value?.uid)

// --- 用户信息 ---
const { data: userInfo } = await useAsyncData(
  `special-user-${specialId.value}`,
  () => {
    if (!specialData.value?.uid) return null
    return specialDetailApi.getUserInfo(specialData.value.uid)
  },
  { server: true, watch: [() => specialData.value?.uid] }
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

const cacheKey = computed(() => `special-articles-${specialId.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: articleData } = await useAsyncData(
  cacheKey,
  () => {
    if (!specialData.value?.uid) return null
    return specialDetailApi.getSpecialArticles({
      userId: specialData.value.uid,
      specialId: specialId.value,
      pageNum: currentPage.value,
      pageSize,
      articleTitle: apiKeyword.value || undefined
    })
  },
  { watch: [() => specialData.value?.uid] }
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
      specialArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置
onMounted(() => {
  if (import.meta.client && window.history.state?.specialArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.specialArticleScroll)
    })
  }
})

// SEO
useHead(() => ({
  title: specialData.value?.specialName ? `${specialData.value.specialName} - ${siteName}` : `专栏 - ${siteName}`,
  meta: [
    { name: 'description', content: specialData.value?.specialIntroduce || specialData.value?.specialName || '' }
  ]
}))
</script>

<style>
/* special/[id].vue - 非 scoped，确保 SSR 可用 */
.special-detail-page {
  height: 100vh;
  width: 800px;
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.sp-error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex: 1;
}

/* --- 专栏头部 - 固定不滚动 --- */
.sp-header {
  flex-shrink: 0;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.sp-header-inner {
  padding: 20px 24px;
  display: flex;
  gap: 20px;
}

.sp-cover {
  width: 140px;
  height: 100px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
}

.sp-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sp-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.sp-header-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sp-name {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  line-height: var(--leading-tight);
}

.sp-introduce {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: var(--leading-relaxed);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.sp-meta {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sp-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sp-author-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.sp-author-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.sp-author-name {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.sp-author-remark {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.sp-stats {
  display: flex;
  gap: 16px;
}

.sp-actions {
  flex-shrink: 0;
  align-self: flex-start;
}

.sp-stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* --- 文章列表标题 - 固定 --- */
.sp-section-header {
  flex-shrink: 0;
  padding: 12px 24px;
  background: var(--color-surface-warm);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.sp-section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  white-space: nowrap;
}

.sp-search {
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
  .sp-header-inner {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .sp-cover {
    width: 100%;
    height: 160px;
  }

  .sp-author {
    justify-content: center;
  }

  .sp-stats {
    justify-content: center;
  }

  .sp-section-header {
    flex-direction: column;
    align-items: stretch;
  }

  .sp-search {
    max-width: 100%;
  }
}
</style>
