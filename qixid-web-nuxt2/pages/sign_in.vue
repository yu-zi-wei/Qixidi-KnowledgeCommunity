<template>
  <div>
    <navigation-bar-module></navigation-bar-module>
    <div class="mt-30">
      <div class="module-main">
        <el-skeleton v-if="skeletonLoading" :rows="10" class="mt-10" animated/>
        <div v-show="!skeletonLoading" :gutter="20" class="mt-10 flex-left">
          <div class="sign-left-list">
            <div class="text-center mt-10 mb-20">
              <el-avatar v-if="userInfo.avatar" :size="100" :src="userInfo.avatar"></el-avatar>
              <el-avatar :size="100" v-else src="/img/tx.jpg"></el-avatar>
              <nuxt-link class="" :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)"
                         target="_blank" rel="noopener">
                <p class="font-bold font-s-20 line-height-24 mt-4 overflow-nowrap-1">{{ userInfo.nickname }}</p>
              </nuxt-link>
              <span class="font-s-13 line-height-24 color-grey">{{ userInfo.occupation }}</span>
            </div>
            <el-menu
              :default-active="$route.path"
              :router="true"
              class="el-menu-vertical-demo">
              <el-menu-item index="/sign_in/daily">
                <span slot="title">每日签到</span>
              </el-menu-item>
              <el-menu-item index="/sign_in/money-changer">
                <span slot="title">我的收获</span>
              </el-menu-item>
              <el-menu-item index="/sign_in/my-harvest">
                <span slot="title">兑换中心</span>
              </el-menu-item>
            </el-menu>
          </div>
          <div style="background: #FFFFFF;padding: 0 20px;" class="flex-1">
            <nuxt-child/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "signIn",
  data() {
    return {
      skeletonLoading: true,
      loading: false,
      logo: "",
      userInfo: {
        nickname: null,
        occupation: null,
        avatar: null,
        createTime: null,
        acurrency: null
      },
    }
  },
  methods: {
    getBasicsUsers() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
      }).finally(() => this.skeletonLoading = false);
    },
  },
  mounted() {
    this.getBasicsUsers();
  }
}
</script>

<style>
.sign-left-list {
  background-color: #FFFFFF;
  border-radius: 4px;
  padding: 20px 10px;
  box-shadow: 0 4px 8px 0 #ecf0f1;
  min-height: 90vh;
  width: 200px;
  margin-right: 20px;
}
</style>
