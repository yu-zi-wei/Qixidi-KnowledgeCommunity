<template>
  <aside class="sidebar-scrollable">
    <!-- 站点数据 -->
    <div v-if="siteStatsData" class="sidebar-card gray-card" style="--delay: 1">
      <div class="card-header">
        <svg class="fixed-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="18" height="18" rx="2"/>
          <path d="M3 9h18"/>
          <path d="M9 21V9"/>
        </svg>
        <span>站点数据</span>
      </div>
      <div class="stats-grid">
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.userCount }}</span>
          <span class="stat-label">用户数</span>
        </div>
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.specialCount }}</span>
          <span class="stat-label">专栏数</span>
        </div>
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.labelCount }}</span>
          <span class="stat-label">标签数</span>
        </div>
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.articleCount }}</span>
          <span class="stat-label">文章数</span>
        </div>
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.dictumCount }}</span>
          <span class="stat-label">随笔数</span>
        </div>
        <div class="stat-cell">
          <span class="stat-num">{{ siteStatsData.timeNotesCount }}</span>
          <span class="stat-label">小记数</span>
        </div>
      </div>
    </div>

    <!-- 精选文章 -->
    <div class="sidebar-card gray-card" style="--delay: 2">
      <div class="card-header">
        <svg class="fixed-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
        </svg>
        <span>精选文章</span>
        <button class="refresh-btn" title="换一批" @click="handleRefresh">
          <svg class="fixed-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 2v6h-6"/>
            <path d="M3 12a9 9 0 0 1 15-6.7L21 8"/>
            <path d="M3 22v-6h6"/>
            <path d="M21 12a9 9 0 0 1-15 6.7L3 16"/>
          </svg>
        </button>
      </div>
      <ol class="rank-list">
        <li v-for="(article, i) in recommendList" :key="article.id" class="rank-item">
          <span class="rank-num" :class="`top-${i + 1}`">{{ i + 1 }}</span>
          <NuxtLink :to="`/articles/${article.id}`" class="rank-title">
            {{ article.articleTitle }}
          </NuxtLink>
        </li>
      </ol>
    </div>

    <!-- 站点信息 -->
    <div class="sidebar-card site-info-card" style="--delay: 3">
      <!-- 品牌和运行天数 -->
      <div class="site-header">
        <div class="brand-info">
          <span class="brand-name">{{ siteName }}</span>
          <span class="brand-slogan">在文字里，找到栖身之所</span>
        </div>
        <div v-if="runningDays > 0" class="running-badge">
          {{ runningDays }}天
        </div>
      </div>

      <!-- 开源地址 -->
      <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank" rel="noopener" class="opensource-link">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
          <path d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 12 0a12 12 0 0 0-.016 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.327 0 .593-.265.593-.593v-1.481a.593.593 0 0 0-.593-.593h-3.556a.593.593 0 0 1-.593-.593V9.778c0-.327.266-.593.593-.593h5.926c.327 0 .593.266.593.593v6.815a2.37 2.37 0 0 1-2.37 2.37H6.518a.593.593 0 0 1-.593-.593V9.778a4.444 4.444 0 0 1 4.444-4.445h7.705z"/>
        </svg>
        <span>开源项目</span>
        <svg class="external-icon" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
          <polyline points="15 3 21 3 21 9"/>
          <line x1="10" y1="14" x2="21" y2="3"/>
        </svg>
      </a>

      <!-- 备案信息 -->
      <div v-if="siteInfoData?.filings || siteInfoData?.securityFilings" class="site-filings">
        <a v-if="siteInfoData.filings" href="https://beian.miit.gov.cn/" target="_blank" rel="noopener" class="filing-link">
          {{ siteInfoData.filings }}
        </a>
        <span v-if="siteInfoData.securityFilings" class="filing-text">
          {{ siteInfoData.securityFilings }}
        </span>
      </div>

      <!-- 版权信息 -->
      <div class="footer-meta">
        © {{ year }} {{ siteName }}
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const siteApi = useSiteApi()
const { siteName } = useRuntimeConfig().public
const articleApi = useArticleApi()

const year = new Date().getFullYear()

// 在组件中获取数据
const { data: siteStatsData } = await useAsyncData('home-sidebar-stats', () =>
  siteApi.getTotalData()
)

const { data: siteInfoData } = await useAsyncData('home-sidebar-info', () =>
  siteApi.getInfo()
)

const MAX_REFRESH_PAGE = 3
const recommendPage = ref(1)
const recommendTotal = ref(0)
const { data: recommendData, refresh: refreshRecommendData } = await useAsyncData(
  'home-sidebar-recommend',
  () => articleApi.getRecommendList(recommendPage.value, 10)
)
const recommendList = computed(() => recommendData.value?.rows || [])

// 记录总数，用于计算最大页数
watch(() => recommendData.value, (data) => {
  if (data?.total !== undefined) recommendTotal.value = data.total
}, { immediate: true })

const handleRefresh = () => {
  const totalPages = Math.max(1, Math.ceil(recommendTotal.value / 10))
  const maxPage = Math.min(MAX_REFRESH_PAGE, totalPages)
  recommendPage.value = recommendPage.value >= maxPage ? 1 : recommendPage.value + 1
  refreshRecommendData()
}

const runningDays = computed(() => {
  if (!siteInfoData.value?.createTime) return 0
  const created = new Date(siteInfoData.value.createTime)
  const now = new Date()
  return Math.floor((now.getTime() - created.getTime()) / (1000 * 60 * 60 * 24))
})
</script>

<style scoped>
/* 卡片入场动画 - 与文章列表协调 */
@keyframes sidebar-card-in {
  from {
    opacity: 0;
    transform: translateX(16px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 滚动容器 */
.sidebar-scrollable {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
  max-height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  padding-right: 4px;
}

.sidebar-scrollable::-webkit-scrollbar {
  display: none;
}

/* 卡片通用样式 - 渐变色背景 */
.sidebar-card {
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.92) 0%,
    rgba(255, 255, 255, 0.78) 100%
  );
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s ease;
  /* 入场动画 - 与文章列表统一 */
  animation: sidebar-card-in 0.4s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.06s) forwards;
  opacity: 0;
}

.sidebar-card:hover {
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.98) 0%,
    rgba(255, 255, 255, 0.88) 100%
  );
}

/* 灰色背景卡片 - 带品牌色渐变 + 毛玻璃效果 */
.sidebar-card.gray-card {
  background: linear-gradient(
    135deg,
    rgba(61, 90, 128, 0.08) 0%,
    rgba(0, 0, 0, 0.04) 50%,
    rgba(90, 127, 168, 0.06) 100%
  );
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
}

.sidebar-card.gray-card:hover {
  background: linear-gradient(
    135deg,
    rgba(61, 90, 128, 0.12) 0%,
    rgba(0, 0, 0, 0.06) 50%,
    rgba(90, 127, 168, 0.09) 100%
  );
  transform: none;
  box-shadow: none;
}

.dark .sidebar-card.gray-card {
  background: linear-gradient(
    135deg,
    rgba(122, 176, 228, 0.1) 0%,
    rgba(255, 255, 255, 0.05) 50%,
    rgba(122, 176, 228, 0.08) 100%
  );
}

.dark .sidebar-card.gray-card:hover {
  background: linear-gradient(
    135deg,
    rgba(122, 176, 228, 0.15) 0%,
    rgba(255, 255, 255, 0.08) 50%,
    rgba(122, 176, 228, 0.12) 100%
  );
}

/* 暗色主题 */
.dark .sidebar-card {
  background: linear-gradient(
    135deg,
    rgba(26, 29, 33, 0.7) 0%,
    rgba(26, 29, 33, 0.4) 100%
  );
  border-color: rgba(255, 255, 255, 0.08);
}

.dark .sidebar-card:hover {
  background: linear-gradient(
    135deg,
    rgba(36, 40, 44, 0.8) 0%,
    rgba(36, 40, 44, 0.5) 100%
  );
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

/* 卡片标题 */
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-ink);
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.dark .card-header {
  border-bottom-color: rgba(255, 255, 255, 0.05);
}

.fixed-icon {
  flex-shrink: 0;
  color: var(--color-primary);
  stroke-width: 2;
}

.refresh-btn {
  margin-left: auto;
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: rgba(0, 0, 0, 0.03);
  color: var(--color-ink-muted);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.dark .refresh-btn {
  background: rgba(255, 255, 255, 0.05);
}

.refresh-btn:hover {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

/* 站点数据网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 12px 8px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 10px;
  transition: all 0.2s;
}

.dark .stat-cell {
  background: rgba(255, 255, 255, 0.05);
}

.stat-cell:hover {
  background: rgba(61, 90, 128, 0.08);
}

.stat-num {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-ink);
  letter-spacing: -0.02em;
}

.stat-label {
  font-size: 11px;
  color: var(--color-ink-muted);
}

/* 精选文章列表 */
.rank-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  margin: 0 -10px;
  border-radius: 8px;
  transition: all 0.2s;
}

.rank-item:hover {
  background: rgba(0, 0, 0, 0.03);
}

.dark .rank-item:hover {
  background: rgba(255, 255, 255, 0.03);
}

.rank-num {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  border-radius: 6px;
  background: rgba(0, 0, 0, 0.04);
  color: var(--color-ink-muted);
}

.dark .rank-num {
  background: rgba(255, 255, 255, 0.06);
}

.rank-num.top-1 {
  background: linear-gradient(135deg, #fef3c7, #fde68a);
  color: #b45309;
}
.rank-num.top-2 {
  background: linear-gradient(135deg, #e5e7eb, #d1d5db);
  color: #4b5563;
}
.rank-num.top-3 {
  background: linear-gradient(135deg, #fed7aa, #fdba74);
  color: #9a3412;
}

.rank-title {
  flex: 1;
  font-size: 13px;
  line-height: 1.5;
  color: var(--color-ink-light);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.2s;
}

.rank-title:hover {
  color: var(--color-primary);
}

/* 站点信息卡片 */
.site-info-card {
  background: rgba(0, 0, 0, 0.02);
  border: 1px solid rgba(0, 0, 0, 0.04);
  padding: 16px;
  gap: 10px;
}

.dark .site-info-card {
  background: rgba(255, 255, 255, 0.02);
  border-color: rgba(255, 255, 255, 0.04);
}

.site-info-card:hover {
  background: rgba(0, 0, 0, 0.03);
  transform: none;
  box-shadow: none;
}

.dark .site-info-card:hover {
  background: rgba(255, 255, 255, 0.03);
}

/* 品牌头部 */
.site-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.brand-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.brand-name {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 700;
  color: var(--color-ink);
  letter-spacing: 1px;
}

.brand-slogan {
  font-size: 11px;
  color: var(--color-ink-muted);
}

.running-badge {
  font-size: 10px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 500;
  flex-shrink: 0;
}

/* 开源链接 */
.opensource-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: var(--color-ink-light);
  font-size: 11px;
  text-decoration: none;
  transition: color 0.2s;
}

.opensource-link:hover {
  color: var(--color-primary);
}

.opensource-link svg {
  flex-shrink: 0;
  opacity: 0.6;
}

.opensource-link:hover svg {
  opacity: 1;
}

.external-icon {
  opacity: 0.4;
}

/* 备案信息 */
.site-filings {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.filing-link,
.filing-text {
  font-size: 10px;
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color 0.2s;
}

.filing-link:hover {
  color: var(--color-primary);
}

/* 版权信息 */
.footer-meta {
  padding-top: 10px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  font-size: 10px;
  color: var(--color-ink-faint);
}

.dark .footer-meta {
  border-top-color: rgba(255, 255, 255, 0.05);
}
</style>
