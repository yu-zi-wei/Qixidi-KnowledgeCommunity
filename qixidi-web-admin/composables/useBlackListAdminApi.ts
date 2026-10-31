/**
 * IP 黑名单 API（SysBlackListAdminController，/system/black）
 * 防刷限流拉黑记录；增删后后端重建内存黑名单即时生效
 */

/** 黑名单行（sys_black_list 全量字段，无单查接口——详情用行数据） */
export interface BlackListRow {
  id: number;
  ip?: string;
  homePlace?: string;
  uid?: string;
  /** 拉黑原因：自动拉黑存 URL 计数 JSON（如 {"\/api\/xx":12}），手动加黑存备注文本 */
  detail?: string;
  createTime?: string;
}

export const useBlackListAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/black/list（ip like / uid eq / homePlace like，id desc） */
    getBlackList: (params: Record<string, any>) => api.postPage<BlackListRow>('/system/black/list', params),

    /** 手动加黑：POST /system/black（ip/uid 至少一项） */
    addBlack: (data: { ip?: string; uid?: string; detail?: string }) => api.post<void>('/system/black', data),

    /** 批量移出黑名单：DELETE /system/black/{ids} */
    deleteBlack: (ids: (number | string)[]) =>
      api.request<void>(`/system/black/${ids.join(',')}`, { method: 'DELETE' })
  };
};
