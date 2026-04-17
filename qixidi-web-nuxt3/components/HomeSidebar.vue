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
      <!-- 品牌 -->
      <div class="site-header">
        <span class="brand-slogan">在文字里，找到栖身之所</span>
        <div v-if="runningDays > 0" class="running-badge">
          已运行 {{ runningYears }}年{{ runningMonths }}月{{ runningRemainDays }}天
        </div>
      </div>

      <!-- 开源地址 -->
      <div class="opensource-row">
        <span class="opensource-label">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
          </svg>
          开源项目
        </span>
        <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank" rel="noopener" class="ext-link">Gitee</a>
        <a href="https://github.com/yu-zi-wei/Qixidi-KnowledgeCommunity" target="_blank" rel="noopener" class="ext-link">GitHub</a>
      </div>

      <!-- 反馈邮箱 -->
      <a v-if="siteInfoData?.mailbox" :href="`mailto:${siteInfoData.mailbox}`" class="opensource-link">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="2" y="4" width="20" height="16" rx="2"/>
          <path d="M22 4L12 13 2 4"/>
        </svg>
        <span>{{ siteInfoData.mailbox }}</span>
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

const runningYears = computed(() => {
  if (!siteInfoData.value?.createTime) return 0
  const created = new Date(siteInfoData.value.createTime)
  const now = new Date()
  let years = now.getFullYear() - created.getFullYear()
  if (now.getMonth() < created.getMonth() || (now.getMonth() === created.getMonth() && now.getDate() < created.getDate())) {
    years--
  }
  return Math.max(0, years)
})

const runningMonths = computed(() => {
  if (!siteInfoData.value?.createTime) return 0
  const created = new Date(siteInfoData.value.createTime)
  const now = new Date()
  let months = now.getMonth() - created.getMonth()
  if (now.getDate() < created.getDate()) months--
  if (months < 0) months += 12
  return months
})

const runningRemainDays = computed(() => {
  if (!siteInfoData.value?.createTime) return 0
  const created = new Date(siteInfoData.value.createTime)
  const now = new Date()
  // 计算去掉年月后剩余的天数
  let year = created.getFullYear()
  let month = created.getMonth()
  month += runningMonths.value
  year += runningYears.value + Math.floor(month / 12)
  month = month % 12
  const afterYM = new Date(year, month, created.getDate())
  if (afterYM > now) {
    month -= 1
    if (month < 0) { month += 12; year -= 1 }
    afterYM.setFullYear(year, month, created.getDate())
  }
  const diff = Math.floor((now.getTime() - afterYM.getTime()) / (1000 * 60 * 60 * 24))
  return Math.max(0, diff)
})
</script>

<style scoped>
/* 卡片入场动画 - 与文章列表协调 */
@keyframes sidebar-card-in {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动容器 */
.sidebar-scrollable {
  display: flex;
  flex-direction: column;
  gap: 12px;
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

/* 卡片通用样式 - 轻透风格，与文章卡片统一 */
.sidebar-card {
  background: rgba(255, 255, 255, 0.25);
  border: none;
  border-radius: 10px;
  padding: 16px;
  transition: all 0.2s ease;
  animation: sidebar-card-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.04s) forwards;
  opacity: 0;
}

.sidebar-card:hover {
  background: rgba(255, 255, 255, 0.4);
}

/* 暗色主题 */
:root.dark .sidebar-card {
  background: rgba(255, 255, 255, 0.03);
}

:root.dark .sidebar-card:hover {
  background: rgba(255, 255, 255, 0.06);
}

/* 卡片标题 */
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-ink);
  margin-bottom: 14px;
}

.fixed-icon {
  flex-shrink: 0;
  color: var(--color-primary);
  stroke-width: 2;
}

.refresh-btn {
  margin-left: auto;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: rgba(0, 0, 0, 0.03);
  color: var(--color-ink-muted);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}

:root.dark .refresh-btn {
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
  gap: 8px;
}

.stat-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 6px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 8px;
  transition: background 0.2s;
}

:root.dark .stat-cell {
  background: rgba(255, 255, 255, 0.04);
}

.stat-cell:hover {
  background: rgba(61, 90, 128, 0.08);
}

.stat-num {
  font-size: 17px;
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
  gap: 2px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 7px 8px;
  margin: 0 -8px;
  border-radius: 8px;
  transition: all 0.2s;
}

.rank-item:hover {
  background: rgba(0, 0, 0, 0.03);
}

:root.dark .rank-item:hover {
  background: rgba(255, 255, 255, 0.03);
}

.rank-num {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  border-radius: 5px;
  background: rgba(0, 0, 0, 0.04);
  color: var(--color-ink-muted);
}

:root.dark .rank-num {
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

/* 站点信息卡片 - 更轻 */
.site-info-card {
  background: rgba(255, 255, 255, 0.15);
  padding: 14px;
}

:root.dark .site-info-card {
  background: rgba(255, 255, 255, 0.02);
}

.site-info-card:hover {
  background: rgba(255, 255, 255, 0.25);
}

:root.dark .site-info-card:hover {
  background: rgba(255, 255, 255, 0.04);
}

/* 品牌头部 */
.site-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.brand-slogan {
  font-family: var(--font-logo);
  font-size: 13px;
  color: var(--color-ink-light);
}

.running-badge {
  font-size: 10px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 500;
  flex-shrink: 0;
  white-space: nowrap;
}

/* 开源链接行 */
.opensource-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
}

.opensource-label {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: var(--color-ink-light);
  flex-shrink: 0;
}

.opensource-label svg {
  flex-shrink: 0;
  opacity: 0.6;
  color: var(--color-primary);
}

.ext-link {
  font-size: 11px;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.2s;
}

.ext-link:hover {
  opacity: 0.8;
  text-decoration: underline;
}

/* 开源链接（邮箱复用） */
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
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  font-size: 10px;
  color: var(--color-ink-faint);
}

:root.dark .footer-meta {
  border-top-color: rgba(255, 255, 255, 0.06);
}
</style>
