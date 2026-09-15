import type { ArticleCommentBo, ArticleCommentVo } from '~/types'

export const useCommentApi = () => {
  const api = useApi()

  /**
   * 获取文章评论列表
   * 接口：/api/white/article/comment/list
   * 返回 R<List<ArticleCommentVo>>，需要解包
   * @param articleId - 文章 id
   */
  const getCommentList = async (articleId: number): Promise<ArticleCommentVo[]> => {
    // 接口实际返回 R 包装的格式，使用 get 方法自动解包
    return await api.get<ArticleCommentVo[]>('/white/article/comment/list', { articleId })
  }

  /**
   * 新增评论
   * 接口：/api/article/comment/insert
   * @param data - 评论数据
   * @returns 新创建评论的 id
   */
  const insertComment = async (data: ArticleCommentBo): Promise<number> => {
    return await api.post<number>('/article/comment/insert', data)
  }

  /**
   * 删除评论
   * 接口：/api/article/delete/comment
   * 后端仅使用 id（子评论与关联通知由后端联动清理）
   * @param id - 评论 id
   */
  const deleteComment = async (id: number): Promise<void> => {
    await api.post('/article/delete/comment', { id })
  }

  return {
    getCommentList,
    insertComment,
    deleteComment
  }
}
