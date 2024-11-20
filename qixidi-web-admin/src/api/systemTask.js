import request from '@/utils/request'

// 获取路由
export const systemTaskList = () => {
  return request({
    url: '/system/task/list',
    method: 'get'
  })
}
