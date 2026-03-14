import type { Navigation } from '~/types'

export const useNavigationApi = () => {
  const api = useApi()

  /**
   * 获取导航栏列表（分页接口，TableDataInfo 直接返回）
   */
  const getList = (type: number = 1, status: number = 0) => {
    return api.getPage<Navigation>(
      '/white/configure/navigation/list',
      { type, status }
    )
  }

  return { getList }
}
