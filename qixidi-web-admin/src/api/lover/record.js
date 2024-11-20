import request from '@/utils/request'

// 查询爱情记录列表
export function listRecord(query) {
  return request({
    url: '/lover/record/list',
    method: 'get',
    params: query
  })
}

// 查询爱情记录详细
export function getRecord(id) {
  return request({
    url: '/lover/record/' + id,
    method: 'get'
  })
}

// 新增爱情记录
export function addRecord(data) {
  return request({
    url: '/lover/record',
    method: 'post',
    data: data
  })
}

// 修改爱情记录
export function updateRecord(data) {
  return request({
    url: '/lover/record',
    method: 'put',
    data: data
  })
}

// 删除爱情记录
export function delRecord(id) {
  return request({
    url: '/lover/record/' + id,
    method: 'delete'
  })
}
