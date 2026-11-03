<template>
  <div v-if="headings.length > 0" class="article-toc">
    <div class="toc-header">
      <List class="toc-icon" />
      <span>目录</span>
    </div>
    <ul class="toc-list">
      <li
        v-for="heading in headings"
        :key="heading.id"
        class="toc-item"
        :class="[
          `toc-level-${heading.level}`,
          { active: activeId === heading.id }
        ]"
      >
        <a
          :href="`#${heading.id}`"
          class="toc-link"
          :title="heading.text"
          @click.prevent="scrollToHeading(heading.id)"
        >
          {{ heading.text }}
        </a>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { List } from '@vicons/tabler'

interface Heading {
  id: string
  text: string
  level: number
}

interface Props {
  content?: string
}

const props = defineProps<Props>()

const headings = ref<Heading[]>([])
const activeId = ref<string>('')

// 从渲染后的 HTML 中提取标题（确保 ID 与实际渲染一致）
const extractHeadingsFromDOM = () => {
  const container = document.querySelector('.markdown-body')
  if (!container) {
    // 如果 DOM 还没渲染，先从 HTML 字符串解析标题作为备用
    return parseHeadingsFromHtml(props.content || '')
  }

  const elements = container.querySelectorAll('h2[id], h3[id], h4[id]')
  const found: Heading[] = []

  elements.forEach((el) => {
    const element = el as HTMLElement
    const level = parseInt(element.tagName.substring(1)) // 2, 3, 4
    const text = element.textContent || ''
    const id = element.id

    found.push({ id, text, level })
  })

  return found
}

// 备用方案：从 HTML 字符串解析标题
const parseHeadingsFromHtml = (html: string) => {
  if (!html) return []

  const regex = /<h([2-4])[^>]*id="([^"]*)"[^>]*>(.*?)<\/h\1>/g
  const found: Heading[] = []
  let match

  while ((match = regex.exec(html)) !== null) {
    const level = parseInt(match[1])
    const id = match[2]
    // 去除 HTML 标签，只保留文本
    const text = match[3].replace(/<[^>]*>/g, '').trim()

    found.push({ id, text, level })
  }

  return found
}


// 更新目录
const updateHeadings = () => {
  headings.value = extractHeadingsFromDOM()
}

// 监听内容变化
watch(() => props.content, () => {
  // 等待 DOM 更新后再提取
  nextTick(() => {
    updateHeadings()
  })
}, { immediate: true })

// 组件挂载后也更新一次（确保 DOM 已渲染）
onMounted(() => {
  updateHeadings()
  // 监听 Markdown 渲染完成事件
  window.addEventListener('markdown-rendered', updateHeadings)
})

onUnmounted(() => {
  window.removeEventListener('markdown-rendered', updateHeadings)
})

// 滚动到指定标题
const scrollToHeading = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    const offset = 80 // 导航栏高度
    const elementPosition = element.getBoundingClientRect().top
    const offsetPosition = elementPosition + window.pageYOffset - offset

    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    })
  }
}

// 监听滚动，高亮当前章节
const updateActiveHeading = () => {
  const headings = document.querySelectorAll('h2[id], h3[id], h4[id]')
  const scrollTop = window.pageYOffset + 100

  let currentId = ''

  headings.forEach((heading) => {
    const element = heading as HTMLElement
    if (element.offsetTop <= scrollTop) {
      currentId = element.id
    }
  })

  activeId.value = currentId

  // 同步进度竖线高度：主色实线铺到当前项行中心（无激活项时归零）
  nextTick(() => {
    const list = document.querySelector('.toc-list')
    const activeEl = list?.querySelector('.toc-item.active') as HTMLElement | null
    if (list instanceof HTMLElement) {
      const progress = activeEl ? `${activeEl.offsetTop + activeEl.offsetHeight / 2}px` : '0px'
      list.style.setProperty('--toc-progress', progress)
    }
  })
}

onMounted(() => {
  window.addEventListener('scroll', updateActiveHeading)
  updateActiveHeading()
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateActiveHeading)
})
</script>

<style scoped>
/* 目录 - 精致竖线 + 圆点指示器 */
.article-toc {
  position: relative;
  padding: 0 0 0 36px; /* 36px：给左侧轨道留足空间，圆点完全落在容器内，不被 overflow 裁切 */
  margin-left: 8px;
  background: transparent;
  flex: 0 1 auto;    /* 按内容高度，不强制撑满：目录少则矮（竖线短），目录多则收缩并滚动 */
  min-height: 0;     /* flex 子项收缩关键，否则 overflow 不生效 */
  overflow-y: auto;  /* 目录过多时滚动 */
}

/* 滚动条美化 */
.article-toc::-webkit-scrollbar {
  width: 6px;
}

.article-toc::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.article-toc::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.toc-header {
  display: none;
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
  position: relative; /* 给竖线 ::before 提供定位上下文 */
}

/* 竖线放在 .toc-list 上（而非 .article-toc），跟随内容一起滚动，保证滚动后每个目录项旁都有竖线 */
/* 底轨：全高均匀淡墨 hairline，是进度线之下的"未读"段 */
.toc-list::before {
  content: '';
  position: absolute;
  left: -27.5px;
  top: 4px;
  bottom: 4px;
  width: 2px;
  background: color-mix(in srgb, var(--color-ink) 8%, transparent);
  border-radius: 1px;
}

/* 进度线：主色实线从顶铺到当前项行中心，高度由 --toc-progress 驱动（滚动高亮时 JS 同步） */
.toc-list::after {
  content: '';
  position: absolute;
  left: -27.5px;
  top: 4px;
  width: 2px;
  height: var(--toc-progress, 0px);
  background: var(--color-primary);
  border-radius: 1px;
  transition: height 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.toc-item {
  position: relative;
  margin-bottom: 0;
}

.toc-link {
  display: block;
  white-space: nowrap;       /* 单行截断：中文字体下行盒高度稳定，无多行亚像素问题 */
  overflow: hidden;
  text-overflow: ellipsis;   /* 超出部分显示省略号，完整标题靠 title 属性 hover 查看 */
  padding: 6px 0;
  font-size: 14px;
  line-height: 21px;
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color 0.2s ease;
}

/* hover 用墨色预览即可，主色留给 active 态 */
.toc-link:hover {
  color: var(--color-ink);
}

/* 层级缩进 */
.toc-level-2 {
  padding-left: 0;
}

.toc-level-3 {
  padding-left: 16px;
}

.toc-level-4 {
  padding-left: 32px;
}

/* 激活状态文字：主色与进度线同源 */
.toc-item.active .toc-link {
  color: var(--color-primary);
  font-weight: 500;
}
</style>
