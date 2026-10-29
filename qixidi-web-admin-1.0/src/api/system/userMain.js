import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listUser(query) {
  return request({
    url: '/system/tripartite/user/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getUser(uuid) {
  return request({
    url: '/system/tripartite/user/' + uuid,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addUser(data) {
  return request({
    url: '/system/tripartite/user',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateUser(data) {
  return request({
    url: '/system/tripartite/user',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delUser(uuid) {
  return request({
    url: '/system/tripartite/user/' + uuid,
    method: 'delete'
  })
}
