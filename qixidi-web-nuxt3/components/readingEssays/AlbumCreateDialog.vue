<template>
  <n-modal
    :show="show"
    preset="card"
    title="新建专辑"
    style="width: 480px"
    :mask-closable="!formLoading"
    @update:show="emit('update:show', $event)"
  >
    <n-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-placement="left"
      require-mark-placement="right"
    >
      <n-form-item label="专辑名称" path="name">
        <n-input v-model:value="formData.name" placeholder="请输入专辑名称" maxlength="50" />
      </n-form-item>
      <n-form-item label="专辑简介" path="briefIntroduction">
        <n-input
          v-model:value="formData.briefIntroduction"
          type="textarea"
          placeholder="请输入专辑简介"
          :rows="3"
          maxlength="200"
          show-count
        />
      </n-form-item>
      <n-form-item label="封面图" path="cover">
        <div class="album-cover-upload">
          <div class="album-cover-preview" v-if="formData.cover">
            <img :src="formData.cover" alt="封面预览" />
            <div class="album-cover-mask">
              <div class="album-cover-actions">
                <n-button size="small" @click="formData.cover = ''">
                  <template #icon><n-icon><X /></n-icon></template>
                  删除
                </n-button>
                <n-upload :custom-request="customUploadRequest" :show-file-list="false">
                  <n-button size="small">
                    <template #icon><n-icon><Upload /></n-icon></template>
                    替换
                  </n-button>
                </n-upload>
              </div>
            </div>
          </div>
          <n-upload v-else :custom-request="customUploadRequest" :show-file-list="false">
            <n-upload-dragger>
              <div class="album-upload-hint">
                <n-icon size="28" :depth="2"><CloudUpload /></n-icon>
                <p>点击或拖拽上传封面</p>
              </div>
            </n-upload-dragger>
          </n-upload>
        </div>
      </n-form-item>
      <n-form-item label="专辑状态" path="albumState">
        <n-select
          v-model:value="formData.albumState"
          :options="stateOptions"
        />
      </n-form-item>
    </n-form>
    <template #footer>
      <div class="album-dialog-footer">
        <n-button @click="emit('update:show', false)">取消</n-button>
        <n-button type="primary" :loading="formLoading" @click="handleConfirm">
          创建
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { FormInst, FormRules, UploadCustomRequestOptions } from 'naive-ui'
import { X, Upload, CloudUpload } from '@vicons/tabler'

const props = defineProps<{
  show: boolean
}>()

const emit = defineEmits<{
  'update:show': [value: boolean]
  /** 创建成功，返回新专辑 id */
  success: [id: number]
}>()

const message = useMessage()
const albumApi = useDictumAlbumApi()
const ossApi = useOssApi()

const formRef = ref<FormInst | null>(null)
const formLoading = ref(false)

const defaultForm = () => ({
  name: '',
  briefIntroduction: '',
  cover: '',
  albumState: 1
})
const formData = ref(defaultForm())

const formRules: FormRules = {
  name: { required: true, message: '请输入专辑名称' }
}

const stateOptions = [
  { label: '公开', value: 1 },
  { label: '私有', value: 2 },
  { label: '关注可看', value: 3 }
]

// 每次打开重置表单
watch(() => props.show, (show) => {
  if (show) formData.value = defaultForm()
})

// 上传封面
const customUploadRequest = async (options: UploadCustomRequestOptions) => {
  const { file, onFinish, onError, onProgress } = options
  try {
    if (!file.file) { onError(); return }
    const url = await ossApi.uploadFile(file.file, (percent) => onProgress({ percent }))
    formData.value.cover = url
    onFinish()
    message.success('上传成功')
  } catch {
    onError()
  }
}

// 提交创建
const handleConfirm = async () => {
  try { await formRef.value?.validate() } catch { return }

  formLoading.value = true
  try {
    const id = await albumApi.create(formData.value)
    message.success('创建成功')
    emit('update:show', false)
    emit('success', id)
  } catch {
  } finally {
    formLoading.value = false
  }
}
</script>

<style scoped>
.album-cover-upload {
  width: 100%;
}

.album-cover-preview {
  position: relative;
  width: 100%;
  height: 120px;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.album-cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.album-cover-mask {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.album-cover-preview:hover .album-cover-mask {
  opacity: 1;
}

.album-cover-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.album-cover-mask :deep(.n-button) {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.15);
}

.album-cover-mask :deep(.n-button:hover) {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.7);
}

.album-upload-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: var(--color-ink-muted);
}

.album-upload-hint p {
  margin: 6px 0 0;
  font-size: var(--text-sm);
}

.album-dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
}
</style>
