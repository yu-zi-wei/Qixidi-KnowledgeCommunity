import { unified } from 'unified'
import remarkParse from 'remark-parse'
import remarkGfm from 'remark-gfm'
import remarkRehype from 'remark-rehype'
import rehypeSlug from 'rehype-slug'
import rehypeRaw from 'rehype-raw'
import rehypeSanitize from 'rehype-sanitize'
import rehypeHighlight from 'rehype-highlight'
import rehypeStringify from 'rehype-stringify'
import remarkMath from 'remark-math'
import rehypeKatex from 'rehype-katex'
import { visit } from 'unist-util-visit'
import hljs from 'highlight.js'
import type { Plugin, Transformer } from 'unified'
import type { Root } from 'hast'

/**
 * remark 插件：段落内软换行转硬换行
 * 将单个换行符 \n 渲染为 <br>，使「回车一次即换行」，无需空两行
 * 覆盖普通段落与引用块（blockquote 的子节点也是 paragraph，会被一并命中）
 */
const remarkSoftBreaks: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'paragraph', (node: any) => {
      if (!node.children) return
      const newChildren: any[] = []
      for (const child of node.children) {
        if (child.type === 'text' && typeof child.value === 'string' && child.value.includes('\n')) {
          const parts = child.value.split('\n')
          for (let i = 0; i < parts.length; i++) {
            if (parts[i]) newChildren.push({ type: 'text', value: parts[i] })
            if (i < parts.length - 1) newChildren.push({ type: 'break' })
          }
        } else {
          newChildren.push(child)
        }
      }
      node.children = newChildren
    })
  }
}

/**
 * HTML 属性值转义（防 XSS）
 */
const escAttr = (s: string) => s.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;')

/**
 * 视频链接识别
 * - 本地视频: .mp4, .webm, .ogg, .mov
 * - B站: bilibili.com/video/, b23.tv/
 * - YouTube: youtube.com/watch, youtu.be/
 */
const VIDEO_PATTERNS = {
  local: /\.(mp4|webm|ogg|mov)(\?.*)?$/i,
  bilibili: /bilibili\.com\/video\/(BV[\w]+)/i,
  bilibiliShort: /b23\.tv\/(BV[\w]+)/i,
  youtube: /(?:youtube\.com\/watch\?v=|youtu\.be\/)([\w-]+)/i
}

/**
 * 解析视频链接
 */
const parseVideoUrl = (url: string): { type: 'local' | 'bilibili' | 'youtube'; embedUrl?: string; videoId?: string } | null => {
  if (!url) return null

  if (VIDEO_PATTERNS.local.test(url)) {
    return { type: 'local', embedUrl: url }
  }

  const bilibiliMatch = url.match(VIDEO_PATTERNS.bilibili)
  if (bilibiliMatch) {
    return { type: 'bilibili', videoId: bilibiliMatch[1] }
  }
  const bilibiliShortMatch = url.match(VIDEO_PATTERNS.bilibiliShort)
  if (bilibiliShortMatch) {
    return { type: 'bilibili', videoId: bilibiliShortMatch[1] }
  }

  const youtubeMatch = url.match(VIDEO_PATTERNS.youtube)
  if (youtubeMatch) {
    return { type: 'youtube', videoId: youtubeMatch[1] }
  }

  return null
}

/**
 * remark 插件：解析 @[video](url) 语法和独立视频链接
 */
const remarkVideo: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'paragraph', (node: any) => {
      if (!node.children) return

      const newChildren: any[] = []
      let changed = false

      for (const child of node.children) {
        // 处理 @[video](url) 语法
        if (child.type === 'text') {
          const text = child.value
          const regex = /@\[video(?:\s+title="([^"]*)")?(?:\s+poster="([^"]*)")?\]\(([^)]+)\)/g
          let lastIndex = 0
          let match

          while ((match = regex.exec(text)) !== null) {
            changed = true
            if (match.index > lastIndex) {
              newChildren.push({ type: 'text', value: text.slice(lastIndex, match.index) })
            }

            const videoInfo = parseVideoUrl(match[3])
            if (videoInfo) {
              newChildren.push({
                type: 'html',
                value: `<div class="video-embed" data-video-type="${videoInfo.type}" data-video-url="${escAttr(videoInfo.embedUrl || '')}" data-video-id="${escAttr(videoInfo.videoId || '')}" data-video-title="${escAttr(match[1] || '')}" data-video-poster="${escAttr(match[2] || '')}"></div>`
              })
            } else {
              newChildren.push({ type: 'text', value: match[0] })
            }
            lastIndex = match.index + match[0].length
          }

          if (lastIndex < text.length) {
            newChildren.push({ type: 'text', value: text.slice(lastIndex) })
          }
        }
        // 处理独立链接（纯链接行）
        else if (child.type === 'link' && node.children.length === 1) {
          const videoInfo = parseVideoUrl(child.url)
          if (videoInfo) {
            changed = true
            newChildren.push({
              type: 'html',
              value: `<div class="video-embed" data-video-type="${videoInfo.type}" data-video-url="${escAttr(videoInfo.embedUrl || '')}" data-video-id="${escAttr(videoInfo.videoId || '')}"></div>`
            })
          } else {
            newChildren.push(child)
          }
        }
        else {
          newChildren.push(child)
        }
      }

      if (changed) {
        node.children = newChildren
      }
    })
  }
}

/**
 * remark 插件：将 mermaid/echarts 代码块替换为占位符（避免被 rehype-highlight 处理）
 */
const remarkDiagram: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'code', (node: any, index: number, parent: any) => {
      if (!parent) return
      if (node.lang === 'mermaid') {
        parent.children.splice(index, 1, {
          type: 'html',
          value: `<div class="mermaid-placeholder" data-mermaid-source="${encodeURIComponent(node.value)}"></div>`
        })
      } else if (node.lang === 'echarts') {
        parent.children.splice(index, 1, {
          type: 'html',
          value: `<div class="echarts-placeholder" data-echarts-option="${encodeURIComponent(node.value)}"></div>`
        })
      }
    })
  }
}

/**
 * HTML 转义
 */
const escapeHtml = (s: string) => s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')

/**
 * 预处理：将 ^text^ 转为 <sup>、~text~（单波浪）转为 <sub>
 * 必须在 remarkParse 之前执行，否则 GFM 会把 ~text~ 当作删除线
 */
const preprocessSuperSub = (markdown: string): string => {
  const lines = markdown.split('\n')
  const result: string[] = []
  let inFence = false

  for (const line of lines) {
    if (/^(`{3,}|~{3,})/.test(line.trim())) {
      inFence = !inFence
    }

    if (inFence) {
      result.push(line)
      continue
    }

    result.push(replaceSuperSubInline(line))
  }

  return result.join('\n')
}

const replaceSuperSubInline = (text: string): string => {
  const parts: string[] = []
  let rest = text

  while (rest.length > 0) {
    const codeStart = rest.indexOf('`')
    if (codeStart === -1) {
      parts.push(replaceSuperSubText(rest))
      break
    }

    if (codeStart > 0) {
      parts.push(replaceSuperSubText(rest.slice(0, codeStart)))
    }

    const codeEnd = rest.indexOf('`', codeStart + 1)
    if (codeEnd === -1) {
      parts.push(rest.slice(codeStart))
      break
    }

    parts.push(rest.slice(codeStart, codeEnd + 1))
    rest = rest.slice(codeEnd + 1)
  }

  return parts.join('')
}

const replaceSuperSubText = (text: string): string => {
  text = text.replace(/\^([^\^]+)\^/g, (_, c) => `<sup>${escapeHtml(c)}</sup>`)
  text = text.replace(/(?<!~)~([^~\n]+?)~(?!~)/g, (_, c) => `<sub>${escapeHtml(c)}</sub>`)
  return text
}

/**
 * 自定义 sanitize schema
 */
const videoSanitizeSchema = {
  tagNames: [
    'div', 'span', 'p', 'br', 'hr',
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'ul', 'ol', 'li',
    'blockquote', 'pre', 'code',
    'strong', 'em', 'del', 'ins',
    'a', 'img', 'input',
    'table', 'thead', 'tbody', 'tr', 'th', 'td',
    'video', 'source', 'iframe',
    'figure', 'figcaption',
    'u', 'sup', 'sub'
  ],
  attributes: {
    '*': ['className', 'class', 'style', 'dataVideoType', 'data-video-type', 'dataVideoUrl', 'data-video-url', 'dataVideoId', 'data-video-id', 'dataVideoTitle', 'data-video-title', 'dataVideoPoster', 'data-video-poster', 'dataMermaidSource', 'data-mermaid-source', 'dataEchartsOption', 'data-echarts-option', 'id'],
    a: ['href', 'title', 'target', 'rel'],
    img: ['src', 'alt', 'title', 'width', 'height', 'loading'],
    input: ['type', 'disabled', 'checked'],
    video: ['src', 'poster', 'controls', 'preload', 'playsinline', 'webkitPlaysinline', 'width', 'height'],
    source: ['src', 'type'],
    iframe: ['src', 'title', 'width', 'height', 'frameborder', 'allow', 'allowfullscreen', 'scrolling'],
    code: ['className', 'class', 'dataLanguage', 'data-language'],
    pre: ['className', 'class'],
    div: ['className', 'class', 'id', 'dataVideoType', 'data-video-type', 'dataVideoUrl', 'data-video-url', 'dataVideoId', 'data-video-id', 'dataVideoTitle', 'data-video-title', 'dataVideoPoster', 'data-video-poster', 'dataMermaidSource', 'data-mermaid-source', 'dataEchartsOption', 'data-echarts-option'],
    th: ['align'],
    td: ['align']
  },
  protocols: {
    href: ['http', 'https'],
    src: ['http', 'https', 'data']
  }
}

/**
 * rehype 插件：将框架语言映射到 highlight.js 支持的语言
 */
const LANG_ALIASES: Record<string, string> = {
  vue: 'html',
  svelte: 'html',
  jsx: 'javascript',
  tsx: 'typescript',
  astro: 'html',
  nginx: 'bash',
  dockerfile: 'bash',
  dos: 'bat',
  cmd: 'bat',
  powershell: 'bash',
  objectivec: 'c',
  objc: 'c',
  kotlin: 'java',
}

const rehypeLanguageAlias: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'element', (node: any) => {
      if (node.tagName !== 'code') return
      const classes: string[] = node.properties?.className || []
      for (let i = 0; i < classes.length; i++) {
        if (typeof classes[i] === 'string' && classes[i].startsWith('language-')) {
          const lang = classes[i].slice(9)
          const mapped = LANG_ALIASES[lang.toLowerCase()]
          if (mapped) {
            classes[i] = `language-${mapped}`
            // 保留原始语言名供后续展示用
            node.properties.dataLanguage = lang
          }
          break
        }
      }
    })
  }
}

/**
 * rehype 插件：不支持的代码语言回退到 php 高亮
 */
const SUPPORTED_LANGS = new Set(hljs.listLanguages())
const rehypeHighlightFallback: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'element', (node: any) => {
      if (node.tagName !== 'code') return
      const classes: string[] = node.properties?.className || []
      for (let i = 0; i < classes.length; i++) {
        if (typeof classes[i] === 'string' && classes[i].startsWith('language-')) {
          const lang = classes[i].slice(9).toLowerCase()
          // 已被 alias 插件处理或 highlight.js 支持，跳过
          if (node.properties?.dataLanguage || SUPPORTED_LANGS.has(lang)) return
          // 不支持的语言：保留原始名称用于显示，用 php 做回退高亮
          node.properties.dataLanguage = lang
          classes[i] = 'language-php'
          return
        }
      }
    })
  }
}

/**
 * Admonition 类型与默认标题
 */
const ADMONITION_TYPES = new Set([
  'note', 'abstract', 'info', 'tip', 'success', 'question',
  'warning', 'failure', 'danger', 'bug', 'example', 'quote',
  'hint', 'caution', 'error', 'attention'
])

const ADMONITION_TITLES: Record<string, string> = {
  note: '笔记', abstract: '摘要', info: '信息', tip: '提示',
  success: '成功', question: '问题', warning: '警告', failure: '失败',
  danger: '危险', bug: 'Bug', example: '示例', quote: '引用',
  hint: '提示', caution: '注意', error: '错误', attention: '注意'
}

const getElementText = (node: any): string => {
  if (node.type === 'text') return node.value || ''
  if (node.children) return node.children.map((c: any) => getElementText(c)).join('')
  return ''
}

/**
 * rehype 插件：!!! type ... !!! 提示框语法
 */
const rehypeAdmonition: Plugin = (): Transformer => {
  return (tree: any) => {
    const children = tree.children
    if (!children) return

    let i = 0
    while (i < children.length) {
      const node = children[i]

      if (node.type === 'element' && node.tagName === 'p') {
        const text = getElementText(node).trim()
        const match = text.match(/^!!!\s+(\w+)(?:\s+"([^"]*)")?(?:\s+(.*))?$/)

        if (match && ADMONITION_TYPES.has(match[1].toLowerCase())) {
          const type = match[1].toLowerCase()
          const title = match[2] || match[3] || ''

          const contentNodes: any[] = []
          let j = i + 1
          let closed = false

          while (j < children.length) {
            const sibling = children[j]
            if (sibling.type === 'element' && sibling.tagName === 'p') {
              if (getElementText(sibling).trim() === '!!!') {
                closed = true
                break
              }
            }
            contentNodes.push(sibling)
            j++
          }

          if (closed) {
            const titleNode = title
              ? {
                  type: 'element',
                  tagName: 'p',
                  properties: { className: ['admonition-title'] },
                  children: [{ type: 'text', value: title }]
                }
              : null

            children.splice(i, j - i + 1, {
              type: 'element',
              tagName: 'div',
              properties: { className: ['admonition', `admonition-${type}`] },
              children: [
                ...(titleNode ? [titleNode] : []),
                ...contentNodes
              ]
            })
            continue
          }
        }
      }

      i++
    }
  }
}

/**
 * 预处理：确保 !!! 标记前后有空行，使其成为独立段落
 * 跳过代码块内的 !!!
 */
const preprocessAdmonition = (markdown: string): string => {
  const lines = markdown.split('\n')
  const result: string[] = []
  let inCodeFence = false

  for (const line of lines) {
    if (/^(`{3,}|~{3,})/.test(line.trim())) {
      inCodeFence = !inCodeFence
    }

    if (!inCodeFence && (/^\s*!!!\s+\w+/.test(line) || /^\s*!!!\s*$/.test(line))) {
      if (result.length > 0 && result[result.length - 1] !== '') {
        result.push('')
      }
      result.push(line)
      result.push('')
    } else {
      result.push(line)
    }
  }

  return result.join('\n')
}

/**
 * Markdown 渲染 Composable
 */
export const useMarkdown = () => {
  const createProcessor = (includeHighlight = true) => {
    const processor = unified()
      .use(remarkParse)
      .use(remarkGfm)
      .use(remarkMath)
      .use(remarkSoftBreaks)
      .use(remarkVideo)
      .use(remarkDiagram)
      .use(remarkRehype, { allowDangerousHtml: true })
      .use(rehypeKatex)
      .use(rehypeSlug)
      .use(rehypeRaw)
      .use(rehypeSanitize, videoSanitizeSchema as any)
      .use(rehypeAdmonition)

    if (includeHighlight) {
      processor.use(rehypeLanguageAlias)
      processor.use(rehypeHighlightFallback)
      processor.use(rehypeHighlight, { detect: false, subset: false })
    }

    processor.use(rehypeStringify)
    return processor
  }

  const renderMarkdown = async (markdown: string): Promise<string> => {
    const processor = createProcessor()
    const result = await processor.process(preprocessAdmonition(preprocessSuperSub(markdown)))
    return String(result)
  }

  return { renderMarkdown }
}
