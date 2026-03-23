<template>
  <div class="reading-essays-page">
    <!-- 分类导航栏 - 直接 sticky -->
    <nav class="category-nav">
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
    />
  </div>
</template>

<script setup lang="ts">
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

// 侧边栏数据（与 ReadingEssaysSidebar 共享）
// groups 用于顶部导航，albums/popularAuthors/popularLabels 用于侧边栏
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

// 分类数据（用于顶部导航）
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

    // 分类数据用于顶部导航
    sidebarData.value.groups = groupsData.rows || []
    // 侧边栏数据
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
      pageNum.value = 2  // 下次加载第 2 页
    } else {
      readingEssays.value = [...readingEssays.value, ...(result.rows || [])]
      pageNum.value++  // 加载后递增
    }
    total.value = result.total || 0
  } catch (error) {
    console.error('加载随笔失败:', error)
    message.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

// 清除筛选（保留分类，通过导航栏切换）
const clearFilters = () => {
  sidebarData.value.selectedAlbumId = null
  sidebarData.value.selectedLabel = null
  // 清除作者筛选，保留 groupId
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

// 监听 query 参数变化（来自导航栏或侧边栏的操作）
watch(() => route.query, async (newQuery) => {
  console.log('route.query changed:', newQuery)

  // 从 query 更新侧边栏状态
  const newAlbumId = newQuery.albumId ? Number(newQuery.albumId) : null
  console.log('newAlbumId:', newAlbumId, 'oldAlbumId:', sidebarData.value.selectedAlbumId)

  sidebarData.value.selectedAlbumId = newAlbumId
  sidebarData.value.selectedLabel = (newQuery.label as string) || null
  sidebarData.value.selectedAuthor = (newQuery.author as string) || null

  // 如果有专辑ID，调用接口获取专辑名称
  if (newAlbumId) {
    console.log('Fetching album detail for id:', newAlbumId)
    try {
      const albumDetail = await readingEssaysApi.getAlbumDetail(newAlbumId)
      console.log('Album detail response:', albumDetail)
      sidebarData.value.selectedAlbumName = albumDetail.name || ''
      console.log('Updated selectedAlbumName:', sidebarData.value.selectedAlbumName)
    } catch (error) {
      console.error('获取专辑详情失败:', error)
      sidebarData.value.selectedAlbumName = ''
    }
  } else {
    sidebarData.value.selectedAlbumName = ''
  }

  // 重新加载随笔
  loadReadingEssays(true)
}, { deep: true })

// 监听侧边栏数据变化（仅在初始化时，确保数据已加载）
watch(() => sidebarData.value.groups, (newGroups) => {
  console.log('侧边栏数据已加载，groups 数量:', newGroups.length)
}, { immediate: false })

// 初始化
onMounted(async () => {
  console.log('=== onMounted 开始 ===')

  // 先读取 query 参数
  const queryAlbumId = route.query.albumId ? Number(route.query.albumId) : null
  const queryLabel = route.query.label as string || null
  const queryAuthor = route.query.author as string || null

  console.log('初始化 query 参数:', { queryAlbumId, queryLabel, queryAuthor })

  if (queryAlbumId) sidebarData.value.selectedAlbumId = queryAlbumId
  if (queryLabel) sidebarData.value.selectedLabel = queryLabel
  if (queryAuthor) sidebarData.value.selectedAuthor = queryAuthor

  // 如果有专辑ID，先获取专辑名称，再加载侧边栏数据（避免被覆盖）
  if (queryAlbumId) {
    try {
      console.log('初始化：获取专辑详情，ID:', queryAlbumId)
      const albumDetail = await readingEssaysApi.getAlbumDetail(queryAlbumId)
      console.log('专辑详情返回:', albumDetail)
      sidebarData.value.selectedAlbumName = albumDetail.name || ''
      console.log('设置专辑名称:', sidebarData.value.selectedAlbumName)
    } catch (error) {
      console.error('获取专辑详情失败:', error)
      sidebarData.value.selectedAlbumName = ''
    }
  }

  // 加载侧边栏数据
  console.log('开始加载侧边栏数据')
  loadSidebarData()

  // 加载随笔列表
  console.log('开始加载随笔列表')
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

  console.log('=== onMounted 完成 ===')
})
</script>

<style scoped>
.reading-essays-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ==================== 移动端布局 ==================== */
/* 覆盖样式在非 scoped 的 <style> 块中 */

/* ==================== 分类导航栏 ==================== */
/*
 * PC端：使用 sticky，在容器内固定
 * 移动端：使用 fixed，全屏固定
 */
.category-nav {
  position: sticky;
  top: 70px;
  z-index: 40;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  background: var(--color-surface-warm);
  border-bottom: 1px solid var(--color-border-light);
  margin-left: -16px;
  margin-right: -16px;
}

.category-nav::-webkit-scrollbar {
  display: none;
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
    overflow-x: visible;
    margin-left: 0;
    margin-right: 0;
  }
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

/* 筛选状态 */
.filter-status {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-sm);
  font-size: 13px;
}

.filter-label {
  color: var(--color-ink-muted);
}

.filter-tag {
  padding: 4px 10px;
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
}

.btn-clear {
  padding: 4px 10px;
  background: transparent;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-clear:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
}
</style>

<!-- 非 scoped 样式：仅对随笔页面覆盖布局的默认 padding-top -->
<style>
@media (max-width: 768px) {
  body.page-reading-essays .home-main {
    padding-top: 170px !important;
  }
}
</style>
