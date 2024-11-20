import request from '@/utils/request'

// 查询爱情轮播图列表
export function listCarousel(query) {
  return request({
    url: '/lover/carousel/list',
    method: 'get',
    params: query
  })
}

// 查询爱情轮播图详细
export function getCarousel(id) {
  return request({
    url: '/lover/carousel/' + id,
    method: 'get'
  })
}

// 新增爱情轮播图
export function addCarousel(data) {
  return request({
    url: '/lover/carousel',
    method: 'post',
    data: data
  })
}

// 修改爱情轮播图
export function updateCarousel(data) {
  return request({
    url: '/lover/carousel',
    method: 'put',
    data: data
  })
}

// 删除爱情轮播图
export function delCarousel(id) {
  return request({
    url: '/lover/carousel/' + id,
    method: 'delete'
  })
}
