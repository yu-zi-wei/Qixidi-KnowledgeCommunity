<template>
  <div>
    <div class="mb-40">
      <div class="flex-space-between">
        <div style="min-height: 1px"></div>
        <!--      热门分类-->
        <div class="label-grouping-div">
          <el-menu
            v-if="!labelGroupingDialog"
            :default-active="$route.path"
            :router="true"
            mode="horizontal"
            text-color="rgba(0, 0, 0, 0.7)"
            :active-text-color="themeColor"
            class="el-menu-demo">
            <el-menu-item v-for="(item,index) in sidebarList" :index="item.route" :title="item.sidebarName"
                          :key="index">
              <nuxt-link :to="item.route">
                <span class="font-s-16">{{ item.sidebarName }}</span>
              </nuxt-link>
            </el-menu-item>
            <el-menu-item>
              <span class="color-grey-3">|</span>
            </el-menu-item>
            <el-menu-item v-for="(item,index) in labelGroupingList" :index="'/popular-group/'+item.id"
                          :title="'分类：'+item.groupingName" :key="index">
              <nuxt-link :to="'/popular-group/'+item.id" class="font-s-14">
                <span class="font-s-15">{{ item.groupingName }}</span>
              </nuxt-link>
            </el-menu-item>
          </el-menu>
        </div>
        <div style="min-height: 1px"></div>
      </div>
      <div class="_module_explicit mt-20">
        <div class="marquee">
          <p class="color-grey">📱 手机端只开放部分功能 | 💻 推荐使用PC端体验完整功能...</p>
        </div>
      </div>
    </div>
    <div class="index-main">
      <!--      左侧-->
      <div style="line-height: 1px">
      </div>
      <!--    中间侧-->
      <div class="article-index-content">
        <nuxt-child/>
      </div>
      <!--    右侧-->
      <div class="index-right _module_hiding">
        <div :class="{'recommend-article-div-true':positionCssRight,'recommend-article-div-false':!positionCssRight}">
          <div class="mb-15 sigIn-info padding-10 border-radius-10"
               v-show="positionCssRight">
            <div v-show="!reportLoading" class="flex-space-between">
              <div>
                <p class="font-bold font-s-16 line-height-28" v-if="isReport">连续签到<span
                  class="color-stand-out ml-4">
                  {{ ctnFatalism }}天</span>
                </p>
                <p class="font-bold font-s-16 line-height-28" v-if="!isReport">{{
                    $utils.obtainTimePeriod(new Date())
                  }}好！</p>
                <p class="font-s-14 line-height-18 color-grey">点亮在{{ websiteName }}的每一天</p>
              </div>
              <el-button size="medium" @click="signIn">
                {{ isReport ? '已签到' : '去签到' }}
              </el-button>
            </div>
          </div>
          <!--            站点总数据-->
          <div class="website-data mt-10" v-show="positionCssRight">
            <p class="font-s-16 mb-10">
              <svg t="1746426533762" class="icon icon-size-16 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="3564">
                <path
                  d="M921.6 0a102.4 102.4 0 0 0-102.4 102.4v819.2a102.4 102.4 0 0 0 204.8 0V102.4A102.4 102.4 0 0 0 921.6 0zM204.8 230.4a102.4 102.4 0 0 0-204.8 0v691.2a102.4 102.4 0 0 0 204.8 0V230.4z m409.6 268.8a102.4 102.4 0 0 0-204.8 0v422.4a102.4 102.4 0 0 0 204.8 0V499.2z"
                  fill="#6F8DBB" p-id="3565"></path>
              </svg>
              <span class="ml-2">站点数据</span>
            </p>
            <hr class="hr-item mb-10"/>
            <el-skeleton :rows="3" animated v-if="!statDataInfoLoading"/>
            <div v-if="statDataInfoLoading" style="padding: 2px">
              <div class="flex-space-between text-center line-height-20"
                   v-if="statDataInfoLoading">
                <div title="点击跳转">
                  <nuxt-link to="/white/user_lit" rel="noopener">
                    <h4 class="color-theme font-s-16">
                      <countTo :startVal='0' :endVal='statDataInfoVo.userCount' :duration='2000'></countTo>
                    </h4>
                    <div class="color-grey font-s-15 text-underline-hover">用户数</div>
                  </nuxt-link>
                </div>
                <div>
                  <h4 class="color-theme font-s-16">
                    <countTo :startVal='0' :endVal='statDataInfoVo.specialCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey font-s-15">专栏数</div>
                </div>
                <div title="点击跳转">
                  <nuxt-link to="/label" rel="noopener">
                    <h4 class="color-theme font-s-16">
                      <countTo :startVal='0' :endVal='statDataInfoVo.labelCount' :duration='2000'></countTo>
                    </h4>
                    <div class="color-grey font-s-15 text-underline-hover">标签数</div>
                  </nuxt-link>
                </div>
              </div>
              <div class="flex-space-between text-center line-height-20 mt-20">
                <div>
                  <h4 class="color-theme font-s-16">
                    <countTo :startVal='0' :endVal='statDataInfoVo.articleCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey font-s-15">文章数</div>
                </div>
                <div title="点击跳转">
                  <nuxt-link to="/dictum" rel="noopener">
                    <h4 class="color-theme font-s-16">
                      <countTo :startVal='0' :endVal='statDataInfoVo.dictumCount' :duration='2000'></countTo>
                    </h4>
                    <div class="color-grey font-s-15 text-underline-hover">随笔数</div>
                  </nuxt-link>
                </div>
                <div title="点击跳转">
                  <nuxt-link to="/time_notes" rel="noopener">
                    <h4 class="color-theme font-s-16">
                      <countTo :startVal='0' :endVal='statDataInfoVo.timeNotesCount' :duration='2000'></countTo>
                    </h4>
                    <div class="color-grey-2 font-s-16 text-underline-hover">小记数</div>
                  </nuxt-link>
                </div>
              </div>
            </div>
          </div>
          <!--        精选文章-->
          <div class="selected-articles-info mt-5">
            <div class="flex-space-between align-items-center mb-10">
              <div class="font-bold ml-10">精选文章</div>
              <div>
                <el-button @click="selectedArticleListApi()"
                           type="text" :loading="selectedArticleLoading" icon="el-icon-refresh">
                  换一换
                </el-button>
              </div>
            </div>
            <el-skeleton :rows="6" animated v-if="selectedArticleLoading"/>
            <ul v-show="!selectedArticleLoading">
              <li v-for="(item,index) in selectedArticleList" :key="index" :ref="`selectedArticlesItem${index}`"
                  class="recommend-article-item flex-left align-items-center font-s-16"
                  :title="item.articleTitle">
                <div class="flex-1 mr-6 text-center">
                  <h1 v-if="index<=2" class="font-s-15"
                      :style="index==0?'color:#eb4d4b':index==1?'color:#f9ca24':'color:#4cd137'">
                    {{ index + 1 }}
                  </h1>
                  <h1 class="font-s-15" v-else>
                    {{ index + 1 }}
                  </h1>
                </div>
                <div class="cursor-pointer overflow-nowrap-1 flex-10">
                  <nuxt-link class="text-underline-hover"
                             :to="`/article-details/`+$base64.encode(item.id)" target="_blank"
                             rel="noopener">
                    {{ item.articleTitle }}
                  </nuxt-link>
                </div>
              </li>
            </ul>
          </div>
          <hr class="hr-item" style="margin: 0px 20px 15px 20px"/>
          <!-- 备案-->
          <el-skeleton class="mt-10" :rows="3" animated v-if="siteInfoLoading"/>
          <div class="mt-10 ml-10 line-height-30 font-s-14 color-grey" style="padding: 0 10px;"
               v-if="!siteInfoLoading">
            <a href="https://beian.miit.gov.cn" class="hover-cl" target="_blank"
               rel="nofollow">{{ siteInfo.filings }}</a>
            .
            <span>反馈邮箱：{{ siteInfo.mailbox }}</span>
            .
            <soan>开源地址：</soan>
            <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank"
               class="text-underline-hover color-grey"><span style="color: #ee5a24">gitee.com</span></a>
            .
            <a href="https://github.com/yu-zi-wei/Qixidi-KnowledgeCommunity" target="_blank"
               class="text-underline-hover color-grey"><span style="color: #ee5a24">gitHub.com</span></a>
            <div>小站已经运行了：{{ siteOperationTime }}</div>
            <div>
              © {{ $utils.parseTime(new Date(), '{y}') }} 栖息地
            </div>
            <!--              订阅rss-->
            <div title="订阅RSS">
              <nuxt-link class="text-underline-hover color-grey" to="rss.xml" rel="noopener" target="_blank">
                <svg t="1745568361410" class="icon-theme-1 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="4983" width="12" height="12">
                  <path
                    d="M306.172191 420.340417c-45.686529-15.009855-95.144964-22.849404-146.053424-23.295566-12.485359-1.335415-24.696472 2.944052-33.541931 11.790534-15.757892 15.757892-16.737196 40.532135-2.158153 55.112202 4.016477 4.016477 9.081843 6.831592 15.454994 8.492418l1.373277 1.373277 3.913123-0.063445c42.561351-0.209778 82.817194 6.701632 130.519636 22.416546 157.437705 51.79976 262.621303 197.078541 274.610358 379.098107l0.86367 9.656941 0.86367-0.130983c1.517563 5.234211 4.213975 9.81453 7.983835 13.58439 13.768585 13.768585 37.062104 12.878309 51.877531-1.937119 4.997827-4.997827 8.532327-11.044544 10.338463-17.616216l2.146896-0.26299-0.394996-8.664334c-9.289574-215.804021-131.041522-388.125715-317.794903-449.556832M155.48626 755.924475c-30.467919 30.467919-30.494525 80.015382-0.039909 110.469997 30.439266 30.439266 80.015382 30.440289 110.482277-0.027629 30.414707-30.414707 30.413684-79.989799-0.025583-110.429065C235.449453 725.483162 185.900967 725.509768 155.48626 755.924475M475.429083 163.478282c-97.17418-37.179784-200.406333-54.302767-302.476009-46.540989l-5.390777 5.390777-0.733711 0.733711c-27.065426 9.841136-33.200146 44.750203-13.973246 63.97608 4.581342 4.581342 14.814404 9.457396 21.331841 11.210319l0 0 0 0c90.249467-7.066953 188.100052 6.101975 273.745664 38.820144 260.736373 99.708909 415.950434 355.591741 369.150548 608.384188l-2.041496 11.096732 2.852978 0.027629c1.622964 7.223518 5.208629 13.845333 10.549264 19.185968 15.417131 15.417131 40.46562 15.388479 55.855122 0 4.424776-4.424776 7.644097-9.78997 9.447163-15.888875l1.231037 0.079818 1.493004-8.089235C946.517301 565.463656 769.473049 275.92326 475.429083 163.478282L475.429083 163.478282z"
                    p-id="4984"></path>
                </svg>
                rss 订阅
              </nuxt-link>
            </div>
          </div>
        </div>
      </div>
      <LoginModule :isLogin="isLoginBinlog" @loginDialogMethod="loginDialogMethod"></LoginModule>
    </div>
  </div>
</template>

<script>
import countTo from 'vue-count-to';
import {createAnimator} from '~/plugins/animationUtils'

export default {
  name: "index",
  components: {countTo},
  data() {
    return {
      websiteName: process.env.PROJECT_NAME,
      themeColor: process.env.THEME_COLOR,
      sidebarDialog: true,
      labelGroupingDialog: true,
      sidebarList: [],
      labelGroupingList: [],
      userInfo: null,
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
      siteOperationTime: null,
      statDataInfoVo: {},
      statDataInfoLoading: false,
      animator: null, // 动画器实例
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
      }).finally(() => {
        this.selectedArticleLoading = false
        this.$nextTick(() => {
          this.animator.triggerAllItemsAnimation(this.selectedArticleList, 'selectedArticlesItem');
        });
      });
    },
    getSiteInfo() {
      this.siteInfoLoading = true;
      this.$API("/white/site/info", this.$get()).then(res => {
        this.siteInfo = res.data;
        this.siteOperationTime = this.timeDifference(this.siteInfo.createTime);
      }).finally(() => this.siteInfoLoading = false);
      this.$API("/white/site/total-data", this.$get()).then(res => {
        this.statDataInfoVo = res.data;
      }).finally(() => this.statDataInfoLoading = true);
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
      this.$API("/white/label/grouping/list", this.$get(), {pageNum: 0, pageSize: 12,}).then(res => {
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
    // 初始化动画器
    this.animator = createAnimator(this, 'commonList');
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
  height: 42px !important;
  line-height: 42px !important;
}

.el-button--medium {
  padding: 10px 20px;
}

</style>
