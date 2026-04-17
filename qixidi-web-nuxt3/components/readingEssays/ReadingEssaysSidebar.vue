<template>
  <aside v-if="isReady" class="reading-essays-sidebar">
    <!-- 专辑（带封面） -->
    <section v-if="displayedAlbums.length > 0" class="sidebar-card sidebar-section" style="--delay: 0">
      <div class="section-title">
        <Folder class="section-icon" />
        <span>推荐专辑</span>
      </div>
      <div class="album-list">
        <NuxtLink
          v-for="album in displayedAlbums"
          :key="album.id"
          :to="buildFilterLink({ albumId: selectedAlbumId === album.id ? null : album.id })"
          class="album-card"
          :class="{ active: selectedAlbumId === album.id }"
          @click="handleAlbumClick(album.id)"
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
        </NuxtLink>
      </div>
      <button v-if="totalAlbums > 5" class="btn-view-all" @click="showAlbumSelector = true">
        查看全部 {{ totalAlbums }} 个专辑
        <ChevronRight style="width: 14px; height: 14px;" />
      </button>
    </section>

    <!-- 热门作者（可展开） -->
    <section v-if="popularAuthors.length > 0" class="sidebar-card sidebar-section" style="--delay: 1">
      <div class="section-title section-title-clickable" @click="toggleAuthorsExpand">
        <User class="section-icon" />
        <span>热门作者</span>
        <ChevronDown class="chevron-icon" :class="{ expanded: authorsExpanded }" />
      </div>
      <div v-show="authorsExpanded" class="tag-cloud">
        <NuxtLink
          v-for="authorItem in displayedAuthors"
          :key="authorItem.author"
          :to="buildFilterLink({ author: selectedAuthor === authorItem.author ? null : authorItem.author })"
          class="tag-cloud-item"
          :class="{ active: selectedAuthor === authorItem.author }"
          @click="handleAuthorClick(authorItem.author)"
        >
          {{ authorItem.author }} +{{ authorItem.count }}
        </NuxtLink>
      </div>
    </section>

    <!-- 热门标签（可展开） -->
    <section v-if="popularLabels.length > 0" class="sidebar-card sidebar-section" style="--delay: 2">
      <div class="section-title section-title-clickable" @click="toggleLabelsExpand">
        <Hash class="section-icon" />
        <span>热门标签</span>
        <ChevronDown class="chevron-icon" :class="{ expanded: labelsExpanded }" />
      </div>
      <div v-show="labelsExpanded" class="tag-cloud">
        <NuxtLink
          v-for="labelItem in popularLabels"
          :key="labelItem.label"
          :to="buildFilterLink({ label: selectedLabel === labelItem.label ? null : labelItem.label })"
          class="tag-cloud-item"
          :class="{ active: selectedLabel === labelItem.label }"
          @click="handleLabelClick(labelItem.label)"
        >
          # {{ labelItem.label }} +{{ labelItem.count }}
        </NuxtLink>
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
import { Folder, Hash, User, ChevronDown, ChevronRight } from '@vicons/tabler'
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

// 点击时仅更新 sidebarData 状态（路由由 NuxtLink :to 处理）
const handleAlbumClick = (id: number) => {
  sidebarData.value.selectedAlbumId = sidebarData.value.selectedAlbumId === id ? null : id
}

// 从弹窗选择专辑（始终设置为选中，仍需手动跳转）
const handleSelectAlbumFromModal = (id: number) => {
  sidebarData.value.selectedAlbumId = id
  nextTick(() => {
    navigateTo(buildFilterLink({ albumId: id }))
  })
}

const handleLabelClick = (label: string) => {
  sidebarData.value.selectedLabel = sidebarData.value.selectedLabel === label ? null : label
}

const handleAuthorClick = (author: string) => {
  sidebarData.value.selectedAuthor = sidebarData.value.selectedAuthor === author ? null : author
}

// 构建筛选链接
const buildFilterLink = (overrides: { albumId?: number | null; label?: string | null; author?: string | null } = {}) => {
  const albumId = overrides.albumId !== undefined ? overrides.albumId : sidebarData.value.selectedAlbumId
  const label = overrides.label !== undefined ? overrides.label : sidebarData.value.selectedLabel
  const author = overrides.author !== undefined ? overrides.author : sidebarData.value.selectedAuthor

  const query: Record<string, string> = {}
  if (albumId) query.albumId = String(albumId)
  if (label) query.label = label
  if (author) query.author = author

  if (isDetailPage()) {
    return { path: '/reading-essays', query }
  }
  return { path: '/reading-essays', query }
}
</script>

<style scoped>
/* 卡片入场动画 - 与首页侧边栏统一 */
@keyframes sidebar-card-in {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动容器 */
.reading-essays-sidebar {
  display: flex;
  flex-direction: column;
  gap: 12px;
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

/* 卡片通用样式 - 轻透风格，与首页侧边栏统一 */
.sidebar-card {
  background: rgba(255, 255, 255, 0.25);
  border: none;
  border-radius: 10px;
  padding: 16px;
  transition: all 0.2s ease;
  animation: sidebar-card-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) calc(var(--delay, 0) * 0.04s) forwards;
  opacity: 0;
}

.sidebar-card:hover {
  background: rgba(255, 255, 255, 0.4);
}

/* 暗色主题 */
:root.dark .sidebar-card {
  background: rgba(255, 255, 255, 0.03);
}

:root.dark .sidebar-card:hover {
  background: rgba(255, 255, 255, 0.06);
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
  border-radius: 10px;
}

:root.dark .skeleton {
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
  gap: 6px;
}

.album-card {
  display: flex;
  gap: 10px;
  padding: 8px;
  margin: 0 -8px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.2s ease;
}

.album-card:hover {
  background: rgba(0, 0, 0, 0.03);
}

.album-card.active {
  background: var(--color-primary-light);
}

:root.dark .album-card:hover {
  background: rgba(255, 255, 255, 0.04);
}

.album-cover {
  width: 44px;
  height: 44px;
  border-radius: 6px;
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
  width: 18px;
  height: 18px;
  opacity: 0.5;
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
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 2px;
  width: 100%;
  padding: 7px 0;
  margin-top: 2px;
  background: none;
  border: none;
  font-size: 12px;
  font-weight: 500;
  color: var(--color-ink-muted);
  cursor: pointer;
  transition: color 0.2s ease;
}

.btn-view-all:hover {
  color: var(--color-primary);
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag-cloud-item {
  font-size: 12px;
  color: var(--color-ink-light);
  background: rgba(0, 0, 0, 0.03);
  padding: 5px 10px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.2s ease;
}

.tag-cloud-item:hover {
  background: rgba(61, 90, 128, 0.08);
  color: var(--color-primary);
}

.tag-cloud-item.active {
  background: var(--color-primary);
  color: #fff;
}

:root.dark .tag-cloud-item {
  background: rgba(255, 255, 255, 0.05);
}

:root.dark .tag-cloud-item:hover {
  background: rgba(90, 127, 168, 0.15);
}
</style>
