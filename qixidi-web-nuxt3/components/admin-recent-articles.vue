<template>
  <div class="recent-articles">
    <div class="panel-header">
      <h3 class="panel-title">最近编辑</h3>
      <NuxtLink to="/admin/articles" class="panel-more">全部文章</NuxtLink>
    </div>

    <div v-if="loading" class="article-loading">
      <n-skeleton v-for="i in 3" :key="i" :sharp="false" height="48px" />
    </div>

    <div v-else-if="articles.length === 0" class="article-empty">
      <n-icon size="36"><FileText /></n-icon>
      <p>还没有文章，写下第一篇吧</p>
      <n-button size="small" type="primary" @click="navigateTo('/write/article')">去写文章</n-button>
    </div>

    <ul v-else class="article-list">
      <li v-for="item in articles" :key="item.id" class="article-item">
        <div class="article-info">
          <div class="article-title-row">
            <span class="article-title">{{ item.articleTitle || '未命名文章' }}</span>
            <span class="article-state" :class="`state-${item.auditState}`">{{ stateText(item.auditState) }}</span>
          </div>
          <time class="article-time" :title="getFullDateTime(item.updateTime)">
            {{ formatTime(item.updateTime) }}编辑
          </time>
        </div>
        <n-button size="tiny" quaternary type="primary" class="article-edit-btn" @click="navigateTo(`/write/article/${item.id}`)">
          继续编辑
        </n-button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { FileText } from '@vicons/tabler'
import { ArticleAuditState } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const articleApi = useAdminArticleApi()
const loading = ref(true)
const articles = ref<{ id: number; articleTitle: string; auditState: number; updateTime: string }[]>([])

const stateText = (state: number) => {
  const map: Record<number, string> = {
    [ArticleAuditState.REVIEWING]: '审核中',
    [ArticleAuditState.PUBLISHED]: '已发布',
    [ArticleAuditState.REJECTED]: '未通过',
    [ArticleAuditState.DRAFT]: '草稿'
  }
  return map[state] || '未知'
}

onMounted(async () => {
  try {
    const { rows } = await articleApi.getArticleList({ pageNum: 1, pageSize: 3 })
    articles.value = (rows || []).map(r => ({
      id: r.id,
      articleTitle: r.articleTitle,
      auditState: r.auditState,
      updateTime: r.updateTime || r.createTime
    }))
  } catch {
    articles.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.recent-articles {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.panel-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.panel-more {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color var(--transition-base);
}

.panel-more:hover {
  color: var(--color-primary);
}

.article-loading {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.article-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-3);
  color: var(--color-ink-faint);
  padding: 24px 0;
}

.article-empty p {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
}

.article-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.article-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 8px;
  border-radius: var(--radius-md);
  transition: background var(--transition-base);
}

.article-item:hover {
  background: var(--color-surface-warm);
}

.article-info {
  flex: 1;
  min-width: 0;
}

.article-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-title {
  font-size: var(--text-sm);
  color: var(--color-ink);
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-state {
  flex-shrink: 0;
  font-size: var(--text-xs);
  padding: 1px 8px;
  border-radius: var(--radius-full);
}

.article-state.state-1 { color: #d97706; background: rgba(217, 119, 6, 0.1); }
.article-state.state-2 { color: #059669; background: rgba(5, 150, 105, 0.1); }
.article-state.state-3 { color: #dc2626; background: rgba(220, 38, 38, 0.1); }
.article-state.state-4 { color: var(--color-ink-muted); background: var(--color-surface-dim); }

.article-time {
  display: block;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin-top: 2px;
}

.article-edit-btn {
  flex-shrink: 0;
}
</style>
