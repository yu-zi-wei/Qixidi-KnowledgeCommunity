import request from '@/utils/request'

// 查询爱情树列表
export function listTree(query) {
  return request({
    url: '/lover/tree/list',
    method: 'get',
    params: query
  })
}

// 查询爱情树详细
export function getTree(id) {
  return request({
    url: '/lover/tree/' + id,
    method: 'get'
  })
}

// 新增爱情树
export function addTree(data) {
  return request({
    url: '/lover/tree',
    method: 'post',
    data: data
  })
}

// 修改爱情树
export function updateTree(data) {
  return request({
    url: '/lover/tree',
    method: 'put',
    data: data
  })
}

// 删除爱情树
export function delTree(id) {
  return request({
    url: '/lover/tree/' + id,
    method: 'delete'
  })
}
