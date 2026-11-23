/**
 * 版本迭代接口：代理 Gitee 发行版（Gitee API 无 CORS 头，浏览器直调会被同源策略拦截，必须服务端代理）
 * GET /changelog-data → { current: 'v2.1.0', releases: [{ tag, name, body, publishedAt }] }
 *
 * 路径说明：不放 /api 下——dev 的 devProxy 把 /api 整体转发到后端 9001，会拦掉 Nitro 自身的 /api 路由；
 * /changelog-data 与 rss.xml 同层（server/routes），dev/生产行为一致
 *
 * 缓存策略：进程内存缓存 1 小时（Gitee 匿名 API 有频率限制）；
 * Gitee 临时不可达时回退上次缓存；首次即失败返回空数据（页面显示空态，不炸）
 */
interface GiteeRelease {
  tag_name?: string
  name?: string
  body?: string
  created_at?: string
}

interface ChangelogRelease {
  tag: string
  name: string
  body: string
  publishedAt: string
}

interface ChangelogData {
  current: string
  releases: ChangelogRelease[]
}

const CACHE_TTL_MS = 60 * 60 * 1000

// Nitro worker 级内存缓存（单实例部署足够）
let cache: { data: ChangelogData; fetchedAt: number } | null = null

const REPO_OWNER = 'yu-zi-wei'
const REPO_NAME = 'qixidi'

const fetchFromGitee = async (): Promise<ChangelogData> => {
  const resp = await $fetch<GiteeRelease[]>(`https://gitee.com/api/v5/repos/${REPO_OWNER}/${REPO_NAME}/releases`, {
    query: { page: 1, per_page: 50 },
    headers: { 'User-Agent': 'qixidi-web' },
    timeout: 10000
  })
  const releases: ChangelogRelease[] = (resp || [])
    .filter(r => r.tag_name)
    .map(r => ({
      tag: r.tag_name as string,
      name: r.name || r.tag_name || '',
      body: r.body || '',
      // published_at 可能为空（未单独填），发行版创建时间即发布时间
      publishedAt: r.created_at || ''
    }))
    // 按发布时间倒序（最新版本在前）
    .sort((a, b) => (b.publishedAt || '').localeCompare(a.publishedAt || ''))
  return { current: releases[0]?.tag || '', releases }
}

export default defineEventHandler(async (): Promise<ChangelogData> => {
  const now = Date.now()
  if (cache && now - cache.fetchedAt < CACHE_TTL_MS) {
    return cache.data
  }
  try {
    const data = await fetchFromGitee()
    cache = { data, fetchedAt: now }
    return data
  } catch (err) {
    // 回退上次缓存；无缓存则给空数据（页面空态）
    if (cache) {
      return cache.data
    }
    console.error('[changelog] gitee fetch failed:', (err as Error)?.message || err)
    return { current: '', releases: [] }
  }
})
