export default ($axios) => ({
  // 语录评论删除
  deleteDictumComment(id) {
    return $axios.delete(`/frontDesk/dictum/comment/delete/${id}`)
  },
  // 语录评论增加
  addDictumComment(data) {
    return $axios.post('/frontDesk/dictum/comment/add', data)
  },
  // 语录详情删除
  deleteDictumInfo(id, groupId) {
    return $axios.delete(`/frontDesk/dictum/info/${id}/${groupId}`)
  },
  // 语录相册增加
  addDictumAlbum(data) {
    return $axios.post('/frontDesk/dictum/album', data)
  },
  // 更新语录相册
  updateDictumAlbum(data) {
    return $axios.put('/frontDesk/dictum/album', data)
  },
  // 删除语录相册
  deleteDictumAlbum(id) {
    return $axios.delete(`/frontDesk/dictum/album/${id}`)
  },
  // 语录信息增加
  addDictumInfo(data) {
    return $axios.post('/frontDesk/dictum/info', data)
  },
  // 语录相册角色列表
  getDictumAlbumRoleList() {
    return $axios.get('/frontDesk/dictum/album/role/list')
  },
  // 语录详情
  getDictumDetails(id) {
    return $axios.get(`/frontDesk/dictum/info/${id}`)
  },
  // 语录角色信息列表
  getDictumInfoRoleList(params) {
    return $axios.get('/frontDesk/dictum/info/role/list', { params })
  }
})



