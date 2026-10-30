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
 * 高级风 · 近无彩色系（2026-10-30 定版）：黑白灰做骨架，蓝色只做交互点缀
 * （链接/选中态/焦点/唯一主按钮），数据可视化与语义色（成功/危险）不受限。
 * 表格/菜单透明底 + hairline 分隔，去卡片化——内容即界面。
 * 非 Naive 上下文的硬编码色（admin-table.scss 的 .is-link 等）用布局根注入的
 * --cell-link / --hairline 变量，与此处 primaryColor 保持同源。
 */
const lightOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#2d63d6',
    primaryColorHover: '#4d7fe8',
    primaryColorPressed: '#2350ae',
    primaryColorSuppl: '#2d63d6',
    bodyColor: '#f7f7f8',
    cardColor: '#ffffff',
    borderColor: 'rgba(0, 0, 0, 0.08)',
    dividerColor: 'rgba(0, 0, 0, 0.08)'
  },
  DataTable: {
    thColor: 'transparent',
    tdColor: 'transparent',
    thColorHover: 'transparent',
    tdColorHover: 'rgba(0, 0, 0, 0.03)',
    borderColor: 'rgba(0, 0, 0, 0.08)',
    thTextColor: 'rgba(0, 0, 0, 0.5)'
  },
  Menu: {
    itemColorActive: 'rgba(0, 0, 0, 0.05)',
    itemColorActiveHover: 'rgba(0, 0, 0, 0.07)',
    itemColorHover: 'rgba(0, 0, 0, 0.04)'
  }
};

const darkOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#6c87f6',
    primaryColorHover: '#84a1f9',
    primaryColorPressed: '#5570e0',
    primaryColorSuppl: '#6c87f6',
    bodyColor: '#111113',
    cardColor: '#1a1a1e',
    modalColor: '#1e1e24',
    popoverColor: '#202028',
    borderColor: 'rgba(255, 255, 255, 0.1)',
    dividerColor: 'rgba(255, 255, 255, 0.08)',
    inputColor: 'rgba(255, 255, 255, 0.05)'
  },
  DataTable: {
    thColor: 'transparent',
    tdColor: 'transparent',
    thColorHover: 'transparent',
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

// 主题唯一数据源；全 CSR，localStorage 直读直写安全。深色为默认主题（仅显式 light 才浅色）
const theme = ref<GlobalTheme | null>(null);
if (import.meta.client && localStorage.getItem(THEME_KEY) !== 'light') {
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
