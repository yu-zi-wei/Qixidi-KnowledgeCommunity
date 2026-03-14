<template>
  <header class="home-nav-bar">
    <!-- 左侧：Logo -->
    <div class="nav-left">
      <NuxtLink to="/" class="nav-logo">
        <span class="logo-icon">栖</span>
        <span class="logo-text">栖息地</span>
      </NuxtLink>
    </div>

    <!-- 中间：搜索框 + 导航菜单（整体居中） -->
    <div class="nav-center">
      <!-- 搜索框 -->
      <div class="search-wrapper" ref="searchWrapperRef">
        <div
          class="search-input-wrapper"
          :class="{ focused: showDropdown }"
          @click="handleSearchClick"
        >
          <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
          <input
            ref="searchInputRef"
            v-model="searchKeyword"
            type="text"
            placeholder="搜索文章、标签、用户..."
            class="search-input"
            @focus="handleSearchFocus"
            @keydown.enter="handleSearch"
          />
          <button v-if="searchKeyword" class="clear-btn" @click.stop="searchKeyword = ''">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>

        <!-- 下拉内容：横向排列的历史记录 -->
        <Transition name="dropdown">
          <div v-if="showDropdown" class="search-dropdown" v-click-outside="() => showDropdown = false">
            <div class="search-dropdown-content">
              <div v-if="searchHistory.length > 0" class="search-history-section">
                <div class="search-history-title">搜索历史</div>
                <div class="search-history-tags">
                  <span
                    v-for="item in searchHistory"
                    :key="item.content"
                    class="history-tag"
                    @click="handleSearchKeyword(item.content)"
                  >
                    {{ item.content }}
                  </span>
                </div>
              </div>
              <div v-else class="search-history-empty">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="11" cy="11" r="8"></circle>
                  <path d="m21 21-4.35-4.35"></path>
                </svg>
                <span>暂无搜索历史</span>
              </div>
            </div>
          </div>
        </Transition>
      </div>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <template v-for="item in plainMenus" :key="item.id">
          <NuxtLink
            :to="item.route"
            class="nav-link"
            :class="{ active: isActive(item.route) }"
          >
            {{ item.navigationName }}
          </NuxtLink>
        </template>

        <template v-for="item in dropdownMenus" :key="item.id">
          <n-dropdown :options="buildDropdownOptions(item)" @select="handleDropdownSelect">
            <span class="nav-link" :class="{ active: isDropdownActive(item) }">
              {{ item.navigationName }}
              <svg class="dropdown-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M6 9l6 6 6-6"></path>
              </svg>
            </span>
          </n-dropdown>
        </template>
      </nav>
    </div>

    <!-- 右侧：功能区 -->
    <div class="nav-right">
      <!-- 主题切换 -->
      <button class="icon-btn theme-toggle" :title="themeTitle" @click="toggleTheme">
        <svg v-show="colorMode.value === 'dark'" class="theme-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
        </svg>
        <svg v-show="colorMode.value !== 'dark'" class="theme-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="5"></circle>
          <line x1="12" y1="1" x2="12" y2="3"></line>
          <line x1="12" y1="21" x2="12" y2="23"></line>
          <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
          <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
          <line x1="1" y1="12" x2="3" y2="12"></line>
          <line x1="21" y1="12" x2="23" y2="12"></line>
          <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
          <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
        </svg>
      </button>

      <template v-if="authStore.isLoggedIn">
        <n-dropdown :options="publishOptions" @select="handlePublish">
          <button class="icon-btn" title="创作">
            <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
          </button>
        </n-dropdown>

        <button class="icon-btn" title="通知" @click="navigateTo('/notifications')">
          <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
          </svg>
        </button>

        <n-dropdown :options="userMenuOptions" @select="handleUserMenu">
          <n-avatar
            round :size="32"
            :src="authStore.user?.avatar"
            fallback-src="/img/tx.jpg"
            class="user-avatar"
          />
        </n-dropdown>
      </template>

      <template v-else>
        <button class="login-btn" @click="handleShowLogin">
          登录
        </button>
      </template>
    </div>

    <n-modal v-model:show="showLogoutConfirm" preset="dialog" type="warning"
      title="退出登录" content="确定要离开吗？"
      positive-text="确定" negative-text="取消"
      @positive-click="doLogout"
    />
  </header>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
// 不再需要导入 Tabler 图标，使用内联 SVG
import type { Navigation } from '~/types'

// v-click-outside 指令
const vClickOutside = {
  mounted(el: HTMLElement, binding: any) {
    const clickOutside = (e: Event) => {
      if (!(e.target instanceof Node) || el.contains(e.target)) return
      if (typeof binding.value === 'function') {
        binding.value(e)
      }
    }
    // 使用 mousedown 而不是 click，避免与内部点击冲突
    document.addEventListener('mousedown', clickOutside)
    ;(el as any)._clickOutside = clickOutside
  },
  unmounted(el: HTMLElement) {
    const clickOutside = (el as any)._clickOutside
    if (clickOutside) {
      document.removeEventListener('mousedown', clickOutside)
    }
  }
}

interface Props {
  navigationList: Navigation[]
}

const props = defineProps<Props>()

const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const colorMode = useColorMode()

const toggleTheme = () => {
  colorMode.preference = colorMode.value === 'dark' ? 'light' : 'dark'
}

const themeTitle = computed(() =>
  colorMode.value === 'dark' ? '切换浅色模式' : '切换深色模式'
)

const handleShowLogin = () => {
  authDialogStore.showLoginDialog()
}

// 搜索功能
const searchApi = useSearchApi()
const searchWrapperRef = ref<HTMLElement>()
const searchInputRef = ref<HTMLInputElement>()
const searchKeyword = ref('')
const searchHistory = ref<{ content: string }[]>([])
const searchHistoryLoaded = ref(false)
const showDropdown = ref(false)

// 加载搜索历史
const loadSearchHistory = async () => {
  if (!authStore.isLoggedIn || searchHistoryLoaded.value) {
    return
  }

  try {
    const result = await searchApi.getSearchHistory({
      pageNum: 1,
      pageSize: 10,
      uid: authStore.user?.uuid
    })
    searchHistory.value = result.rows || []
    searchHistoryLoaded.value = true
  } catch (error) {
    console.error('加载搜索历史失败:', error)
  }
}

// 点击搜索框
const handleSearchClick = () => {
  loadSearchHistory()
  showDropdown.value = true
  searchInputRef.value?.focus()
}

// 搜索框聚焦时加载历史记录
const handleSearchFocus = () => {
  loadSearchHistory()
  showDropdown.value = true
}

// 关闭下拉
const closeDropdown = () => {
  showDropdown.value = false
}

// 点击历史标签
const handleSearchKeyword = (keyword: string) => {
  searchKeyword.value = keyword
  showDropdown.value = false
  navigateTo({ path: '/search', query: { q: keyword } })
}

// 回车搜索
const handleSearch = () => {
  if (!searchKeyword.value.trim()) return
  const keyword = searchKeyword.value.trim()
  showDropdown.value = false
  navigateTo({ path: '/search', query: { q: keyword } })
}

const plainMenus = computed(() =>
  props.navigationList.filter(item => item.isList === 0)
)

const dropdownMenus = computed(() =>
  props.navigationList.filter(item => item.isList === 1)
)

const isActive = (path: string) => route.path === path

const isDropdownActive = (item: Navigation) => {
  if (!item.levelList) return false
  return item.levelList.some(sub => route.path === sub.route)
}

const buildDropdownOptions = (item: Navigation) => {
  if (!item.levelList) return []
  return item.levelList.map(sub => ({
    label: sub.navigationName,
    key: sub.route
  }))
}

const handleDropdownSelect = (key: string) => {
  navigateTo(key)
}

const publishOptions = [
  { label: '写文章', key: 'article' },
  { label: '记随笔', key: 'essay' },
  { label: '时光小记', key: 'note' }
]

const handlePublish = (key: string) => {
  const routes: Record<string, string> = {
    article: '/write',
    essay: '/admin/essays/edit',
    note: '/admin/notes'
  }
  navigateTo(routes[key])
}

const userMenuOptions = computed(() => [
  { label: '创作中心', key: 'admin' },
  { label: '我的主页', key: 'profile' },
  { label: '我的设置', key: 'settings' },
  { type: 'divider', key: 'd1' },
  { label: '退出登录', key: 'logout' }
])

const authApi = useAuthApi()
const showLogoutConfirm = ref(false)

const doLogout = async () => {
  try { await authApi.logout() } catch {}
  authStore.logout()
  showLogoutConfirm.value = false
  navigateTo('/')
}

const handleUserMenu = async (key: string) => {
  if (key === 'logout') {
    showLogoutConfirm.value = true
    return
  }
  const routes: Record<string, string> = {
    admin: '/admin',
    profile: `/user/${authStore.user?.uuid}`,
    settings: '/admin/settings'
  }
  if (routes[key]) {
    navigateTo(routes[key])
  }
}
</script>

<style scoped>
/* ==================== 导航栏容器：CSS Grid 三列布局 ==================== */
.home-nav-bar {
  position: sticky;
  top: 0;
  z-index: 50;
  display: grid;
  /* 左侧固定 | 中间自适应 | 右侧固定 */
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 24px;
  padding: 16px 0;
  background: var(--color-surface-warm);
  backdrop-filter: blur(8px) saturate(180%);
  -webkit-backdrop-filter: blur(8px) saturate(180%);
  height: 70px;
}

/* ==================== 左侧：Logo 区域 ==================== */
.nav-left {
  display: flex;
  align-items: center;
  min-width: max-content; /* 防止内容被压缩 */
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  background: var(--color-primary);
  color: #fff;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  border-radius: var(--radius-md);
  flex-shrink: 0;
}

.logo-text {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--color-ink);
  letter-spacing: 2px;
}

/* ==================== 中间：搜索框 + 导航菜单（整体居中） ==================== */
.nav-center {
  display: flex;
  align-items: center;
  gap: 24px;
  justify-self: center; /* 在 Grid 列中居中 */
  max-width: max-content; /* 根据内容自适应，不扩展 */
}

/* 搜索框容器 */
.search-wrapper {
  position: relative;
  min-width: 180px;
  max-width: 240px;
}

/* 搜索输入框包装器 */
.search-input-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-full);
  padding: 6px 14px;
  transition: all 0.2s ease;
  cursor: text;
}

.search-input-wrapper:hover {
  border-color: var(--color-primary);
  background: var(--color-surface-dim);
}

.search-input-wrapper.focused {
  border-color: var(--color-primary);
  background: var(--color-surface-dim);
  box-shadow: 0 0 0 3px rgba(var(--color-primary-rgb, 59, 130, 246), 0.1);
}

/* 搜索图标 */
.search-icon {
  width: 16px;
  height: 16px;
  color: var(--color-ink-muted);
  flex-shrink: 0;
  stroke-width: 2;
}

/* 搜索输入框 */
.search-input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  font-family: var(--font-body);
  color: var(--color-ink);
  min-width: 0;
}

.search-input::placeholder {
  color: var(--color-ink-faint);
}

/* 清除按钮 */
.clear-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  border: none;
  background: transparent;
  color: var(--color-ink-muted);
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.2s ease;
  flex-shrink: 0;
  padding: 0;
}

.clear-btn:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.clear-btn svg {
  width: 12px;
  height: 12px;
  stroke-width: 2;
}

/* ==================== 搜索下拉内容 ==================== */
.search-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  z-index: 100;
  min-width: 280px;
  max-width: 400px;
}

.search-dropdown-content {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  padding: 16px;
}

/* 暗色模式 */
.dark .search-dropdown-content {
  background: rgba(24, 24, 28, 0.85);
  border-color: rgba(255, 255, 255, 0.08);
}

/* 下拉动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s ease;
  transform-origin: top center;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(0.95);
}

.dropdown-enter-to,
.dropdown-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}

/* 搜索历史区域 */
.search-history-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.search-history-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-ink-muted);
}

/* 历史标签 - 横向排列自动换行 */
.search-history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.history-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  font-size: 13px;
  color: var(--color-ink-light);
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.history-tag:hover {
  color: var(--color-primary);
  background: var(--color-primary-light);
  border-color: var(--color-primary);
  transform: translateY(-1px);
}

/* 空状态 */
.search-history-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  color: var(--color-ink-faint);
}

.search-history-empty svg {
  width: 48px;
  height: 48px;
  stroke-width: 1.5;
}

.search-history-empty span {
  font-size: 13px;
}

/* 导航菜单 */
.nav-menu {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link {
  position: relative;
  padding: 8px 16px;
  font-size: 15px;
  font-weight: 400;
  color: var(--color-ink-light);
  text-decoration: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  white-space: nowrap;
  transition: color 0.25s ease, background 0.25s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  letter-spacing: 0.5px;
}

.nav-link:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.nav-link.active {
  color: var(--color-primary);
  font-weight: 600;
}

.dropdown-icon {
  width: 14px;
  height: 14px;
  color: var(--color-ink-muted);
  stroke-width: 1.5;
}

/* ==================== 右侧：功能区 ==================== */
.nav-right {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: max-content; /* 根据内容自适应，不被压缩 */
  justify-content: flex-end;
}

/* 主题切换按钮 */
.theme-toggle {
  color: var(--color-ink-light);
  transition: all 0.3s ease;
}

.theme-toggle:hover {
  color: var(--color-accent);
  background: var(--color-surface-dim);
}

.theme-icon {
  width: 20px;
  height: 20px;
  stroke-width: 1.5;
}

.icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  border: none;
  background: transparent;
  color: var(--color-ink-muted);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.icon-btn:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.action-icon {
  width: 20px;
  height: 20px;
  stroke-width: 1.5;
}

.user-avatar {
  cursor: pointer;
  transition: transform 0.2s ease;
  flex-shrink: 0;
}

.user-avatar:hover {
  transform: scale(1.08);
}

.login-btn {
  padding: 8px 24px;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-surface);
  background: var(--color-ink);
  border: none;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all 0.25s ease;
  font-family: var(--font-body);
  letter-spacing: 1px;
  white-space: nowrap;
  /* 固定最小宽度，避免字体加载导致的偏移 */
  min-width: 80px;
  text-align: center;
}

.login-btn:hover {
  background: var(--color-primary);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}
</style>
