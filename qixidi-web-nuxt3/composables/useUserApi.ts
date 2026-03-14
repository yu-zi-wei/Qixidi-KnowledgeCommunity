import type { FollowUser } from '~/types'

export const useUserApi = () => {
  const api = useApi()

  const getFollowList = (type: number = 1) => {
    return api.get<FollowUser[]>(`/user/follow/list/${type}`)
  }

  return { getFollowList }
}
