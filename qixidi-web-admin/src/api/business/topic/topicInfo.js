import request from '@/utils/request'

// 查询话题信息列表
export function listInfo(query) {
  return request({
    url: '/topic/info/list',
    method: 'get',
    params: query
  })
}

// 查询话题信息详细
export function getInfo(id) {
  return request({
    url: '/topic/info/' + id,
    method: 'get'
  })
}

// 新增话题信息
export function addInfo(data) {
  return request({
    url: '/topic/info',
    method: 'post',
    data: data
  })
}

// 修改话题信息
export function updateInfo(data) {
  return request({
    url: '/topic/info',
    method: 'put',
    data: data
  })
}

// 删除话题信息
export function delInfo(id) {
  return request({
    url: '/topic/info/' + id,
    method: 'delete'
  })
}
