export default ($axios) => ({
  // 浏览历史列表
  getBrowsingHistoryList(params) {
    return $axios.get('/frontDesk/browsing/history/list', { params })
  },
  // 增加浏览历史
  addBrowsingHistory(data) {
    return $axios.post('/frontDesk/browsing/history', data)
  }
})



