export {};
declare global {
  // 示例：接口返回数据
  interface Result {
    code: number;
    data: any;
    msg: string;
  }

  // ... 自定义类型
}

import { AxiosRequestConfig } from 'axios';

declare module 'axios' {
  interface AxiosInstance {
    (config: AxiosRequestConfig): Promise<any>;
  }
}
