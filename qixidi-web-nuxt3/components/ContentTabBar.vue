<template>
  <!-- 二级导航栏 -->
  <nav ref="tabBarRef" class="tab-bar" :class="{ 'is-sticky': isSticky }">
    <!-- 固定导航：最新 / 精选 / 关注 -->
    <div class="tab-group">
      <NuxtLink
        v-for="tab in fixedTabs"
        :key="tab.key"
        class="content-tab"
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
        class="content-tab"
        :class="{ active: activeGroupingId === label.id }"
        :to="`/category/${label.id}`"
      >
        {{ label.groupingName }}
      </NuxtLink>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, onUnmounted } from 'vue'
import type { LabelGrouping } from '~/types'

defineProps<{
  labels: LabelGrouping[]
}>()

const route = useRoute()
const tabBarRef = ref<HTMLElement | null>(null)
const isSticky = ref(false)

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

// 监听吸顶状态
onMounted(() => {
  const checkSticky = () => {
    if (!tabBarRef.value) return
    // 获取元素相对于视口的位置
    const rect = tabBarRef.value.getBoundingClientRect()
    // 当元素顶部到达或超过吸顶位置（66px）时，认为已吸顶
    isSticky.value = rect.top <= 66
  }

  // 初始检查
  checkSticky()

  // 监听滚动
  window.addEventListener('scroll', checkSticky, { passive: true })

  onUnmounted(() => {
    window.removeEventListener('scroll', checkSticky)
  })
})
</script>

<style scoped>
/* ==================== 分类标签栏 ==================== */
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
  border-radius: 2px 2px 10px 10px;
  transition: all 0.3s ease;
}

/* 吸顶状态 - 磨玻璃效果 */
.tab-bar.is-sticky {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* 暗色主题吸顶 */
:root.dark .tab-bar.is-sticky {
  background: rgba(13, 15, 17, 0.7);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
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
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
  }

  :root.dark .tab-bar {
    background: rgba(13, 15, 17, 0.7);
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

.content-tab {
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

.content-tab:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.content-tab.active {
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
  .content-tab {
    white-space: normal;
  }

  .tab-group {
    flex-wrap: wrap;
  }
}
</style>
