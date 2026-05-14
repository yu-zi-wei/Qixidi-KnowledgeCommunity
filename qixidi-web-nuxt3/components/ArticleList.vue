<template>
  <div class="article-list">
    <div
      v-for="(item, index) in articles"
      :key="item.id"
      :ref="el => setCardRef(el, index)"
      class="article-card"
      :class="{ 'is-visible': visibleCards.has(index) }"
      :style="{ '--delay': Math.min(index, 8) }"
    >
      <!-- 左侧装饰条 -->
      <div class="card-accent"></div>
      <div class="article-body">
        <div class="article-meta">
          <NuxtLink :to="`/user-home/article/${item.userId}`" target="_blank" class="meta-author">
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
        <p class="article-excerpt" :style="{ WebkitLineClamp: excerptLines }">{{ item.articleAbstract }}</p>
        <div class="article-stats">
          <span class="article-stat">
            <Eye class="stat-icon" />
            {{ item.numberTimes || 0 }}
          </span>
          <span class="article-stat">
            <ThumbUp class="stat-icon" />
            {{ item.likeTimes || 0 }}
          </span>
          <span class="article-stat">
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
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { Eye, ThumbUp, MessageCircle } from '@vicons/tabler'
import type { ArticleInfo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const props = withDefaults(defineProps<{
  articles: ArticleInfo[]
  loading: boolean
  noMore: boolean
  hideCover?: boolean
  excerptLines?: number
}>(), {
  excerptLines: 2
})

const emit = defineEmits<{
  loadMore: []
}>()

const loadTrigger = ref<HTMLElement | null>(null)
const visibleCards = ref<Set<number>>(new Set())
const cardRefs = new Map<number, HTMLElement>()
let cardObserver: IntersectionObserver | null = null

// 收集卡片 ref
const setCardRef = (el: any, index: number) => {
  if (el) {
    cardRefs.set(index, el as HTMLElement)
  } else {
    cardRefs.delete(index)
  }
}

// 设置卡片可见性观察器
const setupCardObserver = () => {
  if (cardObserver) {
    cardObserver.disconnect()
  }

  cardObserver = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        const index = Number((entry.target as HTMLElement).dataset.index)
        if (entry.isIntersecting) {
          visibleCards.value.add(index)
          // 一旦可见就不再观察
          cardObserver?.unobserve(entry.target)
        }
      })
    },
    { rootMargin: '50px', threshold: 0.1 }
  )

  // 观察所有未可见的卡片
  cardRefs.forEach((el, index) => {
    if (!visibleCards.value.has(index)) {
      el.dataset.index = String(index)
      cardObserver?.observe(el)
    }
  })
}

// 加载更多观察器
onMounted(() => {
  if (!loadTrigger.value) return

  const loadObserver = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        emit('loadMore')
      }
    },
    { rootMargin: '200px' }
  )

  loadObserver.observe(loadTrigger.value)

  // 初始化卡片观察
  setTimeout(setupCardObserver, 0)

  onUnmounted(() => {
    loadObserver.disconnect()
    cardObserver?.disconnect()
  })
})

// 监听文章列表变化，重新设置观察器
watch(() => props.articles.length, () => {
  setTimeout(setupCardObserver, 0)
})
</script>

<style scoped>
.article-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 卡片入场动画 */
@keyframes card-fade-in {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 卡片设计 - 浅色背景无边框 */
.article-card {
  position: relative;
  display: flex;
  gap: 16px;
  padding: 16px;
  padding-left: 20px;
  background: rgba(255, 255, 255, 0.25);
  border: none;
  border-radius: 10px;
  transition: all 0.2s ease;
  /* 初始状态：不可见 */
  opacity: 0;
  transform: translateY(12px);
}

/* 可见状态：触发动画 */
.article-card.is-visible {
  animation: card-fade-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.04s) forwards;
}

/* 左侧装饰条 */
.card-accent {
  position: absolute;
  left: 0;
  top: 12px;
  bottom: 12px;
  width: 3px;
  background: linear-gradient(
    180deg,
    var(--color-primary) 0%,
    var(--color-accent) 100%
  );
  border-radius: 0 4px 4px 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.article-card:hover {
  background: rgba(255, 255, 255, 0.4);
}

.article-card:hover .card-accent {
  opacity: 1;
}

:root.dark .article-card {
  background: rgba(255, 255, 255, 0.03);
}

:root.dark .article-card:hover {
  background: rgba(255, 255, 255, 0.06);
}

.article-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* 元数据 - 更紧凑 */
.article-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: var(--color-ink-muted);
  margin-bottom: 8px;
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
  width: 22px;
  height: 22px;
  border-radius: 50%;
  object-fit: cover;
  background: var(--color-surface-dim);
  border: 1.5px solid rgba(255, 255, 255, 0.5);
}

.meta-author:hover .meta-avatar {
  transform: scale(1.08);
}

.meta-sep {
  opacity: 0.4;
}

.meta-tag {
  padding: 3px 10px;
  font-size: 11px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: 16px;
  font-weight: 500;
}

/* 标题 */
.article-title {
  font-size: 17px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 8px;
  overflow-wrap: break-word;
  word-break: break-word;
  letter-spacing: -0.01em;
}

.article-title a {
  color: var(--color-ink);
  text-decoration: none;
  transition: color 0.2s ease;
}

.article-title a:hover {
  color: var(--color-primary);
}

/* 摘要 */
.article-excerpt {
  font-size: 13px;
  line-height: 1.7;
  color: var(--color-ink-light);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 10px;
  flex: 1;
}

/* 行内样式会覆盖 -webkit-line-clamp，此处保留默认值 */

/* 互动数据 - 更紧凑 */
.article-stats {
  display: flex;
  gap: 16px;
  margin-top: auto;
  padding-top: 10px;
  border-top: 1px solid #eef0f2;
}

:root.dark .article-stats {
  border-top-color: #363a3e;
}

.article-stat {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: var(--color-ink-muted);
  transition: all 0.2s ease;
  padding: 4px 8px;
  margin: -4px -8px;
  border-radius: 6px;
}

.article-stat:hover {
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.stat-icon {
  width: 14px;
  height: 14px;
  stroke-width: 1.8;
  flex-shrink: 0;
}

/* 封面图片 */
.article-cover {
  width: 170px;
  height: 110px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  align-self: center;
  transition: opacity 0.2s ease;
}

.article-card:hover .article-cover {
  opacity: 0.9;
}

.load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 16px;
  min-height: 60px;
}

.load-more-text {
  font-size: 12px;
  color: var(--color-ink-muted);
  letter-spacing: 1px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .article-cover {
    width: 140px;
    height: 90px;
  }
}

@media (max-width: 768px) {
  .article-list {
    padding: 12px;
    border-radius: 8px;
  }

  .article-card {
    flex-direction: column;
    gap: 12px;
    padding: 14px;
    padding-left: 14px;
    border-radius: 8px;
  }

  .card-accent {
    display: none;
  }

  .article-cover {
    width: 100%;
    height: 140px;
    border-radius: 8px;
  }

  .meta-tag {
    margin-left: 0;
  }

  .article-stats {
    padding-top: 8px;
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .article-list {
    padding: 8px;
  }

  .article-card {
    padding: 12px;
  }

  .article-title {
    font-size: 15px;
  }

  .article-cover {
    height: 120px;
  }
}
</style>
