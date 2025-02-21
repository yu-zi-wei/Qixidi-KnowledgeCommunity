<template>
  <div>
    <navigation-bar-module :userInfo.sync="userInfo" :isConceal="false"></navigation-bar-module>
    <div class="module-main flex-left" v-if="userInfo!=null">
      <div style="width: 360px">
        <!--        侧边栏-->
        <div style="position: fixed">
          <div class="user-admin-left-div">
            <div class="mt-20 flex-left">
              <div>
                <el-avatar class="cursor-pointer" :size="50" v-if="userInfo.avatar"
                           :src="userInfo.avatar"/>
                <el-avatar v-else src="/img/tx.jpg"></el-avatar>
              </div>
              <div class="ml-10">
                <p class="cursor-pointer font-bold font-s-16 overflow-nowrap overflow-nowrap-1"
                   v-text="userInfo.nickname"
                   :title="userInfo.nickname"></p>
                <p class="color-grey font-s-14 line-height-18" v-text="userInfo.occupation==null?'职业-~-':userInfo.occupation"></p>
              </div>
            </div>
            <div :gutter="20" class="mt-15">
              <div style="text-align: center;padding: 0 6px">
                <nuxt-link to="/article/publish-article" target="_blank">
                  <el-button style="width: 100%;" type="primary">发布文章</el-button>
                </nuxt-link>
              </div>
            </div>
            <div class="mt-10">
              <div class="left-list">
                <el-skeleton class="mt-10 mb-10" :rows="12" animated v-if="loading"/>
                <el-menu
                  v-if="!loading"
                  :default-active="analysisUrl($route.path)"
                  :default-openeds="openeds"
                  :router="true"
                  class="el-menu-vertical-demo">
                  <div v-for="item of toUserSidebar">
                    <el-menu-item v-if="item.isList==0" :index="item.route">
                      <template slot="title">
                        <i v-if="item.sidebarIcon!=null" v-html="item.sidebarIcon"></i>
                        <span v-text="item.sidebarName"></span>
                      </template>
                    </el-menu-item>
                    <el-submenu v-else :index="item.route">
                      <template slot="title">
                        <i v-if="item.sidebarIcon!=null" v-html="item.sidebarIcon"></i>
                        <span v-text="item.sidebarName"></span>
                      </template>
                      <el-menu-item-group>
                        <el-menu-item v-for="(levelItem,index) in item.levelList"
                                      :index="levelItem.route"
                                      v-text="levelItem.sidebarName" :key="index"
                                      :title="levelItem.route"></el-menu-item>
                      </el-menu-item-group>
                    </el-submenu>
                  </div>
                </el-menu>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="" style="width: 100%">
        <nuxt-child/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      userInfo: {
        avatar: null,
        nickname: undefined,
        remark: undefined,
      },
      loading: true,
      //默认展开数组
      openeds: [],
      toUserSidebar: [],
    }
  },
  methods: {
    analysisUrl(url) {
      if (url == "/user_admin/article_data/article-manage") {//解决高亮问题
        return "/user_admin/article_data/article-manage?state=all";
      }
      if (url == "/user_admin/dictum/manage") {//解决高亮问题
        return "/user_admin/dictum/manage?state=all";
      }
      return url;
    },
    listSidebar() {
      this.loading = true;
      let data = {type: 2, status: 0}
      this.$API("/white/configure/sidebar/list", this.$get(), data).then(res => {
        this.toUserSidebar = res.data;
        this.toUserSidebar.forEach(item => {
          this.openeds.push(item.route);
        })
      }).finally(() => this.loading = false);
    },
  },
  mounted() {
    this.listSidebar();
  }
}
</script>

<style scoped>
.left-list {
  height: calc(100vh - 300px);
  overflow: auto;
  border: none;
  /*height: 60vh;*/
  margin-top: 10px;
  width: 100%;
}

.left-list::-webkit-scrollbar {
  width: 4px;
  height: 4px;
  background-color: #ced6e0;
}

.left-list::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 2px;
}

.left-list::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}
.user-admin-left-div{
  width: 260px;
  //border: 1px solid #4e5969;
  box-shadow: 0 6px 8px 0 #ecf0f1;
  padding: 0px 10px;
}

</style>
