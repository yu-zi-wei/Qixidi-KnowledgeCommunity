<template>
  <div class="transfer-page">
    <div class="transfer-card">
      <!-- 平台标识：识别到第三方平台则显示平台图标，否则显示站点 logo -->
      <div class="platform-badge">
        <div v-if="platform" class="platform-icon-wrap" :style="{ color: platform.color }">
          <svg viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path :d="platform.svgPath" />
          </svg>
        </div>
        <img v-else src="/images/logo.svg" alt="栖息地" class="transfer-logo" />
      </div>

      <div class="transfer-spin">
        <n-spin v-if="status === 'loading'" size="large" />
        <div v-else class="transfer-status-icon" :class="status">
          <svg v-if="status === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <polyline points="20 6 9 17 4 12"></polyline>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
          </svg>
        </div>
      </div>
      <p class="transfer-text">{{ statusText }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
definePageMeta({ layout: 'blank' })

const route = useRoute()
const authStore = useAuthStore()
const message = useMessage()

const REDIRECT_STORAGE_KEY = 'oauth_redirect_target'

// 第三方平台映射（source 来自后端 callback redirect 的 query 参数）
// 新增平台时在此扩展即可
const PLATFORM_MAP: Record<string, { name: string; color: string; svgPath: string }> = {
  gitee: {
    name: 'Gitee',
    color: '#c71d23',
    svgPath: 'M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12A12 12 0 0 0 24 12 12 12 0 0 0 12 0a12 12 0 0 0-.016 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.982 0 1.778-.796 1.778-1.778v-.296a.594.594 0 0 0-.592-.593h-4.15a.594.594 0 0 1-.592-.592v-1.482a.594.594 0 0 1 .593-.592h6.815c.327 0 .593.265.593.592v3.408a4.15 4.15 0 0 1-4.148 4.15H6.074a.594.594 0 0 1-.593-.593V9.333a4.074 4.074 0 0 1 4.074-4.074h8.519z'
  }
}

const source = computed(() => ((route.query.source as string) || '').toLowerCase())
const platform = computed(() => PLATFORM_MAP[source.value] || null)

type Status = 'loading' | 'success' | 'error'
const status = ref<Status>('loading')
const statusText = computed(() => {
  const name = platform.value?.name || ''
  if (status.value === 'loading') {
    return name ? `正在使用${name}登录，请稍候...` : '正在登录，请稍候...'
  }
  if (status.value === 'success') return '登录成功，即将跳转...'
  return name ? `${name}登录失败，即将返回首页` : '登录失败，即将返回首页'
})

onMounted(async () => {
  const token = route.query.token as string | undefined

  // 缺少 token：可能是误访问或回调异常
  if (!token) {
    status.value = 'error'
    setTimeout(() => navigateTo('/'), 2000)
    return
  }

  try {
    authStore.setToken(token)
    await authStore.fetchUser()

    if (!authStore.isLoggedIn) {
      throw new Error('登录状态获取失败')
    }

    status.value = 'success'
    message.success('登录成功')

    // 读取并清除 OAuth 前存储的跳转目标（整页跳转会丢失内存状态，故用 sessionStorage）
    const redirect = sessionStorage.getItem(REDIRECT_STORAGE_KEY)
    sessionStorage.removeItem(REDIRECT_STORAGE_KEY)

    setTimeout(() => {
      navigateTo(redirect || '/')
    }, 500)
  } catch (e) {
    status.value = 'error'
    authStore.logout()
    setTimeout(() => navigateTo('/'), 2000)
  }
})
</script>

<style scoped>
.transfer-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.transfer-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding: 48px 56px;
  background: var(--color-surface);
  border-radius: 20px;
  box-shadow: var(--shadow-lg);
  max-width: 90vw;
}

.platform-badge {
  display: flex;
  align-items: center;
  justify-content: center;
}

.transfer-logo {
  width: 56px;
  height: 56px;
  object-fit: contain;
}

.platform-icon-wrap {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
}

.platform-icon-wrap svg {
  width: 36px;
  height: 36px;
}

.transfer-spin {
  min-height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.transfer-status-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.transfer-status-icon svg {
  width: 24px;
  height: 24px;
}

.transfer-status-icon.success {
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.transfer-status-icon.error {
  color: var(--color-danger);
  background: rgba(220, 38, 38, 0.1);
}

.transfer-text {
  font-size: 15px;
  color: var(--color-ink-light);
  margin: 0;
  text-align: center;
}
</style>
