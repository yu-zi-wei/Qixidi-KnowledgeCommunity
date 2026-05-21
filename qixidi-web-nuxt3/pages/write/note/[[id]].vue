<template>
  <div class="write-note-page">
    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左侧：编辑器 -->
      <div class="editor-area">
        <TimeNotesEditor
          ref="editorRef"
          :time-note-id="noteId"
          :initial-data="initialNoteData"
          :record-time="recordTime"
          @save="handleSave"
        />
      </div>

      <!-- 右侧：配置面板 -->
      <div class="config-panel">
        <div class="config-header">
          <h3 class="config-title">发布设置</h3>
          <n-button text @click="handleBack" class="back-btn">
            <template #icon>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="19" y1="12" x2="5" y2="12"></line>
                <polyline points="12 19 5 12 12 5"></polyline>
              </svg>
            </template>
            返回
          </n-button>
        </div>

        <div class="config-content">
          <!-- 记录时间 -->
          <div class="config-section">
            <label class="section-label">记录时间</label>
            <n-date-picker
              v-model:formatted-value="recordTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              class="date-picker"
            />
            <p class="section-hint">可以选择过去的日期来补记</p>
          </div>

          <!-- 提示信息 -->
          <div class="tip-card">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="16" x2="12" y2="12"></line>
              <line x1="12" y1="8" x2="12.01" y2="8"></line>
            </svg>
            <div class="tip-content">
              <p class="tip-title">时光小记</p>
              <p class="tip-desc">记录生活中的点滴，支持 Markdown 格式</p>
            </div>
          </div>
        </div>

        <!-- 底部操作按钮 -->
        <div class="config-footer">
          <n-button type="primary" @click="handlePublish" :loading="publishing" block>
            {{ noteId ? '保存修改' : '发布' }}
          </n-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesInfo } from '~/types'

definePageMeta({
  layout: 'editor',
  middleware: 'creator'
})

useHead({ title: '时光小记' })

const route = useRoute()
const router = useRouter()
const message = useMessage()
const timeNotesApi = useTimeNotesAdminApi()

// 获取笔记 ID（不存在则为新增模式）
const noteId = computed(() => {
  const id = route.params.id
  return id && id !== 'new' ? (id as string) : undefined
})

// 记录时间
const recordTime = ref(new Date().toISOString().split('T')[0])

// 初始数据
const initialNoteData = ref<Partial<TimeNotesInfo>>({})

// 加载笔记详情（编辑模式）
if (noteId.value) {
  const { data: noteDetail } = await useAsyncData(
    `time-note-${noteId.value}`,
    () => timeNotesApi.getTimeNotesDetail(Number(noteId.value))
  )

  if (noteDetail.value) {
    initialNoteData.value = noteDetail.value
    recordTime.value = noteDetail.value.recordTime?.split('T')[0] || new Date().toISOString().split('T')[0]
  }
}

// 编辑器引用
const editorRef = ref<InstanceType<typeof TimeNotesEditor>>()
const publishing = ref(false)

// 发布
const handlePublish = async () => {
  if (!editorRef.value) return

  publishing.value = true
  try {
    const success = await editorRef.value.save()
    if (success) {
      message.success(noteId.value ? '保存成功' : '发布成功')
      setTimeout(() => {
        navigateTo('/time-notes')
      }, 500)
    }
  } finally {
    publishing.value = false
  }
}

// 保存成功回调
const handleSave = () => {
  navigateTo('/time-notes')
}

// 返回
const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.write-note-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  overflow: hidden;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  overflow: hidden;
  min-height: 0;
}

/* 左侧编辑器 */
.editor-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
}

.editor-area :deep(.time-notes-editor) {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.editor-area :deep(.time-notes-editor),
.editor-area :deep(.editor-section),
.editor-area :deep(.content-editor),
.editor-area :deep(.md-editor-with-video) {
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.editor-area :deep(.editor-section.flex-1) {
  flex: 1;
}

.editor-area :deep(.md-editor) {
  height: 100%;
}

/* 右侧配置面板 */
.config-panel {
  width: 380px;
  min-width: 380px;
  display: flex;
  flex-direction: column;
  background: #fafafa;
  border-left: 1px solid var(--color-border-light);
}

.dark .config-panel {
  background: rgba(0, 0, 0, 0.02);
}

.config-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.config-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-ink-muted);
}

.back-btn svg {
  width: 16px;
  height: 16px;
}

.back-btn:hover {
  color: var(--color-ink);
}

.config-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 配置区块 */
.config-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.section-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink);
}

.section-hint {
  font-size: 12px;
  color: var(--color-ink-muted);
  margin: 0;
}

.date-picker {
  width: 100%;
}

/* 提示卡片 */
.tip-card {
  display: flex;
  gap: 12px;
  padding: 14px;
  background: var(--color-primary-light);
  border-radius: var(--radius-md);
}

.tip-card svg {
  width: 18px;
  height: 18px;
  color: var(--color-primary);
  flex-shrink: 0;
  margin-top: 1px;
}

.tip-content {
  flex: 1;
}

.tip-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-ink);
  margin: 0 0 2px;
}

.tip-desc {
  font-size: 12px;
  color: var(--color-ink-light);
  margin: 0;
  line-height: 1.4;
}

/* 底部操作按钮 */
.config-footer {
  padding: 16px 20px;
  border-top: 1px solid var(--color-border-light);
  background: var(--color-surface);
  flex-shrink: 0;
  display: flex;
  justify-content: flex-end;
}

.config-footer .n-button {
  width: 50%;
}

/* 响应式 */
@media (max-width: 768px) {
  .config-panel {
    display: none;
  }
}
</style>
