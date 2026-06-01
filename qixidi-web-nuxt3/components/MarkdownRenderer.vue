<template>
  <div class="markdown-renderer">
    <div ref="contentRef" class="markdown-body" v-html="html"></div>

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
import 'highlight.js/styles/atom-one-dark.css'
import 'katex/dist/katex.min.css'

interface Props {
  html: string
}

const props = defineProps<Props>()
const contentRef = ref<HTMLElement>()

// 图片预览状态
const previewVisible = ref(false)
const previewImage = ref('')
const previewAlt = ref('')

// 用于存储事件处理器
const imageClickHandlers = new WeakMap<HTMLImageElement, () => void>()

// ECharts 实例清理
const echartsCleanups: (() => void)[] = []

// hydrate 版本号，防止过期异步操作
let hydrateVersion = 0

// 客户端交互增强
const hydrate = () => {
  // 清理上一次的 ECharts 实例
  echartsCleanups.forEach(fn => fn())
  echartsCleanups.length = 0

  const currentVersion = ++hydrateVersion

  nextTick(() => {
    bindLinkTargets()
    bindImageClickEvents()
    hydrateVideoEmbeds()
    enhanceCodeBlocks()
    hydrateMermaidDiagrams()
    hydrateEchartsDiagrams()
    window.dispatchEvent(new CustomEvent('markdown-rendered'))
  })
}

onMounted(hydrate)

onUnmounted(() => {
  echartsCleanups.forEach(fn => fn())
  echartsCleanups.length = 0
})

watch(() => props.html, () => {
  if (import.meta.client) hydrate()
})

// 给所有 <a> 标签添加新标签页打开
const bindLinkTargets = () => {
  if (!contentRef.value) return
  const links = contentRef.value.querySelectorAll('a')
  links.forEach((link) => {
    if (!link.getAttribute('target')) {
      link.setAttribute('target', '_blank')
      link.setAttribute('rel', 'noopener noreferrer')
    }
  })
}

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

// 代码块增强：语言标签 + 复制按钮
const enhanceCodeBlocks = () => {
  if (!contentRef.value) return

  contentRef.value.querySelectorAll('pre').forEach((pre) => {
    if (pre.querySelector('.code-block-header')) return

    const code = pre.querySelector('code')
    if (!code) return

    // 提取语言名（优先用 data-language，其次从 class 解析）
    const dataLang = code.getAttribute('data-language')
    const classList = code.className || ''
    const classLang = classList.match(/language-(\w+)/)?.[1]
    const lang = dataLang || classLang || ''

    // 创建头部
    const header = document.createElement('div')
    header.className = 'code-block-header'

    // 左侧：三色点
    const dots = document.createElement('span')
    dots.className = 'code-block-dots'
    dots.innerHTML = '<i></i><i></i><i></i>'

    // 语言标签
    const langLabel = document.createElement('span')
    langLabel.className = 'code-block-lang'
    langLabel.textContent = lang || 'code'

    header.appendChild(dots)

    // 右侧容器
    const right = document.createElement('span')
    right.className = 'code-block-right'

    right.appendChild(langLabel)

    const copyBtn = document.createElement('button')
    copyBtn.className = 'code-block-copy'
    copyBtn.textContent = '复制'
    copyBtn.addEventListener('click', async () => {
      const text = code.textContent || ''
      try {
        await navigator.clipboard.writeText(text)
        copyBtn.textContent = '已复制'
        copyBtn.classList.add('copied')
        setTimeout(() => {
          copyBtn.textContent = '复制'
          copyBtn.classList.remove('copied')
        }, 2000)
      } catch {
        copyBtn.textContent = '失败'
        setTimeout(() => { copyBtn.textContent = '复制' }, 2000)
      }
    })

    right.appendChild(copyBtn)
    header.appendChild(right)
    pre.insertBefore(header, pre.firstChild)
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
        if (!/^[\w]+$/.test(videoId || '')) return
        playerHtml = `
          <iframe
            src="https://player.bilibili.com/player.html?bvid=${videoId}&high_quality=1&autoplay=0"
            title="B站视频"
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
        if (!/^[\w-]+$/.test(videoId || '')) return
        playerHtml = `
          <iframe
            src="https://www.youtube.com/embed/${videoId}"
            title="YouTube视频"
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

// Mermaid 图表 hydrate（懒加载）
let mermaidInitialized = false
const hydrateMermaidDiagrams = async () => {
  if (!contentRef.value) return
  const placeholders = contentRef.value.querySelectorAll('.mermaid-placeholder')
  if (placeholders.length === 0) return

  const mermaid = await import('mermaid')
  if (!mermaidInitialized) {
    mermaid.default.initialize({ startOnLoad: false, securityLevel: 'strict', theme: 'default' })
    mermaidInitialized = true
  }

  placeholders.forEach((el, index) => {
    const htmlEl = el as HTMLElement
    if (htmlEl.querySelector('svg')) return

    const source = decodeURIComponent(htmlEl.dataset.mermaidSource || '')
    if (!source) return

    mermaid.default.render(`mermaid-${Date.now()}-${index}`, source)
      .then(({ svg }) => {
        if (hydrateVersion !== currentVersion) return
        htmlEl.innerHTML = svg
        htmlEl.classList.add('mermaid-rendered')
      })
      .catch((err: Error) => {
        if (hydrateVersion !== currentVersion) return
        htmlEl.innerHTML = `<p style="color:var(--color-danger);font-size:13px;">图表渲染失败: ${err.message}</p>`
      })
  })
}

// ECharts 图表 hydrate（懒加载）
const hydrateEchartsDiagrams = async () => {
  if (!contentRef.value) return
  const placeholders = contentRef.value.querySelectorAll('.echarts-placeholder')
  if (placeholders.length === 0) return

  const echarts = await import('echarts')

  placeholders.forEach((el) => {
    const htmlEl = el as HTMLElement
    if (htmlEl.querySelector('canvas')) return

    const raw = decodeURIComponent(htmlEl.dataset.echartsOption || '')
    if (!raw) return

    try {
      const option = new Function('return (' + raw + ')')()
      // 创建容器
      const chartEl = document.createElement('div')
      chartEl.style.width = '100%'
      chartEl.style.height = '400px'
      htmlEl.innerHTML = ''
      htmlEl.appendChild(chartEl)

      const chart = echarts.init(chartEl, undefined, { renderer: 'canvas' })
      chart.setOption(option)
      htmlEl.classList.add('echarts-rendered')

      // 响应窗口变化
      const resizeObserver = new ResizeObserver(() => chart.resize())
      resizeObserver.observe(htmlEl)

      // 注册清理函数
      echartsCleanups.push(() => {
        resizeObserver.disconnect()
        chart.dispose()
      })
    } catch (err: any) {
      htmlEl.innerHTML = `<p style="color:var(--color-danger);font-size:13px;">图表渲染失败: ${err.message}</p>`
    }
  })
}

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

/* 行内代码 */
.markdown-body :deep(code:not(pre code)) {
  font-family: var(--font-mono);
  font-size: 0.9em;
  padding: 2px 6px;
  border-radius: 4px;
  background: #DAE5ED !important;
  color: #449CF8 !important;
}
</style>
