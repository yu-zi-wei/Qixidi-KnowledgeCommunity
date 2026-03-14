import type { R } from '~/types'

/**
 * API 请求封装
 *
 * SSR 时使用 runtimeConfig.apiBase（服务端直连后端）
 * CSR 时使用 runtimeConfig.public.apiBase（通过 devProxy 代理）
 */
export const useApi = () => {
  const config = useRuntimeConfig()
  const baseURL = import.meta.server
    ? (config.apiBase as string)
    : (config.public.apiBase as string)

  const request = async <T = any>(
    url: string,
    options: Record<string, any> = {}
  ): Promise<T> => {
    try {
      // 注入 Authorization header
      const authStore = useAuthStore()
      const headers: Record<string, string> = {
        'Content-Type': 'application/json',
        ...options.headers
      }
      if (authStore.token) {
        headers['Authorization'] = `Bearer ${authStore.token}`
      }

      const response = await $fetch<T>(url, {
        baseURL,
        credentials: 'include',
        headers,
        ...options
      })

      const res = response as any
      if (res?.code !== undefined && res.code !== 200) {
        throw createError({
          statusCode: res.code,
          statusMessage: res.msg || '请求失败'
        })
      }

      return response
    } catch (error: any) {
      if (error.statusCode === 401) {
        const authStore = useAuthStore()
        authStore.logout()
        navigateTo('/login')
      }
      throw error
    }
  }

  const get = async <T = any>(url: string, params?: Record<string, any>) => {
    const res = await request<R<T>>(url, { method: 'GET', query: params })
    return res.data
  }

  const getPage = <T = any>(url: string, params?: Record<string, any>) => {
    return request<{ total: number; rows: T[] }>(url, { method: 'GET', query: params })
  }

  const post = async <T = any>(url: string, data?: any) => {
    const res = await request<R<T>>(url, {
      method: 'POST',
      body: data
    })
    return res.data
  }

  const put = async <T = any>(url: string, data?: any) => {
    const res = await request<R<T>>(url, {
      method: 'PUT',
      body: data
    })
    return res.data
  }

  const del = async <T = any>(url: string, params?: Record<string, any>) => {
    const res = await request<R<T>>(url, { method: 'DELETE', query: params })
    return res.data
  }

  return { request, get, getPage, post, put, delete: del }
}
