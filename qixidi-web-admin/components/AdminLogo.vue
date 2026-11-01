<template>
  <!-- 后台 logo（前台同款树形，墨水蓝即品牌原色 #3D5A80，前后台同源）：
       树冠/根部圆点取布局根注入的 --logo-bg（与 app.vue primaryColor 同源，深色模式换亮墨蓝），
       白色枝干为半透明装饰线。与前台的区分：树整体缩至 86% 外套圆角「印章框」（书签印章，
       前台是自然生长的树、后台是盖了章的树）。login 页不套布局，由页面根元素自行定义该变量。
       favicon.ico 是同图形位图版（含印框）。 -->
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 100 100"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
  >
    <!-- 印章框（后台标识） -->
    <rect class="logo-seal" x="5" y="5" width="90" height="90" rx="12" stroke-width="3.5" />
    <g transform="translate(50 50) scale(0.86) translate(-50 -50)">
      <!-- 树冠 -->
      <path
        class="logo-tree"
        d="M50 12 C28 30, 18 55, 22 75 C24 83, 30 88, 38 90 L50 82 L62 90 C70 88, 76 83, 78 75 C82 55, 72 30, 50 12Z"
      />
      <!-- 枝干（白色半透明） -->
      <path d="M50 18 L50 82" stroke="#ffffff" stroke-width="2" opacity="0.35" stroke-linecap="round" />
      <path d="M50 40 L36 52" stroke="#ffffff" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path d="M50 40 L64 52" stroke="#ffffff" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path d="M50 55 L32 70" stroke="#ffffff" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path d="M50 55 L68 70" stroke="#ffffff" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <!-- 根部圆点 -->
      <circle class="logo-tree" cx="50" cy="92" r="3.5" />
    </g>
  </svg>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ size?: number }>(), { size: 26 });
</script>

<style scoped>
.logo-tree {
  fill: var(--logo-bg, #3d5a80);
}

.logo-seal {
  stroke: var(--logo-bg, #3d5a80);
  fill: none;
}
</style>
