import request from '@/utils/request'

// 查询标签信息列表
export function listInfo(query) {
  return request({
    url: '/business/info/list',
    method: 'get',
    params: query
  })
}

// 查询标签信息详细
export function getInfo(id) {
  return request({
    url: '/business/info/' + id,
    method: 'get'
  })
}

// 新增标签信息
export function addInfo(data) {
  return request({
    url: '/business/info',
    method: 'post',
    data: data
  })
}

// 修改标签信息
export function updateInfo(data) {
  return request({
    url: '/business/info',
    method: 'put',
    data: data
  })
}

// 删除标签信息
export function delInfo(id) {
  return request({
    url: '/business/info/' + id,
    method: 'delete'
  })
}

