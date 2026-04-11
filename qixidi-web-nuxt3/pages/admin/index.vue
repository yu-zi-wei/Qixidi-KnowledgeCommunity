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
            <div class="stat-value">{{ censusData?.columnCount || 0 }}</div>
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
            <div class="stat-value">{{ censusData?.essayCount || 0 }}</div>
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
  </div>
</template>

<script setup lang="ts">
import { FileText, Layout, Bookmark, Users, MessageCircle, Disc, Notebook, Clock } from '@vicons/tabler'

definePageMeta({ layout: 'admin' })

const censusApi = useUserCensusApi()

// 获取当前月份
const currentMonth = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
})

// 获取统计数据
const { data: censusData } = await useAsyncData(
  'admin-census',
  () => censusApi.getUserCensusCount(currentMonth.value)
)

// 获取投稿记录
const { data: submissionData } = await useAsyncData(
  'admin-submission',
  () => censusApi.getUserSubmission()
)
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
  grid-template-columns: repeat(4, 1fr);
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
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
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
