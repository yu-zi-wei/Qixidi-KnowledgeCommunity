<template>
  <div class="mt-10 mb-10">
    <el-tabs v-model="activeName" tab-position="left" @tab-click="handleClick">
      <el-tab-pane label="关注用户" name="followUser">
        <div style="min-height: 200px;">
          <el-skeleton class="mt-10" v-if="loginLoading" :rows="4" animated/>
          <ul v-show="followArrList.length>0 && !loginLoading" class="follow-cl-ul">
            <li v-for="item of followArrList">
              <div style="padding: 10px 20px 0 20px" class="flex-left align-items-center">
                <div>
                  <el-avatar v-if="item.avatar" :size="60"
                             :src="item.avatar" fit="fill"></el-avatar>
                  <el-avatar v-else src="/img/tx.jpg" :size="60" fit="fill"></el-avatar>
                </div>
                <div class="flex-space-between">
                  <div class="ml-10 mb-6">
                    <div>
                <span class="cursor-pointer color-theme text-underline-hover"
                      @click="routeJumpWindowUuid('/user_home/article',item.uuid)"
                      v-text="item.nickname"></span>
                      <span class="ml-8 font-s-13 color-grey">{{ item.occupation }}</span>
                    </div>
                    <div class="font-s-14 color-grey mt-15">
                      <span class="mr-8">关注数：{{ item.fansFollowCount == 0 ? '--' : item.fansFollowCount }}</span>
                      <span>关注时间：{{ $utils.parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                    </div>
                  </div>
                  <div>
                    <el-button size="small" type="primary" @click="followClick(item,item.uuid,1)"
                               :loading="item.buttonLoading">已关注
                    </el-button>
                  </div>
                </div>
              </div>
              <el-divider></el-divider>
            </li>
          </ul>
          <div v-if="followArrList.length==0 && !loginLoading"
               style="text-align: center;margin-top: 10px">
            <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="2698" width="40" height="50">
              <path
                d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
                p-id="2699"></path>
            </svg>
            <div class="font-s-13 color-grey">暂无关注</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关注标签" name="followLabel">
        <div style="min-height: 200px;">
          <el-skeleton class="mt-10" v-if="loginLoading" :rows="4" animated/>
          <ul v-show="followArrList.length>0 && !loginLoading" class="follow-cl-ul">
            <li v-for="item of followArrList">
              <div class="flex-left align-items-center" :gutter="20" style="padding: 10px 20px 0 20px">
                <div v-html="item.labelCover" class="mr-10"></div>
                <div class="flex-8">
                  <p class="font-bold cursor-pointer mb-8 text-underline-hover" @click="jumpLabel(item)"
                     v-text="item.labelName"></p>
                  <div class="font-s-14 color-grey">
                    <span class="mr-8">文章数：{{ item.articleNumber == 0 ? '--' : item.articleNumber }}</span>
                    <span>关注时间：{{ $utils.parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                  </div>
                </div>
                <div>
                  <div class="mt-20">
                    <el-button :id="item.id" size="mini" @click="followClick(item,item.id,2)"
                               :loading="item.buttonLoading" type="primary">已关注
                    </el-button>
                  </div>
                </div>
              </div>
              <el-divider></el-divider>
            </li>
          </ul>
          <div v-if="followArrList.length==0 && !loginLoading"
               style="text-align: center;margin-top: 10px">
            <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="2698" width="40" height="50">
              <path
                d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
                p-id="2699"></path>
            </svg>
            <div class="font-s-13 color-grey">暂无关注</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关注专栏" name="followSpecial ">
        <div style="text-align: center;margin: 10px 0">
          <svg t="1685706589002" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="5864" width="45" height="45">
            <path d="M1464.3 279.7" fill="#1296db" p-id="5865"></path>
            <path
              d="M512 960C264.6 960 64 759.4 64 512S264.6 64 512 64s448 200.6 448 448-200.6 448-448 448z m0-72c207.7 0 376-168.3 376-376S719.7 136 512 136 136 304.3 136 512s168.3 376 376 376z m0-120"
              p-id="5866"></path>
            <path d="M512 638c-19.8 0-36-16.2-36-36V314c0-19.8 16.2-36 36-36s36 16.2 36 36v288c0 19.8-16.2 36-36 36z"
                  p-id="5867"></path>
            <path d="M512 710m-36 0a36 36 0 1 0 72 0 36 36 0 1 0-72 0Z" fill="#1296db" p-id="5868"></path>
          </svg>
          <div class="font-s-14 color-grey">功能待开发</div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关注专辑" name="followCircle">
        <div style="text-align: center;margin: 10px 0">
          <svg t="1685706589002" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="5864" width="45" height="45">
            <path d="M1464.3 279.7" fill="#1296db" p-id="5865"></path>
            <path
              d="M512 960C264.6 960 64 759.4 64 512S264.6 64 512 64s448 200.6 448 448-200.6 448-448 448z m0-72c207.7 0 376-168.3 376-376S719.7 136 512 136 136 304.3 136 512s168.3 376 376 376z m0-120"
              p-id="5866"></path>
            <path d="M512 638c-19.8 0-36-16.2-36-36V314c0-19.8 16.2-36 36-36s36 16.2 36 36v288c0 19.8-16.2 36-36 36z"
                  p-id="5867"></path>
            <path d="M512 710m-36 0a36 36 0 1 0 72 0 36 36 0 1 0-72 0Z" fill="#1296db" p-id="5868"></path>
          </svg>
          <div class="font-s-14 color-grey">功能待开发</div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: "userFollow",
  data() {
    return {
      cover: "",
      activeName: 'followUser',
      queryParams: {
        uid: null,
        type: 1,
      },
      followArrList: [],
      loginLoading: true,
    }
  },

  watch: {
    // 监听路由是否变化
    '$route'() {
      this.followLists();
    }
  },

  methods: {
    followClick(item, id, type) {
      item.buttonLoading = true;
      this.$API("/user/follow/cancel", "post", null, {targetId: id, type: type,}).finally(() => {
        this.followLists();
        item.buttonLoading = false;
      });
    },
    routeJumpWindowUuid(url, id) {
      let routeInfo = this.$router.resolve({
        path: url,
        query: {uuid: this.$base64.encode(id)},
      });
      window.open(routeInfo.href, '_blank');
    },
    jumpLabel(item) {
      let routeInfo = this.$router.resolve({
        path: '/external_info/label-info',
        query: {data: item.id, type: 2,}
      });
      window.open(routeInfo.href, '_blank');
    },
    handleClick(tab) {
      switch (tab.paneName) {
        case "followUser":
          this.queryParams.type = 1;
          break
        case "followLabel":
          this.queryParams.type = 2;
          break
        case "followSpecial":
          break
        case "followCircle":
          break
        default:
          break
      }
      this.followLists();
    },
    followLists() {
      this.loginLoading = true;
      this.queryParams.uid = this.$base64.decode(this.$route.query.uuid)
      this.$API(`/white/user/follow/list/${this.queryParams.uid}/${this.queryParams.type}`, "get").then(res => {
        this.followArrList = res.data;
      }).finally(() => this.loginLoading = false);
    }
  },
  mounted() {
    this.followLists();
  }
}
</script>

<style scoped>
.follow-cl-ul {
  max-height: 50vh;
  overflow: scroll;
  overflow-x: hidden;
}

.follow-cl-ul::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: #ced6e0;
}

.follow-cl-ul::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 2px;
}

.follow-cl-ul::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}
</style>
