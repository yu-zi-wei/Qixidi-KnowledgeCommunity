export const useCreatorApplyStore = defineStore('creatorApply', () => {
  const visible = ref(false)

  const show = () => { visible.value = true }
  const hide = () => { visible.value = false }

  return { visible, show, hide }
})
