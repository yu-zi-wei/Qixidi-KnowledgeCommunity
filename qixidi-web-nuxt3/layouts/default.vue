<template>
  <div class="layout-default">
    <div class="home-container">
      <!-- 左侧内容区 -->
      <div class="home-main">
        <!-- 导航栏 - 在 layout 中获取数据，确保 SSR 阶段就有数据 -->
        <HomeNavBar :navigation-list="navigationList" />

        <!-- 分类栏 - 可选显示 -->
        <ContentTabBar v-if="pageMeta.showTabBar" :labels="labelList" />

        <!-- 页面内容 -->
        <div class="page-content">
          <slot />
        </div>
      </div>

      <!-- 右侧侧边栏 - 动态组件 -->
      <div class="home-sidebar">
        <component :is="sidebarComponent" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 导航和标签数据（SSR 友好）
const labelStore = useLabelStore()
const labelApi = useLabelApi()
const navigationApi = useNavigationApi()

// 并行获取导航数据和标签数据
const [{ data: navigationData }, { data: labelData }] = await Promise.all([
  useAsyncData('layout-navigation', async () => {
    const { rows } = await navigationApi.getList(1, 0)
    return rows || []
  }),
  useAsyncData('layout-labels', async () => {
    const result = await labelApi.getGroupingList(1, 12)
    return result.rows || []
  })
])

const navigationList = computed(() => navigationData.value || [])
const labelList = computed(() => labelData.value || [])

// 同步更新 labelStore 状态
if (labelData.value && labelData.value.length > 0) {
  labelStore.labelList = labelData.value
  labelStore.loaded = true
}

// 获取当前页面的 page meta 配置
const pageMeta = computed(() => {
  const route = useRoute()
  return {
    showTabBar: route.meta.showTabBar || false,
    sidebar: route.meta.sidebar || 'home'
  }
})

// 侧边栏组件映射表（配置化，易于扩展）
const sidebarComponents = {
  home: defineAsyncComponent(() => import('~/components/HomeSidebar.vue')),
  article: defineAsyncComponent(() => import('~/components/ArticleSidebar.vue')),
  readingEssays: defineAsyncComponent(() => import('~/components/readingEssays/ReadingEssaysSidebar.vue')),
  // 新增侧边栏类型只需在这里添加一行
  // 例如: user: defineAsyncComponent(() => import('~/components/UserSidebar.vue'))
}

// 动态侧边栏组件
const sidebarComponent = computed(() => {
  const sidebarType = pageMeta.value.sidebar || 'home'
  return sidebarComponents[sidebarType] || sidebarComponents.home
})
</script>

<style scoped>
.layout-default {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.home-container {
  flex: 1;
  display: flex;
  gap: 40px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
  padding: 20px 24px 24px;
}

/* 左侧内容区 (75%) */
.home-main {
  flex: 3;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* 右侧侧边栏 (25%) */
.home-sidebar {
  flex: 1;
  min-width: 260px;
  max-width: 320px;
  position: sticky;
  top: 24px;
  align-self: flex-start;
  height: calc(100vh - 48px);
  overflow: hidden;
}

.page-content {
  flex: 1;
}

/* 响应式 */
@media (max-width: 1024px) {
  .home-container {
    flex-direction: column;
  }

  .home-main {
    min-width: 100%;
  }

  .home-sidebar {
    min-width: 100%;
    max-width: 100%;
  }
}
</style>
