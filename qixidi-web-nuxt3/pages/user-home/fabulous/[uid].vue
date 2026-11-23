<template>
  <div class="fabulous-page">
    <!-- 左侧菜单 -->
    <div class="fabulous-sidebar">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="fabulous-menu-item"
        :class="{ active: activeTab === tab.value }"
        @click="switchTab(tab.value)"
      >
        <n-icon size="16"><component :is="tab.icon" /></n-icon>
        {{ tab.label }}
      </button>
    </div>

    <!-- 右侧内容 -->
    <div class="fabulous-content">
      <!-- 加载状态 -->
      <div v-if="pending" class="fabulous-loading">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!currentRows.length" class="fabulous-empty">
        <CommonEmptyState :description="`暂无点赞的${activeTabName}`" />
      </div>

      <template v-else>
        <!-- 文章列表 -->
        <CommonArticleList
          v-if="activeTab === 'article'"
          ref="articleListRef"
          :article-list="currentRows"
          hide-cover
          @save-state="handleSaveState"
        />

        <!-- 小记列表（行式：衬线日期 + 内容摘要，展示时间=点赞时间） -->
        <div v-else-if="activeTab === 'timeNotes'" class="fabulous-notes-list">
          <NuxtLink
            v-for="note in currentRows"
            :key="note.id"
            :to="`/time-notes/${note.id}`"
            class="fabulous-note-row"
          >
            <span class="fabulous-note-date">{{ (note.recordTime || '').slice(5) }}</span>
            <div class="fabulous-note-main">
              <p class="fabulous-note-title">{{ note.title || note.content }}</p>
              <span class="fabulous-note-time">赞于 {{ formatTime(note.createTime) }}</span>
            </div>
          </NuxtLink>
        </div>

        <!-- 随笔列表（行式：内容 + 出处，展示时间=点赞时间） -->
        <div v-else-if="activeTab === 'dictum'" class="fabulous-dictum-list">
          <NuxtLink
            v-for="essay in currentRows"
            :key="essay.id"
            :to="`/reading-essays/${essay.id}`"
            class="fabulous-dictum-row"
          >
            <p class="fabulous-dictum-content">{{ essay.content }}</p>
            <div class="fabulous-dictum-meta">
              <span v-if="essay.nickname" class="fabulous-dictum-author">{{ essay.nickname }}</span>
              <span v-if="essay.author" class="fabulous-dictum-source">—— {{ essay.author }}<template v-if="essay.worksName">《{{ essay.worksName }}》</template></span>
              <span class="fabulous-dictum-time">赞于 {{ formatTime(essay.createTime) }}</span>
            </div>
          </NuxtLink>
        </div>
      </template>

      <!-- 分页（有数据即显示：共 N 条 + 翻页器，三个 tab 一致） -->
      <div v-if="currentTotal > 0" class="fabulous-pagination">
        <span class="fabulous-pagination-total">共 {{ currentTotal }} 条</span>
        <n-pagination
          :page="currentPage"
          :item-count="currentTotal"
          :page-size="pageSize"
          :page-slot="5"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Heart, Notebook, Quote } from '@vicons/tabler'
import { formatTime } from '~/utils/formatTime'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const fabulousApi = useFabulousApi()

const uid = computed(() => route.params.uid as string)
const pageSize = 20

// 左侧菜单（tab 进 URL query，刷新保持）
const tabs = [
  { value: 'article', label: '文章', icon: Heart, name: '文章' },
  { value: 'timeNotes', label: '小记', icon: Notebook, name: '小记' },
  { value: 'dictum', label: '随笔', icon: Quote, name: '随笔' }
] as const

const activeTab = computed(() => {
  const t = route.query.tab as string
  return tabs.some(x => x.value === t) ? t : 'article'
})

const activeTabName = computed(() => tabs.find(t => t.value === activeTab.value)?.name || '')

const switchTab = (value: string) => {
  if (value === 'article') {
    navigateTo({ query: {} })
  } else {
    navigateTo({ query: { tab: value } })
  }
}

const currentPage = computed(() => Number(route.query.page) || 1)

const cacheKey = computed(() => `fabulous-${activeTab.value}-${uid.value}-${currentPage.value}`)

const fetchList = (): Promise<any> => {
  if (activeTab.value === 'timeNotes') return fabulousApi.getFabulousTimeNotesList(uid.value, currentPage.value, pageSize)
  if (activeTab.value === 'dictum') return fabulousApi.getFabulousDictumList(uid.value, currentPage.value, pageSize)
  return fabulousApi.getFabulousArticleList(uid.value, currentPage.value, pageSize)
}

const { data: pageData, pending } = useAsyncData(cacheKey, fetchList)

const currentRows = computed(() => pageData.value?.rows || [])
const currentTotal = computed(() => pageData.value?.total || 0)

const handlePageChange = (page: number) => {
  navigateTo({ query: { ...route.query, page } })
}

const articleListRef = ref<InstanceType<typeof CommonArticleList> | null>(null)

const handleSaveState = (scrollTop: number) => {
  if (import.meta.client) {
    history.replaceState({
      ...(window.history.state || {}),
      fabulousArticleScroll: scrollTop
    }, '')
  }
}

// 恢复滚动位置（文章列表）
onMounted(() => {
  if (import.meta.client && window.history.state?.fabulousArticleScroll) {
    nextTick(() => {
      articleListRef.value?.restoreScroll(window.history.state.fabulousArticleScroll)
    })
  }
})
</script>

<style>
.fabulous-page {
  display: flex;
  flex: 1;
  min-height: 0;
}

/* 左侧菜单（左右留白：选中框不贴纸缘，与内容拉开距离） */
.fabulous-sidebar {
  width: 152px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-left: 12px;
  padding-right: 16px;
  border-right: 1px solid var(--color-border-light);
}

.fabulous-menu-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  width: 100%;
}

.fabulous-menu-item:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.fabulous-menu-item.active {
  color: var(--color-primary);
  font-weight: 600;
  background: var(--color-primary-light);
}

/* 右侧内容 */
.fabulous-content {
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow-y: auto;
  padding-left: 20px;
  display: flex;
  flex-direction: column;
}

.fabulous-content::-webkit-scrollbar {
  width: 4px;
}

.fabulous-content::-webkit-scrollbar-track {
  background: transparent;
}

.fabulous-content::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.fabulous-content::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.fabulous-loading,
.fabulous-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.fabulous-pagination {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 12px 0 16px;
  border-top: 1px solid var(--color-border-light);
}

.fabulous-pagination-total {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  white-space: nowrap;
}

/* ===== 小记列表（行式，衬线日期锚点） ===== */
.fabulous-notes-list {
  display: flex;
  flex-direction: column;
}

.fabulous-note-row {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 8px;
  text-decoration: none;
  border-bottom: 1px solid var(--color-border-light);
  transition: background-color var(--transition-base);
}

.fabulous-note-row:last-child {
  border-bottom: none;
}

.fabulous-note-row:hover {
  background: color-mix(in srgb, var(--color-ink) 3%, transparent);
}

.fabulous-note-date {
  font-family: var(--font-display);
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink-faint);
  min-width: 46px;
  text-align: center;
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
}

.fabulous-note-row:hover .fabulous-note-date {
  color: var(--color-ink-muted);
}

.fabulous-note-main {
  flex: 1;
  min-width: 0;
}

.fabulous-note-title {
  margin: 0;
  font-size: var(--text-sm);
  color: var(--color-ink);
  line-height: var(--leading-normal);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-fast);
}

.fabulous-note-row:hover .fabulous-note-title {
  color: var(--color-primary);
}

.fabulous-note-time {
  display: block;
  margin-top: 2px;
  font-size: var(--text-2xs);
  color: var(--color-ink-faint);
}

/* ===== 随笔列表（行式，内容 + 出处） ===== */
.fabulous-dictum-list {
  display: flex;
  flex-direction: column;
}

.fabulous-dictum-row {
  display: block;
  padding: 16px 8px;
  text-decoration: none;
  border-bottom: 1px solid var(--color-border-light);
  transition: background-color var(--transition-base);
}

.fabulous-dictum-row:last-child {
  border-bottom: none;
}

.fabulous-dictum-row:hover {
  background: color-mix(in srgb, var(--color-ink) 3%, transparent);
}

.fabulous-dictum-content {
  margin: 0;
  font-size: var(--text-sm);
  color: var(--color-ink);
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color var(--transition-fast);
}

.fabulous-dictum-row:hover .fabulous-dictum-content {
  color: var(--color-primary);
}

.fabulous-dictum-meta {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-top: 6px;
  font-size: var(--text-2xs);
  color: var(--color-ink-muted);
  flex-wrap: wrap;
}

.fabulous-dictum-author {
  color: var(--color-ink-light);
  font-weight: 500;
}

.fabulous-dictum-source {
  color: var(--color-ink-muted);
  font-style: italic;
}

.fabulous-dictum-time {
  margin-left: auto;
  color: var(--color-ink-faint);
}

@media (max-width: 640px) {
  .fabulous-page {
    flex-direction: column;
  }

  .fabulous-sidebar {
    width: 100%;
    flex-direction: row;
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    padding-left: 0;
    padding-right: 0;
    padding-bottom: 8px;
  }

  .fabulous-content {
    padding-left: 0;
    padding-top: 12px;
  }

  .fabulous-dictum-time {
    margin-left: 0;
  }
}
</style>
