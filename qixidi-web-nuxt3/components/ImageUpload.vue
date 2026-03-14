<template>
  <div class="image-upload">
    <div
      v-if="!modelValue"
      class="upload-area"
      :class="{ 'drag-over': isDragOver }"
      @click="handleClick"
      @dragenter.prevent="isDragOver = true"
      @dragleave.prevent="isDragOver = false"
      @dragover.prevent
      @drop.prevent="handleDrop"
    >
      <div class="upload-icon">📷</div>
      <div class="upload-text">点击或拖拽上传</div>
      <div class="upload-hint">支持 JPG、PNG，最大 5MB</div>
    </div>

    <div v-else class="preview-area">
      <img :src="modelValue" alt="封面图" class="preview-image" />
      <div class="preview-mask">
        <button class="preview-btn" @click="handleRemove">删除</button>
        <button class="preview-btn" @click="handleReplace">替换</button>
      </div>
      <div v-if="uploadProgress > 0 && uploadProgress < 100" class="progress-bar">
        <div class="progress-fill" :style="{ width: uploadProgress + '%' }"></div>
      </div>
    </div>

    <input
      ref="fileInputRef"
      type="file"
      accept="image/jpeg,image/png,image/webp"
      style="display: none"
      @change="handleFileChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  modelValue?: string
  maxSize?: number // 字节
}

const props = withDefaults(defineProps<Props>(), {
  maxSize: 5 * 1024 * 1024 // 默认 5MB
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const ossApi = useOssApi()
const message = useMessage()

const fileInputRef = ref<HTMLInputElement>()
const isDragOver = ref(false)
const uploadProgress = ref(0)

const handleClick = () => {
  fileInputRef.value?.click()
}

const handleFileChange = (e: Event) => {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    uploadFile(file)
  }
}

const handleDrop = (e: DragEvent) => {
  isDragOver.value = false
  const file = e.dataTransfer?.files[0]
  if (file) {
    uploadFile(file)
  }
}

const uploadFile = async (file: File) => {
  // 校验文件类型
  if (!['image/jpeg', 'image/png', 'image/webp'].includes(file.type)) {
    message.error('仅支持 JPG、PNG、WEBP 格式')
    return
  }

  // 校验文件大小
  if (file.size > props.maxSize) {
    message.error(`图片大小不能超过 ${(props.maxSize / 1024 / 1024).toFixed(0)}MB`)
    return
  }

  try {
    uploadProgress.value = 0
    const url = await ossApi.uploadFile(file, (percent) => {
      uploadProgress.value = percent
    })
    emit('update:modelValue', url)
    message.success('上传成功')
  } catch (error: any) {
    message.error(error.message || '上传失败')
  } finally {
    uploadProgress.value = 0
  }
}

const handleRemove = () => {
  emit('update:modelValue', '')
}

const handleReplace = () => {
  fileInputRef.value?.click()
}
</script>

<style scoped>
.image-upload {
  width: 100%;
}

.upload-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-3);
  padding: var(--space-8);
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface-dim);
  cursor: pointer;
  transition: all var(--transition-base);
}

.upload-area:hover,
.upload-area.drag-over {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.upload-icon {
  font-size: 32px;
  opacity: 0.6;
}

.upload-text {
  font-size: var(--text-sm);
  color: var(--color-ink);
}

.upload-hint {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.preview-area {
  position: relative;
  width: 100%;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.preview-mask {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-3);
  background: rgba(0, 0, 0, 0.5);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.preview-area:hover .preview-mask {
  opacity: 1;
}

.preview-btn {
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-sm);
  color: white;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-base);
}

.preview-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.progress-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: rgba(0, 0, 0, 0.3);
}

.progress-fill {
  height: 100%;
  background: var(--color-primary);
  transition: width 0.3s ease;
}
</style>
