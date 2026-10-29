import request from '@/utils/request'

// 查询用户浏览历史列表
export function listHistory(query) {
  return request({
    url: '/browsing/history/list',
    method: 'get',
    params: query
  })
}

// 查询用户浏览历史详细
export function getHistory(id) {
  return request({
    url: '/browsing/history/' + id,
    method: 'get'
  })
}

// 新增用户浏览历史
export function addHistory(data) {
  return request({
    url: '/browsing/history',
    method: 'post',
    data: data
  })
}

// 修改用户浏览历史
export function updateHistory(data) {
  return request({
    url: '/browsing/history',
    method: 'put',
    data: data
  })
}

// 删除用户浏览历史
export function delHistory(id) {
  return request({
    url: '/browsing/history/' + id,
    method: 'delete'
  })
}
