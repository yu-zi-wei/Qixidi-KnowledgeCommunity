<template>
  <n-modal
    v-model:show="showModal"
    preset="card"
    :style="{ width: '640px', maxWidth: '90vw' }"
    title="选择专辑"
    :bordered="false"
    :segmented="{ content: 'soft' }"
    to="body"
    @update:show="handleClose"
  >
      <!-- 搜索框 -->
      <div class="search-section">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索专辑名称..."
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <n-icon :component="Search" />
          </template>
        </n-input>
      </div>

      <!-- 专辑列表 -->
      <div v-if="loading && albums.length === 0" class="loading-state">
        <n-spin size="medium" />
      </div>

      <div v-else-if="albums.length === 0" class="empty-state">
        <p>没有找到专辑</p>
      </div>

      <div v-else class="album-grid">
        <div
          v-for="album in albums"
          :key="album.id"
          class="album-grid-item"
          :class="{ selected: selectedAlbumId === album.id }"
          @click="handleSelect(album)"
        >
          <div v-if="album.cover" class="album-cover">
            <img :src="album.cover" :alt="album.name" loading="lazy" />
          </div>
          <div v-else class="album-cover album-cover-placeholder">
            <Folder class="placeholder-icon" />
          </div>
          <div class="album-info">
            <div class="album-name">{{ album.name }}</div>
            <div class="album-meta">
              <span v-if="album.employSum !== undefined" class="album-count">{{ album.employSum }} 篇</span>
            </div>
            <div v-if="album.briefIntroduction" class="album-intro" :title="album.briefIntroduction">
              {{ album.briefIntroduction }}
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > pageSize" class="pagination-section">
        <n-pagination
          v-model:page="pageNum"
          :page-count="Math.ceil(total / pageSize)"
          :page-size="pageSize"
          @update:page="handlePageChange"
        />
      </div>
    </n-modal>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { NModal, NInput, NIcon, NSpin, NPagination } from 'naive-ui'
import { Search, Folder } from '@vicons/tabler'
import type { ReadingEssaysAlbum } from '~/types'

interface Props {
  show: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'select', albumId: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const readingEssaysApi = useReadingEssaysApi()

// 内部状态
const showModal = computed({
  get: () => props.show,
  set: (val) => emit('update:show', val)
})

const searchKeyword = ref('')
const pageNum = ref(1)
const pageSize = 8
const loading = ref(false)
const albums = ref<ReadingEssaysAlbum[]>([])
const total = ref(0)

// 当前选中的专辑ID（从路由或状态获取）
const route = useRoute()
const selectedAlbumId = computed(() => {
  const id = route.query.albumId
  return id ? Number(id) : null
})

// 加载专辑列表
const loadAlbums = async () => {
  loading.value = true
  try {
    const result = await readingEssaysApi.getRecommendedAlbums({
      pageNum: pageNum.value,
      pageSize,
      albumName: searchKeyword.value || undefined
    })
    albums.value = result.rows || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载专辑失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索处理（防抖）
let searchTimer: ReturnType<typeof setTimeout> | null = null
const handleSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    pageNum.value = 1
    loadAlbums()
  }, 300)
}

// 分页变化
const handlePageChange = () => {
  loadAlbums()
}

// 选择专辑
const handleSelect = (album: ReadingEssaysAlbum) => {
  emit('select', album.id)
  handleClose()
}

// 关闭弹窗
const handleClose = () => {
  emit('update:show', false)
  // 重置搜索状态
  searchKeyword.value = ''
  pageNum.value = 1
}

// 监听弹窗打开，加载数据
watch(() => props.show, (newVal) => {
  if (newVal) {
    loadAlbums()
  }
})
</script>

<style scoped>
.search-section {
  margin-bottom: 16px;
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: var(--color-ink-muted);
  font-size: 14px;
}

/* 专辑网格 */
.album-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 16px;
  max-height: 420px;
  overflow-y: auto;
  padding-right: 4px;
}

.album-grid::-webkit-scrollbar {
  width: 4px;
}

.album-grid::-webkit-scrollbar-track {
  background: transparent;
}

.album-grid::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 4px;
}

.album-grid-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.2s ease;
  background: rgba(0, 0, 0, 0.03);
  align-items: flex-start;
}

.album-grid-item:hover {
  background: rgba(61, 90, 128, 0.06);
}

.album-grid-item.selected {
  background: var(--color-primary-light);
}

.album-grid-item .album-cover {
  width: 64px;
  height: 64px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
}

.album-grid-item .album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.album-grid-item .album-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.album-grid-item .placeholder-icon {
  width: 22px;
  height: 22px;
  opacity: 0.5;
}

.album-grid-item .album-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  min-width: 0;
}

.album-grid-item .album-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.4;
}

.album-grid-item .album-meta {
  display: flex;
  align-items: center;
  gap: 6px;
}

.album-grid-item .album-count {
  font-size: 12px;
  color: var(--color-ink-muted);
}

.album-grid-item .album-intro {
  font-size: 12px;
  color: var(--color-ink-muted);
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-break: break-word;
}

.album-grid-item.selected .album-name {
  color: var(--color-primary);
}

.album-grid-item.selected .album-count {
  color: var(--color-primary);
}

/* 暗色主题 */
:global(.dark) .album-grid-item {
  background: rgba(255, 255, 255, 0.04);
}

:global(.dark) .album-grid-item:hover {
  background: rgba(90, 127, 168, 0.1);
}

:global(.dark) .album-grid-item.selected {
  background: rgba(90, 127, 168, 0.15);
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}
</style>
