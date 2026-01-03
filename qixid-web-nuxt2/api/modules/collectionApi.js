export default ($axios) => ({
  // 增加收藏
  addCollection(data) {
    return $axios.post('/frontDesk/add/collection', data)
  },
  // 增加收藏数据
  addCollectionData(data) {
    return $axios.post('/frontDesk/add/collection/data', data)
  },
  // 删除收藏
  deleteCollection(id) {
    return $axios.delete(`/frontDesk/delete/collection/${id}`)
  },
  // 删除收藏数据
  deleteCollectionData(recordId, labelId) {
    return $axios.get(`/frontDesk/delete/collection/data/${recordId}/${labelId}`)
  },
  // 删除收藏记录数据 (仅根据recordId)
  deleteCollectionDataById(recordId) {
    return $axios.get(`/frontDesk/delete/collection/data/${recordId}`)
  },
  // 更新收藏数据
  updateCollectionData(data) {
    return $axios.put('/frontDesk/update/collection/data', data)
  }
})



