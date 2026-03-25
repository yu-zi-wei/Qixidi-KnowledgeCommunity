<template>
  <div class="edit-page">
    <div class="edit-header">
      <div class="header-left">
        <h1 class="page-title">编辑时光小记</h1>
      </div>
      <div class="header-right">
        <n-button text @click="handleBack">
          <template #icon>
            <ArrowLeft class="icon" />
          </template>
          返回
        </n-button>
      </div>
    </div>

    <div class="edit-container">
      <TimeNotesEditor
        ref="editorRef"
        :time-note-id="timeNoteId"
        :initial-data="initialTimeNote"
        @save="handleSaveSuccess"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ArrowLeft } from '@vicons/tabler'
import type { TimeNotesInfo } from '~/types'

definePageMeta({
  layout: 'admin',
  middleware: 'auth'
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const dialog = useDialog()

const timeNotesApi = useTimeNotesAdminApi()

const editorRef = ref<InstanceType<typeof TimeNotesEditor>>()

// 时光小记 ID
const timeNoteId = computed(() => route.params.id as string)

// 加载时光小记数据
const initialTimeNote = ref<Partial<TimeNotesInfo>>({})

const { data: timeNoteDetail } = await useAsyncData(
  `time-note-${timeNoteId.value}`,
  () => timeNotesApi.getTimeNotesDetail(Number(timeNoteId.value))
)

watch(() => timeNoteDetail.value, (newData) => {
  if (newData) {
    initialTimeNote.value = {
      id: newData.id,
      title: newData.title || '',
      content: newData.content || '',
      recordTime: newData.recordTime || ''
    }
  }
}, { immediate: true })

// 保存成功
const handleSaveSuccess = () => {
  message.success('保存成功！')
  setTimeout(() => {
    router.push('/admin/time_notes')
  }, 500)
}

// 返回
const handleBack = () => {
  router.back()
}

// 路由离开前确认
onBeforeRouteLeave((to, from, next) => {
  if (editorRef.value?.hasChanges()) {
    dialog.warning({
      title: '确认离开',
      content: '您有未保存的内容，确定要离开吗？',
      positiveText: '离开',
      negativeText: '取消',
      onPositiveClick: () => next(),
      onNegativeClick: () => next(false)
    })
  } else {
    next()
  }
})
</script>

<style scoped>
.edit-page {
  min-height: 100vh;
  background: var(--color-surface);
}

.edit-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-5) var(--space-6);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.page-title {
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.icon {
  width: 18px;
  height: 18px;
  stroke-width: 1.5;
}

.edit-container {
  max-width: 900px;
  margin: 0 auto;
  padding: var(--space-6);
}

@media (max-width: 768px) {
  .edit-container {
    padding: var(--space-4);
  }

  .edit-header {
    padding: var(--space-4) var(--space-4);
  }

  .page-title {
    font-size: var(--text-lg);
  }
}
</style>
