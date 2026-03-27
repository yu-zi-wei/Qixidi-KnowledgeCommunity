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
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { Eye, Heart, MessageCircle } from '@vicons/tabler'
import type { ArticleInfo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const props = defineProps<{
  articles: ArticleInfo[]
  loading: boolean
  noMore: boolean
  hideCover?: boolean
}>()

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
  gap: 20px;
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

/* 卡片设计 - 渐变背景 + 左侧装饰条 */
.article-card {
  position: relative;
  display: flex;
  gap: 24px;
  padding: 24px;
  padding-left: 28px;
  border-radius: 16px;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.9) 0%,
    rgba(255, 255, 255, 0.75) 100%
  );
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  /* 初始状态：不可见 */
  opacity: 0;
  transform: translateY(16px);
}

/* 可见状态：触发动画 */
.article-card.is-visible {
  animation: card-fade-in 0.4s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.05s) forwards;
}

/* 左侧装饰条 */
.card-accent {
  position: absolute;
  left: 0;
  top: 20px;
  bottom: 20px;
  width: 4px;
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
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.95) 0%,
    rgba(255, 255, 255, 0.85) 100%
  );
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.article-card:hover .card-accent {
  opacity: 1;
}

:root.dark .article-card {
  background: linear-gradient(
    135deg,
    rgba(40, 42, 45, 0.9) 0%,
    rgba(40, 42, 45, 0.75) 100%
  );
  border-color: rgba(255, 255, 255, 0.08);
}

:root.dark .article-card:hover {
  background: linear-gradient(
    135deg,
    rgba(50, 52, 55, 0.95) 0%,
    rgba(50, 52, 55, 0.85) 100%
  );
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
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
  width: 26px;
  height: 26px;
  border-radius: 50%;
  object-fit: cover;
  background: var(--color-surface-dim);
  border: 2px solid rgba(255, 255, 255, 0.6);
  transition: transform 0.2s ease;
}

.meta-author:hover .meta-avatar {
  transform: scale(1.1);
}

.meta-sep {
  opacity: 0.4;
}

.meta-tag {
  padding: 4px 12px;
  font-size: 12px;
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.1) 0%, rgba(176, 137, 104, 0.08) 100%);
  color: var(--color-primary);
  border-radius: 20px;
  font-weight: 500;
  margin-left: auto;
  border: 1px solid rgba(61, 90, 128, 0.15);
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
  background: linear-gradient(90deg, transparent 0%, transparent 100%);
  background-size: 0% 2px;
  background-position: left bottom;
  background-repeat: no-repeat;
}

.article-title a:hover {
  color: var(--color-primary);
  background: linear-gradient(90deg, var(--color-primary-light) 0%, var(--color-primary-light) 100%);
  background-size: 100% 2px;
}

/* 摘要 - 更舒适的阅读体验 */
.article-excerpt {
  font-size: 14px;
  line-height: 1.75;
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
  padding-top: 14px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

:root.dark .article-stats {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.stat-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--color-ink-muted);
  transition: all 0.2s ease;
  padding: 4px 8px;
  margin: -4px -8px;
  border-radius: 6px;
}

.stat-item:hover {
  color: var(--color-primary);
  background: var(--color-primary-light);
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
  transition: all 0.35s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.article-card:hover .article-cover {
  transform: scale(1.03);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.15);
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
    padding-left: 20px;
    border-radius: 14px;
  }

  .card-accent {
    display: none;
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
