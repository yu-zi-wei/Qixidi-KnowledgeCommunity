<template>
  <div class="reading-essays-detail-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <p>{{ error.message }}</p>
    </div>

    <!-- 随笔详情 -->
    <template v-else-if="readingEssay">
      <article class="reading-essays-detail-card">
        <!-- 随笔内容 -->
        <div class="reading-essays-content">
          {{ readingEssay.content }}
        </div>

        <!-- 作者信息 -->
        <div v-if="readingEssay.author || readingEssay.worksName" class="reading-essays-source">
          <span v-if="readingEssay.author">{{ readingEssay.author }}</span>
          <span v-if="readingEssay.worksName">《{{ readingEssay.worksName }}》</span>
        </div>

        <!-- 分类信息 -->
        <div class="reading-essays-meta">
          <span v-if="readingEssay.groupName" class="meta-badge">{{ readingEssay.groupName }}</span>
          <span v-if="readingEssay.albumName" class="meta-badge">{{ readingEssay.albumName }}</span>
          <span class="meta-time">{{ getFullDateCN(readingEssay.createTime) }}</span>
        </div>

        <!-- 标签 -->
        <div v-if="readingEssay.labelList && readingEssay.labelList.length > 0" class="reading-essays-labels">
          <span v-for="label in readingEssay.labelList" :key="label" class="label-tag">
            # {{ label }}
          </span>
        </div>

        <!-- 统计信息 -->
        <div class="reading-essays-stats">
          <span class="stat-item">
            <ThumbUp class="stat-icon" />
            {{ readingEssay.helpSum || 0 }}
          </span>
          <span class="stat-item">
            <MessageCircle class="stat-icon" />
            {{ readingEssay.commentSum || 0 }}
          </span>
        </div>
      </article>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ThumbUp, MessageCircle } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'
import { getFullDateCN } from '~/utils/formatTime'

definePageMeta({
  sidebar: 'readingEssays'
})

const route = useRoute()
const readingEssaysApi = useReadingEssaysApi()

// 侧边栏数据共享状态
const sidebarData = useState('reading-essays-sidebar-data', () => ({
  groups: [] as any[],
  albums: [] as ReadingEssaysAlbum[],
  totalAlbums: 0,
  popularAuthors: [] as any[],
  popularLabels: [] as any[],
  selectedAlbumId: null as number | null,
  selectedLabel: null as string | null,
  selectedAuthor: null as string | null,
  selectedAlbumName: '' as string
}))

// 加载侧边栏数据
const loadSidebarData = async () => {
  // 如果已经加载过，跳过
  if (sidebarData.value.groups.length > 0) return

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
    console.error('加载侧边栏数据失败:', error)
  }
}

// 使用 computed 获取路由参数（参考文章详情页的实现）
const readingEssayId = computed(() => route.params.id as string)

// 使用 useAsyncData 获取随笔详情
// 注意：ID 作为字符串传递，避免精度丢失
const { data: readingEssay, pending, error, refresh } = await useAsyncData(
  () => `reading-essays-${readingEssayId.value}`,
  () => readingEssaysApi.getReadingEssaysDetail(readingEssayId.value)
)

// SEO
useHead({
  title: () => readingEssay.value?.content?.substring(0, 50) || '随笔详情'
})

// 初始化：加载侧边栏数据
await loadSidebarData()

// 监听路由参数变化，手动刷新数据
watch(() => route.params.id, async (newId, oldId) => {
  if (newId !== oldId) {
    await refresh()
    // 刷新后滚动到顶部
    window.scrollTo({ top: 0, behavior: 'instant' })
  }
})
</script>

<style scoped>
.reading-essays-detail-page {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.reading-essays-detail-card {
  max-width: 700px;
  width: 100%;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 40px;
  box-shadow: var(--shadow-md);
}

/* 随笔内容 */
.reading-essays-content {
  font-size: var(--text-xl);
  line-height: var(--leading-relaxed);
  color: var(--color-ink);
  margin-bottom: 24px;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
}

/* 作者信息 */
.reading-essays-source {
  font-size: var(--text-base);
  color: var(--color-ink-muted);
  font-style: italic;
  margin-bottom: 20px;
}

/* 分类信息 */
.reading-essays-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.meta-badge {
  font-size: var(--text-sm);
  padding: 4px 12px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-sm);
  color: var(--color-ink-light);
}

.meta-time {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 标签 */
.reading-essays-labels {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--color-border-light);
}

.label-tag {
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 4px 12px;
  border-radius: var(--radius-sm);
}

/* 统计信息 */
.reading-essays-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.stat-icon {
  width: 18px;
  height: 18px;
}

/* 加载/错误状态 */
.loading-state,
.error-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  color: var(--color-ink-muted);
}

/* 响应式 */
@media (max-width: 768px) {
  .reading-essays-detail-page {
    padding: 20px 16px;
  }

  .reading-essays-detail-card {
    padding: 24px;
  }

  .reading-essays-content {
    font-size: var(--text-lg);
  }
}
</style>
