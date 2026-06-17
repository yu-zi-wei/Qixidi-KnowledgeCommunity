/**
 * 通用吸顶检测 composable
 *
 * 使用 requestAnimationFrame 节流，避免高频滚动事件触发回流。
 * 通过 getBoundingClientRect 判断元素是否已吸顶（top <= threshold）。
 *
 * @param selectorOrRef 元素选择器字符串，或已存在的 ref
 * @param threshold 吸顶阈值（默认 0，即元素顶部到达视口顶部）
 */
export function useStickyScroll(
  selectorOrRef: string | Ref<HTMLElement | null>,
  threshold: number = 0
) {
  const isSticky = ref(false)
  let frameId: number | null = null
  let cleanup: (() => void) | null = null

  const getElement = (): HTMLElement | null => {
    if (typeof selectorOrRef === 'string') {
      return document.querySelector(selectorOrRef)
    }
    return selectorOrRef.value
  }

  const check = () => {
    if (frameId !== null) return
    frameId = requestAnimationFrame(() => {
      frameId = null
      const el = getElement()
      if (el) {
        isSticky.value = el.getBoundingClientRect().top <= threshold
      }
    })
  }

  onMounted(() => {
    check()
    window.addEventListener('scroll', check, { passive: true })
    window.addEventListener('resize', check, { passive: true })
    cleanup = () => {
      window.removeEventListener('scroll', check)
      window.removeEventListener('resize', check)
      if (frameId !== null) {
        cancelAnimationFrame(frameId)
        frameId = null
      }
    }
    onUnmounted(cleanup)
  })

  return { isSticky, forceCheck: check }
}
