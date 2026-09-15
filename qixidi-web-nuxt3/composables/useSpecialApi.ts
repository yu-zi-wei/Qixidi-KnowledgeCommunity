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

  /**
   * 新增专栏
   * 接口：POST /white/aut/special
   * @returns 新创建专栏的 id
   */
  const create = async (data: {
    specialName: string
    specialIntroduce: string
    cover: string
  }): Promise<number> => {
    return api.post<number>('/white/aut/special', data)
  }

  return {
    getList,
    create
  }
}
