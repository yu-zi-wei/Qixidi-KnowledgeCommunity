<template>
  <div class="home-top-index">
    <el-tabs v-model="activeName">
      <el-row>
        <el-col :span="10">
          <el-input v-model="keyword" placeholder="请输入标题关键字"
                    suffix-icon="el-icon-el-input__icon el-icon-search"></el-input>
        </el-col>
      </el-row>
      <el-tab-pane label="文 章" name="article">
        <el-menu :default-active="$route.query.state"
                 text-color="#000000"
                 :active-text-color="themeColor"
                 class="el-menu-vertical-demo" mode="horizontal"
                 @select="handleClick">
          <el-menu-item index="all">全部</el-menu-item>
          <el-menu-item label="已发布" index="published">已发布</el-menu-item>
          <el-menu-item label="审核中" index="review">审核中</el-menu-item>
<!--          <el-menu-item label="未通过" index="failed">未通过</el-menu-item>-->
          <el-menu-item label="草稿箱" index="drafts">草稿箱</el-menu-item>
        </el-menu>
        <div>
          <articleIndex :state="state" :keyword="keyword"></articleIndex>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import articleIndex from './article-index.vue'

export default {
  name: "articleManage",
  components: {
    articleIndex
  },
  data() {
    return {
      themeColor: process.env.THEME_COLOR,
      activeName: 'article',
      // activeIndex: this.$route.path + '?state=' + this.$route.query.state,
      state: null,
      keyword: null,
    }
  },
  methods: {
    handleClick(tab, event) {
      if (tab == 'published') {
        this.state = 2;
      } else if (tab == 'drafts') {
        this.state = 4;
      } else if (tab == 'review') {
        this.state = 1;
      } else {
        this.state = null;
      }
      // this.activeIndex = tab;
      this.$router.push('/user_admin/article_data/article-manage?state=' + tab)
    },
  }
}
</script>

<style>
.el-menu.el-menu--horizontal {
  border: none;
}

.home-top-index {
  background-color: #FFFFFF;
  border-radius: 4px;
  padding: 10px;
  min-height: 100px;
}
</style>
