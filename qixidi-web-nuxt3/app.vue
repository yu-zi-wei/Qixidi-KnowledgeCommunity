<template>
  <n-config-provider :theme='theme' :locale='zhCN' :date-locale='dateZhCN' >
    <NuxtPage/>
    <n-global-style/>
  </n-config-provider>
</template>

<script setup lang='ts'>
import { dateZhCN, zhCN } from 'naive-ui';
import type {GlobalTheme} from 'naive-ui';
const theme = ref<GlobalTheme | null>(null);
provide('theme', theme);

// 切换主题
const switchTheme = (style: GlobalTheme | null) => {
  theme.value = style
}
provide('switchTheme', switchTheme);

</script>
<style lang='scss'>
body {
  padding: 0;
  margin: 0
}
</style>
