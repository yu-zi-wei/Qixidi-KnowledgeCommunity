import type { NewsUserSumVo, NewsUserInfoVo, ArticleCommentNewsVo } from '~/types'

export const useNewsApi = () => {
  const api = useApi()

  /** 消息汇总（各类型未读数） */
  const getNewsSum = () => {
    return api.get<NewsUserSumVo[]>('/frontDesk/news/list/sum')
  }

  /** 评论消息列表 */
  const getCommentList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.getPage<ArticleCommentNewsVo>('/frontDesk/news/comment/list', { pageNum, pageSize })
  }

  /** 点赞消息列表 */
  const getFabulousList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.getPage<NewsUserInfoVo>('/frontDesk/news/fabulous/list', { pageNum, pageSize })
  }

  /** 关注消息列表 */
  const getFollowList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.getPage<NewsUserInfoVo>('/frontDesk/news/follow/list', { pageNum, pageSize })
  }

  /** 系统消息列表 */
  const getSystemList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.getPage<NewsUserInfoVo>('/frontDesk/news/system/list', { pageNum, pageSize })
  }

  /** 标记已读 */
  const markRead = (type: number) => {
    return api.get('/frontDesk/news/news-read', { type, beenRead: 1 })
  }

  return {
    getNewsSum,
    getCommentList,
    getFabulousList,
    getFollowList,
    getSystemList,
    markRead
  }
}
