<template>
  <div class="h-screen flex items-center justify-center">
    <n-result status="404" title="404 页面不存在" description="地址有误或功能开发中">
      <template #footer>
        <n-button @click="goHome">回首页</n-button>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
const router = useRouter();
const goHome = () => {
  router.push('/');
};
</script>
