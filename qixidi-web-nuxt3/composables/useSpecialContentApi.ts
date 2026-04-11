/**
 * 专栏内容管理 API
 */
export const useSpecialContentApi = () => {
  const api = useApi()

  /**
   * 获取用户文章列表（用于穿梭框左侧）
   */
  const getUserArticleList = (params: { userId: string; pageNum?: number; pageSize?: number }) => {
    return api.getPage<{
      id: number
      articleTitle: string
      articleCover?: string
      articleAbstract?: string
    }>('/white/article/user/list', params)
  }

  /**
   * 获取专栏内的文章列表（用于穿梭框右侧）
   * 注意：后端返回的是 ArticleInformationVo 数组，不是 ID 数组
   */
  const getSpecialArticleIds = (specialId: number, uid: string) => {
    return api.get<{ id: number }[]>('/white/select/special', { id: specialId, uid })
  }

  /**
   * 更新专栏文章（保存）
   */
  const updateSpecialArticles = (specialId: number, uid: string, articleIds: string[]) => {
    return api.post(`/white/update/special/${specialId}/${uid}`, articleIds)
  }

  return {
    getUserArticleList,
    getSpecialArticleIds,
    updateSpecialArticles
  }
}
