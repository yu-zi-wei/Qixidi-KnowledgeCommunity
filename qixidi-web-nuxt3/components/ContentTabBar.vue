<template>
  <!-- 二级导航栏 -->
  <nav class="tab-bar">
    <!-- 固定导航：最新 / 精选 / 关注 -->
    <div class="tab-group">
      <NuxtLink
        v-for="tab in fixedTabs"
        :key="tab.key"
        class="tab-item"
        :class="{ active: activeFixed === tab.key }"
        :to="tab.to"
      >
        {{ tab.label }}
      </NuxtLink>
    </div>

    <!-- 分隔线 -->
    <span v-if="labels.length" class="tab-divider"></span>

    <!-- 动态分类标签 -->
    <div v-if="labels.length" class="tab-group">
      <NuxtLink
        v-for="label in labels"
        :key="label.id"
        class="tab-item"
        :class="{ active: activeGroupingId === label.id }"
        :to="`/category/${label.id}`"
      >
        {{ label.groupingName }}
      </NuxtLink>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { LabelGrouping } from '~/types'

defineProps<{
  labels: LabelGrouping[]
}>()

const route = useRoute()

const fixedTabs = [
  { key: 'latest', label: '最新', to: '/' },
  { key: 'recommend', label: '精选', to: '/featured' },
  { key: 'follow', label: '关注', to: '/follow' }
]

const currentPath = computed(() => route.path)

const activeFixed = computed(() => {
  if (route.path.startsWith('/category/')) return ''
  if (route.query.groupingId) return ''
  const path = route.path
  if (path === '/featured') return 'recommend'
  if (path === '/follow') return 'follow'
  return 'latest'
})

const activeGroupingId = computed(() => {
  if (route.path.startsWith('/category/')) {
    return Number(route.params.id)
  }
  const gid = route.query.groupingId
  return gid ? Number(gid) : null
})
</script>

<style scoped>
/* ==================== 分类标签栏 ==================== */
/*
 * PC端：使用 sticky，在容器内固定（top: 70px 是导航栏高度）
 * 移动端：使用 fixed，全屏固定
 */
.tab-bar {
  position: sticky;
  top: 70px;
  z-index: 40;
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: var(--space-2);
  padding: 10px 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  background: var(--color-surface-warm);
  border-bottom: 1px solid var(--color-border-light);
  margin-left: -16px;
  margin-right: -16px;
}

/* 移动端样式 */
@media (max-width: 768px) {
  .tab-bar {
    position: fixed;
    top: 56px;
    left: 0;
    right: 0;
    z-index: 90;
    flex-wrap: wrap;
    padding: 10px 16px;
    margin-left: 0;
    margin-right: 0;
  }
}

.tab-bar::-webkit-scrollbar {
  display: none;
}

.tab-group {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.tab-item {
  padding: 7px 14px;
  font-size: 13px;
  color: var(--color-ink-light);
  background: transparent;
  border-radius: var(--radius-sm);
  cursor: pointer;
  white-space: nowrap;
  transition: all var(--transition-fast);
  font-family: var(--font-body);
  text-decoration: none;
  font-weight: 400;
  line-height: 1.5;
  display: inline-flex;
  align-items: center;
}

.tab-item:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.tab-item.active {
  background: var(--color-surface-dim);
  color: var(--color-ink);
  font-weight: 500;
}

/* 分隔线 */
.tab-divider {
  width: 1px;
  height: 14px;
  background: var(--color-border);
  flex-shrink: 0;
  margin: 0 var(--space-1);
}

/* ==================== 移动端适配 ==================== */
@media (max-width: 768px) {
  .tab-item {
    white-space: normal;
  }

  .tab-group {
    flex-wrap: wrap;
  }
}
</style>
