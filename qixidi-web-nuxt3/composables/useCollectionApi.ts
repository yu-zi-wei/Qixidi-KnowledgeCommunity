import type { CollectionFolder, CreateCollectionBo, AddCollectionBo } from '~/types'

export const useCollectionApi = () => {
  const api = useApi()

  /**
   * 获取用户收藏夹列表
   * 接口：/white/collection/list/{userId}
   * @param userId - 用户 id
   */
  const getCollectionFolders = async (userId: string): Promise<CollectionFolder[]> => {
    return await api.get<CollectionFolder[]>(`/white/collection/list/${userId}`)
  }

  /**
   * 创建收藏夹
   * 接口：/frontDesk/add/collection
   * @param data - 收藏夹数据
   */
  const createCollectionFolder = async (data: CreateCollectionBo): Promise<void> => {
    await api.post('/frontDesk/add/collection', data)
  }

  /**
   * 收藏文章到收藏夹
   * 接口：/frontDesk/add/collection/data
   * @param data - 收藏数据
   */
  const addArticleToCollection = async (data: AddCollectionBo): Promise<void> => {
    await api.post('/frontDesk/add/collection/data', data)
  }

  /**
   * 取消收藏文章
   * 接口：/frontDesk/delete/collection/data/{articleId}（GET 请求）
   * @param articleId - 文章 id
   */
  const removeArticleFromCollection = async (articleId: number): Promise<void> => {
    await api.get(`/frontDesk/delete/collection/data/${articleId}`)
  }

  return {
    getCollectionFolders,
    createCollectionFolder,
    addArticleToCollection,
    removeArticleFromCollection
  }
}
