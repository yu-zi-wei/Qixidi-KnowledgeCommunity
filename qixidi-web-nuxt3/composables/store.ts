import { defineStore } from 'pinia';

// @ts-ignore
export const useStore = defineStore('home', {
  state: () => {
    return {
      token: '' as string
    };
  },
  actions: {
    clearToken(): void {
      this.token = '';
    },
    saveToken(token: string): void {
      this.token = token;
    }
  },
  persist: process.client && {
    storage: localStorage
  }
});
