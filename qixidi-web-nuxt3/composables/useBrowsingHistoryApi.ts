export interface BrowsingHistoryVo {
  id: number
  uid: string
  targetId: number
  targetTitle: string
  targetUid: string
  targetType: number // 1=文章, 2=小记, 3=随笔
  actionType: number // 1=阅读, 2=点赞, 3=评论
  content?: string | null // 评论内容快照（仅评论行为有；评论删除不影响快照）
  createTime: string
  updateTime: string
}

export const useBrowsingHistoryApi = () => {
  const api = useApi()

  /** 获取浏览历史列表 */
  const getList = (params: { pageNum: number; pageSize: number; uid: string }) => {
    return api.postPage<BrowsingHistoryVo>('/frontDesk/browsing/history/list', params)
  }

  /** 记录浏览足迹（文章详情到达时调用；未登录后端静默跳过） */
  const addBrowsing = (data: { targetId: number; targetTitle: string; targetUid: string; targetType: number }) => {
    return api.post<void>('/frontDesk/browsing/history', { actionType: 1, ...data })
  }

  return { getList, addBrowsing }
}
