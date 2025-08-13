<template>
  <div style="width: 100%">
    <el-skeleton style="padding: 20px 10px" :rows="6" animated v-if="initialLoading"/>
    <div v-show="!initialLoading">
      <ul>
        <li v-for="(item,index) in labelList" :key="index" :ref="`searchLabelItem${index}`">
          <div class="flex-left align-items-center" style="padding: 10px 20px 0 20px">
            <div class="mr-10">
              <div  v-if="item.labelCover" v-html="item.labelCover" class="svg-translateY-4"></div>
              <el-image v-else style="width: 50px; height: 50px;border-radius: 4px"
                        src="/img/label.jpg" fit="cover"></el-image>
            </div>
            <div class="flex-8">
              <nuxt-link class="font-bold cursor-pointer mb-8" target="_blank"
                         :to="'/external_info/label-info?data='+item.id+'&type='+2">
                {{ item.labelName }}
              </nuxt-link>
              <div class="font-s-14 color-grey mt-8">
                <span class="mr-8">关注数：{{ item.followNumber == 0 ? '--' : item.followNumber }}</span>
                <span>文章数：{{ item.articleNumber == 0 ? '--' : item.articleNumber }}</span>
              </div>
            </div>
            <div>
              <div class="mt-20">
                <el-button size="small" @click="followClick(item)" :loading="item.buttonLoading"
                           :type="item.isFollow?'primary':''">{{ item.isFollow ? '已关注' : '关注' }}
                </el-button>
              </div>
            </div>
          </div>
          <el-divider></el-divider>
        </li>
      </ul>
      <div v-show="labelList.length==0" style="margin: auto;max-width: 200px">
        <div class="pt-20 text-center">
          <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="60">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-14 color-grey color-grey-2">暂无相关标签</div>
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
  name: "searchLabel",
  data() {
    return {
      cover: "",
      queryParams: {
        labelName: this.$route.query.data,
        uid: null,
        type: 2,
      },
      labelList: [],
      initialLoading: true,
      loginDialog: false,
      userInfo: null,
      animator: null, // 动画器实例
    }
  },
  watch: {
    $route() {
      this.queryParams.labelName = this.$route.query.data;
      this.fdLabelLists();
    },
  },
  methods: {
    followClick(item) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      item.buttonLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API("/user/follow/cancel", "post", null, {targetId: item.id, type: 2,})
          .finally(() => {
            this.fdLabelLists()
            item.buttonLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API("/user/follow/add", "post", null, {targetId: item.id, type: 2,})
          .finally(() => {
            this.fdLabelLists()
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
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    fdLabelLists() {
      this.$API("/white/label/list", "get", this.queryParams).then(res => {
        this.labelList = res;
        this.initialLoading = false;
        this.animator.triggerAllItemsAnimation(this.labelList, 'searchLabelItem');
      })
    },
  },
  mounted() {
    // 初始化动画器
    this.animator = createAnimator(this, 'searchArticle');
    this.fdLabelLists();
    this.getBasicsUsers();
  }
}
</script>

<style scoped>

</style>
