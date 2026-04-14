<template>
  <div class="reading-essays-detail-page">
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
        <ReadingEssaysDetailContent :essay="essay">
          <template #actions>
            <n-button v-if="isOwner && !isMobile" quaternary size="small" @click="handleEdit" title="编辑">
              <template #icon>
                <n-icon><Edit /></n-icon>
              </template>
            </n-button>
            <n-button quaternary size="small" @click="copyShareLink" title="复制链接">
              <template #icon>
                <n-icon><Share /></n-icon>
              </template>
            </n-button>
          </template>
        </ReadingEssaysDetailContent>
      </article>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Share, Edit } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'
import { useEssayDrawerStore } from '~/stores/essayDrawer'

definePageMeta({
  layout: false,
  showTabBar: false
})

const route = useRoute()
const readingEssaysApi = useReadingEssaysApi()
const authStore = useAuthStore()
const essayDrawerStore = useEssayDrawerStore()
const message = useMessage()

const essayId = computed(() => route.params.id as string)

const isMobile = ref(false)
onMounted(() => {
  isMobile.value = window.innerWidth < 768
})

const isOwner = computed(() => {
  if (!essay.value || !authStore.user?.uuid) return false
  return essay.value.uid === authStore.user.uuid
})

const { data: essay, pending, error, refresh } = await useAsyncData(
  () => `reading-essays-detail-${essayId.value}`,
  () => readingEssaysApi.getReadingEssaysDetail(essayId.value)
)

useHead({
  title: () => essay.value?.content?.substring(0, 50) || '随笔详情'
})

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

const handleEdit = () => {
  if (!essay.value) return
  essayDrawerStore.openEdit(essay.value.id)
}

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
  background: var(--color-surface-warm);
}

/* 主内容区域 */
.detail-main {
  display: flex;
  justify-content: center;
  padding: 32px 24px;
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
  .detail-main {
    padding: 16px;
  }
}
</style>
