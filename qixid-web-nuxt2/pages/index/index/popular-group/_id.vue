<template>
  <div>
    <article-group-list :last="true" :type="2"></article-group-list>
  </div>
</template>

<script>
export default {
  name: "popularGroupId",
  validate({params}) {
    return /^\d+$/.test(params.id);
  },
}
</script>

<style scoped>

</style>
