import type { Ref, ComputedRef } from 'vue'
import type { ArticleInfo } from '~/types'

interface UseInfiniteScrollOptions {
  // 数据获取函数
  fetchFn: (page: number) => Promise<{ rows: ArticleInfo[]; total: number }>
  // Store 键（用于区分不同页面，可以是 computed）
  storeKey: string | Ref<string> | ComputedRef<string>
  // 页面大小
  pageSize?: number
}

// Tab 切换页面路径：这些页面间切换时需要刷新数据
const tabPaths = ['/', '/featured', '/follow', '/latest']

function isTabNavigation(path: string): boolean {
  if (tabPaths.includes(path)) return true
  if (path.match(/^\/category\//)) return true
  return false
}

// 记录哪些 storeKey 应该在返回时使用缓存（从详情页返回）
const preserveCacheKeys = new Set<string>()

/**
 * 无限滚动 Hook
 * - 自动管理文章列表和分页状态
 * - 自动保存/恢复滚动位置
 * - 数据持久化到 Store
 * - 支持 storeKey 动态变化
 * - 客户端检查保护，避免 SSR 问题
 */
export const useInfiniteScroll = (options: UseInfiniteScrollOptions) => {
  const { fetchFn, storeKey, pageSize = 10 } = options

  const articlesStore = useArticlesStore()

  // 加载更多状态
  const loadingMore = ref(false)

  // 计算当前的 storeKey（响应式）
  const currentStoreKey = computed(() => {
    return typeof storeKey === 'string' ? storeKey : storeKey.value
  })

  // 从 Store 获取数据（响应式）
  const articles = computed(() => articlesStore.getArticles(currentStoreKey.value))
  const pageNum = computed(() => articlesStore.getPageNum(currentStoreKey.value))
  const total = computed(() => articlesStore.getTotal(currentStoreKey.value))
  const hasMore = computed(() => articlesStore.hasMore(currentStoreKey.value))

  // 判断是否已加载完所有数据
  const noMore = computed(() => {
    return articles.value.length >= total.value && total.value > 0
  })

  /**
   * 加载更多文章
   */
  const loadMore = async () => {
    if (loadingMore.value || noMore.value) return

    loadingMore.value = true
    try {
      const nextPage = pageNum.value + 1
      const result = await fetchFn(nextPage)

      // 追加到 Store（Store 会自动更新 pageNum）
      articlesStore.appendArticles(currentStoreKey.value, result.rows, result.total)
    } catch (error) {
      console.error('加载文章失败:', error)
    } finally {
      loadingMore.value = false
    }
  }

  /**
   * 初始化数据
   * - 从详情页返回：使用缓存 + 恢复滚动位置
   * - 其他场景（首次访问、Tab 切换）：获取最新数据
   */
  const initArticles = async () => {
    if (loadingMore.value) return

    const key = currentStoreKey.value

    // 从详情页返回 → 使用缓存，恢复滚动位置
    if (preserveCacheKeys.has(key)) {
      preserveCacheKeys.delete(key)
      if (import.meta.client) {
        nextTick(() => {
          const savedPosition = localStorage.getItem(`scroll-${key}`)
          if (savedPosition) {
            window.scrollTo({ top: Number(savedPosition), behavior: 'instant' })
          }
        })
      }
      return
    }

    // 其他场景 → 获取最新数据
    loadingMore.value = true
    try {
      const result = await fetchFn(1)
      articlesStore.replacePage(key, result.rows, result.total)
    } catch (error) {
      console.error('加载文章失败:', error)
    } finally {
      loadingMore.value = false
    }
  }

  /**
   * 保存当前滚动位置
   */
  const saveScrollPosition = () => {
    if (import.meta.client) {
      localStorage.setItem(`scroll-${currentStoreKey.value}`, String(window.pageYOffset))
    }
  }

  /**
   * 监听滚动事件，自动保存位置
   */
  let scrollTimer: ReturnType<typeof setTimeout> | null = null
  const handleScroll = () => {
    if (scrollTimer) clearTimeout(scrollTimer)
    scrollTimer = setTimeout(() => {
      saveScrollPosition()
    }, 100) // 防抖，避免频繁保存
  }

  onMounted(() => {
    window.addEventListener('scroll', handleScroll, { passive: true })
    // 初始化数据
    initArticles()
  })

  onBeforeUnmount(() => {
    window.removeEventListener('scroll', handleScroll)
    if (scrollTimer) clearTimeout(scrollTimer)
  })

  // 离开页面时：保存滚动位置，非 Tab 切换时标记保留缓存
  onBeforeRouteLeave((to) => {
    saveScrollPosition()
    if (!isTabNavigation(to.path)) {
      preserveCacheKeys.add(currentStoreKey.value)
    }
  })

  return {
    articles,
    pageNum,
    total,
    loadingMore,
    noMore,
    hasMore,
    loadMore,
    initArticles
  }
}
