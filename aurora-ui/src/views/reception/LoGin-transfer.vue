<template>
  <div>
    <h2> 登录中</h2>
  </div>
</template>

<script>
import {isLogin} from "@/api/login";
import Cookies from "js-cookie";

export default {
  name: "LoGinTransfer",
  methods: {
    isLogin() {
      isLogin().then(res => {
        console.log("res:" + JSON.stringify(res))
      })
    }
  },
  created() {
    let token = this.$route.query.token;
    //获取到token 并保存到cookies中
    // sessionStorage.setItem("Authorization",token)
    // localStorage.setItem("Authorization",token)
    if (token != undefined && token != {}) {
      // this.$store.commit('SET_TOKEN', token);
      Cookies.set("Admin-Token", token);
    }
    //跳转到首页
    this.$router.push('/index')
  },
}
</script>

<style scoped>

</style>
