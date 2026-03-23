<template>
  <aside v-if="isReady" class="reading-essays-sidebar">
    <!-- 专辑（带封面） -->
    <section v-if="displayedAlbums.length > 0" class="sidebar-card sidebar-section">
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
    <section v-if="popularAuthors.length > 0" class="sidebar-card sidebar-section">
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
    <section v-if="popularLabels.length > 0" class="sidebar-card sidebar-section">
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
.reading-essays-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
  max-height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  /* 隐藏滚动条 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}

.reading-essays-sidebar::-webkit-scrollbar {
  display: none; /* Chrome/Safari */
}

/* 卡片样式 */
.sidebar-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 16px;
  border: 1px solid var(--color-border-light);
}

/* 占位状态 */
.reading-essays-sidebar.placeholder {
  opacity: 0.5;
}

.skeleton {
  min-height: 120px;
  background: linear-gradient(90deg, var(--color-surface-dim) 25%, var(--color-border-light) 50%, var(--color-surface-dim) 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s ease-in-out infinite;
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

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
}

.section-title-clickable {
  cursor: pointer;
  user-select: none;
}

.section-icon {
  width: 18px;
  height: 18px;
  color: var(--color-primary);
  flex-shrink: 0;
}

.chevron-icon {
  width: 16px;
  height: 16px;
  margin-left: auto;
  transition: transform var(--transition-fast);
  color: var(--color-ink-muted);
}

.chevron-icon.expanded {
  transform: rotate(180deg);
}

/* 专辑卡片 */
.album-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.album-card {
  display: flex;
  gap: 8px;
  padding: 8px;
  border: 1px solid transparent;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  background: var(--color-surface);
}

.album-card:hover {
  background: var(--color-surface);
  border-color: var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.album-card.active {
  background: var(--color-primary-light);
  border-color: var(--color-primary);
}

.album-cover {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
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
  border: 1px solid var(--color-border-light);
  color: var(--color-ink-muted);
}

.placeholder-icon {
  width: 20px;
  height: 20px;
  opacity: 0.6;
}

.album-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
  flex: 1;
  min-width: 0;
}

.album-name {
  font-size: var(--text-xs);
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
  font-size: 10px;
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
  padding: 8px 12px;
  margin-top: 8px;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  color: var(--color-ink-light);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-view-all:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
  background: var(--color-primary-light);
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-cloud-item {
  font-size: var(--text-xs);
  color: var(--color-ink-light);
  background: var(--color-surface);
  padding: 5px 11px;
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tag-cloud-item:hover {
  background: var(--color-primary-light);
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.tag-cloud-item.active {
  background: var(--color-primary);
  color: #fff;
  border-color: var(--color-primary);
}
</style>
