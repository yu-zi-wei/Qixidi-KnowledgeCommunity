/**
 * 专栏管理 API（SpecialInformationController，/system/special/information）
 */

/** 专栏行（SpecialInformationVo；username/includedCount 为列表 join 产物） */
export interface SpecialRow {
  id: number;
  specialName?: string;
  specialIntroduce?: string;
  cover?: string;
  /** 0 正常 / 1 未启用 */
  state?: number;
  uid?: string;
  username?: string;
  /** 收录文章数（join 产物） */
  includedCount?: number;
  updateTime?: string;
  createTime?: string;
}

/** 专栏状态 */
export const SPECIAL_STATE_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '未启用', type: 'error' }
};

export const useSpecialAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/special/information/list（specialName 模糊 / states 多选 / username 模糊） */
    getSpecialList: (params: Record<string, any>) =>
      api.postPage<SpecialRow>('/system/special/information/list', params),

    /** 详情：GET /system/special/information/{id} */
    getSpecial: (id: number | string) =>
      api.get<SpecialRow>(`/system/special/information/${id}`),

    /** 修改：PUT /system/special/information（EditGroup 必填名称/简介） */
    editSpecial: (data: Partial<SpecialRow>) =>
      api.put<void>('/system/special/information', data),

    /** 批量删除：DELETE /system/special/information/{ids} */
    deleteSpecials: (ids: (number | string)[]) =>
      api.request<void>(`/system/special/information/${ids.join(',')}`, { method: 'DELETE' })
  };
};
