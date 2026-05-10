<template>
  <div>
    <ArticleList
      :articles="articleList"
      :loading="loadingMore"
      :no-more="noMore"
      @load-more="loadMore"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { ArticleInfo } from '~/types'

definePageMeta({
  showTabBar: true,
  sidebar: 'home'
})

const pageMeta = useState('pageMeta')
pageMeta.value = {
  showTabBar: true,
  sidebar: 'home'
}

const route = useRoute()
const articleApi = useArticleApi()
const labelStore = useLabelStore()

const labelList = computed(() => labelStore.labelList)

// 确保标签列表已加载（客户端）
if (import.meta.client && !labelStore.loaded) {
  await labelStore.fetchLabelList()
}

// ============ 文章列表 ============
const articleList = ref<ArticleInfo[]>([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const loadingMore = ref(false)
const noMore = computed(() => articleList.value.length >= total.value && total.value > 0)

const fetchArticles = (page: number) => {
  const { groupingId } = route.query
  return articleApi.getSortList({
    pageNum: page, pageSize,
    createTime: 1,
    ...(groupingId ? { groupingId: Number(groupingId) } : {})
  })
}

const loadMore = async () => {
  if (loadingMore.value || noMore.value) return
  loadingMore.value = true
  try {
    pageNum.value++
    const result = await fetchArticles(pageNum.value)
    articleList.value.push(...result.rows)
    total.value = result.total
  } finally {
    loadingMore.value = false
  }
}

const { data: initialArticles } = await useAsyncData(
  'latest-articles',
  () => fetchArticles(1),
  { watch: [() => route.query.groupingId] }
)

if (initialArticles.value) {
  articleList.value = initialArticles.value.rows || []
  total.value = initialArticles.value.total
}

watch(() => route.query.groupingId, () => {
  pageNum.value = 1
  articleList.value = initialArticles.value?.rows || []
  total.value = initialArticles.value?.total || 0
}, { flush: 'post' })
</script>
