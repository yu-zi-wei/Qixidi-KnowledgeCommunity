import type { Sidebar } from '~/types'

export const useSidebarApi = () => {
  const api = useApi()

  const getList = (type: number = 1, status: number = 0) => {
    return api.get<Sidebar[]>(
      '/white/configure/sidebar/list',
      { type, status }
    )
  }

  return { getList }
}
