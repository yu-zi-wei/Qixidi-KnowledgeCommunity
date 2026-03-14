<template>
  <aside v-if="isReady" class="article-sidebar">
    <!-- 作者信息卡片 -->
    <div v-if="author" class="sidebar-card author-card">
      <NuxtLink :to="`/user/${author.userId}`" class="author-link">
        <img :src="author.avatar" :alt="author.nickname" class="author-avatar" />
        <div class="author-info">
          <h4 class="author-name">{{ author.nickname }}</h4>
          <p class="author-title">{{ author.occupation || '博主' }}</p>
        </div>
      </NuxtLink>

      <div class="author-stats">
        <div class="stat-item">
          <span class="stat-num">{{ authorStats.articleCount || 0 }}</span>
          <span class="stat-label">文章</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-num">{{ formatNumber(authorStats.fansFollowCount || 0) }}</span>
          <span class="stat-label">粉丝</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-num">{{ formatNumber(authorStats.fabulousCount || 0) }}</span>
          <span class="stat-label">获赞</span>
        </div>
      </div>

      <button
        class="follow-btn"
        :class="{ following: author.isFollow }"
        @click="toggleFollow"
      >
        <UserPlus v-if="!author.isFollow" class="icon" />
        <UserCheck v-else class="icon" />
        {{ author.isFollow ? '已关注' : '关注' }}
      </button>
    </div>

    <!-- 文章目录 -->
    <ArticleToc v-if="showToc && articleContent" :content="articleContent" />
  </aside>

  <!-- 加载占位 -->
  <aside v-else class="article-sidebar placeholder">
    <div class="sidebar-card skeleton"></div>
    <div class="sidebar-card skeleton"></div>
  </aside>
</template>

<script setup lang="ts">
import { UserPlus, UserCheck } from '@vicons/tabler'

// 从 useState 获取侧边栏数据
const sidebarData = useState('article-sidebar-data', () => ({
  author: null,
  articleContent: '',
  showToc: true
}))

// 检查是否准备好数据
const isReady = computed(() => sidebarData.value.author !== null)
const author = computed(() => sidebarData.value.author)
const articleContent = computed(() => sidebarData.value.articleContent)
const showToc = computed(() => sidebarData.value.showToc)

// 作者统计数据
const authorStats = computed(() => ({
  articleCount: author.value?.articleCount || 0,
  fansFollowCount: author.value?.fansFollowCount || 0,
  fabulousCount: author.value?.fabulousCount || 0
}))

// 格式化数字
const formatNumber = (num: number) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

// 关注/取消关注
const toggleFollow = async () => {
  if (!author.value) return

  const authStore = useAuthStore()
  if (!authStore.isLoggedIn) {
    const authDialogStore = useAuthDialogStore()
    authDialogStore.showLoginDialog()
    return
  }

  // TODO: 调用关注接口
  const newState = !author.value.isFollow
  author.value.isFollow = newState
}
</script>

<style scoped>
.article-sidebar.placeholder {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.skeleton {
  height: 200px;
  background: linear-gradient(90deg, var(--color-surface-dim) 25%, var(--color-border-light) 50%, var(--color-surface-dim) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
</style>

<style scoped>
.article-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 作者信息卡片 */
.author-card {
  padding: 16px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border-light);
}

.author-link {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: inherit;
  margin-bottom: 12px;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.author-info {
  flex: 1;
  min-width: 0;
}

.author-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0 0 2px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.author-title {
  font-size: 13px;
  color: var(--color-ink-muted);
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.author-stats {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 12px 0;
  margin-bottom: 12px;
  border-top: 1px solid var(--color-border-light);
  border-bottom: 1px solid var(--color-border-light);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.stat-num {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
}

.stat-label {
  font-size: 12px;
  color: var(--color-ink-muted);
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: var(--color-border-light);
}

.follow-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 500;
  color: #fff;
  background: var(--color-primary);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.follow-btn:hover {
  background: var(--color-primary-hover);
}

.follow-btn.following {
  background: var(--color-surface-dim);
  color: var(--color-ink-light);
  border: 1px solid var(--color-border);
}

.follow-btn.following:hover {
  background: var(--color-border-light);
}

.icon {
  width: 16px;
  height: 16px;
  stroke-width: 1.5;
}
</style>
