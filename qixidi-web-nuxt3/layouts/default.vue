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
    <CreatorApplyDialog />
    <ScrollToTop />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

const labelStore = useLabelStore()
const labelApi = useLabelApi()
const navigationApi = useNavigationApi()

// 完整 SSR 模式：useAsyncData 在 SSR 阶段调用接口，try/catch 防止抛错
// default 选项确保初始值非空；onMounted 兜底覆盖 SSR 失败场景
const { data: navigationData, refresh: refreshNavigation } = await useAsyncData(
  'layout-navigation',
  async () => {
    try {
      const { rows } = await navigationApi.getList(1, 0)
      return rows || []
    } catch (e: any) {
      console.error('[layout-default] navigation SSR error:', e?.message || e)
      return []
    }
  },
  { default: () => [] as any }
)

const { data: labelData, refresh: refreshLabels } = await useAsyncData(
  'layout-labels',
  async () => {
    try {
      const result = await labelApi.getGroupingList(1, 9)
      return result.rows || []
    } catch (e: any) {
      console.error('[layout-default] labels SSR error:', e?.message || e)
      return []
    }
  },
  { default: () => [] as any }
)

const navigationList = computed(() => navigationData.value || [])
const labelList = computed(() => labelData.value || [])

// 同步 labelStore
watch(() => labelData.value, (val) => {
  if (val && val.length > 0) {
    labelStore.setLabelList(val)
  }
}, { immediate: true })

// 客户端兜底：SSR 失败时（如本机回环不可用）刷新数据
onMounted(async () => {
  if (!navigationData.value || navigationData.value.length === 0) {
    await refreshNavigation()
  }
  if (!labelData.value || labelData.value.length === 0) {
    await refreshLabels()
  }
})

const route = useRoute()

const pageMeta = computed(() => ({
  showTabBar: route.meta.showTabBar ?? false,
  sidebar: route.meta.sidebar ?? 'home'
}))

const showSidebar = computed(() => pageMeta.value.sidebar !== false)

const sidebarComponents = {
  home: resolveComponent('HomeSidebar'),
  article: resolveComponent('ArticleSidebar'),
  readingEssays: resolveComponent('ReadingEssaysSidebar'),
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
