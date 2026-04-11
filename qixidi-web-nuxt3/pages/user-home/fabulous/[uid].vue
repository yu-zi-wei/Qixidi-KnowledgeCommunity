<template>
  <div class="fabulous-page">
    <!-- 左侧菜单 -->
    <div class="fabulous-sidebar">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="fabulous-menu-item"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value"
      >
        <n-icon size="16"><component :is="tab.icon" /></n-icon>
        {{ tab.label }}
      </button>
    </div>

    <!-- 右侧内容 -->
    <div class="fabulous-content">
      <!-- 加载状态 -->
      <div v-if="pending" class="fabulous-loading">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!articleList.length" class="fabulous-empty">
        <n-empty description="暂无点赞的文章" />
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
      <div v-if="articleTotal > pageSize" class="fabulous-pagination">
        <n-pagination
          :page="currentPage"
          :item-count="articleTotal"
          :page-size="pageSize"
          :page-slot="5"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Heart } from '@vicons/tabler'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const fabulousApi = useFabulousApi()

const uid = computed(() => route.params.uid as string)
const activeTab = ref(1)
const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)
const pageSize = 20

const tabs = [
  { value: 1, label: '点赞的文章', icon: Heart }
]

// --- 文章列表 ---
const currentPage = computed(() => Number(route.query.page) || 1)

const cacheKey = computed(() => `fabulous-articles-${uid.value}-${currentPage.value}`)

const { data: articleData, pending } = await useAsyncData(
  cacheKey,
  () => fabulousApi.getFabulousArticleList(uid.value, currentPage.value, pageSize)
)

const articleList = computed(() => articleData.value?.rows || [])
const articleTotal = computed(() => articleData.value?.total || 0)

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

const handleSaveState = (scrollTop: number) => {
  if (import.meta.client) {
    history.replaceState({
      ...(window.history.state || {}),
      fabulousArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置
onMounted(() => {
  if (import.meta.client && window.history.state?.fabulousArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.fabulousArticleScroll)
    })
  }
})
</script>

<style>
.fabulous-page {
  display: flex;
  flex: 1;
  min-height: 0;
}

/* 左侧菜单 */
.fabulous-sidebar {
  width: 140px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-right: 12px;
  border-right: 1px solid var(--color-border-light);
}

.fabulous-menu-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  width: 100%;
}

.fabulous-menu-item:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.fabulous-menu-item.active {
  color: var(--color-primary);
  font-weight: 600;
  background: var(--color-primary-light);
}

/* 右侧内容 */
.fabulous-content {
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow-y: auto;
  padding-left: 16px;
  display: flex;
  flex-direction: column;
}

.fabulous-content::-webkit-scrollbar {
  width: 4px;
}

.fabulous-content::-webkit-scrollbar-track {
  background: transparent;
}

.fabulous-content::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.fabulous-content::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.fabulous-loading,
.fabulous-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.fabulous-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding: 12px 0 16px;
  border-top: 1px solid var(--color-border-light);
}

@media (max-width: 640px) {
  .fabulous-page {
    flex-direction: column;
  }

  .fabulous-sidebar {
    width: 100%;
    flex-direction: row;
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    padding-right: 0;
    padding-bottom: 8px;
  }

  .fabulous-content {
    padding-left: 0;
    padding-top: 12px;
  }
}
</style>
