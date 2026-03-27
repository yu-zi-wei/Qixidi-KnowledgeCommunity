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
    // 如果 DOM 还没渲染，先解析 Markdown 源文本作为备用
    return parseHeadingsFromMarkdown(props.content || '')
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

// 备用方案：从 Markdown 源文本解析
const parseHeadingsFromMarkdown = (content: string) => {
  if (!content) return []

  const regex = /^(#{2,4})\s+(.+)$/gm
  const found: Heading[] = []
  let match

  while ((match = regex.exec(content)) !== null) {
    const level = match[1].length
    const text = match[2].trim()
    // 生成与 rehype-slug 一致的 ID
    const id = generateSlug(text)

    found.push({ id, text, level })
  }

  return found
}

// 生成与 rehype-slug 一致的 ID
const generateSlug = (text: string): string => {
  return text
    .toLowerCase()
    .trim()
    .replace(/\s+/g, '-')
    .replace(/[^\w\u4e00-\u9fa5\u4e00-\u9fff\u3000-\u303f\uff00-\uffef-]/g, '')
    .replace(/^[-]+|[-]+$/g, '')
    .replace(/[-]{2,}/g, '-')
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
  padding: 0 0 0 28px;
  margin-left: 8px;
  background: transparent;
}

/* 竖线 - 稍微粗一点 */
.article-toc::before {
  content: '';
  position: absolute;
  left: 0;
  top: 4px;
  bottom: 4px;
  width: 2px;
  background: linear-gradient(
    180deg,
    transparent 0%,
    rgba(0, 0, 0, 0.08) 8%,
    rgba(0, 0, 0, 0.08) 92%,
    transparent 100%
  );
  border-radius: 1px;
}

.dark .article-toc::before {
  background: linear-gradient(
    180deg,
    transparent 0%,
    rgba(255, 255, 255, 0.08) 8%,
    rgba(255, 255, 255, 0.08) 92%,
    transparent 100%
  );
}

.toc-header {
  display: none;
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  position: relative;
  margin-bottom: 0;
}

/* 圆点指示器 - 对齐竖线中心，完全不透明避免颜色叠加 */
.toc-item::before {
  content: '';
  position: absolute;
  left: -30.5px; /* 微调：-31px + 0.5px */
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #d1d5db;
  border: none;
  box-shadow: 0 0 0 3px #fff;
  z-index: 1;
  transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}

.dark .toc-item::before {
  background: #4b5563;
  box-shadow: 0 0 0 3px #1a1a1a;
}

/* 激活状态 - 品牌色 + 微光晕 */
.toc-item.active::before {
  background: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(61, 90, 128, 0.15);
}

/* 悬停状态 */
.toc-item:hover::before {
  background: rgba(61, 90, 128, 0.25);
}

.toc-item.active:hover::before {
  background: var(--color-primary);
}

.toc-link {
  display: block;
  padding: 6px 0;
  font-size: 13px;
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color 0.2s ease;
  line-height: 1.5;
}

.toc-link:hover {
  color: var(--color-primary);
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

/* 激活状态文字 */
.toc-item.active .toc-link {
  color: var(--color-ink);
  font-weight: 500;
}
</style>
