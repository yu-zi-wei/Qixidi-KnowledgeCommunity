export default ($axios) => ({
  // 消息列表
  getNewsList(params) {
    return $axios.get('/frontDesk/news/list', { params })
  },
  // 标记消息已读
  readNews(params) {
    return $axios.get('/frontDesk/news/news-read', { params })
  },
  // 获取消息概要列表
  getNewsListInfo() {
    return $axios.get('/frontDesk/news/list/info')
  }
})



