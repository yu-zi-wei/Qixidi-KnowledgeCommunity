import type { LoginForm, LoginResult, RegisterForm, GetInfoResult } from '~/types'

const toBase64 = (str: string) => btoa(unescape(encodeURIComponent(str)))

export const useAuthApi = () => {
  const api = useApi()

  const login = (form: LoginForm) => {
    return api.post<LoginResult>('/oauth/frontDesk/login', {
      username: toBase64(form.username),
      password: toBase64(form.password)
    })
  }

  const register = (form: RegisterForm) => {
    return api.post<void>('/oauth/frontDesk/register', {
      ...form,
      email: toBase64(form.email),
      password: toBase64(form.password)
    })
  }

  const resetPassword = (form: RegisterForm) => {
    return api.post<void>('/oauth/reset/password', {
      ...form,
      email: toBase64(form.email),
      password: toBase64(form.password)
    })
  }

  const sendEmailCode = (email: string, type: number) => {
    return api.get<void>(`/oauth/email/code/${email}/${type}`)
  }

  const getInfo = () => {
    return api.get<GetInfoResult>('/oauth/getInfo')
  }

  const logout = () => {
    return api.post<void>('/oauth/logout')
  }

  return { login, register, resetPassword, sendEmailCode, getInfo, logout }
}
