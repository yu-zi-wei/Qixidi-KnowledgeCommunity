/**
 * 个人设置相关 API
 */

export const useSettingsApi = () => {
  const api = useApi()

  /** 获取当前用户信息 */
  const getUserInfo = () => {
    return api.get<any>('/frontDesk/user/info')
  }

  /** 更新个人信息 */
  const updateUserInfo = (data: {
    uuid: string
    nickname: string
    avatar?: string
    occupation?: string
    company?: string
    homepage?: string
    introduce?: string
  }) => {
    return api.put('/frontDesk/user/update/info', data)
  }

  /** 绑定/换绑邮箱 */
  const bindEmail = (data: {
    type: number // 1:绑定邮箱 2:换绑邮箱
    email: string
    code: string
  }) => {
    return api.put('/frontDesk/user/update/email', data)
  }

  /** 发送邮箱验证码 (type: 3=绑定邮箱, 2=重置密码) */
  const sendEmailCode = (email: string, type: number) => {
    return api.get(`/oauth/email/code/${encodeURIComponent(email)}/${type}`)
  }

  /** 重置密码（字段需 Base64 编码） */
  const resetPassword = (data: {
    registerType: number
    email: string
    password: string
    phone: string
    code: string
  }) => {
    return api.post('/oauth/reset/password', {
      ...data,
      email: btoa(data.email),
      password: btoa(data.password),
      phone: btoa(data.phone),
      code: btoa(data.code)
    })
  }

  /** 账号注销 */
  const cancelAccount = () => {
    return api.get('/oauth/account/cancellation')
  }

  return { getUserInfo, updateUserInfo, bindEmail, sendEmailCode, resetPassword, cancelAccount }
}
