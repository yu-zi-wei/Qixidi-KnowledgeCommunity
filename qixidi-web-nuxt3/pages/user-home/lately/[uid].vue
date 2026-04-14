<template>
  <div class="lately-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!historyList.length" class="empty-state">
      <n-empty description="暂无浏览记录" />
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
          <div class="item-type-badge" :class="getTypeClass(item.targetType)">
            {{ getTypeName(item.targetType) }}
          </div>
          <div class="item-info">
            <h3 class="item-title">{{ item.targetTitle }}</h3>
            <span class="item-time" :title="getFullDateTime(item.createTime)">
              {{ formatTime(item.createTime) }}
            </span>
          </div>
          <n-icon class="item-arrow"><ChevronRight /></n-icon>
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
import { ChevronRight } from '@vicons/tabler'
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

const { data: pageData, pending } = useAsyncData(
  cacheKey,
  () => browsingHistoryApi.getList({
    pageNum: currentPage.value,
    pageSize,
    uid: uid.value
  })
)

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
  return type === 1 ? '文章' : '帖子'
}

const getTypeClass = (type: number) => {
  return type === 1 ? 'type-article' : 'type-post'
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
  gap: 8px;
  padding-bottom: 12px;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  cursor: pointer;
  text-decoration: none;
  transition: all var(--transition-base);
}

.history-item:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.item-type-badge {
  flex-shrink: 0;
  padding: 2px 10px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
}

.type-article {
  background: var(--color-primary-light);
  color: var(--color-primary);
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
