import { defineEventHandler, setResponseHeader } from 'h3'

interface ArticleItem {
  id: number
  articleTitle: string
  articleAbstract: string
  nickname: string
  createTime: string
}

export default defineEventHandler(async (event) => {
  const config = useRuntimeConfig()
  const apiBase = config.apiBase as string
  const siteName = config.public.siteName as string
  const siteUrl = getRequestURL(event).origin

  // TableDataInfo 格式：{ total, rows, code, msg }
  const res = await $fetch<{ total: number; rows: ArticleItem[] }>(
    `${apiBase}/white/article/sort`,
    {
      params: { pageNum: 1, pageSize: 20, createTime: 1 }
    }
  )

  const articles = res?.rows || []

  const escapeXml = (str: string) =>
    str
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&apos;')

  const items = articles
    .map(
      (a) => `    <item>
      <title>${escapeXml(a.articleTitle)}</title>
      <link>${siteUrl}/articles/${a.id}</link>
      <description>${escapeXml(a.articleAbstract || '')}</description>
      <author>${escapeXml(a.nickname)}</author>
      <pubDate>${new Date(a.createTime).toUTCString()}</pubDate>
      <guid isPermaLink="true">${siteUrl}/articles/${a.id}</guid>
    </item>`
    )
    .join('\n')

  const xml = `<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
  <channel>
    <title>${escapeXml(siteName)}</title>
    <link>${siteUrl}</link>
    <description>${escapeXml(siteName)} - 专注分享，记录成长</description>
    <language>zh-CN</language>
    <lastBuildDate>${new Date().toUTCString()}</lastBuildDate>
    <atom:link href="${siteUrl}/rss.xml" rel="self" type="application/rss+xml" />
${items}
  </channel>
</rss>`

  setResponseHeader(event, 'content-type', 'application/xml; charset=utf-8')
  return xml
})
