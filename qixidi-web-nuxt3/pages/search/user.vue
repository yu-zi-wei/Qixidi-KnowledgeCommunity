<template>
  <div>
    <div v-if="searching" class="search-loading">
      <n-spin size="large" />
    </div>
    <div v-else-if="!userList.length" class="search-empty">
      <CommonEmptyState :description="keyword ? '未找到相关用户' : '请输入搜索关键词'" />
    </div>
    <div v-else class="user-results">
      <div v-for="user in userList" :key="user.uuid" class="user-result-card">
        <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="user-result-avatar">
          <img v-if="user.avatar" :src="user.avatar" :alt="user.nickname" />
          <div v-else class="user-result-avatar-placeholder">
            <n-icon size="20"><User /></n-icon>
          </div>
        </NuxtLink>
        <div class="user-result-info">
          <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="user-result-name">
            {{ user.nickname || '用户' }}
          </NuxtLink>
          <p class="user-result-intro" v-if="user.remark">{{ user.remark }}</p>
          <div class="user-result-meta">
            <span v-if="user.occupation" class="user-result-tag">
              <n-icon size="12"><Briefcase /></n-icon>
              {{ user.occupation }}
            </span>
            <span v-if="user.location" class="user-result-tag">
              <n-icon size="12"><MapPin /></n-icon>
              {{ user.location }}
            </span>
          </div>
        </div>
        <n-button
          size="small"
          :type="user.isFollow ? 'tertiary' : 'primary'"
          :loading="user._loading"
          @click="toggleFollow(user)"
        >
          {{ user.isFollow ? '已关注' : '关注' }}
        </n-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { User, Briefcase, MapPin } from '@vicons/tabler'

definePageMeta({ showTabBar: false })

const route = useRoute()
const searchApi = useSearchApi()
const followApi = useFollowApi()
const authStore = useAuthStore()
const message = useMessage()

const keyword = computed(() => (route.query.q as string || '').trim())

interface UserResult {
  uuid: string
  nickname?: string
  avatar?: string
  location?: string
  remark?: string
  occupation?: string
  isFollow?: boolean
  _loading?: boolean
}

const searching = ref(false)
const userList = ref<UserResult[]>([])

const searchUsers = async () => {
  if (!keyword.value) return
  searching.value = true
  try {
    const res = await searchApi.searchUsers(keyword.value)
    userList.value = (res || []).map((u: any) => ({ ...u, _loading: false }))
  } finally {
    searching.value = false
  }
}

const toggleFollow = async (user: UserResult) => {
  if (!authStore.isLoggedIn) {
    message.warning('请先登录')
    return
  }
  user._loading = true
  try {
    if (user.isFollow) {
      await followApi.cancelFollow(user.uuid, 1)
      user.isFollow = false
    } else {
      await followApi.addFollow(user.uuid, 1)
      user.isFollow = true
    }
  } finally {
    user._loading = false
  }
}

watch(keyword, () => searchUsers())
onMounted(() => searchUsers())
</script>

<style>
.user-results {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-result-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  transition: all var(--transition-base);
}

.user-result-card:hover {
  box-shadow: var(--shadow-sm);
}

.user-result-avatar {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-full);
  overflow: hidden;
  flex-shrink: 0;
}

.user-result-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.user-result-avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.user-result-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-result-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-result-name:hover {
  color: var(--color-primary);
}

.user-result-intro {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-result-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.user-result-tag {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 11px;
  padding: 1px 8px;
  border-radius: var(--radius-full);
  color: var(--color-primary);
  background: var(--color-primary-light);
}
</style>
