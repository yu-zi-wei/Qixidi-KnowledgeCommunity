<template>
  <NTooltip v-if="time" trigger="hover">
    <template #trigger>
      <span class="admin-time-text">{{ formatTime(time) }}</span>
    </template>
    {{ getFullDateTime(time) }}
  </NTooltip>
  <span v-else class="admin-time-text admin-time-text--empty">—</span>
</template>

<script setup lang="ts">
/**
 * 表格时间单元格：相对时间 + hover 完整时间（title 语义）
 * 文章/评论/用户三页共用，替代列 render 里重复的 NTooltip + formatTime 组合
 */
import { NTooltip } from 'naive-ui';
import { formatTime, getFullDateTime } from '~/utils/format';

defineProps<{ time?: string | null }>();
</script>

<style scoped lang="scss">
.admin-time-text {
  font-size: 12px;
  opacity: 0.65;
  font-variant-numeric: tabular-nums;
}

.admin-time-text--empty {
  opacity: 0.4;
}
</style>
