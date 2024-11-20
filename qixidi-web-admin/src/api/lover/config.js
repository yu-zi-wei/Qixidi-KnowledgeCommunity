import request from '@/utils/request'

// 查询基本配置列表
export function listConfig(query) {
  return request({
    url: '/lover/config/list',
    method: 'get',
    params: query
  })
}

// 查询基本配置详细
export function getConfig(id) {
  return request({
    url: '/lover/config/' + id,
    method: 'get'
  })
}

// 新增基本配置
export function addConfig(data) {
  return request({
    url: '/lover/config',
    method: 'post',
    data: data
  })
}

// 修改基本配置
export function updateConfig(data) {
  return request({
    url: '/lover/config',
    method: 'put',
    data: data
  })
}

// 删除基本配置
export function delConfig(id) {
  return request({
    url: '/lover/config/' + id,
    method: 'delete'
  })
}
