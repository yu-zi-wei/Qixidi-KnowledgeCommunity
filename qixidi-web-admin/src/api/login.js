import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 后台退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 前台退出方法
export function oauthLogout() {
  return request({
    url: '/oauth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

//前台登录
export function LoginGitee(type) {
  return request({
    url: `/oauth/render/${type}`,
    headers: {
      isToken: true
    },
    method: 'post',
    timeout: 20000
  })
}

//前台判断是否登录
export function isLogin() {
  return request({
    url: `/oauth/isLogin`,
    headers: {
      isToken: true
    },
    method: 'get',
    timeout: 20000
  })
}
