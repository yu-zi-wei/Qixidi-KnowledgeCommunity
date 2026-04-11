/**
 * 关注相关 API
 */

interface FollowUserVo {
  uuid: string
  nickname?: string
  avatar?: string
  location?: string
  remark?: string
  occupation?: string
  introduce?: string
  isFollow?: boolean
  createTime?: string
}

interface FollowTagVo {
  id: number
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  isFollow?: boolean
  createTime?: string
}

export const useFollowApi = () => {
  const api = useApi()

  /** 关注列表（type: 1=用户, 2=标签） */
  const getFollowList = (uid: string, type: number) => {
    return api.get<(FollowUserVo | FollowTagVo)[]>(`/white/user/follow/list/${uid}/${type}`)
  }

  return { getFollowList }
}
