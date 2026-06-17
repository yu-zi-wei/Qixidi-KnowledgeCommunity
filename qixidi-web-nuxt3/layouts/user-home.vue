<template>
  <div class="user-home-page">
    <div class="user-home-container">
      <div class="user-home-layout">
        <!-- 左侧 -->
        <div class="user-home-left">
          <!-- 用户信息头部 + 菜单（合为一个卡片） -->
          <div class="user-header-card" v-if="userInfo">
            <div class="user-header-body">
              <NuxtLink to="/" class="back-home-link">
                <n-icon size="14"><ArrowLeft /></n-icon>
                主页
              </NuxtLink>
              <div class="user-avatar-wrap">
                <img v-if="userInfo.avatar" :src="userInfo.avatar" :alt="userInfo.nickname || userInfo.username" class="user-home-avatar" />
                <div v-else class="user-avatar user-avatar-placeholder">
                  <n-icon size="36"><User /></n-icon>
                </div>
                <span class="owner-badge" v-if="isOwner">本人</span>
              </div>
              <div class="user-main">
                <h1 class="user-nickname">
                  {{ userInfo.nickname || userInfo.username }}
                  <UserRoleBadge :role-id="userInfo.roleId" />
                </h1>
                <p class="user-introduce" v-if="userInfo.introduce">{{ userInfo.introduce }}</p>
                <div class="user-meta">
                  <span v-if="userInfo.occupation" class="meta-tag">
                    <n-icon size="12"><Briefcase /></n-icon>{{ userInfo.occupation }}
                  </span>
                  <span v-if="userInfo.location" class="meta-tag">
                    <n-icon size="12"><MapPin /></n-icon>{{ userInfo.location }}
                  </span>
                  <span v-if="userInfo.createTime" class="meta-tag" :title="getFullDateTime(userInfo.createTime)">
                    <n-icon size="12"><Calendar /></n-icon>
                    {{ userInfo.createTime ? userInfo.createTime.substring(0, 10) : '' }}
                  </span>
                </div>
              </div>
              <div class="user-header-actions">
                <n-button
                  v-if="isOwner"
                  size="small"
                  @click="navigateTo('/settings')"
                >
                  个人信息设置
                </n-button>
                <template v-if="!isOwner">
                  <n-button
                    :type="userInfo.isFollow ? 'tertiary' : 'primary'"
                    size="small"
                    round
                    :loading="followLoading"
                    @click="handleFollow"
                  >
                    {{ userInfo.isFollow ? '已关注' : '+ 关注' }}
                  </n-button>
                  <n-button
                    size="small"
                    round
                    :loading="pmLoading"
                    @click="handlePrivateMsg"
                  >
                    私信
                  </n-button>
                </template>
              </div>
            </div>
            <!-- 统计数据 -->
            <div class="user-stats-row" v-if="userInfo">
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.articleCount || 0 }}</span>
                <span class="user-stat-label">文章</span>
              </div>
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.dictumCount || 0 }}</span>
                <span class="user-stat-label">随笔</span>
              </div>
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.timeNotesCount || 0 }}</span>
                <span class="user-stat-label">小记</span>
              </div>
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.albumCount || 0 }}</span>
                <span class="user-stat-label">专辑</span>
              </div>
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.specialColumnCount || 0 }}</span>
                <span class="user-stat-label">专栏</span>
              </div>
              <div class="user-stat">
                <span class="user-stat-val">{{ userInfo.fansFollowCount || 0 }}</span>
                <span class="user-stat-label">关注者</span>
              </div>
            </div>

            <!-- 菜单合并进头部卡片 -->
            <div class="user-menu" v-if="menuList.length">
              <NuxtLink
                v-for="menu in menuList"
                :key="menu.id"
                :to="`${menu.route}/${uid}`"
                class="menu-item"
                :class="{ active: isMenuActive(menu.route) }"
              >
                {{ menu.navigationName }}
              </NuxtLink>
            </div>
          </div>

          <!-- 内容区域（可滚动） -->
          <div class="user-content">
            <slot />
          </div>
        </div>

        <!-- 右侧 -->
        <div class="user-home-right">
          <div class="stats-card" v-if="userInfo">
            <h3 class="stats-title">数据统计</h3>
            <div class="stats-grid">
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.articleCount || 0 }}</span>
                <span class="stat-label">文章</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.dictumCount || 0 }}</span>
                <span class="stat-label">随笔</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.timeNotesCount || 0 }}</span>
                <span class="stat-label">小记</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.albumCount || 0 }}</span>
                <span class="stat-label">专辑</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.specialColumnCount || 0 }}</span>
                <span class="stat-label">专栏</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ userInfo.fansFollowCount || 0 }}</span>
                <span class="stat-label">关注者</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, User, Briefcase, MapPin, Calendar } from '@vicons/tabler'

const route = useRoute()
const authStore = useAuthStore()
const specialDetailApi = useSpecialDetailApi()
const navigationApi = useNavigationApi()

const uid = computed(() => route.params.uid as string)
const currentPath = computed(() => route.path)

const isOwner = computed(() => authStore.user?.uuid && authStore.user.uuid === uid.value)

// --- 用户信息（SSR） ---
const { data: userInfo, refresh: refreshUserInfo } = await useAsyncData(
  `user-home-info-${uid.value}`,
  () => specialDetailApi.getUserInfo(uid.value),
  { watch: [uid] }
)

// 登录后刷新用户信息（isFollow 字段依赖登录态）
watch(() => authStore.isLoggedIn, (val) => {
  if (val) refreshUserInfo()
})

// --- 个人主页菜单 ---
const { data: menuData, refresh: refreshMenu } = await useAsyncData(
  'user-home-menu',
  async () => {
    try {
      const { rows } = await navigationApi.getList(2, 0)
      return rows || []
    } catch (e) {
      return []
    }
  },
  { default: () => [] as any }
)

// 客户端兜底：SSR 失败导致菜单为空时，重新获取
onMounted(async () => {
  if (!menuData.value || menuData.value.length === 0) {
    await refreshMenu()
  }
})
const menuList = computed(() => {
  const list = menuData.value || []
  return list.filter((m: any) => {
    const j = m.jurisdiction || 0
    // 0: 公开，所有人可见
    if (j === 0) return true
    // 1: 仅自己可见
    if (j === 1) return isOwner.value
    // 2: 登录用户可见
    if (j === 2) return authStore.isLoggedIn
    // 3: VIP 可见（暂按登录用户处理）
    if (j === 3) return authStore.isLoggedIn
    return true
  })
})

const isMenuActive = (menuRoute: string) => {
  return currentPath.value.startsWith(menuRoute)
}

// 根据当前路由匹配菜单名称，设置页面标题：个人主页-菜单名称-栖息地
const pageTitle = computed(() => {
  const path = currentPath.value
  let menuName = ''
  if (menuData.value) {
    const menu = (menuData.value as any[]).find((m: any) => path.startsWith(m.route))
    if (menu) menuName = menu.navigationName
  }
  if (path.includes('/lately')) menuName = '最近'
  return menuName ? `个人主页-${menuName}` : '个人主页'
})

useHead(() => ({ title: pageTitle.value }))

// 关注
const followApi = useFollowApi()
const followLoading = ref(false)
const handleFollow = async () => {
  if (!authStore.isLoggedIn) {
    const authDialogStore = useAuthDialogStore()
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }
  if (!userInfo.value) return
  followLoading.value = true
  try {
    if (userInfo.value.isFollow) {
      await followApi.cancelFollow(uid.value, 1)
      userInfo.value.isFollow = false
    } else {
      await followApi.addFollow(uid.value, 1)
      userInfo.value.isFollow = true
    }
  } catch {
    // useApi 统一处理错误
  } finally {
    followLoading.value = false
  }
}

// 私信
const privateApi = usePrivateMessageApi()
const pmLoading = ref(false)
const handlePrivateMsg = async () => {
  if (!authStore.isLoggedIn) {
    const authDialogStore = useAuthDialogStore()
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }
  pmLoading.value = true
  try {
    await privateApi.addUser(uid.value)
    navigateTo({ path: '/news', query: { type: 4, uid: uid.value } })
  } catch {
    // useApi 统一处理错误
  } finally {
    pmLoading.value = false
  }
}
</script>

<style scoped>
.user-home-page {
  height: 100vh;
  overflow: hidden;
  background: var(--color-surface-warm);
}

.user-home-container {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  height: 100%;
  padding: 16px 24px;
}

.user-home-layout {
  display: flex;
  gap: 20px;
  height: 100%;
}

.user-home-left {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 100%;
  overflow: hidden;
}

.user-home-right {
  width: 260px;
  flex-shrink: 0;
}

/* ======== 用户头部 + 菜单合并卡片 ======== */
.user-header-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.user-header-body {
  display: flex;
  gap: 14px;
  padding: 32px 20px 22px;
  position: relative;
}

.back-home-link {
  position: absolute;
  top: 10px;
  right: 16px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.back-home-link:hover {
  color: var(--color-primary);
}

.user-avatar-wrap {
  position: relative;
  flex-shrink: 0;
  width: 52px;
  height: 52px;
}

.user-home-avatar {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-full);
  object-fit: cover;
  border: 2px solid var(--color-border-light);
  display: block;
}

.user-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary);
  color: #fff;
}

.owner-badge {
  position: absolute;
  bottom: 0;
  right: -4px;
  padding: 0 5px;
  font-size: 10px;
  line-height: 14px;
  color: #fff;
  background: var(--color-primary);
  border-radius: var(--radius-full);
  border: 2px solid var(--color-surface);
  font-weight: 500;
}

.user-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 4px;
}

.user-nickname {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: var(--text-lg);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-introduce {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-meta {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  margin-top: 2px;
}

.meta-tag {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 11px;
  padding: 1px 8px;
  border-radius: var(--radius-full);
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.user-header-actions {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  flex-shrink: 0;
  gap: 6px;
}

/* ======== 菜单（卡片内部） ======== */
.user-menu {
  display: flex;
  border-top: 1px solid var(--color-border-light);
  overflow-x: auto;
  padding: 0 4px;
}

.menu-item {
  padding: 10px 18px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  text-decoration: none;
  white-space: nowrap;
  border-bottom: 2px solid transparent;
  transition: all var(--transition-fast);
}

.menu-item:hover {
  color: var(--color-primary);
}

.menu-item.active {
  color: var(--color-primary);
  font-weight: 600;
  border-bottom-color: var(--color-primary);
}

/* ======== 内容区域（flex 容器，子页面自行管理滚动） ======== */
.user-content {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-right: 4px;
}

/* ======== 右侧统计卡片 ======== */
.stats-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: 18px;
  position: sticky;
  top: 16px;
}

.stats-title {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink-light);
  margin: 0 0 14px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.profile-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 4px;
  border-radius: var(--radius-md);
  transition: background var(--transition-fast);
}

.profile-stat:hover {
  background: var(--color-surface-dim);
}

.stat-value {
  font-size: var(--text-xl);
  font-weight: 700;
  color: var(--color-ink);
  line-height: 1;
}

.stat-label {
  font-size: 11px;
  color: var(--color-ink-faint);
}

/* ======== 头部内嵌统计（移动端显示） ======== */
.user-stats-row {
  display: none;
}

.user-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.user-stat-val {
  font-size: var(--text-lg);
  font-weight: 700;
  color: var(--color-ink);
}

.user-stat-label {
  font-size: 11px;
  color: var(--color-ink-faint);
}

/* ======== 响应式 ======== */
@media (max-width: 768px) {
  .user-home-page {
    height: auto;
    min-height: 100vh;
    overflow: auto;
  }

  .user-home-container {
    padding: 12px;
    height: auto;
  }

  .user-home-layout {
    flex-direction: column;
    height: auto;
  }

  .user-home-left {
    height: auto;
    overflow: visible;
  }

  .user-home-right {
    display: none;
  }

  .user-stats-row {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 4px;
    padding: 12px 20px;
    border-top: 1px solid var(--color-border-light);
  }

  .user-stat-val {
    font-size: var(--text-base);
  }

  .user-content {
    overflow: visible;
    padding-right: 0;
  }
}
</style>
