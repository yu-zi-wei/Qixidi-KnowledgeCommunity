import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo } from '~/types'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(null)
  const user = ref<UserInfo | null>(null)

  const isLoggedIn = computed(() => !!token.value && !!user.value)

  const isCreator = computed(() => {
    const rid = user.value?.roleId
    return rid === 2 || rid === 3
  })

  const setToken = (val: string) => {
    token.value = val
  }

  const setUser = (val: UserInfo) => {
    user.value = val
  }

  const logout = () => {
    token.value = null
    user.value = null
  }

  /** 通过 token 获取用户信息 */
  const fetchUser = async () => {
    if (!token.value) return
    try {
      const authApi = useAuthApi()
      const info = await authApi.getInfo()
      if (info?.isLogin && info.user) {
        user.value = info.user
      } else {
        logout()
      }
    } catch {
      logout()
    }
  }

  return { token, user, isLoggedIn, isCreator, setToken, setUser, logout, fetchUser }
}, {
  persist: true
})
