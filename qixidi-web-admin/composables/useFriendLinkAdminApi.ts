/**
 * 友链管理 API（FriendLinkAdminController，/system/friendLink）
 */

/** 友链行（FriendLinkVo） */
export interface FriendLinkRow {
  id: number;
  linkName?: string;
  linkIntro?: string;
  linkAvatar?: string;
  linkUrl?: string;
  /** 0 上架 / 1 下架（前台只展示上架） */
  status?: number;
  createTime?: string;
}

/** 友链状态 */
export const FRIEND_LINK_STATUS_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '上架', type: 'success' },
  1: { label: '下架', type: 'error' }
};

export const useFriendLinkAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/friendLink/list */
    getFriendLinkList: (params: Record<string, any>) =>
      api.postPage<FriendLinkRow>('/system/friendLink/list', params),

    /** 详情：GET /system/friendLink/{id} */
    getFriendLink: (id: number | string) => api.get<FriendLinkRow>(`/system/friendLink/${id}`),

    /** 新增：POST /system/friendLink */
    addFriendLink: (data: Partial<FriendLinkRow>) => api.post<void>('/system/friendLink', data),

    /** 修改：PUT /system/friendLink */
    editFriendLink: (data: Partial<FriendLinkRow>) => api.put<void>('/system/friendLink', data),

    /** 批量删除（物理删）：DELETE /system/friendLink/{ids} */
    deleteFriendLinks: (ids: (number | string)[]) =>
      api.request<void>(`/system/friendLink/${ids.join(',')}`, { method: 'DELETE' })
  };
};
