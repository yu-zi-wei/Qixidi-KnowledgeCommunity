<template>
  <div class="markdown-renderer">
    <div ref="contentRef" class="markdown-body" v-html="renderedContent"></div>

    <!-- 图片预览 -->
    <ImagePreview
      :visible="previewVisible"
      :image-src="previewImage"
      :alt="previewAlt"
      @close="closePreview"
    />
  </div>
</template>

<script setup lang="ts">
interface Props {
  content: string
}

const props = defineProps<Props>()
const { renderMarkdown } = useMarkdown()
const renderedContent = ref('')
const contentRef = ref<HTMLElement>()

// 图片预览状态
const previewVisible = ref(false)
const previewImage = ref('')
const previewAlt = ref('')

// 用于存储图片点击事件处理器，避免重复绑定
const imageClickHandlers = new WeakMap<HTMLImageElement, () => void>()

const render = async () => {
  if (props.content) {
    renderedContent.value = await renderMarkdown(props.content)
  }
}

await render()

watch(() => props.content, async () => {
  await render()
  // 内容更新后重新绑定事件并通知外部
  nextTick(() => {
    bindImageClickEvents()
    // 触发自定义事件，通知 ArticleToc 更新
    window.dispatchEvent(new CustomEvent('markdown-rendered'))
  })
})

// 绑定图片点击事件
const bindImageClickEvents = () => {
  if (!contentRef.value) return

  const images = contentRef.value.querySelectorAll('img')
  images.forEach((img) => {
    // 如果已经绑定过事件，跳过
    if (imageClickHandlers.has(img)) return

    img.style.cursor = 'zoom-in'

    const handler = () => {
      openPreview(img.src, img.alt)
    }

    img.addEventListener('click', handler)
    imageClickHandlers.set(img, handler)
  })
}

// 初始渲染后绑定事件并通知
onMounted(() => {
  bindImageClickEvents()
  nextTick(() => {
    window.dispatchEvent(new CustomEvent('markdown-rendered'))
  })
})

// 打开预览
const openPreview = (src: string, alt: string) => {
  previewImage.value = src
  previewAlt.value = alt
  previewVisible.value = true
}

// 关闭预览
const closePreview = () => {
  previewVisible.value = false
}
</script>

<style scoped>
/* Markdown 样式已在 main.css 中全局定义，支持双主题 */
.markdown-body {
  font-size: 16px;
}

.markdown-body img {
  cursor: zoom-in;
  transition: opacity var(--transition-fast);
}

.markdown-body img:hover {
  opacity: 0.9;
}
</style>
