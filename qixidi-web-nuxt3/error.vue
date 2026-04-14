<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-code">{{ error?.statusCode || 404 }}</div>
      <div class="error-divider"></div>
      <div class="error-body">
        <h1 class="error-title">{{ title }}</h1>
        <p class="error-desc">{{ description }}</p>
        <div class="error-actions">
          <n-button type="primary" @click="handleGoHome">回到首页</n-button>
          <n-button @click="handleGoBack">返回上页</n-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Home, ArrowLeft } from '@vicons/tabler'

const { siteName } = useRuntimeConfig().public

const props = defineProps<{
  error: {
    statusCode?: number
    statusMessage?: string
    message?: string
    url?: string
  }
}>()

const title = computed(() => {
  const code = props.error?.statusCode
  if (code === 404) return '页面走丢了'
  if (code === 500) return '服务器开小差了'
  return '出错了'
})

const description = computed(() => {
  const code = props.error?.statusCode
  if (code === 404) return '你访问的页面不存在，可能已被移动或删除'
  if (code === 500) return '服务器遇到了一些问题，请稍后再试'
  return props.error?.statusMessage || '发生了一些意料之外的情况'
})

const handleGoHome = () => clearError({ redirect: '/' })
const handleGoBack = () => {
  if (window.history.length > 1) {
    clearError()
    window.history.back()
  } else {
    clearError({ redirect: '/' })
  }
}

useHead({
  title: `${props.error?.statusCode || 404} - ${siteName}`
})
</script>

<style scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-warm);
  padding: 24px;
}

.error-content {
  display: flex;
  align-items: center;
  gap: 48px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 48px 56px;
  box-shadow: var(--shadow-md);
}

.error-code {
  font-size: 96px;
  font-weight: 800;
  line-height: 1;
  color: var(--color-primary);
  opacity: 0.25;
  font-family: var(--font-display);
  user-select: none;
}

.error-divider {
  width: 1px;
  height: 80px;
  background: var(--color-border-light);
}

.error-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.error-title {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
}

.error-desc {
  font-size: var(--text-base);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
}

.error-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

@media (max-width: 640px) {
  .error-content {
    flex-direction: column;
    text-align: center;
    padding: 32px 24px;
    gap: 24px;
  }

  .error-code {
    font-size: 64px;
  }

  .error-divider {
    width: 60px;
    height: 1px;
  }

  .error-actions {
    justify-content: center;
  }
}
</style>
