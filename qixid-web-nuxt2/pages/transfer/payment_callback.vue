<template>
  <div>
    <!--    支付回调中转-->
  </div>
</template>

<script>
export default {
  name: "paymentCallback",
  methods: {
    jumpIndex() {
      let url = sessionStorage.getItem('payUrl');
      if (url == null) {
        this.$router.push('/')
        return;
      }
      this.$router.push(url)
      //删除url
      sessionStorage.removeItem('payUrl');
    }
  },
  mounted() {
    this.jumpIndex()
  }
}
</script>

<style scoped>

</style>
