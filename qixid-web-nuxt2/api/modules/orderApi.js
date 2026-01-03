export default ($axios) => ({
  // 删除订单
  deleteOrder(id) {
    return $axios.delete(`/frontDesk/order/delete/${id}`)
  },
  // 订单列表
  getOrderList(params) {
    return $axios.get('/frontDesk/order/list', { params })
  }
})



