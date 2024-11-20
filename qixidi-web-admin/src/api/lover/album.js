import request from '@/utils/request'

// 查询时光相册列表
export function listAlbum(query) {
  return request({
    url: '/lover/album/list',
    method: 'get',
    params: query
  })
}

// 查询时光相册详细
export function getAlbum(id) {
  return request({
    url: '/lover/album/' + id,
    method: 'get'
  })
}

// 新增时光相册
export function addAlbum(data) {
  return request({
    url: '/lover/album',
    method: 'post',
    data: data
  })
}

// 修改时光相册
export function updateAlbum(data) {
  return request({
    url: '/lover/album',
    method: 'put',
    data: data
  })
}

// 删除时光相册
export function delAlbum(id) {
  return request({
    url: '/lover/album/' + id,
    method: 'delete'
  })
}
