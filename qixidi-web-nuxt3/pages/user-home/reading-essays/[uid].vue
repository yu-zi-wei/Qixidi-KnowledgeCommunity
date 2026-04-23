<template>
  <div class="reading-essays-page">
    <div class="re-search">
      <n-input v-model:value="searchKeyword" placeholder="搜索随笔内容" clearable>
        <template #prefix>
          <n-icon><Search /></n-icon>
        </template>
      </n-input>
    </div>

    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <div v-else-if="!essayList.length" class="empty-state">
      <CommonEmptyState :description="apiKeyword ? '没有找到相关随笔' : '暂无随笔'" />
    </div>

    <template v-else>
      <div class="essay-list-scroll">
        <div class="essay-list">
          <div
            v-for="essay in essayList"
            :key="essay.id"
            class="essay-card"
          >
            <div class="essay-content">{{ essay.content }}</div>

            <div v-if="essay.author || essay.worksName" class="essay-source">
              <span class="source-sep">——</span>
              <span v-if="essay.author" class="source-author">{{ essay.author }}</span>
              <span v-if="essay.worksName" class="source-work">《{{ essay.worksName }}》</span>
            </div>

            <div class="essay-meta">
              <span v-if="essay.groupName" class="meta-badge">{{ essay.groupName }}</span>
              <div v-if="essay.labelList && essay.labelList.length > 0" class="meta-labels">
                <span v-for="label in essay.labelList.slice(0, 3)" :key="label" class="label-tag">
                  # {{ label }}
                </span>
              </div>
            </div>

            <div class="essay-footer">
              <div class="footer-left">
                <span class="user-essay-stat">
                  <n-icon size="14"><Heart /></n-icon>
                  {{ essay.helpSum || 0 }}
                </span>
                <span class="user-essay-stat">
                  <n-icon size="14"><MessageCircle /></n-icon>
                  {{ essay.commentSum || 0 }}
                </span>
                <span class="user-essay-stat" :title="getFullDateTime(essay.createTime)">
                  <n-icon size="14"><Calendar /></n-icon>
                  {{ formatTime(essay.createTime) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="total > pageSize" class="re-pagination">
        <n-pagination
          :page="currentPage"
          :item-count="total"
          :page-size="pageSize"
          :page-slot="5"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { Heart, MessageCircle, Calendar, Search } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const readingEssaysApi = useReadingEssaysApi()

const uid = computed(() => route.params.uid as string)
const pageSize = 20
const currentPage = computed(() => Number(route.query.page) || 1)

const searchKeyword = ref('')
const apiKeyword = ref('')

let debounceTimer: ReturnType<typeof setTimeout> | null = null
watch(searchKeyword, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    apiKeyword.value = val
  }, 400)
})

const cacheKey = computed(() => `user-home-essays-${uid.value}-${currentPage.value}-${apiKeyword.value}`)

const { data: pageData, pending } = await useAsyncData(
  cacheKey,
  () => readingEssaysApi.getReadingEssaysList({
    pageNum: currentPage.value,
    pageSize,
    uid: uid.value,
    content: apiKeyword.value || undefined
  })
)

const essayList = computed(() => pageData.value?.rows || [])
const total = computed(() => pageData.value?.total || 0)

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}
</script>

<style>
.reading-essays-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.re-search {
  flex-shrink: 0;
  padding: 0 24px 12px;
}

.reading-essays-page .loading-state,
.reading-essays-page .empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.essay-list-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 0 24px;
}

.essay-list-scroll::-webkit-scrollbar {
  width: 6px;
}

.essay-list-scroll::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.essay-list-scroll::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.essay-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 4px 0;
}

.essay-card {
  padding: 16px 20px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition: all var(--transition-base);
}

.essay-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.essay-content {
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  color: var(--color-ink);
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.essay-source {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  font-style: italic;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
}

.source-sep {
  color: var(--color-ink-faint);
}

.source-author {
  color: var(--color-ink-light);
}

.source-work {
  color: var(--color-ink-muted);
}

.essay-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.meta-badge {
  font-size: var(--text-xs);
  padding: 4px 10px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  color: var(--color-ink-light);
}

.dark .meta-badge {
  background: rgba(255, 255, 255, 0.08);
}

.meta-labels {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.label-tag {
  font-size: 12px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 4px 10px;
  border-radius: 12px;
}

.essay-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .essay-footer {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-essay-stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.reading-essays-page .re-pagination {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  padding: 12px 0 16px;
  background: var(--color-surface-warm);
  border-top: 1px solid var(--color-border-light);
}
</style>
