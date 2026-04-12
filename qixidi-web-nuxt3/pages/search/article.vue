<template>
  <div>
    <div v-if="searching" class="search-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!articleList.length" class="search-empty">
      <n-empty :description="keyword ? '未找到相关文章' : '请输入搜索关键词'" />
    </div>
    <ArticleList
      v-else
      :articles="articleList"
      :loading="loadingMore"
      :no-more="noMore"
      @load-more="loadMore"
    />
  </div>
</template>

<script setup lang="ts">
import type { ArticleInfo } from '~/types'

definePageMeta({ showTabBar: false })

const route = useRoute()
const searchApi = useSearchApi()

const keyword = computed(() => (route.query.q as string || '').trim())

const articleList = ref<ArticleInfo[]>([])
const articleTotal = ref(0)
const articlePage = ref(1)
const searching = ref(false)
const loadingMore = ref(false)
const noMore = computed(() => articleList.value.length >= articleTotal.value && articleTotal.value > 0)

const searchArticles = async (reset = true) => {
  if (!keyword.value) return
  if (reset) {
    searching.value = true
    articlePage.value = 1
  }
  try {
    const res = await searchApi.searchArticles({ pageNum: articlePage.value, pageSize: 20, articleTitle: keyword.value })
    if (reset) {
      articleList.value = res.rows || []
    } else {
      articleList.value.push(...(res.rows || []))
    }
    articleTotal.value = res.total || 0
  } finally {
    searching.value = false
  }
}

const loadMore = async () => {
  if (loadingMore.value || noMore.value) return
  loadingMore.value = true
  articlePage.value++
  try {
    await searchArticles(false)
  } finally {
    loadingMore.value = false
  }
}

watch(keyword, () => searchArticles())
onMounted(() => searchArticles())
</script>
