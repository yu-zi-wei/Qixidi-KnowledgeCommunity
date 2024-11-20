import request from '@/utils/request'

// 查询网站文档列表
export function listSiteFile(query) {
  return request({
    url: '/site/file/list',
    method: 'get',
    params: query
  })
}

// 查询网站文档详细
export function getSiteFile(id) {
  return request({
    url: '/site/file/' + id,
    method: 'get'
  })
}

// 新增网站文档
export function addSiteFile(data) {
  return request({
    url: '/site/file',
    method: 'post',
    data: data
  })
}

// 修改网站文档
export function updateSiteFile(data) {
  return request({
    url: '/site/file',
    method: 'put',
    data: data
  })
}

// 删除网站文档
export function delSiteFile(id) {
  return request({
    url: '/site/file/' + id,
    method: 'delete'
  })
}
