import type { ReadingEssaysInfo, ReadingEssaysGroup, ReadingEssaysAlbum, ReadingEssaysAuthor, ReadingEssaysLabel } from '~/types'

export const useReadingEssaysApi = () => {
  const api = useApi()

  /**
   * 获取随笔列表
   * 接口：/white/dictum/info/list
   */
  const getReadingEssaysList = async (params: {
    pageNum?: number
    pageSize?: number
    groupId?: number
    albumId?: number
    author?: string
    label?: string
  }) => {
    return await api.getPage<{ total: number; rows: ReadingEssaysInfo[] }>(
      '/white/dictum/info/list',
      params
    )
  }

  /**
   * 获取随笔分类列表
   * 接口：/white/dictum/group/list
   */
  const getReadingEssaysGroups = async (params?: { pageNum?: number; pageSize?: number }) => {
    return await api.getPage<{ total: number; rows: ReadingEssaysGroup[] }>(
      '/white/dictum/group/list',
      params || { pageNum: 1, pageSize: 100 }
    )
  }

  /**
   * 获取推荐专辑（支持分页和搜索）
   * 接口：POST /white/dictum/recommended/album
   * 注意：返回的是 TableDataInfo，不是 R 包装，需要用 $fetch
   */
  const getRecommendedAlbums = async (params?: {
    pageNum?: number
    pageSize?: number
    albumName?: string
  }): Promise<{ total: number; rows: ReadingEssaysAlbum[] }> => {
    const config = useRuntimeConfig()
    const baseURL = config.public.apiBase || '/api'

    const response = await $fetch<{ total: number; rows: ReadingEssaysAlbum[] }>(
      '/white/dictum/recommended/album',
      {
        method: 'POST',
        baseURL,
        body: {
          pageNum: params?.pageNum || 1,
          pageSize: params?.pageSize || 10,
          ...(params?.albumName && { albumName: params.albumName })
        }
      }
    )
    return response || { total: 0, rows: [] }
  }

  /**
   * 获取单个专辑详情
   * 接口：GET /white/dictum/album/{id}
   */
  const getAlbumDetail = async (id: number): Promise<ReadingEssaysAlbum> => {
    return await api.get<ReadingEssaysAlbum>(`/white/dictum/album/${id}`)
  }

  /**
   * 获取热门作者
   * 接口：/white/dictum/popular/authors
   * 返回格式：[{ author: string, count: string }]
   */
  const getPopularAuthors = async (): Promise<ReadingEssaysAuthor[]> => {
    const data = await api.get<ReadingEssaysAuthor[]>('/white/dictum/popular/authors')
    return data || []
  }

  /**
   * 获取热门标签
   * 接口：/white/dictum/popular/label
   * 返回格式：[{ label: string, count: string }]
   */
  const getPopularLabels = async (): Promise<ReadingEssaysLabel[]> => {
    const data = await api.get<ReadingEssaysLabel[]>('/white/dictum/popular/label')
    return data || []
  }

  /**
   * 获取随笔详情（前台接口，用于编辑时获取）
   * 接口：GET /frontDesk/dictum/info/{id}
   */
  const getDictumDetail = async (id: number): Promise<ReadingEssaysInfo> => {
    return await api.get<ReadingEssaysInfo>(`/frontDesk/dictum/info/${id}`)
  }

  /**
   * 获取随笔分类列表（白名单接口）
   * 接口：GET /white/dictum/group/list
   */
  const getDictumGroups = async (params?: { pageNum?: number; pageSize?: number }) => {
    return await api.getPage<{ total: number; rows: ReadingEssaysGroup[] }>(
      '/white/dictum/group/list',
      params || { pageNum: 1, pageSize: 100 }
    )
  }

  /**
   * 获取用户可选的专辑列表
   * 接口：GET /frontDesk/dictum/album/role/list
   */
  const getDictumAlbums = async (params?: { pageNum?: number; pageSize?: number; albumName?: string }) => {
    return await api.getPage<{ total: number; rows: ReadingEssaysAlbum[] }>(
      '/frontDesk/dictum/album/role/list',
      params || { pageNum: 1, pageSize: 100 }
    )
  }

  /**
   * 新增随笔
   * 接口：POST /frontDesk/dictum/info
   */
  const createDictum = async (data: DictumForm): Promise<number> => {
    return await api.post<number>('/frontDesk/dictum/info', data)
  }

  /**
   * 修改随笔
   * 接口：PUT /frontDesk/dictum/info
   */
  const updateDictum = async (data: DictumForm): Promise<void> => {
    return await api.put<void>('/frontDesk/dictum/info', data)
  }

  /**
   * 获取随笔详情（白名单接口）
   * 接口：GET /white/dictum/info/{id}
   */
  const getReadingEssaysDetail = async (id: number): Promise<ReadingEssaysInfo> => {
    return await api.get<ReadingEssaysInfo>(`/white/dictum/info/${id}`)
  }

  return {
    getReadingEssaysList,
    getReadingEssaysGroups,
    getRecommendedAlbums,
    getAlbumDetail,
    getPopularAuthors,
    getPopularLabels,
    getReadingEssaysDetail,
    // 编辑页面使用
    getDictumDetail,
    getDictumGroups,
    getDictumAlbums,
    createDictum,
    updateDictum
  }
}

// 随笔表单数据
export interface DictumForm {
  id?: number
  content: string           // 内容（必填）
  contentMd?: string        // Markdown 内容
  groupId: number           // 分类 id（必填）
  albumId?: number          // 专辑 id
  label?: string            // 标签（多个逗号隔开）
  author?: string           // 作者
  worksName?: string        // 作品名称
  picture?: string          // 图片（多个逗号隔开）
  dictumState: number       // 状态：1=公开，2=私有
}
