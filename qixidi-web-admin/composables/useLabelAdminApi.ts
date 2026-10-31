/**
 * 标签/标签分类 API（LabelInfoController /system/label-info + LabelGroupingInfoController /system/groupingInfo）
 * 合并接口：源标签的文章挂载与关注（或源分类下的标签与文章归属）迁移至目标后删除源，计数由后端定时任务重算
 */

/** 标签行（LabelInfoVo 字段子集） */
export interface LabelRow {
  id: number;
  labelName?: string;
  labelDescribe?: string;
  /** 所属分类 id */
  labelGroupingId?: number;
  /** 所属分类名（列表 join 产物，详情接口不返回） */
  groupingName?: string;
  articleNumber?: number;
  followNumber?: number;
  state?: number;
  createTime?: string;
}

/** 标签分类行（LabelGroupingInfoVo 字段子集） */
export interface GroupingRow {
  id: number;
  groupingName?: string;
  groupingDescribe?: string;
  icon?: string;
  /** 收录文章数（定时任务重算的冗余计数） */
  entriesNumber?: number;
  state?: number;
  createTime?: string;
}

/** 状态：0 正常 / 1 已删除 */
export const LABEL_STATE_MAP: Record<number, { label: string; type: 'success' | 'default' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已删除', type: 'default' }
};

export const useLabelAdminApi = () => {
  const api = useApi();

  return {
    /** 标签分页列表：POST /system/label-info/list（labelName like，labelGroupingId/state eq） */
    getLabels: (params: Record<string, any>) => api.postPage<LabelRow>('/system/label-info/list', params),

    /** 标签详情：GET /system/label-info/{id}（groupingName 为列表 join 产物，此处为空） */
    getLabel: (id: number | string) => api.get<LabelRow>(`/system/label-info/${id}`),

    /** 新增标签：POST /system/label-info（labelName/labelGroupingId 必填） */
    addLabel: (data: Partial<LabelRow>) => api.post<void>('/system/label-info', data),

    /** 编辑标签：PUT /system/label-info（labelName/labelGroupingId 必填） */
    updateLabel: (data: Partial<LabelRow>) => api.put<void>('/system/label-info', data),

    /** 合并标签：POST /system/label-info/merge（源标签文章与关注迁入目标后删除源） */
    mergeLabel: (sourceId: number, targetId: number) =>
      api.post<void>('/system/label-info/merge', { sourceId, targetId }),

    /** 批量删除标签：DELETE /system/label-info/{ids}（物理删除，不清理文章上的残留引用） */
    deleteLabels: (ids: (number | string)[]) =>
      api.request<void>(`/system/label-info/${ids.join(',')}`, { method: 'DELETE' }),

    /** 分类分页列表：POST /system/groupingInfo/list（groupingName like，state eq） */
    getGroupings: (params: Record<string, any>) => api.postPage<GroupingRow>('/system/groupingInfo/list', params),

    /** 分类详情：GET /system/groupingInfo/{id} */
    getGrouping: (id: number | string) => api.get<GroupingRow>(`/system/groupingInfo/${id}`),

    /** 新增分类：POST /system/groupingInfo（groupingName/groupingDescribe 必填） */
    addGrouping: (data: Partial<GroupingRow>) => api.post<void>('/system/groupingInfo', data),

    /** 编辑分类：PUT /system/groupingInfo（groupingName/groupingDescribe 必填） */
    updateGrouping: (data: Partial<GroupingRow>) => api.put<void>('/system/groupingInfo', data),

    /** 合并分类：POST /system/groupingInfo/merge（源分类下标签与文章归属迁入目标后删除源） */
    mergeGrouping: (sourceId: number, targetId: number) =>
      api.post<void>('/system/groupingInfo/merge', { sourceId, targetId }),

    /** 批量删除分类：DELETE /system/groupingInfo/{ids}（物理删除，分类下标签失去归属） */
    deleteGroupings: (ids: (number | string)[]) =>
      api.request<void>(`/system/groupingInfo/${ids.join(',')}`, { method: 'DELETE' })
  };
};
