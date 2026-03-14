import { defineStore } from 'pinia'
import type { LabelGrouping } from '~/types'

export const useLabelStore = defineStore('label', () => {
  const labelList = ref<LabelGrouping[]>([])
  const loaded = ref(false)

  const labelApi = useLabelApi()

  // 获取标签列表（只加载一次）
  async function fetchLabelList() {
    if (loaded.value) return labelList.value

    try {
      const result = await labelApi.getGroupingList(0, 10)
      labelList.value = result.rows || []
      loaded.value = true
      return labelList.value
    } catch (error) {
      console.error('获取标签列表失败:', error)
      return []
    }
  }

  return {
    labelList,
    loaded,
    fetchLabelList
  }
})
