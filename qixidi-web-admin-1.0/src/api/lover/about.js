import request from '@/utils/request'

// 查询关于我们列表
export function listAbout(query) {
  return request({
    url: '/lover/about/list',
    method: 'get',
    params: query
  })
}

// 查询关于我们详细
export function getAbout(id) {
  return request({
    url: '/lover/about/' + id,
    method: 'get'
  })
}

// 新增关于我们
export function addAbout(data) {
  return request({
    url: '/lover/about',
    method: 'post',
    data: data
  })
}

// 修改关于我们
export function updateAbout(data) {
  return request({
    url: '/lover/about',
    method: 'put',
    data: data
  })
}

// 删除关于我们
export function delAbout(id) {
  return request({
    url: '/lover/about/' + id,
    method: 'delete'
  })
}
