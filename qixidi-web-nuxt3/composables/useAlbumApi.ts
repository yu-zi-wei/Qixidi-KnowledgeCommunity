export interface AlbumItem {
  id: number
  uid: string
  /** 来源内容id（字符串避免 JS 精度丢失） */
  sourceId: string
  /** 来源内容时间 yyyy-MM-dd */
  sourceTime: string
  /** 来源：1-时光小记，2-文章 */
  source: number
  url: string
}

export interface AlbumMonthStat {
  /** 月份 yyyy-MM */
  month: string
  /** 该月图片张数 */
  count: number
}

export const useAlbumApi = () => {
  const api = useApi()

  /**
   * 相册图片分页（免登录白名单，全站图片墙）
   * 接口：POST /white/album/list
   * @param month 时间筛选，yyyy-MM 按月 / yyyy 按年，缺省全部
   */
  const getAlbumList = async (params: { pageNum?: number; pageSize?: number; uid?: string; month?: string }) => {
    return await api.postPage<AlbumItem>('/white/album/list', params)
  }

  /**
   * 月份统计（免登录白名单，目录用，与图片分页解耦）
   * 接口：GET /white/album/months
   */
  const getAlbumMonths = async (): Promise<AlbumMonthStat[]> => {
    return await api.get<AlbumMonthStat[]>('/white/album/months')
  }

  return { getAlbumList, getAlbumMonths }
}
