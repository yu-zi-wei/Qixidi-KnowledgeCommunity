import type { SearchRecordsVo, TableDataInfo } from '~/types'

export const useSearchApi = () => {
  const api = useApi()

  /**
   * 获取搜索历史记录列表
   * 接口：/frontDesk/search/records/list
   * @param pageNum - 页码，从 1 开始
   * @param pageSize - 每页数量
   * @param uid - 用户 ID（登录用户才有）
   */
  const getSearchHistory = (params: {
    pageNum?: number
    pageSize?: number
    uid?: string
  }) => {
    return api.getPage<SearchRecordsVo>('/frontDesk/search/records/list', params)
  }

  return {
    getSearchHistory
  }
}
