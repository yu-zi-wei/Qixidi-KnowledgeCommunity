import type { ReadingEssaysAlbum } from '~/types'

/**
 * 随笔专辑管理 API
 */
export const useDictumAlbumApi = () => {
  const api = useApi()

  return {
    /** 获取专辑列表（分页 1,-1 查全部） */
    getList: () => api.getPage<ReadingEssaysAlbum>('/frontDesk/dictum/album/role/list', {
      pageNum: 1,
      pageSize: -1
    }),

    /** 新建专辑 */
    create: (data: Partial<ReadingEssaysAlbum>) => api.post('/frontDesk/dictum/album', data),

    /** 更新专辑 */
    update: (data: Partial<ReadingEssaysAlbum>) => api.put('/frontDesk/dictum/album', data),

    /** 删除专辑 */
    delete: (id: number) => api.delete(`/frontDesk/dictum/album/${id}`)
  }
}
