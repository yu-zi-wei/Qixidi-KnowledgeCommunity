<template>
  <div class="admin-favorites">
    <!-- 顶部操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索收藏夹名称..."
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
        新建收藏夹
      </n-button>
    </div>

    <!-- 收藏夹列表 -->
    <div class="list-section">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="large" />
      </div>

      <!-- 空状态 -->
      <div v-else-if="!filteredList.length" class="empty-state">
        <n-empty :description="searchKeyword ? '未找到匹配的收藏夹' : '暂无收藏夹'">
          <template v-if="!searchKeyword" #extra>
            <n-button type="primary" @click="handleCreate">立即创建</n-button>
          </template>
        </n-empty>
      </div>

      <!-- 列表 -->
      <template v-else>
        <div class="list-header">
          <span class="total-count">共 {{ filteredList.length }} 个收藏夹</span>
        </div>
        <div class="collection-list-wrapper">
          <div class="collection-list">
            <div
              v-for="item in filteredList"
              :key="item.id"
              class="collection-item"
            >
              <!-- 收藏夹图标 -->
              <div class="collection-icon">
                <n-icon size="32"><Folder /></n-icon>
              </div>

              <!-- 收藏夹信息 -->
              <div class="collection-content">
                <h3 class="collection-name">
                  <a :href="`/public/collection/${item.id}`" target="_blank" class="collection-link">{{ item.collectionName }}</a>
                </h3>
                <p class="collection-introduce" v-if="item.collectionIntroduce">
                  {{ item.collectionIntroduce }}
                </p>
                <div class="collection-footer">
                  <div class="collection-meta">
                    <span class="meta-item">
                      <n-icon><FileText /></n-icon>
                      {{ item.includedCount || 0 }} 篇文章
                    </span>
                    <span class="meta-item" :title="getFullDateTime(item.createTime)">
                      <n-icon><Calendar /></n-icon>
                      {{ formatTime(item.createTime) }}
                    </span>
                  </div>
                  <div class="collection-actions">
                    <n-button text size="tiny" type="primary" @click="handleEdit(item)">
                      编辑
                    </n-button>
                    <n-popconfirm @positive-click="handleDelete(item.id)">
                      <template #trigger>
                        <n-button text size="tiny" type="error">删除</n-button>
                      </template>
                      确定要删除这个收藏夹吗？
                    </n-popconfirm>
                  </div>
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
      :title="editingId ? '编辑收藏夹' : '新建收藏夹'"
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
        <n-form-item label="收藏夹名称" path="collectionName">
          <n-input
            v-model:value="formData.collectionName"
            placeholder="请输入收藏夹名称"
            maxlength="50"
          />
        </n-form-item>

        <n-form-item label="收藏夹简介" path="collectionIntroduce">
          <n-input
            v-model:value="formData.collectionIntroduce"
            type="textarea"
            placeholder="请输入收藏夹简介"
            :rows="3"
            maxlength="200"
            show-count
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
import { Plus, Folder, FileText, Calendar, Search } from '@vicons/tabler'
import type { FormInst, FormRules } from 'naive-ui'
import type { CollectionItem } from '~/types'

definePageMeta({ layout: 'admin' })

const message = useMessage()
const collectionApi = useCollectionApi()

// 列表
const collectionList = ref<CollectionItem[]>([])
const loading = ref(false)
const searchKeyword = ref('')

const filteredList = computed(() => {
  if (!searchKeyword.value) return collectionList.value
  const kw = searchKeyword.value.toLowerCase()
  return collectionList.value.filter(c => c.collectionName.toLowerCase().includes(kw))
})

// 弹窗
const showModal = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInst | null>(null)
const formLoading = ref(false)

const formData = ref({
  collectionName: '',
  collectionIntroduce: ''
})

const formRules: FormRules = {
  collectionName: { required: true, message: '请输入收藏夹名称' }
}

// 获取列表
const fetchList = async () => {
  loading.value = true
  try {
    const result = await collectionApi.getMyCollections()
    collectionList.value = result || []
  } catch {
  } finally {
    loading.value = false
  }
}

// 新建
const handleCreate = () => {
  editingId.value = null
  formData.value = { collectionName: '', collectionIntroduce: '' }
  showModal.value = true
}

// 编辑
const handleEdit = (item: CollectionItem) => {
  editingId.value = item.id
  formData.value = {
    collectionName: item.collectionName,
    collectionIntroduce: item.collectionIntroduce || ''
  }
  showModal.value = true
}

// 删除
const handleDelete = async (id: number) => {
  try {
    await collectionApi.deleteCollection(id)
    message.success('删除成功')
    fetchList()
  } catch {}
}

// 提交
const handleConfirm = async () => {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  formLoading.value = true
  try {
    if (editingId.value) {
      await collectionApi.updateCollection({
        id: editingId.value,
        ...formData.value
      })
      message.success('更新成功')
    } else {
      await collectionApi.createCollectionFolder(formData.value)
      message.success('创建成功')
    }
    showModal.value = false
    fetchList()
  } catch {
  } finally {
    formLoading.value = false
  }
}

onMounted(() => fetchList())
</script>

<style scoped>
.admin-favorites {
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

.list-header {
  flex-shrink: 0;
  padding-bottom: var(--space-3);
  margin-bottom: var(--space-3);
  border-bottom: 1px solid var(--color-border-light);
}

.total-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

/* 列表滚动 */
.collection-list-wrapper {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding-right: var(--space-2);
  margin-right: calc(var(--space-2) * -1);
}

.collection-list-wrapper::-webkit-scrollbar {
  width: 6px;
}

.collection-list-wrapper::-webkit-scrollbar-thumb {
  background: var(--color-ink-faint);
  border-radius: 3px;
}

.collection-list-wrapper::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-muted);
}

.collection-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-4);
}

/* 收藏夹卡片 */
.collection-item {
  display: flex;
  flex-direction: column;
  background: var(--color-surface-warm);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all var(--transition-base);
}

.collection-item:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

/* 图标区 */
.collection-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

/* 信息区 */
.collection-content {
  flex: 1;
  padding: var(--space-3);
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.collection-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.collection-link {
  color: inherit;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.collection-link:hover {
  color: var(--color-primary);
}

.collection-introduce {
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

/* 底部行 */
.collection-footer {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: auto;
  padding-top: var(--space-2);
  border-top: 1px solid var(--color-border-light);
}

.collection-meta {
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

.collection-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

/* 弹窗底部 */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
}

/* 响应式 */
@media (max-width: 768px) {
  .collection-list {
    grid-template-columns: 1fr;
  }
}
</style>
