/**
 * 文章管理 API（ArticleInformationController，/system/article/information）
 */

/** 文章行（ArticleInformationVo 列表用到的字段子集） */
export interface ArticleRow {
  id: number;
  userId?: string;
  articleTitle: string;
  articleCover?: string;
  articleAbstract?: string;
  articleContent?: string;
  theme?: string;
  type?: number;
  reprintUrl?: string;
  state?: number;
  updateId?: string;
  specialId?: number;
  groupingId?: number;
  groupingName?: string;
  labelId?: string;
  labelNameList?: string[];
  likeTimes?: number;
  commentTimes?: number;
  numberTimes?: number;
  collectionTimes?: number;
  isPublic?: number;
  auditState?: number;
  auditTime?: string;
  createTime?: string;
  updateTime?: string;
  nickname?: string;
  username?: string;
  avatar?: string;
  occupation?: string;
}

/** 文章审核状态（ArticleAuditStateEnums） */
export const AUDIT_STATE_MAP: Record<number, { label: string; type: 'warning' | 'success' | 'error' | 'default' }> = {
  1: { label: '审核中', type: 'warning' },
  2: { label: '已通过', type: 'success' },
  3: { label: '未通过', type: 'error' },
  4: { label: '草稿', type: 'default' }
};

/** 分类行（LabelGroupingInfoVo 字段子集） */
export interface GroupingRow {
  id: number;
  groupingName: string;
  state?: number;
}

export const useArticleAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：GET /system/article/information/list */
    getArticles: (params: Record<string, any>) =>
      api.postPage<ArticleRow>('/system/article/information/list', params),

    /** 分类全量（pageSize=100 拉取，供搜索下拉）：GET /system/groupingInfo/list */
    getGroupings: () =>
      api.postPage<GroupingRow>('/system/groupingInfo/list', { pageNum: 1, pageSize: 100 }),

    /** 详情：GET /system/article/information/{id} */
    getArticle: (id: number | string) => api.get<ArticleRow>(`/system/article/information/${id}`),

    /** 审核（终审：auditState 2 通过 / 3 拒绝，支持批量）：PUT /system/article/information/audit */
    auditArticles: (ids: Array<number | string>, auditState: number) =>
      api.put<void>('/system/article/information/audit', { ids, auditState }),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/article/information/{id1,id2} */
    removeArticles: (ids: Array<number | string>) =>
      api.request<void>(`/system/article/information/${ids.join(',')}`, { method: 'DELETE' })
  };
};
