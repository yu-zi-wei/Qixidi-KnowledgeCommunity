/**
 * 监控审计 API（qixidi-system monitor 包）
 * 操作日志 /monitor/operlog + 登录日志 /monitor/logininfor，均为只读列表 + 删除/清空
 */

/** 操作日志行（SysOperLog 全量字段，无单查接口——详情直接用行数据） */
export interface OperLogRow {
  operId: number;
  title?: string;
  /** 业务类型（BusinessType.ordinal）：0 其它 1 新增 2 修改 3 删除 4 授权 5 导出 6 导入 7 强退 8 生成代码 9 清空 */
  businessType?: number;
  method?: string;
  requestMethod?: string;
  operatorType?: number;
  operName?: string;
  deptName?: string;
  operUrl?: string;
  operIp?: string;
  operLocation?: string;
  operParam?: string;
  jsonResult?: string;
  /** 0 正常 1 异常 */
  status?: number;
  errorMsg?: string;
  operTime?: string;
}

/** 业务类型（BusinessType.ordinal 顺序） */
export const OPER_BIZ_TYPE_MAP: Record<number, { label: string; type: 'default' | 'info' | 'warning' | 'error' | 'success' }> = {
  0: { label: '其它', type: 'default' },
  1: { label: '新增', type: 'success' },
  2: { label: '修改', type: 'info' },
  3: { label: '删除', type: 'error' },
  4: { label: '授权', type: 'warning' },
  5: { label: '导出', type: 'info' },
  6: { label: '导入', type: 'info' },
  7: { label: '强退', type: 'warning' },
  8: { label: '生成代码', type: 'default' },
  9: { label: '清空', type: 'error' }
};

/** 操作状态：0 正常 / 1 异常 */
export const OPER_STATUS_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '异常', type: 'error' }
};

/** 登录日志行（SysLogininfor 全量字段） */
export interface LogininforRow {
  infoId: number;
  userName?: string;
  /** 登录状态（String）：0 成功 1 失败 */
  status?: string;
  ipaddr?: string;
  loginLocation?: string;
  browser?: string;
  os?: string;
  msg?: string;
  loginTime?: string;
}

/** 登录状态（后端 status 是 String 类型，搜索传 '0'/'1'） */
export const LOGIN_STATUS_MAP: Record<string, { label: string; type: 'success' | 'error' }> = {
  '0': { label: '成功', type: 'success' },
  '1': { label: '失败', type: 'error' }
};

export const useMonitorAdminApi = () => {
  const api = useApi();

  return {
    /** 操作日志分页：POST /monitor/operlog/list（默认 oper_id desc） */
    getOperLogs: (params: Record<string, any>) => api.postPage<OperLogRow>('/monitor/operlog/list', params),

    /** 批量删除操作日志：DELETE /monitor/operlog/{operIds} */
    deleteOperLogs: (ids: (number | string)[]) =>
      api.request<void>(`/monitor/operlog/${ids.join(',')}`, { method: 'DELETE' }),

    /** 清空操作日志：DELETE /monitor/operlog/clean */
    cleanOperLogs: () => api.request<void>('/monitor/operlog/clean', { method: 'DELETE' }),

    /** 登录日志分页：POST /monitor/logininfor/list（默认 info_id desc） */
    getLogininfors: (params: Record<string, any>) => api.postPage<LogininforRow>('/monitor/logininfor/list', params),

    /** 批量删除登录日志：DELETE /monitor/logininfor/{infoIds} */
    deleteLogininfors: (ids: (number | string)[]) =>
      api.request<void>(`/monitor/logininfor/${ids.join(',')}`, { method: 'DELETE' }),

    /** 清空登录日志：DELETE /monitor/logininfor/clean */
    cleanLogininfors: () => api.request<void>('/monitor/logininfor/clean', { method: 'DELETE' })
  };
};
