import request from '@/utils/request'

// 查询私信记录列表
export function listUser(query) {
  return request({
    url: '/business/private/user/list',
    method: 'get',
    params: query
  })
}

// 查询私信记录详细
export function getUser(id) {
  return request({
    url: '/business/private/user/' + id,
    method: 'get'
  })
}

// 新增私信记录
export function addUser(data) {
  return request({
    url: '/business/private/user',
    method: 'post',
    data: data
  })
}

// 修改私信记录
export function updateUser(data) {
  return request({
    url: '/business/private/user',
    method: 'put',
    data: data
  })
}

// 删除私信记录
export function delUser(id) {
  return request({
    url: '/business/private/user/' + id,
    method: 'delete'
  })
}
