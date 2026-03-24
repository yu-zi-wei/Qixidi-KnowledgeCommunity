<template>
  <aside class="sidebar-scrollable">
    <!-- 签到卡片 -->
    <div class="sidebar-card sign-card">
      <div class="sign-info">
        <span class="sign-label">连续签到</span>
        <span class="sign-days">1 <small>天</small></span>
        <p class="sign-tip">点亮在栖息地的每一天</p>
      </div>
      <button class="sign-btn">签到</button>
    </div>

    <!-- 站点数据 -->
    <div v-if="siteStatsData" class="sidebar-card gray-card">
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
    <div class="sidebar-card gray-card">
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
    <div class="sidebar-card site-info-card">
      <!-- 品牌和运行天数 -->
      <div class="site-header">
        <div class="brand-info">
          <span class="brand-name">栖息地</span>
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
        © {{ year }} 栖息地
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const siteApi = useSiteApi()
const articleApi = useArticleApi()

const year = new Date().getFullYear()

// 在组件中获取数据
const { data: siteStatsData } = await useAsyncData('home-sidebar-stats', () =>
  siteApi.getTotalData()
)

const { data: siteInfoData } = await useAsyncData('home-sidebar-info', () =>
  siteApi.getInfo()
)

const recommendPage = ref(1)
const { data: recommendData, refresh: refreshRecommendData } = await useAsyncData(
  'home-sidebar-recommend',
  () => articleApi.getRecommendList(recommendPage.value, 10)
)
const recommendList = computed(() => recommendData.value?.rows || [])

const refreshRecommend = () => {
  recommendPage.value++
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

/* 卡片通用样式 - 毛玻璃效果 */
.sidebar-card {
  background: var(--card-bg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s ease;
}

.sidebar-card:hover {
  background: rgba(255, 255, 255, 0.5);
}

/* 灰色背景卡片 */
.sidebar-card.gray-card {
  background: rgba(0, 0, 0, 0.04);
  backdrop-filter: none;
  -webkit-backdrop-filter: none;
}

.sidebar-card.gray-card:hover {
  background: rgba(0, 0, 0, 0.06);
  transform: none;
  box-shadow: none;
}

.dark .sidebar-card.gray-card {
  background: rgba(255, 255, 255, 0.04);
}

.dark .sidebar-card.gray-card:hover {
  background: rgba(255, 255, 255, 0.06);
}

/* 暗色主题 */
.dark .sidebar-card {
  border-color: rgba(255, 255, 255, 0.08);
}

.dark .sidebar-card:hover {
  background: rgba(18, 20, 22, 0.6);
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

/* 签到卡片 - 特殊渐变 */
.sign-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.08) 0%, rgba(255, 255, 255, 0.4) 100%);
  border: 1px solid rgba(61, 90, 128, 0.15);
}

.dark .sign-card {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.15) 0%, rgba(18, 20, 22, 0.4) 100%);
  border-color: rgba(93, 138, 168, 0.2);
}

.sign-card:hover {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.12) 0%, rgba(255, 255, 255, 0.6) 100%);
  border-color: rgba(61, 90, 128, 0.25);
}

.sign-info {
  flex: 1;
}

.sign-label {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin-bottom: 2px;
}

.sign-days {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1.2;
}

.sign-days small {
  font-size: 14px;
  font-weight: 400;
  color: var(--color-ink-muted);
}

.sign-tip {
  font-size: 11px;
  color: var(--color-ink-faint);
  margin-top: 4px;
}

.sign-btn {
  padding: 10px 28px;
  font-size: 13px;
  font-weight: 500;
  color: #fff;
  background: var(--color-primary);
  border: none;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.25s ease;
  font-family: var(--font-body);
  box-shadow: 0 2px 8px rgba(61, 90, 128, 0.3);
}

.sign-btn:hover {
  background: var(--color-primary-hover);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(61, 90, 128, 0.4);
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
