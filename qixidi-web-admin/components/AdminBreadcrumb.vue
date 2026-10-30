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
  text-decoration: none;
  opacity: 0.75;
  transition: opacity 0.2s ease;

  &:hover {
    opacity: 1;
  }
}

.crumb-text {
  font-size: 13px;
  opacity: 0.55;

  &.crumb-current {
    font-weight: 600;
    opacity: 0.9;
  }
}
</style>
