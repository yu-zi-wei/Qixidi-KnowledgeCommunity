<template>
  <div class="recent-activity">
    <div class="panel-header">
      <h3 class="panel-title">最新互动</h3>
      <NuxtLink to="/news" class="panel-more">查看全部</NuxtLink>
    </div>

    <div v-if="loading" class="activity-loading">
      <n-skeleton v-for="i in 4" :key="i" :sharp="false" height="44px" />
    </div>

    <div v-else-if="activities.length === 0" class="activity-empty">
      <n-icon size="36"><BellOff /></n-icon>
      <p>暂无互动消息，发布内容让更多人看到你吧</p>
    </div>

    <ul v-else class="activity-list">
      <li v-for="item in activities" :key="item.key" class="activity-item" @click="goTarget(item)">
        <n-avatar round :size="34" :src="item.senderAvatar || '/images/default-avatar.svg'" class="activity-avatar" />
        <div class="activity-body">
          <p class="activity-text" :title="`${item.senderName} ${item.action}${item.target ? ` ${item.target}` : ''}`">
            <span class="activity-name">{{ item.senderName }}</span>
            <span class="activity-action">{{ item.action }}</span>
            <span v-if="item.target" class="activity-target">{{ item.target }}</span>
          </p>
          <time class="activity-time" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
        </div>
        <n-icon v-if="item.beenRead === 0" class="activity-unread" size="8"><Point /></n-icon>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { BellOff, Point } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

interface ActivityItem {
  key: string
  senderName: string
  senderAvatar: string
  action: string
  target?: string
  createTime: string
  beenRead: number
  linkTo?: string
}

const newsApi = useNewsApi()
const loading = ref(true)
const activities = ref<ActivityItem[]>([])

// 三类互动（点赞/关注/文章评论）各取一页，前端归并按时间倒序
const loadActivities = async () => {
  loading.value = true
  try {
    const [fabulous, follow, comment] = await Promise.all([
      newsApi.getFabulousList(1, 5),
      newsApi.getFollowList(1, 5),
      newsApi.getCommentList(1, 5)
    ])
    const merged: ActivityItem[] = [
      ...(fabulous.rows || []).map(r => ({
        key: `fab-${r.newsId}`,
        senderName: r.senderName,
        senderAvatar: r.senderAvatar,
        action: '赞了你的内容',
        createTime: r.createTime,
        beenRead: r.beenRead,
        linkTo: r.targetId ? `/articles/${r.targetId}` : undefined
      })),
      ...(follow.rows || []).map(r => ({
        key: `fol-${r.newsId}`,
        senderName: r.senderName,
        senderAvatar: r.senderAvatar,
        action: '关注了你',
        createTime: r.createTime,
        beenRead: r.beenRead,
        linkTo: r.senderId ? `/user-home/article/${r.senderId}` : undefined
      })),
      ...(comment.rows || []).map(r => ({
        key: `cmt-${r.newsId}`,
        senderName: r.commentName,
        senderAvatar: r.commentAvatar,
        action: '评论了你的文章',
        target: r.articleTitle,
        createTime: r.createTime,
        beenRead: r.beenRead,
        linkTo: r.articleId ? `/articles/${r.articleId}` : undefined
      }))
    ]
    merged.sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime())
    activities.value = merged.slice(0, 6)
  } catch {
    activities.value = []
  } finally {
    loading.value = false
  }
}

const goTarget = (item: ActivityItem) => {
  if (item.linkTo) navigateTo(item.linkTo)
}

onMounted(loadActivities)
</script>

<style scoped>
.recent-activity {
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

.activity-loading {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-3);
  color: var(--color-ink-faint);
  padding: 32px 0;
}

.activity-empty p {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
}

.activity-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 10px 8px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background var(--transition-base);
}

.activity-item:hover {
  background: var(--color-surface-warm);
}

.activity-avatar {
  flex-shrink: 0;
}

.activity-body {
  flex: 1;
  min-width: 0;
}

.activity-text {
  margin: 0;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.activity-name {
  color: var(--color-ink);
  font-weight: 500;
}

.activity-target {
  color: var(--color-primary);
}

.activity-time {
  display: block;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin-top: 2px;
}

.activity-unread {
  color: var(--color-danger);
  margin-top: 6px;
  flex-shrink: 0;
}
</style>
