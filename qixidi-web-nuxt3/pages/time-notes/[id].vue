<template>
  <div class="time-notes-detail-page">
    <!-- 内容区域 -->
    <main class="detail-main">
      <div class="detail-column">
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
          <div class="detail-actions">
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
          <TimeNotesDetailContent :note="note" />
        </article>

        <!-- 评论区 -->
        <ThreadedCommentSection
          v-if="note"
          class="detail-comment"
          :biz-id="noteId"
          :biz-uid="note.uid"
          :fetch-list="fetchCommentList"
          :submit="submitComment"
          :remove="removeComment"
        />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Share, Edit } from '@vicons/tabler'
import type { CommentSubmitPayload, PageQuery, TimeNotesInfo } from '~/types'

definePageMeta({
  layout: false,
  showTabBar: false
})

const route = useRoute()
const router = useRouter()
const timeNotesApi = useTimeNotesApi()
const timeNotesCommentApi = useTimeNotesCommentApi()
const authStore = useAuthStore()
const message = useMessage()

const noteId = computed(() => route.params.id as string)

const isOwner = computed(() => {
  if (!note.value || !authStore.user?.uuid) return false
  return note.value.uid === authStore.user.uuid
})

const { data: note, pending, error, refresh } = await useAsyncData(
  () => `time-notes-detail-${noteId.value}`,
  () => timeNotesApi.getTimeNotesDetail(Number(noteId.value))
)

useHead({
  title: () => note.value?.title || note.value?.content?.substring(0, 50) || '时光小记详情'
})

const copyShareLink = async () => {
  if (!note.value) return
  const url = `${window.location.origin}/time-notes/${note.value.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}

const handleEdit = () => {
  if (!note.value) return
  router.push(`/write/note/${note.value.id}`)
}

const handleRefresh = () => {
  refresh()
}

// 评论区数据注入（业务 id 通过闭包组装，组件本身业务无关）
const fetchCommentList = (pageQuery: PageQuery) =>
  timeNotesCommentApi.getCommentList(noteId.value, pageQuery)

const submitComment = (payload: CommentSubmitPayload) =>
  timeNotesCommentApi.addComment({ timeNotesId: noteId.value, ...payload })

const removeComment = (id: string | number) =>
  timeNotesCommentApi.deleteComment(id)

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
  background: var(--color-surface-warm);
}

/* 主内容区域 */
.detail-main {
  display: flex;
  justify-content: center;
  padding: 32px 24px;
}

.detail-column {
  width: 100%;
  max-width: 800px;
}

.detail-comment {
  margin-top: var(--space-4);
}

.detail-card {
  max-width: 800px;
  width: 100%;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  overflow: hidden;
  position: relative;
}

.detail-actions {
  position: absolute;
  top: 16px;
  right: 16px;
  display: flex;
  gap: 4px;
  z-index: 10;
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
