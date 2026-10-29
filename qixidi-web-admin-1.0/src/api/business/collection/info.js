import request from '@/utils/request'

// 查询收藏夹信息列表
export function listInformation(query) {
  return request({
    url: '/business/collection/information/list',
    method: 'get',
    params: query
  })
}

// 查询收藏夹信息详细
export function getInformation(id) {
  return request({
    url: '/business/collection/information/' + id,
    method: 'get'
  })
}

// 新增收藏夹信息
export function addInformation(data) {
  return request({
    url: '/business/collection/information',
    method: 'post',
    data: data
  })
}

// 修改收藏夹信息
export function updateInformation(data) {
  return request({
    url: '/business/collection/information',
    method: 'put',
    data: data
  })
}

// 删除收藏夹信息
export function delInformation(id) {
  return request({
    url: '/business/collection/information/' + id,
    method: 'delete'
  })
}
