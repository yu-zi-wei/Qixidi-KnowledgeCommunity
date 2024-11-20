<template>
  <div class="home-top-index">
    <el-tabs v-model="dictum">
      <el-tab-pane label="名 言" name="dictum" style="padding: 0 10px">
        <el-row>
          <el-col :span="10">
            <el-input v-model="keyword" placeholder="请输入标题关键字"
                      suffix-icon="el-icon-el-input__icon el-icon-search"></el-input>
          </el-col>
        </el-row>
        <el-menu :default-active="$route.query.state"
                 text-color="#0a3d62"
                 :active-text-color="themeColor"
                 class="el-menu-demo" mode="horizontal"
                 @select="handleClick">
          <el-menu-item index="all">全部</el-menu-item>
          <el-menu-item label="已发布" index="open">公开</el-menu-item>
          <el-menu-item label="审核中" index="private">私有</el-menu-item>
          <!--                              <el-menu-item label="未通过" index="failed">未通过</el-menu-item>-->
          <!--          <el-menu-item label="草稿箱" index="drafts">草稿箱</el-menu-item>-->
        </el-menu>

        <div>
          <dictumManageList :state="state" :keyword="keyword"></dictumManageList>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

export default {
  name: "dictumManage",
  data() {
    return {
      themeColor: process.env.THEME_COLOR,
      dictum: 'dictum',
      activeIndex: this.$route.path + '?state=' + this.$route.query.state,
      state: null,
      keyword: null,
    }
  },
  methods: {
    handleClick(tab, event) {
      if (tab == 'open') {
        this.state = 1;
      } else if (tab == 'private') {
        this.state = 2;
      } else {
        this.state = null;
      }
      this.activeIndex = tab;
      this.$router.push('/user_admin/dictum/manage?state=' + tab)
    },
  },
  mounted() {

  }
}
</script>

<style scoped>
.home-top-index {
  background-color: #fefefe;
  border-radius: 4px;
  padding: 20px;
  min-height: 100px;
}
</style>
