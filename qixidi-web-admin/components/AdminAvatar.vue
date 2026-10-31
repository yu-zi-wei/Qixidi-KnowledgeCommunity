<template>
  <img
    v-if="src && !failed"
    :src="src"
    :style="sizeStyle"
    class="admin-avatar"
    loading="lazy"
    alt=""
    @error="failed = true"
  />
  <div v-else class="admin-avatar admin-avatar--fallback" :style="sizeStyle">
    <span :style="{ fontSize: fallbackFontSize + 'px' }">{{ fallbackChar }}</span>
  </div>
</template>

<script setup lang="ts">
/**
 * 后台通用头像/封面：外链裂图自动兜底为首字占位
 * 页面 scoped CSS 选不中 n-data-table 列 render 里的元素，样式全部内聚本组件
 */
const props = withDefaults(
  defineProps<{
    src?: string | null;
    /** 兜底占位取首字的名称 */
    name?: string | null;
    /** 正方形传 number，矩形封面对面传 [w, h] */
    size: number | [number, number];
    shape?: 'circle' | 'square';
  }>(),
  { src: null, name: null, shape: 'circle' }
);

/** 切换 src 时重置失败态（详情抽屉复用同一组件实例） */
const failed = ref(false);
watch(
  () => props.src,
  () => {
    failed.value = false;
  }
);

const width = computed(() => (Array.isArray(props.size) ? props.size[0] : props.size));
const height = computed(() => (Array.isArray(props.size) ? props.size[1] : props.size));
const radius = computed(() => (props.shape === 'circle' ? '50%' : '4px'));
const sizeStyle = computed(() => ({
  width: `${width.value}px`,
  height: `${height.value}px`,
  borderRadius: radius.value
}));

const fallbackChar = computed(() => (props.name || '?').trim().slice(0, 1) || '?');
const fallbackFontSize = computed(() => Math.round(Math.min(width.value, height.value) * 0.42));
</script>

<style scoped lang="scss">
.admin-avatar {
  object-fit: cover;
  flex-shrink: 0;
  display: block;
}

.admin-avatar--fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  /* 书签风暖灰兜底（中性偏暖，白纸片与深色纸片上都成立） */
  background: rgba(140, 134, 120, 0.18);
  color: #8d877a;
  font-weight: 600;
  user-select: none;
}
</style>
