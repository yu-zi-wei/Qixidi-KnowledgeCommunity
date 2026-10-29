import request from '@/utils/request'

// 查询网站基本信息列表
export function listInfo(query) {
  return request({
    url: '/basic/info/list',
    method: 'get',
    params: query
  })
}

// 查询网站基本信息详细
export function getInfo(id) {
  return request({
    url: '/basic/info/' + id,
    method: 'get'
  })
}

// 新增网站基本信息
export function addInfo(data) {
  return request({
    url: '/basic/info',
    method: 'post',
    data: data
  })
}

// 修改网站基本信息
export function updateInfo(data) {
  return request({
    url: '/basic/info',
    method: 'put',
    data: data
  })
}

// 删除网站基本信息
export function delInfo(id) {
  return request({
    url: '/basic/info/' + id,
    method: 'delete'
  })
}

// 查询网站基本信息列表前台接口
export function listInfoApi(query) {
  return request({
    url: '/api/lover/basicInfo',
    method: 'get',
    params: query
  })
}
