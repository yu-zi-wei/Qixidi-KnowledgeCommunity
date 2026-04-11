import type { DictumCommentBo, DictumCommentVo, PageQuery, TableDataInfo } from '~/types'

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
  ): Promise<TableDataInfo<DictumCommentVo>> => {
    return await api.getPage<DictumCommentVo>(
      `/white/dictum/comment/list/${dictumId}`,
      pageQuery
    )
  }

  /**
   * 新增评论
   * 接口：POST /frontDesk/dictum/comment/add
   * @param data - 评论数据
   */
  const addComment = async (data: DictumCommentBo): Promise<void> => {
    await api.post('/frontDesk/dictum/comment/add', data)
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
