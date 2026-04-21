import type { TimeNotesVo, TimeNotesInfo, TimeNotesSearchBo, TableDataInfo } from '~/types'

/**
 * 时光小记 API - 前台展示页面使用
 * 路径前缀：/white/time/notes
 */
export const useTimeNotesApi = () => {
  const api = useApi()

  /**
   * 获取时光小记列表（按日期分组）
   * 接口：POST /white/time/notes/list
   * 返回格式：{ total, rows: TimeNotesVo[] }
   */
  const getTimeNotesList = async (params: TimeNotesSearchBo): Promise<TableDataInfo<TimeNotesVo>> => {
    return await api.postPage<TimeNotesVo>(
      '/white/time/notes/list',
      params
    )
  }

  /**
   * 获取时光小记详情
   * 接口：GET /white/time/notes/getInfo/{id}
   */
  const getTimeNotesDetail = async (id: number): Promise<TimeNotesInfo> => {
    return await api.get<TimeNotesInfo>(`/white/time/notes/getInfo/${id}`)
  }

  /**
   * 小记归档
   * 接口：POST /white/time/notes/archive
   * 返回格式：{ total, rows: TimeNotesVo[] }
   */
  const getTimeNotesArchive = async (params?: TimeNotesSearchBo): Promise<TableDataInfo<TimeNotesVo>> => {
    return await api.postPage<TimeNotesVo>(
      '/white/time/notes/archive',
      params
    )
  }

  return {
    getTimeNotesList,
    getTimeNotesDetail,
    getTimeNotesArchive
  }
}
