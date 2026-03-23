<template>
  <div class="layout-home">
    <div class="home-container">
      <!-- 左侧内容区 (70%) -->
      <div class="home-main">
        <!-- 导航栏 - 直接 sticky -->
        <HomeNavBar :navigation-list="navigationList" @toggle-mobile-menu="showMobileMenu = true" />

        <!-- 二级导航栏 - 直接 sticky -->
        <ContentTabBar :labels="labelList" />

        <!-- 页面内容 -->
        <div class="page-content">
          <slot />
        </div>
      </div>

      <!-- 右侧侧边栏 (30%) - 移动端隐藏 -->
      <div class="home-sidebar" v-show="!isMobile">
        <HomeSidebar />
      </div>
    </div>

    <!-- 移动端菜单抽屉 -->
    <transition name="slide">
      <div v-if="showMobileMenu" class="mobile-menu-overlay" @click="showMobileMenu = false">
        <div class="mobile-menu-drawer" @click.stop>
          <div class="mobile-menu-header">
            <span class="menu-title">菜单</span>
            <button class="close-btn" @click="showMobileMenu = false">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
              </svg>
            </button>
          </div>
          <nav class="mobile-nav-list">
            <template v-for="item in navigationList" :key="item.id">
              <!-- 一级菜单 -->
              <div v-if="item.isList === 0" class="mobile-nav-item" @click="handleNavClick(item.route)">
                <span>{{ item.navigationName }}</span>
              </div>
              <!-- 下拉菜单 -->
              <div v-else class="mobile-nav-dropdown">
                <div class="mobile-nav-item mobile-nav-dropdown-header" @click="toggleDropdown(item.id)">
                  <span>{{ item.navigationName }}</span>
                  <svg class="dropdown-arrow" :class="{ expanded: expandedDropdowns.has(item.id) }" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="6 9 12 15 18 9"></polyline>
                  </svg>
                </div>
                <div v-show="expandedDropdowns.has(item.id)" class="mobile-nav-sublist">
                  <div
                    v-for="sub in item.levelList"
                    :key="sub.route"
                    class="mobile-nav-subitem"
                    @click="handleNavClick(sub.route)"
                  >
                    {{ sub.navigationName }}
                  </div>
                </div>
              </div>
            </template>
          </nav>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

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

// 移动端相关
const isMobile = ref(false)
const showMobileMenu = ref(false)
const expandedDropdowns = ref<Set<number>>(new Set())

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

const toggleDropdown = (id: number) => {
  if (expandedDropdowns.value.has(id)) {
    expandedDropdowns.value.delete(id)
  } else {
    expandedDropdowns.value.add(id)
  }
}

const handleNavClick = (route: string) => {
  showMobileMenu.value = false
  navigateTo(route)
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
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

/* ==================== 移动端菜单抽屉 ==================== */
.mobile-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 150;
  background: rgba(0, 0, 0, 0.5);
}

.mobile-menu-drawer {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 280px;
  max-width: 80vw;
  background: var(--color-surface);
  display: flex;
  flex-direction: column;
  box-shadow: var(--shadow-float);
}

.mobile-menu-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--color-border-light);
}

.menu-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-ink);
}

.close-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  background: transparent;
  color: var(--color-ink-light);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.close-btn svg {
  width: 20px;
  height: 20px;
}

.mobile-nav-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  font-size: 15px;
  color: var(--color-ink);
  cursor: pointer;
  transition: background 0.2s ease;
}

.mobile-nav-item:hover {
  background: var(--color-surface-dim);
}

.mobile-nav-dropdown-header {
  color: var(--color-ink-light);
}

.dropdown-arrow {
  width: 16px;
  height: 16px;
  color: var(--color-ink-muted);
  transition: transform 0.2s ease;
}

.dropdown-arrow.expanded {
  transform: rotate(180deg);
}

.mobile-nav-sublist {
  padding-left: 20px;
  background: var(--color-surface-dim);
}

.mobile-nav-subitem {
  padding: 12px 20px 12px 24px;
  font-size: 14px;
  color: var(--color-ink-light);
  cursor: pointer;
  transition: all 0.2s ease;
}

.mobile-nav-subitem:hover {
  background: var(--color-surface);
  color: var(--color-ink);
}

/* 动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
}

.slide-enter-from .mobile-menu-drawer,
.slide-leave-to .mobile-menu-drawer {
  transform: translateX(-100%);
}
</style>
