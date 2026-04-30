import { unified } from 'unified'
import remarkParse from 'remark-parse'
import remarkGfm from 'remark-gfm'
import remarkRehype from 'remark-rehype'
import rehypeSlug from 'rehype-slug'
import rehypeRaw from 'rehype-raw'
import rehypeSanitize from 'rehype-sanitize'
import rehypeHighlight from 'rehype-highlight'
import rehypeStringify from 'rehype-stringify'
import { visit } from 'unist-util-visit'
import type { Plugin, Transformer } from 'unified'
import type { Root } from 'hast'

/**
 * remark 插件：blockquote 内软换行转为硬换行
 * 解决 > 12\n> 34\n> 45 渲染为一行的问题
 */
const remarkBlockquoteLineBreaks: Plugin = (): Transformer => {
  return (tree: any) => {
    visit(tree, 'blockquote', (blockNode: any) => {
      if (!blockNode.children) return
      for (const child of blockNode.children) {
        if (child.type !== 'paragraph' || !child.children) continue
        const newChildren: any[] = []
        for (const inline of child.children) {
          if (inline.type === 'text' && typeof inline.value === 'string' && inline.value.includes('\n')) {
            const parts = inline.value.split('\n')
            for (let i = 0; i < parts.length; i++) {
              if (parts[i]) newChildren.push({ type: 'text', value: parts[i] })
              if (i < parts.length - 1) newChildren.push({ type: 'break' })
            }
          } else {
            newChildren.push(inline)
          }
        }
        child.children = newChildren
      }
    })
  }
}

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
                value: `<div class="video-embed" data-video-type="${videoInfo.type}" data-video-url="${videoInfo.embedUrl || ''}" data-video-id="${videoInfo.videoId || ''}" data-video-title="${match[1] || ''}" data-video-poster="${match[2] || ''}"></div>`
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
              value: `<div class="video-embed" data-video-type="${videoInfo.type}" data-video-url="${videoInfo.embedUrl || ''}" data-video-id="${videoInfo.videoId || ''}"></div>`
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
 * 自定义 sanitize schema
 */
const videoSanitizeSchema = {
  tagNames: [
    'div', 'span', 'p', 'br', 'hr',
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'ul', 'ol', 'li',
    'blockquote', 'pre', 'code',
    'strong', 'em', 'del', 'ins',
    'a', 'img',
    'table', 'thead', 'tbody', 'tr', 'th', 'td',
    'video', 'source', 'iframe',
    'figure', 'figcaption'
  ],
  attributes: {
    '*': ['className', 'class', 'dataVideoType', 'data-video-type', 'dataVideoUrl', 'data-video-url', 'dataVideoId', 'data-video-id', 'dataVideoTitle', 'data-video-title', 'dataVideoPoster', 'data-video-poster', 'id'],
    a: ['href', 'title', 'target', 'rel'],
    img: ['src', 'alt', 'title', 'width', 'height', 'loading'],
    video: ['src', 'poster', 'controls', 'preload', 'playsinline', 'webkitPlaysinline', 'width', 'height'],
    source: ['src', 'type'],
    iframe: ['src', 'title', 'width', 'height', 'frameborder', 'allow', 'allowfullscreen', 'scrolling'],
    code: ['className', 'class'],
    pre: ['className', 'class'],
    div: ['className', 'class', 'id', 'dataVideoType', 'data-video-type', 'dataVideoUrl', 'data-video-url', 'dataVideoId', 'data-video-id', 'dataVideoTitle', 'data-video-title', 'dataVideoPoster', 'data-video-poster'],
    th: ['align'],
    td: ['align']
  },
  protocols: {
    href: ['http', 'https'],
    src: ['http', 'https', 'data']
  }
}

/**
 * Markdown 渲染 Composable
 */
export const useMarkdown = () => {
  const createProcessor = (includeHighlight = true) => {
    const processor = unified()
      .use(remarkParse)
      .use(remarkGfm)
      .use(remarkBlockquoteLineBreaks)
      .use(remarkVideo)
      .use(remarkRehype, { allowDangerousHtml: true })
      .use(rehypeSlug)
      .use(rehypeRaw)
      .use(rehypeSanitize, videoSanitizeSchema as any)

    if (includeHighlight && import.meta.client) {
      processor.use(rehypeHighlight, { detect: true, subset: false })
    }

    processor.use(rehypeStringify)
    return processor
  }

  const renderMarkdown = async (markdown: string): Promise<string> => {
    const processor = createProcessor()
    const result = await processor.process(markdown)
    return String(result)
  }

  return { renderMarkdown }
}
