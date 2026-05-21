<template>
  <div class="layout-admin">
    <div class="admin-body">
      <aside class="admin-sidebar">
        <!-- 返回首页按钮 -->
        <div class="sidebar-header">
          <n-button quaternary class="back-home-btn" @click="goHome">
            <template #icon>
              <n-icon><Home /></n-icon>
            </template>
            返回首页
          </n-button>
        </div>

        <div class="sidebar-user" v-if="authStore.user">
          <n-avatar
            round
            :size="44"
            :src="authStore.user.avatar || '/images/default-avatar.svg'"
          />
          <div class="sidebar-user-info">
            <div class="sidebar-user-name">{{ authStore.user.nickname }}</div>
            <div class="sidebar-user-meta"><UserRoleBadge /><span v-if="authStore.user.occupation" class="sidebar-user-desc">{{ authStore.user.occupation }}</span></div>
          </div>
        </div>
        <n-menu
          :value="activeMenu"
          :options="menuOptions"
          :root-indent="20"
          :default-expanded-keys="defaultExpandedKeys"
          @update:value="handleMenuSelect"
        />
        <div v-if="!authStore.isCreator" class="sidebar-apply">
          <n-button type="primary" block round @click="handleApply">
            申请成为创作者
          </n-button>
        </div>
      </aside>
      <main class="admin-content">
        <slot />
      </main>
    </div>
    <CreatorApplyDialog />
  </div>
</template>

<script setup lang="ts">
import { computed, h } from 'vue'
import { Home } from '@vicons/tabler'
import type { MenuOption } from 'naive-ui'
import type { Sidebar } from '~/types'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const { getList: getSidebarList } = useSidebarApi()

// 未登录重定向到首页
if (!authStore.isLoggedIn) {
  await navigateTo('/')
}

const activeMenu = computed(() => route.path)

// 获取后台侧边栏菜单（type=2 表示后台管理）
const { data: sidebarData } = await useAsyncData(
  'admin-sidebar',
  () => getSidebarList(2, 0)
)

// 将 SVG 字符串转换为组件
const createIconComponent = (svgString: string) => {
  return () => h('div', {
    class: 'sidebar-icon',
    innerHTML: svgString
  })
}

// 递归获取所有父级菜单的 key（用于默认展开）
const getParentKeys = (items: Sidebar[]): string[] => {
  const keys: string[] = []
  items.forEach((item) => {
    if (item.levelList && item.levelList.length > 0) {
      keys.push(item.route || `menu-${item.id}`)
    }
  })
  return keys
}

// 递归构建菜单选项
const buildMenuOptions = (items: Sidebar[]): MenuOption[] => {
  return items.map((item) => {
    const menuItem: MenuOption = {
      label: item.sidebarName,
      key: item.route || `menu-${item.id}`,
      icon: item.sidebarIcon ? createIconComponent(item.sidebarIcon) : undefined
    }

    // 有子菜单
    if (item.levelList && item.levelList.length > 0) {
      menuItem.children = buildMenuOptions(item.levelList)
    }

    return menuItem
  })
}

// 默认展开的菜单 keys
const defaultExpandedKeys = computed<string[]>(() => {
  if (!sidebarData.value || sidebarData.value.length === 0) {
    return []
  }
  return getParentKeys(sidebarData.value)
})

// 菜单选项
const menuOptions = computed<MenuOption[]>(() => {
  if (!sidebarData.value || sidebarData.value.length === 0) {
    return []
  }
  return buildMenuOptions(sidebarData.value)
})

const handleMenuSelect = (key: string) => {
  if (key.startsWith('/')) {
    navigateTo(key)
  }
}

// 根据当前路由匹配侧边栏菜单名称，设置页面标题：菜单名称-创作者中心-栖息地
const currentMenuLabel = computed(() => {
  const path = route.path
  const findLabel = (items: Sidebar[]): string | undefined => {
    for (const item of items) {
      if (item.route === path) return item.sidebarName
      if (item.levelList?.length) {
        const found = findLabel(item.levelList)
        if (found) return found
      }
    }
  }
  if (sidebarData.value) {
    const label = findLabel(sidebarData.value)
    return label ? `${label}-创作者中心` : '创作者中心'
  }
  return '创作者中心'
})

useHead(() => ({
  title: currentMenuLabel.value,
  titleTemplate: '%s - 栖息地'
}))

// 返回首页
const goHome = () => {
  router.push('/')
}

const handleApply = () => {
  const creatorApplyStore = useCreatorApplyStore()
  creatorApplyStore.show()
}
</script>

<style scoped>
.layout-admin {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
  overflow: hidden;
}

.admin-body {
  flex: 1;
  display: flex;
  width: 100%;
  max-width: var(--max-width);
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
  min-height: 0;
  overflow: hidden;
}

.admin-sidebar {
  width: 220px;
  flex-shrink: 0;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 16px;
  box-shadow: var(--shadow-sm);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 12px;
}

.back-home-btn {
  width: 100%;
  justify-content: flex-start;
  color: var(--color-ink-muted);
  transition: color var(--transition-base);
}

.back-home-btn:hover {
  color: var(--color-primary);
}

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 8px 16px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 12px;
}

.sidebar-user-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
}

.sidebar-user-desc {
  font-size: 12px;
  color: var(--color-ink-muted);
}

.sidebar-user-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 4px;
}

.admin-content {
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.sidebar-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-icon :deep(svg) {
  width: 18px;
  height: 18px;
}

.sidebar-apply {
  margin-top: auto;
  padding-top: 16px;
}

/* 响应式 */
@media (max-width: 768px) {
  .admin-body {
    padding: 16px;
  }

  .admin-sidebar {
    width: 200px;
  }
}
</style>
