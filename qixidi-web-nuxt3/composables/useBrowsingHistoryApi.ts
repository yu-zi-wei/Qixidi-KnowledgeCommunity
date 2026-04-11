export interface BrowsingHistoryVo {
  id: number
  uid: string
  targetId: number
  targetTitle: string
  targetUid: string
  targetType: number // 1=文章, 2=帖子
  createTime: string
  updateTime: string
}

export const useBrowsingHistoryApi = () => {
  const api = useApi()

  /** 获取浏览历史列表 */
  const getList = (params: { pageNum: number; pageSize: number; uid: string }) => {
    return api.getPage<BrowsingHistoryVo>('/frontDesk/browsing/history/list', params)
  }

  return { getList }
}
