<template>
  <n-config-provider :theme='theme' :theme-overrides='themeOverrides' :locale='zhCN' :date-locale='dateZhCN' >
    <n-message-provider>
      <n-dialog-provider>
        <NuxtLayout>
          <NuxtPage/>
        </NuxtLayout>
      </n-dialog-provider>
    </n-message-provider>
    <n-global-style/>
  </n-config-provider>
</template>

<script setup lang='ts'>
import { dateZhCN, zhCN, darkTheme } from 'naive-ui';
import type {GlobalTheme, GlobalThemeOverrides} from 'naive-ui';

const THEME_KEY = 'admin-theme';

/**
 * 极简书签风·墨水与纸张（2026-11-02 定稿）：暖纸底色系——内容区 #FAF9F6、侧栏米白
 * #EDEAE1（布局根注入 --sider-bg），区块是白纸片卡片（细暖灰边框、轻投影）。
 * 品牌色是墨水蓝 #3D5A80（前台树形 logo 同源，前后台一体）：低饱和灰调与暖纸同族，
 * 如墨水落在纸面，只做交互点缀（链接/选中态/焦点/唯一主按钮），拒绝高饱和鲜色。
 * 深色模式：墨黑纸 + 亮墨蓝 #8FA9C4。非 Naive 上下文的硬编码色（admin-table.scss
 * 的 .is-link 等）用布局根注入的 --cell-link / --hairline 变量，与此处同源。
 */
const lightOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#3D5A80',
    primaryColorHover: '#4E6E99',
    primaryColorPressed: '#2E4560',
    primaryColorSuppl: '#3D5A80',
    bodyColor: '#FAF9F6',
    cardColor: '#ffffff',
    borderColor: '#E8E4D9',
    dividerColor: '#E8E4D9'
  },
  DataTable: {
    // 纸片实色（=cardColor）：fixed 操作列 sticky 定位下透明背景会透出滚动内容
    thColor: '#ffffff',
    tdColor: '#ffffff',
    thColorHover: '#ffffff',
    tdColorHover: '#F0F3F7',
    borderColor: '#E8E4D9',
    thTextColor: 'rgba(30, 41, 59, 0.55)'
  },
  Menu: {
    itemColorActive: '#C9D4E2',
    itemColorActiveHover: '#BCC9DA',
    itemColorHover: 'rgba(30, 41, 59, 0.05)'
  }
};

const darkOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#8FA9C4',
    primaryColorHover: '#A5BCD4',
    primaryColorPressed: '#7590AC',
    primaryColorSuppl: '#8FA9C4',
    bodyColor: '#111113',
    cardColor: '#1a1a1e',
    modalColor: '#1e1e24',
    popoverColor: '#202028',
    borderColor: 'rgba(255, 255, 255, 0.1)',
    dividerColor: 'rgba(255, 255, 255, 0.08)',
    inputColor: 'rgba(255, 255, 255, 0.05)'
  },
  DataTable: {
    // 纸片实色（=cardColor）：fixed 操作列 sticky 定位下透明背景会透出滚动内容
    thColor: '#1a1a1e',
    tdColor: '#1a1a1e',
    thColorHover: '#1a1a1e',
    tdColorHover: 'rgba(255, 255, 255, 0.04)',
    borderColor: 'rgba(255, 255, 255, 0.08)',
    thTextColor: 'rgba(255, 255, 255, 0.5)'
  },
  Menu: {
    itemColorActive: 'rgba(255, 255, 255, 0.07)',
    itemColorActiveHover: 'rgba(255, 255, 255, 0.1)',
    itemColorHover: 'rgba(255, 255, 255, 0.05)',
    arrowColor: 'rgba(255, 255, 255, 0.4)',
    arrowColorHover: 'rgba(255, 255, 255, 0.6)',
    arrowColorActive: 'rgba(255, 255, 255, 0.9)',
    arrowColorChildActive: 'rgba(255, 255, 255, 0.9)'
  }
};

// 主题唯一数据源；全 CSR，localStorage 直读直写安全。书签风为默认（仅显式 dark 才深色）
const theme = ref<GlobalTheme | null>(null);
if (import.meta.client && localStorage.getItem(THEME_KEY) === 'dark') {
  theme.value = darkTheme;
}
provide('theme', theme);

const themeOverrides = computed<GlobalThemeOverrides>(() =>
  theme.value?.name === 'dark' ? darkOverrides : lightOverrides
);

// 切换主题（同步持久化，刷新后保持）
const switchTheme = (style: GlobalTheme | null) => {
  theme.value = style
  localStorage.setItem(THEME_KEY, style?.name === 'dark' ? 'dark' : 'light')
}
provide('switchTheme', switchTheme);

</script>

<style lang='scss'>
body {
  padding: 0;
  margin: 0
}
</style>
