<template>
  <div class="background-color-fefefe">
    <div class="_module_hiding">
      <navigation-bar-module></navigation-bar-module>
    </div>
    <div class="module-main _module_hiding">
      <nuxt-child/>
    </div>
    <!--    手机端-->
    <div class="_module_explicit">
      <nuxt-child/>
    </div>
  </div>
</template>
<script>
export default {
  name: "index"
}
</script>

<style scoped>
</style>
