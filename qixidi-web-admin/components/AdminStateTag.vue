<template>
  <n-tag v-if="meta" size="small" round :type="meta.type" :bordered="false" class="state-tag">
    <span class="dot" />
    {{ meta.label }}
  </n-tag>
  <span v-else class="unknown">—</span>
</template>

<script setup lang="ts">
/**
 * 通用状态标签：各页面定义自己的 code → {label, type} 映射传入，
 * 组件只负责呈现（圆点 + 彩色 tag），双主题由 n-tag 内置 type 自适应。
 *
 * 映射来源（后端 VO 注解 / 枚举，禁止凭空编）：
 * - 文章审核 ArticleAuditStateEnums：1 审核中 / 2 通过 / 3 不通过 / 4 草稿
 * - 用户 state：0 正常 / 1 冻结 / 2 已删除
 * - 评论 state：0 正常 / 1 已删除
 */
import type { TagProps } from 'naive-ui';

export interface StateMeta {
  label: string;
  type: NonNullable<TagProps['type']>;
}

const props = defineProps<{
  value?: number | string | null;
  map: Record<string | number, StateMeta>;
}>();

const meta = computed<StateMeta | undefined>(() =>
  props.value === null || props.value === undefined ? undefined : props.map[props.value]
);
</script>

<style scoped lang="scss">
.state-tag .dot {
  display: inline-block;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: currentColor;
  margin-right: 5px;
  vertical-align: middle;
  opacity: 0.9;
}

.unknown {
  opacity: 0.4;
}
</style>
