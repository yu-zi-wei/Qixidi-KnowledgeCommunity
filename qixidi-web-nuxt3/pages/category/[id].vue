<template>
  <div class="category-page">
    <!-- 推荐 / 最新 切换标签 -->
    <div class="custom-tabs">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="tab-button"
        :class="{ active: activeTab === tab.value }"
        @click="handleTabChange(tab.value)"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- 使用 ClientOnly 避免滚动条闪烁 -->
    <ClientOnly>
      <!-- 加载状态 -->
      <div v-if="articles.length === 0 && !loadingMore" class="empty-state">
        <p>暂无文章</p>
      </div>

      <ArticleList
        v-show="articles.length > 0"
        :articles="articles"
        :loading="loadingMore"
        :no-more="noMore"
        hide-cover
        @load-more="loadMore"
      />
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
definePageMeta({
  showTabBar: true,
  sidebar: 'home'
})

useHead({ bodyAttrs: { class: 'page-home' } })

const route = useRoute()
const articleApi = useArticleApi()
const labelStore = useLabelStore()

// 确保标签列表已加载（客户端）
if (import.meta.client && !labelStore.loaded) {
  await labelStore.fetchLabelList()
}

// 获取当前分类 ID
const groupingId = computed(() => Number(route.params.id))

// 标签配置
const tabs = [
  { label: '最新', value: 'latest', sortType: 2 },  // 按时间
  { label: '推荐', value: 'recommend', sortType: 1 }  // 按权重
]

// 当前激活的标签（默认最新）
const activeTab = ref('latest')

// Store key：根据分类 ID 和当前标签动态生成
const storeKey = computed(() => `category-${route.params.id}-${activeTab.value}`)

// 使用无限滚动 Hook
const { articles, loadingMore, noMore, loadMore, initArticles } = useInfiniteScroll({
  fetchFn: (page) => articleApi.getCategoryList({
    pageNum: page,
    pageSize: 10,
    sortType: activeTab.value === 'latest' ? 2 : 1,  // 最新=2, 推荐=1
    groupingId: groupingId.value
  }),
  storeKey: storeKey
})

// 标签切换时重新加载数据
const handleTabChange = (value: string) => {
  if (activeTab.value === value) return
  activeTab.value = value
  // 强制重新加载数据
  initArticles(true)
}
</script>

<style scoped>
.category-page {
  min-height: 100vh;
}

/* === 自定义 Tabs === */
.custom-tabs {
  display: flex;
  gap: 4px;
  margin-bottom: 16px;
  padding: 0 4px;
}

.tab-button {
  padding: 6px 14px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink-light);
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.tab-button:hover {
  color: var(--color-ink);
}

.tab-button.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

/* === 空状态 === */
.empty-state {
  text-align: center;
  padding: 60px var(--space-5);
  color: var(--color-ink-muted);
}

.empty-state p {
  font-size: var(--text-sm);
  margin: 0;
}
</style>
