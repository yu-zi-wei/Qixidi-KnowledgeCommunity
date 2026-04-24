import type { UserCensusCount, UserSubmissionItem, UserSubmissionRecord } from '~/types'

export const useUserCensusApi = () => {
  const api = useApi()

  /** 获取用户统计数据（实时查询） */
  const getUserCensusCount = () => {
    return api.get<UserCensusCount>('/user/census/count/user')
  }

  /**
   * 获取用户投稿记录
   * 返回格式：[{dateTimes, censusSum}, ...]
   */
  const getUserSubmission = async (): Promise<UserSubmissionRecord> => {
    const data = await api.get<UserSubmissionItem[]>('/user/census/submission')
    // 转换为 { dateTimes: censusSum } 格式
    return (data || []).reduce((acc, item) => {
      acc[item.dateTimes] = item.censusSum
      return acc
    }, {} as UserSubmissionRecord)
  }

  return {
    getUserCensusCount,
    getUserSubmission
  }
}
