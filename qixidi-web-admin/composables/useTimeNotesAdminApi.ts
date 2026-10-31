/**
 * 小记管理 API（TimeNotesAdminController / TimeNotesCommentAdminController，/system/time/notes[/comment]）
 * 列表全量用户 + 详情 + 删除（含相册清理）；新增/编辑走前台用户端，后台不提供
 */

/** 小记行（TimeNotesAdminVo） */
export interface TimeNotesRow {
  id: number;
  title?: string;
  content?: string;
  uid?: string;
  nickname?: string;
  avatar?: string;
  /** 记录时间（yyyy-MM-dd） */
  recordTime?: string;
}

/** 小记评论行（TimeNotesCommentVo） */
export interface TimeNotesCommentRow {
  id: number;
  /** 所在小记 id */
  timeNotesId: number;
  /** 所在小记标题（列表批量填充展示字段） */
  timeNotesTitle?: string;
  /** 小记作者 uid */
  uid?: string;
  parentId?: number;
  /** 评论等级（1 一级 / 2 二级） */
  commentGrade?: number;
  targetId?: number;
  targetUid?: string;
  commentUid?: string;
  content: string;
  /** 评论类型（1 小记 / 2 评论） */
  type?: number;
  /** 评论状态（0 正常 / 1 已删除） */
  status?: number;
  createTime?: string;
  /** 评论人（批量填充） */
  nickname?: string;
  avatar?: string;
  /** 回复对象（批量填充） */
  targetNickname?: string;
}

/** 小记评论 status：0 正常 / 1 已删除（实体注释） */
export const TIME_NOTES_COMMENT_STATUS_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已删除', type: 'error' }
};

export const useTimeNotesAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/time/notes/list */
    getTimeNotesList: (params: Record<string, any>) =>
      api.postPage<TimeNotesRow>('/system/time/notes/list', params),

    /** 详情：GET /system/time/notes/{id} */
    getTimeNote: (id: number | string) => api.get<TimeNotesRow>(`/system/time/notes/${id}`),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/time/notes/{ids} */
    deleteTimeNotes: (ids: (number | string)[]) =>
      api.request<void>(`/system/time/notes/${ids.join(',')}`, { method: 'DELETE' }),

    /** 评论分页列表：POST /system/time/notes/comment/list */
    getTimeNotesCommentList: (params: Record<string, any>) =>
      api.postPage<TimeNotesCommentRow>('/system/time/notes/comment/list', params),

    /** 评论批量删除（路径参数逗号分隔）：DELETE /system/time/notes/comment/{ids} */
    deleteTimeNotesComments: (ids: (number | string)[]) =>
      api.request<void>(`/system/time/notes/comment/${ids.join(',')}`, { method: 'DELETE' })
  };
};
