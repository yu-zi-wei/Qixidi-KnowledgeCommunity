<template>
  <!-- 栖鸟·枝头：品牌 logo（紫色圆角方块 + 白色栖鸟立于枝头）。
       底色取布局根注入的 --logo-bg（与 app.vue primaryColor 同源，深色模式换亮紫），
       login 页不套布局，由页面根元素自行定义该变量。favicon.ico 是同图形位图版。 -->
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 48 48"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
  >
    <rect class="logo-bg" x="3" y="3" width="42" height="42" rx="10" />
    <!-- 尾羽 -->
    <polygon class="logo-bird" points="15.5,26 4.5,21 13,32.5" />
    <!-- 身体 -->
    <ellipse class="logo-bird" cx="23" cy="30.5" rx="10" ry="8" />
    <!-- 头 -->
    <circle class="logo-bird" cx="30" cy="22" r="6" />
    <!-- 喙 -->
    <polygon class="logo-bird" points="35.5,20.8 41,22.8 35.5,24.6" />
    <!-- 点睛（背景色） -->
    <circle class="logo-eye" cx="31.5" cy="20.8" r="1.4" />
    <!-- 枝条 -->
    <line class="logo-branch" x1="8" y1="39.5" x2="40" y2="39.5" />
  </svg>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ size?: number }>(), { size: 26 });
</script>

<style scoped>
.logo-bg {
  fill: var(--logo-bg, #534ab7);
}

.logo-bird {
  fill: #ffffff;
}

.logo-eye {
  fill: var(--logo-bg, #534ab7);
}

.logo-branch {
  stroke: #ffffff;
  stroke-width: 3;
  stroke-linecap: round;
}
</style>
