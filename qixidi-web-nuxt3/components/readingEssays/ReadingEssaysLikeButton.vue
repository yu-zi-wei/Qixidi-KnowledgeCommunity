<template>
  <button
    class="essay-like-btn"
    :class="{ 'is-liked': essay.isFabulous }"
    :title="essay.isFabulous ? '取消点赞' : '点赞'"
    @click.stop="toggleLike"
  >
    <Heart class="essay-like-icon" :class="{ 'liked-fill': essay.isFabulous }" />
    <span class="essay-like-count">{{ essay.likeTimes || 0 }}</span>
  </button>
</template>

<script setup lang="ts">
import { Heart } from '@vicons/tabler'
import type { ReadingEssaysInfo } from '~/types'

const props = defineProps<{
  essay: ReadingEssaysInfo
}>()

// 点赞/取消（type=5 随笔；后端点赞即新增、取消即物理删除，并记足迹+发消息）
const route = useRoute()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const fabulousApi = useFabulousApi()
const likeLoading = ref(false)

const toggleLike = async () => {
  if (likeLoading.value) return
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }
  const toLike = !props.essay.isFabulous
  const payload = {
    typeId: props.essay.id,
    targetId: props.essay.id,
    targetUid: props.essay.uid,
    type: 5,
    // 随笔无标题，用内容前缀作消息/足迹标题
    targetTitle: props.essay.content?.slice(0, 50) || '阅读随笔'
  }
  likeLoading.value = true
  try {
    if (toLike) {
      await fabulousApi.likeArticle(payload)
    } else {
      await fabulousApi.cancelLikeArticle(payload)
    }
    props.essay.isFabulous = toLike
    props.essay.likeTimes = Math.max(0, (props.essay.likeTimes || 0) + (toLike ? 1 : -1))
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    likeLoading.value = false
  }
}
</script>

<style scoped>
/* 点赞按钮：小胶囊，赞过红心实底 */
.essay-like-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 3px 12px;
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

.essay-like-btn:hover {
  color: var(--color-ink);
}

.essay-like-btn.is-liked {
  color: var(--color-danger);
  background: color-mix(in srgb, var(--color-danger) 10%, transparent);
}

.essay-like-icon {
  width: 14px;
  height: 14px;
}

.essay-like-btn .liked-fill {
  fill: currentColor;
}

.essay-like-count {
  font-variant-numeric: tabular-nums;
}
</style>
