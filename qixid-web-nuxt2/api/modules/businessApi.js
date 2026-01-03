export default ($axios) => ({
  // 分组信息列表
  getGroupingInfoList() {
    return $axios.get('/business/groupingInfo/list')
  },
  // 标签信息列表
  getLabelInfoList() {
    return $axios.get('/business/label-info/list')
  },
  // 收藏信息更新
  updateCollectionInformation(data) {
    return $axios.put('/business/collection/information', data)
  }
})



