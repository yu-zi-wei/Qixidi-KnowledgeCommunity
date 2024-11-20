import request from '@/utils/request'

// 查询点赞列表
export function listRecord(query) {
  return request({
    url: '/business/fabulous/record/list',
    method: 'get',
    params: query
  })
}

// 查询点赞详细
export function getRecord(id) {
  return request({
    url: '/business/fabulous/record/' + id,
    method: 'get'
  })
}

// 新增点赞
export function addRecord(data) {
  return request({
    url: '/business/fabulous/record',
    method: 'post',
    data: data
  })
}

// 修改点赞
export function updateRecord(data) {
  return request({
    url: '/business/fabulous/record',
    method: 'put',
    data: data
  })
}

// 删除点赞
export function delRecord(id) {
  return request({
    url: '/business/fabulous/record/' + id,
    method: 'delete'
  })
}
