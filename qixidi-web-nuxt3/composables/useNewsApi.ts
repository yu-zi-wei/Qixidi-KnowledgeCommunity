import type { NewsUserSumVo, NewsUserInfoVo, ArticleCommentNewsVo, TimeNotesCommentNewsVo, DictumCommentNewsVo } from '~/types'

export const useNewsApi = () => {
  const api = useApi()

  /** 消息汇总（各类型未读数） */
  const getNewsSum = () => {
    return api.get<NewsUserSumVo[]>('/frontDesk/news/list/sum')
  }

  /** 评论消息列表（文章） */
  const getCommentList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<ArticleCommentNewsVo>('/frontDesk/news/comment/list', { pageNum, pageSize })
  }

  /** 小记评论消息列表 */
  const getTimeNotesCommentList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<TimeNotesCommentNewsVo>('/frontDesk/news/comment/time-notes/list', { pageNum, pageSize })
  }

  /** 随笔评论消息列表 */
  const getDictumCommentList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<DictumCommentNewsVo>('/frontDesk/news/comment/dictum/list', { pageNum, pageSize })
  }

  /** 点赞消息列表 */
  const getFabulousList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<NewsUserInfoVo>('/frontDesk/news/fabulous/list', { pageNum, pageSize })
  }

  /** 关注消息列表 */
  const getFollowList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<NewsUserInfoVo>('/frontDesk/news/follow/list', { pageNum, pageSize })
  }

  /** 系统消息列表 */
  const getSystemList = (pageNum: number = 1, pageSize: number = 20) => {
    return api.postPage<NewsUserInfoVo>('/frontDesk/news/system/list', { pageNum, pageSize })
  }

  /** 标记已读 */
  const markRead = (type: number) => {
    return api.post('/frontDesk/news/news-read', { type, beenRead: 1 })
  }

  return {
    getNewsSum,
    getCommentList,
    getTimeNotesCommentList,
    getDictumCommentList,
    getFabulousList,
    getFollowList,
    getSystemList,
    markRead
  }
}
