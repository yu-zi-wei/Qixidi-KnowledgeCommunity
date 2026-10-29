import request from '@/utils/request'

// 查询工具信息列表
export function listToolInfo(query) {
  return request({
    url: '/business/toolInfo/list',
    method: 'get',
    params: query
  })
}

// 查询工具信息详细
export function getToolInfo(id) {
  return request({
    url: '/business/toolInfo/' + id,
    method: 'get'
  })
}

// 新增工具信息
export function addToolInfo(data) {
  return request({
    url: '/business/toolInfo',
    method: 'post',
    data: data
  })
}

// 修改工具信息
export function updateToolInfo(data) {
  return request({
    url: '/business/toolInfo',
    method: 'put',
    data: data
  })
}

// 删除工具信息
export function delToolInfo(id) {
  return request({
    url: '/business/toolInfo/' + id,
    method: 'delete'
  })
}
