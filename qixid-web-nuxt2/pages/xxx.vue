<template>
</template>

<script>
export default {
  name: "xxx",
  data() {
    return {
      url: '/',
    }
  },
  methods: {
    jumpIndex() {
      let url = sessionStorage.getItem('url');
      if (url == null) {
        this.$router.push(this.url)
        return;
      }
      this.$router.push(url)
      //删除url
      sessionStorage.removeItem('url');
    },
  },
  created() {
    this.jumpIndex()
  }
}
</script>

<style scoped>

</style>
