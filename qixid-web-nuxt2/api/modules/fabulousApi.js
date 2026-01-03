export default ($axios) => ({
  // 点赞列表
  getFabulousList(params) {
    return $axios.get('/frontDesk/fabulous/fabulous/list', { params })
  },
  // 取消点赞
  cancelFabulous(data) {
    return $axios.post('/frontDesk/fabulous/cancel', data)
  },
  // 点赞
  spotFabulous(data) {
    return $axios.post('/frontDesk/fabulous/spot', data)
  }
})



