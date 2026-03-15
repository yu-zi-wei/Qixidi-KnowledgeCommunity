<template>
  <div class="article-detail-container">
    <!-- 左侧：固定操作栏（脱离文档流） -->
    <aside class="article-actions-sidebar">
      <button class="action-btn-vertical" :class="{ active: article.isCollection }" @click="$emit('collect')" title="收藏">
        <Heart class="icon" :class="{ filled: article.isCollection }" />
        <span class="count">{{ article.collectionTimes || 0 }}</span>
      </button>

      <button class="action-btn-vertical" :class="{ active: isLiked }" @click="$emit('like')" title="点赞">
        <ThumbUp class="icon" />
        <span class="count">{{ article.likeTimes || 0 }}</span>
      </button>

      <button class="action-btn-vertical" @click="$emit('comment')" title="评论">
        <MessageCircle class="icon" />
        <span class="count">{{ article.commentTimes || 0 }}</span>
      </button>

      <!-- 编辑按钮：只有文章作者可见 -->
      <button v-if="isAuthor" class="action-btn-vertical action-btn-edit" @click="$emit('edit')" title="编辑">
        <Edit class="icon" />
      </button>
    </aside>

    <!-- 右侧：文章内容 -->
    <main class="article-main-content">
      <!-- 文章标题 -->
      <h1 class="article-title">{{ article.articleTitle }}</h1>

      <!-- 文章元数据 -->
      <div class="article-meta">
        <div class="meta-left">
          <NuxtLink :to="`/user/${article.userId}`" class="author-link">
            <img :src="article.avatar" :alt="article.nickname" class="author-avatar" />
            <span class="author-name">{{ article.nickname }}</span>
          </NuxtLink>
          <span class="meta-divider">·</span>
          <time class="publish-time">{{ formatTime(article.createTime) }}</time>
          <span v-if="article.groupingName" class="meta-divider">·</span>
          <NuxtLink v-if="article.groupingName" :to="`/category/${article.groupingId}`" class="category-link">
            {{ article.groupingName }}
          </NuxtLink>
        </div>

        <div class="meta-right">
          <span class="meta-item">
            <Eye class="meta-icon" />
            {{ article.numberTimes }}
          </span>
        </div>
      </div>

      <!-- 文章摘要 -->
      <p v-if="article.articleAbstract" class="article-abstract">
        {{ article.articleAbstract }}
      </p>

      <!-- 文章内容 -->
      <MarkdownRenderer :content="article.articleContent" class="article-content" />

      <!-- 文章标签 -->
      <div v-if="labelNames.length > 0" class="article-tags">
        <NuxtLink
          v-for="label in labelNames"
          :key="label"
          :to="`/label/${label}`"
          class="tag tag-small"
        >
          # {{ label }}
        </NuxtLink>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Eye, Heart, ThumbUp, MessageCircle, Edit } from '@vicons/tabler'

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
  fabulousUserSet?: Set<string>
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
}

const props = defineProps<Props>()

defineEmits<{
  collect: []
  like: []
  comment: []
  edit: []
}>()

// 是否已点赞
const isLiked = computed(() => {
  const authStore = useAuthStore()
  const userId = authStore.user?.uuid  // 使用 uuid 而不是 userId
  if (!userId || !props.article.fabulousUserSet) return false

  // 处理 SSR 序列化后 Set 变成数组的情况
  const userSet = props.article.fabulousUserSet
  if (typeof userSet.has === 'function') {
    // 是 Set 类型 - 需要转换为字符串比较
    return userSet.has(String(userId))
  } else if (Array.isArray(userSet)) {
    // 是数组类型（SSR 序列化后）
    return userSet.some(id => String(id) === String(userId))
  }
  return false
})

// 是否是文章作者
const isAuthor = computed(() => {
  const authStore = useAuthStore()
  return authStore.isLoggedIn && authStore.user?.uuid === props.article.userId
})

// 从 labelList 提取标签名列表
const labelNames = computed(() => {
  // 优先使用 labelNameList（字符串数组）
  if (props.article.labelNameList && props.article.labelNameList.length > 0) {
    return props.article.labelNameList
  }
  // 其次使用 labelList（对象数组）
  if (props.article.labelList && props.article.labelList.length > 0) {
    return props.article.labelList.map(item => item.labelName)
  }
  return []
})

// 格式化时间
const formatTime = (time: string) => {
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days} 天前`

  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}
</script>

<style scoped>
.article-detail-container {
  position: relative;
}

/* 左侧固定操作栏 - 视口垂直居中 */
.article-actions-sidebar {
  position: fixed;
  left: max(24px, calc((100vw - 1600px) / 2 + 24px));
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 60px;
  z-index: 10;
}

.action-btn-vertical {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 10px 8px;
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.action-btn-vertical:hover {
  color: var(--color-primary);
  background: var(--color-surface-dim);
}

.action-btn-vertical.active {
  color: var(--color-primary);
}

.action-btn-vertical.active .icon {
  stroke-width: 0;
  fill: currentColor;
}

.action-btn-vertical .icon {
  width: 24px;
  height: 24px;
  stroke-width: 1.5;
}

.action-btn-vertical .count {
  font-size: 11px;
  font-weight: 500;
}

/* 右侧内容区 */
.article-main-content {
  margin-left: 92px; /* 60px 侧边栏 + 32px gap */
}

/* 文章标题 */
.article-title {
  font-size: 28px;
  font-weight: 700;
  line-height: var(--leading-tight);
  color: var(--color-ink);
  margin: 0 0 16px 0;
}

/* 文章元数据 */
.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-top: 1px solid var(--color-border-light);
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 20px;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--color-ink-light);
}

.author-link {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: inherit;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: var(--color-ink);
}

.author-link:hover .author-name {
  color: var(--color-primary);
}

.meta-divider {
  color: var(--color-ink-faint);
}

.publish-time {
  color: var(--color-ink-muted);
}

.category-link {
  color: var(--color-primary);
  text-decoration: none;
}

.meta-right {
  display: flex;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-ink-muted);
}

.meta-icon {
  width: 15px;
  height: 15px;
}

/* 文章摘要 */
.article-abstract {
  font-size: 16px;
  line-height: var(--leading-relaxed);
  color: var(--color-ink-light);
  padding: 16px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
  margin-bottom: 20px;
}

/* 文章标签 */
.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border-light);
}

.tag {
  display: inline-block;
  padding: 4px 12px;
  font-size: 13px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.tag:hover {
  background: var(--color-primary);
  color: #fff;
}

/* 小尺寸标签（文章底部） */
.tag-small {
  padding: 3px 10px;
  font-size: 12px;
}

/* 文章内容容器 */
.article-content {
  /* Markdown 样式已在 main.css 中全局定义 */
}

/* 响应式 */
@media (max-width: 768px) {
  .article-actions-sidebar {
    position: relative;
    left: 0;
    top: 0;
    transform: none;
    width: 100%;
    flex-direction: row;
    justify-content: flex-start;
    margin-bottom: 16px;
  }

  .article-main-content {
    margin-left: 0;
  }
}
</style>
