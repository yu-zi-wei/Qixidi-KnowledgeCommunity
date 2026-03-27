<template>
  <aside v-if="isReady" ref="sidebarRef" class="article-sidebar" :class="{ 'is-visible': isVisible }">
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

    <!-- 作者信息占位（保持目录位置不变） -->
    <div v-else class="author-card-placeholder"></div>

    <!-- 文章目录 -->
    <ArticleToc v-if="showToc && articleContent" :content="articleContent" class="toc-section" />
  </aside>

  <!-- 加载占位 - 精致骨架屏 -->
  <aside v-else class="article-sidebar placeholder">
    <div class="skeleton-card">
      <div class="skeleton-header">
        <div class="skeleton-avatar"></div>
        <div class="skeleton-info">
          <div class="skeleton-name"></div>
          <div class="skeleton-title"></div>
        </div>
      </div>
      <div class="skeleton-stats">
        <div class="skeleton-stat"></div>
        <div class="skeleton-stat"></div>
        <div class="skeleton-stat"></div>
      </div>
      <div class="skeleton-btn"></div>
    </div>
    <div class="skeleton-toc">
      <div class="skeleton-toc-item"></div>
      <div class="skeleton-toc-item short"></div>
      <div class="skeleton-toc-item"></div>
      <div class="skeleton-toc-item short"></div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { UserPlus, UserCheck } from '@vicons/tabler'

// 从 useState 获取侧边栏数据
const sidebarData = useState('article-sidebar-data', () => ({
  author: null,
  articleContent: '',
  showToc: true
}))

// 检查是否准备好数据（有作者信息或有文章内容即可显示）
const isReady = computed(() => sidebarData.value.author !== null || sidebarData.value.articleContent !== '')
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

// 动画相关
const sidebarRef = ref<HTMLElement | null>(null)
const isVisible = ref(false)

onMounted(() => {
  // 延迟触发动画
  setTimeout(() => {
    isVisible.value = true
  }, 100)
})
</script>

<style scoped>
/* 入场动画 */
@keyframes sidebar-fade-in {
  from {
    opacity: 0;
    transform: translateX(16px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.article-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
  /* 初始状态 */
  opacity: 0;
  transform: translateX(16px);
}

.article-sidebar.is-visible {
  animation: sidebar-fade-in 0.5s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

/* 骨架屏占位 */
.article-sidebar.placeholder {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.skeleton-card {
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.dark .skeleton-card {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(255, 255, 255, 0.06);
}

.skeleton-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.skeleton-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.04) 25%, rgba(0, 0, 0, 0.06) 50%, rgba(0, 0, 0, 0.04) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-avatar {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.04) 25%, rgba(255, 255, 255, 0.08) 50%, rgba(255, 255, 255, 0.04) 75%);
}

.skeleton-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.skeleton-name {
  width: 70%;
  height: 16px;
  border-radius: 4px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.04) 25%, rgba(0, 0, 0, 0.06) 50%, rgba(0, 0, 0, 0.04) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-name {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.04) 25%, rgba(255, 255, 255, 0.08) 50%, rgba(255, 255, 255, 0.04) 75%);
}

.skeleton-title {
  width: 50%;
  height: 12px;
  border-radius: 4px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.03) 25%, rgba(0, 0, 0, 0.05) 50%, rgba(0, 0, 0, 0.03) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-title {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.03) 25%, rgba(255, 255, 255, 0.06) 50%, rgba(255, 255, 255, 0.03) 75%);
}

.skeleton-stats {
  display: flex;
  gap: 20px;
  padding: 12px 0;
  margin-bottom: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .skeleton-stats {
  border-top-color: rgba(255, 255, 255, 0.06);
  border-bottom-color: rgba(255, 255, 255, 0.06);
}

.skeleton-stat {
  width: 40px;
  height: 28px;
  border-radius: 4px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.03) 25%, rgba(0, 0, 0, 0.05) 50%, rgba(0, 0, 0, 0.03) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-stat {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.03) 25%, rgba(255, 255, 255, 0.06) 50%, rgba(255, 255, 255, 0.03) 75%);
}

.skeleton-btn {
  width: 100%;
  height: 34px;
  border-radius: 16px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.03) 25%, rgba(0, 0, 0, 0.05) 50%, rgba(0, 0, 0, 0.03) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-btn {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.03) 25%, rgba(255, 255, 255, 0.06) 50%, rgba(255, 255, 255, 0.03) 75%);
}

/* 目录骨架 */
.skeleton-toc {
  padding-left: 28px;
  margin-left: 8px;
  border-left: 2px solid rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.dark .skeleton-toc {
  border-left-color: rgba(255, 255, 255, 0.06);
}

.skeleton-toc-item {
  width: 80%;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.03) 25%, rgba(0, 0, 0, 0.05) 50%, rgba(0, 0, 0, 0.03) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.dark .skeleton-toc-item {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.03) 25%, rgba(255, 255, 255, 0.06) 50%, rgba(255, 255, 255, 0.03) 75%);
}

.skeleton-toc-item.short {
  width: 55%;
}

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* 目录区域动画延迟 */
.toc-section {
  animation-delay: 0.1s;
}
</style>

<style scoped>
.article-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 作者信息 - 轻微背景 */
.author-card {
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.dark .author-card {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(255, 255, 255, 0.06);
}

.author-link {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: inherit;
  margin-bottom: 12px;
}

.author-avatar {
  width: 42px;
  height: 42px;
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
  transition: color 0.2s ease;
}

.author-link:hover .author-name {
  color: var(--color-primary);
}

.author-title {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.author-stats {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 20px;
  padding: 12px 0;
  margin-bottom: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .author-stats {
  border-top-color: rgba(255, 255, 255, 0.06);
  border-bottom-color: rgba(255, 255, 255, 0.06);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1px;
}

.stat-num {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-ink);
}

.stat-label {
  font-size: 11px;
  color: var(--color-ink-muted);
}

.stat-divider {
  display: none;
}

.follow-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-primary);
  background: transparent;
  border: 1px solid var(--color-primary);
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.follow-btn:hover {
  background: var(--color-primary);
  color: #fff;
}

.follow-btn.following {
  color: var(--color-ink-muted);
  background: transparent;
  border-color: rgba(0, 0, 0, 0.1);
}

.follow-btn.following:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
  background: transparent;
}

.dark .follow-btn.following {
  border-color: rgba(255, 255, 255, 0.1);
}

.icon {
  width: 16px;
  height: 16px;
  stroke-width: 1.5;
}

/* 作者信息占位（保持目录位置不变） */
.author-card-placeholder {
  height: 180px;
  margin-bottom: 20px;
}
</style>
