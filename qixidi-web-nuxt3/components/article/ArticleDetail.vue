<template>
  <div class="article-detail-container">
    <!-- 左侧：固定操作栏（脱离文档流） -->
    <aside ref="sidebarRef" class="article-actions-sidebar" :class="{ 'is-visible': sidebarVisible }">
      <button class="action-btn-vertical" :class="{ active: isLiked }" @click="$emit('like')" title="点赞">
        <ThumbUp class="icon" />
        <span class="count">{{ article.likeTimes || 0 }}</span>
      </button>

      <button v-if="showCollect" class="action-btn-vertical" :class="{ active: article.isCollection }" @click="$emit('collect')" title="收藏">
        <Heart class="icon" :class="{ filled: article.isCollection }" />
        <span class="count">{{ article.collectionTimes || 0 }}</span>
      </button>

      <button class="action-btn-vertical" @click="$emit('comment')" title="评论">
        <MessageCircle class="icon" />
        <span class="count">{{ article.commentTimes || 0 }}</span>
      </button>

      <!-- 编辑按钮：只有文章作者可见且开启了编辑功能 -->
      <button v-if="isAuthor && showEdit" class="action-btn-vertical action-btn-edit" @click="$emit('edit')" title="编辑">
        <Edit class="icon" />
      </button>
    </aside>

    <!-- 右侧：文章内容 -->
    <main ref="mainRef" class="article-main-content" :class="{ 'is-visible': mainVisible }">
      <!-- 装饰条 -->
      <div class="content-accent"></div>

      <!-- 文章标题 -->
      <h1 class="article-title">{{ article.articleTitle }}</h1>

      <!-- 文章元数据 -->
      <div class="article-meta">
        <div class="meta-left">
          <NuxtLink :to="`/user-home/article/${article.userId}`" target="_blank" class="author-link">
            <img :src="article.avatar" :alt="article.nickname" class="author-avatar" />
            <span class="author-name">{{ article.nickname }}</span>
          </NuxtLink>
          <span class="meta-divider">·</span>
          <time class="publish-time" :title="getFullDateTime(article.createTime)">{{ formatTime(article.createTime) }}</time>
          <span v-if="article.groupingName" class="meta-divider">·</span>
          <NuxtLink v-if="article.groupingName" :to="`/category/${article.groupingId}`" class="category-link">
            {{ article.groupingName }}
          </NuxtLink>
        </div>

        <div class="meta-right">
          <span class="article-meta-item">
            <Eye class="meta-icon" />
            {{ article.numberTimes }}
          </span>
        </div>
      </div>

      <!-- 文章摘要 -->
      <div v-if="showAbstract && article.articleAbstract" class="article-abstract-wrapper">
        <div class="abstract-icon">
          <Quote class="icon" />
        </div>
        <p class="article-abstract">
          {{ article.articleAbstract }}
        </p>
      </div>

      <!-- 文章内容 -->
      <MarkdownRenderer :content="article.articleContent" class="article-content" />

      <!-- 文章标签 -->
      <div v-if="labelItems.length > 0" class="article-tags">
        <div class="tags-label">
          <Hash class="icon" />
          <span>相关标签</span>
        </div>
        <div class="tags-list">
          <NuxtLink
            v-for="label in labelItems"
            :key="label.id"
            :to="`/public/label/${label.id}`"
            class="tag"
          >
            # {{ label.labelName }}
          </NuxtLink>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { Eye, Heart, ThumbUp, MessageCircle, Edit, Quote, Hash } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

interface LabelInfo {
  id: number
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  state?: number
  labelGroupingId?: number
}

interface Article {
  id: number
  userId: string
  nickname: string
  avatar: string
  articleTitle: string
  articleAbstract?: string
  articleContent: string
  articleSummary?: string
  theme?: string
  type?: number
  reprintUrl?: string
  createTime: string
  updateTime?: string
  groupingId?: number
  groupingName?: string
  labelList?: LabelInfo[]  // 后端返回的是对象数组
  labelNameList?: string[]
  numberTimes: number
  collectionTimes: number
  likeTimes: number
  commentTimes: number
  isCollection?: boolean
  isFabulous?: boolean
  collectionRecordId?: number  // 收藏记录 id（用于取消收藏）
  isFollow?: boolean
  // 继承自 CountUserWebsiteEntity 的统计字段
  fabulousCount?: number
  collectionCount?: number
  followCount?: number
  fansFollowCount?: number
  commentCount?: number
  articleCount?: number
}

interface Props {
  article: Article
  showCollect?: boolean
  showEdit?: boolean
  showAbstract?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showCollect: true,
  showEdit: true,
  showAbstract: true
})

defineEmits<{
  collect: []
  like: []
  comment: []
  edit: []
}>()

// 是否已点赞
const isLiked = computed(() => {
  return !!props.article.isFabulous
})

// 是否是文章作者
const isAuthor = computed(() => {
  const authStore = useAuthStore()
  return authStore.isLoggedIn && authStore.user?.uuid === props.article.userId
})

// 动画相关
const sidebarRef = ref<HTMLElement | null>(null)
const mainRef = ref<HTMLElement | null>(null)
const sidebarVisible = ref(false)
const mainVisible = ref(false)

// 使用 IntersectionObserver 监听元素进入视口
onMounted(() => {
  // 侧边栏动画
  if (sidebarRef.value) {
    const sidebarObserver = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          sidebarVisible.value = true
          sidebarObserver.disconnect()
        }
      },
      { threshold: 0.1 }
    )
    sidebarObserver.observe(sidebarRef.value)
  }

  // 主内容区动画
  if (mainRef.value) {
    const mainObserver = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          mainVisible.value = true
          mainObserver.disconnect()
        }
      },
      { threshold: 0.1 }
    )
    mainObserver.observe(mainRef.value)
  }
})

// 从 labelList 提取标签（带 id）
const labelItems = computed(() => {
  if (props.article.labelList && props.article.labelList.length > 0) {
    return props.article.labelList
  }
  return []
})
</script>

<style scoped>
/* 入场动画 */
@keyframes sidebar-fade-in {
  from {
    opacity: 0;
    transform: translateY(-50%) translateX(-16px);
  }
  to {
    opacity: 1;
    transform: translateY(-50%) translateX(0);
  }
}

.article-detail-container {
  position: relative;
}

/* 左侧固定操作栏 - 透明简洁 */
.article-actions-sidebar {
  position: fixed;
  left: max(24px, calc((100vw - 1400px) / 2 + 24px));
  top: 50%;
  transform: translateY(-50%) translateX(-16px);
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px 6px;
  z-index: 10;
  background: transparent;
  border: none;
  border-radius: 16px;
  /* 初始状态 */
  opacity: 0;
  transition: all 0.3s ease;
}

.article-actions-sidebar:hover {
  background: var(--color-block-bg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

.article-actions-sidebar.is-visible {
  animation: sidebar-fade-in 0.5s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.action-btn-vertical {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  padding: 10px 8px;
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-btn-vertical:hover {
  color: var(--color-primary);
  background: rgba(154, 113, 88, 0.08);
}

.action-btn-vertical.active {
  color: var(--color-primary);
}

.action-btn-vertical.active .icon {
  stroke-width: 0;
  fill: currentColor;
}

.action-btn-vertical .icon {
  width: 20px;
  height: 20px;
  stroke-width: 1.8;
}

.action-btn-vertical .count {
  font-size: 11px;
  font-weight: 500;
}

/* 右侧内容区 - 透明透气设计 */
.article-main-content {
  margin-left: 62px; /* 60px 侧边栏 + 32px gap */
  padding: 0 10px 28px 32px;
  position: relative;
  /* 更透明的背景，能隐约看到背景格子 */
  background: var(--color-content-tint);
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border-radius: 20px;
  border: none;
}

.article-main-content.is-visible {
  /* 无需动画，直接显示 */
}

/* 移除装饰条，保持简洁 */
.content-accent {
  display: none;
}

/* 装饰条 */
.content-accent {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(
    90deg,
    var(--color-primary) 0%,
    var(--color-accent) 100%
  );
  border-radius: 20px 20px 0 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.article-main-content:hover .content-accent {
  opacity: 1;
}

/* 文章标题 - 简洁大气 */
.article-title {
  font-size: 36px;
  font-weight: 700;
  line-height: 1.25;
  color: var(--color-ink);
  margin: 0 0 24px 0;
  letter-spacing: -0.03em;
}

.article-title::after {
  display: none;
}

/* 文章元数据 - 简洁分隔 */
.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  margin-bottom: 28px;
}

.dark .article-meta {
  border-bottom-color: rgba(255, 255, 255, 0.06);
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--color-ink-light);
}

.author-link {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: inherit;
  padding: 4px 8px;
  margin: -4px -8px;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.author-link:hover {
  background: rgba(154, 113, 88, 0.08);
}

.author-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.6);
  transition: transform 0.2s ease;
}

.author-link:hover .author-avatar {
  transform: scale(1.1);
}

.author-name {
  font-weight: 600;
  color: var(--color-ink);
}

.author-link:hover .author-name {
  color: var(--color-primary);
}

.meta-divider {
  color: var(--color-ink-faint);
  opacity: 0.5;
}

.publish-time {
  color: var(--color-ink-muted);
}

.category-link {
  color: var(--color-ink-muted);
  text-decoration: none;
  transition: color 0.2s ease;
}

.category-link:hover {
  color: var(--color-primary);
}

.meta-right {
  display: flex;
  gap: 16px;
}

.article-meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
  color: var(--color-ink-muted);
  padding: 4px 8px;
  margin: -4px -8px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.article-meta-item:hover {
  background: rgba(154, 113, 88, 0.08);
  color: var(--color-primary);
}

.meta-icon {
  width: 16px;
  height: 16px;
}

/* 文章摘要 - 左侧竖线装饰 */
.article-abstract-wrapper {
  position: relative;
  display: flex;
  gap: 16px;
  padding: 0 0 0 20px;
  margin-bottom: 32px;
  border-left: 2px solid var(--color-primary);
}

.abstract-icon {
  display: none;
}

.article-abstract {
  flex: 1;
  font-size: 15px;
  line-height: 1.8;
  color: var(--color-ink-light);
  margin: 0;
  font-style: italic;
}

/* 文章标签 - 简洁设计 */
.article-tags {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .article-tags {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.tags-label {
  display: none;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 12px;
  font-size: 13px;
  color: var(--color-ink-muted);
  background: transparent;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 16px;
  text-decoration: none;
  transition: all 0.2s ease;
}

.tag:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
  background: rgba(154, 113, 88, 0.05);
}

.dark .tag {
  border-color: rgba(255, 255, 255, 0.08);
}

.dark .tag:hover {
  background: rgba(201, 160, 122, 0.1);
}

/* 文章内容容器 */
.article-content {
  /* Markdown 样式已在 main.css 中全局定义 */
}

.article-content :deep(p),
.article-content :deep(li) {
  line-height: 30px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .article-actions-sidebar {
    left: 12px;
    padding: 10px 6px;
  }

  .article-main-content {
    margin-left: 72px;
  }

  .article-title {
    font-size: 30px;
  }
}

@media (max-width: 768px) {
  .article-actions-sidebar {
    position: fixed;
    left: 50%;
    top: auto;
    bottom: 24px;
    transform: translateX(-50%);
    flex-direction: row;
    width: auto;
    padding: 10px 14px;
    gap: 6px;
    background: var(--color-dropdown-bg);
    backdrop-filter: blur(12px);
  }

  .article-actions-sidebar.is-visible {
    animation: none;
    opacity: 1;
  }

  .action-btn-vertical {
    padding: 8px 10px;
  }

  .action-btn-vertical .count {
    display: none;
  }

  .article-main-content {
    margin-left: 0;
  }

  .article-main-content.is-visible {
    animation: none;
    opacity: 1;
  }

  .article-title {
    font-size: 24px;
  }

  .article-abstract-wrapper {
    padding-left: 16px;
  }

  .article-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .meta-right {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .article-title {
    font-size: 22px;
  }

  .author-avatar {
    width: 24px;
    height: 24px;
  }
}
</style>
