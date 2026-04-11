/**
 * 专栏详情页 API（公开，无需登录）
 */

interface SpecialDetailVo {
  id: number
  specialName: string
  specialIntroduce?: string
  cover?: string
  state?: number
  uid: string
  username?: string
  createTime?: string
  updateTime?: string
  includedCount?: number
}

interface SpecialArticleVo {
  id: number
  articleTitle: string
  articleCover?: string
  articleAbstract?: string
  createTime?: string
  numberTimes?: number
  likeTimes?: number
  commentTimes?: number
}

interface UserInfoVo {
  uuid?: string
  username?: string
  nickname?: string
  avatar?: string
  remark?: string
  introduce?: string
  blog?: string
  location?: string
  company?: string
}

export const useSpecialDetailApi = () => {
  const api = useApi()

  /** 专栏详情 */
  const getSpecialDetail = (id: number) => {
    return api.get<SpecialDetailVo>(`/white/special/${id}`)
  }

  /** 用户专栏列表 */
  const getSpecialList = (uid: string) => {
    return api.get<SpecialDetailVo[]>('/white/special/list', { uid })
  }

  /** 专栏文章列表（分页） */
  const getSpecialArticles = (params: { userId: string; specialId: number; pageNum: number; pageSize: number; articleTitle?: string }) => {
    return api.getPage<SpecialArticleVo>('/white/article/user/list', params)
  }

  /** 用户信息 */
  const getUserInfo = (uuid: string) => {
    return api.get<UserInfoVo>(`/white/user/info/${uuid}`)
  }

  return { getSpecialDetail, getSpecialList, getSpecialArticles, getUserInfo }
}
