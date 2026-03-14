<template>
  <div class="layout-home">
    <div class="home-container">
      <!-- 左侧内容区 (70%) -->
      <div class="home-main">
        <!-- 导航栏 - 在 layout 中获取数据，确保 SSR 阶段就有数据 -->
        <HomeNavBar :navigation-list="navigationList" />

        <!-- 分类栏 -->
        <ContentTabBar :labels="labelList" />

        <!-- 页面内容 -->
        <div class="page-content">
          <slot />
        </div>
      </div>

      <!-- 右侧侧边栏 (30%) -->
      <div class="home-sidebar">
        <HomeSidebar />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const labelStore = useLabelStore()
const labelApi = useLabelApi()
const navigationApi = useNavigationApi()

// 并行获取导航数据和标签数据，都使用 await 确保 SSR 阶段加载完成
const [{ data: navigationData }, { data: labelData }] = await Promise.all([
  useAsyncData('layout-navigation', async () => {
    const { rows } = await navigationApi.getList(1, 0)
    return rows || []
  }),
  useAsyncData('layout-labels', async () => {
    const result = await labelApi.getGroupingList(1, 12)  // 获取前12个热门分类
    return result.rows || []
  })
])

// 直接使用数据，确保 SSR 阶段就有数据
const navigationList = computed(() => navigationData.value || [])
const labelList = computed(() => labelData.value || [])

// 同步更新 labelStore 状态（供其他组件使用）
if (labelData.value && labelData.value.length > 0) {
  labelStore.labelList = labelData.value
  labelStore.loaded = true
}
</script>

<style scoped>
.layout-home {
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
  padding: 20px 24px 40px;
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
