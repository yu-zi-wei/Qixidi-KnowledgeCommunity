import request from '@/utils/request'

// 查询用户消息列表
export function listUserInfo(query) {
  return request({
    url: '/business/news/list',
    method: 'get',
    params: query
  })
}

// 查询用户消息详细
export function getUserInfo(id) {
  return request({
    url: '/business/news/' + id,
    method: 'get'
  })
}

// 新增用户消息
export function addUserInfo(data) {
  return request({
    url: '/business/news',
    method: 'post',
    data: data
  })
}

// 修改用户消息
export function updateUserInfo(data) {
  return request({
    url: '/business/news',
    method: 'put',
    data: data
  })
}

// 删除用户消息
export function delUserInfo(id) {
  return request({
    url: '/business/news/userInfo/' + id,
    method: 'delete'
  })
}
