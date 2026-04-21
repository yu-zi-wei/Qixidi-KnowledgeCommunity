<template>
  <n-tooltip trigger="hover" placement="bottom">
    <template #trigger>
      <span class="user-role-badge" :class="roleClass">{{ roleLabel }}</span>
    </template>
    {{ roleDesc }}
  </n-tooltip>
</template>

<script setup lang="ts">
const props = withDefaults(defineProps<{ roleId?: number }>(), {
  roleId: undefined
})

const authStore = useAuthStore()

const resolvedRoleId = computed(() => props.roleId ?? authStore.user?.roleId)

const roleClass = computed(() => {
  switch (resolvedRoleId.value) {
    case 3: return 'is-admin'
    case 2: return 'is-creator'
    default: return 'is-user'
  }
})

const roleLabel = computed(() => {
  switch (resolvedRoleId.value) {
    case 3: return '管理员'
    case 2: return '创作者'
    default: return '用户'
  }
})

const roleDesc = computed(() => {
  switch (resolvedRoleId.value) {
    case 3: return '管理员：拥有全部权限，包括内容审核、系统管理等'
    case 2: return '创作者：可发布文章、小记、随笔等内容'
    default: return '普通用户：可浏览内容、收藏、点赞、评论'
  }
})
</script>

<style scoped>
.user-role-badge {
  display: inline-flex;
  align-items: center;
  padding: 1px 8px;
  font-size: 11px;
  font-weight: 500;
  border-radius: var(--radius-full);
  line-height: 1.6;
  white-space: nowrap;
  cursor: default;
}

.user-role-badge.is-admin {
  color: #b45309;
  background: rgba(217, 119, 6, 0.1);
}

.user-role-badge.is-creator {
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.user-role-badge.is-user {
  color: var(--color-ink-muted);
  background: var(--color-surface-dim);
}

.dark .user-role-badge.is-admin {
  color: #fbbf24;
  background: rgba(217, 119, 6, 0.2);
}
</style>
