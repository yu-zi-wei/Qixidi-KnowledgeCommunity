<template>
  <div class="admin-dashboard">
    <!-- 统计卡片区域 -->
    <section class="stats-section">
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon stat-icon-article">
            <FileText />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.articleCount || 0 }}</div>
            <div class="stat-label">文章</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-column">
            <Layout />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.specialColumnCount || 0 }}</div>
            <div class="stat-label">专栏</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-collection">
            <Bookmark />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.collectionCount || 0 }}</div>
            <div class="stat-label">收藏夹</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-follow">
            <Users />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.followCount || 0 }}</div>
            <div class="stat-label">关注</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-comment">
            <MessageCircle />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.commentCount || 0 }}</div>
            <div class="stat-label">评论</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-album">
            <Disc />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.albumCount || 0 }}</div>
            <div class="stat-label">专辑</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-essay">
            <Notebook />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.dictumCount || 0 }}</div>
            <div class="stat-label">随笔</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-notes">
            <Clock />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.timeNotesCount || 0 }}</div>
            <div class="stat-label">时光小记</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-fabulous">
            <Heart />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ censusData?.fabulousCount || 0 }}</div>
            <div class="stat-label">获赞</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon stat-icon-view">
            <Eye />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatViewCount(censusData?.viewCount) }}</div>
            <div class="stat-label">总浏览</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 快捷创作入口 -->
    <section class="quick-actions-section">
      <div class="quick-actions">
        <button class="quick-action-btn" @click="navigateTo('/write/article')">
          <n-icon size="18"><Edit /></n-icon>
          <span>写文章</span>
        </button>
        <button class="quick-action-btn" @click="openEssayDrawer">
          <n-icon size="18"><Notebook /></n-icon>
          <span>记随笔</span>
        </button>
        <button class="quick-action-btn" @click="navigateTo('/write/note')">
          <n-icon size="18"><Clock /></n-icon>
          <span>时光小记</span>
        </button>
      </div>
    </section>

    <!-- 投稿贡献图 -->
    <section class="submission-section">
      <div class="section-header">
        <h3 class="section-title">投稿贡献</h3>
      </div>
      <div class="chart-wrapper">
        <SubmissionChart :profile="submissionData || {}" />
      </div>
    </section>

    <!-- 近30天数据趋势 -->
    <section class="trend-section">
      <div class="section-header">
        <h3 class="section-title">数据趋势（近30天）</h3>
      </div>
      <div class="trend-grid">
        <AdminTrendChart
          title="内容浏览"
          :dates="trendDates"
          :series="[{ name: '浏览量', values: browseValues }]"
        />
        <AdminTrendChart
          title="互动"
          :dates="trendDates"
          :series="[{ name: '获赞', values: fabulousValues }, { name: '评论', values: commentValues }]"
        />
      </div>
    </section>

    <!-- 内容表现 TOP5 -->
    <AdminTopArticles />

    <!-- 最新互动 + 最近编辑 -->
    <section class="insight-section">
      <AdminRecentActivity />
      <AdminRecentArticles />
    </section>
  </div>
</template>

<script setup lang="ts">
import { FileText, Layout, Bookmark, Users, MessageCircle, Disc, Notebook, Clock, Heart, Edit, Eye } from '@vicons/tabler'
import type { CensusTrendItem } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const censusApi = useUserCensusApi()

const openEssayDrawer = () => {
  const essayDrawerStore = useEssayDrawerStore()
  essayDrawerStore.open()
}

// 获取统计数据（实时查询，无需传时间）
const { data: censusData } = await useAsyncData(
  'admin-census',
  () => censusApi.getUserCensusCount()
)

// 获取投稿记录
const { data: submissionData } = await useAsyncData(
  'admin-submission',
  () => censusApi.getUserSubmission()
)

// 获取近30天趋势数据
const { data: browseTrendData } = await useAsyncData(
  'admin-browse-trend',
  () => censusApi.getBrowseTrend().catch(() => [] as CensusTrendItem[])
)

const { data: interactTrendData } = await useAsyncData(
  'admin-interact-trend',
  () => censusApi.getInteractTrend().catch(() => [] as CensusTrendItem[])
)

// 生成最近 30 天的日期轴（含今天，YYYY-MM-DD）
const trendDates = Array.from({ length: 30 }, (_, i) => {
  const d = new Date()
  d.setDate(d.getDate() - (29 - i))
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${d.getFullYear()}-${mm}-${dd}`
})

// 趋势数据 → 按序列名过滤的 日期→数量 映射
const toDailyMap = (items: CensusTrendItem[] | null, seriesName?: string): Record<string, number> => {
  const map: Record<string, number> = {}
  for (const item of items || []) {
    if (seriesName && item.title !== seriesName) continue
    map[item.dateTimes] = (map[item.dateTimes] || 0) + item.censusSum
  }
  return map
}

const toAlignedValues = (map: Record<string, number>) => trendDates.map(d => map[d] || 0)

const browseValues = computed(() => toAlignedValues(toDailyMap(browseTrendData.value)))
const fabulousValues = computed(() => toAlignedValues(toDailyMap(interactTrendData.value, 'fabulous')))
const commentValues = computed(() => toAlignedValues(toDailyMap(interactTrendData.value, 'comment')))

// 浏览量过万缩写显示
const formatViewCount = (v: number | undefined) => {
  const n = v || 0
  if (n >= 10000) return `${(n / 10000).toFixed(1)}w`
  return String(n)
}
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 统计卡片区域 */
.stats-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: var(--color-surface-warm);
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon svg {
  width: 22px;
  height: 22px;
  color: #fff;
}

.stat-icon-article {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon-column {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon-collection {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon-follow {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon-comment {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon-album {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
}

.stat-icon-essay {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
}

.stat-icon-essay svg {
  color: #666;
}

.stat-icon-notes {
  background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%);
}

.stat-icon-fabulous {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
}

.stat-icon-view {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

/* 快捷创作入口 */
.quick-actions-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 16px 24px;
  box-shadow: var(--shadow-sm);
}

.quick-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.quick-action-btn {
  flex: 1;
  min-width: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 20px;
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  background: var(--color-surface-warm);
  color: var(--color-ink);
  font-size: var(--text-base);
  cursor: pointer;
  transition: all var(--transition-base);
}

.quick-action-btn:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
  transform: translateY(-1px);
  box-shadow: var(--shadow-sm);
}

/* 最新互动 + 最近编辑双栏：minmax(0,1fr) 等宽且允许收缩到内容省略，否则子项 min-width:auto 会互相挤压 */
.insight-section {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 24px;
}

.stat-content {
  min-width: 0;
}

.stat-value {
  font-size: var(--text-xl);
  font-weight: 700;
  color: var(--color-ink);
  line-height: 1.2;
}

.stat-label {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin-top: 2px;
}

/* 投稿贡献图区域 */
.submission-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

/* 数据趋势区域 */
.trend-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.trend-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.chart-wrapper {
  width: 100%;
}

/* 响应式 */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(4, 1fr);
  }

  .trend-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .insight-section {
    grid-template-columns: 1fr;
  }

  .trend-grid {
    gap: 20px;
  }

  .quick-action-btn {
    min-width: 100%;
  }

  .stat-card {
    padding: 12px;
  }

  .stat-icon {
    width: 36px;
    height: 36px;
  }

  .stat-icon svg {
    width: 18px;
    height: 18px;
  }
}
</style>
