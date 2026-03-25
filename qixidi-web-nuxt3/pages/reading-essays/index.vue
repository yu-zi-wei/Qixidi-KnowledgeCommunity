<template>
  <div class="reading-essays-page">
    <!-- 分类导航栏 - sticky + 滚动显示背景 -->
    <nav ref="navRef" class="category-nav" :class="{ 'is-sticky': isNavSticky }">
      <NuxtLink
        to="/reading-essays"
        class="nav-item"
        :class="{ active: !selectedGroupId }"
      >
        全部
      </NuxtLink>
      <NuxtLink
        v-for="group in groups"
        :key="group.id"
        :to="`/reading-essays?groupId=${group.id}`"
        class="nav-item"
        :class="{ active: selectedGroupId === group.id }"
      >
        {{ group.name }}
      </NuxtLink>
    </nav>

    <!-- 当前筛选状态（排除分类，因为已在导航栏显示） -->
    <div v-if="hasActiveFilterWithoutGroup" class="filter-status">
      <span class="filter-label">当前筛选：</span>
      <span v-if="selectedAlbumName" class="filter-tag">{{ selectedAlbumName }}</span>
      <span v-if="selectedLabel" class="filter-tag"># {{ selectedLabel }}</span>
      <span v-if="selectedAuthor" class="filter-tag">{{ selectedAuthor }}</span>
      <button class="btn-clear" @click="clearFilters">清除</button>
    </div>

    <!-- 瀑布流内容 -->
    <ReadingEssaysWaterfall
      :reading-essays="readingEssays"
      :loading="loading"
      :has-more="hasMore"
      @collect="handleCollect"
      @show-detail="handleShowDetail"
    />

    <!-- 详情抽屉 -->
    <n-drawer v-model:show="drawerVisible" :width="800" placement="right">
      <n-drawer-content title="随笔详情" closable :native-scrollbar="false">
        <template #header>
          <div class="drawer-header">
            <span class="drawer-title">随笔详情</span>
            <div class="drawer-actions">
              <n-button quaternary size="small" @click="openInNewTab" title="新标签打开">
                <template #icon>
                  <n-icon><ExternalLink /></n-icon>
                </template>
              </n-button>
              <n-button quaternary size="small" @click="copyShareLink" title="复制链接">
                <template #icon>
                  <n-icon><Share /></n-icon>
                </template>
              </n-button>
            </div>
          </div>
        </template>
        <ReadingEssaysDetailContent v-if="selectedEssay" :essay="selectedEssay" />
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ExternalLink, Share } from '@vicons/tabler'
import type { ReadingEssaysInfo, ReadingEssaysLabel } from '~/types'

definePageMeta({
  showTabBar: false,
  sidebar: 'readingEssays'
})

// 给 body 添加页面特定的 class，用于覆盖布局样式
useHead({
  bodyAttrs: {
    class: 'page-reading-essays'
  }
})

const readingEssaysApi = useReadingEssaysApi()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const message = useMessage()
const route = useRoute()

// 导航栏吸顶状态
const navRef = ref<HTMLElement | null>(null)
const isNavSticky = ref(false)

// 侧边栏数据（与 ReadingEssaysSidebar 共享）
const sidebarData = useState('reading-essays-sidebar-data', () => ({
  groups: [],
  albums: [],
  popularAuthors: [],
  popularLabels: [] as ReadingEssaysLabel[],
  selectedAlbumId: null as number | null,
  selectedLabel: null as string | null,
  selectedAuthor: null as string | null,
  selectedAlbumName: '' as string
}))

// 随笔列表数据
const readingEssays = ref<ReadingEssaysInfo[]>([])
const pageNum = ref(1)
const pageSize = 20
const total = ref(0)
const loading = ref(false)
const hasMore = computed(() => readingEssays.value.length < total.value)

// 分类数据
const groups = computed(() => sidebarData.value.groups)
const selectedGroupId = computed(() => route.query.groupId ? Number(route.query.groupId) : null)

// 当前筛选名称
const selectedGroupName = computed(() => {
  if (!selectedGroupId.value) return ''
  const group = (sidebarData.value.groups as any[]).find((g: any) => g.id === selectedGroupId.value)
  return group?.name || ''
})

const selectedAlbumName = computed(() => sidebarData.value.selectedAlbumName || '')
const selectedLabel = computed(() => sidebarData.value.selectedLabel)
const selectedAuthor = computed(() => sidebarData.value.selectedAuthor)

// 是否有除分类外的筛选条件
const hasActiveFilterWithoutGroup = computed(() => {
  return sidebarData.value.selectedAlbumId || sidebarData.value.selectedLabel || sidebarData.value.selectedAuthor
})

// 是否有筛选条件（包括分类）
const hasActiveFilter = computed(() => {
  return selectedGroupId.value || sidebarData.value.selectedAlbumId || sidebarData.value.selectedLabel || sidebarData.value.selectedAuthor
})

// 加载侧边栏数据
const loadSidebarData = async () => {
  try {
    const [groupsData, albumsData, authorsData, labelsData] = await Promise.all([
      readingEssaysApi.getReadingEssaysGroups({ pageNum: 1, pageSize: 100 }),
      readingEssaysApi.getRecommendedAlbums({ pageNum: 1, pageSize: 5 }),
      readingEssaysApi.getPopularAuthors(),
      readingEssaysApi.getPopularLabels()
    ])

    sidebarData.value.groups = groupsData.rows || []
    sidebarData.value.albums = albumsData.rows || []
    sidebarData.value.totalAlbums = albumsData.total || 0
    sidebarData.value.popularAuthors = authorsData || []
    sidebarData.value.popularLabels = labelsData || []
  } catch (error) {
    console.error('加载筛选数据失败:', error)
  }
}

// 加载随笔列表
const loadReadingEssays = async (reset = false) => {
  if (loading.value) return

  loading.value = true
  try {
    const currentPage = reset ? 1 : pageNum.value
    const result = await readingEssaysApi.getReadingEssaysList({
      pageNum: currentPage,
      pageSize,
      groupId: selectedGroupId.value || undefined,
      albumId: sidebarData.value.selectedAlbumId || undefined,
      label: sidebarData.value.selectedLabel || undefined,
      author: sidebarData.value.selectedAuthor || undefined
    })

    if (reset) {
      readingEssays.value = result.rows || []
      pageNum.value = 2
    } else {
      readingEssays.value = [...readingEssays.value, ...(result.rows || [])]
      pageNum.value++
    }
    total.value = result.total || 0
  } catch (error) {
    console.error('加载随笔失败:', error)
    message.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

// 清除筛选
const clearFilters = () => {
  sidebarData.value.selectedAlbumId = null
  sidebarData.value.selectedLabel = null
  const query = { ...route.query }
  delete query.albumId
  delete query.label
  delete query.author
  navigateTo({ query }, { replace: true })
}

// 收藏随笔
const handleCollect = (id: number) => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }
  message.info('收藏功能开发中')
}

// 抽屉状态
const drawerVisible = ref(false)
const selectedEssay = ref<ReadingEssaysInfo | null>(null)

// 显示详情抽屉
const handleShowDetail = (item: ReadingEssaysInfo) => {
  selectedEssay.value = item
  drawerVisible.value = true
}

// 新标签打开详情页
const openInNewTab = () => {
  if (!selectedEssay.value) return
  const url = `/reading-essays/${selectedEssay.value.id}`
  window.open(url, '_blank')
}

// 复制分享链接
const copyShareLink = async () => {
  if (!selectedEssay.value) return
  const url = `${window.location.origin}/reading-essays/${selectedEssay.value.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}

// 监听导航栏吸顶状态
onMounted(() => {
  const checkSticky = () => {
    if (!navRef.value) return
    const rect = navRef.value.getBoundingClientRect()
    isNavSticky.value = rect.top <= 66
  }

  checkSticky()
  window.addEventListener('scroll', checkSticky, { passive: true })

  onUnmounted(() => {
    window.removeEventListener('scroll', checkSticky)
  })
})

// 监听 query 参数变化
watch(() => route.query, async (newQuery) => {
  const newAlbumId = newQuery.albumId ? Number(newQuery.albumId) : null

  sidebarData.value.selectedAlbumId = newAlbumId
  sidebarData.value.selectedLabel = (newQuery.label as string) || null
  sidebarData.value.selectedAuthor = (newQuery.author as string) || null

  if (newAlbumId) {
    try {
      const albumDetail = await readingEssaysApi.getAlbumDetail(newAlbumId)
      sidebarData.value.selectedAlbumName = albumDetail.name || ''
    } catch (error) {
      console.error('获取专辑详情失败:', error)
      sidebarData.value.selectedAlbumName = ''
    }
  } else {
    sidebarData.value.selectedAlbumName = ''
  }

  loadReadingEssays(true)
}, { deep: true })

// 初始化
onMounted(async () => {
  const queryAlbumId = route.query.albumId ? Number(route.query.albumId) : null
  const queryLabel = route.query.label as string || null
  const queryAuthor = route.query.author as string || null

  if (queryAlbumId) sidebarData.value.selectedAlbumId = queryAlbumId
  if (queryLabel) sidebarData.value.selectedLabel = queryLabel
  if (queryAuthor) sidebarData.value.selectedAuthor = queryAuthor

  if (queryAlbumId) {
    try {
      const albumDetail = await readingEssaysApi.getAlbumDetail(queryAlbumId)
      sidebarData.value.selectedAlbumName = albumDetail.name || ''
    } catch (error) {
      console.error('获取专辑详情失败:', error)
      sidebarData.value.selectedAlbumName = ''
    }
  }

  loadSidebarData()
  loadReadingEssays(true)

  // 无限滚动
  const handleScroll = () => {
    const scrollTop = window.scrollY
    const windowHeight = window.innerHeight
    const documentHeight = document.documentElement.scrollHeight

    if (scrollTop + windowHeight >= documentHeight - 200 && !loading.value && hasMore.value) {
      loadReadingEssays(false)
    }
  }

  window.addEventListener('scroll', handleScroll)

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
})
</script>

<style scoped>
.reading-essays-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ==================== 分类导航栏 ==================== */
.category-nav {
  position: sticky;
  top: 66px;
  z-index: 40;
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: var(--space-2);
  padding: 10px 16px;
  margin-top: 12px;
  margin-bottom: 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  background: transparent;
  border-radius: 32px;
  transition: all 0.3s ease;
}

.category-nav.is-sticky {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border-radius: 2px;
}

.dark .category-nav.is-sticky {
  background: rgba(13, 15, 17, 0.7);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.category-nav::-webkit-scrollbar {
  display: none;
}

.nav-item {
  flex-shrink: 0;
  padding: 7px 14px;
  font-size: 13px;
  color: var(--color-ink-light);
  text-decoration: none;
  border-radius: var(--radius-sm);
  background: transparent;
  transition: all var(--transition-fast);
  white-space: nowrap;
  font-weight: 400;
  line-height: 1.5;
}

.nav-item:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.nav-item.active {
  background: var(--color-surface-dim);
  color: var(--color-ink);
  font-weight: 500;
}

/* 移动端样式 */
@media (max-width: 768px) {
  .category-nav {
    position: fixed;
    top: 56px;
    left: 0;
    right: 0;
    z-index: 90;
    flex-wrap: wrap;
    padding: 10px 16px;
    margin-top: 0;
    margin-bottom: 0;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
  }

  .dark .category-nav {
    background: rgba(13, 15, 17, 0.7);
  }
}

/* 筛选状态 */
.filter-status {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 10px;
  font-size: 13px;
}

.dark .filter-status {
  background: rgba(30, 32, 35, 0.7);
  border-color: rgba(255, 255, 255, 0.06);
}

.filter-label {
  color: var(--color-ink-muted);
}

.filter-tag {
  padding: 5px 12px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.btn-clear {
  padding: 5px 12px;
  background: rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 20px;
  font-size: 12px;
  color: var(--color-ink-muted);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-clear:hover {
  background: var(--color-primary-light);
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.dark .btn-clear {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(255, 255, 255, 0.08);
}

/* ==================== 抽屉样式 ==================== */
.drawer-content {
  padding: 24px 32px;
}

.essay-content {
  font-size: 17px;
  line-height: 1.85;
  color: var(--color-ink);
  margin-bottom: 28px;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
}

.essay-source {
  font-size: var(--text-base);
  color: var(--color-ink-muted);
  font-style: italic;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
}

.essay-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 24px;
}

.meta-badge {
  font-size: var(--text-sm);
  padding: 5px 12px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: var(--radius-sm);
  color: var(--color-ink-light);
}

:global(.dark) .meta-badge {
  background: rgba(255, 255, 255, 0.08);
}

.meta-time {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.essay-labels {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 28px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--color-border-light);
}

.label-tag {
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 6px 14px;
  border-radius: var(--radius-sm);
}

.essay-stats {
  display: flex;
  gap: 24px;
  padding-top: 8px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: var(--text-base);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 18px;
  height: 18px;
}

/* 抽屉操作按钮 */
.drawer-actions {
  display: flex;
  gap: 4px;
}

.drawer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding-right: 32px;
}

.drawer-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-ink);
}
</style>

<!-- 非 scoped 样式 -->
<style>
@media (max-width: 768px) {
  body.page-reading-essays .home-main {
    padding-top: 170px !important;
  }
}
</style>
