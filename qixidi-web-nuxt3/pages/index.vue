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

// 使用无限滚动 Hook
const { articles, loadingMore, noMore, loadMore } = useInfiniteScroll({
  fetchFn: (page) => articleApi.getSortList({
    pageNum: page,
    pageSize: 10,
    createTime: 1,
    sortType: 1
  }),
  storeKey: 'index' // 首页使用 'index' 作为 key
})
</script>
