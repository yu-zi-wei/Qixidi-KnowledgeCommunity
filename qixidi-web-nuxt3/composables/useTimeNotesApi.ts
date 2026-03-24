import type { TimeNotes, TimeNotesVo, TimeNotesInfo, TimeNotesSearchBo, TableDataInfo } from '~/types'

export const useTimeNotesApi = () => {
  const api = useApi()

  /**
   * 获取时光小记列表（按日期分组）
   * 接口：POST /white/time/notes/list
   * 返回格式：{ total, rows: TimeNotesVo[] }，每个 TimeNotesVo 包含 recordTime 和 list
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

  return {
    getTimeNotesList,
    getTimeNotesDetail
  }
}
