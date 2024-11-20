import request from '@/utils/request'

// 查询导航栏配置列表
export function listNavigation(query) {
  return request({
    url: '/business/configure/navigation/list',
    method: 'get',
    params: query
  })
}

// 查询导航栏配置详细
export function getNavigation(id) {
  return request({
    url: '/business/configure/navigation/' + id,
    method: 'get'
  })
}

// 新增导航栏配置
export function addNavigation(data) {
  return request({
    url: '/business/configure/navigation',
    method: 'post',
    data: data
  })
}

// 修改导航栏配置
export function updateNavigation(data) {
  return request({
    url: '/business/configure/navigation',
    method: 'put',
    data: data
  })
}

// 删除导航栏配置
export function delNavigation(id) {
  return request({
    url: '/business/configure/navigation/' + id,
    method: 'delete'
  })
}
