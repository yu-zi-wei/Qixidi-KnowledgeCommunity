<template>
  <div>
    <el-skeleton style="padding: 20px 10px" :rows="6" animated v-if="initialLoading"/>
    <div v-show="!initialLoading">
      <div v-for="(item,index) in userList" :key="index" :ref="`searchUserItem${index}`">
        <div class="flex-space-between padding-10 align-items-center">
          <div class="flex-left">
            <div>
              <el-avatar v-if="item.avatar" :size="60" :src="item.avatar" fit="fill"></el-avatar>
              <el-avatar v-else :size="60" src="/img/tx.jpg" fit="fill"></el-avatar>
            </div>
            <div class="ml-6 mt-8">
              <div class="mb-4">
                <nuxt-link class="hover-cl font-bold" :to="`/user_home/article?uuid=`+$base64.encode(item.uuid)"
                           target="_blank">
                  {{ item.nickname }}
                </nuxt-link>
                <span class="ml-6 font-s-13 color-grey">{{ item.occupation }}</span>
              </div>
              <p class="font-s-14 color-grey mt-8">关注数：{{
                  item.fansFollowCount == 0 ? '--' : item.fansFollowCount
                }}</p>
            </div>
          </div>
          <div>
            <el-button size="small" v-if="userInfo!=null && userInfo.uuid!=item.uuid"
                       @click="followClick(item)" :loading="item.buttonLoading"
                       :type="item.isFollow?'primary':''">{{ item.isFollow ? '已关注' : '关注' }}
            </el-button>
          </div>
        </div>
        <hr class="hr-item"/>
      </div>

      <div v-show="userList.length==0" style="margin: auto;max-width: 200px">
        <div class="pt-20 text-center">
          <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="60">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-14 color-grey-2">暂无相关用户</div>
        </div>
      </div>
    </div>
    <!--                  登陆弹层组件-->
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils'

export default {
  name: "searchUser",
  data() {
    return {
      queryParams: {
        nickname: this.$route.query.data,
        uid: null,
        type: 1,
      },
      userList: [],
      initialLoading: true,
      userInfo: null,
      loginDialog: false,
      animator: null, // 动画器实例
    }
  },
  watch: {
    $route() {
      this.queryParams.nickname = this.$route.query.data;
      this.fdUserLists();
    },
  },
  methods: {
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    followClick(item) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      item.buttonLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API("/user/follow/cancel", "post", null, {targetId: item.uuid, type: 1,})
          .finally(() => {
            this.fdUserLists()
            item.buttonLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API("/user/follow/add", "post", null, {targetId: item.uuid, type: 1,})
          .finally(() => {
            this.fdUserLists()
            item.buttonLoading = false;
          });
      }
    },
    getBasicsUsers() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
      });
    },
    fdUserLists() {
      this.$API("/white/user/list", "get", this.queryParams).then(res => {
        this.userList = res;
        this.initialLoading = false;
        this.animator.triggerAllItemsAnimation(this.userList, 'searchUserItem');
      })
    },
  },
  mounted() {
    // 初始化动画器
    this.animator = createAnimator(this, 'searchArticle');
    this.getBasicsUsers();
    this.fdUserLists();
  }
}
</script>

<style scoped>

</style>
