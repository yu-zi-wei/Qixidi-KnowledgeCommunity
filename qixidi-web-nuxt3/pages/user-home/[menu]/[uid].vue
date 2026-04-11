<template>
  <div class="user-home-page">
    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>
    <div v-else-if="!currentMenu" class="empty-state">
      <n-empty description="页面不存在" />
    </div>
    <div v-else class="menu-content">
      <!-- 根据 jurisdiction 判断是否需要登录 -->
      <div v-if="currentMenu.jurisdiction === 1 && !authStore.isLoggedIn" class="login-prompt">
        <p>登录后查看更多内容</p>
        <n-button type="primary" @click="authDialogStore.showLoginDialog(route.fullPath)">立即登录</n-button>
      </div>
      <!-- 占位：各菜单内容后续开发 -->
      <div v-else class="content-placeholder">
        <p class="placeholder-text">{{ currentMenu.navigationName }} - 内容开发中</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
definePageMeta({ layout: 'user-home' })

const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const navigationApi = useNavigationApi()

const menuRoute = computed(() => `/user-home/${route.params.menu}`)

// 获取菜单数据
const { data: menuList, pending } = await useAsyncData(
  'user-home-menu-page',
  async () => {
    const { rows } = await navigationApi.getList(2, 0)
    return rows || []
  }
)

const currentMenu = computed(() => {
  const list = menuList.value || []
  return list.find((m: any) => m.route === menuRoute.value)
})
</script>

<style scoped>
.user-home-page {
  min-height: 400px;
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.login-prompt {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 80px 0;
  color: var(--color-ink-muted);
}

.content-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.placeholder-text {
  color: var(--color-ink-muted);
  font-size: var(--text-base);
}
</style>
