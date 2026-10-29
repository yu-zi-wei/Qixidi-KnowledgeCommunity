import request from '@/utils/request'

// 查询网站数据信息
export function statInfo(query) {
  return request({
    url: '/business/stat/data/info',
    method: 'get',
    params: query
  })
}

// 查询网站数据信息
export function statTheInfo(query) {
  return request({
    url: '/business/stat/data/the/info',
    method: 'get',
    params: query
  })
}

// 查询网站标签/分类统计
export function labelDate(query) {
  return request({
    url: '/business/stat/data/label/data',
    method: 'get',
    params: query
  })
}
