<template>
  <!-- 二级导航栏 -->
  <nav ref="tabBarRef" class="tab-bar" :class="{ 'is-sticky': isSticky }">
    <!-- 固定导航：最新 / 精选 / 关注 -->
    <div class="tab-group">
      <NuxtLink
        v-for="tab in fixedTabs"
        :key="tab.key"
        class="bm-tab"
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
        class="bm-tab"
        :class="{ active: activeGroupingId === label.id }"
        :to="`/category/${label.id}`"
      >
        {{ label.groupingName }}
      </NuxtLink>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import type { LabelGrouping } from '~/types'

defineProps<{
  labels: LabelGrouping[]
}>()

const route = useRoute()
const tabBarRef = ref<HTMLElement | null>(null)
const { isSticky } = useStickyScroll(tabBarRef, 66)

const fixedTabs = [
  { key: 'latest', label: '最新', to: '/' },
  { key: 'recommend', label: '精选', to: '/featured' },
  { key: 'follow', label: '关注', to: '/follow' }
]

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
/* ==================== 分类标签栏（书签+极简：纯纸色+细线，无磨玻璃无阴影） ==================== */
.tab-bar {
  position: sticky;
  top: 66px;
  z-index: 40;
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: var(--space-2);
  padding: 10px 16px;
  margin-top: 12px;
  margin-bottom: 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  /* 默认透明 */
  background: transparent;
  transition: background-color 0.3s ease;
}

/* 吸顶状态 - 纸色 + 底部细线 + 悬垂影 */
.tab-bar.is-sticky {
  background: var(--color-page-bg);
  border-bottom: 1px solid var(--color-border);
  box-shadow: var(--shadow-nav);
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
    margin-top: 0;
    margin-bottom: 0;
    /* 移动端默认就显示背景（因为始终 fixed） */
    background: var(--color-page-bg);
    border-bottom: 1px solid var(--color-border);
    box-shadow: var(--shadow-nav);
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

/* tab 样式统一走全局 .bm-tab 书签语言（main.css） */

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
  .bm-tab {
    white-space: normal;
  }

  .tab-group {
    flex-wrap: wrap;
  }
}
</style>
