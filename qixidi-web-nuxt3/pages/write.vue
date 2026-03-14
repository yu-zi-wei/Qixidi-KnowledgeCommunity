<template>
  <ArticleWritePageContent
    :groupings="groupings"
    :labels="labels"
    :specials="specials"
  />
</template>

<script setup lang="ts">
definePageMeta({
  layout: 'editor',
  middleware: 'auth'
})

const groupingApi = useGroupingApi()
const labelApi = useLabelApi()
const specialApi = useSpecialApi()

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
