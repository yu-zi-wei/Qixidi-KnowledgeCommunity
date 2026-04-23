import type { SiteStats, SiteInfo } from '~/types'

export const useSiteApi = () => {
  const api = useApi()

  const getTotalData = () => {
    return api.get<SiteStats>('/white/site/total-data')
  }

  const getInfo = () => {
    return api.get<SiteInfo>('/white/site/info')
  }

  const getUserAllList = () => {
    return api.getPage<any>('/white/userAllList')
  }

  return { getTotalData, getInfo, getUserAllList }
}
