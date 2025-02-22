<template>
  <div class="index-main">
    <!--    左侧-->
    <div class="index-left-width">
      <div :class="{'index-left-cl':true, 'index-left-cl-true':goTopLoading,'index-left-cl-false':!goTopLoading}">
        <div class="auroora-card mb-15">
          <el-skeleton class="mt-10" style="width: 100%" :rows="4" animated v-show="sidebarDialog"/>
          <el-menu
            v-show="!sidebarDialog"
            :router="true"
            :default-active="$route.path"
            style="width:100px"
            class="el-menu-vertical-demo tool-list-left">
            <el-menu-item v-for="(item,index) in sidebarList" :index="item.route" :title="item.sidebarName"
                          :key="index">
              <nuxt-link :to="item.route">
                <i v-if="item.sidebarIcon!=null" :class="$route.path==item.route?'icon-theme':''"
                   v-html="item.sidebarIcon"></i>
                {{ item.sidebarName }}
              </nuxt-link>
            </el-menu-item>
          </el-menu>
        </div>

        <div class="auroora-card border-1-e6e6e6 border-radius-10" v-if="false">
          <div class="font-bold font-s-14">热门分类</div>
          <hr class="hr-item mb-10 mt-10"/>
          <el-skeleton class="mt-10" :rows="8" animated v-show="labelGroupingDialog"/>
          <el-menu
            v-if="!labelGroupingDialog"
            :default-active="$route.path"
            :router="true"
            class="el-menu-vertical-demo">
            <el-menu-item v-for="(item,index) in labelGroupingList" :index="'/popular-group/'+item.id"
                          :title="item.groupingName" :key="index">
              <nuxt-link :to="'/popular-group/'+item.id" class="overflow-nowrap-1">
                <span class="mr-6 svg-text-center-20-z" v-html="item.icon"></span>
                {{ item.groupingName }}
              </nuxt-link>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
    <!--    中间内容区域-->
    <div class="article-index-content">
      <nuxt-child/>
    </div>
    <!--    右侧-->
    <div class="index-right">
      <div :class="{'recommend-article-div-true':positionCssRight,'recommend-article-div-false':!positionCssRight}">
        <div class="mb-15 sigIn-info padding-10 border-radius-10" style="background-color: rgba(149, 225, 211,0.2);"
             v-show="positionCssRight">
          <div v-show="!reportLoading" class="flex-space-between">
            <div>
              <p class="font-bold mb-4" v-if="isReport">连续签到<span class="color-stand-out ml-4">{{
                  ctnFatalism
                }}天</span>
              </p>
              <p class="font-bold mb-4" v-if="!isReport">{{ $utils.obtainTimePeriod(new Date()) }}好！</p>
              <p class="font-s-13 line-height-16 color-grey-2">点亮在{{ websiteName }}的每一天</p>
            </div>
            <el-button size="medium" @click="signIn">
              {{ isReport ? '已签到' : '去签到' }}
            </el-button>
          </div>
        </div>
        <!--                用户信息-->
        <div class="auroora-card mb-15 overflow-hidden border-1-e6e6e6 border-radius-10" v-show="positionCssRight">
          <div class="fl-right flex-left" v-if="userInfo!=null">
            <div style="background-color: #FC625D" class="aurora-dot mr-6"></div>
            <div style="background-color: #FDBC40" class="aurora-dot mr-6"></div>
            <div style="background-color: #35CD4B" class="aurora-dot"></div>
          </div>
          <el-skeleton :rows="4" animated v-if="userInfoLoading"/>
          <div v-show="!userInfoLoading">
            <div v-if="userInfo==null || userInfo=={}" class="flex-define justify-content-center cursor-pointer"
                 @click="isLoginBinlog=true"
                 title="点击登陆">
              <div class="hover-cl">
                <svg t="1734329555517" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="35239" width="60" height="60">
                  <path
                    d="M633 960c-1.1 0-2.3-0.1-3.4-0.3-1.1-0.2-2.3-0.5-3.4-0.9-33.6-9.7-61.3-21.2-86.4-36.3-25.2-15.1-47.6-33.8-70.4-57.9-29.5-31.1-52.4-67.7-68-107.7-15.6-39.9-23.8-83.1-23.6-127.2 0-37.4 15-71.4 39.1-95.9s57.4-39.6 94.1-39.6 70 15.1 94.1 39.6c24.2 24.6 39.1 58.5 39.1 95.9 0 22.2 9.2 42.4 23.9 57.1 14.8 14.7 35.3 23.9 57.9 23.9 22.7 0 43.2-9.1 58-23.8 14.7-14.6 23.9-34.8 23.9-57.1 0-82-33.3-156.3-87-210.1-53.9-53.9-128.2-87.3-210.2-87.3-58.4 0-114.2 17.2-161.5 47.8-47.1 30.5-85.7 74.4-109.7 127.7-8 17.5-13.9 36.5-17.9 56.8-4 20.4-6 42.2-6 65.1 0 17.1 0.7 39.1 4.5 65.5 3.8 26.5 10.7 57.4 22.9 92.3 2.4 7.1 2 14.5-0.8 20.8-2.7 6.4-7.7 11.6-14.4 14.3-6.7 2.6-13.7 2.1-19.7-0.8-6-2.9-10.9-8.2-13.4-15.3-10.3-29.4-18-58.6-23.1-87.8-5.1-29.5-7.7-59-7.7-89.1 0-27 2.5-52.7 7.3-77 4.9-24.3 12.1-47.2 21.8-68.5C221.3 421.6 266.6 370 321.9 334c55.3-35.9 120.5-56.1 188.6-56.1 96 0 183.1 39.4 246.3 103.2S859.1 532.9 859.1 630c0 37.5-15 71.4-39.2 95.9-24.1 24.5-57.4 39.7-94.1 39.7-36.6 0-69.9-15.2-94.1-39.8-24.2-24.5-39.2-58.5-39.2-95.9 0-22.2-9.1-42.4-23.9-57.1-14.8-14.7-35.3-23.9-57.9-23.9s-43.1 9.1-57.9 23.8c-14.7 14.6-23.9 34.8-23.9 57.1 0 37.1 6.8 73 19.8 106.3s32.1 63.7 56.7 89.8c19.4 20.5 38.5 36.4 60 49.2 21.4 12.8 45.1 22.6 73.7 31h0.1l0.7 0.2c6.8 2.1 12.3 6.8 15.5 12.8 3.3 6.1 4.4 13.5 2.5 20.8-1.5 6-4.8 10.9-9.1 14.4-4.6 3.7-10 5.7-15.8 5.7z m121.2-776.2c-2.1 0-4.1-0.3-6-0.8-1.9-0.5-3.7-1.2-5.6-2.1h-0.2c-39.6-21.8-76.6-37.2-113.9-47.4-37.3-10.1-75-14.7-116.1-14.7-40.9 0-80.7 5.2-119 15.6-38.5 10.3-75.5 25.8-110.9 46.2l-0.2 0.1c-6.2 3.1-12.9 3.4-19.1 1.3-6-2.1-11.3-6.5-14.6-12.9-3.3-6.4-3.9-13.6-2.3-20.1 1.6-6.5 5.5-12.4 11.3-16.3l0.3-0.1c39.3-22.7 80.3-39.8 122.8-51.2C423.2 69.8 467.2 64 512.4 64c44.8 0 86.8 5.3 128.3 16.4 41.1 11 81.8 27.9 124.3 51.1l1 0.5c6.4 3.4 10.7 9.3 12.7 16 1.9 6.3 1.7 13.3-1.1 19.7l-0.4 0.8-0.1 0.3c-2.3 4.5-5.6 8.2-9.6 10.7-4 2.8-8.6 4.3-13.3 4.3zM157.6 416.2c-2.6 0-5.2-0.4-7.7-1.3-2.5-0.8-4.8-2-7.1-3.6h-0.1c-2.8-2.1-5.1-4.7-6.9-7.7-1.8-3-3-6.4-3.6-9.9-0.6-3.6-0.5-7.2 0.2-10.7 0.8-3.4 2.1-6.8 4.1-9.7 20.8-31.3 44.5-59.3 70.9-83.7 26.5-24.6 55.7-45.6 87.4-62.9 66.3-36.4 141.7-54.6 217.2-54.7 75.4 0 151 18 217.3 54.3 31.6 17.2 60.6 38.1 87 62.4s50.1 52 71.1 83c4.1 6.1 5.5 13.5 4.4 20.3s-4.6 13.3-10.4 17.7c-5.8 4.4-12.8 5.9-19.2 4.7-6.5-1.2-12.6-4.9-16.7-11.1-18.5-27.6-39.6-52.2-62.9-73.7-23.3-21.4-48.9-39.8-76.6-54.8-59.1-32.3-126.5-48.3-193.7-48.3-67.3 0.1-134.7 16.3-193.7 48.8-28 15.3-53.8 33.9-77.2 55.6-23.3 21.6-44.2 46.3-62.6 74l-0.1 0.1c-2.5 3.5-5.7 6.3-9.2 8.2-3.8 2-7.9 3-11.9 3z m260.8 534.3c-3.4 0-6.7-0.7-9.9-2.1-3-1.3-5.9-3.4-8.4-6-18.2-19.6-32.2-35.5-45.2-53.3-13.1-17.9-25.2-37.5-39.3-64.5-14.6-27.6-25.7-58.3-33.2-91.2-7.5-32.8-11.4-67.7-11.4-103.8 0-67.2 27.1-128.2 70.8-172.4 43.6-44.1 103.9-71.4 170.2-71.4 66.4 0 126.7 27.3 170.3 71.4 43.7 44.1 70.8 105.1 70.8 172.4 0 7.6-2.9 14.4-7.6 19.3-4.7 5-11.1 8.1-18.2 8.1s-13.5-3.1-18.2-8.1c-4.7-4.9-7.6-11.7-7.6-19.3 0-52.1-21.2-99.4-55.5-133.6-34.3-34.3-81.7-55.6-134.1-55.6-52.3 0-99.7 21.3-134.1 55.6-34.3 34.3-55.5 81.5-55.5 133.6 0 31.5 3.3 61.8 9.7 90 6.4 28.2 15.8 54.5 28.2 78.2 13.3 25.2 24.4 43.3 36.1 59.1 11.6 15.9 24 29.9 40 46.9 0 0.1 0 0.1 0.4 0.5 4.7 5.3 7 12.1 7 18.9 0 6.8-2.5 13.6-7.3 18.8 0 0-0.1 0-0.3 0.3-2.3 2.7-5 4.7-7.9 6-3.1 1.4-6.5 2.2-9.8 2.2z m298.7-82c-25.2 0-48.9-3.4-71-10.1-22.1-6.7-42.5-16.7-61-30-31.6-22.8-56.9-52.8-74.3-86.9-17.4-34.2-26.9-72.3-26.9-111.7 0-7.5 2.9-14.3 7.6-19.3 4.7-4.9 11.1-8 18.2-8s13.5 3.1 18.2 8c4.7 5 7.6 11.8 7.6 19.3 0 30.3 7.3 59.7 20.8 86 13.5 26.4 33.2 49.6 57.9 67.4 14.3 10.3 29.8 18 46.8 23.1 17.1 5.1 35.7 7.7 56.1 7.7 5.4 0 12.8-0.3 21.2-1.1 6.7-0.6 14.1-1.6 21.5-3 6.9-1.3 13.7 0.5 19.1 4.5 5.4 4 9.4 10.3 10.7 17.6 1.3 7.3-0.4 14.6-4.3 20.3-3.8 5.7-9.6 10-16.6 11.4-12.4 2.4-23.9 3.6-32.9 4.3-9.1 0.5-15.8 0.5-18.7 0.5z"
                    fill="#fab1a0" p-id="35240"></path>
                </svg>
                <div class="font-s-13 mt-2 text-center">点击登录</div>
              </div>
            </div>
            <div v-else class="user-admin-info">
              <div class="flex-define">
                <div v-if="userInfo!=null">
                  <el-avatar :size="50" v-if="userInfo.avatar" :src="userInfo.avatar"/>
                  <el-avatar :size="50" v-else src="/img/tx.jpg"></el-avatar>
                </div>
                <div class="ml-8 mt-2">
                  <nuxt-link class="" :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)"
                             target="_blank" rel="noopener">
                    <p class="font-s-18 overflow-nowrap-1">{{ userInfo.nickname }}</p>
                  </nuxt-link>
                  <p class="font-s-13 line-height-24 overflow-nowrap-1 color-grey-2 mt-4">
                    {{ userInfo.occupation == null ? '职业-~-' : userInfo.occupation }}</p>
                </div>
              </div>
              <hr class="hr-item mt-20 mb-20"/>
              <div class="flex-space-between align-items-center">
                <div class="cursor-pointer hover-cl">
                  <nuxt-link :to="`/user_home/follow?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 ">{{ userInfo.followCount }}</p>
                    <p>关注</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer hover-cl">
                  <nuxt-link :to="`/user_home/collection?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 ">{{ userInfo.collectionCount }}</p>
                    <p>收藏</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer hover-cl">
                  <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 ">{{ userInfo.articleCount }}</p>
                    <p>文章</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer hover-cl">
                  <nuxt-link :to="`/dictum/space/content-list?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6">{{ userInfo.dictumCount }}</p>
                    <p>名言</p>
                  </nuxt-link>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--        精选文章-->
        <div class="selected-articles-info">
          <div class="flex-space-between align-items-center">
            <div class="font-bold border-left-2-solid">精选文章</div>
            <div>
              <el-button @click="selectedArticleListApi()"
                         type="text" :loading="selectedArticleLoading" icon="el-icon-refresh">
                刷新
              </el-button>
            </div>
          </div>
          <el-skeleton class="mt-10" :rows="6" animated v-if="selectedArticleLoading"/>
          <ul v-show="!selectedArticleLoading">
            <li v-for="(item,index) in selectedArticleList" :key="index"
                class="recommend-article-item flex-left align-items-center"
                :title="item.articleTitle">
              <div class="flex-1 mr-6">
                <div v-if="index<=2" class="ranking-item-1-3"
                     :style="index==0?'background-color:#eb4d4b':index==1?'background-color:#f9ca24':'background-color:#4cd137'">
                  {{ index + 1 }}
                </div>
                <div v-else class="ranking-item-3-">
                  {{ index + 1 }}
                </div>
              </div>
              <div class="cursor-pointer overflow-nowrap-1 flex-10">
                <nuxt-link class="hover-cl text-underline-hover"
                           :to="`/article/article-details/`+$base64.encode(item.id)" target="_blank"
                           rel="noopener">
                  {{ item.articleTitle }}
                </nuxt-link>
              </div>
            </li>
          </ul>
          <hr class="hr-item mt-15 mb-10" style="margin: 0 20px"/>
        </div>
        <!-- 备案-->
        <el-skeleton class="mt-10" :rows="3" animated v-if="siteInfoLoading"/>
        <div class="mt-10 ml-10 color-grey-2 line-height-24 font-s-13" style="padding: 0 10px" v-if="!siteInfoLoading">
          <a href="https://beian.miit.gov.cn" class="hover-cl" target="_blank"
             rel="nofollow">{{ siteInfo.filings }}</a>
          .
          <span>反馈邮箱：{{ siteInfo.mailbox }}</span>
          .
          <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank"
             class="text-underline-hover">开源地址：<span style="color: #ee5a24">gitee.com</span></a>
          <div>小站已经运行了：{{ siteOperationTime }}</div>
          <div>
            <svg t="1740204474294" class="icon-theme-1 icon-size-18 svg-translateY-4" viewBox="0 0 1024 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="2805">
              <path
                d="M512 106.666667a405.333333 405.333333 0 1 0 405.333333 405.333333A405.333333 405.333333 0 0 0 512 106.666667z m0 768a362.666667 362.666667 0 1 1 362.666667-362.666667 362.666667 362.666667 0 0 1-362.666667 362.666667z"
                fill="#333333" p-id="2806"></path>
              <path
                d="M624.853333 399.146667a21.333333 21.333333 0 0 0 30.293334-30.293334 202.24 202.24 0 1 0 0 286.293334 21.333333 21.333333 0 0 0-30.293334-30.293334 159.573333 159.573333 0 1 1 0-225.706666z"
                fill="#333333" p-id="2807"></path>
            </svg>
            {{ $utils.parseTime(new Date(), '{y}') }} 栖息地
          </div>
        </div>
      </div>
    </div>
    <LoginModule :isLogin="isLoginBinlog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      websiteName: process.env.PROJECT_NAME,
      sidebarDialog: true,
      labelGroupingDialog: true,
      sidebarList: [],
      labelGroupingList: [],
      userInfo: {},
      goTopLoading: false,

      isReport: false,
      reportLoading: true,
      ctnFatalism: 0,
      sumFatalism: 0,

      initialLoading: true,
      selectedArticleLoading: true,
      selectedArticleList: [],

      positionCssRight: true,

      isLoginBinlog: false,
      userInfoLoading: true,

      siteInfoLoading: true,
      siteInfo: true,
      siteOperationTime: null
    }
  },
  methods: {
    signIn() {
      if (this.userInfo != null) {
        this.$router.push("/sign_in/daily");
        return;
      }
      this.isLoginBinlog = true;
    },
    loginDialogMethod(val) {
      this.isLoginBinlog = val;
    },
    gitUserInfo() {
      this.userInfoLoading = false
      this.$API("/front-desk/user/info", this.$get()).then(res => {
        this.userInfo = res.data;
      }).finally(() => this.userInfoLoading = false);
    },
    selectedArticleListApi() {
      this.selectedArticleLoading = true;
      this.$API("/white/article/selected", this.$get()).then(res => {
        this.selectedArticleList = res.data;
      }).finally(() => this.selectedArticleLoading = false);
    },
    getSiteInfo() {
      this.siteInfoLoading = true;
      this.$API("/white/site/info", this.$get()).then(res => {
        this.siteInfo = res.data;
        this.siteOperationTime = this.timeDifference(this.siteInfo.createTime);
      }).finally(() => this.siteInfoLoading = false);
    },
    timeDifference(targetDateString) {
      // 解析目标时间
      const targetDate = new Date(targetDateString);
      // 获取当前时间
      const now = new Date();
      // 计算时间差（毫秒）
      let diffInMillis = now - targetDate;
      // 计算年、月、日
      const oneDayMillis = 24 * 60 * 60 * 1000; // 一天的毫秒数
      const oneMonthMillis = 30 * oneDayMillis; // 大约一个月的毫秒数
      const oneYearMillis = 365 * oneDayMillis; // 大约一年（365天）的毫秒数

      let years = Math.floor(diffInMillis / oneYearMillis);
      diffInMillis -= years * oneYearMillis;
      let months = Math.floor(diffInMillis / oneMonthMillis);
      diffInMillis -= months * oneMonthMillis;
      let days = Math.floor(diffInMillis / oneDayMillis);

      return `${years}年${months}月${days}天`;
    },

    listSidebar() {
      let sidebarList = localStorage.getItem('sidebarList');
      if (sidebarList != null) {
        this.sidebarList = JSON.parse(sidebarList);
        this.sidebarDialog = false
      }
      let labelGroupingList = localStorage.getItem('labelGroupingList');
      if (sidebarList != null) {
        this.labelGroupingList = JSON.parse(labelGroupingList);
        this.labelGroupingDialog = false
      }
      this.$API("/frontDesk/user/report/list", this.$get()).then(res => {
        if (res.data != null) {
          this.ctnFatalism = res.data.ctnFatalism;
          this.isReport = res.data.isReport;
          this.sumFatalism = res.data.sumFatalism;
        }
      }).finally(() => this.reportLoading = false);

      this.selectedArticleListApi();
      this.getSiteInfo();
      this.$API("/white/label//grouping/list", this.$get(), {pageNum: 0, pageSize: 8,}).then(res => {
        this.labelGroupingList = res.rows;
        localStorage.setItem("labelGroupingList", JSON.stringify(this.labelGroupingList));
      }).finally(() => this.labelGroupingDialog = false);

      this.$API("/white/configure/sidebar/list", this.$get(), {type: 1, status: 0}).then(res => {
        this.sidebarList = res.data;
        localStorage.setItem("sidebarList", JSON.stringify(this.sidebarList));
      }).finally(() => this.sidebarDialog = false);
    },
    handleScroll() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      if (scrollTop > (clientHeight / 2)) {
        this.goTopLoading = true;
      } else {
        this.goTopLoading = false;
      }
      // let scrollHeight = document.documentElement.scrollHeight
      if (scrollTop > (clientHeight / 1)) {
        this.positionCssRight = false;
      } else {
        this.positionCssRight = true;
      }
    },
  },
  mounted() {
    this.gitUserInfo();
    this.listSidebar();
    window.addEventListener('scroll', this.handleScroll, true);// 向页面添加滚动事件
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.handleScroll, true)
  }
}
</script>

<style scoped>
@import url("static/css/server/pc/index/index.css");

.el-menu-item {
  height: 42px;
  line-height: 42px;
  width: 100%;
  border-radius: 0px;
}

.el-button--medium {
  padding: 10px 20px;
}

</style>
