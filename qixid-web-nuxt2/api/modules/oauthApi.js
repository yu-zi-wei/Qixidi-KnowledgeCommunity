export default ($axios) => ({
  // 退出登登录
  logout() {
    return $axios.post('/oauth/logout')
  },
  // 获取登录信息
  getInfo() {
    return $axios.get('/oauth/getInfo')
  },
  // 是否登录
  isLogin() {
    return $axios.get('/oauth/isLogin')
  },
  // 账号注销
  cancelAccount() {
    return $axios.get('/oauth/account/cancellation')
  },
  // 重置密码
  resetPassword(data) {
    return $axios.post('/oauth/reset/password', data)
  },
  // 获取手机验证码
  getPhoneCode(phone, type) {
    return $axios.get(`/oauth/phone/code/${phone}/${type}`)
  },
  // 获取邮箱验证码
  getEmailCode(email, type) {
    return $axios.get(`/oauth/email/code/${email}/${type}`)
  },
  // 前端注册
  register(data) {
    return $axios.post('/oauth/front-desk/register', data)
  },
  // 前端登录
  login(data) {
    return $axios.post('/oauth/front-desk/login', data)
  },
  // 第三方登录类型
  renderAuth(type) {
    return $axios.post(`/oauth/render/${type}`)
  }
})


