import { createDiscreteApi } from 'naive-ui';

const { message: discreteMessage } = createDiscreteApi(['message']);

/**
 * 会话失效判定（后端 SaTokenConfig 把拦截器里的未登录异常包装成 ServiceException，
 * 业务码是 500 而非 401，故除 401 外还需识别固定 msg；M3 后端瘦身后可收敛为仅判 401）
 */
const isSessionInvalid = (error: any) =>
  error?.statusCode === 401 || String(error?.statusMessage || '').includes('登录失效');

/**
 * API 请求封装（qixidi-service 对接，与前台 qixidi-web-nuxt3 同款语义）
 *
 * 管理后台全 CSR，统一走 public.apiBase（dev 经 devProxy / 生产经 nginx 反代）
 * - 非分页 { code, msg, data }：get/post/put/delete 自动解包 data
 * - 分页 { total, rows }：getPage/postPage 原样返回
 * - 错误统一弹后端 msg；会话失效（401/登录失效）清会话跳登录页
 */
export const useApi = () => {
  const config = useRuntimeConfig();
  const baseURL = config.public.apiBase as string;

  const request = async <T = any>(
    url: string,
    options: Record<string, any> = {}
  ): Promise<T> => {
    try {
      const adminStore = useAdminStore();
      const headers: Record<string, string> = {
        'Content-Type': 'application/json',
        ...options.headers
      };
      if (adminStore.token) {
        headers['Authorization'] = `Bearer ${adminStore.token}`;
      }

      const response = await $fetch<T>(url, {
        baseURL,
        headers,
        ...options
      });

      const res = response as any;
      if (res?.code !== undefined && res.code !== 200) {
        throw createError({
          statusCode: res.code,
          statusMessage: res.msg || '请求失败'
        });
      }

      return response;
    } catch (error: any) {
      // 客户端统一弹出后端错误信息
      if (import.meta.client && error.statusMessage) {
        discreteMessage.error(error.statusMessage);
      }
      if (isSessionInvalid(error)) {
        const adminStore = useAdminStore();
        adminStore.logout();
        navigateTo('/login');
      }
      throw error;
    }
  };

  const get = async <T = any>(url: string, params?: Record<string, any>) => {
    const res = await request<R<T>>(url, { method: 'GET', query: params });
    return res.data;
  };

  const getPage = <T = any>(url: string, params?: Record<string, any>) => {
    return request<PageResult<T>>(url, { method: 'GET', query: params });
  };

  const postPage = <T = any>(url: string, data?: Record<string, any>) => {
    return request<PageResult<T>>(url, { method: 'POST', body: data });
  };

  const post = async <T = any>(url: string, data?: any) => {
    const res = await request<R<T>>(url, { method: 'POST', body: data });
    return res.data;
  };

  const put = async <T = any>(url: string, data?: any) => {
    const res = await request<R<T>>(url, { method: 'PUT', body: data });
    return res.data;
  };

  const del = async <T = any>(url: string, params?: Record<string, any>) => {
    const res = await request<R<T>>(url, { method: 'DELETE', query: params });
    return res.data;
  };

  return { request, get, getPage, postPage, post, put, delete: del };
};
