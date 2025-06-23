<template>
  <n-result status='418' title='418 我是个杯具' description='一切尽在不言中'>
    <template #footer>
      <n-button @click='goHome'>回首页</n-button>
    </template>
  </n-result>
</template>

<script setup lang='ts'>
const router = useRouter();
const goHome = () => {
  router.push('/');
};

</script>

