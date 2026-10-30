/**
 * 后台账号管理 API（SysUserController /system/user，sys_user 系统用户）
 * 与前台用户（useUserAdminApi → /system/tripartite/user）是两套数据，勿混用
 */

/** 后台账号行（SysUser 列表字段子集；列表接口 join 出 roles） */
export interface AccountRow {
  userId: number;
  userName: string;
  nickName?: string;
  phonenumber?: string;
  email?: string;
  /** 0 男 / 1 女 / 2 未知 */
  sex?: string;
  /** 0 正常 / 1 停用 */
  status: string;
  remark?: string;
  createTime?: string;
  roles?: Array<{ roleId: number; roleName: string }>;
}

/** GET /system/user/{userId} 响应：user + 全量角色 + 已勾选角色 */
export interface AccountDetail {
  user: AccountRow;
  roles: Array<{ roleId: number; roleName: string }>;
  roleIds: number[];
}

export const ACCOUNT_STATUS_MAP: Record<string, { label: string; type: 'success' | 'error' }> = {
  '0': { label: '正常', type: 'success' },
  '1': { label: '停用', type: 'error' }
};

export const useAccountAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/user/list */
    getAccounts: (params: Record<string, any>) => api.postPage<AccountRow>('/system/user/list', params),

    /** 详情/角色下拉二合一：GET /system/user/{userId}（不传 userId 只返回全量角色，用于新增下拉） */
    getAccount: (userId?: number) => api.get<AccountDetail>(`/system/user/${userId ?? ''}`),

    /** 新增：POST /system/user（password 必填，后端 BCrypt 加密） */
    addAccount: (data: Partial<AccountRow> & { password?: string; roleIds?: number[] }) =>
      api.post<void>('/system/user', data),

    /** 修改：PUT /system/user（roleIds 全删全插） */
    editAccount: (data: Partial<AccountRow> & { roleIds?: number[] }) =>
      api.put<void>('/system/user', data),

    /** 重置密码：PUT /system/user/resetPwd */
    resetAccountPwd: (userId: number, password: string) =>
      api.put<void>('/system/user/resetPwd', { userId, password }),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/user/{userIds} */
    removeAccounts: (userIds: Array<number | string>) =>
      api.request<void>(`/system/user/${userIds.join(',')}`, { method: 'DELETE' })
  };
};
