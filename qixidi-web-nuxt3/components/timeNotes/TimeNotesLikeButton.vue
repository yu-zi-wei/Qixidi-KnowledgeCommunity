<template>
  <button
    class="tn-like"
    :class="{ 'is-liked': isLiked }"
    :title="isLiked ? '取消点赞' : '点赞'"
    @click="toggleLike"
  >
    <n-icon size="14" class="tn-like-icon"><Heart /></n-icon>
    <span class="tn-like-count">{{ likeTimes }}</span>
  </button>
</template>

<script setup lang="ts">
import { Heart } from '@vicons/tabler'
import type { TimeNotesInfo } from '~/types'

const props = defineProps<{
  note: TimeNotesInfo
}>()

const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const fabulousApi = useFabulousApi()

const isLiked = computed(() => props.note.isLiked === 0)
const likeTimes = computed(() => props.note.likeTimes || 0)

// 点赞/取消（type=2 小记；后端点赞即新增、取消即物理删除，并记足迹+发消息）
const toggleLike = async () => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }
  const toLike = !isLiked.value
  const payload = {
    typeId: props.note.id,
    targetId: props.note.id,
    targetUid: props.note.uid,
    type: 2,
    targetTitle: props.note.title || props.note.content?.substring(0, 50) || '时光小记'
  }
  try {
    if (toLike) {
      await fabulousApi.likeArticle(payload)
    } else {
      await fabulousApi.cancelLikeArticle(payload)
    }
    props.note.isLiked = toLike ? 0 : 1
    props.note.likeTimes = Math.max(0, (props.note.likeTimes || 0) + (toLike ? 1 : -1))
  } catch {
    /* useApi 已统一弹错 */
  }
}
</script>

<style scoped>
.tn-like {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 12px;
  border: none;
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  background: color-mix(in srgb, var(--color-ink) 4%, transparent);
  cursor: pointer;
  transition: all var(--transition-fast);
  font-family: var(--font-body);
  line-height: 1.5;
}

.tn-like:hover {
  color: var(--color-ink);
}

.tn-like.is-liked {
  color: var(--color-danger);
  background: color-mix(in srgb, var(--color-danger) 10%, transparent);
}

/* 已赞态心形实心填充 */
.tn-like.is-liked :deep(svg) {
  fill: currentColor;
}

.tn-like-count {
  font-variant-numeric: tabular-nums;
}
</style>
