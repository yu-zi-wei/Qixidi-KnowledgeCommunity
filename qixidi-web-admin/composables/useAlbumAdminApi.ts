/**
 * 时光相册管理 API（AlbumAdminController，/system/album）
 */

/** 相册图片行（AlbumVo；nickname/avatar 为列表填充展示字段） */
export interface AlbumRow {
  id: number;
  /** 图片归属用户 uuid */
  uid?: string;
  nickname?: string;
  avatar?: string;
  /** 来源内容 id（小记 id / 文章 id） */
  sourceId?: number;
  /** 来源内容时间 */
  sourceTime?: string;
  /** 来源：1 时光小记 / 2 文章 */
  source?: number;
  url?: string;
  createTime?: string;
}

/** 来源 */
export const ALBUM_SOURCE_MAP: Record<number, { label: string; type: 'warning' | 'info' }> = {
  1: { label: '时光小记', type: 'warning' },
  2: { label: '文章', type: 'info' }
};

export const useAlbumAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/album/list（uid/来源多选/月份 yyyy-MM 或 yyyy） */
    getAlbumList: (params: Record<string, any>) =>
      api.postPage<AlbumRow>('/system/album/list', params),

    /** 批量删除（移出相册；来源内容再编辑/回填会重新解析入库）：DELETE /system/album/{ids} */
    deleteAlbums: (ids: (number | string)[]) =>
      api.request<void>(`/system/album/${ids.join(',')}`, { method: 'DELETE' }),

    /** 存量回填（按小记全删重插，可重复触发）：POST /system/album/backfill */
    backfillAlbum: () => api.post<void>('/system/album/backfill')
  };
};
