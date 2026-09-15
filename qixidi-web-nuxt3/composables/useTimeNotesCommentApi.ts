import type { CommentItem, PageQuery, TableDataInfo, TimeNotesCommentBo } from '~/types'

/**
 * 时光小记评论 API
 */
export const useTimeNotesCommentApi = () => {
  const api = useApi()

  /**
   * 获取时光小记评论列表（分页，免登录）
   * 接口：GET /white/time/notes/comment/list/{id}
   * @param timeNotesId - 时光小记 id
   * @param pageQuery - 分页参数
   */
  const getCommentList = async (
    timeNotesId: string | number,
    pageQuery?: PageQuery
  ): Promise<TableDataInfo<CommentItem>> => {
    return await api.getPage<CommentItem>(
      `/white/time/notes/comment/list/${timeNotesId}`,
      pageQuery
    )
  }

  /**
   * 新增评论（需登录）
   * 接口：POST /frontDesk/time/notes/comment/add
   * @param data - 评论数据
   * @returns 后端生成的新评论（至少含真实 id，供乐观更新原位落定）
   */
  const addComment = async (data: TimeNotesCommentBo): Promise<Partial<CommentItem>> => {
    return await api.post<Partial<CommentItem>>('/frontDesk/time/notes/comment/add', data)
  }

  /**
   * 删除评论（仅本人，需登录）
   * 接口：DELETE /frontDesk/time/notes/comment/delete/{id}
   * @param id - 评论 id
   */
  const deleteComment = async (id: number | string): Promise<void> => {
    await api.delete(`/frontDesk/time/notes/comment/delete/${id}`)
  }

  return {
    getCommentList,
    addComment,
    deleteComment
  }
}
