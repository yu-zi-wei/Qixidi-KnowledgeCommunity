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
   * 获取随笔详情
   * 接口：/white/dictum/info/{id}
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
    getReadingEssaysDetail
  }
}
