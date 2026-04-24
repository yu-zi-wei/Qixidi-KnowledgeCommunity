<template>
  <div class="article-scroll" ref="scrollAreaRef">
    <!-- 空状态 -->
    <div v-if="!articleList.length" class="article-list-empty">
      <CommonEmptyState description="暂无文章" />
    </div>

    <div class="article-list" v-else>
      <div
        v-for="(article, index) in articleList"
        :key="article.id + '-' + index"
        class="article-item"
      >
        <NuxtLink
          v-if="article.articleCover && !hideCover"
          :to="`/articles/${article.id}`"
          class="item-cover"
          @click="handleClick"
        >
          <img :src="article.articleCover" :alt="article.articleTitle" />
        </NuxtLink>
        <div class="item-body">
          <NuxtLink :to="`/articles/${article.id}`" class="item-title-link" @click="handleClick">
            <h3 class="item-title">{{ article.articleTitle }}</h3>
          </NuxtLink>
          <NuxtLink v-if="article.articleAbstract" :to="`/articles/${article.id}`" class="item-abstract-link" @click="handleClick">
            <p class="item-abstract">{{ article.articleAbstract }}</p>
          </NuxtLink>
          <div class="item-footer">
            <div class="item-footer-left">
              <NuxtLink
                v-if="article.userId"
                :to="`/user-home/article/${article.userId}`"
                target="_blank"
                class="item-author"
              >
                <img v-if="article.avatar" :src="article.avatar" class="item-author-avatar" />
                <span class="item-author-name">{{ article.nickname || article.username }}</span>
              </NuxtLink>
              <span class="item-time" v-if="article.createTime" :title="getFullDateTime(article.createTime)">
                <n-icon><Calendar /></n-icon>
                {{ formatTime(article.createTime) }}
              </span>
            </div>
            <div class="item-stats">
              <span><n-icon><Eye /></n-icon>{{ article.numberTimes || 0 }}</span>
              <span><n-icon><ThumbUp /></n-icon>{{ article.likeTimes || 0 }}</span>
              <span><n-icon><MessageCircle /></n-icon>{{ article.commentTimes || 0 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Calendar, Eye, ThumbUp, MessageCircle } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

const props = withDefaults(defineProps<{
  articleList: any[]
  hideCover?: boolean
}>(), {
  hideCover: false
})

const emit = defineEmits<{
  'save-state': [scrollTop: number]
}>()

const scrollAreaRef = ref<HTMLElement | null>(null)

const handleClick = () => {
  if (import.meta.client) {
    emit('save-state', scrollAreaRef.value?.scrollTop || 0)
  }
}

const restoreScroll = (scrollTop: number) => {
  nextTick(() => {
    if (scrollAreaRef.value) scrollAreaRef.value.scrollTop = scrollTop
  })
}

defineExpose({ restoreScroll, scrollAreaRef })
</script>

<style>
/* UserArticleList - 非 scoped */
.article-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.article-scroll::-webkit-scrollbar {
  width: 4px;
}

.article-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.article-scroll::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.article-scroll::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.article-list-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px 0 12px;
}

.article-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  transition: all var(--transition-base);
}

.article-item:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.item-cover {
  width: 120px;
  height: 80px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
  display: block;
}

.item-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
}

.item-cover:hover img {
  transform: scale(1.05);
}

.item-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-title-link {
  text-decoration: none;
}

.item-title-link:hover .item-title {
  color: var(--color-primary);
}

.item-title {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-fast);
}

.item-abstract-link {
  text-decoration: none;
  display: block;
}

.item-abstract-link:hover .item-abstract {
  color: var(--color-ink-light);
}

.item-abstract {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-fast);
}

.item-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px;
}

.item-footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-author {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.item-author:hover .item-author-name {
  color: var(--color-primary);
}

.item-author-avatar {
  width: 18px;
  height: 18px;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.item-author-name {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  transition: color var(--transition-fast);
}

.item-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.item-stats {
  display: flex;
  gap: 12px;
}

.item-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

@media (max-width: 640px) {
  .article-item {
    flex-direction: column;
  }

  .item-cover {
    display: none;
  }
}
</style>
