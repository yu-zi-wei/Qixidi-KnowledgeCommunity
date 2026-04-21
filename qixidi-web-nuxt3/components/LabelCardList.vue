<template>
  <div class="label-grid">
    <NuxtLink
      v-for="label in list"
      :key="label.id"
      :to="`/public/label/${label.id}`"
      target="_blank"
      class="label-card"
    >
      <div class="label-card-cover" v-if="label.labelCover" v-html="label.labelCover" />
      <div class="label-card-cover label-card-placeholder" v-else>
        <n-icon size="20"><Tag /></n-icon>
      </div>
      <div class="label-card-body">
        <h3 class="label-card-name">{{ label.labelName }}</h3>
        <p class="label-card-desc" v-if="label.labelDescribe">{{ label.labelDescribe }}</p>
        <div class="label-card-meta">
          <span><n-icon><FileText /></n-icon>{{ label.articleNumber || 0 }} 篇</span>
          <span><n-icon><Users /></n-icon>{{ label.followNumber || 0 }} 关注</span>
        </div>
      </div>
      <n-button
        size="small"
        :type="isFollow(label.id) ? 'default' : 'primary'"
        :loading="togglingId === label.id"
        :disabled="togglingId !== null"
        @click.prevent="handleToggle(label)"
      >
        {{ isFollow(label.id) ? '已关注' : '关注' }}
      </n-button>
    </NuxtLink>
  </div>
</template>

<script setup lang="ts">
import { Tag, FileText, Users } from '@vicons/tabler'

interface LabelItem {
  id: number
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  isFollow?: boolean
}

const props = defineProps<{
  list: LabelItem[]
}>()

const followApi = useFollowApi()
const authStore = useAuthStore()
const message = useMessage()

const togglingId = ref<number | null>(null)
const followState = reactive<Record<number, boolean>>({})

const isFollow = (id: number) => {
  if (followState[id] !== undefined) return followState[id]
  return !!props.list.find(l => l.id === id)?.isFollow
}

const handleToggle = async (item: LabelItem) => {
  if (!authStore.isLoggedIn) {
    message.warning('请先登录')
    return
  }
  if (togglingId.value !== null) return
  togglingId.value = item.id

  const followed = isFollow(item.id)
  try {
    if (followed) {
      await followApi.cancelFollow(item.id, 2)
    } else {
      await followApi.addFollow(item.id, 2)
    }
    followState[item.id] = !followed
  } finally {
    togglingId.value = null
  }
}
</script>

<style>
.label-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.label-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
}

.label-card:hover {
  box-shadow: var(--shadow-sm);
}

.label-card-cover {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.label-card-cover :deep(svg) {
  width: 100%;
  height: 100%;
  display: block;
}

.label-card-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.label-card-body {
  flex: 1;
  min-width: 0;
}

.label-card-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.label-card-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 2px 0 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.label-card-meta {
  display: flex;
  gap: 10px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin-top: 4px;
}

.label-card-meta span {
  display: flex;
  align-items: center;
  gap: 3px;
}

@media (max-width: 768px) {
  .label-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .label-grid {
    grid-template-columns: 1fr;
  }
}
</style>
