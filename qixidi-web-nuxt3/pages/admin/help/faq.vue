<template>
  <div class="admin-placeholder">
    <div class="placeholder-icon">
      <n-icon size="48"><Help /></n-icon>
    </div>
    <h2 class="placeholder-title">常见问题</h2>
    <p class="placeholder-desc">功能开发中，敬请期待</p>
  </div>
</template>

<script setup lang="ts">
import { Help } from '@vicons/tabler'

definePageMeta({ layout: 'admin', middleware: 'creator' })
</script>

<style scoped>
.admin-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.placeholder-icon {
  color: var(--color-ink-faint);
}

.placeholder-title {
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.placeholder-desc {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
}
</style>
