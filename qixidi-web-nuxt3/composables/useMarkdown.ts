import { unified } from 'unified'
import remarkParse from 'remark-parse'
import remarkGfm from 'remark-gfm'
import remarkRehype from 'remark-rehype'
import rehypeSlug from 'rehype-slug'
import rehypeRaw from 'rehype-raw'
import rehypeSanitize from 'rehype-sanitize'
import rehypeHighlight from 'rehype-highlight'
import rehypeStringify from 'rehype-stringify'

/**
 * Markdown 渲染 Composable
 * 使用 unified + remark + rehype 安全渲染 Markdown
 */
export const useMarkdown = () => {
  const createProcessor = (includeHighlight = true) => {
    const processor = unified()
      .use(remarkParse)
      .use(remarkGfm)
      .use(remarkRehype, { allowDangerousHtml: true })
      .use(rehypeSlug)
      .use(rehypeRaw)
      .use(rehypeSanitize)

    // 只在客户端使用代码高亮，避免 Hydration mismatch
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
