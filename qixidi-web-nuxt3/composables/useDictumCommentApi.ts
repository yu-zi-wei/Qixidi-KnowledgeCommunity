import type { CommentItem, DictumCommentBo, DictumCommentVo, PageQuery, TableDataInfo } from '~/types'

export const useDictumCommentApi = () => {
  const api = useApi()

  /**
   * 获取随笔评论列表（分页）
   * 接口：GET /white/dictum/comment/list/{id}
   * @param dictumId - 随笔 id
   * @param pageQuery - 分页参数
   */
  const getCommentList = async (
    dictumId: string | number,
    pageQuery?: PageQuery
  ): Promise<TableDataInfo<CommentItem>> => {
    const result = await api.getPage<DictumCommentVo>(
      `/white/dictum/comment/list/${dictumId}`,
      pageQuery
    )
    //后端次级集合字段是 dictumCommentVoList，映射为通用评论组件的 children
    return {
      total: result.total,
      rows: result.rows.map(row => ({ ...row, children: row.dictumCommentVoList || [] }))
    }
  }

  /**
   * 新增评论
   * 接口：POST /frontDesk/dictum/comment/add
   * @param data - 评论数据
   * @returns 后端生成的新评论（至少含真实 id，供乐观更新原位落定）
   */
  const addComment = async (data: DictumCommentBo): Promise<Partial<CommentItem>> => {
    return await api.post<Partial<CommentItem>>('/frontDesk/dictum/comment/add', data)
  }

  /**
   * 删除评论
   * 接口：DELETE /frontDesk/dictum/comment/delete/{id}
   * @param id - 评论 id
   */
  const deleteComment = async (id: number | string): Promise<void> => {
    await api.delete(`/frontDesk/dictum/comment/delete/${id}`)
  }

  return {
    getCommentList,
    addComment,
    deleteComment
  }
}
