import { defineStore } from 'pinia';

export interface AdminInfo {
  userId: number | string;
  userName: string;
  nickName?: string;
  avatar?: string;
}

/**
 * 管理员会话 store
 * 管理后台全 CSR，无 SSR 水合问题，persist 用 localStorage 即可
 */
export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: '' as string,
    adminInfo: null as AdminInfo | null,
    /** 登录用户权限码集合（超管为 ['*'] 通配），驱动菜单/按钮可见性（usePerm） */
    permissions: [] as string[]
  }),
  actions: {
    saveToken(token: string) {
      this.token = token;
    },
    saveAdminInfo(info: AdminInfo) {
      this.adminInfo = info;
    },
    savePermissions(perms: string[]) {
      this.permissions = perms || [];
    },
    logout() {
      this.token = '';
      this.adminInfo = null;
      this.permissions = [];
    }
  },
  persist: process.client && {
    storage: localStorage
  }
});
