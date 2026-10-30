import type { AdminInfo } from './useAdminStore';

/** GET /captchaImage 响应（captchaOnOff=false 时无 uuid/img） */
export interface CaptchaData {
  captchaOnOff?: boolean;
  uuid?: string;
  /** hutool base64，无 data: 前缀，展示需自行拼接 */
  img?: string;
}

export interface LoginBody {
  username: string;
  password: string;
  code?: string;
  uuid?: string;
}

/** GET /getInfo 响应（permissions 为 RBAC 权限码集合，超管 ['*']；roles 已无消费方） */
export interface AdminInfoData {
  user: AdminInfo & Record<string, any>;
  roles: string[];
  permissions: string[];
}

/**
 * 管理员认证接口（qixidi-service SysLoginController / CaptchaController）
 */
export const useAuthApi = () => {
  const api = useApi();

  return {
    getCaptcha: () => api.get<CaptchaData>('/captchaImage'),
    login: (data: LoginBody) => api.post<{ token: string }>('/login', data),
    getAdminInfo: () => api.get<AdminInfoData>('/getInfo'),
    logout: () => api.post<void>('/logout')
  };
};
