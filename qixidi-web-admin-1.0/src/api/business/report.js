import request from '@/utils/request'

// 查询用户签到列表
export function listReport(query) {
  return request({
    url: '/business/user/report/list',
    method: 'get',
    params: query
  })
}

// 查询用户签到详细
export function getReport(id) {
  return request({
    url: '/business/user/report/' + id,
    method: 'get'
  })
}

// 新增用户签到
export function addReport(data) {
  return request({
    url: '/business/user/report',
    method: 'post',
    data: data
  })
}

// 修改用户签到
export function updateReport(data) {
  return request({
    url: '/business/user/report',
    method: 'put',
    data: data
  })
}

// 删除用户签到
export function delReport(id) {
  return request({
    url: '/business/user/report/' + id,
    method: 'delete'
  })
}
