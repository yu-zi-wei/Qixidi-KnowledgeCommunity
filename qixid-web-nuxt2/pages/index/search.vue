<template>
  <div style="width: 1000px;margin: auto">
    <el-menu :default-active="$route.path"
             class="el-menu-demo" mode="horizontal"
             @select="handleSelect"
             text-color="#2f3542"
             active-text-color="#8854d0">
      <el-menu-item index="/search/data">综合</el-menu-item>
      <el-menu-item index="/search/article">文章</el-menu-item>
      <el-menu-item index="/search/label">标签</el-menu-item>
      <el-menu-item index="/search/user">用户</el-menu-item>
    </el-menu>
    <div class="data-page">
      <nuxt-child/>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      query: {
        data: this.$route.query.data,
      }
    }
  },
  methods: {
    handleSelect(path) {
      // console.log("data",data)
      this.$router.push({
        path: path,
        query: {
          data: this.$route.query.data,
        }
      })
    }
  },
  mounted() {

  }
}
</script>

<style scoped>
.data-page {
  margin-top: 10px;
  background-color: #fefefe;
  border-radius: 4px;
  padding-bottom: 20px;
}
</style>
