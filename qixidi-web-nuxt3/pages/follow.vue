<template>
  <div>
    <!-- 未登录提示 -->
    <div v-if="!authStore.isLoggedIn" class="login-prompt-card">
      <div class="login-prompt-content">
        <Icon name="mdi:lock-outline" size="32" class="lock-icon" />
        <h3>需要登录</h3>
        <p>登录后查看关注用户的文章动态</p>
        <button class="login-btn-prompt" @click="authDialogStore.showLoginDialog('/follow')">
          立即登录
        </button>
      </div>
    </div>

    <template v-else>
      <!-- 关注用户列表（横向滚动） -->
      <div class="follow-users-card">
        <div class="follow-header">
          <Icon name="mdi:heart-outline" size="16" />
          <span>关注列表</span>
        </div>

        <div v-if="followUsers.length" class="follow-users-scroll">
          <NuxtLink
            v-for="user in followUsers"
            :key="user.userId"
            :to="`/user-home/article/${user.userId}`"
            target="_blank"
            class="follow-user-item"
          >
            <img :src="user.avatar" :alt="user.nickname" class="follow-avatar" />
            <span class="follow-name">{{ user.nickname }}</span>
          </NuxtLink>
        </div>
        <p v-else class="follow-empty">还没有关注任何用户</p>
      </div>

      <!-- 推荐 / 最新 切换标签 -->
      <div class="custom-tabs">
        <button
          v-for="tab in tabs"
          :key="tab.value"
          class="tab-button"
          :class="{ active: activeTab === tab.value }"
          @click="handleTabChange(tab.value)"
        >
          {{ tab.label }}
        </button>
      </div>

      <ArticleList
        :articles="articles"
        :loading="loadingMore"
        :no-more="noMore"
        @load-more="loadMore"
      />
    </template>
  </div>
</template>

<script setup lang="ts">
import type { FollowUser } from '~/types'

definePageMeta({
  showTabBar: true,
  sidebar: 'home',
  middleware: 'auth'
})

const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const articleApi = useArticleApi()
const userApi = useUserApi()
const labelStore = useLabelStore()

// ============ 关注用户列表（需登录） ============
const followUsers = ref<FollowUser[]>([])

// 页面挂载时初始化数据
onMounted(async () => {
  // 加载标签列表
  if (!labelStore.loaded) {
    await labelStore.fetchLabelList()
  }

  // 加载关注用户列表（需要登录）
  if (authStore.isLoggedIn) {
    try {
      const users = await userApi.getFollowList(1)
      followUsers.value = users || []
    } catch {
      followUsers.value = []
    }
  }
})

// ============ 子标签：推荐 / 最新 ============
// 标签配置
const tabs = [
  { label: '最新', value: 'latest', sortType: 2 },  // 按时间
  { label: '推荐', value: 'recommend', sortType: 1 }  // 按权重
]

// 当前激活的标签（默认最新）
const activeTab = ref('latest')

// Store key：根据当前标签动态生成
const storeKey = computed(() => `follow-${activeTab.value}`)

// 使用无限滚动 Hook
const { articles, loadingMore, noMore, loadMore, initArticles } = useInfiniteScroll({
  fetchFn: (page) => articleApi.getFollowList({
    pageNum: page,
    pageSize: 10,
    sortType: activeTab.value === 'latest' ? 2 : 1  // 最新=2, 推荐=1
  }),
  storeKey: storeKey
})

// 标签切换时重新加载数据
const handleTabChange = (value: string) => {
  if (activeTab.value === value) return
  activeTab.value = value
  // 强制重新加载数据
  initArticles(true)
}
</script>

<style scoped>
/* === 登录提示卡片 === */
.login-prompt-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 60px var(--space-5);
  margin-bottom: var(--space-4);
  border: 1px solid var(--color-border-light);
  text-align: center;
}

.login-prompt-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
}

.lock-icon {
  color: var(--color-ink-muted);
  opacity: 0.6;
}

.login-prompt-content h3 {
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.login-prompt-content p {
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  margin: 0;
}

.login-btn-prompt {
  margin-top: var(--space-2);
  padding: var(--space-2) var(--space-6);
  background: var(--color-primary);
  color: white;
  border: none;
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-base);
}

.login-btn-prompt:hover {
  opacity: 0.9;
}

/* === 关注用户列表 === */
.follow-users-card {
  background: transparent;
  border-radius: 0;
  padding: 0;
  margin-bottom: var(--space-4);
  border: none;
}

.follow-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink-muted);
  margin-bottom: var(--space-3);
  padding-bottom: var(--space-2);
  border-bottom: 1px solid var(--color-border-light);
}

.follow-users-scroll {
  display: flex;
  gap: var(--space-3);
  overflow-x: auto;
  padding-bottom: var(--space-2);
  scrollbar-width: none;
}

.follow-users-scroll::-webkit-scrollbar {
  display: none;
}

.follow-user-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
  text-decoration: none;
  transition: all var(--transition-fast);
}

.follow-user-item:hover {
  opacity: 0.7;
}

.follow-avatar {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-full);
  object-fit: cover;
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
}

.follow-name {
  font-size: 11px;
  color: var(--color-ink-muted);
  max-width: 48px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
}

.follow-empty {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  text-align: center;
  padding: var(--space-3) 0;
}

/* === 自定义 Tabs === */
.custom-tabs {
  display: flex;
  gap: 4px;
  margin-bottom: 16px;
  padding: 0 4px;
}

.tab-button {
  padding: 6px 14px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink-light);
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.tab-button:hover {
  color: var(--color-ink);
}

.tab-button.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}
</style>
