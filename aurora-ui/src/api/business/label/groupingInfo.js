import request from '@/utils/request'

// 查询标签分组信息列表
export function listGroupingInfo(query) {
  return request({
    url: '/business/groupingInfo/list',
    method: 'get',
    params: query
  })
}

// 查询标签分组信息详细
export function getGroupingInfo(id) {
  return request({
    url: '/business/groupingInfo/' + id,
    method: 'get'
  })
}

// 新增标签分组信息
export function addGroupingInfo(data) {
  return request({
    url: '/business/groupingInfo',
    method: 'post',
    data: data
  })
}

// 修改标签分组信息
export function updateGroupingInfo(data) {
  return request({
    url: '/business/groupingInfo',
    method: 'put',
    data: data
  })
}

// 删除标签分组信息
export function delGroupingInfo(id) {
  return request({
    url: '/business/groupingInfo/' + id,
    method: 'delete'
  })
}
