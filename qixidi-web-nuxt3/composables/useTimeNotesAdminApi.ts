import type { TimeNotesBo, TimeNotesInfo, TimeNotesSearchBo, TableDataInfo } from '~/types'

/**
 * 时光小记管理 API - 后台管理页面使用
 * 路径前缀：/frontDesk/time/notes
 */
export const useTimeNotesAdminApi = () => {
  const api = useApi()

  /**
   * 获取时光小记列表（按日期分组）
   * 接口：POST /frontDesk/time/notes/list
   * 返回格式：{ total, rows: TimeNotesVo[] }
   */
  const getTimeNotesList = async (params: TimeNotesSearchBo): Promise<TableDataInfo<any>> => {
    return await api.postPage<any>(
      '/frontDesk/time/notes/list',
      params
    )
  }

  /**
   * 获取时光小记详情
   * 接口：GET /frontDesk/time/notes/getInfo/{id}
   */
  const getTimeNotesDetail = async (id: number): Promise<TimeNotesInfo> => {
    return await api.get<TimeNotesInfo>(`/frontDesk/time/notes/getInfo/${id}`)
  }

  /**
   * 新增时光小记
   * 接口：POST /frontDesk/time/notes/add
   * 返回：1 表示成功
   */
  const addTimeNotes = async (data: TimeNotesBo): Promise<number> => {
    return await api.post<number>('/frontDesk/time/notes/add', data)
  }

  /**
   * 更新时光小记
   * 接口：POST /frontDesk/time/notes/update
   * 返回：1 表示成功
   */
  const updateTimeNotes = async (data: TimeNotesBo): Promise<number> => {
    return await api.post<number>('/frontDesk/time/notes/update', data)
  }

  /**
   * 删除时光小记
   * 接口：GET /frontDesk/time/notes/delete/{id}
   * 返回：1 表示成功
   */
  const deleteTimeNotes = async (id: number): Promise<number> => {
    return await api.get<number>(`/frontDesk/time/notes/delete/${id}`)
  }

  return {
    getTimeNotesList,
    getTimeNotesDetail,
    addTimeNotes,
    updateTimeNotes,
    deleteTimeNotes
  }
}
