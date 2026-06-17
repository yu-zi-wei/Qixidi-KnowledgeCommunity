import { defineStore } from 'pinia'
import type { DictumForm } from '~/composables/useReadingEssaysApi'

/** 复制/预填时可携带的字段子集 */
export type EssayPreset = Partial<Pick<DictumForm, 'groupId' | 'author' | 'worksName' | 'albumId' | 'label'>>

export const useEssayDrawerStore = defineStore('essayDrawer', () => {
  const visible = ref(false)
  const editId = ref<number | undefined>(undefined)
  const preset = ref<EssayPreset | undefined>(undefined)

  // 打开新建抽屉
  const open = () => {
    editId.value = undefined
    preset.value = undefined
    visible.value = true
  }

  // 打开编辑抽屉
  const openEdit = (id: number) => {
    editId.value = id
    preset.value = undefined
    visible.value = true
  }

  // 打开新建抽屉并预填字段（用于"复制"快捷发布场景）
  const openWithPreset = (data: EssayPreset) => {
    editId.value = undefined
    preset.value = data
    visible.value = true
  }

  // 关闭抽屉
  const close = () => {
    visible.value = false
    editId.value = undefined
    preset.value = undefined
  }

  return {
    visible,
    editId,
    preset,
    open,
    openEdit,
    openWithPreset,
    close
  }
})
