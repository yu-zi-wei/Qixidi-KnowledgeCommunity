import type { ArticleInfo } from '~/types'

export const useArticleApi = () => {
  const api = useApi()

  const getRecommendList = (pageNum: number = 1, pageSize: number = 10) => {
    return api.getPage<ArticleInfo>(
      '/white/article/recommend/list',
      { pageNum, pageSize }
    )
  }

  const getSortList = (params: {
    pageNum?: number
    pageSize?: number
    createTime?: number
    numberTimes?: number
    likeTimes?: number
    sortType?: number
    groupingId?: number
  }) => {
    return api.getPage<ArticleInfo>('/white/article/sort', params)
  }

  /**
   * 获取用户最近文章列表
   * 接口：/user/lately/article/list
   */
  const getLatelyArticleList = (params: {
    pageNum?: number
    pageSize?: number
    auditState?: number
  }) => {
    return api.getPage<ArticleInfo>('/user/lately/article/list', params)
  }

  const getFollowList = (params: {
    pageNum?: number
    pageSize?: number
    sortType?: number
  }) => {
    return api.getPage<ArticleInfo>('/white/article/follow/list', params)
  }

  const getCategoryList = (params: {
    pageNum?: number
    pageSize?: number
    sortType?: number
    groupingId?: number
  }) => {
    return api.getPage<ArticleInfo>('/white/article/label/list', params)
  }

  /**
   * 获取文章详情（编辑用）
   * 接口：/user/get/article/{id}
   */
  const getDetail = async (id: string | number): Promise<any> => {
    const data = await api.get<any>(`/user/get/article/${id}`)
    // 转换后端的 labelId（字符串）为前端的 labelIds（数组）
    if (data && data.labelId) {
      data.labelIds = data.labelId.split(',').map(Number)
      // 不要删除原字段，可能其他地方需要
      // delete (data as any).labelId
    }
    return data
  }

  /**
   * 获取文章详情（前台展示）
   * 接口：/white/article/details/{id}
   * 返回：ArticleInformationVo
   */
  const getArticleDetail = async (id: string | number) => {
    return api.get<any>(`/white/article/details/${id}`)
  }

  /**
   * 转换表单数据为后端格式
   */
  const transformFormData = (data: any) => {
    const transformed = { ...data }

    // 转换 labelIds（数组）为 labelId（逗号分隔字符串）
    if (transformed.labelIds && Array.isArray(transformed.labelIds)) {
      transformed.labelId = transformed.labelIds.join(',')
      delete transformed.labelIds
    }

    return transformed
  }

  /**
   * 新增/更新文章
   * 返回文章信息（包含 ID）
   */
  const insertArticle = (data: any): Promise<ArticleInfo> => {
    const transformedData = transformFormData(data)
    return api.post<ArticleInfo>('/user/insert/article', transformedData)
  }

  /**
   * 保存草稿
   * 返回文章信息（包含 ID）
   */
  const saveDraft = (data: any): Promise<ArticleInfo> => {
    const transformedData = transformFormData(data)
    return api.put<ArticleInfo>('/user/save/draft', transformedData)
  }

  return {
    getRecommendList,
    getSortList,
    getCategoryList,
    getFollowList,
    getDetail,
    getArticleDetail,
    getLatelyArticleList,
    insertArticle,
    saveDraft
  }
}
