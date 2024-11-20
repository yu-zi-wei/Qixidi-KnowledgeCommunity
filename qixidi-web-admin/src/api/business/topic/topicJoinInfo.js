import request from '@/utils/request'

// 查询话题参与信息列表
export function listJoinInfo(query) {
  return request({
    url: '/topicJoin/joinInfo/list',
    method: 'get',
    params: query
  })
}

// 查询话题参与信息详细
export function getJoinInfo(id) {
  return request({
    url: '/topicJoin/joinInfo/' + id,
    method: 'get'
  })
}

// 新增话题参与信息
export function addJoinInfo(data) {
  return request({
    url: '/topicJoin/joinInfo',
    method: 'post',
    data: data
  })
}

// 修改话题参与信息
export function updateJoinInfo(data) {
  return request({
    url: '/topicJoin/joinInfo',
    method: 'put',
    data: data
  })
}

// 删除话题参与信息
export function delJoinInfo(id) {
  return request({
    url: '/topicJoin/joinInfo/' + id,
    method: 'delete'
  })
}
