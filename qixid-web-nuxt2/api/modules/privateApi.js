export default ($axios) => ({
  // 私信用户增加
  addPrivateUser(uuid) {
    return $axios.get(`/frontDesk/private/user/add/${uuid}`)
  },
  // 清空私信用户
  deleteAllPrivateUser() {
    return $axios.delete('/frontDesk/private/user/all')
  },
  // 私信用户列表
  getPrivateUserList(params) {
    return $axios.get('/frontDesk/private/user/list', { params })
  },
  // 删除私信用户
  deletePrivateUser(uuid) {
    return $axios.delete(`/frontDesk/private/user/${uuid}`)
  },
  // 私信消息已读
  readPrivateNews(uuid) {
    return $axios.get(`/frontDesk/private/newsInfo/been/read/${uuid}`)
  },
  // 私信消息列表
  getPrivateNewsList(params) {
    return $axios.get('/frontDesk/private/newsInfo/list', { params })
  },
  // 发送私信消�?
  sendPrivateNews(data) {
    return $axios.post('/frontDesk/private/newsInfo', data)
  }
})



