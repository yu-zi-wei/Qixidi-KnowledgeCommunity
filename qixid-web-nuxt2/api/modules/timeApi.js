export default ($axios) => ({
  // 删除时间�?
  deleteTimeNotes(id) {
    return $axios.get(`/frontDesk/time/notes/delete/${id}`)
  },
  // 时间轴列�?
  getTimeNotesList(data) {
    return $axios.post('/frontDesk/time/notes/list', data)
  },
  // 更新时间�?
  updateTimeNotes(data) {
    return $axios.post('/frontDesk/time/notes/update', data)
  },
  // 添加时间�?
  addTimeNotes(data) {
    return $axios.post('/frontDesk/time/notes/add', data)
  }
})



