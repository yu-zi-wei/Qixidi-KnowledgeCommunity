import request from '@/utils/request'

// 查询充值信息列表
export function listRechargeInfo(query) {
  return request({
    url: '/business/rechargeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询充值信息详细
export function getRechargeInfo(id) {
  return request({
    url: '/business/rechargeInfo/' + id,
    method: 'get'
  })
}

// 新增充值信息
export function addRechargeInfo(data) {
  return request({
    url: '/business/rechargeInfo',
    method: 'post',
    data: data
  })
}

// 修改充值信息
export function updateRechargeInfo(data) {
  return request({
    url: '/business/rechargeInfo',
    method: 'put',
    data: data
  })
}

// 删除充值信息
export function delRechargeInfo(id) {
  return request({
    url: '/business/rechargeInfo/' + id,
    method: 'delete'
  })
}
