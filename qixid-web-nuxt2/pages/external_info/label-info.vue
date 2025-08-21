<template>
  <div class="module-main-2">
    <div class="label-info-cl flex-space-between">
      <el-skeleton style="padding: 20px 10px" :rows="3" animated v-if="initialLoading"/>
      <div style="line-height: 1px"></div>
      <div v-show="!initialLoading">
        <div class="flex-left">
          <div v-if="labelInfo.labelCover" v-html="labelInfo.labelCover" class="svg-translateY-5- mr-10"></div>
          <div class="font-s-32 font-bold text-center">{{ labelInfo.labelName }}</div>
        </div>
        <div class="font-s-14 color-grey text-center mt-6">
          <span class="mr-6">{{ labelInfo.followNumber }}关注</span>
          <span class="ml-6">{{ labelInfo.articleNumber }}文章</span>
        </div>
      </div>
      <el-button size="small" class="mr-20" @click="followClick(labelInfo)" :loading="followLoading"
                 :type="labelInfo.isFollow?'primary':''">
        {{ labelInfo.isFollow ? '已关注' : '关注' }}
      </el-button>
    </div>
    <div>
      <ArticleSearchList :query-params="queryParams"></ArticleSearchList>
    </div>
    <!--                  登陆弹层组件-->
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>

import ArticleSearchList from "../../components/article/article-search-list.vue";

export default {
  name: "labelInfo",
  components: {ArticleSearchList},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        labelId: this.$route.query.data,
      },
      labelInfo: {
        isFollow: null,
      },
      userInfo: {},
      initialLoading: true,
      loginDialog: false,
      followLoading: false,
    }
  },
  methods: {
    followClick(item) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      this.followLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API('/user/follow/cancel', 'post', null, {targetId: item.id, type: 2,})
          .finally(() => {
            this.fdLabelInfos(this.$route.query.data, this.$route.query.type);
            this.followLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API('/user/follow/add', 'post', null, {targetId: item.id, type: 2,}).finally(() => {
          this.fdLabelInfos(this.$route.query.data, this.$route.query.type);
          this.followLoading = false;
        });
      }
    },
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    fdLabelInfos() {
      let id = this.$route.query.data
      let type = this.$route.query.type
      this.$API(`/white/label/info/${id}/${type}`, 'get').then(res => {
        this.labelInfo = res;
        this.initialLoading = false;
      })
    },
    getBasicsUsers() {
      this.$API('/front-desk/user/basics', 'get').then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
      });
    },
  },
  mounted() {
    this.fdLabelInfos();
    this.getBasicsUsers();
  },

}
</script>

<style scoped>
.label-info-cl {
  align-items: center;
  height: 160px;
  margin-top: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  background: url(
  data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAa0lEQVQoU2NkgIKmQ37/6+w2MYK4xpnX/h9piWDgFL4E54MZhBSdna7FyEiMIpBhYBNxWQcyCWYjTjchKwK5nRGbw9EVga3+/lbvP7LvsCkCGYbiRlyKdtW2QDwD0oFPkbDUMogbCSkCGQYAka1/qtQO9d8AAAAASUVORK5CYII=
  ) repeat;
}

@media (max-width: 510px) {
  .label-info-cl {
    height: 120px;
    margin: 10px;
  }
}

.el-button--small {
  padding: 9px 15px;
}
</style>
