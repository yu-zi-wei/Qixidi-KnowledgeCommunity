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

// 用于存储事件处理器
const imageClickHandlers = new WeakMap<HTMLImageElement, () => void>()

const render = async () => {
  if (props.content) {
    renderedContent.value = await renderMarkdown(props.content)
  }
}

await render()

watch(() => props.content, async () => {
  await render()
  nextTick(() => {
    bindImageClickEvents()
    hydrateVideoEmbeds()
    window.dispatchEvent(new CustomEvent('markdown-rendered'))
  })
})

// 绑定图片点击事件
const bindImageClickEvents = () => {
  if (!contentRef.value) return

  const images = contentRef.value.querySelectorAll('img')
  images.forEach((img) => {
    if (imageClickHandlers.has(img)) return
    img.style.cursor = 'zoom-in'

    const handler = () => openPreview(img.src, img.alt)
    img.addEventListener('click', handler)
    imageClickHandlers.set(img, handler)
  })
}

/**
 * 将视频占位符替换为真实播放器
 */
const hydrateVideoEmbeds = () => {
  if (!contentRef.value) return

  const videoEmbeds = contentRef.value.querySelectorAll('.video-embed')
  videoEmbeds.forEach((embed) => {
    const el = embed as HTMLElement
    const type = el.dataset.videoType
    const url = el.dataset.videoUrl
    const videoId = el.dataset.videoId
    const title = el.dataset.videoTitle || ''
    const poster = el.dataset.videoPoster || ''

    // 避免重复处理
    if (el.querySelector('video, iframe')) return

    let playerHtml = ''

    switch (type) {
      case 'local':
        playerHtml = `
          <video
            src="${url}"
            poster="${poster}"
            controls
            preload="metadata"
            playsinline
            webkit-playsinline
            style="max-width: 100%; border-radius: 8px;"
          >
            您的浏览器不支持视频播放，<a href="${url}" target="_blank">点击下载</a>
          </video>
        `
        break

      case 'bilibili':
        playerHtml = `
          <iframe
            src="https://player.bilibili.com/player.html?bvid=${videoId}&high_quality=1&autoplay=0"
            title="${title || 'B站视频'}"
            scrolling="no"
            border="0"
            frameborder="no"
            framespacing="0"
            allowfullscreen="true"
            style="width: 100%; aspect-ratio: 16/9; border-radius: 8px;"
          ></iframe>
        `
        break

      case 'youtube':
        playerHtml = `
          <iframe
            src="https://www.youtube.com/embed/${videoId}"
            title="${title || 'YouTube视频'}"
            frameborder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
            style="width: 100%; aspect-ratio: 16/9; border-radius: 8px;"
          ></iframe>
        `
        break

      default:
        return
    }

    el.innerHTML = playerHtml
  })
}

// 初始渲染后绑定事件
onMounted(() => {
  bindImageClickEvents()
  hydrateVideoEmbeds()
  nextTick(() => {
    window.dispatchEvent(new CustomEvent('markdown-rendered'))
  })
})

const openPreview = (src: string, alt: string) => {
  previewImage.value = src
  previewAlt.value = alt
  previewVisible.value = true
}

const closePreview = () => {
  previewVisible.value = false
}
</script>

<style scoped>
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

/* 视频容器样式 */
.markdown-body :deep(.video-embed) {
  margin: 16px 0;
  max-width: 100%;
}

.markdown-body :deep(.video-embed video) {
  width: 100%;
  max-width: 100%;
  border-radius: 12px;
  background: #000;
}

.markdown-body :deep(.video-embed iframe) {
  width: 100%;
  border-radius: 12px;
  background: #000;
}
</style>
