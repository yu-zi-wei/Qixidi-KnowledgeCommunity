<template>
  <div class="md-editor-with-video">
    <ClientOnly>
      <!-- 视频操作栏 -->
      <div class="video-actions-bar">
        <span class="bar-title">扩展功能</span>
        <div class="bar-actions">
          <n-upload
            :custom-request="handleVideoUpload"
            :show-file-list="false"
            accept="video/*"
          >
            <n-button size="small" quaternary :loading="videoUploading">
              <template #icon>
                <n-icon :size="16">
                  <PlayerRecord />
                </n-icon>
              </template>
              上传视频
            </n-button>
          </n-upload>
          <n-button size="small" quaternary @click="showVideoLinkDialog = true">
            <template #icon>
              <n-icon :size="16">
                <LinkIcon />
              </n-icon>
            </template>
            插入第三方视频
          </n-button>
          <n-button size="small" quaternary @click="showGalleryDialog = true">
            <template #icon>
              <n-icon :size="16">
                <Photo />
              </n-icon>
            </template>
            多图排版
          </n-button>
          <n-upload
            :custom-request="handleAttachmentUpload"
            :show-file-list="false"
          >
            <n-button size="small" quaternary :loading="attachmentUploading">
              <template #icon>
                <n-icon :size="16">
                  <Paperclip />
                </n-icon>
              </template>
              上传附件
            </n-button>
          </n-upload>
        </div>
      </div>

      <MdEditor
        ref="mdEditorRef"
        v-model="content"
        :toolbars="editorToolbars"
        :theme="editorTheme"
        :placeholder="placeholder"
        class="markdown-editor"
        @onUploadImg="handleImageUpload"
      />
      <template #fallback>
        <div class="editor-loading">
          <n-spin size="medium" />
          <span>编辑器加载中...</span>
        </div>
      </template>
    </ClientOnly>

    <!-- 视频链接弹窗 -->
    <n-modal
      v-model:show="showVideoLinkDialog"
      preset="dialog"
      title="添加视频链接"
      positive-text="插入"
      negative-text="取消"
      @positive-click="handleInsertVideoLink"
    >
      <n-form :model="videoLinkForm" label-placement="left" label-width="80">
        <n-form-item label="视频链接" path="url">
          <n-input
            v-model:value="videoLinkForm.url"
            placeholder="支持 B站 / YouTube / 直链 (.mp4/.webm)"
          />
        </n-form-item>
        <n-form-item label="标题" path="title">
          <n-input
            v-model:value="videoLinkForm.title"
            placeholder="视频标题（可选）"
          />
        </n-form-item>
      </n-form>
      <div class="video-link-tips">
        <p>支持的格式：</p>
        <ul>
          <li>B站视频：https://www.bilibili.com/video/BVxxx</li>
          <li>YouTube：https://www.youtube.com/watch?v=xxx</li>
          <li>直链：.mp4 / .webm / .ogg / .mov</li>
        </ul>
      </div>
    </n-modal>

    <!-- 多图排版弹窗 -->
    <n-modal
      v-model:show="showGalleryDialog"
      preset="card"
      title="多图排版"
      style="width: 600px"
      :mask-closable="false"
    >
      <div class="gallery-dialog-content">
        <div class="gallery-option">
          <span class="gallery-label">列数</span>
          <n-radio-group v-model:value="galleryColumns" size="small">
            <n-radio-button :value="2">2 列</n-radio-button>
            <n-radio-button :value="3">3 列</n-radio-button>
            <n-radio-button :value="4">4 列</n-radio-button>
          </n-radio-group>
          <div style="flex: 1" />
          <n-upload
            :key="galleryKey"
            :custom-request="handleGalleryUpload"
            :show-file-list="false"
            accept="image/*"
            multiple
            :disabled="galleryUploading"
          >
            <n-button :disabled="galleryUploading">
              {{ galleryUploading ? '上传中...' : '选择图片上传' }}
            </n-button>
          </n-upload>
        </div>
        <div
          v-if="galleryUrls.length > 0"
          class="gallery-preview"
          :style="{ gridTemplateColumns: `repeat(${galleryColumns}, 1fr)` }"
        >
          <div v-for="(url, i) in galleryUrls" :key="i" class="gallery-preview-item">
            <img :src="url" class="gallery-preview-img" />
            <button class="gallery-preview-remove" @click="galleryUrls.splice(i, 1)">
              <n-icon :size="14"><X /></n-icon>
            </button>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="gallery-footer">
          <div v-if="galleryUploading" class="gallery-uploading-tip">
            <n-spin size="small" />
            <span>图片上传中，请稍候...</span>
          </div>
          <div class="gallery-footer-actions">
            <n-button @click="showGalleryDialog = false">取消</n-button>
            <n-button type="primary" @click="handleGalleryInsert">
              {{ galleryUrls.length > 0 ? `插入（${galleryUrls.length} 张）` : '插入空表格' }}
            </n-button>
          </div>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { PlayerRecord, Link as LinkIcon, Photo, Paperclip, X } from '@vicons/tabler'
import type { UploadSetCustomRequestOptions } from 'naive-ui'

const colorMode = useColorMode()
const editorTheme = computed(() => colorMode.value === 'dark' ? 'dark' : 'light')

interface Props {
  modelValue: string
  placeholder?: string
  toolbars?: string[] | null
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '开始编写内容...',
  toolbars: null
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const message = useMessage()
const ossApi = useOssApi()

const mdEditorRef = ref<InstanceType<typeof MdEditor>>()
const attachmentUploading = ref(false)

const showVideoLinkDialog = ref(false)
const videoUploading = ref(false)

// 多图排版状态
const showGalleryDialog = ref(false)
const galleryColumns = ref(2)
const galleryUrls = ref<string[]>([])
const galleryKey = ref(0)
const galleryUploadingCount = ref(0)
const galleryUploading = computed(() => galleryUploadingCount.value > 0)

const videoLinkForm = reactive({
  url: '',
  title: ''
})

// 双向绑定
const content = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  content.value = val
})

watch(content, (val) => {
  emit('update:modelValue', val)
})

// 默认工具栏（精简版）
const defaultToolbars = [
  'bold',
  'italic',
  'strikeThrough',
  '-',
  'title',
  'quote',
  '-',
  'unorderedList',
  'orderedList',
  '-',
  'codeRow',
  'code',
  '-',
  'link',
  'image',
  '-',
  'table',
  '=',
  'pageFullscreen',
  'fullscreen',
  'preview'
]

// 使用传入的工具栏或默认的
const editorToolbars = computed(() => props.toolbars ?? defaultToolbars)

// 图片上传
const handleImageUpload = async (files: File[], callback: (urls: string[]) => void) => {
  try {
    const urls: string[] = []
    for (const file of files) {
      const url = await ossApi.uploadFile(file)
      urls.push(url)
    }
    callback(urls)
  } catch (error) {
    console.error('图片上传失败:', error)
    message.error('图片上传失败，请重试')
    callback([])
  }
}

// 上传附件（在光标位置插入 URL）
const handleAttachmentUpload = async ({ file, onFinish, onError }: UploadSetCustomRequestOptions) => {
  attachmentUploading.value = true
  try {
    const url = await ossApi.uploadFile(file.file as File)
    if (mdEditorRef.value) {
      mdEditorRef.value.insert(() => ({
        targetValue: url,
        select: true
      }))
    } else {
      content.value += url
    }
    message.success('附件上传成功')
    onFinish()
  } catch (error) {
    console.error('附件上传失败:', error)
    message.error('附件上传失败，请重试')
    onError()
  } finally {
    attachmentUploading.value = false
  }
}

// 视频上传
const handleVideoUpload = async ({ file, onFinish, onError }: UploadSetCustomRequestOptions) => {
  videoUploading.value = true
  try {
    const url = await ossApi.uploadFile(file.file as File)

    const fileName = file.name || 'video.mp4'
    const videoSyntax = `\n[${fileName}](${url})\n`

    content.value += videoSyntax
    message.success('视频上传成功！')
    onFinish()
  } catch (error) {
    console.error('视频上传失败:', error)
    message.error('视频上传失败，请重试')
    onError()
  } finally {
    videoUploading.value = false
  }
}

// 插入视频链接
const handleInsertVideoLink = () => {
  let { url, title } = videoLinkForm

  if (!url) {
    message.warning('请输入视频链接')
    return false
  }

  // 解析 B站分享链接格式：【标题】 URL
  const bilibiliSharePattern = /^【(.+?)】\s*(https?:\/\/.+)/
  const match = url.match(bilibiliSharePattern)

  if (match) {
    // 从分享链接中提取标题和 URL
    const extractedTitle = match[1].trim()
    let extractedUrl = match[2].trim()

    // 清理 URL：移除 share_source 和 vd_source 参数
    try {
      const urlObj = new URL(extractedUrl)
      urlObj.searchParams.delete('share_source')
      urlObj.searchParams.delete('vd_source')
      extractedUrl = urlObj.toString()
    } catch {
      // URL 解析失败，保持原样
    }

    // 使用提取的标题（如果用户没有手动填写标题）
    const displayTitle = title || extractedTitle
    const videoSyntax = `\n[${displayTitle}](${extractedUrl})\n`

    content.value += videoSyntax

    videoLinkForm.url = ''
    videoLinkForm.title = ''

    message.success('视频链接已插入')
    return true
  }

  // 普通链接格式
  // 清理 URL：移除 share_source 和 vd_source 参数
  try {
    const urlObj = new URL(url)
    urlObj.searchParams.delete('share_source')
    urlObj.searchParams.delete('vd_source')
    url = urlObj.toString()
  } catch {
    // URL 解析失败，保持原样
  }

  const displayTitle = title || '视频'
  const videoSyntax = `\n[${displayTitle}](${url})\n`

  content.value += videoSyntax

  videoLinkForm.url = ''
  videoLinkForm.title = ''

  message.success('视频链接已插入')
  return true
}

// 多图排版上传
const handleGalleryUpload = async ({ file, onFinish, onError }: UploadSetCustomRequestOptions) => {
  galleryUploadingCount.value++
  try {
    const url = await ossApi.uploadFile(file.file as File)
    galleryUrls.value.push(url)
    onFinish()
  } catch (error) {
    console.error('图片上传失败:', error)
    message.error(`${file.name} 上传失败`)
    onError()
  } finally {
    galleryUploadingCount.value--
  }
}

// 多图排版插入
const handleGalleryInsert = () => {
  const cols = galleryColumns.value
  const urls = galleryUrls.value
  const rows: string[] = []

  if (urls.length === 0) {
    const cells = Array.from({ length: cols }, () => '<td><img src=""/></td>')
    rows.push(`<tr>\n${cells.join('\n')}\n</tr>`)
  } else {
    for (let i = 0; i < urls.length; i += cols) {
      const cells = urls.slice(i, i + cols).map(url => `<td><img src="${url}" /></td>`)
      while (cells.length < cols) cells.push('<td><img src=""/></td>')
      rows.push(`<tr>\n${cells.join('\n')}\n</tr>`)
    }
  }

  const tableHtml = `\n<table>\n${rows.join('\n')}\n</table>\n`
  content.value += tableHtml
  showGalleryDialog.value = false
  message.success(urls.length > 0
    ? `已插入 ${urls.length} 张图片（${cols}列排版）`
    : `已插入 ${cols} 列空表格`
  )
  return true
}

// 关闭弹窗时重置状态，key 递增强制 n-upload 重建
watch(showGalleryDialog, (val) => {
  if (!val) {
    galleryUrls.value = []
    galleryColumns.value = 2
    galleryUploadingCount.value = 0
    galleryKey.value++
  }
})

// 暴露方法
defineExpose({
  getContent: () => content.value,
  setContent: (val: string) => { content.value = val }
})
</script>

<style scoped>
.md-editor-with-video {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.video-actions-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-2) var(--space-3);
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
  border-bottom: none;
  border-radius: 8px 8px 0 0;
}

.bar-title {
  font-size: 12px;
  font-weight: 500;
  color: var(--color-ink-muted);
}

.bar-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.markdown-editor {
  flex: 1;
  min-height: 0;
  border: none;
}

.markdown-editor :deep(.md-editor) {
  height: 100%;
  border-radius: 0 0 8px 8px;
}

.editor-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  gap: var(--space-3);
  color: var(--color-ink-muted);
}

.video-link-tips {
  margin-top: var(--space-4);
  padding: var(--space-3);
  background: var(--color-surface-dim);
  border-radius: 8px;
  font-size: 12px;
  color: var(--color-ink-muted);
}

.video-link-tips p {
  margin: 0 0 var(--space-2) 0;
}

.video-link-tips ul {
  margin: 0;
  padding-left: var(--space-4);
}

.video-link-tips li {
  margin: var(--space-1) 0;
}

.gallery-dialog-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.gallery-option {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.gallery-label {
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  white-space: nowrap;
}

.gallery-preview {
  display: grid;
  gap: 8px;
  margin-top: var(--space-3);
}

.gallery-preview {
  display: grid;
  gap: 8px;
  margin-top: var(--space-3);
}

.gallery-preview-item {
  position: relative;
}

.gallery-preview-img {
  width: 100%;
  aspect-ratio: 4 / 3;
  object-fit: cover;
  border-radius: var(--radius-sm);
}

.gallery-preview-remove {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  cursor: pointer;
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.gallery-preview-item:hover .gallery-preview-remove {
  opacity: 1;
}

.gallery-uploading-tip {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.gallery-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.gallery-footer-actions {
  display: flex;
  gap: var(--space-3);
}
</style>
