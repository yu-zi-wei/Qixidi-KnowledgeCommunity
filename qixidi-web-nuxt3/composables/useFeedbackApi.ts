import type { FeedbackStatusSumVo, FeedbackVo } from '~/types'

export const useFeedbackApi = () => {
  const api = useApi()

  /**
   * 反馈列表（白名单接口，可传 uid 只看某用户的反馈）
   */
  const getList = (params: { pageNum?: number; pageSize?: number; uid?: string; status?: number }) => {
    return api.getPage<FeedbackVo>('/white/feedback/list', params)
  }

  /**
   * 反馈状态汇总（各状态数量）
   */
  const getStatusSum = () => {
    return api.get<FeedbackStatusSumVo>('/white/feedback/status/sum')
  }

  /**
   * 提交反馈
   */
  const addFeedback = (data: { feedbackTitle: string; feedbackContent: string }) => {
    return api.post('/frontDesk/feedback/add', data)
  }

  /**
   * 修改反馈状态（管理端操作，会触发系统消息 + WebSocket 推送）
   */
  const updateStatus = (id: number, status: number) => {
    return api.get(`/frontDesk/feedback/update/status/${id}/${status}`)
  }

  /**
   * 删除反馈
   */
  const deleteFeedback = (ids: string | number) => {
    return api.delete(`/frontDesk/feedback/delete/${ids}`)
  }

  return { getList, getStatusSum, addFeedback, updateStatus, deleteFeedback }
}
