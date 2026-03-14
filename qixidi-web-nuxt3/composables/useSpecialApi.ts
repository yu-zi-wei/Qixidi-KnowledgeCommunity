/**
 * 专栏 API
 */
export const useSpecialApi = () => {
  const api = useApi()

  /**
   * 查询用户专栏列表
   * 返回直接数组，非分页格式
   */
  const getList = async (): Promise<SpecialInfo[]> => {
    return api.get<SpecialInfo[]>('/white/aut/special/list')
  }

  return {
    getList
  }
}
