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
.article-toc {
  padding: 16px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border-light);
}

.toc-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-ink);
}

.toc-icon {
  width: 16px;
  height: 16px;
  color: var(--color-primary);
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  margin-bottom: 4px;
}

.toc-link {
  display: block;
  padding: 6px 12px;
  font-size: 13px;
  color: var(--color-ink-light);
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  line-height: 1.5;
}

.toc-link:hover {
  color: var(--color-primary);
  background: var(--color-surface-dim);
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

/* 激活状态 */
.toc-item.active .toc-link {
  color: var(--color-primary);
  background: var(--color-primary-light);
  font-weight: 500;
}
</style>
