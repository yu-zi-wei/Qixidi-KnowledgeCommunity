<template>
  <div>
    <n-card title="数据看板">
      <n-result status="info" title="P0 开发中" description="站点核心数据统计将在此展示（文章/评论/用户增长等）">
        <template #footer>
          <span class="text-sm opacity-60">开发计划见 docs/开发需求与设计.md</span>
        </template>
      </n-result>
    </n-card>
  </div>
</template>

<script setup lang="ts">
// TODO(P0-2): 数据看板——站点核心指标卡片 + 近期趋势（复用后台 census 接口）
</script>
