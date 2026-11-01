<template>
  <div class="app-shell" :class="isDark ? 'theme-dark' : 'theme-light'">
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
        <div class="logo" :style="{ height: '56px' }">
          <AdminLogo :size="26" />
          <span v-if="!collapsed" class="font-bold text-base">栖息地后台</span>
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
        <n-layout-header bordered class="header" :style="{ height: '56px' }">
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

        <!-- 内容区：去卡片化，表格/区块直接坐主题底色上，hairline 分隔（app.vue token 统一配色）；左右留白略大于上下 -->
        <n-layout-content content-style="padding: 16px 20px;" class="content">
          <slot />
        </n-layout-content>
      </n-layout>
    </n-layout>
  </div>
</template>

<script setup lang="ts">
import { h } from 'vue';
import type { MenuOption } from 'naive-ui';
import { darkTheme, NIcon } from 'naive-ui';
import type { GlobalTheme } from 'naive-ui';
import { MoonStars, Sun, Logout } from '@vicons/tabler';
import { useAdminFilteredMenu, type AdminMenuItem } from '~/composables/useAdminMenu';

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

// 菜单树来自 useAdminMenu（与面包屑同一数据源），按登录用户权限码过滤后转 n-menu 形态
const filteredMenu = useAdminFilteredMenu();
const toMenuOption = (item: AdminMenuItem): MenuOption => ({
  label: item.label,
  key: item.key,
  icon: item.icon ? renderIcon(item.icon) : undefined,
  children: item.children?.map(toMenuOption)
});
const menuOptions = computed<MenuOption[]>(() => filteredMenu.value.map(toMenuOption));

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
/* 主题变量注入：非 Naive 上下文的硬编码色（admin-table.scss 等）统一取这里，
   与 app.vue themeOverrides 的 primaryColor 同源（深色用亮变体保证对比度）。
   墨水与纸张：侧栏米白 #EDEAE1 分层（内容区暖纸 #FAF9F6）、纸片 #FFFFFF + 暖灰 hairline，
   品牌墨水蓝 #3D5A80（前台 logo 同源） */
.theme-dark {
  --cell-link: #8FA9C4;
  --hairline: rgba(255, 255, 255, 0.08);
  --sider-bg: #131820;
  --logo-bg: #8FA9C4;
  --logo-ink: #131820;
  --panel-bg: #1a1a1e;
  --panel-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
  --scrollbar-thumb: rgba(255, 255, 255, 0.18);
  --scrollbar-thumb-hover: rgba(255, 255, 255, 0.32);
}

.theme-light {
  --cell-link: #3D5A80;
  --hairline: #E8E4D9;
  --sider-bg: #EDEAE1;
  --logo-bg: #3D5A80;
  --logo-ink: #FAF9F6;
  --panel-bg: #ffffff;
  --panel-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  --scrollbar-thumb: #b9c4d4;
  --scrollbar-thumb-hover: #a3b1c6;
}

.app-shell {
  height: 100vh;

  :deep(.n-layout-sider) {
    background: var(--sider-bg);
  }
}

/* Tailwind 工具类在本项目未编译生效，flex/gap 必须这里手写 */
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid var(--hairline);
}

/* Tailwind 的 px/flex 类在本项目未编译生效（v3 语法 + v4 postcss 插件），布局一律这里写死 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.content {
  height: calc(100vh - 56px);
}
</style>
