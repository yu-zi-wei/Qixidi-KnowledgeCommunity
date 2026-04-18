<template>
  <div class="text-center mt-20">
    <div v-loading.fullscreen.lock="fullscreenLoading"></div>
  </div>
</template>

<script>
export default {
  name: "logOut",
  data() {
    return {
      fullscreenLoading: false
    }
  },
  created() {
    this.fullscreenLoading = true;
    this.$store.commit('removeToken')//删除 token
    let url = sessionStorage.getItem('url');
    if (url == null) {
      this.$router.push('/')
      return;
    }
    this.$router.push(url)
    //删除url
    sessionStorage.removeItem('url');
  }
}
</script>

<style scoped>

</style>
