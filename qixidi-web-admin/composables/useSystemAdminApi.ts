/**
 * 系统管理 API（屏蔽词 / 系统消息）
 * 屏蔽词：ToShieldWordController /system/shield/shieldWord
 * 系统消息：NewsSystemInfoController /system/news/systemInfo（群发广播，保存即全员可见）
 */

/** 屏蔽词行（ToShieldWordVo） */
export interface ShieldWordRow {
  id?: number;
  keyword?: string;
  /** 类型（1：文章，2：评论） */
  type?: number;
  weight?: number;
  /** 状态（0：正常，1：已失效） */
  state?: number;
  createTime?: string;
}

/** 屏蔽词类型（1：文章，2：评论） */
export const SHIELD_TYPE_MAP: Record<number, { label: string; type: 'info' | 'success' }> = {
  1: { label: '文章', type: 'info' },
  2: { label: '评论', type: 'success' }
};

export const SHIELD_STATE_MAP: Record<number, { label: string; type: 'success' | 'default' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已失效', type: 'default' }
};

/** 系统消息行（NewsSystemInfoVo） */
export interface SystemMessageRow {
  id?: number;
  newsTitle?: string;
  newsContent?: string;
  isDetails?: number;
  /** 发送方式（1：群发，2：指定） */
  isMassAir?: number;
  /** 状态（0：生效，1：已失效） */
  state?: number;
  createTime?: string;
}

export const MESSAGE_STATE_MAP: Record<number, { label: string; type: 'success' | 'default' }> = {
  0: { label: '生效中', type: 'success' },
  1: { label: '已失效', type: 'default' }
};

export const useSystemAdminApi = () => {
  const api = useApi();

  return {
    // ---------- 屏蔽词 ----------
    /** 屏蔽词分页：POST /system/shield/shieldWord/list */
    getShieldWordList: (params: Record<string, any>) =>
      api.postPage<ShieldWordRow>('/system/shield/shieldWord/list', params),
    /** 屏蔽词详情：GET /system/shield/shieldWord/{id} */
    getShieldWord: (id: number) => api.get<ShieldWordRow>(`/system/shield/shieldWord/${id}`),
    /** 新增屏蔽词：POST /system/shield/shieldWord */
    addShieldWord: (data: Record<string, any>) =>
      api.post<void>('/system/shield/shieldWord', data),
    /** 修改屏蔽词：PUT /system/shield/shieldWord */
    editShieldWord: (data: Record<string, any>) =>
      api.put<void>('/system/shield/shieldWord', data),
    /** 删除屏蔽词：DELETE /system/shield/shieldWord/{id1,id2} */
    deleteShieldWords: (ids: Array<string | number>) =>
      api.request<void>(`/system/shield/shieldWord/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 系统消息 ----------
    /** 系统消息分页：POST /system/news/systemInfo/list */
    getSystemMessageList: (params: Record<string, any>) =>
      api.postPage<SystemMessageRow>('/system/news/systemInfo/list', params),
    /** 系统消息详情：GET /system/news/systemInfo/{id} */
    getSystemMessage: (id: number) => api.get<SystemMessageRow>(`/system/news/systemInfo/${id}`),
    /** 新增系统消息（群发，保存即全员可见）：POST /system/news/systemInfo */
    addSystemMessage: (data: Record<string, any>) =>
      api.post<void>('/system/news/systemInfo', data),
    /** 修改系统消息：PUT /system/news/systemInfo */
    editSystemMessage: (data: Record<string, any>) =>
      api.put<void>('/system/news/systemInfo', data),
    /** 失效/恢复：PUT /system/news/systemInfo/state */
    updateMessageState: (id: number, state: number) =>
      api.put<void>('/system/news/systemInfo/state', { id, state }),
    /** 删除系统消息：DELETE /system/news/systemInfo/{id1,id2} */
    deleteSystemMessages: (ids: Array<string | number>) =>
      api.request<void>(`/system/news/systemInfo/${ids.join(',')}`, { method: 'DELETE' })
  };
};
