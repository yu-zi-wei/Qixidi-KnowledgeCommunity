<template>
  <div>
    <div v-if="searching" class="search-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!labelList.length" class="search-empty">
      <n-empty :description="keyword ? '未找到相关标签' : '请输入搜索关键词'" />
    </div>
    <LabelCardList v-else :list="labelList" />
  </div>
</template>

<script setup lang="ts">
definePageMeta({ showTabBar: false })

const route = useRoute()
const searchApi = useSearchApi()

const keyword = computed(() => (route.query.q as string || '').trim())

interface LabelItem {
  id: number
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  isFollow?: boolean
  _loading?: boolean
}

const searching = ref(false)
const labelList = ref<LabelItem[]>([])

const searchLabels = async () => {
  if (!keyword.value) return
  searching.value = true
  try {
    const res = await searchApi.searchLabels(keyword.value)
    labelList.value = (res || []).map((l: any) => ({ ...l, _loading: false }))
  } finally {
    searching.value = false
  }
}

watch(keyword, () => searchLabels())
onMounted(() => searchLabels())
</script>

<style>
.search-loading,
.search-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}
</style>
