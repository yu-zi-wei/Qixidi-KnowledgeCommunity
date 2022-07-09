<template>
  <div>
    <div class="container">
      <div class="nav">
        <p class="title">掘金</p>
        <div class="toggle-wrap">
          <label for="toggle-nav">
            <span class="more">三</span>
          </label>
        </div>
        <input type="checkbox" id="toggle-nav">
        <div class="wrap">
          <ul class="label-wrap">
            <li class="label">推荐</li>
            <li class="label">后端</li>
            <li class="label">前端</li>
            <li class="label">Android</li>
            <li class="label">iOS</li>
            <li class="label" @click="ToUserInfo">
              测试
            </li>
            <li class="label" @click="LoginGitee('gitee')">
              gitee
            </li>
            <li class="label">
              <router-link to="/admin/index">
                后台
              </router-link>
            </li>
          </ul>
          <div class="btn-wrap">
            <span class="btn">写文章</span>
          </div>
        </div>
      </div>
    </div>
    <div style="max-width: 80%;margin: auto;">
      <div class="mt-20">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import {LoginGitee,isLogin} from "@/api/login";
import "./css/index.css"

export default {
  name: "admin",
  methods: {
    ToUserInfo() {
      this.$router.push({
        path: '/index/test',
      })
    },
    LoginGitee(type) {
      LoginGitee(type).then(res => {
        if (res.code == 200) {
          window.location.href = res.data.url;
        }else {
          this.$modal.alertError(res.msg);
        }
      })
    }
  },

}
</script>

<style scoped>

</style>
