<template>
  <n-config-provider :theme='theme' :locale='zhCN' :date-locale='dateZhCN' >
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
import type {GlobalTheme} from 'naive-ui';

const THEME_KEY = 'admin-theme';

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
