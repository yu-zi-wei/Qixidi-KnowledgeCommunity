<template>
  <div class="album-detail-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <n-spin size="large" />
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error || !albumData" class="error-state">
      <p style="color: var(--color-ink-muted);">该专辑可能已被删除或不存在</p>
      <n-button @click="navigateTo('/reading-essays')">返回随笔</n-button>
    </div>

    <!-- 正常内容 -->
    <template v-else>
      <!-- 专辑头部 - 固定不滚动 -->
      <header class="album-header">
        <div class="header-inner">
          <div class="header-cover" v-if="albumData.cover">
            <img :src="albumData.cover" :alt="albumData.name" />
          </div>
          <div class="header-cover cover-placeholder" v-else>
            <n-icon size="36"><Folder /></n-icon>
          </div>
          <div class="header-info">
            <h1 class="album-name">{{ albumData.name }}</h1>
            <p class="album-introduce" v-if="albumData.briefIntroduction">
              {{ albumData.briefIntroduction }}
            </p>
            <div class="album-meta">
              <span class="stat-item">
                <n-icon><FileText /></n-icon>
                {{ albumData.employSum || 0 }} 篇随笔
              </span>
              <span class="stat-item" v-if="albumData.createTime" :title="getFullDateTime(albumData.createTime)">
                <n-icon><Calendar /></n-icon>
                {{ formatTime(albumData.createTime) }}
              </span>
            </div>
          </div>
        </div>
      </header>

      <!-- 随笔列表标题 - 固定 -->
      <div class="section-header">
        <div class="section-header-inner">
          <h2 class="section-title">专辑随笔</h2>
        </div>
      </div>

      <!-- 随笔列表 - 可滚动 -->
      <main class="essay-scroll-area">
        <div class="essay-section">
          <div v-if="essayList.length === 0 && !loadingMore" class="empty-state">
            <n-empty description="暂无随笔" />
          </div>

          <div class="essay-list" v-else>
            <div
              v-for="essay in essayList"
              :key="essay.id"
              class="essay-card"
            >
              <!-- 随笔内容 -->
              <NuxtLink :to="`/reading-essays/${essay.id}`" class="essay-content">
                {{ essay.content }}
              </NuxtLink>

              <!-- 作者和作品 -->
              <div v-if="essay.author || essay.worksName" class="essay-source">
                <span class="source-sep">——</span>
                <span v-if="essay.author" class="source-author">{{ essay.author }}</span>
                <span v-if="essay.worksName" class="source-work">《{{ essay.worksName }}》</span>
              </div>

              <!-- 分类和标签 -->
              <div class="essay-meta">
                <span v-if="essay.groupName" class="meta-badge">{{ essay.groupName }}</span>
                <div v-if="essay.labelList && essay.labelList.length > 0" class="meta-labels">
                  <span v-for="label in essay.labelList.slice(0, 3)" :key="label" class="label-tag">
                    # {{ label }}
                  </span>
                </div>
              </div>

              <!-- 底部 -->
              <div class="essay-footer">
                <div class="footer-left">
                  <span class="stat-item">
                    <n-icon><MessageCircle /></n-icon>
                    {{ essay.commentSum || 0 }}
                  </span>
                  <span class="stat-item" :title="getFullDateTime(essay.createTime)">
                    <n-icon><Calendar /></n-icon>
                    {{ formatTime(essay.createTime) }}
                  </span>
                </div>
                <NuxtLink :to="`/reading-essays/${essay.id}`" class="essay-detail-link">详情</NuxtLink>
              </div>
            </div>
          </div>

          <div class="load-more" v-if="hasMore">
            <n-button :loading="loadingMore" @click="loadMore">
              加载更多
            </n-button>
          </div>
        </div>
      </main>
    </template>
  </div>
</template>

<script setup lang="ts">
import { Folder, FileText, Calendar, MessageCircle } from '@vicons/tabler'
import type { ReadingEssaysAlbum, ReadingEssaysInfo } from '~/types'

definePageMeta({ layout: 'blank' })

const { siteName } = useRuntimeConfig().public

const route = useRoute()
const albumId = computed(() => Number(route.params.id))
const readingEssaysApi = useReadingEssaysApi()

const pageSize = 18

// --- 专辑详情 ---
const { data: albumData, pending, error } = await useAsyncData(
  `album-detail-${albumId.value}`,
  () => readingEssaysApi.getAlbumDetail(albumId.value) as Promise<ReadingEssaysAlbum>,
  { server: false }
)

// --- 随笔列表 ---
const pageNum = ref(1)
const essayList = ref<ReadingEssaysInfo[]>([])
const essayTotal = ref(0)
const loadingMore = ref(false)

const hasMore = computed(() => essayList.value.length < essayTotal.value)

const fetchEssays = async (page: number) => {
  loadingMore.value = true
  try {
    const res = await readingEssaysApi.getReadingEssaysList({
      pageNum: page,
      pageSize,
      albumId: albumId.value
    })
    if (page === 1) {
      essayList.value = res.rows || []
    } else {
      essayList.value.push(...(res.rows || []))
    }
    essayTotal.value = res.total || 0
    pageNum.value = page
  } catch (e) {
    console.error('获取随笔列表失败:', e)
  } finally {
    loadingMore.value = false
  }
}

const loadMore = () => {
  if (loadingMore.value || !hasMore.value) return
  fetchEssays(pageNum.value + 1)
}

// 监听专辑数据加载后获取随笔
watch(() => albumData.value?.id, (id) => {
  if (id) fetchEssays(1)
}, { immediate: true })

// SEO
useHead(() => ({
  title: albumData.value?.name ? `${albumData.value.name} - ${siteName}` : `专辑 - ${siteName}`,
  meta: [
    { name: 'description', content: albumData.value?.briefIntroduction || albumData.value?.name || '' }
  ]
}))
</script>

<style scoped>
.album-detail-page {
  height: 100vh;
  width: 800px;
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
}

.loading-state,
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex: 1;
}

/* --- 专辑头部 - 固定不滚动 --- */
.album-header {
  flex-shrink: 0;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.header-inner {
  padding: 20px 24px;
  display: flex;
  gap: 20px;
}

.header-cover {
  width: 140px;
  height: 100px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
}

.header-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.header-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.album-name {
  font-size: var(--text-2xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
  line-height: var(--leading-tight);
}

.album-introduce {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: var(--leading-relaxed);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.album-meta {
  margin-top: auto;
  display: flex;
  gap: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* --- 随笔列表标题 - 固定 --- */
.section-header {
  flex-shrink: 0;
  background: var(--color-surface-warm);
}

.section-header-inner {
  padding: 16px 24px 0;
}

.section-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

/* --- 随笔列表 - 滚动区域 --- */
.essay-scroll-area {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.essay-scroll-area::-webkit-scrollbar {
  width: 4px;
}

.essay-scroll-area::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 2px;
}

.essay-scroll-area::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.essay-section {
  padding: 16px 24px 24px;
}

.empty-state {
  padding: 40px 0;
  display: flex;
  justify-content: center;
}

.essay-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 随笔卡片 */
.essay-card {
  padding: 16px 20px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition: all var(--transition-base);
}

.essay-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.essay-detail-link {
  font-size: var(--text-sm);
  color: var(--color-primary);
  text-decoration: none;
}

.essay-detail-link:hover {
  text-decoration: underline;
}

.essay-content {
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  color: var(--color-ink);
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  cursor: pointer;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.essay-content:hover {
  color: var(--color-primary);
}

/* 作者和作品 */
.essay-source {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  font-style: italic;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
}

.source-sep {
  color: var(--color-ink-faint);
}

.source-author {
  color: var(--color-ink-light);
}

.source-work {
  color: var(--color-ink-muted);
}

/* 分类和标签 */
.essay-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.meta-badge {
  font-size: var(--text-xs);
  padding: 4px 10px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  color: var(--color-ink-light);
}

.dark .meta-badge {
  background: rgba(255, 255, 255, 0.08);
}

.meta-labels {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.label-tag {
  font-size: 12px;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 4px 10px;
  border-radius: 12px;
}

/* 底部 */
.essay-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.dark .essay-footer {
  border-top-color: rgba(255, 255, 255, 0.06);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.load-more {
  display: flex;
  justify-content: center;
  padding-top: 16px;
}

/* --- 响应式 --- */
@media (max-width: 640px) {
  .header-inner {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .header-cover {
    width: 100%;
    height: 160px;
  }

  .album-meta {
    justify-content: center;
  }
}
</style>
