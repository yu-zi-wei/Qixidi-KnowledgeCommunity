import { defineStore } from 'pinia'

export const useEssayDrawerStore = defineStore('essayDrawer', () => {
  const visible = ref(false)
  const editId = ref<number | undefined>(undefined)

  // 打开新建抽屉
  const open = () => {
    editId.value = undefined
    visible.value = true
  }

  // 打开编辑抽屉
  const openEdit = (id: number) => {
    editId.value = id
    visible.value = true
  }

  // 关闭抽屉
  const close = () => {
    visible.value = false
    editId.value = undefined
  }

  return {
    visible,
    editId,
    open,
    openEdit,
    close
  }
})
