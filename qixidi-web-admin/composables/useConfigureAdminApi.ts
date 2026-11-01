/**
 * 前台配置管理 API（导航 ToNavigationController / 侧边栏 ToSidebarController / 工具 ToToolInfoController）
 */

/** 导航栏行（ToNavigationVo） */
export interface NavigationRow {
  id: number;
  /** 父级 id（顶级为 null/0） */
  parentId?: number | null;
  navigationName?: string;
  navigationIcon?: string;
  iconColor?: string;
  route?: string;
  order?: number;
  /** 是否有下拉（0 没有 / 1 有） */
  isList?: number;
  /** 可见范围（0 公开 / 1 普通用户 / 2 vip 用户） */
  jurisdiction?: number;
  /** 0 有效 / 1 失效 */
  status?: number;
  /** 1 主页路由 / 2 用户页面路由 */
  type?: number;
}

/** 侧边栏行（ToSidebarVo，字段语义同导航） */
export interface SidebarRow {
  id: number;
  parentId?: number | null;
  sidebarName?: string;
  sidebarIcon?: string;
  iconColor?: string;
  route?: string;
  order?: number;
  isList?: number;
  jurisdiction?: number;
  status?: number;
  type?: number;
}

/** 工具行（ToToolInfoVo） */
export interface ToolRow {
  id: number;
  parentId?: number | null;
  toolName?: string;
  describe?: string;
  icon?: string;
  /** 1 父级 / 2 子级 */
  isParent?: number;
  toolUrl?: string;
  order?: number;
  type?: number;
  /** 0 正常 / 1 已失效 */
  state?: number;
  createBy?: string;
  createTime?: string;
}

/** 配置状态（导航/侧边栏共用：0 有效 / 1 失效） */
export const CONFIG_STATUS_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '有效', type: 'success' },
  1: { label: '失效', type: 'error' }
};

/** 工具状态（0 正常 / 1 已失效） */
export const TOOL_STATE_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已失效', type: 'error' }
};

/** 可见范围（导航/侧边栏共用） */
export const CONFIG_JURISDICTION_MAP: Record<number, { label: string; type: 'success' | 'info' | 'warning' }> = {
  0: { label: '公开', type: 'success' },
  1: { label: '普通用户', type: 'info' },
  2: { label: 'VIP', type: 'warning' }
};

export const useConfigureAdminApi = () => {
  const api = useApi();

  return {
    // ---------- 导航栏（/system/configure/navigation） ----------
    getNavigationList: (params: Record<string, any>) =>
      api.postPage<NavigationRow>('/system/configure/navigation/list', params),

    getNavigation: (id: number | string) =>
      api.get<NavigationRow>(`/system/configure/navigation/${id}`),

    addNavigation: (data: Partial<NavigationRow>) =>
      api.post<void>('/system/configure/navigation', data),

    editNavigation: (data: Partial<NavigationRow>) =>
      api.put<void>('/system/configure/navigation', data),

    deleteNavigations: (ids: (number | string)[]) =>
      api.request<void>(`/system/configure/navigation/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 侧边栏（/system/configure/sidebar） ----------
    getSidebarList: (params: Record<string, any>) =>
      api.postPage<SidebarRow>('/system/configure/sidebar/list', params),

    getSidebar: (id: number | string) => api.get<SidebarRow>(`/system/configure/sidebar/${id}`),

    addSidebar: (data: Partial<SidebarRow>) => api.post<void>('/system/configure/sidebar', data),

    editSidebar: (data: Partial<SidebarRow>) => api.put<void>('/system/configure/sidebar', data),

    deleteSidebars: (ids: (number | string)[]) =>
      api.request<void>(`/system/configure/sidebar/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 工具（/system/toolInfo） ----------
    getToolList: (params: Record<string, any>) =>
      api.postPage<ToolRow>('/system/toolInfo/list', params),

    getTool: (id: number | string) => api.get<ToolRow>(`/system/toolInfo/${id}`),

    addTool: (data: Partial<ToolRow>) => api.post<void>('/system/toolInfo', data),

    editTool: (data: Partial<ToolRow>) => api.put<void>('/system/toolInfo', data),

    deleteTools: (ids: (number | string)[]) =>
      api.request<void>(`/system/toolInfo/${ids.join(',')}`, { method: 'DELETE' })
  };
};
