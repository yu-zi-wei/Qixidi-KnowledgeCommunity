<template>
  <div>
    <navigation-bar-module :isConceal="false"></navigation-bar-module>
    <div class="mt-30">
      <div class="module-main flex-left news-index">
        <div class="flex-1">
          <el-skeleton class="mt-10" :rows="4" animated v-if="loading"/>
          <div class="left-list-menu" v-if="!loading">
            <el-menu :default-active="$route.path"
                     :router="true"
                     class="el-menu-demo">
              <el-menu-item v-for="(item,index) in userNewsList" :index="item.route" style="font-size: 16px"
                            :key="index">
                {{ item.typeInfo }}
              </el-menu-item>
            </el-menu>
          </div>
        </div>
        <div class="flex-9 news-child-content">
          <nuxt-child/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "news",
  data() {
    return {
      userNewsList: [],
      activeName: '1',
      loading: true,
    }
  },
  methods: {
    userNewsListSums() {
      this.$API("/frontDesk/news/list/info", "get").then(res => {
        this.userNewsList = res.data;
        this.loading = false;
      })
    },
  },
  mounted() {
    this.userNewsListSums();
  }
}
</script>

<style scoped>
.news-index {
  padding: 0px 10px;
}

.left-list-menu {
  padding: 0px 10px;
  background: #fefefe;
  border-radius: 4px;
  height: 80ch;
  position: fixed;
}

.news-child-content {
  box-shadow: 0 6px 10px 0 #ecf0f1;
  //border: 1px solid #e2e2e5;
  margin-left: 20px;
  border-radius: 4px;
}
</style>
