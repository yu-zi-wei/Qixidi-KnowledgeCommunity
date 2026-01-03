export default ($axios) => ({
  // 获取评论详情 (仅出现一次的接口示例)
  getCommentById(id) {
    return $axios.get(`/get/comment/${id}`)
  },
  // 检查用户是否在�?
  checkUserOnline(uuid) {
    return $axios.get(`/websocket/is-online/${uuid}`)
  },
  // OSS上传
  uploadOss(data) {
    return $axios.post('/system/oss/upload', data)
  }
})


