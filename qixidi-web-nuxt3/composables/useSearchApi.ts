import type { SearchRecordsVo, ArticleInfo, TimeNotesVo, TableDataInfo } from '~/types'

export const useSearchApi = () => {
  const api = useApi()

  const getSearchHistory = (params: {
    pageNum?: number
    pageSize?: number
    uid?: string
  }) => {
    return api.getPage<SearchRecordsVo>('/frontDesk/search/records/list', params)
  }

  /** 搜索文章 */
  const searchArticles = (params: {
    pageNum?: number
    pageSize?: number
    articleTitle?: string
  }) => {
    return api.getPage<ArticleInfo>('/white/article/list', params)
  }

  /** 搜索小记 */
  const searchTimeNotes = (params: {
    pageNum?: number
    pageSize?: number
    title?: string
  }) => {
    return api.postPage<TimeNotesVo>('/white/time/notes/list', params)
  }

  /** 搜索标签 */
  const searchLabels = (labelName: string) => {
    return api.get<any[]>('/white/label/list', { labelName, type: 2 })
  }

  /** 搜索用户 */
  const searchUsers = (nickname: string) => {
    return api.get<any[]>('/white/user/list', { nickname, type: 1 })
  }

  return {
    getSearchHistory,
    searchArticles,
    searchTimeNotes,
    searchLabels,
    searchUsers
  }
}
