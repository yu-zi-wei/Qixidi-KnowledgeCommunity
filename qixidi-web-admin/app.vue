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

// 品牌主色：深蓝微偏紫；.is-link 等非 Naive 上下文的硬编码色（admin-table.scss）与此同步维护
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#3b74e8',
    primaryColorHover: '#5e90f2',
    primaryColorPressed: '#2d5ec2',
    primaryColorSuppl: '#3b74e8'
  }
};

// 主题唯一数据源；全 CSR，localStorage 直读直写安全
const theme = ref<GlobalTheme | null>(null);
if (import.meta.client && localStorage.getItem(THEME_KEY) === 'dark') {
  theme.value = darkTheme;
}
provide('theme', theme);

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
