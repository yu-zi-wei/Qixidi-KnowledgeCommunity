/**
 * 专辑管理 API（后台）
 */
export const useAdminSpecialApi = () => {
  const api = useApi()

  /**
   * 获取专辑列表
   */
  const getSpecialList = (uid: string) => {
    return api.get<{
      id: number
      specialName: string
      specialIntroduce?: string
      cover?: string
      state?: number
      uid?: string
      username?: string
      includedCount?: number
    }[]>('/white/aut/special/list', { uid })
  }

  /**
   * 创建专辑
   */
  const createSpecial = (data: { specialName: string; specialIntroduce: string; cover: string }) => {
    return api.post('/special/information', data)
  }

  /**
   * 更新专辑
   */
  const updateSpecial = (data: { id: number; specialName: string; specialIntroduce: string; cover: string }) => {
    return api.put('/special/information', data)
  }

  /**
   * 删除专辑
   */
  const deleteSpecial = (id: number) => {
    return api.delete(`/white/delete/special/${id}`)
  }

  return {
    getSpecialList,
    createSpecial,
    updateSpecial,
    deleteSpecial
  }
}
