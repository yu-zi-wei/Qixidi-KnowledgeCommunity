<template>
  <div class="admin-page-header">
    <div class="header-main">
      <div class="header-text">
        <h2 class="title">{{ title }}</h2>
        <p v-if="description" class="description">{{ description }}</p>
      </div>

      <div class="header-side">
        <div v-if="stats && stats.length" class="stats">
          <div v-for="s in stats" :key="s.label" class="stat-pill" :class="{ accent: s.accent }">
            <span class="stat-value">{{ s.loading ? '…' : (s.value ?? '—') }}</span>
            <span class="stat-label">{{ s.label }}</span>
          </div>
        </div>
        <div v-if="$slots.actions" class="actions">
          <slot name="actions" />
        </div>
      </div>
    </div>
    <n-divider class="header-divider" />
  </div>
</template>

<script setup lang="ts">
/**
 * 后台页面统一页头：标题 + 描述 + 统计胶囊 + 右侧操作区
 * 三个管理页（文章/评论/用户）共用，保证页面骨架一致性
 */
export interface HeaderStat {
  label: string;
  value?: number | string;
  loading?: boolean;
  /** 强调胶囊（如"审核中"，数量 > 0 时传 true 高亮） */
  accent?: boolean;
}

defineProps<{
  title: string;
  description?: string;
  stats?: HeaderStat[];
}>();
</script>

<style scoped lang="scss">
.admin-page-header {
  margin-bottom: 16px;
}

.header-main {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
}

.title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  line-height: 1.3;
}

.description {
  margin: 4px 0 0;
  font-size: 13px;
  opacity: 0.55;
}

.header-side {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.stats {
  display: flex;
  gap: 10px;
}

.stat-pill {
  display: flex;
  align-items: baseline;
  gap: 6px;
  padding: 5px 14px;
  border-radius: 999px;
  font-variant-numeric: tabular-nums;

  background: rgba(128, 128, 128, 0.08);

  .stat-value {
    font-size: 18px;
    font-weight: 700;
    line-height: 1;
  }

  .stat-label {
    font-size: 12px;
    opacity: 0.55;
  }

  &.accent .stat-value {
    color: var(--warning-color, #f0a020);
  }
}

.header-divider {
  margin: 14px 0 0;
}
</style>
