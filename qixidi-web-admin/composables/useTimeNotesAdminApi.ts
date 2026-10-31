/**
 * 小记管理 API（TimeNotesAdminController，/system/time/notes）
 * 列表全量用户 + 删除（含相册清理）；新增/编辑走前台用户端，后台不提供
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

export const useTimeNotesAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/time/notes/list */
    getTimeNotesList: (params: Record<string, any>) =>
      api.postPage<TimeNotesRow>('/system/time/notes/list', params),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/time/notes/{ids} */
    deleteTimeNotes: (ids: (number | string)[]) =>
      api.request<void>(`/system/time/notes/${ids.join(',')}`, { method: 'DELETE' })
  };
};
