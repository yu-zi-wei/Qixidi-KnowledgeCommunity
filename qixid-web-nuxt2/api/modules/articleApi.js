export default ($axios) => ({
  // 获取已选择的文�?(之前已经添加)
  getSelectedArticles() {
    return $axios.get('/white/article/selected')
  },
  // 删除评论
  deleteComment(data) {
    return $axios.post('/article/delete/comment', data)
  },
  // 插入评论
  insertComment(data) {
    return $axios.post('/article/comment/insert', data)
  }
})


