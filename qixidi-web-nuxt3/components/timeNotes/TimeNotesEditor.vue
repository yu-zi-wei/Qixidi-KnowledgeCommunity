<template>
  <div class="time-notes-editor">
    <!-- 标题 -->
    <div class="editor-section">
      <label class="form-label">标题</label>
      <n-input
        v-model:value="form.title"
        placeholder="给这篇记录起个标题..."
        :maxlength="100"
        clearable
        size="large"
      />
    </div>

    <!-- 内容编辑器 -->
    <div class="editor-section flex-1">
      <label class="form-label">内容</label>
      <div class="content-editor">
        <CommonMdEditorWithVideo
          ref="editorRef"
          v-model="form.content"
          placeholder="开始记录这一刻..."
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted } from 'vue'
import type { TimeNotesInfo, TimeNotesBo } from '~/types'

interface Props {
  timeNoteId?: string
  initialData?: Partial<TimeNotesInfo>
  recordTime?: string
}

const props = withDefaults(defineProps<Props>(), {
  recordTime: () => new Date().toISOString().split('T')[0]
})

const emit = defineEmits<{
  'update:recordTime': [value: string]
  save: []
}>()

const message = useMessage()
const timeNotesApi = useTimeNotesAdminApi()

const editorRef = ref<InstanceType<typeof CommonMdEditorWithVideo>>()
const originalContent = ref('')

// 表单数据
const form = reactive({
  title: '',
  content: ''
})

// 是否编辑模式
const isEdit = computed(() => !!props.timeNoteId)

// 是否有修改
const hasChanges = () => {
  return form.title !== (props.initialData?.title || '') ||
         form.content !== (props.initialData?.content || '')
}

// 初始化表单
onMounted(() => {
  if (props.initialData) {
    form.title = props.initialData.title || ''
    form.content = props.initialData.content || ''
    originalContent.value = form.content
  }
})

// 监听初始数据变化
watch(() => props.initialData, (newData) => {
  if (newData) {
    form.title = newData.title || ''
    form.content = newData.content || ''
    originalContent.value = form.content
  }
}, { deep: true })

// 保存（供父组件调用）
const save = async () => {
  if (!form.title.trim()) {
    message.warning('请输入标题')
    return false
  }

  try {
    const data: TimeNotesBo = {
      title: form.title,
      content: form.content,
      recordTime: props.recordTime
    }

    if (isEdit.value && props.timeNoteId) {
      data.id = Number(props.timeNoteId)
      await timeNotesApi.updateTimeNotes(data)
    } else {
      await timeNotesApi.addTimeNotes(data)
    }

    emit('save')
    return true
  } catch (error) {
    console.error('保存失败:', error)
    return false
  }
}

// 暴露方法给父组件
defineExpose({
  hasChanges,
  save
})
</script>

<style scoped>
.time-notes-editor {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
  height: 100%;
  min-height: 0;
  padding: 24px;
  overflow: hidden;
}

.editor-section {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  flex-shrink: 0;
}

.editor-section.flex-1 {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.form-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-ink-light);
}

.content-editor {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.content-editor :deep(.md-editor-with-video) {
  flex: 1;
  min-height: 0;
}

@media (max-width: 768px) {
  .time-notes-editor {
    padding: 16px;
  }
}
</style>
