export default ($axios) => ({
  // 搜索记录列表
  getSearchRecordsList(params) {
    return $axios.get('/frontDesk/search/records/list', { params })
  }
})



