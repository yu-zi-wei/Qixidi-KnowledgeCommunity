import request from '@/utils/request'

// 查询屏蔽词库列表
export function listShieldWord(query) {
  return request({
    url: '/business/shield/shieldWord/list',
    method: 'get',
    params: query
  })
}

// 查询屏蔽词库详细
export function getShieldWord(id) {
  return request({
    url: '/business/shield/shieldWord/' + id,
    method: 'get'
  })
}

// 新增屏蔽词库
export function addShieldWord(data) {
  return request({
    url: '/business/shield/shieldWord',
    method: 'post',
    data: data
  })
}

// 修改屏蔽词库
export function updateShieldWord(data) {
  return request({
    url: '/business/shield/shieldWord',
    method: 'put',
    data: data
  })
}

// 删除屏蔽词库
export function delShieldWord(id) {
  return request({
    url: '/business/shield/shieldWord/' + id,
    method: 'delete'
  })
}
