<template>
  <n-modal
    v-model:show="showModal"
    preset="card"
    :style="{ width: '700px', maxWidth: '90vw' }"
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
            <div v-if="album.employSum !== undefined" class="album-count">{{ album.employSum }} 篇</div>
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
          show-quick-jumper
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
  margin-bottom: 20px;
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: var(--color-ink-muted);
}

/* 专辑网格 */
.album-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 20px;
  max-height: 450px;
  overflow-y: auto;
  padding-right: 4px;
}

/* 滚动条样式 */
.album-grid::-webkit-scrollbar {
  width: 5px;
}

.album-grid::-webkit-scrollbar-track {
  background: transparent;
}

.album-grid::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.album-grid-item {
  display: flex;
  flex-direction: row;
  gap: 14px;
  padding: 14px;
  border: 1px solid rgba(61, 90, 128, 0.12);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.25s ease;
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.06) 0%, rgba(93, 138, 168, 0.02) 100%);
  align-items: flex-start;
}

.album-grid-item:hover {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.12) 0%, rgba(93, 138, 168, 0.06) 100%);
  border-color: rgba(61, 90, 128, 0.25);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(61, 90, 128, 0.15);
}

.album-grid-item.selected {
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.18) 0%, rgba(93, 138, 168, 0.1) 100%);
  border-color: var(--color-primary);
  box-shadow: 0 4px 16px rgba(61, 90, 128, 0.2);
}

.album-grid-item .album-cover {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
}

.album-grid-item .album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.album-grid-item:hover .album-cover img {
  transform: scale(1.08);
}

.album-grid-item .album-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(61, 90, 128, 0.15) 0%, rgba(93, 138, 168, 0.08) 100%);
  color: var(--color-primary);
}

.album-grid-item .placeholder-icon {
  width: 26px;
  height: 26px;
  opacity: 0.7;
}

.album-grid-item .album-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
  min-width: 0;
}

.album-grid-item .album-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.album-grid-item .album-count {
  font-size: 12px;
  color: var(--color-ink-muted);
  display: flex;
  align-items: center;
  gap: 4px;
}

.album-grid-item .album-intro {
  font-size: 12px;
  color: var(--color-ink-light);
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

.album-grid-item.selected .album-intro {
  color: rgba(61, 90, 128, 0.8);
}

/* 暗色主题 */
:global(.dark) .album-grid-item {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.1) 0%, rgba(61, 90, 128, 0.04) 100%);
  border-color: rgba(93, 138, 168, 0.2);
}

:global(.dark) .album-grid-item:hover {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.18) 0%, rgba(61, 90, 128, 0.1) 100%);
  border-color: rgba(93, 138, 168, 0.35);
}

:global(.dark) .album-grid-item.selected {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.25) 0%, rgba(61, 90, 128, 0.15) 100%);
}

:global(.dark) .album-grid-item .album-cover-placeholder {
  background: linear-gradient(135deg, rgba(93, 138, 168, 0.2) 0%, rgba(61, 90, 128, 0.1) 100%);
}

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  padding-top: 16px;
  border-top: 1px solid var(--color-border-light);
}
</style>
