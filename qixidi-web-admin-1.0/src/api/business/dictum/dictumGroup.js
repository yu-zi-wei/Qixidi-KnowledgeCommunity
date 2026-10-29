import request from '@/utils/request'

// 查询名言分组列表
export function listGroup(query) {
  return request({
    url: '/business/dictum/group/list',
    method: 'get',
    params: query
  })
}

// 查询名言分组详细
export function getGroup(id) {
  return request({
    url: '/business/dictum/group/' + id,
    method: 'get'
  })
}

// 新增名言分组
export function addGroup(data) {
  return request({
    url: '/business/dictum/group',
    method: 'post',
    data: data
  })
}

// 修改名言分组
export function updateGroup(data) {
  return request({
    url: '/business/dictum/group',
    method: 'put',
    data: data
  })
}

// 删除名言分组
export function delGroup(id) {
  return request({
    url: '/business/dictum/group/' + id,
    method: 'delete'
  })
}
