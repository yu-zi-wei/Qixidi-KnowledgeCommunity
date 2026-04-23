<template>
  <div class="users-page">
    <div class="page-header">
      <h1 class="page-title">网站用户</h1>
      <span v-if="total > 0" class="user-count">共 {{ total }} 位</span>
    </div>

    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <div v-else-if="!users.length" class="empty-state">
      <CommonEmptyState description="暂无用户" />
    </div>

    <div v-else class="user-grid">
      <div v-for="user in users" :key="user.uuid" class="user-card">
        <div class="user-top">
          <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="user-avatar-link">
            <img :src="user.avatar || '/images/default-avatar.png'" :alt="user.nickname" class="user-avatar" />
          </NuxtLink>
          <div class="user-main">
            <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="user-name">
              {{ user.nickname || user.username }}
            </NuxtLink>
            <div class="user-tags">
              <UserRoleBadge :role-id="user.roleId" />
              <span v-if="user.occupation" class="user-occupation">{{ user.occupation }}</span>
            </div>
            <div class="user-meta">
              <span v-if="user.createTime" class="meta-text">{{ formatJoinTime(user.createTime) }}加入</span>
              <span v-if="user.source" class="meta-text">来自 {{ user.source }}</span>
            </div>
          </div>
          <n-button
            size="small"
            :type="isFollow(user.uuid) ? 'default' : 'primary'"
            :loading="togglingId === user.uuid"
            :disabled="togglingId !== null"
            @click="handleToggle(user)"
          >
            {{ isFollow(user.uuid) ? '已关注' : '关注' }}
          </n-button>
        </div>

        <p v-if="user.introduce" class="user-bio">{{ user.introduce }}</p>

        <div class="user-stats">
          <div class="stat-item">
            <span class="stat-num">{{ user.articleCount || 0 }}</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ user.timeNotesCount || 0 }}</span>
            <span class="stat-label">小记</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ user.dictumCount || 0 }}</span>
            <span class="stat-label">随笔</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ user.fansFollowCount || 0 }}</span>
            <span class="stat-label">粉丝</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ user.fansFabulousCount || 0 }}</span>
            <span class="stat-label">获赞</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
useHead({
  bodyAttrs: {
    class: 'page-users'
  }
})

const siteApi = useSiteApi()
const followApi = useFollowApi()
const authStore = useAuthStore()
const message = useMessage()

const { data: rawData, pending } = await useAsyncData(
  'site-users',
  () => siteApi.getUserAllList()
)

const users = computed(() => rawData.value?.rows || [])
const total = computed(() => rawData.value?.total || 0)

const formatJoinTime = (timestamp: number) => {
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const togglingId = ref<string | null>(null)
const followState = reactive<Record<string, boolean>>({})

const isFollow = (uuid: string) => {
  if (followState[uuid] !== undefined) return followState[uuid]
  const user = users.value.find(u => u.uuid === uuid)
  return !!user?.isFollow
}

const handleToggle = async (user: any) => {
  if (!authStore.isLoggedIn) {
    message.warning('请先登录')
    return
  }
  if (togglingId.value !== null) return
  togglingId.value = user.uuid

  const followed = isFollow(user.uuid)
  try {
    if (followed) {
      await followApi.cancelFollow(user.uuid, 1)
    } else {
      await followApi.addFollow(user.uuid, 1)
    }
    followState[user.uuid] = !followed
  } finally {
    togglingId.value = null
  }
}
</script>

<style>
.users-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-top: 24px;
  padding-left: 8px;
}

.page-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
}

.user-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.user-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.user-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  transition: all var(--transition-base);
}

.user-card:hover {
  box-shadow: var(--shadow-md);
  border-color: var(--color-border);
}

.user-top {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar-link {
  flex-shrink: 0;
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-full);
  object-fit: cover;
  background: var(--color-surface-dim);
}

.user-main {
  flex: 1;
  min-width: 0;
}

.user-name {
  display: block;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-fast);
}

.user-name:hover {
  color: var(--color-primary);
}

.user-tags {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 4px;
}

.user-occupation {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 3px;
}

.meta-text {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.user-bio {
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.user-stats {
  display: flex;
  gap: 4px;
  padding-top: 12px;
  border-top: 1px solid var(--color-border-light);
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 6px 4px;
  border-radius: var(--radius-sm);
  text-decoration: none;
  transition: background var(--transition-fast);
}

.stat-item:hover {
  background: var(--color-surface-dim);
}

.stat-num {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
}

.stat-label {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

@media (max-width: 768px) {
  body.page-users .home-main {
    padding-top: 70px !important;
  }

  .user-grid {
    grid-template-columns: 1fr;
  }
}
</style>
