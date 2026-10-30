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
        <AdminBreadcrumb />
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

      <!-- 内容区底色：浅灰衬托白卡片（dark 下用主题暗底，卡片色由 n-card 随主题自带） -->
      <n-layout-content content-style="padding: 20px;" class="content" :style="{ background: isDark ? '#101014' : '#f5f6f8' }">
        <slot />
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup lang="ts">
import { h } from 'vue';
import type { MenuOption } from 'naive-ui';
import { darkTheme, NIcon } from 'naive-ui';
import type { GlobalTheme } from 'naive-ui';
import { MoonStars, Sun, Logout } from '@vicons/tabler';
import { ADMIN_MENU } from '~/composables/useAdminMenu';

const collapsed = ref(false);
const route = useRoute();
const router = useRouter();
const adminStore = useAdminStore();

// 主题唯一数据源在 app.vue（theme ref），这里只派生与派发，避免双份状态
const theme = inject<Ref<GlobalTheme | null>>('theme', ref(null));
const switchTheme = inject<(style: GlobalTheme | null) => void>('switchTheme')!;
const isDark = computed(() => theme.value?.name === 'dark');
const toggleTheme = () => {
  switchTheme(isDark.value ? null : darkTheme);
};

// 菜单图标渲染
const renderIcon = (icon: Component) => () =>
  h(NIcon, null, { default: () => h(icon) });

// 菜单树来自 useAdminMenu（与面包屑同一数据源），这里只做 n-menu 形态转换
const toMenuOption = (item: (typeof ADMIN_MENU)[number]): MenuOption => ({
  label: item.label,
  key: item.key,
  icon: item.icon ? renderIcon(item.icon) : undefined,
  children: item.children?.map(toMenuOption)
});
const menuOptions: MenuOption[] = ADMIN_MENU.map(toMenuOption);

const activeMenu = computed(() => route.path);

const handleMenuSelect = (key: string) => {
  router.push(key);
};

const userDropdown = [
  { label: '退出登录', key: 'logout', icon: renderIcon(Logout) }
];

const handleUserAction = (key: string) => {
  if (key === 'logout') {
    // 尽力通知后端销毁会话（token 此刻仍在 store，读取同步），失败不阻塞本地登出
    useAuthApi()
      .logout()
      .catch(() => {});
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
