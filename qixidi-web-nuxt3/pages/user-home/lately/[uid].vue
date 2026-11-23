<template>
  <div class="lately-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!historyList.length" class="empty-state">
      <CommonEmptyState description="暂无浏览记录" />
    </div>

    <!-- 列表 -->
    <template v-else>
      <div class="history-list">
        <NuxtLink
          v-for="item in historyList"
          :key="item.id"
          :to="getTargetLink(item)"
          class="history-item"
          @click="handleClick(item)"
        >
          <div class="item-info">
            <div class="item-title-row">
              <span class="item-action-badge" :class="getActionClass(item.actionType)">
                {{ getActionName(item.actionType) }}
              </span>
              <h3 class="item-title">{{ item.targetTitle }}</h3>
              <span class="item-type-badge" :class="getTypeClass(item.targetType)">
                {{ getTypeName(item.targetType) }}
              </span>
            </div>
            <!-- 评论内容快照：评论删除后仍保留当时评的内容 -->
            <p v-if="item.content" class="item-comment-snapshot">{{ item.content }}</p>
            <span class="item-time" :title="getFullDateTime(item.createTime)">
              {{ formatTime(item.createTime) }}
            </span>
          </div>
          <n-icon class="item-arrow"><ArrowRight /></n-icon>
        </NuxtLink>
      </div>

      <!-- 分页 -->
      <div v-if="total > pageSize" class="pagination">
        <n-pagination
          v-model:page="currentPage"
          :item-count="total"
          :page-size="pageSize"
          :page-slot="5"
          size="small"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ArrowRight } from '@vicons/tabler'
import type { BrowsingHistoryVo } from '~/composables/useBrowsingHistoryApi'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const browsingHistoryApi = useBrowsingHistoryApi()

const uid = computed(() => route.params.uid as string)
const pageSize = 20
const currentPage = ref(1)

// 客户端：从 history.state 恢复页码
if (import.meta.client) {
  const saved = window.history.state?.latelyPage
  if (saved) currentPage.value = saved
}

// computed key —— 页码变化时自动重新获取
const cacheKey = computed(() => `user-home-lately-${uid.value}-${currentPage.value}`)

const { data: pageData, pending, refresh: refreshHistory } = useAsyncData(
  cacheKey,
  () => browsingHistoryApi.getList({
    pageNum: currentPage.value,
    pageSize,
    uid: uid.value
  })
)

// 登录后刷新浏览记录
const authStore = useAuthStore()
watch(() => authStore.isLoggedIn, (val) => {
  if (val) refreshHistory()
})

const historyList = computed(() => pageData.value?.rows || [])
const total = computed(() => pageData.value?.total || 0)

// 点击时保存当前状态，返回后可恢复
const handleClick = (item: BrowsingHistoryVo) => {
  if (import.meta.client) {
    const listEl = document.querySelector('.history-list') as HTMLElement | null
    history.replaceState({
      ...(window.history.state || {}),
      latelyPage: currentPage.value,
      latelyScroll: listEl?.scrollTop || 0
    }, '')
  }
}

const getTargetLink = (item: BrowsingHistoryVo) => {
  if (item.targetType === 1) return `/articles/${item.targetId}`
  if (item.targetType === 2) return `/time-notes/${item.targetId}`
  if (item.targetType === 3) return `/reading-essays/${item.targetId}`
  return ''
}

// 恢复滚动位置（仅从详情页返回时）
onMounted(() => {
  if (import.meta.client && window.history.state?.latelyScroll) {
    nextTick(() => {
      const listEl = document.querySelector('.history-list') as HTMLElement | null
      if (listEl) listEl.scrollTop = window.history.state.latelyScroll
    })
  }
})

const getTypeName = (type: number) => {
  return type === 1 ? '文章' : type === 2 ? '小记' : type === 3 ? '随笔' : '内容'
}

const getTypeClass = (type: number) => {
  return type === 1 ? 'type-article' : type === 2 ? 'type-note' : type === 3 ? 'type-dictum' : 'type-post'
}

// 行为徽标：这条足迹由什么行为产生（点赞/评论会覆盖此前的阅读记录）
const getActionName = (action: number) => {
  return action === 2 ? '赞过' : action === 3 ? '评论过' : '阅读'
}

const getActionClass = (action: number) => {
  return action === 2 ? 'action-like' : action === 3 ? 'action-comment' : 'action-read'
}
</script>

<style scoped>
.lately-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.history-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  padding-bottom: 12px;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  cursor: pointer;
  text-decoration: none;
  transition: background var(--transition-base);
}

/* 条目间细分隔线（纸上排版线） */
.history-item + .history-item {
  border-top: 1px solid var(--color-border-light);
}

.history-item:hover {
  background: color-mix(in srgb, var(--color-ink) 4%, transparent);
}

.item-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

/* 评论内容快照：引用式一行截断，淡墨 */
.item-comment-snapshot {
  margin: 2px 0 0;
  font-size: var(--text-xs);
  line-height: var(--leading-normal);
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-type-badge {
  flex-shrink: 0;
  padding: 1px 8px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
}

/* 行为徽标（阅读/赞过/评论过）：与类型徽标同形，色相区分 */
.item-action-badge {
  flex-shrink: 0;
  padding: 1px 8px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
}

.action-read {
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.action-like {
  background: color-mix(in srgb, var(--color-danger) 10%, transparent);
  color: var(--color-danger);
}

.action-comment {
  background: color-mix(in srgb, var(--tag-teal) 13%, transparent);
  color: var(--tag-teal);
}

.type-article {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.type-note {
  background: color-mix(in srgb, var(--tag-green) 13%, transparent);
  color: var(--tag-green);
}

.type-dictum {
  background: color-mix(in srgb, var(--tag-amber) 13%, transparent);
  color: var(--tag-amber);
}

.type-post {
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.item-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.item-title {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
  margin: 0;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-time {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.item-arrow {
  flex-shrink: 0;
  color: var(--color-ink-faint);
}

.pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding-top: 12px;
  border-top: 1px solid var(--color-border-light);
  margin-top: 8px;
}

/* 细滚动条 */
.history-list::-webkit-scrollbar {
  width: 4px;
}

.history-list::-webkit-scrollbar-track {
  background: transparent;
}

.history-list::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.history-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}
</style>
