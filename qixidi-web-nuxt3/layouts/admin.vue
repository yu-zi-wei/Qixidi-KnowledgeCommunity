<template>
  <div class="layout-admin">
    <AppHeader />
    <div class="admin-body">
      <aside class="admin-sidebar">
        <div class="sidebar-user" v-if="authStore.user">
          <n-avatar
            round
            :size="44"
            :src="authStore.user.avatar"
            fallback-src="/img/tx.jpg"
          />
          <div class="sidebar-user-info">
            <div class="sidebar-user-name">{{ authStore.user.nickname }}</div>
            <div class="sidebar-user-desc">{{ authStore.user.occupation || '' }}</div>
          </div>
        </div>
        <n-menu
          :value="activeMenu"
          :options="menuOptions"
          :root-indent="20"
          @update:value="handleMenuSelect"
        />
      </aside>
      <main class="admin-content">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, h } from 'vue'

const route = useRoute()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)

const menuOptions = [
  {
    label: '数据面板',
    key: '/admin',
    icon: () => h(resolveComponent('Icon'), { name: 'mdi:view-dashboard-outline', size: 20 })
  },
  {
    label: '内容管理',
    key: 'content',
    icon: () => h(resolveComponent('Icon'), { name: 'mdi:file-document-outline', size: 20 }),
    children: [
      { label: '专栏管理', key: '/admin/columns' },
      { label: '收藏夹管理', key: '/admin/favorites' }
    ]
  },
  {
    label: '阅读随笔',
    key: 'essays',
    icon: () => h(resolveComponent('Icon'), { name: 'mdi:book-open-page-variant-outline', size: 20 }),
    children: [
      { label: '专辑管理', key: '/admin/essays/albums' },
      { label: '随笔管理', key: '/admin/essays' }
    ]
  },
  {
    type: 'divider'
  },
  {
    label: '个人资料',
    key: '/admin/profile',
    icon: () => h(resolveComponent('Icon'), { name: 'mdi:account-outline', size: 20 })
  },
  {
    label: '账号设置',
    key: '/admin/settings',
    icon: () => h(resolveComponent('Icon'), { name: 'mdi:cog-outline', size: 20 })
  }
]

const handleMenuSelect = (key: string) => {
  navigateTo(key)
}
</script>

<style scoped>
.layout-admin {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.admin-body {
  flex: 1;
  display: flex;
  max-width: var(--max-width);
  width: 100%;
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
}

.admin-sidebar {
  width: 220px;
  flex-shrink: 0;
}

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 8px 24px;
}

.sidebar-user-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
}

.sidebar-user-desc {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin-top: 2px;
}

.admin-content {
  flex: 1;
  min-width: 0;
}
</style>
