<template>
  <!-- 后台 logo（实心印章反白）：前台是墨蓝的树自然生长在纸上，后台是纸色的树
       盖进墨蓝印章里（真印章是实心落款，非空框）。印块 class `logo-seal` 取
       布局根注入的 --logo-bg（与 app.vue primaryColor 同源，浅色墨水蓝 #3D5A80、
       深色亮墨蓝），树冠/根点 class `logo-tree` 取 --logo-ink（所在底色：
       浅色暖纸 #FAF9F6、深色墨黑 #131820，形成"镂空"观感），枝干为印色半透明。
       login 页 layout:false 不套布局，由页面根元素自行注入这两个变量。
       favicon.ico 是同图形位图版（恒定蓝底纸树，不随主题）。 -->
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 100 100"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
  >
    <!-- 印章底（后台标识） -->
    <rect class="logo-seal" x="4" y="4" width="92" height="92" rx="22" />
    <g transform="translate(50 51) scale(0.78) translate(-50 -51)">
      <!-- 树冠（反白：所在底色） -->
      <path
        class="logo-tree"
        d="M50 12 C28 30, 18 55, 22 75 C24 83, 30 88, 38 90 L50 82 L62 90 C70 88, 76 83, 78 75 C82 55, 72 30, 50 12Z"
      />
      <!-- 枝干（印色半透明） -->
      <path class="logo-branch" d="M50 18 L50 82" stroke-width="2" opacity="0.35" stroke-linecap="round" />
      <path class="logo-branch" d="M50 40 L36 52" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path class="logo-branch" d="M50 40 L64 52" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path class="logo-branch" d="M50 55 L32 70" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <path class="logo-branch" d="M50 55 L68 70" stroke-width="1.5" opacity="0.25" stroke-linecap="round" />
      <!-- 根部圆点 -->
      <circle class="logo-tree" cx="50" cy="92" r="3.5" />
    </g>
  </svg>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ size?: number }>(), { size: 26 });
</script>

<style scoped>
.logo-seal {
  fill: var(--logo-bg, #3d5a80);
}

.logo-tree {
  fill: var(--logo-ink, #faf9f6);
}

.logo-branch {
  stroke: var(--logo-bg, #3d5a80);
}
</style>
