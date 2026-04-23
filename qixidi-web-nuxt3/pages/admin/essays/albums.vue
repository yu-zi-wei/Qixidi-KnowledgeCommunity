<template>
  <div class="admin-albums">
    <!-- 顶部操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索专辑名称..."
          clearable
        >
          <template #prefix>
            <n-icon><Search /></n-icon>
          </template>
        </n-input>
      </div>
      <n-button type="primary" @click="handleCreate">
        <template #icon>
          <n-icon><Plus /></n-icon>
        </template>
        新建专辑
      </n-button>
    </div>

    <!-- 列表 -->
    <div class="list-section">
      <div v-if="loading" class="loading-state">
        <n-spin size="large" />
      </div>

      <div v-else-if="!filteredList.length" class="empty-state">
        <CommonEmptyState :description="searchKeyword ? '未找到匹配的专辑' : '暂无专辑'">
          <template v-if="!searchKeyword" #extra>
            <n-button type="primary" @click="handleCreate">立即创建</n-button>
          </template>
        </CommonEmptyState>
      </div>

      <template v-else>
        <div class="list-meta">
          <span class="total-count">共 {{ filteredList.length }} 个专辑</span>
        </div>
        <div class="album-list">
          <div
            v-for="album in filteredList"
            :key="album.id"
            class="album-item"
          >
            <!-- 封面 -->
            <div class="album-cover" v-if="album.cover">
              <img :src="album.cover" :alt="album.name" />
            </div>
            <div class="album-cover cover-placeholder" v-else>
              <n-icon size="28"><Folder /></n-icon>
            </div>

            <!-- 信息 -->
            <div class="album-content">
              <h3 class="album-name">
                <a :href="`/reading-essays/album/${album.id}`" target="_blank" class="album-link">{{ album.name }}</a>
              </h3>
              <p class="album-desc" v-if="album.briefIntroduction">{{ album.briefIntroduction }}</p>
              <div class="album-footer">
                <div class="album-meta">
                  <span class="album-meta-item">
                    <n-icon><FileText /></n-icon>
                    {{ album.employSum || 0 }} 篇
                  </span>
                  <span class="album-meta-item" :title="getFullDateTime(album.createTime)">
                    <n-icon><Calendar /></n-icon>
                    {{ formatTime(album.createTime) }}
                  </span>
                  <span class="state-badge" :class="getStateClass(album.albumState)">
                    {{ getStateText(album.albumState) }}
                  </span>
                </div>
                <div class="album-actions">
                  <n-button text size="tiny" type="primary" @click="handleEdit(album)">编辑</n-button>
                  <n-popconfirm @positive-click="handleDelete(album.id)">
                    <template #trigger>
                      <n-button text size="tiny" type="error">删除</n-button>
                    </template>
                    确定要删除这个专辑吗？
                  </n-popconfirm>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>

    <!-- 新建/编辑弹窗 -->
    <n-modal
      v-model:show="showModal"
      preset="card"
      :title="editingId ? '编辑专辑' : '新建专辑'"
      style="width: 480px"
      :mask-closable="!formLoading"
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
          <div class="cover-upload">
            <div class="cover-preview" v-if="formData.cover">
              <img :src="formData.cover" alt="封面预览" />
              <n-button class="remove-btn" circle size="tiny" @click="formData.cover = ''">
                <template #icon><n-icon><X /></n-icon></template>
              </n-button>
            </div>
            <n-upload v-else :custom-request="customUploadRequest" :show-file-list="false">
              <n-upload-dragger>
                <div class="upload-hint">
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
        <div class="modal-footer">
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" :loading="formLoading" @click="handleConfirm">
            {{ editingId ? '保存' : '创建' }}
          </n-button>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { Plus, Folder, FileText, Calendar, CloudUpload, X, Search } from '@vicons/tabler'
import type { FormInst, FormRules, UploadCustomRequestOptions } from 'naive-ui'
import type { ReadingEssaysAlbum } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const message = useMessage()
const albumApi = useDictumAlbumApi()
const ossApi = useOssApi()

// 列表
const albumList = ref<ReadingEssaysAlbum[]>([])
const loading = ref(false)
const searchKeyword = ref('')

// 前端搜索过滤
const filteredList = computed(() => {
  if (!searchKeyword.value) return albumList.value
  const kw = searchKeyword.value.toLowerCase()
  return albumList.value.filter(a => a.name.toLowerCase().includes(kw))
})

// 弹窗
const showModal = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInst | null>(null)
const formLoading = ref(false)

const formData = ref({
  name: '',
  briefIntroduction: '',
  cover: '',
  albumState: 1
})

const formRules: FormRules = {
  name: { required: true, message: '请输入专辑名称' }
}

const stateOptions = [
  { label: '公开', value: 1 },
  { label: '私有', value: 2 },
  { label: '关注可看', value: 3 }
]

// 获取列表
const fetchList = async () => {
  loading.value = true
  try {
    const result = await albumApi.getList()
    albumList.value = result.rows || []
  } catch {
  } finally {
    loading.value = false
  }
}

// 新建
const handleCreate = () => {
  editingId.value = null
  formData.value = { name: '', briefIntroduction: '', cover: '', albumState: 1 }
  showModal.value = true
}

// 编辑
const handleEdit = (album: ReadingEssaysAlbum) => {
  editingId.value = album.id
  formData.value = {
    name: album.name,
    briefIntroduction: album.briefIntroduction || '',
    cover: album.cover || '',
    albumState: album.albumState || 1
  }
  showModal.value = true
}

// 删除
const handleDelete = async (id: number) => {
  try {
    await albumApi.delete(id)
    message.success('删除成功')
    fetchList()
  } catch {}
}

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

// 提交
const handleConfirm = async () => {
  try { await formRef.value?.validate() } catch { return }

  formLoading.value = true
  try {
    if (editingId.value) {
      await albumApi.update({ id: editingId.value, ...formData.value })
      message.success('更新成功')
    } else {
      await albumApi.create(formData.value)
      message.success('创建成功')
    }
    showModal.value = false
    fetchList()
  } catch {
  } finally {
    formLoading.value = false
  }
}

// 状态文案
const getStateText = (state?: number) => {
  switch (state) {
    case 1: return '公开'
    case 2: return '私有'
    case 3: return '关注可看'
    default: return '未知'
  }
}

const getStateClass = (state?: number) => {
  switch (state) {
    case 1: return 'state-public'
    case 2: return 'state-private'
    case 3: return 'state-follow'
    default: return ''
  }
}

onMounted(() => fetchList())
</script>

<style scoped>
.admin-albums {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
  overflow: hidden;
}

/* 顶部操作区 */
.header-section {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.search-bar {
  display: flex;
  gap: var(--space-3);
  flex: 1;
}

.search-bar .n-input {
  flex: 1;
}

/* 列表区域 */
.list-section {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-4);
  box-shadow: var(--shadow-sm);
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-meta {
  flex-shrink: 0;
  padding-bottom: var(--space-3);
  margin-bottom: var(--space-3);
  border-bottom: 1px solid var(--color-border-light);
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.album-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.album-list::-webkit-scrollbar {
  width: 6px;
}

.album-list::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.album-list::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

/* 专辑卡片 */
.album-item {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-4);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  transition: all var(--transition-base);
}

.album-item:hover {
  border-color: var(--color-primary-light);
  box-shadow: var(--shadow-md);
}

/* 封面 */
.album-cover {
  width: 100px;
  height: 72px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
}

.album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

/* 信息 */
.album-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.album-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.album-link {
  color: inherit;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.album-link:hover {
  color: var(--color-primary);
}

.album-desc {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.album-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
  padding-top: var(--space-2);
  border-top: 1px solid var(--color-border-light);
}

.album-meta {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.album-meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.state-badge {
  padding: 1px 6px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
}

.state-public {
  background: rgba(34, 197, 94, 0.1);
  color: #22c55e;
}

.state-private {
  background: rgba(107, 114, 128, 0.1);
  color: #6b7280;
}

.state-follow {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.album-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

/* 封面上传 */
.cover-upload {
  width: 100%;
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 120px;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-preview .remove-btn {
  position: absolute;
  top: 8px;
  right: 8px;
}

.upload-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: var(--color-ink-muted);
}

.upload-hint p {
  margin: 6px 0 0;
  font-size: var(--text-sm);
}

/* 弹窗底部 */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
}
</style>
