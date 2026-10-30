/**
 * 站点统计接口（qixidi-service StatDataInfoController，字段以 StatDataInfoVo/StatReturnDataVo 为准）
 */

/** GET /system/stat/data/info —— 单日快照（b_stat_data_info 最新一条 + 标签实时计数） */
export interface StatDataInfo {
  statTime: string;
  userCount: number;
  onlineUserCount: number;
  articleCount: number;
  specialCount: number;
  favoritesCount: number;
  labelGroupCount: number;
  labelCount: number;
  dictumGroupCount: number;
  dictumCount: number;
  dictumAlbumCount: number;
  timeNotesCount: number;
}

/** 趋势/标签通用条目（StatReturnDataVo） */
export interface StatReturnItem {
  /** 指标中文名（趋势）或 标签/分类名 */
  name: string;
  /** 标签数据专用："文章数" | "关注数" */
  twoName?: string;
  /** 趋势数据的月份 */
  time?: string;
  data: number;
}

export const useStatApi = () => {
  const api = useApi();

  return {
    /** 综合统计快照 */
    getStatInfo: () => api.post<StatDataInfo>('/system/stat/data/info'),
    /** 近 6 个月月度序列（SQL ORDER BY stat_time DESC LIMIT 6） */
    getTheStat: () => api.post<StatReturnItem[]>('/system/stat/data/the/info'),
    /** 标签/分类维度：labelGroup=分类文章数，label=标签的文章数/关注数 */
    getLabelStat: () =>
      api.post<{ labelGroup: StatReturnItem[]; label: StatReturnItem[] }>('/system/stat/data/label/data')
  };
};
