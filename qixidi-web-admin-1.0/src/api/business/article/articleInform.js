import request from '@/utils/request'

// 查询文章信息列表
export function listInformation(query) {
  return request({
    url: '/business/article/information/list',
    method: 'get',
    params: query
  })
}

// 查询文章信息详细
export function getInformation(id) {
  return request({
    url: '/business/article/information/' + id,
    method: 'get'
  })
}

// 新增文章信息
export function addInformation(data) {
  return request({
    url: '/business/article/information',
    method: 'post',
    data: data
  })
}

// 修改文章信息
export function updateInformation(data) {
  return request({
    url: '/business/article/information',
    method: 'put',
    data: data
  })
}

// 删除文章信息
export function delInformation(id) {
  return request({
    url: '/business/article/information/' + id,
    method: 'delete'
  })
}

// 上传图片
export function fileUpload(data) {
  return request({
    url: '/system/oss/upload',
    method: 'post',
    data: data
  })
}
