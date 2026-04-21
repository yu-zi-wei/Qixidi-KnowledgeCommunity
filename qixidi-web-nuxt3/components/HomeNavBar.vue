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
        <img src="/images/logo.svg" alt="栖息地" class="logo-icon" />
        <span class="logo-text">{{ siteName }}</span>
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
        <n-dropdown v-if="authStore.isCreator" :options="publishOptions" @select="handlePublish">
          <button class="icon-btn" title="创作">
            <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
          </button>
        </n-dropdown>

        <n-dropdown :options="newsDropdownOptions" @select="handleNewsSelect" :show-arrow="false" placement="bottom-end">
          <button class="icon-btn news-trigger" title="通知">
            <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
              <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
            </svg>
            <span v-if="totalUnread > 0" class="news-badge">{{ totalUnread > 99 ? '99+' : totalUnread }}</span>
          </button>
        </n-dropdown>

        <n-popover v-model:show="showUserPopover" trigger="hover" placement="bottom-end" :show-arrow="false" :width="260" raw :delay="100" :duration="150">
          <template #trigger>
            <n-avatar round :size="32" :src="authStore.user?.avatar" fallback-src="/img/tx.jpg" class="user-avatar" />
          </template>
          <div class="user-popover">
            <!-- 用户信息 -->
            <div class="user-popover-header">
              <n-avatar round :size="40" :src="authStore.user?.avatar" fallback-src="/img/tx.jpg" />
              <div class="popover-user-meta">
                <div class="popover-name-row"><span class="popover-name">{{ authStore.user?.nickname }}</span><UserRoleBadge /></div>
                <span v-if="authStore.user?.occupation" class="popover-job">{{ authStore.user.occupation }}</span>
              </div>
            </div>
            <!-- 统计 -->
            <div class="popover-stats">
              <NuxtLink :to="`/user-home/article/${authStore.user?.uuid}`" class="stat-item">
                <span class="stat-num">{{ userStats?.articleCount || 0 }}</span>
                <span class="stat-label">文章</span>
              </NuxtLink>
              <NuxtLink :to="`/user-home/time-notes/${authStore.user?.uuid}`" class="stat-item">
                <span class="stat-num">{{ userStats?.timeNotesCount || 0 }}</span>
                <span class="stat-label">小记</span>
              </NuxtLink>
              <NuxtLink :to="`/user-home/reading-essays/${authStore.user?.uuid}`" class="stat-item">
                <span class="stat-num">{{ userStats?.dictumCount || 0 }}</span>
                <span class="stat-label">随笔</span>
              </NuxtLink>
              <NuxtLink :to="`/user-home/collection/${authStore.user?.uuid}`" class="stat-item">
                <span class="stat-num">{{ userStats?.collectionCount || 0 }}</span>
                <span class="stat-label">收藏</span>
              </NuxtLink>
            </div>
            <!-- 菜单两列 -->
            <div class="popover-menu-grid">
              <NuxtLink to="/admin" class="popover-menu-item" @click="showUserPopover = false">
                <n-icon size="15"><Dashboard /></n-icon>创作中心
              </NuxtLink>
              <NuxtLink :to="`/user-home/lately/${authStore.user?.uuid}`" class="popover-menu-item" @click="showUserPopover = false">
                <n-icon size="15"><User /></n-icon>我的主页
              </NuxtLink>
              <NuxtLink to="/admin/articles" class="popover-menu-item" @click="showUserPopover = false">
                <n-icon size="15"><News /></n-icon>文章
              </NuxtLink>
              <NuxtLink to="/admin/time-notes" class="popover-menu-item" @click="showUserPopover = false">
                <n-icon size="15"><Clock /></n-icon>小记
              </NuxtLink>
              <NuxtLink to="/admin/essays" class="popover-menu-item" @click="showUserPopover = false">
                <n-icon size="15"><Notebook /></n-icon>随笔
              </NuxtLink>
            </div>
            <!-- 底部 -->
            <div class="popover-footer">
              <NuxtLink to="/settings" class="popover-link-secondary" @click="showUserPopover = false">
                <n-icon size="14"><Settings /></n-icon>我的设置
              </NuxtLink>
              <button class="popover-link-secondary" @click="showLogoutConfirm = true; showUserPopover = false">
                <n-icon size="14"><Logout /></n-icon>退出登录
              </button>
            </div>
          </div>
        </n-popover>
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
import type { UserCensusCount } from '~/types'
import { FileText, Notebook, News, Dashboard, User, Settings, Logout, Clock } from '@vicons/tabler'
import { useEssayDrawerStore } from '~/stores/essayDrawer'

const { siteName } = useRuntimeConfig().public

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
const showUserPopover = ref(false)
const censusApi = useUserCensusApi()
const { data: userStats } = await useAsyncData<UserCensusCount>(
  'user-census-count',
  () => censusApi.getUserCensusCount('')
)

const authApi = useAuthApi()
const showLogoutConfirm = ref(false)

const doLogout = async () => {
  try { await authApi.logout() } catch {}
  authStore.logout()
  showLogoutConfirm.value = false
  navigateTo('/')
}

// 消息通知下拉
const { totalUnread, getUnread: getNewsUnread } = useWebSocket()

const renderNewsLabel = (text: string, count: number) => {
  return () => h('div', { style: 'display: flex; align-items: center; justify-content: space-between; min-width: 100px;' }, [
    h('span', text),
    count > 0 ? h('span', {
      style: 'margin-left: 12px; padding: 1px 6px; font-size: 11px; font-weight: 600; color: #fff; background: var(--color-danger); border-radius: 9px; line-height: 1.4;'
    }, count > 99 ? '99+' : String(count)) : null
  ])
}

const newsDropdownOptions = computed(() => [
  { label: renderNewsLabel('评论', getNewsUnread(1)), key: '1' },
  { label: renderNewsLabel('点赞', getNewsUnread(2)), key: '2' },
  { label: renderNewsLabel('关注', getNewsUnread(3)), key: '3' },
  { label: renderNewsLabel('私信', getNewsUnread(4)), key: '4' },
  { label: renderNewsLabel('系统', getNewsUnread(5)), key: '5' }
])

const handleNewsSelect = (key: string) => {
  const type = Number(key)
  if (type === 1) {
    navigateTo('/news')
  } else {
    navigateTo({ path: '/news', query: { type } })
  }
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
.logo-icon { width: 32px; height: 32px; flex-shrink: 0; object-fit: contain; transition: all 0.2s ease; }
.logo-text { font-family: var(--font-logo); font-size: 20px; font-weight: 400; color: var(--color-ink); letter-spacing: 3px; line-height: 38px; }

/* 中间区域 */
.nav-center { display: flex; align-items: center; gap: 24px; max-width: max-content; }

/* 搜索框 */
.search-wrapper { position: relative; min-width: 180px; max-width: 240px; }
.search-input-wrapper { display: flex; align-items: center; gap: 8px; background: var(--color-surface); border: 1px solid var(--color-border); border-radius: var(--radius-full); padding: 6px 14px; cursor: text; transition: all 0.2s ease; }
.search-input-wrapper:hover { border-color: var(--color-primary); background: var(--color-surface-dim); }
.search-input-wrapper.focused { border-color: var(--color-primary); box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
.search-icon { width: 16px; height: 16px; color: var(--color-ink-muted); stroke-width: 2; }
.search-input { flex: 1; border: none; background: transparent; outline: none; font-size: 14px; color: var(--color-ink); min-width: 0; }
.search-input::placeholder { color: var(--color-ink-faint); }

/* 搜索下拉 */
.search-dropdown { position: absolute; top: calc(100% + 8px); left: 0; z-index: 100; min-width: 280px; }
.search-dropdown-content { background: var(--color-dropdown-bg); backdrop-filter: blur(10px); border-radius: 12px; border: 1px solid var(--color-border-light); box-shadow: var(--shadow-lg); padding: 16px; }
.search-history-section { display: flex; flex-direction: column; gap: 12px; }
.search-history-title { font-size: 13px; font-weight: 600; color: var(--color-ink-muted); }
.search-history-tags { display: flex; flex-wrap: wrap; gap: 8px; }
.history-tag { padding: 6px 12px; font-size: 13px; color: var(--color-ink-light); background: var(--color-surface-dim); border: 1px solid var(--color-border-light); border-radius: var(--radius-md); cursor: pointer; text-decoration: none; }
.history-tag:hover { color: var(--color-primary); background: var(--color-primary-light); border-color: var(--color-primary); }
.search-history-empty { padding: 24px; text-align: center; color: var(--color-ink-muted); font-size: 13px; }

/* 导航菜单 */
.nav-menu { display: flex; align-items: center; gap: 2px; }
.nav-link { padding: 7px 12px; font-size: 14px; color: var(--color-ink-light); text-decoration: none; border-radius: var(--radius-sm); cursor: pointer; white-space: nowrap; transition: all 0.25s ease; display: inline-flex; align-items: center; gap: 4px; }
.nav-link:hover { color: var(--color-ink); background: var(--color-surface-dim); }
.nav-link.active { color: var(--color-primary); font-weight: 600; }
.dropdown-icon { width: 14px; height: 14px; color: var(--color-ink-muted); }

/* 右侧功能区 */
.nav-right { display: flex; align-items: center; gap: 8px; flex-shrink: 0; justify-content: flex-end; }
.theme-toggle { color: var(--color-ink-light); transition: all 0.3s ease; }
.theme-toggle:hover { color: var(--color-accent); background: var(--color-surface-dim); }
.theme-icon { width: 20px; height: 20px; stroke-width: 1.5; }
.icon-btn { display: flex; align-items: center; justify-content: center; width: 36px; height: 36px; border: none; background: transparent; color: var(--color-ink-muted); border-radius: var(--radius-md); cursor: pointer; text-decoration: none; transition: all 0.2s ease; }
.icon-btn:hover { background: var(--color-surface-dim); color: var(--color-ink); }
.action-icon { width: 20px; height: 20px; stroke-width: 1.5; }
.user-avatar { flex-shrink: 0; cursor: pointer; transition: transform 0.2s ease; }
.user-avatar:hover { transform: scale(1.08); }
.login-btn { padding: 7px 18px; font-size: 13px; font-weight: 500; color: var(--color-surface); background: var(--color-ink); border: none; border-radius: var(--radius-full); cursor: pointer; transition: all 0.25s ease; }
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

/* ==================== 消息通知 ==================== */
.news-trigger { position: relative; }

.news-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  font-size: 10px;
  font-weight: 600;
  line-height: 16px;
  text-align: center;
  color: #fff;
  background: var(--color-danger);
  border-radius: 8px;
  pointer-events: none;
}

/* 搜索下拉动画 */
.dropdown-enter-active, .dropdown-leave-active { transition: all 0.2s ease; transform-origin: top center; }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-8px) scale(0.95); }

/* ==================== 用户 Popover ==================== */
.user-popover {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-float);
  border: 1px solid var(--color-border-light);
  overflow: hidden;
}

.user-popover-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 16px 10px;
}

.popover-user-meta {
  display: flex;
  flex-direction: column;
  gap: 1px;
  min-width: 0;
}

.popover-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.popover-name-row {
  display: flex;
  align-items: center;
  gap: 6px;
}

.popover-job {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.popover-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  border-top: 1px solid var(--color-border-light);
  border-bottom: 1px solid var(--color-border-light);
}

.popover-stats .stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 10px 4px;
  text-decoration: none;
  transition: background 0.15s;
}

.popover-stats .stat-item:hover {
  background: var(--color-surface-dim);
}

.popover-stats .stat-num {
  font-size: var(--text-lg);
  font-weight: 700;
  color: var(--color-ink);
}

.popover-stats .stat-label {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 菜单两列网格 */
.popover-menu-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 2px;
  padding: 6px 8px;
}

.popover-menu-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 10px;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all 0.15s;
  white-space: nowrap;
  width: calc(50% - 1px);
}

.popover-menu-item:hover {
  color: var(--color-primary);
  background: var(--color-surface-dim);
}

/* 底部：设置 + 退出 */
.popover-footer {
  display: flex;
  justify-content: space-between;
  padding: 6px 8px 8px;
  border-top: 1px solid var(--color-border-light);
}

.popover-link-secondary {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all 0.15s;
}

.popover-link-secondary:hover {
  color: var(--color-ink-muted);
  background: var(--color-surface-dim);
}

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
  .theme-toggle, .icon-btn:not(.news-trigger) { display: none; }
  .news-trigger { display: flex !important; width: 36px; height: 36px; }
  .news-badge { top: 0; right: 0; }
  .user-avatar { width: 32px !important; height: 32px !important; flex-shrink: 0; }
  .logo-icon { width: 28px; height: 28px; }
  .logo-text { font-size: 16px; }
}

@media (max-width: 480px) {
  .home-nav-bar { padding: 10px 12px; }
  .logo-text { display: none; }
}
</style>
