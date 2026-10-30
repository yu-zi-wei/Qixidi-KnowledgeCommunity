<template>
  <div class="row-card">
    <div v-if="$slots.leading" class="row-leading">
      <slot name="leading" />
    </div>
    <div v-if="$slots.media" class="row-media">
      <slot name="media" />
    </div>
    <div class="row-main">
      <slot />
    </div>
    <div v-if="$slots.side" class="row-side">
      <slot name="side" />
    </div>
  </div>
</template>

<script setup lang="ts">
/**
 * 行卡片：富信息列表的统一行骨架（参考图定版布局）
 * 四区：leading（选择框等前置）/ media（封面·头像）/ main（标题+摘要+meta）/ side（数据+状态+操作）
 * 内容全部由页面模板提供，scoped CSS 对 slot 内容正常生效
 */
</script>

<style scoped lang="scss">
.row-card {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 14px;
  border-radius: 10px;
  border: 1px solid rgba(128, 128, 128, 0.1);
  transition: background 0.2s ease, border-color 0.2s ease;

  &:hover {
    background: rgba(128, 128, 128, 0.06);
    border-color: rgba(128, 128, 128, 0.2);
  }
}

.row-leading {
  display: flex;
  align-items: center;
  height: 48px; // 与 media 等高，视觉对齐
  flex-shrink: 0;
}

.row-media {
  flex-shrink: 0;
}

.row-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.row-side {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  gap: 8px;
  min-height: 48px;
}
</style>
