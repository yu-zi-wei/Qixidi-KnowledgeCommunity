import request from '@/utils/request'

// 查询爱情清单列表
export function listRepertoire(query) {
  return request({
    url: '/lover/repertoire/list',
    method: 'get',
    params: query
  })
}

// 查询爱情清单详细
export function getRepertoire(id) {
  return request({
    url: '/lover/repertoire/' + id,
    method: 'get'
  })
}

// 新增爱情清单
export function addRepertoire(data) {
  return request({
    url: '/lover/repertoire',
    method: 'post',
    data: data
  })
}

// 修改爱情清单
export function updateRepertoire(data) {
  return request({
    url: '/lover/repertoire',
    method: 'put',
    data: data
  })
}

// 删除爱情清单
export function delRepertoire(id) {
  return request({
    url: '/lover/repertoire/' + id,
    method: 'delete'
  })
}
