<template>
  <div class="time-notes-detail-page">
    <!-- 顶部操作栏 -->
    <header class="detail-header">
      <n-button quaternary @click="goBack">
        <template #icon>
          <n-icon><ArrowLeft /></n-icon>
        </template>
        返回
      </n-button>
      <div class="header-actions">
        <n-button v-if="isOwner" quaternary size="small" @click="handleEdit" title="编辑">
          <template #icon>
            <n-icon><Edit /></n-icon>
          </template>
        </n-button>
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
        <n-button @click="handleRefresh">重试</n-button>
      </div>

      <!-- 时光小记详情 -->
      <article v-else-if="note" class="detail-card">
        <TimeNotesDetailContent :note="note" />
      </article>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Share, Edit } from '@vicons/tabler'
import type { TimeNotesInfo } from '~/types'

definePageMeta({
  layout: false,
  showTabBar: false
})

const route = useRoute()
const router = useRouter()
const timeNotesApi = useTimeNotesApi()
const authStore = useAuthStore()
const message = useMessage()

// 使用 computed 获取路由参数
const noteId = computed(() => route.params.id as string)

// 是否是当前用户的时光小记
const isOwner = computed(() => {
  if (!note.value || !authStore.user?.uuid) return false
  return note.value.uid === authStore.user.uuid
})

// 获取时光小记详情
const { data: note, pending, error, refresh } = await useAsyncData(
  () => `time-notes-detail-${noteId.value}`,
  () => timeNotesApi.getTimeNotesDetail(Number(noteId.value))
)

// SEO
useHead({
  title: () => note.value?.title || note.value?.content?.substring(0, 50) || '时光小记详情'
})

// 返回列表页
const goBack = () => {
  router.push('/time_notes')
}

// 复制分享链接
const copyShareLink = async () => {
  if (!note.value) return
  const url = `${window.location.origin}/time_notes/${note.value.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}

// 编辑时光小记
const handleEdit = () => {
  if (!note.value) return
  // 跳转到编辑页面
  router.push(`/write/note/${note.value.id}`)
}

// 重试加载
const handleRefresh = () => {
  refresh()
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
.time-notes-detail-page {
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
