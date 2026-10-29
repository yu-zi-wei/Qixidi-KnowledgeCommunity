/**
 * 文章分类 API
 */
export const useGroupingApi = () => {
  const api = useApi()

  /**
   * 查询所有分类
   */
  const getList = async (): Promise<GroupingInfo[]> => {
    const result = await api.getPage<GroupingInfo>('/system/groupingInfo/list')
    return result.rows || []
  }

  return {
    getList
  }
}
