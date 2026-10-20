<template>
  <div class="album-page">
    <!-- 左侧年份目录（时间线，独立滚动） -->
    <aside class="album-sidebar">
      <div v-if="monthsLoading" class="album-loading">
        <n-spin size="small" />
      </div>
      <template v-else>
        <div class="album-year-row">
          <button class="album-year-btn" :class="{ active: !filterValue }" @click="filterTo(null)">
            全部
            <span class="album-month-count">{{ totalCount }}</span>
          </button>
        </div>
        <div v-for="yearGroup in yearGroups" :key="yearGroup.year" class="album-year-row">
          <button
            class="album-year-btn"
            :class="{ active: activeMonth?.startsWith(yearGroup.year) }"
            @click="handleYearClick(yearGroup.year)"
          >
            {{ yearGroup.year }}年
            <span class="album-month-count">{{ yearGroup.total }}</span>
          </button>
        </div>
      </template>
    </aside>

    <!-- 右侧瀑布流内容（按月分组） -->
    <div class="album-main">
      <!-- 吸顶月份条：跟随当前浏览年份，与左侧年份目录同步滚动/切换 -->
      <div v-if="barMonths.length" ref="monthBarRef" class="album-month-bar" :class="{ stuck: barStuck }">
        <span class="album-month-bar-year">{{ barYear }}年</span>
        <div class="album-month-bar-list">
          <button
            v-for="stat in barMonths"
            :key="stat.month"
            class="album-month-bar-item"
            :class="{ active: activeMonth === stat.month }"
            @click="handleMonthClick(stat.month)"
          >
            {{ formatMonthTitle(stat.month) }}
            <span class="album-month-bar-count">{{ stat.count }}</span>
          </button>
        </div>
      </div>
      <div v-if="listLoading && !albumList.length" class="album-loading">
        <n-spin size="large" />
      </div>
      <CommonEmptyState v-else-if="!albumList.length" description="暂无图片" />
      <template v-else>
        <div
          v-for="group in monthGroups"
          :key="group.month"
          :ref="(el: any) => setMonthRef(group.month, el)"
          class="album-month-group"
        >
          <div class="album-month-header">
            <span>{{ formatMonthTitle(group.month) }}</span>
            <span class="album-month-sum">「{{ group.items.length }}张」</span>
          </div>
          <div class="album-masonry">
            <div v-for="item in group.items" :key="item.id" class="album-item">
              <!-- 点击图片放大预览 -->
              <n-image
                :src="item.url"
                :img-props="{ loading: 'lazy' }"
                object-fit="cover"
                class="album-img"
              />
              <div class="album-meta">
                <button
                  v-if="item.source === 1"
                  class="album-note-link"
                  @click="handleNoteClick(item)"
                >
                  查看小记
                </button>
                <span class="album-time">{{ item.sourceTime }}</span>
              </div>
            </div>
          </div>
        </div>
      </template>
      <!-- 触底加载状态 -->
      <div v-if="albumList.length" class="album-load-status">
        <span v-if="listLoading">加载中...</span>
        <span v-else-if="!hasMore">— 没有更多了 —</span>
      </div>
    </div>

    <!-- 小记详情抽屉（PC；移动端直接跳详情页） -->
    <n-drawer v-model:show="drawerVisible" :width="800" placement="right">
      <n-drawer-content title="小记详情" :closable="true" :native-scrollbar="false">
        <div class="album-drawer-wrapper">
          <div v-if="drawerLoading" class="album-drawer-loading">
            <n-spin size="medium" />
          </div>
          <TimeNotesDetail v-else-if="drawerDetail" :detail="drawerDetail" @edit="handleNoteEdit" />
          <div v-else class="album-drawer-loading">加载失败</div>
        </div>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesInfo } from '~/types'
import type { AlbumItem, AlbumMonthStat } from '~/composables/useAlbumApi'

definePageMeta({
  sidebar: false,
  showTabBar: false
})

useHead({
  title: '相册',
  bodyAttrs: { class: 'page-album' }
})

const albumApi = useAlbumApi()
const timeNotesApi = useTimeNotesApi()
const router = useRouter()
const route = useRoute()

// ==================== 小记详情抽屉 ====================
// 移动端检测
const isMobile = ref(false)

const drawerVisible = ref(false)
const drawerDetail = ref<TimeNotesInfo | null>(null)
const drawerLoading = ref(false)

// PC 开抽屉查看，移动端直接跳详情页
const handleNoteClick = async (item: AlbumItem) => {
  if (isMobile.value) {
    navigateTo(`/time-notes/${item.sourceId}`)
    return
  }
  drawerDetail.value = null
  drawerVisible.value = true
  drawerLoading.value = true
  try {
    drawerDetail.value = await timeNotesApi.getTimeNotesDetail(item.sourceId)
  } catch {
    // useApi 统一处理错误
  } finally {
    drawerLoading.value = false
  }
}

const handleNoteEdit = (id: number) => {
  drawerVisible.value = false
  router.push(`/write/note/${id}`)
}

onMounted(() => {
  isMobile.value = window.innerWidth < 768
  const handleResize = () => {
    isMobile.value = window.innerWidth < 768
  }
  window.addEventListener('resize', handleResize)
  onUnmounted(() => window.removeEventListener('resize', handleResize))
})

interface AlbumMonthGroup {
  month: string
  items: AlbumItem[]
}

interface AlbumYearGroup {
  year: string
  total: number
  months: AlbumMonthStat[]
}

// ==================== 图片列表（分页 + 触底加载） ====================
const PAGE_SIZE = 20

// 时间筛选：null=全部 / yyyy-MM=单月 / yyyy=单年（点远月只拉该范围图片，省流量）
const filterValue = ref<string | null>(null)

const albumList = ref<AlbumItem[]>([])
const pageNum = ref(1)
const total = ref(0)
const listLoading = ref(false)
const hasMore = computed(() => albumList.value.length < total.value)

// reset=true 重置到第 1 页（首屏），否则追加下一页
const loadAlbum = async (reset = false) => {
  if (listLoading.value) return false
  listLoading.value = true
  try {
    const current = reset ? 1 : pageNum.value
    const result = await albumApi.getAlbumList({
      pageNum: current,
      pageSize: PAGE_SIZE,
      month: filterValue.value || undefined
    })
    const rows = result.rows || []
    albumList.value = reset ? rows : [...albumList.value, ...rows]
    total.value = result.total || 0
    pageNum.value = current + 1
    return true
  } catch {
    return false
  } finally {
    listLoading.value = false
  }
}

// 已加载图片按 yyyy-MM 分组（渐进构建：同月跨页追加，新月新建分组）
const monthGroups = computed<AlbumMonthGroup[]>(() => {
  const map = new Map<string, AlbumItem[]>()
  albumList.value.forEach((item) => {
    if (!item.sourceTime) return
    const month = item.sourceTime.substring(0, 7)
    const list = map.get(month) || []
    list.push(item)
    map.set(month, list)
  })
  return Array.from(map.entries())
    .sort(([a], [b]) => b.localeCompare(a))
    .map(([month, items]) => ({ month, items }))
})

// ==================== 月份目录（统计接口独立加载，不依赖图片分页） ====================
const monthStats = ref<AlbumMonthStat[]>([])
const monthsLoading = ref(true)

// 按年聚合（年份/月份均倒序，张数来自统计）
const yearGroups = computed<AlbumYearGroup[]>(() => {
  const map = new Map<string, AlbumMonthStat[]>()
  monthStats.value.forEach((stat) => {
    const year = stat.month.split('-')[0]
    const list = map.get(year) || []
    list.push(stat)
    map.set(year, list)
  })
  return Array.from(map.entries())
    .sort(([a], [b]) => b.localeCompare(a))
    .map(([year, months]) => ({ year, months, total: months.reduce((n, m) => n + m.count, 0) }))
})

// 全部图片总张数（「全部」目录项角标）
const totalCount = computed(() => monthStats.value.reduce((n, s) => n + s.count, 0))

// 右侧月份条：跟随当前浏览年份（滚动跨年自动切换显示该年月份）
const barYear = computed(() =>
  activeMonth.value?.split('-')[0] || monthStats.value[0]?.month.split('-')[0] || '')
const barMonths = computed(() =>
  monthStats.value.filter(s => s.month.startsWith(barYear.value)))

const monthRefs = ref<Record<string, HTMLElement>>({})
const activeMonth = ref<string | null>(null)

// 月份条是否吸顶：贴合导航栏后去圆角边框，避免图片从圆角缺口漏出
const monthBarRef = ref<HTMLElement | null>(null)
const barStuck = ref(false)

const setMonthRef = (month: string, el: any) => {
  // el 为 null 表示该分组已卸载（筛选切换重置列表），清掉悬空引用避免锚点滚动失效
  if (el) {
    monthRefs.value[month] = el as HTMLElement
  } else {
    delete monthRefs.value[month]
  }
}

// 高亮当前月份（左侧年份目录与右侧月份条据此联动）
const setActiveMonth = (month: string) => {
  activeMonth.value = month
}

// 切换筛选：null=全部 / yyyy-MM=单月 / yyyy=单年，只请求该范围图片；同步 URL 可分享可刷新
const filterTo = (value: string | null) => {
  if (filterValue.value === value) return
  filterValue.value = value
  router.replace({ query: value ? { month: value } : {} })
  if (value?.includes('-')) {
    setActiveMonth(value)
  } else if (value) {
    // 年筛选：月份条切到该年第一个月
    const first = monthStats.value.find(s => s.month.startsWith(value))
    if (first) setActiveMonth(first.month)
  }
  loadAlbum(true).then(() => {
    window.scrollTo(0, 0)
    handleScroll()  // 该范围不满一屏时兜底触发加载
  })
}

// 月份点击：全部模式下已加载的月份锚点滚动，否则切换到该月筛选
const handleMonthClick = (month: string) => {
  const el = !filterValue.value ? monthRefs.value[month] : null
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
    setActiveMonth(month)
    return
  }
  filterTo(month)
}

// 年份点击：全部模式下已加载的定位到该年第一个月，否则切换到该年筛选
const handleYearClick = (year: string) => {
  if (!filterValue.value) {
    const loaded = monthGroups.value.find(g => g.month.startsWith(year))
    const el = loaded ? monthRefs.value[loaded.month] : null
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' })
      setActiveMonth(loaded.month)
      return
    }
  }
  filterTo(year)
}

// 滚动：高亮当前月份 + 触底加载更多
const handleScroll = () => {
  if (monthBarRef.value) {
    // 吸顶位 66px，留 1px 容差
    barStuck.value = monthBarRef.value.getBoundingClientRect().top <= 67
  }
  const groups = monthGroups.value
  let current = groups[0]?.month || null
  for (let i = groups.length - 1; i >= 0; i--) {
    const el = monthRefs.value[groups[i].month]
    if (el && el.getBoundingClientRect().top <= 160) {
      current = groups[i].month
      break
    }
  }
  if (current) setActiveMonth(current)

  // 距底部 200px 内加载下一页
  if (!listLoading.value && hasMore.value
      && window.scrollY + window.innerHeight >= document.documentElement.scrollHeight - 200) {
    loadAlbum()
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  onUnmounted(() => window.removeEventListener('scroll', handleScroll))
})

// 首屏：目录统计与图片第一页并行加载；筛选状态从 URL 恢复
onMounted(async () => {
  filterValue.value = typeof route.query.month === 'string' ? route.query.month : null
  loadAlbum(true).then(() => handleScroll())  // 首页不满一屏时兜底触发加载
  try {
    monthStats.value = (await albumApi.getAlbumMonths()) || []
    if (monthStats.value[0]) setActiveMonth(monthStats.value[0].month)
  } catch {
    // useApi 统一处理错误
  } finally {
    monthsLoading.value = false
  }
})

// 月份标签（月份条与分组头只出 "10月"，年份由年份目录/月份条年份标识展示）
const formatMonthTitle = (month: string) => {
  if (!month) return ''
  return `${Number(month.split('-')[1])}月`
}
</script>

<style scoped>
.album-page {
  display: flex;
  gap: 24px;
  padding-top: 20px;
  align-items: flex-start;
}

/* 左侧年份目录 - 时间线（独立滚动） */
.album-sidebar {
  width: 130px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 78px;
  max-height: calc(100vh - 98px);
  overflow-y: auto;
  padding-left: 12px;
  border-left: 2px solid var(--color-border-light);
  margin-left: 8px;
}

/* 细滚动条 */
.album-sidebar::-webkit-scrollbar {
  width: 4px;
}

.album-sidebar::-webkit-scrollbar-track {
  background: transparent;
}

.album-sidebar::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.album-sidebar::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

/* 年份行（时间线节点） */
.album-year-row {
  display: flex;
  align-items: center;
  position: relative;
}

.album-year-row::before {
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

.album-year-btn {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 2px 10px 0;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  font-weight: 500;
}

.album-year-btn:hover {
  color: var(--color-ink);
}

.album-year-row:hover::before {
  background: var(--color-ink-muted);
}

.album-year-btn.active {
  color: var(--color-primary);
  font-weight: 600;
}

.album-year-row:has(.album-year-btn.active)::before {
  background: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 右侧吸顶月份条（跟随当前年份，与左侧目录同步）；top=导航栏高度 66px，紧贴无缝 */
.album-month-bar {
  position: sticky;
  top: 66px;
  z-index: 5;
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-2) var(--space-3);
  margin-bottom: var(--space-4);
  background: var(--nav-bar-bg);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  overflow-x: auto;
  scrollbar-width: none;
}

.album-month-bar::-webkit-scrollbar {
  display: none;
}

/* 吸顶态：去圆角边框满宽贴合导航栏，只留底部分隔线 */
.album-month-bar.stuck {
  border: none;
  border-bottom: 1px solid var(--color-border-light);
  border-radius: 0;
}

.album-month-bar-year {
  flex-shrink: 0;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
}

.album-month-bar-list {
  display: flex;
  gap: var(--space-2);
  /* 胶囊均不收缩，必须解除 flex 子项 min-width:auto，否则把父链撑出横向滚动 */
  min-width: 0;
}

.album-month-bar-item {
  display: flex;
  align-items: baseline;
  gap: 4px;
  padding: 4px 12px;
  border: none;
  background: var(--color-surface-dim);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  color: var(--color-ink-light);
  cursor: pointer;
  white-space: nowrap;
  flex-shrink: 0;
  transition: all var(--transition-fast);
}

.album-month-bar-item:hover {
  color: var(--color-ink);
}

.album-month-bar-item.active {
  background: var(--color-primary-light);
  color: var(--color-primary);
  font-weight: 600;
}

.album-month-bar-count {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.album-month-count {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

/* 右侧内容 */
.album-main {
  flex: 1;
  min-width: 0;
  /* 防月份条等不收缩内容在移动端撑出页面横向滚动 */
  max-width: 100%;
  overflow-x: clip;
  padding-left: 16px;
}

.album-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* 触底加载状态条 */
.album-load-status {
  display: flex;
  justify-content: center;
  padding: var(--space-4) 0;
  font-size: var(--text-sm);
  color: var(--color-ink-faint);
}

.album-month-group {
  margin-bottom: 24px;
  /* 停在吸顶月份条下方（66 条吸顶位 + 约 44 条高 + 间隙） */
  scroll-margin-top: 118px;
}

.album-month-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: var(--space-4);
}

.album-month-sum {
  font-size: var(--text-sm);
  font-weight: 400;
  color: var(--color-ink-muted);
}

/* 组内瀑布流：大屏 4 列，小屏 3 列（手机 2 列在移动端区块） */
.album-masonry {
  columns: 3;
  column-gap: var(--space-4);
}

@media (min-width: 1100px) {
  .album-masonry {
    columns: 4;
  }
}

.album-item {
  break-inside: avoid;
  margin-bottom: var(--space-4);
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--color-surface);
}

.album-img {
  display: block;
  width: 100%;
  min-height: 60px;
  cursor: zoom-in;
  transition: transform var(--transition-slow);
}

/* 悬停放大（卡片 overflow:hidden，不溢出） */
.album-item:hover .album-img {
  transform: scale(1.05);
}

.album-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
}

.album-time {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  /* 无小记按钮（来源非小记）时仍保持右侧 */
  margin-left: auto;
  /* 日期不压缩断行 */
  white-space: nowrap;
}

.album-note-link {
  font-size: var(--text-xs);
  color: var(--color-primary);
  text-decoration: none;
  flex-shrink: 0;
  border: none;
  background: none;
  padding: 0;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.album-note-link:hover {
  color: var(--color-primary-hover);
}

/* 小记详情抽屉 */
.album-drawer-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.album-drawer-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

/* 移动端：目录横向滚动，瀑布流两列 */
@media (max-width: 768px) {
  .album-page {
    flex-direction: column;
    gap: 12px;
    padding-top: 0;
  }

  .album-sidebar {
    width: 100%;
    position: static;
    max-height: none;
    flex-direction: row;
    overflow-x: auto;
    overflow-y: hidden;
    scrollbar-width: none;
    -ms-overflow-style: none;
    border-left: none;
    padding-left: 0;
    margin-left: 0;
    gap: 4px;
  }

  .album-sidebar::-webkit-scrollbar {
    display: none;
  }

  .album-year-row::before {
    display: none;
  }

  .album-year-btn {
    flex: none;
    padding: 8px 14px;
  }

  /* 移动端月份条紧贴 fixed 导航栏（高 56px）底边 */
  .album-month-bar {
    top: 56px;
  }

  .album-main {
    padding-left: 0;
  }

  .album-masonry {
    columns: 2;
    column-gap: 10px;
  }

  .album-item {
    margin-bottom: 10px;
  }

  /* 窄列下放不下时按钮/时间整体换行，避免挤压断行 */
  .album-meta {
    padding: var(--space-1) var(--space-2);
    gap: var(--space-1);
    flex-wrap: wrap;
  }
}
</style>

<!-- 非 scoped 样式：覆盖布局的移动端默认间距 -->
<style>
@media (max-width: 768px) {
  body.page-album .home-main {
    padding-top: 80px !important;
  }
}
</style>
