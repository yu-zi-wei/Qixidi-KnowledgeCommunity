import axios from 'axios';
import { createDiscreteApi } from 'naive-ui';
import type { Ref } from 'vue';
import type { UseFetchOptions } from 'nuxt/app';

const BASE_URL = 'http://127.0.0.1:0509/';
/**
 * @description: 服务端渲染请求
 * @param data 请求数据
 * @param options 请求配置
 */
export const ssrApi = <T>(
  data: { method: string; url: string; data?: any },
  options?: UseFetchOptions<T>
): { data: Ref<T | null>; error: Ref<Error | null>; refresh: () => Promise<void> } => {
  const store = localStorage.getItem('home');
  // @ts-ignore
  return useFetch(data.url, {
    ...options,
    baseURL: BASE_URL,
    timeout: 20000,
    method: data.method,
    headers: {
      Authorization: store && JSON.parse(store).token ? JSON.parse(store).token : 'null'
    },
    params: data?.data,
    $fetch: useNuxtApp().$api as typeof $fetch
  } as UseFetchOptions<T>);
};

/**
 * @description: 客户端请求
 */
const { notification } = createDiscreteApi(['notification']);
const { loadingBar } = createDiscreteApi(['loadingBar']);
const requests = axios.create({
  baseURL: BASE_URL,
  timeout: 20000
});

const errorTips = ['网络服务出错啦', '开发被外星人抓走了👾'];
/**
 * @description: 错误提示
 */
const errorNotification = () => {
  notification['error']({
    content: errorTips[0],
    meta: errorTips[1],
    duration: 2500,
    keepAliveOnHover: true
  });
};

requests.interceptors.request.use(
  (config: any) => {
    const store = localStorage.getItem('ego');
    if (store && JSON.parse(store).token) {
      config.headers.Authorization = JSON.parse(store).token;
    } else {
      config.headers.Authorization = 'null';
    }
    loadingBar.start();
    return config;
  },
  (error: any) => {
    errorNotification();
    loadingBar.error();
    setTimeout(() => loadingBar.finish());
    return Promise.reject(new Error(errorTips[0]));
  }
);

requests.interceptors.response.use(
  (response: any) => {
    loadingBar.finish();
    return response.data;
  },
  (error: any) => {
    loadingBar.error();
    setTimeout(() => loadingBar.finish());
    errorNotification();
    return error.request;
  }
);

export default requests;
