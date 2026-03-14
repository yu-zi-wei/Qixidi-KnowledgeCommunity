import { defineStore } from 'pinia'
import type { ArticleInfo } from '~/types'

interface ArticlePageState {
  articles: ArticleInfo[]
  pageNum: number
  total: number
  scrollPosition: number
  hasMore: boolean
}

interface ArticlesState {
  pages: Record<string, ArticlePageState>
}

export const useArticlesStore = defineStore('articles', {
  state: (): ArticlesState => ({
    pages: {}
  }),

  getters: {
    // 获取指定页面的状态
    getPageState: (state) => (key: string): ArticlePageState => {
      if (!state.pages[key]) {
        return {
          articles: [],
          pageNum: 1,
          total: 0,
          scrollPosition: 0,
          hasMore: true
        }
      }
      return state.pages[key]
    },

    // 获取指定页面的文章列表
    getArticles: (state) => (key: string): ArticleInfo[] => {
      return state.pages[key]?.articles || []
    },

    // 获取指定页面的当前页码
    getPageNum: (state) => (key: string): number => {
      return state.pages[key]?.pageNum || 1
    },

    // 获取指定页面的总数
    getTotal: (state) => (key: string): number => {
      return state.pages[key]?.total || 0
    },

    // 获取指定页面的滚动位置
    getScrollPosition: (state) => (key: string): number => {
      return state.pages[key]?.scrollPosition || 0
    },

    // 判断是否还有更多数据
    hasMore: (state) => (key: string): boolean => {
      const pageState = state.pages[key]
      if (!pageState) return true
      return pageState.articles.length < pageState.total && pageState.total > 0
    }
  },

  actions: {
    // 初始化或更新页面数据（用于 SSR 首屏渲染）
    initPage(key: string, articles: ArticleInfo[], total: number) {
      if (!this.pages[key]) {
        this.pages[key] = {
          articles: [],
          pageNum: 1,
          total: 0,
          scrollPosition: 0,
          hasMore: true
        }
      }

      // 只有当数据为空时才初始化，避免覆盖已加载的数据
      if (this.pages[key].articles.length === 0) {
        this.pages[key].articles = articles
        this.pages[key].total = total
      }
    },

    // 追加文章列表（用于加载更多）
    appendArticles(key: string, newArticles: ArticleInfo[], total: number) {
      if (!this.pages[key]) {
        this.pages[key] = {
          articles: [],
          pageNum: 0,
          total: 0,
          scrollPosition: 0,
          hasMore: true
        }
      }

      this.pages[key].articles.push(...newArticles)
      this.pages[key].pageNum += 1
      this.pages[key].total = total
    },

    // 保存滚动位置
    saveScrollPosition(key: string, position: number) {
      if (this.pages[key]) {
        this.pages[key].scrollPosition = position
      }
    },

    // 重置指定页面的数据
    resetPage(key: string) {
      if (this.pages[key]) {
        this.pages[key].articles = []
        this.pages[key].pageNum = 1
        this.pages[key].total = 0
        this.pages[key].scrollPosition = 0
        this.pages[key].hasMore = true
      }
    },

    // 清除所有页面数据（用于退出登录等场景）
    clearAll() {
      this.pages = {}
    }
  }
})
