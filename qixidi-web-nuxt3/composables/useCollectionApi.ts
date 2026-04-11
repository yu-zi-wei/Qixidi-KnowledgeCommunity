import type { CollectionFolder, CollectionItem, CreateCollectionBo, AddCollectionBo } from '~/types'

export const useCollectionApi = () => {
  const api = useApi()

  /**
   * 获取用户收藏夹列表（公开）
   * 接口：/white/collection/list/{uuid}
   */
  const getCollectionFolders = async (userId: string): Promise<CollectionFolder[]> => {
    return await api.get<CollectionFolder[]>(`/white/collection/list/${userId}`)
  }

  /**
   * 获取当前登录用户收藏夹列表
   * 接口：/frontDesk/collection/list
   */
  const getMyCollections = (): Promise<CollectionItem[]> => {
    return api.get<CollectionItem[]>('/frontDesk/collection/list')
  }

  /**
   * 创建收藏夹
   * 接口：/frontDesk/add/collection
   */
  const createCollectionFolder = async (data: CreateCollectionBo): Promise<void> => {
    await api.post('/frontDesk/add/collection', data)
  }

  /**
   * 编辑收藏夹
   * 接口：PUT /business/collection/information
   */
  const updateCollection = (data: CreateCollectionBo & { id: number }): Promise<void> => {
    return api.put('/business/collection/information', data)
  }

  /**
   * 删除收藏夹
   * 接口：/frontDesk/delete/collection/{id}
   */
  const deleteCollection = (id: number): Promise<void> => {
    return api.delete(`/frontDesk/delete/collection/${id}`)
  }

  /**
   * 收藏文章到收藏夹
   * 接口：/frontDesk/add/collection/data
   */
  const addArticleToCollection = async (data: AddCollectionBo): Promise<void> => {
    await api.post('/frontDesk/add/collection/data', data)
  }

  /**
   * 取消收藏文章
   * 接口：/frontDesk/delete/collection/data/{articleId}（GET 请求）
   */
  const removeArticleFromCollection = async (articleId: number): Promise<void> => {
    await api.get(`/frontDesk/delete/collection/data/${articleId}`)
  }

  /**
   * 获取收藏夹详情（公开）
   * 接口：/white/collection/{id}
   */
  const getCollectionDetail = (id: number) => {
    return api.get<CollectionItem>(`/white/collection/${id}`)
  }

  /**
   * 获取收藏夹文章列表（公开，分页）
   * 接口：/white/collection/article/list
   */
  const getCollectionArticles = (params: { collectionId: number; pageNum: number; pageSize: number; articleTitle?: string }) => {
    return api.getPage<any>('/white/collection/article/list', params)
  }

  return {
    getCollectionFolders,
    getMyCollections,
    getCollectionDetail,
    getCollectionArticles,
    createCollectionFolder,
    updateCollection,
    deleteCollection,
    addArticleToCollection,
    removeArticleFromCollection
  }
}
