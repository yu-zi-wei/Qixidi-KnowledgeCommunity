import type { PrivateUserVo, PrivateNewsInfoVo, PrivateNewsInfoBo, UserSimpleInfoVo, PageQuery } from '~/types'

export const usePrivateMessageApi = () => {
  const api = useApi()

  /** 私信用户列表 */
  const getUserList = (uid: string, pageNum: number = 1, pageSize: number = 50) => {
    return api.getPage<PrivateUserVo>('/frontDesk/private/user/list', { uid, pageNum, pageSize })
  }

  /** 私信消息列表 */
  const getMessageList = (replyTargetUid: string, pageNum: number = 1, pageSize: number = 50) => {
    return api.getPage<PrivateNewsInfoVo>('/frontDesk/private/newsInfo/list', { replyTargetUid, pageNum, pageSize })
  }

  /** 标记私信已读 */
  const markRead = (targetUid: string) => {
    return api.get<number>(`/frontDesk/private/newsInfo/been/read/${targetUid}`)
  }

  /** 发送私信 */
  const sendMessage = (bo: PrivateNewsInfoBo) => {
    return api.post('/frontDesk/private/newsInfo', bo)
  }

  /** 判断用户是否在线 */
  const isOnline = (userId: string) => {
    return api.get<UserSimpleInfoVo>(`/websocket/is-online/${userId}`)
  }

  /** 删除私信用户 */
  const deleteUser = (id: string) => {
    return api.delete<void>(`/frontDesk/private/user/${id}`)
  }

  /** 新增私信用户 */
  const addUser = (targetUid: string) => {
    return api.get<void>(`/frontDesk/private/user/add/${targetUid}`)
  }

  return { getUserList, getMessageList, markRead, sendMessage, isOnline, deleteUser, addUser }
}
