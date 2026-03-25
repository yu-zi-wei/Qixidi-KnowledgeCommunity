<template>
  <div class="reading-essays-detail-page">
    <!-- 顶部操作栏 -->
    <header class="detail-header">
      <n-button quaternary @click="goBack">
        <template #icon>
          <n-icon><ArrowLeft /></n-icon>
        </template>
        返回
      </n-button>
      <div class="header-actions">
        <n-button quaternary size="small" @click="copyShareLink" title="复制链接">
          <template #icon>
            <n-icon><Share /></n-icon>
          </template>
        </n-button>
      </div>
    </header>

    <!-- 内容区域 -->
    <main class="detail-main">
      <!-- 加载状态 -->
      <div v-if="pending" class="loading-state">
        <n-spin size="large" />
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <p>加载失败</p>
        <n-button @click="refresh">重试</n-button>
      </div>

      <!-- 随笔详情 -->
      <article v-else-if="essay" class="detail-card">
        <ReadingEssaysDetailContent :essay="essay" />
      </article>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Share } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'

definePageMeta({
  layout: false,
  showTabBar: false
})

const route = useRoute()
const router = useRouter()
const readingEssaysApi = useReadingEssaysApi()
const message = useMessage()

// 使用 computed 获取路由参数
const essayId = computed(() => route.params.id as string)

// 获取随笔详情
const { data: essay, pending, error, refresh } = await useAsyncData(
  () => `reading-essays-detail-${essayId.value}`,
  () => readingEssaysApi.getReadingEssaysDetail(essayId.value)
)

// SEO
useHead({
  title: () => essay.value?.content?.substring(0, 50) || '随笔详情'
})

// 返回列表页
const goBack = () => {
  router.push('/reading-essays')
}

// 复制分享链接
const copyShareLink = async () => {
  if (!essay.value) return
  const url = `${window.location.origin}/reading-essays/${essay.value.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}

// 监听路由参数变化
watch(() => route.params.id, async (newId, oldId) => {
  if (newId !== oldId) {
    await refresh()
    window.scrollTo({ top: 0, behavior: 'instant' })
  }
})
</script>

<style scoped>
.reading-essays-detail-page {
  min-height: 100vh;
  background: var(--color-surface);
}

/* 顶部操作栏 */
.detail-header {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--color-border-light);
}

:global(.dark) .detail-header {
  background: rgba(18, 18, 18, 0.9);
}

.header-actions {
  display: flex;
  gap: 8px;
}

/* 主内容区域 */
.detail-main {
  display: flex;
  justify-content: center;
  padding: 40px 24px;
}

.detail-card {
  max-width: 800px;
  width: 100%;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  overflow: hidden;
}

/* 加载/错误状态 */
.loading-state,
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 16px;
  color: var(--color-ink-muted);
}

/* 响应式 */
@media (max-width: 768px) {
  .detail-header {
    padding: 12px 16px;
  }

  .detail-main {
    padding: 20px 16px;
  }
}
</style>
