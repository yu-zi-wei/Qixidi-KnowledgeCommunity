<template>
  <n-layout has-sider class="h-screen">
    <n-layout-sider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="220"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="logo flex items-center justify-center gap-2" :style="{ height: '56px' }">
        <span v-if="!collapsed" class="font-bold text-base">栖息地后台</span>
        <span v-else class="font-bold">栖</span>
      </div>
      <n-menu
        :collapsed="collapsed"
        :collapsed-width="64"
        :options="menuOptions"
        :value="activeMenu"
        @update:value="handleMenuSelect"
      />
    </n-layout-sider>

    <n-layout>
      <n-layout-header bordered class="header flex items-center justify-between px-5" :style="{ height: '56px' }">
        <div class="text-sm opacity-60">qixidi · 管理后台</div>
        <n-space align="center">
          <n-button quaternary circle @click="toggleTheme">
            <template #icon>
              <n-icon><component :is="isDark ? Sun : MoonStars" /></n-icon>
            </template>
          </n-button>
          <n-dropdown :options="userDropdown" @select="handleUserAction">
            <n-button quaternary>
              {{ adminStore.adminInfo?.nickName || adminStore.adminInfo?.userName || '管理员' }}
            </n-button>
          </n-dropdown>
        </n-space>
      </n-layout-header>

      <n-layout-content content-style="padding: 20px;" class="content">
        <slot />
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup lang="ts">
import { h } from 'vue';
import type { MenuOption } from 'naive-ui';
import { NIcon } from 'naive-ui';
import {
  Dashboard,
  FileText,
  MessageCircle,
  Users,
  Settings,
  MoonStars,
  Sun,
  Logout
} from '@vicons/tabler';

const collapsed = ref(false);
const route = useRoute();
const router = useRouter();
const adminStore = useAdminStore();

const switchTheme = inject<(style: any) => void>('switchTheme')!;
const isDark = ref(false);
const toggleTheme = () => {
  isDark.value = !isDark.value;
  switchTheme(isDark.value ? darkTheme : null);
};

// 菜单图标渲染
const renderIcon = (icon: Component) => () =>
  h(NIcon, null, { default: () => h(icon) });

const menuOptions: MenuOption[] = [
  { label: '数据看板', key: '/', icon: renderIcon(Dashboard) },
  { label: '文章管理', key: '/article', icon: renderIcon(FileText) },
  { label: '评论管理', key: '/comment', icon: renderIcon(MessageCircle) },
  { label: '前台用户', key: '/user', icon: renderIcon(Users) },
  { label: '系统设置', key: '/setting', icon: renderIcon(Settings) }
];

const activeMenu = computed(() => route.path);

const handleMenuSelect = (key: string) => {
  router.push(key);
};

const userDropdown = [
  { label: '退出登录', key: 'logout', icon: renderIcon(Logout) }
];

const handleUserAction = (key: string) => {
  if (key === 'logout') {
    adminStore.logout();
    router.push('/login');
  }
};
</script>

<style scoped lang="scss">
.logo {
  border-bottom: 1px solid rgba(128, 128, 128, 0.15);
}

.content {
  height: calc(100vh - 56px);
}
</style>
