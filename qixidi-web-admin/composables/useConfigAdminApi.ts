/**
 * 系统设置 API（SysConfigController /system/config + cacheRefreshController /system/cache/refresh）
 * 配置增改后端已自动按 key 更新 Redis 缓存；refreshCache 为全量重建兜底
 */

/** 配置行（SysConfig 字段子集） */
export interface ConfigRow {
  configId: number;
  configName?: string;
  configKey?: string;
  configValue?: string;
  /** 系统内置（Y 是 / N 否） */
  configType?: string;
  remark?: string;
  updateTime?: string;
}

/** 内置标记：Y 内置 / N 外置 */
export const CONFIG_TYPE_MAP: Record<string, { label: string; type: 'warning' | 'default' }> = {
  Y: { label: '内置', type: 'warning' },
  N: { label: '外置', type: 'default' }
};

/** 站点缓存刷新项（cacheRefreshController，无权限码） */
export const SITE_CACHE_KINDS = [
  { key: 'navigation', label: '导航栏配置' },
  { key: 'sidebar', label: '侧边栏配置' },
  { key: 'blocking-words', label: '屏蔽词库' }
] as const;

export const useConfigAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/config/list（configName/configKey like，configType eq） */
    getConfigs: (params: Record<string, any>) => api.postPage<ConfigRow>('/system/config/list', params),

    /** 详情：GET /system/config/{configId} */
    getConfig: (configId: number | string) => api.get<ConfigRow>(`/system/config/${configId}`),

    /** 新增：POST /system/config（后端校验 configKey 唯一） */
    addConfig: (data: Partial<ConfigRow>) => api.post<void>('/system/config', data),

    /** 编辑：PUT /system/config（configName/Key/Value 必填） */
    updateConfig: (data: Partial<ConfigRow>) => api.put<void>('/system/config', data),

    /** 批量删除：DELETE /system/config/{configIds} */
    deleteConfigs: (ids: (number | string)[]) =>
      api.request<void>(`/system/config/${ids.join(',')}`, { method: 'DELETE' }),

    /** 全量重建参数缓存：DELETE /system/config/refreshCache */
    refreshConfigCache: () => api.request<void>('/system/config/refreshCache', { method: 'DELETE' }),

    /** 刷新站点缓存：GET /system/cache/refresh/{kind} */
    refreshSiteCache: (kind: string) => api.get<void>(`/system/cache/refresh/${kind}`)
  };
};
