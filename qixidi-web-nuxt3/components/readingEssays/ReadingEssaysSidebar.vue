<template>
  <aside v-if="isReady" class="reading-essays-sidebar">
    <!-- 专辑（带封面） -->
    <section v-if="displayedAlbums.length > 0" class="sidebar-card gray-card sidebar-section">
      <div class="section-title">
        <Folder class="section-icon" />
        <span>推荐专辑</span>
      </div>
      <div class="album-list">
        <div
          v-for="album in displayedAlbums"
          :key="album.id"
          class="album-card"
          :class="{ active: selectedAlbumId === album.id }"
          @click="handleSelectAlbum(album.id)"
        >
          <div v-if="album.cover" class="album-cover">
            <img :src="album.cover" :alt="album.name" loading="lazy" />
          </div>
          <div v-else class="album-cover album-cover-placeholder">
            <Folder class="placeholder-icon" />
          </div>
          <div class="album-info">
            <div class="album-name">{{ album.name }}</div>
            <div v-if="album.employSum !== undefined" class="album-count">{{ album.employSum }} 篇</div>
          </div>
        </div>
      </div>
      <button v-if="totalAlbums > 5" class="btn-view-all" @click="showAlbumSelector = true">
        查看全部 {{ totalAlbums }} 个专辑 →
      </button>
    </section>

    <!-- 热门作者（可展开） -->
    <section v-if="popularAuthors.length > 0" class="sidebar-card gradient-card sidebar-section">
      <div class="section-title section-title-clickable" @click="toggleAuthorsExpand">
        <User class="section-icon" />
        <span>热门作者</span>
        <ChevronDown class="chevron-icon" :class="{ expanded: authorsExpanded }" />
      </div>
      <div v-show="authorsExpanded" class="tag-cloud">
        <span
          v-for="authorItem in displayedAuthors"
          :key="authorItem.author"
          class="tag-cloud-item"
          :class="{ active: selectedAuthor === authorItem.author }"
          @click="handleSelectAuthor(authorItem.author)"
        >
          {{ authorItem.author }} +{{ authorItem.count }}
        </span>
      </div>
    </section>

    <!-- 热门标签（可展开） -->
    <section v-if="popularLabels.length > 0" class="sidebar-card gradient-card sidebar-section">
      <div class="section-title section-title-clickable" @click="toggleLabelsExpand">
        <Hash class="section-icon" />
        <span>热门标签</span>
        <ChevronDown class="chevron-icon" :class="{ expanded: labelsExpanded }" />
      </div>
      <div v-show="labelsExpanded" class="tag-cloud">
        <span
          v-for="labelItem in popularLabels"
          :key="labelItem.label"
          class="tag-cloud-item"
          :class="{ active: selectedLabel === labelItem.label }"
          @click="handleSelectLabel(labelItem.label)"
        >
          # {{ labelItem.label }} +{{ labelItem.count }}
        </span>
      </div>
    </section>
  </aside>

  <!-- 加载占位 -->
  <aside v-else class="reading-essays-sidebar placeholder">
    <div class="sidebar-card skeleton"></div>
    <div class="sidebar-card skeleton"></div>
    <div class="sidebar-card skeleton"></div>
  </aside>

  <!-- 专辑选择器弹窗（客户端组件） -->
  <AlbumSelectorClient v-model:show="showAlbumSelector" @select="handleSelectAlbumFromModal" />
</template>

<script setup lang="ts">
import { Folder, Hash, User, ChevronDown } from '@vicons/tabler'
import type { ReadingEssaysAlbum, ReadingEssaysAuthor, ReadingEssaysLabel } from '~/types'
import AlbumSelectorClient from './AlbumSelector.client.vue'

// 从 useState 获取侧边栏数据
// 与 index.vue 共享，groups 用于顶部导航
const sidebarData = useState('reading-essays-sidebar-data', () => ({
  groups: [] as any[],
  albums: [] as ReadingEssaysAlbum[],
  totalAlbums: 0,
  popularAuthors: [] as ReadingEssaysAuthor[],
  popularLabels: [] as ReadingEssaysLabel[],
  selectedAlbumId: null as number | null,
  selectedLabel: null as string | null,
  selectedAuthor: null as string | null,
  selectedAlbumName: '' as string  // 当前选中的专辑名称
}))

// 标签展开状态
const labelsExpanded = ref(true)
const authorsExpanded = ref(true)
const maxAuthors = 5
const maxAlbums = 5

// 专辑选择器弹窗状态
const showAlbumSelector = ref(false)

// 是否准备就绪（groups 数据已加载到顶部导航）
const isReady = computed(() => sidebarData.value.groups.length > 0)

const albums = computed(() => sidebarData.value.albums)
const totalAlbums = computed(() => sidebarData.value.totalAlbums || 0)
const popularAuthors = computed(() => sidebarData.value.popularAuthors)
const popularLabels = computed(() => sidebarData.value.popularLabels)
const selectedAlbumId = computed(() => sidebarData.value.selectedAlbumId)
const selectedLabel = computed(() => sidebarData.value.selectedLabel)
const selectedAuthor = computed(() => sidebarData.value.selectedAuthor)

// 显示的专辑（限制数量）
const displayedAlbums = computed(() => {
  return albums.value.slice(0, maxAlbums)
})

// 显示的作者（限制数量）
const displayedAuthors = computed(() => {
  return popularAuthors.value.slice(0, maxAuthors)
})

// 切换标签展开状态
const toggleLabelsExpand = () => {
  labelsExpanded.value = !labelsExpanded.value
}

const toggleAuthorsExpand = () => {
  authorsExpanded.value = !authorsExpanded.value
}

// 判断是否在详情页
const isDetailPage = () => {
  const route = useRoute()
  return route.params.id !== undefined
}

// 更新选择状态
const handleSelectAlbum = (id: number) => {
  sidebarData.value.selectedAlbumId = sidebarData.value.selectedAlbumId === id ? null : id
  navigateToUpdateQuery()
}

// 从弹窗选择专辑（始终设置为选中）
const handleSelectAlbumFromModal = (id: number) => {
  sidebarData.value.selectedAlbumId = id
  // 确保 DOM 更新后再更新路由
  nextTick(() => {
    navigateToUpdateQuery()
  })
}

const handleSelectLabel = (label: string) => {
  sidebarData.value.selectedLabel = sidebarData.value.selectedLabel === label ? null : label
  navigateToUpdateQuery()
}

const handleSelectAuthor = (author: string) => {
  sidebarData.value.selectedAuthor = sidebarData.value.selectedAuthor === author ? null : author
  navigateToUpdateQuery()
}

// 通过路由 query 参数通知页面更新
const navigateToUpdateQuery = () => {
  const route = useRoute()
  const query = { ...route.query }

  if (sidebarData.value.selectedAlbumId) {
    query.albumId = String(sidebarData.value.selectedAlbumId)
  } else {
    delete query.albumId
  }

  if (sidebarData.value.selectedLabel) {
    query.label = sidebarData.value.selectedLabel
  } else {
    delete query.label
  }

  if (sidebarData.value.selectedAuthor) {
    query.author = sidebarData.value.selectedAuthor
  } else {
    delete query.author
  }

  // 如果在详情页，跳转到列表页并应用筛选
  if (isDetailPage()) {
    navigateTo({ path: '/reading-essays', query })
  } else {
    navigateTo({ query }, { replace: true })
  }
}
</script>

<style scoped>
/* 滚动容器 */
.reading-essays-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
  max-height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  padding-right: 4px;
}

.reading-essays-sidebar::-webkit-scrollbar {
  display: none;
}

/* 卡片通用样式 */
.sidebar-card {
  background: rgba(255, 255, 255, 0.75);
  border: 1px solid rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s ease;
}

.sidebar-card:hover {
  background: rgba(255, 255, 255, 0.85);
}

/* 灰色背景卡片 - 也使用半透明白色 */
.sidebar-card.gray-card {
  background: rgba(255, 255, 255, 0.7);
}

.sidebar-card.gray-card:hover {
  background: rgba(255, 255, 255, 0.8);
}

.dark .sidebar-card.gray-card {
  background: rgba(40, 42, 45, 0.7);
}

.dark .sidebar-card.gray-card:hover {
  background: rgba(50, 52, 55, 0.8);
}

/* 渐变背景卡片 */
.sidebar-card.gradient-card {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.06) 0%, rgba(93, 138, 168, 0.02) 100%);
  border-color: rgba(61, 90, 128, 0.08);
}

.sidebar-card.gradient-card:hover {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.1) 0%, rgba(93, 138, 168, 0.04) 100%);
}

.dark .sidebar-card.gradient-card {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.08) 0%, rgba(61, 90, 128, 0.03) 100%);
  border-color: rgba(93, 138, 168, 0.1);
}

.dark .sidebar-card.gradient-card:hover {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.12) 0%, rgba(61, 90, 128, 0.05) 100%);
}

/* 暗色主题 */
.dark .sidebar-card {
  background: rgba(40, 42, 45, 0.75);
  border-color: rgba(255, 255, 255, 0.04);
}

.dark .sidebar-card:hover {
  background: rgba(50, 52, 55, 0.85);
}

/* 占位状态 */
.reading-essays-sidebar.placeholder {
  opacity: 0.6;
}

.skeleton {
  min-height: 140px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.04) 25%, rgba(0, 0, 0, 0.06) 50%, rgba(0, 0, 0, 0.04) 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s ease-in-out infinite;
  border-radius: 16px;
}

.dark .skeleton {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.04) 25%, rgba(255, 255, 255, 0.06) 50%, rgba(255, 255, 255, 0.04) 75%);
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* 分区 */
.sidebar-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 分区标题 */
.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-ink);
  margin-bottom: 4px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.dark .section-title {
  border-bottom-color: rgba(255, 255, 255, 0.05);
}

.section-title-clickable {
  cursor: pointer;
  user-select: none;
}

.section-icon {
  width: 16px;
  height: 16px;
  color: var(--color-primary);
  flex-shrink: 0;
}

.chevron-icon {
  width: 14px;
  height: 14px;
  margin-left: auto;
  transition: transform 0.2s ease;
  color: var(--color-ink-muted);
}

.chevron-icon.expanded {
  transform: rotate(180deg);
}

/* 专辑卡片 */
.album-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.album-card {
  display: flex;
  gap: 10px;
  padding: 10px;
  border: 1px solid transparent;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: rgba(0, 0, 0, 0.06);
}

.album-card:hover {
  background: rgba(61, 90, 128, 0.1);
  border-color: rgba(61, 90, 128, 0.2);
}

.album-card.active {
  background: var(--color-primary-light);
  border-color: var(--color-primary);
}

.dark .album-card {
  background: rgba(255, 255, 255, 0.06);
}

.dark .album-card:hover {
  background: rgba(61, 90, 128, 0.15);
  border-color: rgba(61, 90, 128, 0.3);
}

.album-cover {
  width: 52px;
  height: 52px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
  background: var(--color-surface-dim);
}

.album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.album-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.placeholder-icon {
  width: 20px;
  height: 20px;
  opacity: 0.5;
}

.album-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 3px;
  flex: 1;
  min-width: 0;
}

.album-name {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
}

.album-count {
  font-size: 11px;
  color: var(--color-ink-muted);
}

.album-card.active .album-name {
  color: var(--color-primary);
}

.album-card.active .album-count {
  color: var(--color-primary);
}

/* 查看全部按钮 */
.btn-view-all {
  width: 100%;
  padding: 10px 14px;
  margin-top: 4px;
  background: rgba(0, 0, 0, 0.02);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 10px;
  font-size: 12px;
  font-weight: 500;
  color: var(--color-ink-light);
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-view-all:hover {
  background: var(--color-primary-light);
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.dark .btn-view-all {
  background: rgba(255, 255, 255, 0.02);
  border-color: rgba(255, 255, 255, 0.06);
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-cloud-item {
  font-size: 12px;
  color: var(--color-ink-light);
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.08) 0%, rgba(93, 138, 168, 0.04) 100%);
  padding: 6px 12px;
  border: 1px solid rgba(61, 90, 128, 0.12);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-cloud-item:hover {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.15) 0%, rgba(93, 138, 168, 0.1) 100%);
  border-color: rgba(61, 90, 128, 0.25);
  color: var(--color-primary);
}

.tag-cloud-item.active {
  background: linear-gradient(135deg, var(--color-primary) 0%, #5a7fa8 100%);
  color: #fff;
  border-color: var(--color-primary);
}

.dark .tag-cloud-item {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.12) 0%, rgba(61, 90, 128, 0.06) 100%);
  border-color: rgba(93, 138, 168, 0.2);
}

.dark .tag-cloud-item:hover {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.2) 0%, rgba(61, 90, 128, 0.12) 100%);
  border-color: rgba(93, 138, 168, 0.35);
}
</style>
