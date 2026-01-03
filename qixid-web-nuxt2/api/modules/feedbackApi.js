export default ($axios) => ({
  // 反馈删除
  deleteFeedback(id) {
    return $axios.delete(`/frontDesk/feedback/delete/${id}`)
  },
  // 反馈增加
  addFeedback(data) {
    return $axios.post('/frontDesk/feedback/add', data)
  },
  // 反馈状态更�?
  updateFeedbackStatus(id, status) {
    return $axios.get(`/frontDesk/feedback/update/status/${id}/${status}`)
  },
  // 反馈更新
  updateFeedback(data) {
    return $axios.post('/frontDesk/feedback/update', data)
  }
})



