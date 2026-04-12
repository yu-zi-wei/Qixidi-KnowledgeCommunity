<template>
  <div class="label-page">
    <div v-if="pending" class="label-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!labels.length" class="label-empty">
      <n-empty description="暂无标签" />
    </div>
    <LabelCardList v-else :list="labels" />
  </div>
</template>

<script setup lang="ts">
definePageMeta({ showTabBar: false })

useHead({
  bodyAttrs: {
    class: 'page-label'
  }
})

const labelApi = useLabelApi()

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

const { data: rawData, pending } = await useAsyncData(
  'system-labels',
  () => labelApi.getSystemLabels()
)

const labels = computed<LabelItem[]>(() =>
  (rawData.value || []).map((l: any) => ({ ...l, _loading: false }))
)
</script>

<style>
.label-page {
  display: flex;
  flex-direction: column;
  padding-top: 20px;
}

.label-loading,
.label-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

@media (max-width: 768px) {
  body.page-label .home-main {
    padding-top: 70px !important;
  }
}
</style>
