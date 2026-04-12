interface FriendLinkVo {
  id: number
  linkName: string
  linkIntro?: string
  linkAvatar?: string
  linkUrl: string
  createTime?: string
}

export const useFriendLinkApi = () => {
  const api = useApi()

  /** 获取友链列表 */
  const getFriendLinkList = () => {
    return api.getPage<FriendLinkVo>('/white/site/friend-link')
  }

  return { getFriendLinkList }
}
