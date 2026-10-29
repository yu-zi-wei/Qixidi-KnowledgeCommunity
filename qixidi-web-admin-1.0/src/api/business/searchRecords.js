import request from '@/utils/request'

// 查询搜索记录列表
export function listRecords(query) {
  return request({
    url: '/business/search/records/list',
    method: 'get',
    params: query
  })
}

// 查询搜索记录详细
export function getRecords(id) {
  return request({
    url: '/business/search/records/' + id,
    method: 'get'
  })
}

// 新增搜索记录
export function addRecords(data) {
  return request({
    url: '/business/search/records',
    method: 'post',
    data: data
  })
}

// 修改搜索记录
export function updateRecords(data) {
  return request({
    url: '/business/search/records',
    method: 'put',
    data: data
  })
}

// 删除搜索记录
export function delRecords(id) {
  return request({
    url: '/business/search/records/' + id,
    method: 'delete'
  })
}
