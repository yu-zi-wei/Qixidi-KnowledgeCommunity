<template>
  <!-- 导航栏 -->
  <header class="home-nav-bar" :class="{ 'is-sticky': isSticky }">
    <!-- 移动端：汉堡菜单 -->
    <button class="mobile-menu-btn" @click="showMobileMenu = true">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <line x1="3" y1="12" x2="21" y2="12"></line>
        <line x1="3" y1="6" x2="21" y2="6"></line>
        <line x1="3" y1="18" x2="21" y2="18"></line>
      </svg>
    </button>

    <!-- 左侧：Logo -->
    <div class="nav-left">
      <NuxtLink to="/" class="nav-logo">
        <svg class="logo-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
          <path d="M844.288 514.56c-52.736-27.392-201.216-35.84-201.216-35.84s152.576-4.352 240.896-33.536c0 0 102.656-40.96 68.864-162.048 0 0-20.736-63.488-132.864-75.264 0 0 9.984-83.968-70.656-132.352 0 0-56.832-34.304-141.056 17.92-71.936 52.224-80.64 234.752-81.152 246.272 0.512-11.008 6.144-177.664-33.792-249.088 0 0-42.752-99.072-164.096-48.128 0 0-102.912 33.024-81.664 128 0 0-150.272-31.744-171.008 111.104 0 0-17.92 116.224 109.568 141.568 41.472 10.24 182.016 32.768 182.016 32.768S79.872 429.312 65.792 569.344c0 0-24.32 131.584 125.184 128.256 0 0-16.128 98.816 79.36 131.328 0.256-0.256 127.232 36.608 187.648-124.928 13.312-37.632 24.576-63.488 30.976-128.256 0 0 2.304 256.512-214.272 387.072l72.192 43.008s157.696-154.88 160-409.6c-0.256-13.568-0.256-21.76-0.256-21.76 0.256 7.424 0.256 14.592 0.256 21.76 1.024 50.176 8.704 173.056 52.992 219.392 0 0 63.232 87.04 172.544 41.984 0 0 65.28-23.808 65.792-109.824 0 0 100.864 15.104 122.88-88.832-0.512 0 29.184-91.648-76.8-144.384z m0 0" fill="#3d5a80"/>
        </svg>
        <span class="logo-text">四叶集</span>
      </NuxtLink>
    </div>

    <!-- 中间：搜索框 + 导航菜单 -->
    <div class="nav-center">
      <!-- 搜索框 -->
      <div class="search-wrapper" ref="searchWrapperRef" v-click-outside="() => showDropdown = false">
        <div
          class="search-input-wrapper"
          :class="{ focused: showDropdown }"
          @click="handleSearchClick"
        >
          <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
          <input
            ref="searchInputRef"
            v-model="searchKeyword"
            type="text"
            placeholder="搜索..."
            class="search-input"
            @focus="handleSearchFocus"
            @keydown.enter="handleSearch"
          />
        </div>

        <!-- 搜索下拉 -->
        <Transition name="dropdown">
          <div v-if="showDropdown" class="search-dropdown">
            <div class="search-dropdown-content">
              <div v-if="searchHistory.length > 0" class="search-history-section">
                <div class="search-history-title">搜索历史</div>
                <div class="search-history-tags">
                  <NuxtLink
                    v-for="item in searchHistory"
                    :key="item.content"
                    :to="searchTargetPath(item.content)"
                    class="history-tag"
                    @click="showDropdown = false"
                  >
                    {{ item.content }}
                  </NuxtLink>
                </div>
              </div>
              <div v-else class="search-history-empty">
                <span>暂无搜索历史</span>
              </div>
            </div>
          </div>
        </Transition>
      </div>

      <!-- 导航菜单（PC端） -->
      <nav class="nav-menu">
        <template v-for="item in plainMenus" :key="item.id">
          <NuxtLink :to="item.route" class="nav-link" :class="{ active: isActive(item.route) }">
            {{ item.navigationName }}
          </NuxtLink>
        </template>

        <template v-for="item in dropdownMenus" :key="item.id">
          <n-dropdown :options="buildDropdownOptions(item)" @select="handleDropdownSelect">
            <span class="nav-link" :class="{ active: isDropdownActive(item) }">
              {{ item.navigationName }}
              <svg class="dropdown-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
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
        <svg v-show="colorMode.value === 'dark'" class="theme-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
        </svg>
        <svg v-show="colorMode.value !== 'dark'" class="theme-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
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
            <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
          </button>
        </n-dropdown>

        <NuxtLink to="/notifications" class="icon-btn" title="通知">
          <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
          </svg>
        </NuxtLink>

        <n-dropdown :options="userMenuOptions" @select="handleUserMenu">
          <n-avatar round :size="32" :src="authStore.user?.avatar" fallback-src="/img/tx.jpg" class="user-avatar" />
        </n-dropdown>
      </template>

      <template v-else>
        <button class="login-btn" @click="handleShowLogin">登录</button>
      </template>
    </div>

    <!-- 移动端菜单抽屉（内嵌） -->
    <ClientOnly>
      <Teleport to="body">
        <Transition name="slide">
          <div v-if="showMobileMenu" class="mobile-menu-overlay" @click="showMobileMenu = false">
            <div class="mobile-menu-drawer" @click.stop>
              <div class="mobile-menu-header">
                <span class="menu-title">菜单</span>
                <button class="close-btn" @click="showMobileMenu = false">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                  </svg>
                </button>
              </div>
              <nav class="mobile-nav-list">
                <template v-for="item in plainMenus" :key="item.id">
                  <div class="mobile-nav-item" @click="handleMobileNavClick(item.route)">
                    {{ item.navigationName }}
                  </div>
                </template>
                <template v-for="item in dropdownMenus" :key="item.id">
                  <div class="mobile-nav-dropdown">
                    <div class="mobile-nav-item mobile-nav-dropdown-header" @click="toggleMobileDropdown(item.id)">
                      <span>{{ item.navigationName }}</span>
                      <svg class="dropdown-arrow" :class="{ expanded: expandedMobileDropdowns.has(item.id) }" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="6 9 12 15 18 9"></polyline>
                      </svg>
                    </div>
                    <div v-show="expandedMobileDropdowns.has(item.id)" class="mobile-nav-sublist">
                      <div
                        v-for="sub in item.levelList"
                        :key="sub.route"
                        class="mobile-nav-subitem"
                        @click="handleMobileNavClick(sub.route)"
                      >
                        {{ sub.navigationName }}
                      </div>
                    </div>
                  </div>
                </template>
              </nav>
            </div>
          </div>
        </Transition>
      </Teleport>
    </ClientOnly>

    <n-modal v-model:show="showLogoutConfirm" preset="dialog" type="warning"
      title="退出登录" content="确定要离开吗？"
      positive-text="确定" negative-text="取消"
      @positive-click="doLogout"
    />
  </header>
</template>

<script setup lang="ts">
import type { Navigation } from '~/types'
import { useEssayDrawerStore } from '~/stores/essayDrawer'

interface Props {
  navigationList: Navigation[]
}

const props = defineProps<Props>()

const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const colorMode = useColorMode()

// 移动端菜单状态（内聚）
const showMobileMenu = ref(false)
const expandedMobileDropdowns = ref<Set<number>>(new Set())

const toggleMobileDropdown = (id: number) => {
  if (expandedMobileDropdowns.value.has(id)) {
    expandedMobileDropdowns.value.delete(id)
  } else {
    expandedMobileDropdowns.value.add(id)
  }
}

const handleMobileNavClick = (path: string) => {
  showMobileMenu.value = false
  expandedMobileDropdowns.value.clear()
  navigateTo(path)
}

// 主题
const toggleTheme = () => {
  colorMode.preference = colorMode.value === 'dark' ? 'light' : 'dark'
}
const themeTitle = computed(() => colorMode.value === 'dark' ? '切换浅色模式' : '切换深色模式')

// 登录
const handleShowLogin = () => authDialogStore.showLoginDialog()

// 搜索
const searchApi = useSearchApi()
const searchWrapperRef = ref<HTMLElement>()
const searchInputRef = ref<HTMLInputElement>()
const searchKeyword = ref('')
const searchHistory = ref<{ content: string }[]>([])
const searchHistoryLoaded = ref(false)
const showDropdown = ref(false)

const loadSearchHistory = async () => {
  if (!authStore.isLoggedIn || searchHistoryLoaded.value) return
  try {
    const result = await searchApi.getSearchHistory({ pageNum: 1, pageSize: 10, uid: authStore.user?.uuid })
    searchHistory.value = result.rows || []
    searchHistoryLoaded.value = true
  } catch (e) {
    console.error('加载搜索历史失败:', e)
  }
}

const handleSearchClick = () => {
  loadSearchHistory()
  showDropdown.value = true
  searchInputRef.value?.focus()
}

const handleSearchFocus = () => {
  loadSearchHistory()
  showDropdown.value = true
}

const handleSearch = () => {
  if (!searchKeyword.value.trim()) return
  showDropdown.value = false
  navigateTo(searchTargetRoute(searchKeyword.value.trim()))
}

const searchTargetRoute = (q: string) => {
  const currentPath = route.path
  const searchPath = currentPath.startsWith('/search/') ? currentPath : '/search/article'
  return { path: searchPath, query: { q } }
}

const searchTargetPath = (q: string) => searchTargetRoute(q)

// 导航菜单
const plainMenus = computed(() => props.navigationList.filter(item => item.isList === 0))
const dropdownMenus = computed(() => props.navigationList.filter(item => item.isList === 1))

const isActive = (path: string) => route.path === path
const isDropdownActive = (item: Navigation) => item.levelList?.some(sub => route.path === sub.route)

const buildDropdownOptions = (item: Navigation) => item.levelList?.map(sub => ({ label: sub.navigationName, key: sub.route })) || []
const handleDropdownSelect = (key: string) => navigateTo(key)

// 发布
const publishOptions = [
  { label: '写文章', key: 'article' },
  { label: '记随笔', key: 'essay' },
  { label: '时光小记', key: 'note' }
]

const handlePublish = (key: string) => {
  if (key === 'essay') {
    const essayDrawerStore = useEssayDrawerStore()
    essayDrawerStore.open()
  } else {
    const routes: Record<string, string> = { article: '/write/article', note: '/write/note' }
    navigateTo(routes[key])
  }
}

// 用户菜单
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

const handleUserMenu = (key: string) => {
  if (key === 'logout') { showLogoutConfirm.value = true; return }
  const routes: Record<string, string> = { admin: '/admin', profile: `/user-home/lately/${authStore.user?.uuid}`, settings: '/settings' }
  if (routes[key]) navigateTo(routes[key])
}

// 吸顶状态
const isSticky = ref(false)

onMounted(() => {
  const checkSticky = () => {
    const nav = document.querySelector('.home-nav-bar')
    if (nav) isSticky.value = nav.getBoundingClientRect().top <= 0
  }
  checkSticky()
  window.addEventListener('scroll', checkSticky, { passive: true })
  onUnmounted(() => window.removeEventListener('scroll', checkSticky))
})
</script>

<style scoped>
/* 导航栏 */
.home-nav-bar {
  position: sticky;
  top: 0;
  z-index: 50;
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 24px;
  padding: 14px 24px;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
  height: 66px;
  border-radius: 32px;
  transition: border-radius 0.3s ease, box-shadow 0.3s ease;
}

.home-nav-bar.is-sticky {
  border-radius: 6px;
  box-shadow: var(--shadow-md);
}

/* Logo */
.nav-left { display: flex; align-items: center; flex-shrink: 0; }
.nav-logo { display: flex; align-items: center; gap: 10px; text-decoration: none; height: 38px; white-space: nowrap; }
.logo-icon { display: flex; align-items: center; justify-content: center; width: 32px; height: 32px; flex-shrink: 0; transition: all 0.2s ease; }
.logo-icon svg { width: 32px; height: 32px; }
.logo-text { font-family: var(--font-display); font-size: 20px; font-weight: 700; color: var(--color-ink); letter-spacing: 2px; line-height: 38px; }

/* 中间区域 */
.nav-center { display: flex; align-items: center; gap: 24px; max-width: max-content; }

/* 搜索框 */
.search-wrapper { position: relative; min-width: 180px; max-width: 240px; }
.search-input-wrapper { display: flex; align-items: center; gap: 8px; background: var(--color-surface); border: 1px solid var(--color-border-light); border-radius: var(--radius-full); padding: 6px 14px; cursor: text; transition: all 0.2s ease; }
.search-input-wrapper:hover { border-color: var(--color-primary); background: var(--color-surface-dim); }
.search-input-wrapper.focused { border-color: var(--color-primary); box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
.search-icon { width: 16px; height: 16px; color: var(--color-ink-muted); stroke-width: 2; }
.search-input { flex: 1; border: none; background: transparent; outline: none; font-size: 14px; color: var(--color-ink); min-width: 0; }
.search-input::placeholder { color: var(--color-ink-faint); }

/* 搜索下拉 */
.search-dropdown { position: absolute; top: calc(100% + 8px); left: 0; z-index: 100; min-width: 280px; }
.search-dropdown-content { background: rgba(255,255,255,0.85); backdrop-filter: blur(10px); border-radius: 12px; border: 1px solid rgba(0,0,0,0.06); box-shadow: 0 8px 30px rgba(0,0,0,0.08); padding: 16px; }
.dark .search-dropdown-content { background: rgba(54,48,44,0.85); border-color: rgba(255,255,255,0.08); }
.search-history-section { display: flex; flex-direction: column; gap: 12px; }
.search-history-title { font-size: 13px; font-weight: 600; color: var(--color-ink-muted); }
.search-history-tags { display: flex; flex-wrap: wrap; gap: 8px; }
.history-tag { padding: 6px 12px; font-size: 13px; color: var(--color-ink-light); background: var(--color-surface-dim); border: 1px solid var(--color-border-light); border-radius: var(--radius-md); cursor: pointer; text-decoration: none; }
.history-tag:hover { color: var(--color-primary); background: var(--color-primary-light); border-color: var(--color-primary); }
.search-history-empty { padding: 24px; text-align: center; color: var(--color-ink-muted); font-size: 13px; }

/* 导航菜单 */
.nav-menu { display: flex; align-items: center; gap: 4px; }
.nav-link { padding: 8px 16px; font-size: 15px; color: var(--color-ink-light); text-decoration: none; border-radius: var(--radius-sm); cursor: pointer; white-space: nowrap; transition: all 0.25s ease; display: inline-flex; align-items: center; gap: 4px; }
.nav-link:hover { color: var(--color-ink); background: var(--color-surface-dim); }
.nav-link.active { color: var(--color-primary); font-weight: 600; }
.dropdown-icon { width: 14px; height: 14px; color: var(--color-ink-muted); }

/* 右侧功能区 */
.nav-right { display: flex; align-items: center; gap: 10px; min-width: 130px; justify-content: flex-end; }
.theme-toggle { color: var(--color-ink-light); transition: all 0.3s ease; }
.theme-toggle:hover { color: var(--color-accent); background: var(--color-surface-dim); }
.theme-icon { width: 20px; height: 20px; stroke-width: 1.5; }
.icon-btn { display: flex; align-items: center; justify-content: center; width: 38px; height: 38px; border: none; background: transparent; color: var(--color-ink-muted); border-radius: var(--radius-md); cursor: pointer; text-decoration: none; transition: all 0.2s ease; }
.icon-btn:hover { background: var(--color-surface-dim); color: var(--color-ink); }
.action-icon { width: 20px; height: 20px; stroke-width: 1.5; }
.user-avatar { cursor: pointer; transition: transform 0.2s ease; }
.user-avatar:hover { transform: scale(1.08); }
.login-btn { padding: 8px 24px; font-size: 14px; font-weight: 500; color: var(--color-surface); background: var(--color-ink); border: none; border-radius: var(--radius-full); cursor: pointer; transition: all 0.25s ease; min-width: 80px; }
.login-btn:hover { background: var(--color-primary); transform: translateY(-1px); box-shadow: var(--shadow-md); }

/* 汉堡菜单按钮 */
.mobile-menu-btn { display: none; align-items: center; justify-content: center; width: 38px; height: 38px; border: none; background: transparent; color: var(--color-ink-light); border-radius: var(--radius-sm); cursor: pointer; }
.mobile-menu-btn:hover { background: var(--color-surface-dim); color: var(--color-ink); }
.mobile-menu-btn svg { width: 22px; height: 22px; stroke-width: 2; }

/* ==================== 移动端菜单抽屉 ==================== */
.mobile-menu-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: 150; background: rgba(0,0,0,0.5); }
.mobile-menu-drawer { position: absolute; top: 0; left: 0; bottom: 0; width: 220px; max-width: 70vw; background: var(--color-surface); display: flex; flex-direction: column; box-shadow: var(--shadow-float); }
.mobile-menu-header { display: flex; align-items: center; justify-content: space-between; padding: 16px; border-bottom: 1px solid var(--color-border-light); }
.menu-title { font-size: 18px; font-weight: 600; color: var(--color-ink); }
.close-btn { display: flex; align-items: center; justify-content: center; width: 36px; height: 36px; border: none; background: transparent; color: var(--color-ink-light); border-radius: var(--radius-md); cursor: pointer; }
.close-btn:hover { background: var(--color-surface-dim); color: var(--color-ink); }
.close-btn svg { width: 20px; height: 20px; }
.mobile-nav-list { flex: 1; overflow-y: auto; padding: 8px 0; }
.mobile-nav-item { display: flex; align-items: center; justify-content: space-between; padding: 14px 20px; font-size: 15px; color: var(--color-ink); cursor: pointer; }
.mobile-nav-item:hover { background: var(--color-surface-dim); }
.mobile-nav-dropdown-header { color: var(--color-ink-light); }
.dropdown-arrow { width: 16px; height: 16px; color: var(--color-ink-muted); transition: transform 0.2s ease; }
.dropdown-arrow.expanded { transform: rotate(180deg); }
.mobile-nav-sublist { padding-left: 20px; background: var(--color-surface-dim); }
.mobile-nav-subitem { padding: 12px 20px 12px 24px; font-size: 14px; color: var(--color-ink-light); cursor: pointer; }
.mobile-nav-subitem:hover { background: var(--color-surface); color: var(--color-ink); }

/* 抽屉动画 */
.slide-enter-active, .slide-leave-active { transition: all 0.3s ease; }
.slide-enter-from, .slide-leave-to { opacity: 0; }
.slide-enter-from .mobile-menu-drawer, .slide-leave-to .mobile-menu-drawer { transform: translateX(-100%); }

/* 搜索下拉动画 */
.dropdown-enter-active, .dropdown-leave-active { transition: all 0.2s ease; transform-origin: top center; }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-8px) scale(0.95); }

/* ==================== 移动端适配 ==================== */
@media (max-width: 768px) {
  .home-nav-bar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    width: 100%;
    grid-template-columns: auto auto 1fr auto;
    gap: 8px;
    padding: 12px 12px 12px 16px;
    padding-right: max(12px, env(safe-area-inset-right));
    z-index: 9999;
    height: 56px;
    border-radius: 0 !important;
    box-sizing: border-box;
  }

  .mobile-menu-btn { display: flex !important; }
  .nav-left { flex-shrink: 0; }
  .nav-center { justify-self: start; min-width: 0; flex-shrink: 1; }
  .nav-menu { display: none !important; }
  .search-wrapper { max-width: 200px !important; min-width: 160px !important; }
  .nav-right { display: flex; align-items: center; gap: 4px; min-width: 68px; flex-shrink: 0; justify-content: flex-end; }
  .theme-toggle, .icon-btn:not(.user-avatar):not([title="通知"]) { display: none; }
  .icon-btn[title="通知"] { width: 36px; height: 36px; }
  .user-avatar { width: 32px !important; height: 32px !important; flex-shrink: 0; }
  .logo-icon svg { width: 28px; height: 28px; }
  .logo-text { font-size: 16px; }
}

@media (max-width: 480px) {
  .home-nav-bar { padding: 10px 12px; }
  .logo-text { display: none; }
}
</style>
