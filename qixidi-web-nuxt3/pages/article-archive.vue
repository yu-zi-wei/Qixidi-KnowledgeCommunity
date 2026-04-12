<template>
  <div class="archive-page">
    <!-- 左侧年份目录 -->
    <aside class="archive-sidebar">
      <div v-if="pending" class="archive-loading">
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

    <!-- 右侧文章列表（全部展示） -->
    <div class="archive-main" ref="mainRef">
      <div v-if="pending" class="archive-loading">
        <n-spin size="large" />
      </div>
      <div v-else-if="!allGroups.length" class="archive-empty">
        <n-empty description="暂无归档" />
      </div>
      <template v-else>
        <div
          v-for="group in allGroups"
          :key="group.createTime"
          :ref="(el) => setYearRef(group.createTime, el)"
          class="archive-month-group"
        >
          <div class="archive-month-header">
            <span>{{ formatMonthTitle(group.createTime) }}</span>
            <span class="archive-month-count">「{{ group.list?.length || 0 }}篇」</span>
          </div>
          <NuxtLink
            v-for="article in group.list"
            :key="article.id"
            :to="`/articles/${article.id}`"
            class="archive-article-item"
            @click="saveScroll"
          >
            <span class="archive-article-date">{{ formatDate(article.createTime) }}</span>
            <span class="archive-article-title">{{ article.articleTitle }}</span>
          </NuxtLink>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
definePageMeta({ showTabBar: false })

const api = useApi()

interface ArchiveArticle {
  id: number
  articleTitle: string
  createTime: string
}

interface ArchiveGroup {
  createTime: string
  list: ArchiveArticle[]
}

const mainRef = ref<HTMLElement | null>(null)
const yearRefs = ref<Record<string, HTMLElement>>({})
const activeYear = ref<string | null>(null)

const setYearRef = (key: string, el: any) => {
  if (el) yearRefs.value[key] = el as HTMLElement
}

const { data: archiveData, pending } = await useAsyncData(
  'article-archive',
  () => api.getPage<ArchiveGroup>('/white/article/archive', { pageNum: 1, pageSize: -1 })
)

const allGroups = computed(() => archiveData.value?.rows || [])

// 提取所有年份
const yearList = computed(() => {
  const years = new Set<string>()
  allGroups.value.forEach((g) => {
    if (g.createTime) {
      const year = g.createTime.split('-')[0]
      years.add(year)
    }
  })
  return Array.from(years).sort((a, b) => b.localeCompare(a))
})

// 点击年份滚动到对应位置
const scrollToYear = (year: string) => {
  const target = allGroups.value.find((g) => g.createTime?.startsWith(year))
  if (!target) return
  const el = yearRefs.value[target.createTime]
  if (!el) return
  el.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

// 滚动时高亮当前可见年份
onMounted(() => {
  const handleScroll = () => {
    const groups = allGroups.value
    for (let i = groups.length - 1; i >= 0; i--) {
      const el = yearRefs.value[groups[i].createTime]
      if (!el) continue
      const rect = el.getBoundingClientRect()
      if (rect.top <= 160) {
        activeYear.value = groups[i].createTime.split('-')[0]
        return
      }
    }
    // 都没到，高亮第一个年份
    if (groups.length > 0) {
      activeYear.value = groups[0].createTime.split('-')[0]
    }
  }

  window.addEventListener('scroll', handleScroll, { passive: true })
  // 初始高亮
  if (yearList.value.length > 0) {
    activeYear.value = yearList.value[0]
  }

  // 返回时恢复滚动位置
  const savedScroll = window.history.state?.archiveScroll
  if (savedScroll) {
    nextTick(() => window.scrollTo({ top: savedScroll, behavior: 'instant' as ScrollBehavior }))
  }

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
})

// 跳转前保存滚动位置
const saveScroll = () => {
  history.replaceState({ ...(window.history.state || {}), archiveScroll: window.scrollY }, '')
}

// 格式化月份标题：2025-03 → 2025年03月
const formatMonthTitle = (dateStr: string) => {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length >= 2) return `${parts[0]}年${parts[1]}月`
  return dateStr
}

// 格式化文章日期：2025-03-28 → 03-28
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length >= 3) return `${parts[1]}-${parts[2]}`
  return dateStr
}

useHead({
  title: '文章归档',
  bodyAttrs: {
    class: 'page-article-archive'
  }
})
</script>

<style>
.archive-page {
  display: flex;
  gap: 24px;
  padding-top: 20px;
  align-items: flex-start;
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

/* 文章条目 */
.archive-article-item {
  display: flex;
  align-items: baseline;
  gap: 12px;
  padding: 10px 16px;
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.archive-article-item:hover {
  background: var(--color-surface-dim);
}

.archive-article-date {
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
}

.archive-article-title {
  font-size: var(--text-base);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  transition: color var(--transition-fast);
}

.archive-article-item:hover .archive-article-title {
  color: var(--color-primary);
}

/* 移动端 */
@media (max-width: 768px) {
  .archive-page {
    flex-direction: column;
    gap: 12px;
    padding-top: 0;
  }

  .archive-sidebar {
    display: none;
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
  body.page-article-archive .home-main {
    padding-top: 70px !important;
  }
}
</style>
