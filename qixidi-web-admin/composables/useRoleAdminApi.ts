/**
 * 后台角色管理 API（SysRoleController /system/role + SysPermController /system/perm）
 * 角色挂权限码（sys_role_perm），新增/编辑时 perms 全量提交
 */

/** 角色行（SysRole） */
export interface RoleRow {
  roleId: number;
  roleName: string;
  roleKey: string;
  roleSort: number;
  /** 0 正常 / 1 停用 */
  status: string;
  remark?: string;
  createTime?: string;
  /** 已分配权限码（详情接口回显 / 表单提交） */
  perms?: string[];
}

/** 权限分组（GET /system/perm/list，后端启动扫描 @SaCheckPermission 产出） */
export interface PermGroup {
  module: string;
  perms: string[];
}

export const ROLE_STATUS_MAP: Record<string, { label: string; type: 'success' | 'error' }> = {
  '0': { label: '正常', type: 'success' },
  '1': { label: '停用', type: 'error' }
};

export const useRoleAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/role/list */
    getRoles: (params: Record<string, any>) => api.postPage<RoleRow>('/system/role/list', params),

    /** 详情（含 perms 回显）：GET /system/role/{roleId} */
    getRole: (roleId: number) => api.get<RoleRow>(`/system/role/${roleId}`),

    /** 新增：POST /system/role */
    addRole: (data: Partial<RoleRow>) => api.post<void>('/system/role', data),

    /** 修改（perms 全删全插）：PUT /system/role */
    editRole: (data: Partial<RoleRow>) => api.put<void>('/system/role', data),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/role/{roleIds} */
    removeRoles: (roleIds: Array<number | string>) =>
      api.request<void>(`/system/role/${roleIds.join(',')}`, { method: 'DELETE' }),

    /** 权限码分组字典：GET /system/perm/list */
    getPermGroups: () => api.get<PermGroup[]>('/system/perm/list'),

    /** 角色下拉：GET /system/role/optionselect */
    getRoleOptions: () => api.get<RoleRow[]>('/system/role/optionselect')
  };
};
