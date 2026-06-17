<template>
  <n-drawer v-model:show="visible" :width="630" placement="right" :mask-closable="false">
    <n-drawer-content :title="isEdit ? '编辑随笔' : '记随笔'" closable>
      <div class="drawer-content">
        <!-- 随笔内容 -->
        <div class="form-item">
          <n-input
            v-model:value="form.content"
            type="textarea"
            placeholder="记录此刻的想法、灵感、感悟..."
            :autosize="{ minRows: 5, maxRows: 10 }"
          />
        </div>

        <!-- 分类 -->
        <div class="form-item">
          <label class="form-label">分类 <span class="required">*</span></label>
          <div class="category-chips">
            <span
              v-for="group in groups"
              :key="group.id"
              :class="['chip', { active: form.groupId === group.id }]"
              @click="handleSelectGroup(group.id)"
            >
              {{ group.name }}
            </span>
          </div>
        </div>

        <!-- 作者 & 作品 -->
        <div class="form-row">
          <div class="form-item half">
            <label class="form-label">作者</label>
            <n-input v-model:value="form.author" placeholder="可选" />
          </div>
          <div class="form-item half">
            <label class="form-label">作品</label>
            <n-input v-model:value="form.worksName" placeholder="可选" />
          </div>
        </div>

        <!-- 专辑 -->
        <div class="form-item">
          <label class="form-label">收录专辑</label>
          <n-select
            v-model:value="form.albumId"
            :options="albumOptions"
            placeholder="选择专辑"
            clearable
            filterable
            :loading="albumsLoading"
            @search="handleAlbumSearch"
          />
        </div>

        <!-- 标签 -->
        <div class="form-item">
          <label class="form-label">标签</label>
          <div class="label-tags">
            <n-tag
              v-for="(label, index) in labelList"
              :key="index"
              closable
              size="small"
              round
              @close="handleRemoveLabel(index)"
            >
              {{ label }}
            </n-tag>
            <n-input
              v-if="showAddLabelInput"
              v-model:value="newLabel"
              size="small"
              placeholder="标签"
              style="width: 80px"
              @keyup.enter="handleAddLabel"
              @blur="handleAddLabel"
            />
            <n-button
              v-else-if="labelList.length < 3"
              size="small"
              quaternary
              round
              @click="showAddLabelInput = true"
            >
              <template #icon>
                <Plus class="icon-tiny" />
              </template>
            </n-button>
          </div>
        </div>

        <!-- 可见范围 -->
        <div class="form-item inline">
          <label class="form-label">公开可见</label>
          <n-switch
            :value="form.dictumState === 1"
            @update:value="form.dictumState = $event ? 1 : 2"
          />
        </div>
      </div>

      <template #footer>
        <div class="drawer-footer">
          <n-button @click="handleClose">取消</n-button>
          <n-button
            type="primary"
            :disabled="isSubmitting"
            :loading="isSubmitting"
            @click="handlePublish"
          >
            {{ isEdit ? '更新' : '发布' }}
          </n-button>
        </div>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch, reactive } from 'vue'
import { Plus } from '@vicons/tabler'
import type { ReadingEssaysGroup, ReadingEssaysAlbum } from '~/types'
import type { DictumForm } from '~/composables/useReadingEssaysApi'
import type { EssayPreset } from '~/stores/essayDrawer'

const props = defineProps<{
  show: boolean
  editId?: number
  preset?: EssayPreset
}>()

const emit = defineEmits<{
  'update:show': [value: boolean]
  success: []
}>()

const message = useMessage()
const dialog = useDialog()
const dictumApi = useReadingEssaysApi()

// 可见状态
const visible = computed({
  get: () => props.show,
  set: (val) => emit('update:show', val)
})

// 是否编辑模式
const isEdit = computed(() => !!props.editId)

// 表单数据
const form = reactive<DictumForm>({
  content: '',
  contentMd: '',
  groupId: 0,
  albumId: undefined,
  label: '',
  author: '',
  worksName: '',
  picture: '',
  dictumState: 1
})

// 分类数据
const groupsData = ref<{ rows: ReadingEssaysGroup[] } | null>(null)
const groupsLoading = ref(false)

// 加载分类
const loadGroups = async () => {
  if (groupsData.value) return // 已加载过就不再加载
  groupsLoading.value = true
  try {
    const result = await dictumApi.getDictumGroups({ pageNum: 1, pageSize: 100 })
    groupsData.value = result
  } finally {
    groupsLoading.value = false
  }
}

const groups = computed<ReadingEssaysGroup[]>(() => groupsData.value?.rows || [])

// 在抽屉打开时加载分类和专辑
watch(() => props.show, (show) => {
  if (show) {
    loadGroups()
    loadAlbums()
  }
})

// 专辑
const albums = ref<ReadingEssaysAlbum[]>([])
const albumsLoading = ref(false)

// 标签
const labelList = ref<string[]>([])
const showAddLabelInput = ref(false)
const newLabel = ref('')

watch(() => form.label, (val) => {
  labelList.value = val ? val.split(',').filter(Boolean) : []
}, { immediate: true })

const albumOptions = computed(() =>
  albums.value.map(item => ({ label: item.name, value: item.id }))
)

// 加载详情
watch([() => props.show, () => props.editId], async ([show, editId]) => {
  if (show && editId) {
    try {
      const data = await dictumApi.getDictumDetail(editId)
      if (data) {
        Object.assign(form, {
          id: data.id,
          content: data.content || '',
          contentMd: data.contentMd || '',
          groupId: data.groupId,
          albumId: data.albumId,
          author: data.author || '',
          worksName: data.worksName || '',
          label: data.label || '',
          dictumState: data.dictumState || 1
        })
      }
    } catch (error) {
      console.error('加载详情失败:', error)
    }
  } else if (show && !editId) {
    // 重置表单
    Object.assign(form, {
      content: '',
      contentMd: '',
      groupId: 0,
      albumId: undefined,
      label: '',
      author: '',
      worksName: '',
      picture: '',
      dictumState: 1
    })
    // 应用预填数据（复制场景：保留分类/作者/作品/专辑，内容留空让用户填写）
    if (props.preset) {
      Object.assign(form, props.preset)
    }
  }
}, { immediate: true })

// 加载专辑
const loadAlbums = async (searchName?: string) => {
  albumsLoading.value = true
  try {
    const result = await dictumApi.getDictumAlbums({ pageNum: 1, pageSize: 50, albumName: searchName })
    albums.value = result.rows || []
  } finally {
    albumsLoading.value = false
  }
}

const handleAlbumSearch = (query: string) => loadAlbums(query)

const handleSelectGroup = (groupId: number) => {
  form.groupId = form.groupId === groupId ? 0 : groupId
}

const handleAddLabel = () => {
  const label = newLabel.value.trim()
  if (label && !labelList.value.includes(label) && labelList.value.length < 3) {
    labelList.value.push(label)
    form.label = labelList.value.join(',')
  }
  newLabel.value = ''
  showAddLabelInput.value = false
}

const handleRemoveLabel = (index: number) => {
  labelList.value.splice(index, 1)
  form.label = labelList.value.join(',')
}

const validateForm = (): boolean => {
  if (!form.content?.trim()) {
    message.error('请输入随笔内容')
    return false
  }
  if (!form.groupId) {
    message.error('请选择分类')
    return false
  }
  return true
}

const isSubmitting = ref(false)

const handlePublish = async () => {
  if (!validateForm()) return

  try {
    isSubmitting.value = true
    if (isEdit.value && props.editId) {
      await dictumApi.updateDictum({ ...form, id: props.editId })
      message.success('更新成功！')
    } else {
      await dictumApi.createDictum(form)
      message.success('发布成功！')
    }
    emit('success')
    handleClose()
  } catch (error: any) {
  } finally {
    isSubmitting.value = false
  }
}

const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.drawer-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item.half {
  flex: 1;
}

.form-item.inline {
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.form-row {
  display: flex;
  gap: 12px;
}

.form-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink);
}

.required {
  color: #e74c3c;
}

.category-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.chip {
  padding: 6px 14px;
  font-size: 14px;
  color: var(--color-ink);
  background: var(--color-surface-dim);
  border-radius: 999px;
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}

.chip:hover {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.chip.active {
  background: var(--color-primary);
  color: white;
}

.label-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
}

.icon-tiny {
  width: 12px;
  height: 12px;
  stroke-width: 2;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
