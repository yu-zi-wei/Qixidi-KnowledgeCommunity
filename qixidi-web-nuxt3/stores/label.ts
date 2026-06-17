import { defineStore } from 'pinia'
import type { LabelGrouping } from '~/types'

export const useLabelStore = defineStore('label', () => {
  const labelList = ref<LabelGrouping[]>([])
  const loaded = ref(false)

  const labelApi = useLabelApi()

  async function fetchLabelList() {
    if (loaded.value) return labelList.value

    try {
      const result = await labelApi.getGroupingList(1, 10)
      labelList.value = result.rows || []
      loaded.value = true
      return labelList.value
    } catch (error) {
      console.error('获取标签列表失败:', error)
      return []
    }
  }

  function setLabelList(list: LabelGrouping[]) {
    if (list && list.length > 0) {
      labelList.value = list
      loaded.value = true
    }
  }

  return {
    labelList,
    loaded,
    fetchLabelList,
    setLabelList
  }
})
