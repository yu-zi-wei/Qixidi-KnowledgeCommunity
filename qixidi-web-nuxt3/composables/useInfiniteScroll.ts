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
   * 初始化数据（用于 SSR 首屏渲染）
   * @param force - 是否强制重新加载（忽略已有数据）
   */
  const initArticles = async (force = false) => {
    // 检查是否已有数据
    if (articles.value.length > 0 && !force) {
      // 有数据则恢复滚动位置（延迟执行，确保 DOM 已渲染）
      if (import.meta.client) {
        nextTick(() => {
          const savedPosition = localStorage.getItem(`scroll-${currentStoreKey.value}`)
          if (savedPosition) {
            // 使用 instant 避免滚动动画
            window.scrollTo({ top: Number(savedPosition), behavior: 'instant' })
          }
        })
      }
      return
    }

    // 无数据或强制刷新则加载第一页
    loadingMore.value = true
    try {
      const result = await fetchFn(1)
      articlesStore.initPage(currentStoreKey.value, result.rows, result.total)
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

  // 离开页面时保存滚动位置
  onBeforeRouteLeave(() => {
    saveScrollPosition()
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
