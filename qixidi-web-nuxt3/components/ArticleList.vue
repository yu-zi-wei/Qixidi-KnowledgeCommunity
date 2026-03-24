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
          <span class="meta-date" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</span>
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
import { formatTime, getFullDateTime } from '~/utils/formatTime'

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
</script>

<style scoped>
.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 卡片设计 - 更多留白和圆角 */
.article-card {
  display: flex;
  gap: 24px;
  padding: 24px;
  border-radius: 16px;
  background: var(--card-bg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.article-card:hover {
  background: rgba(255, 255, 255, 0.6);
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

:root.dark .article-card:hover {
  background: rgba(18, 20, 22, 0.6);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.article-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* 元数据 - 更精致的布局 */
.article-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--color-ink-muted);
  margin-bottom: 14px;
  flex-wrap: wrap;
}

.meta-author {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: var(--color-ink);
  font-weight: 500;
  transition: color var(--transition-fast);
}

.meta-author:hover {
  color: var(--color-primary);
}

.meta-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
  background: var(--color-surface-dim);
  border: 2px solid rgba(255, 255, 255, 0.5);
}

.meta-sep {
  opacity: 0.4;
}

.meta-tag {
  padding: 4px 12px;
  font-size: 12px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: 20px;
  font-weight: 500;
  margin-left: auto;
}

/* 标题 - 更有层次感 */
.article-title {
  font-size: 20px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 10px;
  overflow-wrap: break-word;
  word-break: break-word;
  letter-spacing: -0.02em;
}

.article-title a {
  color: var(--color-ink);
  text-decoration: none;
  transition: color var(--transition-base);
}

.article-title a:hover {
  color: var(--color-primary);
}

/* 摘要 - 更舒适的阅读体验 */
.article-excerpt {
  font-size: 14px;
  line-height: 1.7;
  color: var(--color-ink-light);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 16px;
  flex: 1;
  overflow-wrap: break-word;
  word-break: break-word;
}

/* 互动数据 - 更紧凑 */
.article-stats {
  display: flex;
  gap: 20px;
  margin-top: auto;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

:root.dark .article-stats {
  border-top-color: rgba(255, 255, 255, 0.05);
}

.stat-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--color-ink-muted);
  transition: color 0.2s;
}

.stat-item:hover {
  color: var(--color-ink-light);
}

.stat-icon {
  width: 16px;
  height: 16px;
  stroke-width: 1.8;
  flex-shrink: 0;
}

/* 封面图片 - 更有质感 */
.article-cover {
  width: 180px;
  height: 140px;
  object-fit: cover;
  border-radius: 12px;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.article-card:hover .article-cover {
  transform: scale(1.03);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
  min-height: 80px;
}

.load-more-text {
  font-size: 13px;
  color: var(--color-ink-muted);
  letter-spacing: 2px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .article-cover {
    width: 150px;
    height: 110px;
  }
}

@media (max-width: 768px) {
  .article-card {
    flex-direction: column;
    gap: 16px;
    padding: 20px;
    border-radius: 12px;
  }

  .article-cover {
    width: 100%;
    height: 160px;
    border-radius: 10px;
  }

  .meta-tag {
    margin-left: 0;
  }

  .article-excerpt {
    display: none;
  }

  .article-stats {
    padding-top: 10px;
    gap: 16px;
  }
}

@media (max-width: 480px) {
  .article-card {
    padding: 16px;
  }

  .article-title {
    font-size: 17px;
  }
}
</style>
