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
  /** 是否有详情（1：无，2：有） */
  isDetails?: number;
  /** 发送方式（1：群发，2：指定人发） */
  isMassAir?: number;
  /** 指定人发的接收人 uuid（群发为 null） */
  uid?: string;
  /** 接收人昵称/头像（uid join b_user_main，群发为 null） */
  recipientName?: string;
  recipientAvatar?: string;
  /** 消息类型（1：系统，2：用户；后台发送一律 2——前台消费链路只认 2） */
  type?: number;
  /** 失效时间 */
  lapseTime?: string;
  /** 状态（0：生效，1：已失效） */
  state?: number;
  createTime?: string;
}

export const MESSAGE_STATE_MAP: Record<number, { label: string; type: 'success' | 'default' }> = {
  0: { label: '生效中', type: 'success' },
  1: { label: '已失效', type: 'default' }
};

/** 系统消息类型（1：系统，2：用户） */
export const MESSAGE_TYPE_MAP: Record<number, { label: string; type: 'info' | 'success' }> = {
  1: { label: '系统', type: 'info' },
  2: { label: '用户', type: 'success' }
};

/** 发送方式（1：群发，2：指定人发） */
export const MESSAGE_MASS_MAP: Record<number, { label: string; type: 'warning' | 'info' }> = {
  1: { label: '群发', type: 'warning' },
  2: { label: '指定人', type: 'info' }
};

/** 用户消息记录行（NewsUserRecordVo，b_news_user_record：用户间互动产生的消息） */
export interface UserNewsRow {
  id?: number;
  /** 消息类型（1：评论，2：点赞，3：关注，6：随笔评论，7：小记评论） */
  type?: number;
  content?: string;
  /** 已读状态（0：未读，1：已读） */
  beenRead?: number;
  createTime?: string;
  /** 发送者昵称/头像（target_uid join b_user_main） */
  senderName?: string;
  senderAvatar?: string;
  /** 发送者 uuid（target_uid，点击打开用户详情用） */
  senderUid?: string;
  /** 接收者昵称/头像（uid join b_user_main） */
  recipientName?: string;
  recipientAvatar?: string;
  /** 接收者 uuid（uid，点击打开用户详情用） */
  recipientUid?: string;
}

export const USER_NEWS_TYPE_MAP: Record<
  number,
  { label: string; type: 'info' | 'success' | 'warning' | 'error' | 'default' }
> = {
  1: { label: '评论', type: 'info' },
  2: { label: '点赞', type: 'success' },
  3: { label: '关注', type: 'warning' },
  6: { label: '随笔评论', type: 'error' },
  7: { label: '小记评论', type: 'default' }
};

export const USER_NEWS_READ_MAP: Record<number, { label: string; type: 'info' | 'default' }> = {
  0: { label: '未读', type: 'info' },
  1: { label: '已读', type: 'default' }
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
    /** 新增系统消息（isMassAir 可选默认群发，指定人发须带 uid）：POST /system/news/systemInfo */
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
      api.request<void>(`/system/news/systemInfo/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 用户消息（b_news_user_record） ----------
    /** 用户消息分页（types 多选 + username 模糊）：POST /system/news/record/list */
    getUserNewsList: (params: Record<string, any>) =>
      api.postPage<UserNewsRow>('/system/news/record/list', params),
    /** 删除用户消息：DELETE /system/news/record/{id1,id2} */
    deleteUserNews: (ids: Array<string | number>) =>
      api.request<void>(`/system/news/record/${ids.join(',')}`, { method: 'DELETE' })
  };
};
