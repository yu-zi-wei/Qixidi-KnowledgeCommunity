<template>
  <div class="feed-list">
    <article
      v-for="(item, index) in articles"
      :key="item.id"
      :ref="el => setCardRef(el, index)"
      class="feed-row"
      :class="{ 'is-visible': visibleCards.has(index) }"
      :style="{ '--delay': Math.min(index, 8) }"
    >
      <NuxtLink :to="`/articles/${item.id}`" class="feed-main">
        <span class="feed-num">{{ String(index + 1).padStart(2, '0') }}</span>
        <div class="feed-body">
          <h3 class="feed-title">{{ item.articleTitle }}</h3>
          <p v-if="item.articleAbstract" class="feed-excerpt">{{ item.articleAbstract }}</p>
        </div>
      </NuxtLink>
      <div class="feed-meta">
        <NuxtLink :to="`/user-home/article/${item.userId}`" target="_blank" class="feed-author">
          <img
            :src="item.avatar"
            :alt="item.nickname"
            class="feed-avatar"
          />
          <span>{{ item.nickname }}</span>
        </NuxtLink>
        <span class="feed-sep">&middot;</span>
        <span class="feed-date" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</span>
        <span v-if="item.groupingName" class="feed-tag">{{ item.groupingName }}</span>
        <div class="feed-stats">
          <span class="feed-stat">
            <Eye class="feed-stat-icon" />
            {{ item.numberTimes || 0 }}
          </span>
          <span class="feed-stat">
            <ThumbUp class="feed-stat-icon" />
            {{ item.likeTimes || 0 }}
          </span>
          <span class="feed-stat">
            <MessageCircle class="feed-stat-icon" />
            {{ item.commentTimes || 0 }}
          </span>
        </div>
      </div>
      <!-- 封面缩略图：右列跨两行，与主链接同目标；无封面不渲染 -->
      <NuxtLink v-if="item.articleCover" :to="`/articles/${item.id}`" class="feed-cover-link">
        <img
          :src="item.articleCover"
          :alt="item.articleTitle"
          class="feed-cover"
          loading="lazy"
          @error="onCoverError"
        >
      </NuxtLink>
    </article>

    <!-- 加载状态：置于纸页容器内，触底观察器 root 是 viewport，不受父级影响 -->
    <!-- ⚠️ 必须用 v-show 不能用 v-if：onMounted 创建触底观察器时数据未到达，
         v-if 会导致 loadTrigger 为 null、观察器永不创建，触底加载失效（2026-10-12 生产实例） -->
    <div v-show="articles.length > 0" ref="loadTrigger" class="feed-load">
      <n-spin v-if="loading" :size="20" />
      <span v-else-if="noMore" class="feed-load-text">— 没有更多了 —</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { Eye, ThumbUp, MessageCircle } from '@vicons/tabler'
import type { ArticleInfo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const props = defineProps<{
  articles: ArticleInfo[]
  loading: boolean
  noMore: boolean
}>()

// 封面加载失败时整块隐藏（裂图占位比缺图更难看）
const onCoverError = (e: Event) => {
  const link = (e.target as HTMLImageElement).parentElement
  if (link) link.style.display = 'none'
}

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
/* 整列透明：不铺底色不出块，层次交给排版（序号/墨阶/分隔线）与小面积点缀（tab/tag/hover） */
.feed-list {
  display: flex;
  flex-direction: column;
}

/* 条目入场动画 */
@keyframes feed-fade-in {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 杂志式列表行：无卡片背景，细分隔线分隔；grid 左内容右封面 */
.feed-row {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  column-gap: 18px;
  align-items: center;
  padding: 22px 16px;
  /* 分隔线用 --color-border（border-light 与页面背景色差过小不可见） */
  border-bottom: 1px solid var(--color-border);
  transition: background-color var(--transition-base);
  /* 初始状态：不可见 */
  opacity: 0;
  transform: translateY(12px);
}

/* 尾行（feed-load 是 div，article 用 of-type 匹配） */
.feed-row:last-of-type {
  border-bottom: none;
}

/* 可见状态：触发动画 */
.feed-row.is-visible {
  animation: feed-fade-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.04s) forwards;
}

/* hover 沾墨：4% 墨色浸染（浅色压暗/深色提亮，双主题同式；surface-warm 与页底同色不可见） */
.feed-row:hover {
  background: color-mix(in srgb, var(--color-ink) 4%, transparent);
}

/* 主区域：序号 + 标题/摘要，整体可点（grid 左列） */
.feed-main {
  grid-column: 1;
  display: flex;
  align-items: center;
  gap: 14px;
  text-decoration: none;
}

/* 大序号：Playfair Display italic 杂志风数字（自托管，回退 Noto Serif） */
.feed-num {
  font-family: 'Playfair Display', var(--font-display);
  font-style: italic;
  font-size: 26px;
  font-weight: 600;
  line-height: 1;
  min-width: 40px;
  /* faint 与 muted 之间偏淡取值（用户 2026-11-02 两轮校色定） */
  color: color-mix(in srgb, var(--color-ink-muted) 45%, var(--color-ink-faint));
  font-variant-numeric: tabular-nums;
  transition: color var(--transition-base);
}

.feed-row:hover .feed-num {
  color: var(--color-primary);
}

.feed-body {
  flex: 1;
  min-width: 0;
}

/* 标题：与序号同族的衬线 display 字体（中文回落 Noto Serif，英文/数字用 Playfair） */
.feed-title {
  margin: 0;
  font-family: 'Playfair Display', var(--font-display);
  font-size: var(--text-xl);
  font-weight: 600;
  line-height: 1.45;
  letter-spacing: -0.01em;
  color: var(--color-ink);
  overflow-wrap: break-word;
  word-break: break-word;
  transition: color var(--transition-fast);
}

.feed-row:hover .feed-title {
  color: var(--color-primary);
}

/* 摘要：单行截断 */
.feed-excerpt {
  margin: 6px 0 0;
  font-size: var(--text-sm);
  line-height: 1.6;
  color: var(--color-ink-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 封面缩略图：右列跨主区+元信息两行 */
.feed-cover-link {
  grid-column: 2;
  grid-row: 1 / 3;
  display: block;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.feed-cover {
  display: block;
  width: 132px;
  height: 88px;
  object-fit: cover;
  background: var(--color-surface-dim);
  transition: transform var(--transition-slow);
}

.feed-row:hover .feed-cover {
  transform: scale(1.04);
}

/* 深色模式压暗封面，避免过曝图在暗底上眩光 */
:root.dark .feed-cover {
  filter: brightness(0.85);
}

/* 元信息行：与标题左对齐（序号 40 + 间距 14） */
.feed-meta {
  grid-column: 1;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
  padding-left: 54px;
  font-size: 13px;
  color: var(--color-ink-muted);
}

.feed-author {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: var(--color-ink-light);
  font-weight: 500;
  transition: color var(--transition-fast);
}

.feed-author:hover {
  color: var(--color-primary);
}

.feed-avatar {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  object-fit: cover;
  background: var(--color-surface-dim);
}

.feed-sep {
  opacity: 0.5;
}

.feed-tag {
  padding: 2px 9px;
  font-size: 12px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: 12px;
  font-weight: 500;
}

/* 互动数据：右对齐 */
.feed-stats {
  display: flex;
  gap: 14px;
  margin-left: auto;
}

.feed-stat {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-ink-muted);
  font-variant-numeric: tabular-nums;
}

.feed-stat-icon {
  width: 14px;
  height: 14px;
  stroke-width: 1.8;
  flex-shrink: 0;
}

.feed-load {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 16px;
  min-height: 60px;
}

.feed-load-text {
  font-size: 13px;
  color: var(--color-ink-muted);
  letter-spacing: 1px;
}

/* 响应式 */
@media (max-width: 768px) {
  .feed-row {
    padding: 16px 12px;
    column-gap: 12px;
  }

  .feed-main {
    gap: 10px;
  }

  .feed-num {
    min-width: 28px;
    font-size: 21px;
  }

  .feed-title {
    font-size: 16.5px;
  }

  .feed-cover {
    width: 104px;
    height: 70px;
  }

  .feed-meta {
    padding-left: 38px;
    gap: 6px;
  }

  .feed-stats {
    gap: 10px;
  }
}

@media (max-width: 480px) {
  .feed-row {
    padding: 14px 10px;
  }

  .feed-title {
    font-size: 16px;
  }

  .feed-cover {
    width: 92px;
    height: 62px;
  }
}
</style>
