<template>
  <div class="admin-columns">
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

    <!-- 专辑列表 -->
    <div class="list-section">
      <!-- 加载状态 -->
      <div v-if="pending" class="loading-state">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!filteredList.length" class="empty-state">
        <n-empty :description="searchKeyword ? '未找到匹配的专辑' : '暂无专辑'">
          <template v-if="!searchKeyword" #extra>
            <n-button type="primary" @click="handleCreate">立即创建</n-button>
          </template>
        </n-empty>
      </div>

      <!-- 专辑列表 -->
      <template v-else>
        <div class="list-header">
          <span class="total-count">共 {{ filteredList.length }} 个专辑</span>
        </div>
        <div class="special-list-wrapper">
          <div class="special-list">
            <div
              v-for="special in filteredList"
              :key="special.id"
              class="special-item"
            >
            <!-- 封面图 -->
            <div class="special-cover" v-if="special.cover">
              <img :src="special.cover" :alt="special.specialName" />
            </div>
            <div class="special-cover cover-placeholder" v-else>
              <n-icon size="32"><Folder /></n-icon>
            </div>

            <!-- 专辑信息 -->
            <div class="special-content">
              <h3 class="special-name">
                <a :href="`/public/special/${special.id}`" target="_blank" class="special-link">{{ special.specialName }}</a>
              </h3>
              <p class="special-introduce" v-if="special.specialIntroduce">
                {{ special.specialIntroduce }}
              </p>
              <div class="special-footer">
                <div class="special-meta">
                  <span class="meta-item">
                    <n-icon><FileText /></n-icon>
                    {{ special.includedCount || 0 }} 篇文章
                  </span>
                  <span class="meta-item" :title="getFullDateTime(special.createTime)">
                    <n-icon><Calendar /></n-icon>
                    {{ formatTime(special.createTime) }}
                  </span>
                </div>
                <div class="special-actions">
                  <n-button text size="tiny" type="info" @click="handleManageContent(special)">
                    管理内容
                  </n-button>
                  <n-button text size="tiny" type="primary" @click="handleEdit(special)">
                    编辑
                  </n-button>
                  <n-popconfirm @positive-click="handleDelete(special.id)">
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
        </div>
      </template>
    </div>

    <!-- 新建/编辑专辑弹窗 -->
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
        <n-form-item label="专辑名称" path="specialName">
          <n-input
            v-model:value="formData.specialName"
            placeholder="请输入专辑名称"
            maxlength="50"
          />
        </n-form-item>

        <n-form-item label="专辑简介" path="specialIntroduce">
          <n-input
            v-model:value="formData.specialIntroduce"
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
              <n-button
                class="remove-btn"
                circle
                size="tiny"
                @click="formData.cover = ''"
              >
                <template #icon>
                  <n-icon><X /></n-icon>
                </template>
              </n-button>
            </div>
            <n-upload
              v-else
              :custom-request="customUploadRequest"
              :show-file-list="false"
            >
              <n-upload-dragger>
                <div class="upload-hint">
                  <n-icon size="32" :depth="2"><CloudUpload /></n-icon>
                  <p>点击或拖拽上传封面图</p>
                  <p class="hint-tip">支持 JPG、PNG、WebP 格式</p>
                </div>
              </n-upload-dragger>
            </n-upload>
          </div>
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
import { Plus, Folder, FileText, CloudUpload, X, Calendar, Search } from '@vicons/tabler'
import type { FormInst, FormRules, UploadCustomRequestOptions } from 'naive-ui'
import type { AdminSpecialItem } from '~/types'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()
const specialApi = useAdminSpecialApi()
const ossApi = useOssApi()

// 专辑列表
const specialList = ref<AdminSpecialItem[]>([])
const pending = ref(false)
const searchKeyword = ref('')

const filteredList = computed(() => {
  if (!searchKeyword.value) return specialList.value
  const kw = searchKeyword.value.toLowerCase()
  return specialList.value.filter(s => s.specialName.toLowerCase().includes(kw))
})

// 弹窗相关
const showModal = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInst | null>(null)
const formLoading = ref(false)

const formData = ref({
  specialName: '',
  specialIntroduce: '',
  cover: ''
})

const formRules: FormRules = {
  specialName: { required: true, message: '请输入专辑名称' },
  specialIntroduce: { required: true, message: '请输入专辑简介' },
  cover: { required: true, message: '请上传封面图' }
}

// 获取专辑列表
const fetchSpecialList = async () => {
  if (!authStore.user?.uuid) return

  pending.value = true
  try {
    const result = await specialApi.getSpecialList(authStore.user.uuid)
    specialList.value = result || []
  } catch {
  } finally {
    pending.value = false
  }
}

// 新建专辑
const handleCreate = () => {
  editingId.value = null
  formData.value = {
    specialName: '',
    specialIntroduce: '',
    cover: ''
  }
  showModal.value = true
}

// 编辑专辑
const handleEdit = (special: AdminSpecialItem) => {
  editingId.value = special.id
  formData.value = {
    specialName: special.specialName,
    specialIntroduce: special.specialIntroduce || '',
    cover: special.cover || ''
  }
  showModal.value = true
}

// 删除专辑
const handleDelete = async (id: number) => {
  try {
    await specialApi.deleteSpecial(id)
    message.success('删除成功')
    fetchSpecialList()
  } catch {}
}

// 管理内容 - 跳转到内容管理页面
const handleManageContent = (special: AdminSpecialItem) => {
    router.push(`/admin/column-content/${special.id}?name=${encodeURIComponent(special.specialName)}`)
}

// 自定义上传请求
const customUploadRequest = async (options: UploadCustomRequestOptions) => {
  const { file, onFinish, onError, onProgress } = options
  try {
    if (!file.file) {
      message.error('文件不存在')
      onError()
      return
    }
    const url = await ossApi.uploadFile(file.file, (percent) => {
      onProgress({ percent })
    })
    formData.value.cover = url
    onFinish()
    message.success('上传成功')
  } catch {
    onError()
  }
}

// 提交表单
const handleConfirm = async () => {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  formLoading.value = true
  try {
    if (editingId.value) {
      await specialApi.updateSpecial({
        id: editingId.value,
        ...formData.value
      })
      message.success('更新成功')
    } else {
      await specialApi.createSpecial(formData.value)
      message.success('创建成功')
    }
    showModal.value = false
    fetchSpecialList()
  } catch {
  } finally {
    formLoading.value = false
  }
}

// 初始化
onMounted(() => fetchSpecialList())
</script>

<style scoped>
.admin-columns {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
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
  padding: 16px;
  box-shadow: var(--shadow-sm);
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-header {
  flex-shrink: 0;
  padding-bottom: 12px;
  margin-bottom: 12px;
  border-bottom: 1px solid var(--color-border-light);
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 专辑列表 - 使用独立滚动容器 */
.special-list-wrapper {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding-right: 8px;
  margin-right: -8px;
}

.special-list-wrapper::-webkit-scrollbar {
  width: 6px;
}

.special-list-wrapper::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.special-list-wrapper::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.special-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

/* 专辑卡片 */
.special-item {
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all var(--transition-base);
}

.special-item:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

/* 封面图 */
.special-cover {
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.special-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
}

.special-item:hover .special-cover img {
  transform: scale(1.05);
}

.cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

/* 专辑信息 */
.special-content {
  flex: 1;
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.special-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.special-link {
  color: inherit;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.special-link:hover {
  color: var(--color-primary);
}

.special-introduce {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: 1.5;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 底部行：文章数 + 操作按钮 */
.special-footer {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: auto;
  padding-top: 8px;
  border-top: 1px solid var(--color-border-light);
}

.special-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.special-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 封面上传 */
.cover-upload {
  width: 100%;
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 160px;
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
  height: 160px;
  color: var(--color-ink-muted);
}

.upload-hint p {
  margin: 8px 0 0;
}

.hint-tip {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

/* 弹窗底部 */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式 */
@media (max-width: 768px) {
  .special-list {
    grid-template-columns: 1fr;
  }
}
</style>
