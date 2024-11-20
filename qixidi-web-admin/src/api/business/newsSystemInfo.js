import request from '@/utils/request'

// 查询系统消息列表
export function listSystemInfo(query) {
  return request({
    url: '/business/news/systemInfo/list',
    method: 'get',
    params: query
  })
}

// 查询系统消息详细
export function getSystemInfo(id) {
  return request({
    url: '/business/news/systemInfo/' + id,
    method: 'get'
  })
}

// 新增系统消息
export function addSystemInfo(data) {
  return request({
    url: '/business/news/systemInfo',
    method: 'post',
    data: data
  })
}

// 修改系统消息
export function updateSystemInfo(data) {
  return request({
    url: '/business/news/systemInfo',
    method: 'put',
    data: data
  })
}

// 删除系统消息
export function delSystemInfo(id) {
  return request({
    url: '/business/news/systemInfo/' + id,
    method: 'delete'
  })
}
