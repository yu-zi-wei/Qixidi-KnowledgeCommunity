<template>
  <aside v-if="isReady" ref="sidebarRef" class="article-sidebar" :class="{ 'is-visible': isVisible }">
    <!-- 作者信息卡片 -->
    <div v-if="author" class="sidebar-card author-card">
      <NuxtLink :to="`/user-home/article/${author.userId}`" target="_blank" class="author-link">
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

    <!-- 特殊卡片 - 平台介绍 -->
    <div v-else-if="specialCard?.type === 'platform'" class="sidebar-card platform-card">
      <div class="platform-header">
        <svg class="platform-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
          <path d="M844.288 514.56c-52.736-27.392-201.216-35.84-201.216-35.84s152.576-4.352 240.896-33.536c0 0 102.656-40.96 68.864-162.048 0 0-20.736-63.488-132.864-75.264 0 0 9.984-83.968-70.656-132.352 0 0-56.832-34.304-141.056 17.92-71.936 52.224-80.64 234.752-81.152 246.272 0.512-11.008 6.144-177.664-33.792-249.088 0 0-42.752-99.072-164.096-48.128 0 0-102.912 33.024-81.664 128 0 0-150.272-31.744-171.008 111.104 0 0-17.92 116.224 109.568 141.568 41.472 10.24 182.016 32.768 182.016 32.768S79.872 429.312 65.792 569.344c0 0-24.32 131.584 125.184 128.256 0 0-16.128 98.816 79.36 131.328 0.256-0.256 127.232 36.608 187.648-124.928 13.312-37.632 24.576-63.488 30.976-128.256 0 0 2.304 256.512-214.272 387.072l72.192 43.008s157.696-154.88 160-409.6c-0.256-13.568-0.256-21.76-0.256-21.76 0.256 7.424 0.256 14.592 0.256 21.76 1.024 50.176 8.704 173.056 52.992 219.392 0 0 63.232 87.04 172.544 41.984 0 0 65.28-23.808 65.792-109.824 0 0 100.864 15.104 122.88-88.832-0.512 0 29.184-91.648-76.8-144.384z m0 0" fill="#3d5a80"/>
        </svg>
        <div class="platform-info">
          <h4 class="platform-name">{{ siteName }}</h4>
          <p class="platform-desc">专注分享，记录成长</p>
        </div>
      </div>

      <div class="platform-badges">
        <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank" class="badge-link">
          <img src="https://gitee.com/yu-zi-wei/qixidi/badge/star.svg?theme=dark" alt="star" class="badge-img" />
        </a>
        <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank" class="badge-link">
          <img src="https://gitee.com/yu-zi-wei/qixidi/badge/fork.svg?theme=dark" alt="fork" class="badge-img" />
        </a>
      </div>

      <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank" class="platform-link">
        <BrandGithub class="icon" />
        <span>查看源码</span>
        <ExternalLink class="external-icon" />
      </a>
    </div>

    <!-- 特殊卡片 - 作者社交信息 -->
    <div v-else-if="specialCard?.type === 'author-social'" class="sidebar-card author-social-card">
      <div class="social-header">
        <img :src="specialCard.data?.avatar" :alt="specialCard.data?.nickname" class="social-avatar" />
        <div class="social-info">
          <h4 class="social-name">{{ specialCard.data?.nickname }}</h4>
          <p class="social-motto">{{ specialCard.data?.motto }}</p>
        </div>
      </div>

      <div class="social-links">
        <a v-if="specialCard.data?.github" :href="specialCard.data.github" target="_blank" class="social-link-item">
          <BrandGithub class="icon" />
          <span>GitHub</span>
        </a>
        <a v-if="specialCard.data?.gitee" :href="specialCard.data.gitee" target="_blank" class="social-link-item">
          <BrandGit class="icon" />
          <span>Gitee</span>
        </a>
        <a v-if="specialCard.data?.email" :href="`mailto:${specialCard.data.email}`" class="social-link-item">
          <Mail class="icon" />
          <span>邮箱</span>
        </a>
      </div>
    </div>

    <!-- 占位符（无特殊卡片时） -->
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
import { UserPlus, UserCheck, BrandGithub, BrandGit, Mail, ExternalLink } from '@vicons/tabler'

const { siteName } = useRuntimeConfig().public

// 特殊卡片类型定义
interface SpecialCardData {
  type: 'platform' | 'author-social'
  data?: {
    nickname?: string
    avatar?: string
    motto?: string
    github?: string
    gitee?: string
    email?: string
  }
}

// 从 useState 获取侧边栏数据
const sidebarData = useState('article-sidebar-data', () => ({
  author: null as any,
  articleContent: '',
  showToc: true,
  specialCard: null as SpecialCardData | null
}))

// 检查是否准备好数据（有作者信息或有文章内容即可显示）
const isReady = computed(() => sidebarData.value.author !== null || sidebarData.value.articleContent !== '')
const author = computed(() => sidebarData.value.author)
const articleContent = computed(() => sidebarData.value.articleContent)
const showToc = computed(() => sidebarData.value.showToc)
const specialCard = computed(() => sidebarData.value.specialCard)

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

/* 平台介绍卡片 */
.platform-card {
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.dark .platform-card {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(255, 255, 255, 0.06);
}

.platform-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.platform-icon {
  width: 42px;
  height: 42px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.platform-info {
  flex: 1;
  min-width: 0;
}

.platform-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0 0 2px 0;
}

.platform-desc {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin: 0;
}

.platform-badges {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  padding: 10px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .platform-badges {
  border-top-color: rgba(255, 255, 255, 0.06);
  border-bottom-color: rgba(255, 255, 255, 0.06);
}

.badge-link {
  display: block;
  transition: transform 0.2s ease;
}

.badge-link:hover {
  transform: translateY(-1px);
}

.badge-img {
  height: 20px;
  display: block;
}

.platform-link {
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
  text-decoration: none;
  transition: all 0.2s ease;
}

.platform-link:hover {
  background: var(--color-primary);
  color: #fff;
}

.platform-link .external-icon {
  width: 12px;
  height: 12px;
  opacity: 0.6;
}

/* 作者社交卡片 */
.author-social-card {
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.dark .author-social-card {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(255, 255, 255, 0.06);
}

.social-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.social-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.social-info {
  flex: 1;
  min-width: 0;
}

.social-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0 0 2px 0;
}

.social-motto {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin: 0;
  font-style: italic;
}

.social-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .social-links {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.social-link-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  text-decoration: none;
  color: var(--color-ink);
  font-size: 13px;
  transition: all 0.2s ease;
}

.dark .social-link-item {
  background: rgba(255, 255, 255, 0.04);
}

.social-link-item:hover {
  background: rgba(var(--color-primary-rgb), 0.1);
  color: var(--color-primary);
}

.social-link-item .icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}
</style>
