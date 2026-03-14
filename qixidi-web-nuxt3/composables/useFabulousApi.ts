import type { FabulousBo } from '~/types'

export const useFabulousApi = () => {
  const api = useApi()

  /**
   * 点赞文章
   * 接口：/api/frontDesk/fabulous/spot
   * @param data - 点赞数据
   */
  const likeArticle = async (data: FabulousBo): Promise<void> => {
    await api.post('/frontDesk/fabulous/spot', data)
  }

  /**
   * 取消点赞文章
   * 接口：/api/frontDesk/fabulous/cancel
   * @param data - 点赞数据
   */
  const cancelLikeArticle = async (data: FabulousBo): Promise<void> => {
    await api.post('/frontDesk/fabulous/cancel', data)
  }

  return {
    likeArticle,
    cancelLikeArticle
  }
}
