<template>
  <div class="article-list">
    <div v-for="item in articles" :key="item.id" class="article-card">
      <div class="article-body">
        <div class="article-meta">
          <NuxtLink :to="`/user/${item.userId}`" class="meta-author">
            <img
              :src="item.avatar"
              :alt="item.nickname"
              class="meta-avatar"
            />
            {{ item.nickname }}
          </NuxtLink>
          <span class="meta-sep">&middot;</span>
          <span class="meta-date">{{ formatDate(item.createTime) }}</span>
          <span v-if="item.groupingName" class="meta-tag">{{ item.groupingName }}</span>
        </div>
        <h3 class="article-title">
          <NuxtLink :to="`/articles/${item.id}`">
            {{ item.articleTitle }}
          </NuxtLink>
        </h3>
        <p class="article-excerpt">{{ item.articleAbstract }}</p>
        <div class="article-stats">
          <span class="stat-item">
            <Eye class="stat-icon" />
            {{ item.numberTimes || 0 }}
          </span>
          <span class="stat-item">
            <Heart class="stat-icon" />
            {{ item.likeTimes || 0 }}
          </span>
          <span class="stat-item">
            <MessageCircle class="stat-icon" />
            {{ item.commentTimes || 0 }}
          </span>
        </div>
      </div>
      <img
        v-if="item.articleCover && !hideCover"
        :src="item.articleCover"
        :alt="item.articleTitle"
        class="article-cover"
      />
    </div>
  </div>

  <!-- 加载状态 -->
  <div ref="loadTrigger" class="load-more">
    <n-spin v-if="loading" :size="20" />
    <span v-else-if="noMore" class="load-more-text">— 没有更多了 —</span>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { Eye, Heart, MessageCircle } from '@vicons/tabler'
import type { ArticleInfo } from '~/types'

const props = defineProps<{
  articles: ArticleInfo[]
  loading: boolean
  noMore: boolean
  hideCover?: boolean // 是否隐藏封面（用于标签分类页面）
}>()

const emit = defineEmits<{
  loadMore: []
}>()

const loadTrigger = ref<HTMLElement | null>(null)

onMounted(() => {
  if (!loadTrigger.value) return

  const observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        emit('loadMore')
      }
    },
    { rootMargin: '200px' }
  )

  observer.observe(loadTrigger.value)
  onUnmounted(() => observer.disconnect())
})

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffMin = Math.floor(diffMs / 60000)
  const diffHour = Math.floor(diffMs / 3600000)
  const diffDay = Math.floor(diffMs / 86400000)

  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin}分钟前`
  if (diffHour < 24) return `${diffHour}小时前`
  if (diffDay < 30) return `${diffDay}天前`
  if (diffDay < 365) return `${Math.floor(diffDay / 30)}个月前`

  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}
</script>

<style scoped>
.article-list {
  display: flex;
  flex-direction: column;
}

/* 极简列表式设计：用细线分隔，而非卡片块 */
.article-card {
  display: flex;
  gap: var(--space-5);
  padding: var(--space-6) 0;
  border-bottom: 1px solid var(--color-border);
  background: transparent;
  transition: all var(--transition-base);
}

.article-card:hover {
  padding: var(--space-6) 4px;
  margin: 0 -4px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
}

.article-card:last-child {
  border-bottom: none;
}

.article-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* 元数据 - 调整间距和字号 */
.article-meta {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin-bottom: var(--space-3);
  flex-wrap: wrap;
}

.meta-author {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: var(--color-ink-light);
  font-weight: 500;
  transition: color var(--transition-fast);
}

.meta-author:hover {
  color: var(--color-primary);
}

.meta-avatar {
  width: 20px;
  height: 20px;
  border-radius: var(--radius-full);
  object-fit: cover;
  background: var(--color-surface-dim);
}

.meta-sep {
  opacity: 0.3;
  margin: 0 2px;
}

.meta-tag {
  padding: 3px 10px;
  font-size: var(--text-xs);
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: var(--radius-sm);
  font-weight: 500;
}

/* 标题 - 增大字号 */
.article-title {
  font-size: var(--text-xl);
  font-weight: 600;
  line-height: var(--leading-tight);
  margin-bottom: var(--space-3);
}

.article-title a {
  color: var(--color-ink);
  text-decoration: none;
  transition: color var(--transition-base);
}

.article-title a:hover {
  color: var(--color-primary);
}

/* 悬停时整行的微妙效果 */
.article-card:hover .article-title a {
  color: var(--color-primary);
}

/* 摘要 - 优化行高和颜色 */
.article-excerpt {
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  color: var(--color-ink-light);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: var(--space-4);
  flex: 1;
}

/* 互动数据 - 提高对比度 */
.article-stats {
  display: flex;
  gap: var(--space-4);
  margin-top: auto;
}

.stat-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 15px;
  height: 15px;
  color: var(--color-ink-muted);
  stroke-width: 2;
  flex-shrink: 0;
}

.article-cover {
  width: 200px;
  height: 130px;
  object-fit: cover;
  border-radius: var(--radius-md);
  flex-shrink: 0;
  background: var(--color-surface-dim);
  transition: transform var(--transition-base);
}

.article-card:hover .article-cover {
  transform: scale(1.02);
}

.load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-6) var(--space-4);
  min-height: 60px;
}

.load-more-text {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  letter-spacing: 1px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .article-cover {
    width: 160px;
    height: 110px;
  }
}

@media (max-width: 768px) {
  .article-card {
    flex-direction: column;
    gap: var(--space-4);
    padding: var(--space-5) 0;
  }

  .article-card:hover {
    padding: var(--space-5) 0;
    margin: 0;
  }

  .article-cover {
    width: 100%;
    height: 180px;
  }

  .article-excerpt {
    display: none;
  }
}

@media (max-width: 480px) {
  .article-title {
    font-size: var(--text-lg);
  }
}
</style>
