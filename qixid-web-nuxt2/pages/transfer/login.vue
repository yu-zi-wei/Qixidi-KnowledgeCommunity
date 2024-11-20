<template>
  <div class="text-center mt-20">
    <div v-loading.fullscreen.lock="fullscreenLoading"></div>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      fullscreenLoading: false
    }
  },
  methods: {
    openFullScreen() {
      this.fullscreenLoading = true;
      setTimeout(() => {
        this.fullscreenLoading = false;
      }, 2000);
    },
    init() {
      this.fullscreenLoading = true;
      let token = this.$route.query.token;
      if (token != undefined && token != {}) {
        //存入token
        this.$store.commit('setToken', token)
      }
      let url = sessionStorage.getItem('url');
      if (url == null) {
        this.$router.push('/')
        return;
      }
      //删除url
      sessionStorage.removeItem('url');
      this.$router.push(url)
    }
  },
  mounted() {
    // this.openFullScreen();
    this.init();
  },
}
</script>
