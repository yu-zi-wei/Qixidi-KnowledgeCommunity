<template>
  <n-modal v-model:show="visible" preset="card" title="选择收藏夹" :style="modalStyle" :mask-closable="false">
    <div class="collection-dialog">
      <!-- 收藏夹列表 -->
      <div v-if="folders.length > 0" class="folder-list">
        <div
          v-for="folder in folders"
          :key="folder.id"
          class="folder-item"
          :class="{ selected: selectedFolderId === folder.id }"
          @click="selectFolder(folder.id)"
        >
          <div class="folder-info">
            <div class="folder-header">
              <div class="folder-name">{{ folder.collectionName }}</div>
              <div class="folder-count">{{ folder.includedCount || 0 }}篇</div>
            </div>
            <div v-if="folder.collectionIntroduce" class="folder-introduce">{{ folder.collectionIntroduce }}</div>
          </div>
          <n-checkbox v-if="showCheckbox" :checked="selectedFolderId === folder.id" @update:checked="() => selectFolder(folder.id)" />
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-folders">
        <p>暂无收藏夹</p>
      </div>

      <!-- 创建收藏夹表单 -->
      <div v-if="showCreateForm" class="create-form">
        <n-form ref="formRef" :model="formData" :rules="formRules">
          <n-form-item label="名称" path="collectionName">
            <n-input v-model:value="formData.collectionName" placeholder="请输入收藏夹名称" />
          </n-form-item>
          <n-form-item label="描述" path="collectionIntroduce">
            <n-input
              v-model:value="formData.collectionIntroduce"
              type="textarea"
              :rows="3"
              placeholder="请输入收藏夹描述（可选）"
            />
          </n-form-item>
        </n-form>
      </div>
    </div>

    <!-- 底部按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <div class="footer-left">
          <n-button v-if="!showCreateForm" quaternary @click="toggleCreateForm">
            <template #icon>
              <Plus class="btn-icon" />
            </template>
            新建收藏夹
          </n-button>
          <n-button v-else quaternary @click="cancelCreateForm">
            取消新建
          </n-button>
        </div>
        <div class="footer-right">
          <n-button v-if="showCreateForm" type="primary" :loading="creating" @click="handleCreateFolder">
            创建
          </n-button>
          <n-button v-else type="primary" :disabled="!selectedFolderId" :loading="collecting" @click="handleCollect">
            收藏
          </n-button>
        </div>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { Plus } from '@vicons/tabler'
import type { CollectionFolder } from '~/types'

interface Props {
  articleId: number
  articleLabelIds: string  // 文章标签 id，逗号分隔
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const collectionApi = useCollectionApi()
const authStore = useAuthStore()
const message = useMessage()

const visible = defineModel<boolean>('show', { type: Boolean, default: false })

// 收藏夹列表
const folders = ref<CollectionFolder[]>([])
const selectedFolderId = ref<number | null>(null)
const loading = ref(false)
const creating = ref(false)
const collecting = ref(false)

// 创建表单
const showCreateForm = ref(false)
const formRef = ref()
const formData = ref({
  collectionName: '',
  collectionIntroduce: ''
})

const formRules = {
  collectionName: {
    required: true,
    message: '请输入收藏夹名称',
    trigger: ['blur', 'change']
  }
}

// 弹窗样式
const modalStyle = {
  width: '500px'
}

// 显示复选框（移动端）
const showCheckbox = computed(() => {
  return window.innerWidth < 768
})

// 监听弹窗显示，加载收藏夹列表
watch(visible, async (val) => {
  if (val) {
    await loadFolders()
  }
})

// 加载收藏夹列表
const loadFolders = async () => {
  if (!authStore.user?.uuid) return

  loading.value = true
  try {
    folders.value = await collectionApi.getCollectionFolders(authStore.user.uuid)
  } catch (error) {
    console.error('加载收藏夹失败:', error)
    folders.value = []
  } finally {
    loading.value = false
  }
}

// 选择收藏夹
const selectFolder = (id: number) => {
  selectedFolderId.value = id
}

// 切换创建表单
const toggleCreateForm = () => {
  showCreateForm.value = true
  selectedFolderId.value = null
}

// 取消创建
const cancelCreateForm = () => {
  showCreateForm.value = false
  formData.value = {
    collectionName: '',
    collectionIntroduce: ''
  }
  formRef.value?.restoreValidation()
}

// 创建收藏夹
const handleCreateFolder = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
  } catch {
    return
  }

  creating.value = true
  try {
    await collectionApi.createCollectionFolder(formData.value)
    message.success('收藏夹创建成功')
    // 刷新收藏夹列表
    await loadFolders()
    // 重置表单
    cancelCreateForm()
  } catch (error) {
    console.error('创建收藏夹失败:', error)
    message.error('创建收藏夹失败')
  } finally {
    creating.value = false
  }
}

// 收藏文章
const handleCollect = async () => {
  if (!selectedFolderId.value) return

  collecting.value = true
  try {
    await collectionApi.addArticleToCollection({
      collectionId: selectedFolderId.value,
      targetId: props.articleId,
      type: 1,
      labelId: props.articleLabelIds
    })
    message.success('收藏成功')
    emit('success')
    visible.value = false
  } catch (error) {
    console.error('收藏失败:', error)
    message.error('收藏失败')
  } finally {
    collecting.value = false
  }
}
</script>

<style scoped>
.collection-dialog {
  min-height: 300px;
}

.folder-list {
  max-height: 300px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.folder-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.folder-item:hover {
  border-color: var(--color-primary);
}

.folder-item.selected {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.folder-info {
  flex: 1;
  min-width: 0;
}

.folder-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: var(--space-2);
  margin-bottom: var(--space-1);
}

.folder-name {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.folder-count {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.folder-introduce {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.empty-folders {
  padding: var(--space-10) var(--space-5);
  text-align: center;
  color: var(--color-ink-muted);
}

.create-form {
  margin-top: var(--space-4);
  padding-top: var(--space-4);
  border-top: 1px solid var(--color-border);
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left,
.footer-right {
  display: flex;
  gap: var(--space-2);
}

.btn-icon {
  width: 16px;
  height: 16px;
}
</style>
