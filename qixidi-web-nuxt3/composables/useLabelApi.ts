import type { LabelGrouping } from '~/types'

export const useLabelApi = () => {
  const api = useApi()

  /**
   * 获取标签分组列表（前台展示用）
   */
  const getGroupingList = (pageNum: number = 0, pageSize: number = 12) => {
    return api.getPage<LabelGrouping>(
      '/white/label/grouping/list',
      { pageNum, pageSize }
    )
  }

  /**
   * 查询所有标签（编辑器用）
   */
  const getList = async (): Promise<LabelInfo[]> => {
    const result = await api.getPage<LabelInfo>('/business/label-info/list')
    return result.rows || []
  }

  return { getGroupingList, getList }
}
