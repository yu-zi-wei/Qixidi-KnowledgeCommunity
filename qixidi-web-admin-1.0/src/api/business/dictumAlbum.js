import request from '@/utils/request'

// 查询名言专辑列表
export function listAlbum(query) {
  return request({
    url: '/business/dictum/album/list',
    method: 'get',
    params: query
  })
}

// 查询名言专辑详细
export function getAlbum(id) {
  return request({
    url: '/business/dictum/album/' + id,
    method: 'get'
  })
}

// 新增名言专辑
export function addAlbum(data) {
  return request({
    url: '/business/dictum/album',
    method: 'post',
    data: data
  })
}

// 修改名言专辑
export function updateAlbum(data) {
  return request({
    url: '/business/dictum/album',
    method: 'put',
    data: data
  })
}

// 删除名言专辑
export function delAlbum(id) {
  return request({
    url: '/business/dictum/album/' + id,
    method: 'delete'
  })
}
