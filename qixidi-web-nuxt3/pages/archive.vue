<template>
  <div class="archive-page">
    <!-- 顶部 Tab 切换 -->
    <div class="archive-tab-bar">
      <button
        class="archive-tab-item"
        :class="{ active: activeTab === 'article' }"
        @click="switchTab('article')"
      >
        文章
      </button>
      <button
        class="archive-tab-item"
        :class="{ active: activeTab === 'notes' }"
        @click="switchTab('notes')"
      >
        小记
      </button>
    </div>

    <!-- 左侧年份目录 -->
    <aside class="archive-sidebar">
      <div v-if="isPending" class="archive-loading">
        <n-spin size="small" />
      </div>
      <template v-else>
        <button
          v-for="year in yearList"
          :key="year"
          class="archive-year-btn"
          :class="{ active: activeYear === year }"
          @click="scrollToYear(year)"
        >
          {{ year }}
        </button>
      </template>
    </aside>

    <!-- 右侧内容 -->
    <div class="archive-main" ref="mainRef">
      <div v-if="isPending" class="archive-loading">
        <n-spin size="large" />
      </div>
      <div v-else-if="!displayGroups.length" class="archive-empty">
        <CommonEmptyState :description="activeTab === 'article' ? '暂无文章' : '暂无小记'" />
      </div>
      <template v-else>
        <div
          v-for="group in displayGroups"
          :key="group.key"
          :ref="(el) => setYearRef(group.key, el)"
          class="archive-month-group"
        >
          <div class="archive-month-header">
            <span>{{ formatMonthTitle(group.key) }}</span>
            <span class="archive-month-count">「{{ group.count }}篇」</span>
          </div>
          <!-- 文章列表 -->
          <template v-if="activeTab === 'article'">
            <NuxtLink
              v-for="article in group.items"
              :key="article.id"
              :to="`/articles/${article.id}`"
              class="archive-item"
              @click="saveScroll"
            >
              <span class="archive-item-date">{{ formatDate(article.createTime) }}</span>
              <span class="archive-item-title">{{ article.articleTitle }}</span>
            </NuxtLink>
          </template>
          <!-- 小记列表 -->
          <template v-else>
            <div
              v-for="note in group.items"
              :key="note.id"
              class="archive-item archive-item--clickable"
              @click="handleNoteClick(note)"
            >
              <span class="archive-item-date">{{ formatDate(note.recordTime) }}</span>
              <span class="archive-item-title">{{ note.title || '无标题' }}</span>
              <n-icon v-if="note.isContent" size="14" class="note-content-icon">
                <FileText />
              </n-icon>
            </div>
          </template>
        </div>
      </template>
    </div>

    <!-- 小记详情抽屉 -->
    <n-drawer v-model:show="drawerVisible" :width="640" placement="right">
      <n-drawer-content title="小记详情" :closable="true" :native-scrollbar="false">
        <div class="drawer-detail-wrapper">
          <div v-if="drawerLoading" class="drawer-loading">
            <n-spin size="medium" />
          </div>
          <TimeNotesDetail v-else-if="drawerDetail" :detail="drawerDetail" @edit="handleNoteEdit" />
          <div v-else class="drawer-loading">加载失败</div>
        </div>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { FileText } from '@vicons/tabler'
import type { TimeNotes, TimeNotesInfo } from '~/types'

definePageMeta({ showTabBar: false })

const api = useApi()
const timeNotesApi = useTimeNotesApi()
const router = useRouter()

// ==================== 文章归档类型 ====================
interface ArchiveArticle {
  id: number
  articleTitle: string
  createTime: string
}

interface ArticleArchiveGroup {
  createTime: string
  list: ArchiveArticle[]
}

// ==================== 统一展示类型 ====================
interface DisplayGroup<T> {
  key: string
  items: T[]
  count: number
}

// ==================== 状态 ====================
const activeTab = ref<'article' | 'notes'>('article')
const mainRef = ref<HTMLElement | null>(null)
const yearRefs = ref<Record<string, HTMLElement>>({})
const activeYear = ref<string | null>(null)

// 抽屉
const drawerVisible = ref(false)
const drawerDetail = ref<TimeNotesInfo | null>(null)
const drawerLoading = ref(false)

const setYearRef = (key: string, el: any) => {
  if (el) yearRefs.value[key] = el as HTMLElement
}

// ==================== 文章数据 ====================
const { data: articleData, pending: articlePending } = await useAsyncData(
  'article-archive',
  () => api.getPage<ArticleArchiveGroup>('/white/article/archive', { pageNum: 1, pageSize: -1 })
)

// ==================== 小记数据 ====================
const { data: notesData, pending: notesPending, refresh: refreshNotes } = await useAsyncData(
  'notes-archive',
  () => timeNotesApi.getTimeNotesArchive({ pageNum: 1, pageSize: -1 })
)

// ==================== 计算属性 ====================
const isPending = computed(() =>
  activeTab.value === 'article' ? articlePending.value : notesPending.value
)

// 文章分组：后端已按月分组
const articleGroups = computed<DisplayGroup<ArchiveArticle>[]>(() => {
  const rows = articleData.value?.rows || []
  return rows.map(g => ({ key: g.createTime, items: g.list || [], count: g.list?.length || 0 }))
})

// 小记分组：后端按日分组，需重新按月聚合
const notesGroups = computed<DisplayGroup<TimeNotes>[]>(() => {
  const rows = notesData.value?.rows || []
  const monthMap = new Map<string, TimeNotes[]>()
  rows.forEach(g => {
    if (!g.recordTime) return
    const month = g.recordTime.substring(0, 7) // "2025-03"
    const existing = monthMap.get(month) || []
    existing.push(...(g.list || []))
    monthMap.set(month, existing)
  })
  return Array.from(monthMap.entries())
    .sort(([a], [b]) => b.localeCompare(a))
    .map(([key, items]) => ({ key, items, count: items.length }))
})

// 当前展示的分组
const displayGroups = computed(() =>
  activeTab.value === 'article' ? articleGroups.value : notesGroups.value
)

// 年份列表
const yearList = computed(() => {
  const years = new Set<string>()
  displayGroups.value.forEach(g => {
    if (g.key) years.add(g.key.split('-')[0])
  })
  return Array.from(years).sort((a, b) => b.localeCompare(a))
})

// ==================== Tab 切换 ====================
const switchTab = (tab: 'article' | 'notes') => {
  if (activeTab.value === tab) return
  activeTab.value = tab
  yearRefs.value = {}
  activeYear.value = null
  // 切换后重新高亮第一个年份
  nextTick(() => {
    if (yearList.value.length > 0) {
      activeYear.value = yearList.value[0]
    }
    window.scrollTo({ top: 0, behavior: 'instant' as ScrollBehavior })
  })
}

// ==================== 滚动交互 ====================
const scrollToYear = (year: string) => {
  const target = displayGroups.value.find(g => g.key?.startsWith(year))
  if (!target) return
  const el = yearRefs.value[target.key]
  if (!el) return
  el.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

onMounted(() => {
  const handleScroll = () => {
    const groups = displayGroups.value
    for (let i = groups.length - 1; i >= 0; i--) {
      const el = yearRefs.value[groups[i].key]
      if (!el) continue
      const rect = el.getBoundingClientRect()
      if (rect.top <= 160) {
        activeYear.value = groups[i].key.split('-')[0]
        return
      }
    }
    if (groups.length > 0) {
      activeYear.value = groups[0].key.split('-')[0]
    }
  }

  window.addEventListener('scroll', handleScroll, { passive: true })
  if (yearList.value.length > 0) {
    activeYear.value = yearList.value[0]
  }

  const savedScroll = window.history.state?.archiveScroll
  if (savedScroll) {
    nextTick(() => window.scrollTo({ top: savedScroll, behavior: 'instant' as ScrollBehavior }))
  }

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
})

// ==================== 小记详情 ====================
const handleNoteClick = async (note: TimeNotes) => {
  drawerDetail.value = null
  drawerVisible.value = true
  drawerLoading.value = true
  try {
    drawerDetail.value = await timeNotesApi.getTimeNotesDetail(note.id)
  } catch {
  } finally {
    drawerLoading.value = false
  }
}

const handleNoteEdit = (id: number) => {
  drawerVisible.value = false
  router.push(`/write/note/${id}`)
}

// ==================== 工具方法 ====================
const saveScroll = () => {
  history.replaceState({ ...(window.history.state || {}), archiveScroll: window.scrollY }, '')
}

const formatMonthTitle = (dateStr: string) => {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length >= 2) return `${parts[0]}年${parts[1]}月`
  return dateStr
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length >= 3) return `${parts[1]}-${parts[2]}`
  return dateStr
}

useHead({
  title: '归档',
  bodyAttrs: {
    class: 'page-archive'
  }
})
</script>

<style>
.archive-page {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  padding-top: 20px;
  align-items: flex-start;
}

/* Tab 切换 */
.archive-tab-bar {
  width: 100%;
  display: flex;
  gap: var(--space-1);
  margin-bottom: var(--space-2);
  border-bottom: 1px solid var(--color-border-light);
}

.archive-tab-item {
  padding: var(--space-2) var(--space-4);
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-base);
  color: var(--color-ink-muted);
  transition: all var(--transition-fast);
  position: relative;
  font-weight: 400;
}

.archive-tab-item:hover {
  color: var(--color-ink);
}

.archive-tab-item.active {
  color: var(--color-primary);
  font-weight: 600;
}

.archive-tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--color-primary);
  border-radius: 1px;
}

/* 左侧年份目录 - 时间线 */
.archive-sidebar {
  width: 80px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 120px;
  padding-left: 12px;
  border-left: 2px solid var(--color-border-light);
  margin-left: 8px;
}

.archive-year-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px 10px 0;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  width: 100%;
  font-weight: 400;
  position: relative;
}

.archive-year-btn::before {
  content: '';
  position: absolute;
  left: -13px;
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-border);
  transition: all var(--transition-fast);
}

.archive-year-btn:hover {
  color: var(--color-ink);
}

.archive-year-btn:hover::before {
  background: var(--color-ink-muted);
}

.archive-year-btn.active {
  color: var(--color-primary);
  font-weight: 600;
}

.archive-year-btn.active::before {
  background: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 右侧内容 */
.archive-main {
  flex: 1;
  min-width: 0;
  padding-left: 16px;
}

.archive-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.archive-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* 按月分组 */
.archive-month-group {
  margin-bottom: 24px;
  scroll-margin-top: 80px;
}

.archive-month-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 4px;
}

.archive-month-count {
  font-size: var(--text-sm);
  font-weight: 400;
  color: var(--color-ink-muted);
}

/* 条目 */
.archive-item {
  display: flex;
  align-items: baseline;
  gap: 12px;
  padding: 10px 16px;
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.archive-item--clickable {
  cursor: pointer;
}

.archive-item:hover {
  background: var(--color-surface-dim);
}

.archive-item-date {
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
}

.archive-item-title {
  font-size: var(--text-base);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  transition: color var(--transition-fast);
}

.archive-item:hover .archive-item-title {
  color: var(--color-primary);
}

/* 小记内容图标 */
.note-content-icon {
  flex-shrink: 0;
  color: var(--color-ink-faint);
}

/* 抽屉 */
.drawer-detail-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.drawer-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

/* 移动端 */
@media (max-width: 768px) {
  .archive-page {
    flex-direction: column;
    gap: 12px;
    padding-top: 0;
  }

  .archive-sidebar {
    width: 100%;
    position: static;
    flex-direction: row;
    overflow-x: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
    border-left: none;
    padding-left: 0;
    margin-left: 0;
    gap: 4px;
  }

  .archive-sidebar::-webkit-scrollbar {
    display: none;
  }

  .archive-year-btn {
    flex-shrink: 0;
    padding: 8px 14px;
  }

  .archive-year-btn::before {
    display: none;
  }
}

/* 覆盖布局移动端 padding-top */
@media (max-width: 768px) {
  body.page-archive .home-main {
    padding-top: 80px !important;
  }
}
</style>
