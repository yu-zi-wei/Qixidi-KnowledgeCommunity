interface ToolInfoVo {
  id: number
  parentId?: number
  toolName: string
  describe?: string
  icon?: string
  isParent?: number
  toolUrl?: string
  order?: number
  type?: number
  state?: number
}

export const useToolApi = () => {
  const api = useApi()

  /** 获取父级工具分类列表 */
  const getToolCategories = () => {
    return api.get<ToolInfoVo[]>('/white/configure/tool/list')
  }

  /** 获取某个分类下的工具列表 */
  const getToolList = (parentId: number) => {
    return api.get<ToolInfoVo[]>('/white/configure/tool/child/list', { id: parentId, isParent: 2 })
  }

  return { getToolCategories, getToolList }
}
