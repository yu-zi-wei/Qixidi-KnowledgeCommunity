/**
 * 私信管理 API（PrivateNewsInfoController /system/private/newsInfo）
 * 后台全站视角，双方昵称/头像为 join 展示字段
 */

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
    // ---------- 消息（/system/private/newsInfo） ----------
    getPrivateMessageList: (params: Record<string, any>) =>
      api.postPage<PrivateMessageRow>('/system/private/newsInfo/list', params),

    deletePrivateMessages: (ids: (number | string)[]) =>
      api.request<void>(`/system/private/newsInfo/${ids.join(',')}`, { method: 'DELETE' })
  };
};
