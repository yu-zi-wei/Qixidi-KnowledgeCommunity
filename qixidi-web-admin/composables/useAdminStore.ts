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
    adminInfo: null as AdminInfo | null
  }),
  actions: {
    saveToken(token: string) {
      this.token = token;
    },
    saveAdminInfo(info: AdminInfo) {
      this.adminInfo = info;
    },
    logout() {
      this.token = '';
      this.adminInfo = null;
    }
  },
  persist: process.client && {
    storage: localStorage
  }
});
