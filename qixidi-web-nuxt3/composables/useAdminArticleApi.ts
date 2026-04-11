import type { AdminArticleItem } from '~/types'

export const useAdminArticleApi = () => {
  const api = useApi()

  /**
   * 获取用户文章列表
   */
  const getArticleList = (params: {
    pageNum?: number
    pageSize?: number
    articleTitle?: string
    auditState?: number
  }) => {
    return api.getPage<AdminArticleItem>('/user/article/list', params)
  }

  /**
   * 删除文章
   */
  const deleteArticle = (id: number) => {
    return api.delete(`/user/delete/article/${id}`)
  }

  return {
    getArticleList,
    deleteArticle
  }
}
