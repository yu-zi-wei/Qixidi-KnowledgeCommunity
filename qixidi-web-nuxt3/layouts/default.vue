<template>
  <div class="layout-default">
    <div class="home-container" :class="{ 'no-sidebar': !showSidebar }">
      <!-- 左侧内容区 -->
      <div class="home-main">
        <!-- 导航栏 -->
        <HomeNavBar :navigation-list="navigationList" />

        <!-- 二级导航栏 - 可选显示 -->
        <ContentTabBar v-if="pageMeta.showTabBar" :labels="labelList" />

        <!-- 页面内容 -->
        <div class="page-content">
          <slot />
        </div>
      </div>

      <!-- 右侧侧边栏 - 移动端隐藏 -->
      <div class="home-sidebar" v-if="showSidebar" v-show="!isMobile">
        <component :is="sidebarComponent" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, defineAsyncComponent } from 'vue'

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
    const result = await labelApi.getGroupingList(1, 10)
    return result.rows || []
  })
])

const navigationList = computed(() => navigationData.value || [])
const labelList = computed(() => labelData.value || [])

// 同步更新 labelStore
if (labelData.value?.length) {
  labelStore.labelList = labelData.value
  labelStore.loaded = true
}

const route = useRoute()

const pageMeta = computed(() => ({
  showTabBar: route.meta.showTabBar ?? false,
  sidebar: route.meta.sidebar ?? 'home'
}))

const showSidebar = computed(() => pageMeta.value.sidebar !== false)

const sidebarComponents = {
  home: defineAsyncComponent(() => import('~/components/HomeSidebar.vue')),
  article: defineAsyncComponent(() => import('~/components/ArticleSidebar.vue')),
  readingEssays: defineAsyncComponent(() => import('~/components/readingEssays/ReadingEssaysSidebar.vue')),
}

const sidebarComponent = computed(() => {
  const type = pageMeta.value.sidebar
  return type === false ? null : sidebarComponents[type as keyof typeof sidebarComponents] || sidebarComponents.home
})

// 移动端检测
const isMobile = ref(false)
const checkMobile = () => { isMobile.value = window.innerWidth < 768 }

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => window.removeEventListener('resize', checkMobile))
</script>

<style scoped>
.layout-default {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.home-container {
  flex: 1;
  display: flex;
  gap: 40px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 20px 24px 24px;
}

/* 左侧内容区 */
.home-main {
  flex: 3;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* 右侧侧边栏 */
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
  min-height: 0;
  /* 移除 overflow: hidden，让详情页可以撑开触发浏览器滚动 */
}

/* 无侧边栏时，主内容区占满 */
.home-container.no-sidebar {
  gap: 0;
}

.home-container.no-sidebar .home-main {
  flex: 1;
  max-width: 100%;
}

/* ==================== 移动端布局 ==================== */
@media (max-width: 768px) {
  .home-main {
    padding-top: 260px; /* 默认值 */
  }
}

/* ==================== 响应式 ==================== */
@media (max-width: 1024px) {
  .home-container {
    gap: 20px;
    padding: 20px 16px 32px;
  }

  .home-sidebar {
    min-width: 260px;
    max-width: 280px;
  }
}

@media (max-width: 768px) {
  .home-container {
    flex-direction: column;
    gap: 16px;
    padding: 0 16px 16px;
  }

  .home-main {
    min-width: 100%;
  }

  .home-sidebar {
    display: none;
  }
}
</style>
