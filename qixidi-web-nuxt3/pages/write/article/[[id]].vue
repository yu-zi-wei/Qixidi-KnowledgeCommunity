<template>
  <ArticleWritePageContent
    :article-id="articleId"
    :groupings="groupings"
    :labels="labels"
    :specials="specials"
  />
</template>

<script setup lang="ts">
definePageMeta({
  layout: 'editor',
  middleware: 'creator'
})

useHead({ title: '写文章' })

const route = useRoute()
const groupingApi = useGroupingApi()
const labelApi = useLabelApi()
const specialApi = useSpecialApi()

// 获取文章 ID（不存在则为新增模式）
const articleId = computed(() => {
  const id = route.params.id
  return id && id !== 'new' ? (id as string) : undefined
})

// 加载选项数据
const { data: groupingsData } = await useAsyncData('groupings', () =>
  groupingApi.getList()
)

const { data: labelsData } = await useAsyncData('labels', () =>
  labelApi.getList()
)

const { data: specialsData } = await useAsyncData('specials', () =>
  specialApi.getList()
)

const groupings = computed(() => groupingsData.value ?? [])
const labels = computed(() => labelsData.value ?? [])
const specials = computed(() => specialsData.value ?? [])
</script>
