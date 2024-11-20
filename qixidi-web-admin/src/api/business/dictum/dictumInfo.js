import request from '@/utils/request'

// 查询名言信息列表
export function listInfo(query) {
  return request({
    url: '/business/dictum/info/list',
    method: 'get',
    params: query
  })
}

// 查询名言信息详细
export function getInfo(id) {
  return request({
    url: '/business/dictum/info/' + id,
    method: 'get'
  })
}

// 新增名言信息
export function addInfo(data) {
  return request({
    url: '/business/dictum/info',
    method: 'post',
    data: data
  })
}

// 修改名言信息
export function updateInfo(data) {
  return request({
    url: '/business/dictum/info',
    method: 'put',
    data: data
  })
}

// 删除名言信息
export function delInfo(id) {
  return request({
    url: '/business/dictum/info/' + id,
    method: 'delete'
  })
}
