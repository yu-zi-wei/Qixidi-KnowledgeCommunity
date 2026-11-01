/**
 * 私信管理 API（PrivateUserController /system/private/user + PrivateNewsInfoController /system/private/newsInfo）
 * 后台全站视角（adminQueryPageList），双方昵称/头像为 join 展示字段
 */

/** 私信会话行（PrivateUserVo；双方昵称/头像为 join 产物） */
export interface PrivateSessionRow {
  id: number;
  /** 发起方 uuid */
  uid?: string;
  uidName?: string;
  uidAvatar?: string;
  /** 接收方 uuid */
  targetUid?: string;
  targetName?: string;
  targetAvatar?: string;
  targetOccupation?: string;
  /** 最后一条消息 */
  lastNews?: string;
  updateTime?: string;
  createTime?: string;
}

/** 私信消息行（PrivateNewsInfoVo；双方昵称/头像为 join 产物） */
export interface PrivateMessageRow {
  id: number;
  /** 发送方 uuid */
  uid?: string;
  nickname?: string;
  userAvatar?: string;
  /** 接收方 uuid */
  replyTargetUid?: string;
  targetName?: string;
  targetAvatar?: string;
  newsComment?: string;
  /** 是否已读（1 未读 / 2 已读） */
  beenRead?: number;
  createTime?: string;
}

/** 消息已读状态 */
export const PRIVATE_READ_MAP: Record<number, { label: string; type: 'warning' | 'success' }> = {
  1: { label: '未读', type: 'warning' },
  2: { label: '已读', type: 'success' }
};

export const usePrivateAdminApi = () => {
  const api = useApi();

  return {
    // ---------- 会话（/system/private/user） ----------
    getPrivateSessionList: (params: Record<string, any>) =>
      api.postPage<PrivateSessionRow>('/system/private/user/list', params),

    deletePrivateSessions: (ids: (number | string)[]) =>
      api.request<void>(`/system/private/user/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 消息（/system/private/newsInfo） ----------
    getPrivateMessageList: (params: Record<string, any>) =>
      api.postPage<PrivateMessageRow>('/system/private/newsInfo/list', params),

    deletePrivateMessages: (ids: (number | string)[]) =>
      api.request<void>(`/system/private/newsInfo/${ids.join(',')}`, { method: 'DELETE' })
  };
};
