import request from '@/utils/request'

//刷新导航栏配置
export function cacheNavigation() {
  return request({
    url: '/business/cache/refresh/navigation',
    method: 'get',
  })
}

//刷新侧边栏配置
export function cacheSidebar() {
  return request({
    url: '/business/cache/refresh/sidebar',
    method: 'get',
  })
}

//刷新屏蔽词缓存
export function cacheBlockingWords() {
  return request({
    url: '/business/cache/refresh/blocking-words',
    method: 'get',
  })
}

//测试屏蔽词
export function cacheBlockingWordsText(text) {
  return request({
    url: '/business/cache/refresh/blocking-words/text/' + text,
    method: 'get',
  })
}
