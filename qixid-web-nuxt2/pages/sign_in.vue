<template>
  <div>
    <navigation-bar-module></navigation-bar-module>
    <div class="module-main">
      <el-skeleton v-if="skeletonLoading" :rows="10" class="mt-10" animated/>
      <el-row v-show="!skeletonLoading" :gutter="20" class="mt-10">
        <el-col :span="5" style="background-color: #fefefe;border-radius: 4px;padding: 20px 0">
          <div class="text-center mt-10 mb-20">
            <el-avatar v-if="userInfo.avatar" :size="100" :src="userInfo.avatar"></el-avatar>
            <el-avatar :size="100" v-else src="/img/tx.jpg"></el-avatar>
            <nuxt-link class="" :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)"
                       target="_blank" rel="noopener">
              <p class="font-bold font-s-20 mt-4 overflow-nowrap-1">{{ userInfo.nickname }}</p>
            </nuxt-link>
            <span class="font-s-13 color-grey">{{ userInfo.occupation }}</span>
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
        </el-col>
        <el-col :span="1" style="height: 1px"/>
        <el-col :span="18" style="background: #fefefe;padding: 0 20px">
          <nuxt-child/>
        </el-col>
      </el-row>
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

<style scoped>

</style>
