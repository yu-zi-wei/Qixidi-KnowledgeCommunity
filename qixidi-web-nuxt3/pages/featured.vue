<template>
  <div>
    <ArticleList
      :articles="articles"
      :loading="loadingMore"
      :no-more="noMore"
      @load-more="loadMore"
    />
  </div>
</template>

<script setup lang="ts">
definePageMeta({
  showTabBar: true,
  sidebar: 'home'
})

const route = useRoute()
const articleApi = useArticleApi()
const labelStore = useLabelStore()

const labelList = computed(() => labelStore.labelList)

// 确保标签列表已加载（客户端）
if (import.meta.client && !labelStore.loaded) {
  await labelStore.fetchLabelList()
}

// Store key：根据 groupingId 动态生成，不同分类的数据分开存储
const storeKey = computed(() => {
  const groupingId = route.query.groupingId as string | undefined
  return groupingId ? `featured-group-${groupingId}` : 'featured'
})

// 使用无限滚动 Hook
const { articles, loadingMore, noMore, loadMore } = useInfiniteScroll({
  fetchFn: (page) => {
    const groupingId = route.query.groupingId
    if (groupingId) {
      // 按分类筛选
      return articleApi.getSortList({
        pageNum: page,
        pageSize: 10,
        createTime: 1,
        sortType: 1,
        groupingId: Number(groupingId)
      })
    }
    // 默认精选
    return articleApi.getRecommendList(page, 10)
  },
  storeKey: storeKey.value
})
</script>
