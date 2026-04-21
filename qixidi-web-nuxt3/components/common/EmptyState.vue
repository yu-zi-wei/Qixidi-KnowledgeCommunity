<template>
  <div class="empty-state">
    <n-icon :size="iconSize" class="empty-state-icon"><FileSearch /></n-icon>
    <p class="empty-state-text">{{ description }}</p>
    <div v-if="$slots.default" class="empty-state-extra">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { FileSearch } from '@vicons/tabler'

const props = withDefaults(defineProps<{
  description?: string
  size?: 'small' | 'medium' | 'large'
}>(), {
  description: '暂无数据',
  size: 'medium'
})

const iconSize = computed(() => props.size === 'small' ? 36 : 48)
</script>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 24px;
}

.empty-state-icon {
  color: var(--color-ink-faint);
  opacity: 0.4;
  margin-bottom: 16px;
}

.empty-state-text {
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
  margin: 0;
}

.empty-state-extra {
  margin-top: 16px;
}
</style>
