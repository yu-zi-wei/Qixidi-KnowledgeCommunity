/**
 * 评论管理 API（ArticleCommentController，/system/article/comment）
 * M2 范围只读 + 删除；新增/编辑走前台用户端，后台不提供
 */

/** 评论行（ArticleCommentVo 列表用到的字段子集） */
export interface CommentRow {
  id: number;
  articleId: number;
  uid: string;
  parentId?: number;
  commentGrade?: number;
  targetId?: string;
  targetUid?: string;
  targetName?: string;
  commentUid?: string;
  commentName?: string;
  commentAvatar?: string;
  content: string;
  type?: number;
  state?: number;
  createTime?: string;
  updateTime?: string;
  commentTotal?: number;
}

/** 评论 state：0 正常 / 1 已删除（VO 注解） */
export const COMMENT_STATE_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已删除', type: 'error' }
};

export const useCommentAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：GET /system/article/comment/list */
    getComments: (params: Record<string, any>) =>
      api.getPage<CommentRow>('/system/article/comment/list', params),

    /** 详情：GET /system/article/comment/{id} */
    getComment: (id: number | string) => api.get<CommentRow>(`/system/article/comment/${id}`),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/article/comment/{ids} */
    deleteComments: (ids: (number | string)[]) =>
      api.request<void>(`/system/article/comment/${ids.join(',')}`, { method: 'DELETE' })
  };
};
