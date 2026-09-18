<template>
  <div class="top-articles">
    <div class="top-header">
      <h3 class="top-title">内容表现 TOP5</h3>
      <n-tabs type="segment" size="small" :value="activeMetric" @update:value="switchMetric">
        <n-tab v-for="m in metrics" :key="m.key" :name="m.key">{{ m.label }}</n-tab>
      </n-tabs>
    </div>

    <div v-if="loading" class="top-loading">
      <n-skeleton v-for="i in 5" :key="i" :sharp="false" height="40px" />
    </div>

    <div v-else-if="topList.length === 0" class="top-empty">
      <n-empty size="small" description="暂无已发布文章的数据" />
    </div>

    <ul v-else class="top-list">
      <li v-for="(item, index) in topList" :key="item.id" class="top-item">
        <span class="top-rank" :class="{ 'top-rank-hot': index < 3 }">{{ index + 1 }}</span>
        <NuxtLink :to="`/articles/${item.id}`" class="top-article-title">
          {{ item.articleTitle || '未命名文章' }}
        </NuxtLink>
        <span class="top-value">
          <n-icon size="14"><component :is="activeMetricIcon" /></n-icon>
          {{ formatCount(item[activeMetric]) }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { Eye, Heart, MessageCircle, Bookmark } from '@vicons/tabler'
import { ArticleAuditState, type AdminArticleItem } from '~/types'
import { markRaw, type Component } from 'vue'

const articleApi = useAdminArticleApi()

interface MetricDef {
  key: keyof Pick<AdminArticleItem, 'numberTimes' | 'likeTimes' | 'commentTimes' | 'collectionTimes'>
  label: string
  icon: Component
}

const metrics: MetricDef[] = [
  { key: 'numberTimes', label: '浏览', icon: markRaw(Eye) },
  { key: 'likeTimes', label: '点赞', icon: markRaw(Heart) },
  { key: 'commentTimes', label: '评论', icon: markRaw(MessageCircle) },
  { key: 'collectionTimes', label: '收藏', icon: markRaw(Bookmark) }
]

const activeMetric = ref<MetricDef['key']>('numberTimes')
const loading = ref(true)
const articles = ref<AdminArticleItem[]>([])

const activeMetricIcon = computed(() => metrics.find(m => m.key === activeMetric.value)?.icon || Eye)

// 已发布文章按当前指标排序取前 5
const topList = computed(() => {
  const key = activeMetric.value
  return articles.value
    .filter(a => a.auditState === ArticleAuditState.PUBLISHED)
    .sort((a, b) => (b[key] || 0) - (a[key] || 0))
    .slice(0, 5)
})

const switchMetric = (key: number | string) => {
  activeMetric.value = key as MetricDef['key']
}

const formatCount = (v: number | undefined) => {
  const n = v || 0
  if (n >= 10000) return `${(n / 10000).toFixed(1)}w`
  if (n >= 1000) return `${(n / 1000).toFixed(1)}k`
  return String(n)
}

onMounted(async () => {
  try {
    const { rows } = await articleApi.getArticleList({ pageNum: 1, pageSize: 100 })
    articles.value = rows || []
  } catch {
    articles.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.top-articles {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.top-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.top-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.top-loading {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top-empty {
  padding: 32px 0;
}

.top-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.top-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  border-radius: var(--radius-md);
  transition: background var(--transition-base);
}

.top-item:hover {
  background: var(--color-surface-warm);
}

.top-rank {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: 600;
  color: var(--color-ink-muted);
  background: var(--color-surface-dim);
  flex-shrink: 0;
}

.top-rank-hot {
  color: #fff;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
}

.top-article-title {
  flex: 1;
  min-width: 0;
  font-size: var(--text-sm);
  color: var(--color-ink);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-base);
}

.top-article-title:hover {
  color: var(--color-primary);
}

.top-value {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink-light);
}
</style>
