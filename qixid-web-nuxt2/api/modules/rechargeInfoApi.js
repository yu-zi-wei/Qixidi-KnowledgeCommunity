export default ($axios) => ({
  // 充值信息列表
  getRechargeInfoList() {
    return $axios.get('/frontDesk/rechargeInfo/list')
  }
})



