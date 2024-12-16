<template>
  <div class="index-main">
    <!--    左侧-->
<!--    <div style="width: 80px"></div>-->
    <div class="index-left-width">
      <div :class="{'index-left-cl':true, 'index-left-cl-true':goTopLoading,'index-left-cl-false':!goTopLoading}">
        <div class="auroora-card mb-15">
          <el-skeleton class="mt-10" style="width: 100%" :rows="4" animated v-show="sidebarDialog"/>
          <el-menu
            v-show="!sidebarDialog"
            :router="true"
            :default-active="$route.path"
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
              <div>
                <!--                <svg t="1667140071514" class="icon-theme-stand-out" viewBox="0 0 1024 1024" version="1.1"-->
                <!--                     xmlns="http://www.w3.org/2000/svg" p-id="2503" width="40" height="40">-->
                <!--                  <path-->
                <!--                    d="M511.965867 605.252267a259.754667 259.754667 0 0 1-185.002667-76.663467c-49.425067-49.425067-76.629333-115.131733-76.629333-185.002667s27.204267-135.5776 76.629333-185.002666a259.9936 259.9936 0 0 1 185.002667-76.629334c69.905067 0 135.611733 27.204267 185.0368 76.629334 49.390933 49.425067 76.629333 115.131733 76.629333 185.002666s-27.2384 135.5776-76.629333 185.002667a259.6864 259.6864 0 0 1-185.0368 76.663467z m0-461.243734a199.850667 199.850667 0 0 0-199.611734 199.5776 199.850667 199.850667 0 0 0 199.611734 199.611734 199.816533 199.816533 0 0 0 199.611733-199.611734 199.816533 199.816533 0 0 0-199.611733-199.5776z m0 0"-->
                <!--                    p-id="2504"></path>-->
                <!--                  <path-->
                <!--                    d="M930.645333 942.011733a31.0272 31.0272 0 0 1-30.139733-23.790933A397.653333 397.653333 0 0 0 512 611.6352h-0.4096a398.336 398.336 0 0 0-210.807467 60.347733 30.993067 30.993067 0 1 1-32.836266-52.599466 459.844267 459.844267 0 0 1 243.575466-69.802667H512c53.0432 0 105.028267 8.942933 154.589867 26.5216a459.946667 459.946667 0 0 1 132.9152 74.001067 461.346133 461.346133 0 0 1 161.3824 253.713066 31.095467 31.095467 0 0 1-30.242134 38.1952z m0 0M93.354667 942.011733a31.061333 31.061333 0 0 1-30.208-38.1952 458.6496 458.6496 0 0 1 69.461333-155.5456 31.061333 31.061333 0 0 1 43.178667-7.816533 31.061333 31.061333 0 0 1 7.816533 43.2128 394.410667 394.410667 0 0 0-60.074667 134.519467 31.1296 31.1296 0 0 1-30.173866 23.825066z m0 0"-->
                <!--                    p-id="2505"></path>-->
                <!--                </svg>-->
                <svg t="1734163719319" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="13371" width="60" height="60">
                  <path d="M524.8 381.44m-322.56 0a322.56 322.56 0 1 0 645.12 0 322.56 322.56 0 1 0-645.12 0Z"
                        fill="#DBDEE3" p-id="13372"></path>
                  <path d="M673.28 592.384m-32.256 0a32.256 32.256 0 1 0 64.512 0 32.256 32.256 0 1 0-64.512 0Z"
                        fill="#CFD4DC" p-id="13373"></path>
                  <path d="M479.232 188.416m-19.456 0a19.456 19.456 0 1 0 38.912 0 19.456 19.456 0 1 0-38.912 0Z"
                        fill="#CFD4DC" p-id="13374"></path>
                  <path d="M733.696 534.016m-12.8 0a12.8 12.8 0 1 0 25.6 0 12.8 12.8 0 1 0-25.6 0Z" fill="#CFD4DC"
                        p-id="13375"></path>
                  <path d="M606.208 206.848m-51.712 0a51.712 51.712 0 1 0 103.424 0 51.712 51.712 0 1 0-103.424 0Z"
                        fill="#CFD4DC" p-id="13376"></path>
                  <path
                    d="M915.456 283.136c-5.632-30.72-30.208-56.32-69.12-75.264 10.752 10.752 17.408 23.04 19.968 36.352 13.824 79.872-134.144 172.544-331.264 207.36-197.12 34.304-368.128-2.048-382.464-81.92-4.096-24.576 6.656-49.664 29.184-74.752C122.88 336.896 91.136 383.488 98.816 427.008c16.384 94.208 212.992 138.752 438.784 98.816 225.28-39.936 394.752-148.48 377.856-242.688zM324.096 928.768c-27.648 0-39.424-34.816-17.92-52.224 19.968-15.872 43.52-29.184 68.608-30.208 61.952-3.584 64.512 37.888 101.888 39.424s53.248-68.608 131.072-68.608c31.744 0 76.288 27.136 113.664 54.784 24.576 17.92 11.776 56.832-18.944 56.832H324.096z"
                    fill="#CFD4DC" p-id="13377"></path>
                  <path
                    d="M322.56 928.768c-29.696 0-39.936-39.424-13.824-53.76 13.312-7.68 28.672-12.8 46.592-13.824 65.536-3.584 68.608 37.888 108.032 39.424s56.32-68.608 139.264-68.608c32.768 0 77.312 22.528 114.688 45.568 23.552 14.848 13.312 51.2-14.848 51.2H322.56z"
                    fill="#DBDEE3" p-id="13378"></path>
                  <path d="M341.504 901.12m-8.704 0a8.704 8.704 0 1 0 17.408 0 8.704 8.704 0 1 0-17.408 0Z"
                        fill="#CFD4DC" p-id="13379"></path>
                  <path d="M612.352 873.984m-14.848 0a14.848 14.848 0 1 0 29.696 0 14.848 14.848 0 1 0-29.696 0Z"
                        fill="#CFD4DC" p-id="13380"></path>
                  <path d="M648.704 866.816m-7.68 0a7.68 7.68 0 1 0 15.36 0 7.68 7.68 0 1 0-15.36 0Z" fill="#CFD4DC"
                        p-id="13381"></path>
                  <path
                    d="M593.92 863.232c0 43.52 0 88.576-69.632 88.576s-69.632-45.056-69.632-88.576 34.816-78.848 69.632-78.848 69.632 35.328 69.632 78.848z"
                    fill="#C0C6CE" p-id="13382"></path>
                  <path
                    d="M628.736 727.04c0 57.856-46.592 93.184-104.448 93.184S419.84 784.896 419.84 727.04c0-57.856 51.712-104.448 104.448-104.448 51.712 0 104.448 46.592 104.448 104.448z"
                    fill="#FFBC80" p-id="13383"></path>
                  <path
                    d="M608.768 724.992c0 46.592-37.888 75.264-84.48 75.264s-84.48-28.672-84.48-75.264 41.984-82.432 84.48-82.432c41.984 0 84.48 35.328 84.48 82.432z"
                    fill="#F9E8E6" p-id="13384"></path>
                  <path d="M470.528 758.272m-25.6 0a25.6 25.6 0 1 0 51.2 0 25.6 25.6 0 1 0-51.2 0Z" fill="#FF8080"
                        opacity=".2" p-id="13385"></path>
                  <path d="M578.56 758.272m-25.6 0a25.6 25.6 0 1 0 51.2 0 25.6 25.6 0 1 0-51.2 0Z" fill="#FF8080"
                        opacity=".2" p-id="13386"></path>
                  <path d="M485.888 738.816m-17.408 0a17.408 17.408 0 1 0 34.816 0 17.408 17.408 0 1 0-34.816 0Z"
                        fill="#FF9933" p-id="13387"></path>
                  <path d="M565.76 738.816m-17.408 0a17.408 17.408 0 1 0 34.816 0 17.408 17.408 0 1 0-34.816 0Z"
                        fill="#FF9933" p-id="13388"></path>
                  <path
                    d="M510.976 879.616c-4.608 4.608-11.776 4.608-16.384 0l-32.768-32.768c-4.608-4.608-4.608-11.776 0-16.384 4.608-4.608 11.776-4.608 16.384 0l32.768 32.768c4.608 4.608 4.608 11.776 0 16.384zM538.112 879.616c4.608 4.608 11.776 4.608 16.384 0l32.768-32.768c4.608-4.608 4.608-11.776 0-16.384-4.608-4.608-11.776-4.608-16.384 0l-32.768 32.768c-4.096 4.608-4.096 11.776 0 16.384z"
                    fill="#DBDEE3" p-id="13389"></path>
                  <path
                    d="M524.288 593.408c-64 0-116.224 52.224-116.224 116.224s52.224 116.224 116.224 116.224 116.224-52.224 116.224-116.224-52.224-116.224-116.224-116.224z m0 226.816c-57.856 0-104.448-35.328-104.448-93.184 0-57.856 51.712-104.448 104.448-104.448 51.712 0 104.448 46.592 104.448 104.448 0 57.856-46.592 93.184-104.448 93.184z"
                    fill="#FFFFFF" opacity=".5" p-id="13390"></path>
                  <path
                    d="M501.76 966.144h-2.048c-9.216 0-16.384-7.168-16.384-16.384v-60.416c0-9.216 7.168-16.384 16.384-16.384h2.048c9.216 0 16.384 7.168 16.384 16.384v60.416c0 8.704-7.168 16.384-16.384 16.384zM548.864 966.144h-2.048c-9.216 0-16.384-7.168-16.384-16.384v-60.416c0-9.216 7.168-16.384 16.384-16.384h2.048c9.216 0 16.384 7.168 16.384 16.384v60.416c0 8.704-7.68 16.384-16.384 16.384z"
                    fill="#CFD4DC" p-id="13391"></path>
                  <path
                    d="M501.76 641.024s2.048 18.432 13.312 18.432 9.216-14.336 9.216-14.336 0.512 14.336 12.288 14.336c12.288 0 8.704-18.432 8.704-18.432H501.76z"
                    fill="#FFBC80" p-id="13392"></path>
                  <path
                    d="M292.352 714.752h-9.216v-9.216c0-4.608-4.096-8.704-8.704-8.704s-8.704 4.096-8.704 8.704v9.216H256c-4.608 0-8.704 4.096-8.704 8.704s4.096 8.704 8.704 8.704h9.216v9.216c0 4.608 4.096 8.704 8.704 8.704s8.704-4.096 8.704-8.704V732.16H291.84c4.608 0 8.704-4.096 8.704-8.704s-3.072-8.704-8.192-8.704z"
                    fill="#DBDEE3" p-id="13393"></path>
                  <path d="M485.888 738.816m-17.408 0a17.408 17.408 0 1 0 34.816 0 17.408 17.408 0 1 0-34.816 0Z"
                        fill="#FF9933" p-id="13394"></path>
                  <path d="M847.36 123.392m-21.504 0a21.504 21.504 0 1 0 43.008 0 21.504 21.504 0 1 0-43.008 0Z"
                        fill="#DBDEE3" p-id="13395"></path>
                  <path d="M733.696 762.88m-21.504 0a21.504 21.504 0 1 0 43.008 0 21.504 21.504 0 1 0-43.008 0Z"
                        fill="#DBDEE3" p-id="13396"></path>
                </svg>

                <div class="font-s-13 mt-2 text-center hover-fw-bold hover-cl">点击登录</div>
              </div>
            </div>
            <div v-else class="user-admin-info">
              <div class="flex-define">
                <div v-if="userInfo!=null">
                  <el-avatar :size="50" v-if="userInfo.avatar" :src="userInfo.avatar"/>
                  <el-avatar :size="50" v-else src="/img/tx.jpg"></el-avatar>
                </div>
                <div class="ml-8 mt-4">
                  <nuxt-link class="" :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)"
                             target="_blank" rel="noopener">
                    <p class="font-s-18 overflow-nowrap-1">{{ userInfo.nickname }}</p>
                  </nuxt-link>
                  <p class="font-s-13 line-height-24 overflow-nowrap-1 color-grey-2">
                    {{ userInfo.occupation == null ? '职业-~-' : userInfo.occupation }}</p>
                </div>
              </div>
              <hr class="hr-item mt-20 mb-20"/>
              <div class="flex-space-between align-items-center">
                <div class="cursor-pointer">
                  <nuxt-link :to="`/user_home/follow?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 color-stand-out">{{ userInfo.followCount }}</p>
                    <p class="hover-cl">关注</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer">
                  <nuxt-link :to="`/user_home/collection?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 color-stand-out">{{ userInfo.collectionCount }}</p>
                    <p class="hover-cl">收藏</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer">
                  <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 color-stand-out">{{ userInfo.articleCount }}</p>
                    <p class="hover-cl">文章</p>
                  </nuxt-link>
                </div>
                <div class="cursor-pointer">
                  <nuxt-link :to="`/dictum/space/content-list?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                             rel="noopener">
                    <p class="flex-center mb-6 color-stand-out">{{ userInfo.dictumCount }}</p>
                    <p class="hover-cl">名言</p>
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
          .
          <span>小站已经运行了：<span>{{ siteOperationTime }}</span></span>
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

</style>
