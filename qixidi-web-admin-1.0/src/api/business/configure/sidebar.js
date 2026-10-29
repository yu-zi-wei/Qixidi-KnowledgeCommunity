import request from '@/utils/request'

// 查询侧边栏配置列表
export function listSidebar(query) {
  return request({
    url: '/business/configure/sidebar/list',
    method: 'get',
    params: query
  })
}

// 查询侧边栏配置详细
export function getSidebar(id) {
  return request({
    url: '/business/configure/sidebar/' + id,
    method: 'get'
  })
}

// 新增侧边栏配置
export function addSidebar(data) {
  return request({
    url: '/business/configure/sidebar',
    method: 'post',
    data: data
  })
}

// 修改侧边栏配置
export function updateSidebar(data) {
  return request({
    url: '/business/configure/sidebar',
    method: 'put',
    data: data
  })
}

// 删除侧边栏配置
export function delSidebar(id) {
  return request({
    url: '/business/configure/sidebar/' + id,
    method: 'delete'
  })
}
