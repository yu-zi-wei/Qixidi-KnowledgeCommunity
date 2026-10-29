import request from '@/utils/request'

// 查询专栏信息列表
export function listInformation(query) {
  return request({
    url: '/special/information/list',
    method: 'get',
    params: query
  })
}

// 查询专栏信息详细
export function getInformation(id) {
  return request({
    url: '/special/information/' + id,
    method: 'get'
  })
}

// 新增专栏信息
export function addInformation(data) {
  return request({
    url: '/special/information',
    method: 'post',
    data: data
  })
}

// 修改专栏信息
export function updateInformation(data) {
  return request({
    url: '/special/information',
    method: 'put',
    data: data
  })
}

// 删除专栏信息
export function delInformation(id) {
  return request({
    url: '/special/information/' + id,
    method: 'delete'
  })
}
