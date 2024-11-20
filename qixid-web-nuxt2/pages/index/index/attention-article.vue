<template>
  <div class="mt-2">
    <div class="follow-list-admin">
      <el-skeleton :rows="4" animated v-if="loading"/>
      <div v-show="!loading">
        <p class="font-s-16 font-bold mb-10 color-theme">关注列表</p>
        <div class="flex-left">
          <div class="mr-15 cursor-pointer" style="max-width: 60px" v-for="(item,index) in followRoleArr.slice(0,10)"
               :key="index"
               :title="item.nickname">
            <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(item.uuid)"
                       target="_blank">
              <el-avatar size="medium" :src="item.avatar"></el-avatar>
              <p class="color-grey font-s-13 overflow-nowrap-1">{{ item.nickname }}</p>
            </nuxt-link>
          </div>
        </div>
        <div v-if="followRoleArr.length>10"
             class="fl-right font-s-13 color-blue cursor-pointer hover-color color-blue2">
          查看更多
        </div>
      </div>
    </div>
    <div class="background-color-fefefe padding-10">
      <el-skeleton :rows="4" animated v-if="loading"/>
      <article-group-list :last="true" :type="1" v-show="!loading"></article-group-list>
    </div>
  </div>
</template>

<script>
export default {
  name: "attentionArticle",
  data() {
    return {
      followRoleArr: [],
      userInfo: null,
      isOwn: false,
      loginDialog: false,
      loading: true,
    }
  },
  methods: {
    getBasicsUsers() {
      //  获取用户关注列表
      this.$API("/user/follow/list/" + 1, "get").then(res => {
        this.followRoleArr = res.data;
      }).finally(() => this.loading = false);
    },
  },
  mounted() {
    this.getBasicsUsers();
  }
}
</script>

<style scoped>
.follow-list-admin {
  background-color: #fefefe;
  margin-bottom: 10px;
  border-radius: 4px;
  padding: 10px;
}
</style>
