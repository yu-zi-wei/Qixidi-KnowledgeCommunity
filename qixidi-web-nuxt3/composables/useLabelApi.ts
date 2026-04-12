import type { LabelGrouping } from '~/types'

interface LabelDetailVo {
  id: number
  uid?: string
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  isFollow?: boolean
  createTime?: string
  groupingName?: string
}

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

  /**
   * 标签详情
   */
  const getLabelInfo = (id: number) => {
    return api.get<LabelDetailVo>(`/white/label/info/${id}`)
  }

  /** 获取系统标签列表（前台展示） */
  const getSystemLabels = (label?: string) => {
    return api.get<LabelDetailVo[]>('/white/dictum/system/label', label ? { label } : undefined)
  }

  return { getGroupingList, getList, getLabelInfo, getSystemLabels }
}
