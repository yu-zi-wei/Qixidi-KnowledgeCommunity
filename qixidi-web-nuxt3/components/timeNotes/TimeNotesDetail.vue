<template>
  <div class="time-notes-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <n-spin size="medium" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!detail" class="empty-state">
      <p>请选择一条记录查看</p>
    </div>

    <!-- 详情内容 -->
    <template v-else>
      <!-- 头部操作栏 -->
      <div class="detail-actions">
        <div class="actions-left">
          <!-- 左侧暂时留空 -->
        </div>
        <div class="actions-right">
          <!-- 编辑按钮（仅作者可见） -->
          <n-button
            v-if="detail.isAuthor === 0"
            quaternary
            size="small"
            class="btn-secondary"
            @click="handleEdit"
          >
            <template #icon>
              <n-icon><Edit /></n-icon>
            </template>
          </n-button>
          <NuxtLink v-if="detail" :to="`/time-notes/${detail.id}`" target="_blank" class="inline-flex">
            <n-button quaternary size="small" class="btn-secondary">
              <template #icon>
                <n-icon><ExternalLink /></n-icon>
              </template>
            </n-button>
          </NuxtLink>
          <n-button quaternary size="small" class="btn-secondary" @click="copyShareLink">
            <template #icon>
              <n-icon><Share /></n-icon>
            </template>
          </n-button>
        </div>
      </div>

      <!-- 内容区域（使用独立组件） -->
      <TimeNotesDetailContent :note="detail" />
    </template>
  </div>
</template>

<script setup lang="ts">
import { Edit, ExternalLink, Share } from '@vicons/tabler'
import type { TimeNotesInfo } from '~/types'

interface Props {
  detail: TimeNotesInfo | null
  loading?: boolean
}

const props = defineProps<Props>()

const emit = defineEmits<{
  edit: [id: number]
}>()

const message = useMessage()

// 编辑
const handleEdit = () => {
  if (!props.detail) return
  emit('edit', props.detail.id)
}

// 复制分享链接
const copyShareLink = async () => {
  if (!props.detail) return
  const url = `${window.location.origin}/time-notes/${props.detail.id}`
  try {
    await navigator.clipboard.writeText(url)
    message.success('链接已复制')
  } catch {
    message.error('复制失败')
  }
}
</script>

<style scoped>
.time-notes-detail {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--color-ink-muted);
}

/* 头部操作栏 */
.detail-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.actions-left,
.actions-right {
  display: flex;
  gap: 4px;
}

/* 次要按钮调淡 */
.btn-secondary {
  color: var(--color-ink-muted) !important;
}

.btn-secondary:hover {
  color: var(--color-ink) !important;
}

/* 响应式 */
@media (max-width: 768px) {
  .detail-actions {
    padding: 10px 12px;
  }
}
</style>

<!-- 非 scoped 样式：修复按钮 focus 状态 -->
<style>
.detail-actions .n-button:not(:hover):not(:active):not(:focus-visible) {
  background-color: transparent !important;
}
</style>
