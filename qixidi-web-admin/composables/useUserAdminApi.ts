/**
 * 前台用户管理 API（TripartiteUserController，/system/tripartite/user）
 * TripartiteUserBo 无 EditGroup 必填约束 → 冻结/解冻只传 {uuid, state}
 */

/** 用户行（TripartiteUserVo 列表用到的字段子集） */
export interface UserRow {
  uuid: string;
  username?: string;
  nickname?: string;
  avatar?: string;
  phone?: string;
  email?: string;
  gender?: string;
  blog?: string;
  company?: string;
  location?: string;
  occupation?: string;
  introduce?: string;
  source?: string;
  userType?: string;
  state?: number;
  loginIp?: string;
  grade?: string;
  empirical?: number;
  createTime?: string;
  fabulousCount?: number;
  collectionCount?: number;
  followCount?: number;
  fansFollowCount?: number;
  commentCount?: number;
}

/** 用户 state（VO Excel 注解）：0 正常 / 1 冻结 / 2 已删除 */
export const USER_STATE_MAP: Record<number, { label: string; type: 'success' | 'warning' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已冻结', type: 'warning' },
  2: { label: '已删除', type: 'error' }
};

export const useUserAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：GET /system/tripartite/user/list */
    getUsers: (params: Record<string, any>) => api.getPage<UserRow>('/system/tripartite/user/list', params),

    /** 详情：GET /system/tripartite/user/{uuid} */
    getUser: (uuid: string) => api.get<UserRow>(`/system/tripartite/user/${uuid}`),

    /** 更新（冻结/解冻）：PUT /system/tripartite/user */
    updateUser: (data: Partial<UserRow>) => api.put<void>('/system/tripartite/user', data)
  };
};
