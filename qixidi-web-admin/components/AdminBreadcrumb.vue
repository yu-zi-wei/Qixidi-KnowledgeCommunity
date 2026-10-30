<template>
  <n-breadcrumb class="admin-breadcrumb">
    <n-breadcrumb-item>
      <NuxtLink to="/" class="crumb-home">
        <n-icon :size="14"><Home /></n-icon>
        <span>首页</span>
      </NuxtLink>
    </n-breadcrumb-item>
    <n-breadcrumb-item v-for="c in crumbs" :key="c.key">
      <span class="crumb-text" :class="{ 'crumb-current': c.current }">{{ c.label }}</span>
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<script setup lang="ts">
import { Home } from '@vicons/tabler';

const crumbs = useAdminBreadcrumbs();
</script>

<style scoped lang="scss">
.crumb-home {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  line-height: 16.25px; // 与 .crumb-text（13px normal）行高一致
  // inline-flex 行内基线取内部 icon 底边而非文字基线，须按中线对齐才能与相邻文字齐平
  vertical-align: middle;
  text-decoration: none;
  opacity: 0.75;
  transition: opacity 0.2s ease;

  &:hover {
    opacity: 1;
  }
}

.crumb-text {
  font-size: 13px;
  vertical-align: middle;
  opacity: 0.55;

  &.crumb-current {
    font-weight: 600;
    opacity: 0.9;
  }
}
</style>
