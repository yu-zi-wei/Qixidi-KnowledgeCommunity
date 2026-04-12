<template>
  <div class="search-page">
    <!-- 二级菜单 -->
    <div ref="tabsRef" class="search-tabs" :class="{ 'is-sticky': isSticky }">
      <NuxtLink
        v-for="tab in tabs"
        :key="tab.value"
        :to="{ path: `/search/${tab.value}`, query: { q: keyword } }"
        class="search-tab-item"
        :class="{ active: activeTab === tab.value }"
      >
        {{ tab.label }}
      </NuxtLink>
    </div>

    <!-- 搜索结果区域 -->
    <div class="search-results">
      <NuxtPage />
    </div>
  </div>
</template>

<script setup lang="ts">
useHead({
  bodyAttrs: {
    class: 'page-search'
  }
})

const tabs = [
  { value: 'article', label: '文章' },
  { value: 'notes', label: '小记' },
  { value: 'label', label: '标签' },
  { value: 'user', label: '用户' }
]

const route = useRoute()
const tabsRef = ref<HTMLElement | null>(null)
const isSticky = ref(false)

const keyword = computed(() => (route.query.q as string || '').trim())
const activeTab = computed(() => {
  const parts = route.path.split('/')
  return parts[2] || 'article'
})

onMounted(() => {
  const checkSticky = () => {
    if (!tabsRef.value) return
    const rect = tabsRef.value.getBoundingClientRect()
    isSticky.value = rect.top <= 66
  }

  checkSticky()
  window.addEventListener('scroll', checkSticky, { passive: true })

  onUnmounted(() => {
    window.removeEventListener('scroll', checkSticky)
  })
})
</script>

<style>
.search-page {
  display: flex;
  flex-direction: column;
}

.search-tabs {
  position: sticky;
  top: 66px;
  z-index: 40;
  display: flex;
  gap: 4px;
  padding: 12px 24px;
  background: transparent;
  border-bottom: 1px solid var(--color-border-light);
  transition: all 0.3s ease;
}

.search-tabs.is-sticky {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border-radius: 0 0 12px 12px;
}

:root.dark .search-tabs.is-sticky {
  background: rgba(13, 15, 17, 0.7);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.search-tab-item {
  padding: 8px 20px;
  text-decoration: none;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-bottom: 2px solid transparent;
  transition: all var(--transition-fast);
}

.search-tab-item:hover {
  color: var(--color-ink);
}

.search-tab-item.active {
  color: var(--color-primary);
  font-weight: 600;
  border-bottom-color: var(--color-primary);
}

.search-results {
  padding: 20px 24px;
  min-height: 400px;
}

.search-loading,
.search-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

@media (max-width: 640px) {
  .search-tabs {
    position: fixed;
    top: 56px;
    left: 0;
    right: 0;
    width: 100%;
    padding: 12px 16px;
    z-index: 90;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border-radius: 0 0 12px 12px;
  }

  :root.dark .search-tabs {
    background: rgba(13, 15, 17, 0.7);
  }

  .search-results {
    padding: 16px;
  }
}
</style>

<style>
@media (max-width: 768px) {
  body.page-search .home-main {
    padding-top: 70px !important;
  }
}
</style>
