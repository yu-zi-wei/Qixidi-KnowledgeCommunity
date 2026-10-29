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
import { darkTheme, NIcon } from 'naive-ui';
import type { GlobalTheme } from 'naive-ui';
import {
  Dashboard,
  FileText,
  MessageCircle,
  Users,
  Settings,
  Adjustments,
  Folders,
  MoonStars,
  Sun,
  Logout
} from '@vicons/tabler';

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

// 层级菜单：叶子 key 用路由（供 :value 高亮匹配），父级 key 用唯一非路由标识
const menuOptions: MenuOption[] = [
  { label: '数据看板', key: '/', icon: renderIcon(Dashboard) },
  {
    label: '内容管理',
    key: 'content',
    icon: renderIcon(Folders),
    children: [
      { label: '文章管理', key: '/article', icon: renderIcon(FileText) },
      { label: '评论管理', key: '/comment', icon: renderIcon(MessageCircle) }
    ]
  },
  { label: '前台用户', key: '/user', icon: renderIcon(Users) },
  {
    label: '系统管理',
    key: 'system',
    icon: renderIcon(Settings),
    children: [{ label: '系统设置', key: '/setting', icon: renderIcon(Adjustments) }]
  }
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
