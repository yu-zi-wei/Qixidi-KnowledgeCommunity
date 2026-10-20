import type { CensusTrendItem, UserCensusCount, UserSubmissionItem, UserSubmissionRecord } from '~/types'

export const useUserCensusApi = () => {
  const api = useApi()

  /** 获取用户统计数据（实时查询） */
  const getUserCensusCount = () => {
    return api.get<UserCensusCount>('/user/census/count/user')
  }

  /** 近30天我的内容浏览趋势（按天聚合） */
  const getBrowseTrend = () => {
    return api.get<CensusTrendItem[]>('/user/census/time/browse')
  }

  /** 近30天互动趋势（获赞+评论两条序列，title 区分） */
  const getInteractTrend = () => {
    return api.get<CensusTrendItem[]>('/user/census/time/interact')
  }

  /**
   * 获取用户投稿记录（文章 + 小记按天统计）
   * 返回格式：[{dateTimes, articleSum, noteSum}, ...]
   */
  const getUserSubmission = async (): Promise<UserSubmissionRecord> => {
    const data = await api.get<UserSubmissionItem[]>('/user/census/submission')
    // 转换为 { dateTimes: {articleSum, noteSum} } 格式
    return (data || []).reduce((acc, item) => {
      acc[item.dateTimes] = { articleSum: item.articleSum || 0, noteSum: item.noteSum || 0 }
      return acc
    }, {} as UserSubmissionRecord)
  }

  return {
    getUserCensusCount,
    getUserSubmission,
    getBrowseTrend,
    getInteractTrend
  }
}
