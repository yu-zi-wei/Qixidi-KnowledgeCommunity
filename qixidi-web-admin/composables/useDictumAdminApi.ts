/**
 * 随笔管理 API（DictumInfo/Group/Album/CommentAdminController，/system/dictum/*）
 * 列表 + 删除最小闭环；新增/编辑走前台用户端，后台不提供
 */

/** 随笔行（DictumInfoVo 列表 join 字段：groupName/albumName/nickname/avatar 为 join 产物） */
export interface DictumRow {
  id: number;
  uid?: string;
  /** 内容纯文本（列表 SQL 返回 content；contentMd 仅详情接口） */
  content?: string;
  contentMd?: string;
  groupId?: number;
  albumId?: number;
  groupName?: string;
  albumName?: string;
  label?: string;
  labelList?: string[];
  author?: string;
  worksName?: string;
  /** 可见性：1 公开 / 2 私有 / 3 关注可看 */
  dictumState?: number;
  /** 0 正常 / 2 已删除 */
  state?: number;
  nickname?: string;
  avatar?: string;
  helpSum?: number;
  commentSum?: number;
  createTime?: string;
}

/** 随笔分组行（DictumGroupVo） */
export interface DictumGroupRow {
  id: number;
  name?: string;
  cover?: string;
  briefIntroduction?: string;
  state?: number;
  /** 收录数（join 产物） */
  employSum?: number;
  nickname?: string;
  avatar?: string;
  createTime?: string;
}

/** 随笔相册行（DictumAlbumVo） */
export interface DictumAlbumRow {
  id: number;
  uid?: string;
  name?: string;
  cover?: string;
  briefIntroduction?: string;
  /** 可见性：1 公开 / 2 私有 / 3 关注可看 */
  albumState?: number;
  recommendRate?: number;
  state?: number;
  employSum?: number;
  helpSum?: number;
  followSum?: number;
  createTime?: string;
}

/** 随笔评论行（DictumCommentVo；nickname/avatar/targetNickname/dictumContent 为批量填充展示字段） */
export interface DictumCommentRow {
  id: number;
  /** 所在随笔 id */
  dictumId: number;
  /** 所在随笔内容（列表批量填充展示字段） */
  dictumContent?: string;
  /** 随笔作者 uid */
  uid?: string;
  parentId?: number;
  /** 评论等级（1 一级 / 2 二级 / 3 三级及以下） */
  commentGrade?: number;
  targetId?: number;
  targetUid?: string;
  commentUid?: string;
  content: string;
  /** 评论类型（1 名言 / 2 评论） */
  type?: number;
  /** 评论状态（0 正常 / 1 已删除） */
  status?: number;
  createTime?: string;
  /** 评论人（批量填充） */
  nickname?: string;
  avatar?: string;
  /** 回复对象（批量填充） */
  targetNickname?: string;
}

/** 可见性（dictumState/albumState 共用） */
export const DICTUM_VISIBLE_MAP: Record<number, { label: string; type: 'success' | 'info' | 'warning' }> = {
  1: { label: '公开', type: 'success' },
  2: { label: '私有', type: 'info' },
  3: { label: '关注可看', type: 'warning' }
};

/** 数据状态（state） */
export const DICTUM_STATE_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  2: { label: '已删除', type: 'error' }
};

/** 随笔评论 status：0 正常 / 1 已删除（实体注释） */
export const DICTUM_COMMENT_STATUS_MAP: Record<number, { label: string; type: 'success' | 'error' }> = {
  0: { label: '正常', type: 'success' },
  1: { label: '已删除', type: 'error' }
};

export const useDictumAdminApi = () => {
  const api = useApi();

  return {
    // ---------- 随笔（/system/dictum/info） ----------
    /** 分页列表：POST /system/dictum/info/list */
    getDictumList: (params: Record<string, any>) =>
      api.postPage<DictumRow>('/system/dictum/info/list', params),

    /** 批量删除（路径参数逗号分隔）：DELETE /system/dictum/info/{ids} */
    deleteDictums: (ids: (number | string)[]) =>
      api.request<void>(`/system/dictum/info/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 随笔分组（/system/dictum/group） ----------
    /** 分页列表：POST /system/dictum/group/list */
    getDictumGroupList: (params: Record<string, any>) =>
      api.postPage<DictumGroupRow>('/system/dictum/group/list', params),

    /** 批量删除：DELETE /system/dictum/group/{ids} */
    deleteDictumGroups: (ids: (number | string)[]) =>
      api.request<void>(`/system/dictum/group/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 随笔相册（/system/dictum/album） ----------
    /** 分页列表：POST /system/dictum/album/list */
    getDictumAlbumList: (params: Record<string, any>) =>
      api.postPage<DictumAlbumRow>('/system/dictum/album/list', params),

    /** 批量删除：DELETE /system/dictum/album/{ids} */
    deleteDictumAlbums: (ids: (number | string)[]) =>
      api.request<void>(`/system/dictum/album/${ids.join(',')}`, { method: 'DELETE' }),

    // ---------- 随笔评论（/system/dictum/comment） ----------
    /** 评论分页列表：POST /system/dictum/comment/list */
    getDictumCommentList: (params: Record<string, any>) =>
      api.postPage<DictumCommentRow>('/system/dictum/comment/list', params),

    /** 评论批量删除（路径参数逗号分隔）：DELETE /system/dictum/comment/{ids} */
    deleteDictumComments: (ids: (number | string)[]) =>
      api.request<void>(`/system/dictum/comment/${ids.join(',')}`, { method: 'DELETE' })
  };
};
