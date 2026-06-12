<template>
  <div class="home-page">
    <div v-if="articles.length === 0 && !loadingMore" class="empty-state">
      <p>暂无文章</p>
    </div>

    <ArticleList
      v-show="articles.length > 0"
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

useHead({
  title: '首页',
  bodyAttrs: {
    class: 'page-home'
  }
})

// 使用无限滚动 Hook
const { articles, loadingMore, noMore, loadMore } = useInfiniteScroll({
  fetchFn: (page) => articleApi.getSortList({
    pageNum: page,
    pageSize: 10,
    createTime: 1
  }),
  storeKey: 'index' // 首页使用 'index' 作为 key
})
</script>

<style scoped>
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

<style>
@media (max-width: 768px) {
  body.page-home .home-main {
    padding-top: 210px !important;
  }
}
</style>
