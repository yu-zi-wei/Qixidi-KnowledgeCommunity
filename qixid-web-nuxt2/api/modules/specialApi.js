export default ($axios) => ({
  // 新增专栏
  addSpecial(data) {
    return $axios.post('/special/information', data)
  },
  // 更新专栏
  updateSpecial(data) {
    return $axios.put('/special/information', data)
  }
})



