import request from '@/utils/request'

// 查询爱情祝福语列表
export function listComment(query) {
  return request({
    url: '/lover/comment/list',
    method: 'get',
    params: query
  })
}

// 查询爱情祝福语详细
export function getComment(id) {
  return request({
    url: '/lover/comment/' + id,
    method: 'get'
  })
}

// 新增爱情祝福语
export function addComment(data) {
  return request({
    url: '/lover/comment',
    method: 'post',
    data: data
  })
}

// 修改爱情祝福语
export function updateComment(data) {
  return request({
    url: '/lover/comment',
    method: 'put',
    data: data
  })
}

// 删除爱情祝福语
export function delComment(id) {
  return request({
    url: '/lover/comment/' + id,
    method: 'delete'
  })
}
