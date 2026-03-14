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
    <div v-if="siteStatsData" class="sidebar-card">
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
    <div class="sidebar-card">
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
    <div v-if="siteInfoData" class="sidebar-card site-info-card">
      <!-- 品牌信息（前置，更突出） -->
      <div class="site-brand">
        <span class="footer-logo">栖息地</span>
        <p class="footer-slogan">在文字里，找到栖身之所</p>
      </div>

      <!-- 运行天数（分隔线） -->
      <div v-if="runningDays > 0" class="running-days">
        <svg class="fixed-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        <span>已运行 {{ runningDays }} 天</span>
      </div>

      <!-- 备案信息（紧凑布局） -->
      <div class="site-filings">
        <!-- 域名（主要信息） -->
        <div v-if="siteInfoData.realmName" class="info-item info-main">
          <span class="info-value">{{ siteInfoData.realmName }}</span>
        </div>

        <!-- 备案信息（次要信息，横向排列） -->
        <div class="filings-secondary">
          <span v-if="siteInfoData.filings" class="filing-tag">
            <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener" class="filing-link">
              {{ siteInfoData.filings }}
            </a>
          </span>
          <span v-if="siteInfoData.securityFilings" class="filing-tag">
            {{ siteInfoData.securityFilings }}
          </span>
          <span v-if="siteInfoData.mailbox" class="filing-tag">
            {{ siteInfoData.mailbox }}
          </span>
        </div>
      </div>

      <!-- 版权信息（底部分隔） -->
      <div class="footer-meta">
        <span>© {{ year }} 栖息地</span>
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
  gap: 20px;
  height: 100%;
  max-height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  /* 完全隐藏滚动条 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}

.sidebar-scrollable::-webkit-scrollbar {
  display: none; /* Chrome/Safari */
}

.sidebar-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 20px;
  border: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-ink);
  margin-bottom: 14px;
}

.fixed-icon {
  flex-shrink: 0;
  color: var(--color-ink);
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
  background: transparent;
  color: var(--color-ink-faint);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;
}

.refresh-btn:hover {
  background: var(--color-surface-dim);
  color: var(--color-primary);
}

.sign-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, var(--color-primary-light) 0%, var(--color-surface) 100%);
  border: 1px solid var(--color-primary-light);
}

.sign-label {
  font-size: 13px;
  color: var(--color-ink-muted);
}

.sign-days {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-primary);
  display: block;
  margin: 4px 0;
}

.sign-days small {
  font-size: 14px;
  font-weight: 400;
}

.sign-tip {
  font-size: 12px;
  color: var(--color-ink-faint);
}

.sign-btn {
  padding: 8px 24px;
  font-size: 14px;
  font-weight: 500;
  color: #fff;
  background: var(--color-primary);
  border: none;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--font-body);
}

.sign-btn:hover {
  background: var(--color-primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-sm);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  text-align: center;
}

.stat-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-num {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-ink);
}

.stat-label {
  font-size: 12px;
  color: var(--color-ink-muted);
}

.rank-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rank-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
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
  color: var(--color-ink-muted);
  background: var(--color-surface-dim);
  border-radius: var(--radius-sm);
}

.rank-num.top-1 { background: #fef3c7; color: #b45309; }
.rank-num.top-2 { background: #e5e7eb; color: #4b5563; }
.rank-num.top-3 { background: #fde68a; color: #92400e; }

.rank-title {
  font-size: 14px;
  line-height: 1.5;
  color: var(--color-ink-light);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  transition: color 0.2s;
}

.rank-title:hover {
  color: var(--color-primary);
}

.site-info-card {
  background: var(--color-surface-dim);
  border: none;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 品牌信息 */
.site-brand {
  text-align: center;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border-light);
}

.footer-logo {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--color-ink);
  letter-spacing: 2px;
  display: block;
}

.footer-slogan {
  margin: 6px 0 0 0;
  font-size: 13px;
  color: var(--color-ink-muted);
  letter-spacing: 0.5px;
}

/* 运行天数 */
.running-days {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 12px;
  color: var(--color-ink-muted);
  padding: 8px 12px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
}

.running-days svg {
  color: var(--color-primary);
}

/* 备案信息 */
.site-filings {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

/* 主信息（域名） */
.info-main .info-value {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink);
  text-align: center;
  padding: 4px 0;
}

/* 次要备案信息 - 横向排列 */
.filings-secondary {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

.filing-tag {
  font-size: 11px;
  color: var(--color-ink-muted);
  background: var(--color-surface);
  padding: 4px 8px;
  border-radius: var(--radius-sm);
}

.filing-link {
  color: var(--color-ink-light);
  text-decoration: none;
  transition: color 0.2s;
}

.filing-link:hover {
  color: var(--color-primary);
}

/* 版权信息 */
.footer-meta {
  margin-top: 4px;
  padding-top: 12px;
  border-top: 1px solid var(--color-border-light);
  font-size: 11px;
  color: var(--color-ink-faint);
  text-align: center;
}
</style>
