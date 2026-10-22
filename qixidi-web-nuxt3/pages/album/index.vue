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
            @click="filterTo(yearGroup.year)"
          >
            {{ yearGroup.year }}年
            <span class="album-month-count">{{ yearGroup.total }}</span>
          </button>
        </div>
      </template>
    </aside>

    <!-- 中间瀑布流内容（连续流，无月份分割） -->
    <div class="album-main">
      <div v-if="listLoading && !albumList.length" class="album-loading">
        <n-spin size="large" />
      </div>
      <CommonEmptyState v-else-if="!albumList.length" description="暂无图片" />
      <div v-else class="album-masonry">
        <div v-for="(col, colIndex) in distributedColumns" :key="colIndex" class="album-col">
          <div
            v-for="item in col"
            :key="item.id"
            class="album-item"
            :data-month="monthOf(item)"
            :data-month-first="monthFirstIds.has(item.id) ? '' : undefined"
          >
            <!-- 点击图片打开 ImagePreview 大图预览（与文章详情同款）；容器裁剪 hover 放大 -->
            <div class="album-img">
              <img
                :src="item.url"
                loading="lazy"
                :style="{ aspectRatio: 'auto 1' }"
                alt="相册图片"
                @load="onImgLoad(item, $event)"
                @click="openPreview(item)"
              >
            </div>
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
      <!-- 触底加载状态 -->
      <div v-if="albumList.length" class="album-load-status">
        <span v-if="listLoading">加载中...</span>
        <span v-else-if="!hasMore">— 没有更多了 —</span>
      </div>
    </div>

    <!-- 右侧月份目录：只显示当前浏览年份的有图月份，滚动跨年自动切换；点击筛选该月 -->
    <aside v-if="navMonths.length" class="album-month-nav">
      <button
        class="album-month-nav-item album-month-nav-all"
        :class="{ active: !filterValue }"
        @click="filterTo(null)"
      >
        全部
        <span class="album-month-nav-count">{{ totalCount }}</span>
      </button>
      <button
        v-for="stat in navMonths"
        :key="stat.month"
        class="album-month-nav-item"
        :class="{ active: activeMonth === stat.month }"
        @click="filterTo(stat.month)"
      >
        {{ formatMonthTitle(stat.month) }}
        <span class="album-month-nav-count">{{ stat.count }}</span>
      </button>
    </aside>

    <!-- 图片大图预览（复用文章详情组件：滚轮缩放 · 拖拽移动 · 双击重置） -->
    <ImagePreview
      :visible="previewVisible"
      :image-src="previewSrc"
      alt="相册图片"
      @close="previewVisible = false"
    />

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

// ==================== 图片大图预览（复用文章详情 ImagePreview） ====================
const previewVisible = ref(false)
const previewSrc = ref('')

const openPreview = (item: AlbumItem) => {
  previewSrc.value = item.url
  previewVisible.value = true
}

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
  calcColumnCount()
  const handleResize = () => {
    isMobile.value = window.innerWidth < 768
    calcColumnCount()
  }
  window.addEventListener('resize', handleResize)
  onUnmounted(() => window.removeEventListener('resize', handleResize))
})

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
  if (reset) assignedColMap.clear() // 重置场景清空分配锁，新数据全量重排
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

// ==================== 组内瀑布流分列（贪心放最矮列） ====================
// 稳定性靠分配锁：item 一旦分到某列就锁定，触底追加、图片加载更新高度时旧图永不换列
// 均衡性靠真实比例：图片 @load 记录宽高比，新图按真实列高进最矮列，底部差距随翻页收敛
// （CSS columns 是列优先填充且追加会全量重平衡，顺序乱、旧图跳位，故弃用）

// item.id -> 图片宽高比（w/h），未加载的图按 1:1 占位估算
const ratioMap = reactive(new Map<string, number>())

// item.id -> 已分配列（分配锁；筛选重置/列数变化时清空，全量重排）
const assignedColMap = reactive(new Map<string, number>())

// 响应式列数：大屏 4 / 中屏 3 / 手机 2
const columnCount = ref(3)

const calcColumnCount = () => {
  const width = window.innerWidth
  const next = width >= 1100 ? 4 : width >= 769 ? 3 : 2
  if (next !== columnCount.value) {
    columnCount.value = next
    assignedColMap.clear()
  }
}

// 单卡高度估算：列宽归一化（1/ratio）+ meta 行近似（约 0.2 列宽）
const estimateHeight = (item: AlbumItem) => 1 / (ratioMap.get(item.id) || 1) + 0.2

// 图片加载后记录真实宽高比，让后续新图的分配越来越准（已分配列不动）
const onImgLoad = (item: AlbumItem, e: Event) => {
  const img = e.target as HTMLImageElement
  if (img.naturalWidth && img.naturalHeight) {
    ratioMap.set(item.id, img.naturalWidth / img.naturalHeight)
  }
}

// 连续瀑布流：全部图片贪心分列（分配锁 + 真实比例，触底追加不重排旧图）
const distributedColumns = computed(() => {
  const n = columnCount.value
  const columns: AlbumItem[][] = Array.from({ length: n }, () => [])
  const heights = new Array(n).fill(0)
  albumList.value.forEach((item) => {
    const locked = assignedColMap.get(item.id)
    let target: number
    if (locked !== undefined && locked < n) {
      target = locked
    } else {
      // 新图进当前最矮的列（高度并列时靠左，视觉顺序大致行优先）
      target = 0
      for (let i = 1; i < n; i++) {
        if (heights[i] < heights[target]) target = i
      }
      assignedColMap.set(item.id, target)
    }
    columns[target].push(item)
    heights[target] += estimateHeight(item)
  })
  return columns
})

// 每月首图 id（时间倒序遍历中每月出现的第一张），渲染时打 data-month-first 标记供滚动高亮定位
const monthFirstIds = computed(() => {
  const ids = new Set<string>()
  let lastMonth = ''
  albumList.value.forEach((item) => {
    const m = item.sourceTime?.substring(0, 7) || ''
    if (m && m !== lastMonth) {
      ids.add(item.id)
      lastMonth = m
    }
  })
  return ids
})

const monthOf = (item: AlbumItem) => item.sourceTime?.substring(0, 7) || ''

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

// 右侧月份目录：只显示当前浏览年份的有图月份（滚动跨年自动切换，月份重置到新年第一个有图月）
const navYear = computed(() =>
  activeMonth.value?.split('-')[0] || monthStats.value[0]?.month.split('-')[0] || '')
const navMonths = computed(() =>
  monthStats.value.filter(s => s.month.startsWith(navYear.value)))

const activeMonth = ref<string | null>(null)

// 高亮当前月份（左侧年份目录与右侧月份目录据此联动）
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
    // 年筛选：月份目录切到该年第一个有图月
    const first = monthStats.value.find(s => s.month.startsWith(value))
    if (first) setActiveMonth(first.month)
  }
  loadAlbum(true).then(() => {
    window.scrollTo(0, 0)
    handleScroll()  // 该范围不满一屏时兜底触发加载
  })
}

// 滚动：高亮当前月份（连续流按月首图标记定位，跨年联动年份目录与右侧月份目录）+ 触底加载更多
const handleScroll = () => {
  // 分列后 DOM 是列优先序 ≠ 时间序，不能取"最后一个过线的"；
  // 时间倒序流中当前浏览位置 = 过线标记里时间最早的月份。
  // 判定线取视口中线：滚动触发懒加载、图片完成撑高布局会让首图位置漂移，顶部线会误判
  const marks = document.querySelectorAll<HTMLElement>('[data-month-first]')
  let current: string | null = null
  marks.forEach((el) => {
    const m = el.getAttribute('data-month')
    if (el.getBoundingClientRect().top <= window.innerHeight / 2 && (!current || m < current)) current = m
  })
  if (current) setActiveMonth(current)

  // 距底部 700px 内加载下一页（窗口须覆盖懒加载图片完成后撑高的幅度，快速滚底时才不会错过触发）
  if (!listLoading.value && hasMore.value
      && window.scrollY + window.innerHeight >= document.documentElement.scrollHeight - 700) {
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

// 月份标签（右栏目录只出 "10月"，年份由年份目录/右栏年份标识展示）
const formatMonthTitle = (month: string) => {
  if (!month) return ''
  return `${Number(month.split('-')[1])}月`
}
</script>

<style scoped>
.album-page {
  display: flex;
  gap: 24px;
  padding-top: var(--space-8);
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

/* 右侧月份目录：只显示当前浏览年份的有图月份，滚动跨年自动切换；点击筛选该月 */
.album-month-nav {
  width: 110px;
  flex-shrink: 0;
  position: sticky;
  top: 78px;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-left: 12px;
  border-left: 2px solid var(--color-border-light);
}

/* 全部入口：置顶加粗，底部分隔；筛选月份后由此切回全部浏览 */
.album-month-nav-all {
  font-weight: 600;
  color: var(--color-ink);
  padding-bottom: var(--space-2);
  margin-bottom: var(--space-1);
  border-bottom: 1px solid var(--color-border-light);
}

.album-month-nav-all.active {
  color: var(--color-primary);
}

.album-month-nav-item {
  display: flex;
  align-items: baseline;
  gap: 4px;
  padding: 6px 2px 6px 0;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  transition: all var(--transition-fast);
  text-align: left;
}

.album-month-nav-item:hover {
  color: var(--color-ink);
}

.album-month-nav-item.active {
  color: var(--color-primary);
  font-weight: 600;
}

.album-month-nav-count {
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

/* 连续瀑布流：JS 贪心分列（列数随断点由 distributedColumns 计算），触底追加不重排旧图 */
.album-masonry {
  display: flex;
  gap: var(--space-4);
  align-items: flex-start;
}

.album-col {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.album-item {
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--color-surface);
}

/* 图片容器：裁剪悬停放大，防止向下延伸盖住 meta 行（卡片 overflow:hidden 裁不到容器内兄弟元素） */
.album-img {
  overflow: hidden;
}

/* 图片按列宽等比缩放，加载前以方形占位防跳高（有内在尺寸用内在比例，否则 1:1） */
.album-img img {
  display: block;
  width: 100%;
  height: auto;
  min-height: 60px;
  cursor: zoom-in;
  transition: transform var(--transition-slow);
}

/* 悬停放大（在容器内裁剪，不溢出、不遮挡 meta） */
.album-item:hover .album-img img {
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

  /* 移动端月份目录变横向胶囊条：排在年份条之后、瀑布流之前（DOM 顺序 sidebar→main→nav，用 order 调整） */
  .album-month-nav {
    order: 1;
    width: 100%;
    position: static;
    flex-direction: row;
    gap: var(--space-2);
    overflow-x: auto;
    scrollbar-width: none;
    border-left: none;
    padding-left: 0;
    /* 胶囊均不收缩，必须解除 flex 子项 min-width:auto，否则把父链撑出横向滚动 */
    min-width: 0;
  }

  .album-month-nav::-webkit-scrollbar {
    display: none;
  }

  .album-month-nav-item {
    flex: none;
    justify-content: center;
    padding: 4px 12px;
    background: var(--color-surface-dim);
    border-radius: var(--radius-full);
    white-space: nowrap;
  }

  .album-month-nav-item.active {
    background: var(--color-primary-light);
  }

  /* "全部"在横向胶囊条中与其他月份同形，去掉桌面端的置顶分隔 */
  .album-month-nav-all {
    border-bottom: none;
    margin-bottom: 0;
  }

  .album-main {
    order: 2;
  }

  .album-masonry {
    gap: 10px;
  }

  .album-col {
    gap: 10px;
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
