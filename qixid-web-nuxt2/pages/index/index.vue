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

        <div class="auroora-card">
          <div class="font-bold font-s-14">热门分类</div>
          <hr class="hr-item mb-10 mt-10"/>
          <el-skeleton class="mt-10" :rows="8" animated v-show="sidebarDialog"/>
          <el-menu
            v-if="!sidebarDialog"
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
        <div class="auroora-card mb-15 sigIn-info" v-show="positionCssRight">
          <el-skeleton :rows="3" animated v-if="reportLoading"/>
          <div v-show="!reportLoading" class="flex-space-between">
            <div>
              <p class="font-bold mb-4" v-if="isReport">连续签到<span class="color-stand-out ml-4">{{
                  ctnFatalism
                }}天</span>
              </p>
              <p class="font-bold mb-4" v-if="!isReport">{{ $utils.obtainTimePeriod(new Date()) }}好！</p>
              <p class="font-s-13 color-grey-2">点亮在{{ websiteName }}的每一天</p>
            </div>
            <el-button size="medium" @click="signIn">
              {{ isReport ? '已签到' : '去签到' }}
            </el-button>
          </div>
        </div>
        <!--                用户信息-->
        <div class="auroora-card mb-15 overflow-hidden" v-show="positionCssRight">
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
                <svg t="1667140071514" class="icon-theme-stand-out" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="2503" width="40" height="40">
                  <path
                    d="M511.965867 605.252267a259.754667 259.754667 0 0 1-185.002667-76.663467c-49.425067-49.425067-76.629333-115.131733-76.629333-185.002667s27.204267-135.5776 76.629333-185.002666a259.9936 259.9936 0 0 1 185.002667-76.629334c69.905067 0 135.611733 27.204267 185.0368 76.629334 49.390933 49.425067 76.629333 115.131733 76.629333 185.002666s-27.2384 135.5776-76.629333 185.002667a259.6864 259.6864 0 0 1-185.0368 76.663467z m0-461.243734a199.850667 199.850667 0 0 0-199.611734 199.5776 199.850667 199.850667 0 0 0 199.611734 199.611734 199.816533 199.816533 0 0 0 199.611733-199.611734 199.816533 199.816533 0 0 0-199.611733-199.5776z m0 0"
                    p-id="2504"></path>
                  <path
                    d="M930.645333 942.011733a31.0272 31.0272 0 0 1-30.139733-23.790933A397.653333 397.653333 0 0 0 512 611.6352h-0.4096a398.336 398.336 0 0 0-210.807467 60.347733 30.993067 30.993067 0 1 1-32.836266-52.599466 459.844267 459.844267 0 0 1 243.575466-69.802667H512c53.0432 0 105.028267 8.942933 154.589867 26.5216a459.946667 459.946667 0 0 1 132.9152 74.001067 461.346133 461.346133 0 0 1 161.3824 253.713066 31.095467 31.095467 0 0 1-30.242134 38.1952z m0 0M93.354667 942.011733a31.061333 31.061333 0 0 1-30.208-38.1952 458.6496 458.6496 0 0 1 69.461333-155.5456 31.061333 31.061333 0 0 1 43.178667-7.816533 31.061333 31.061333 0 0 1 7.816533 43.2128 394.410667 394.410667 0 0 0-60.074667 134.519467 31.1296 31.1296 0 0 1-30.173866 23.825066z m0 0"
                    p-id="2505"></path>
                </svg>
                <div style="width: 40px;" class="font-s-14 mt-2 text-center hover-fw-bold hover-cl">登 录</div>
              </div>
            </div>
            <div v-else class="user-admin-info">
              <div class="flex-define">
                <div v-if="userInfo!=null">
                  <el-avatar :size="50" v-if="userInfo.avatar" :src="userInfo.avatar"/>
                  <el-avatar :size="50" v-else src="/img/tx.jpg"></el-avatar>
                </div>
                <div class="ml-8">
                  <nuxt-link class="" :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)"
                             target="_blank" rel="noopener">
                    <p class="font-s-18 overflow-nowrap-1">{{ userInfo.nickname }}</p>
                  </nuxt-link>
                  <p class="font-s-13 mt-4 overflow-nowrap-1 color-grey-2">
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
        <div class="auroora-card selected-articles-info">
          <div class="flex-space-between align-items-center">
            <div class="font-bold">精选文章</div>
            <div>
              <el-button @click="selectedArticleListApi()"
                         type="text" :loading="selectedArticleLoading" icon="el-icon-refresh">
                刷新
              </el-button>
            </div>
          </div>
          <el-skeleton class="mt-10" :rows="6" animated v-if="selectedArticleLoading"/>
          <ul v-show="!selectedArticleLoading" class="ml-6">
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
                <nuxt-link class="hover-cl text-underline-hover" :to="`/article/article-details/`+$base64.encode(item.id)" target="_blank"
                           rel="noopener">
                  {{ item.articleTitle }}
                </nuxt-link>
              </div>
            </li>
          </ul>
          <hr class="hr-item mt-15 mb-10"/>
          <p class="text-center font-s-14 color-grey hover-cl cursor-pointer mb-10">查看更多 ></p>
        </div>
        <!-- 备案-->
        <el-skeleton class="mt-10" :rows="3" animated v-if="siteInfoLoading"/>
        <ul class="mt-15 color-grey-2 ml-10" v-if="!siteInfoLoading">
          <li title="备案" class="mb-4">
            <svg t="1720577032759" class="icon icon-size-16 svg-translateY-3-5 mr-2 icon-theme" viewBox="0 0 1024 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="17008">
              <path
                d="M534.79424 1017.92768c-0.73728 0.37888-1.45408 0.73728-2.0224 0.9984-5.52448 2.97472-13.95712 5.04832-22.28736 5.04832-2.21696 0-7.26528-0.45056-9.43104-0.85504-4.50048-0.6912-9.83552-2.4064-14.71488-4.90496-0.18944-0.11776-0.40448-0.21504-0.6656-0.3584C-11.28448 814.53056 62.99136 238.85312 63.82592 233.04192c3.16928-22.81472 20.19328-40.96 42.36288-45.12768C268.55424 157.26592 447.488 34.05312 475.42272 14.2848c5.92896-5.23776 12.83584-9.78944 20.1728-11.81184 8.38144-2.88256 18.67264-3.21536 28.3136-0.59392 8.71424 2.33472 16.57344 6.71744 22.97856 12.81024 30.79168 21.43232 209.6128 143.0528 370.688 173.22496 22.12352 4.1216 39.17312 22.2208 42.38848 45.0304 0.04608 0.0256 0.04608 0.04608 0.04608 0.07168C960.82432 238.83264 1035.12576 814.91456 534.79424 1017.92768zM911.744 264.04864c0 0 0-0.0256-0.0256-0.04608-2.85696-20.55168-18.00192-36.864-37.67296-40.55552-143.07328-27.19744-301.91104-144.21504-329.27744-163.5328-5.69344-5.4784-12.66688-9.40544-20.43392-11.52512-8.5504-2.33472-17.69472-2.048-25.12384 0.54784-6.52288 1.83296-12.6464 5.26336-17.93024 9.97888-24.81664 17.78688-189.70112 132.4544-333.94176 160.0768-19.69664 3.7376-34.816 24.52992-37.62688 45.0816C108.97408 269.312 48.94208 787.71712 490.37824 970.79808c0.24064 0.14336 0.43008 0.24064 0.59392 0.3328 4.33664 2.26304 9.07264 3.80928 13.07136 4.4288 1.93024 0.3584 6.43072 0.76288 8.36096 0.76288 7.42912 0 14.90944-1.85856 19.8144-4.54656 0.50176-0.21504 1.14176-0.54784 1.80736-0.90624C978.47296 788.07552 912.4608 269.2864 911.744 264.04864z"
                p-id="17009"></path>
              <path d="M231.3472 383.44704l31.85152 0 0 289.59232-31.85152 0L231.3472 383.44704z" p-id="17010"></path>
              <path
                d="M539.75552 573.12768l37.65248 8.68352c-18.35008 62.76096-60.3392 94.59712-125.97248 95.5648-83.03104-4.8384-126.94528-52.59776-131.7632-143.34464 3.85024-98.46272 48.73728-150.0928 134.66112-154.9312 55.02464 1.9456 94.11584 29.93152 117.28896 83.98336l-36.1984 8.68864C518.0416 430.27968 490.03008 409.0368 451.4304 408.064c-58.89024 5.79072-91.21792 46.83264-97.00864 123.07456 3.84512 74.34752 35.70176 114.39104 95.5648 120.18176C497.27488 649.3952 527.20128 623.32928 539.75552 573.12768z"
                p-id="17011"></path>
              <path
                d="M746.81856 555.75552l-89.77408 0 0 117.27872-31.85664 0L625.18784 383.44704l124.52352 0c58.86976 2.89792 91.21792 31.38048 97.01376 85.43232C845.75744 525.84448 812.45696 554.79808 746.81856 555.75552zM741.02784 412.40576l-83.98336 0 0 114.39104 83.98336 0c46.33088-0.95232 69.97504-20.74624 70.94784-59.3664C810.03008 431.72864 786.39104 413.37856 741.02784 412.40576z"
                p-id="17012"></path>
            </svg>
            <a href="https://beian.miit.gov.cn" class="font-s-13 hover-cl" target="_blank"
               rel="nofollow">{{ siteInfo.filings }}</a>
          </li>

          <li title="反馈邮箱">
            <svg t="1720059190371" class="icon icon-size-16 svg-translateY-3-5 mr-2 icon-theme-1"
                 viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="24830">
              <path
                d="M875.52 851.342222H150.376296c-50.536296 0-91.591111-41.054815-91.591111-91.591111V263.300741c0-50.536296 41.054815-91.591111 91.591111-91.591111h725.143704c50.536296 0 91.591111 41.054815 91.591111 91.591111v53.475555c0 15.739259-12.705185 28.444444-28.444444 28.444445s-28.444444-12.705185-28.444445-28.444445v-53.475555c0-19.152593-15.54963-34.702222-34.702222-34.702222H150.376296c-19.152593 0-34.702222 15.54963-34.702222 34.702222v496.45037c0 19.152593 15.54963 34.702222 34.702222 34.702222h725.143704c19.152593 0 34.702222-15.54963 34.702222-34.702222v-240.82963c0-15.739259 12.705185-28.444444 28.444445-28.444444s28.444444 12.705185 28.444444 28.444444v240.82963c0 50.536296-41.054815 91.591111-91.591111 91.591111z"
                p-id="24831"></path>
              <path
                d="M510.388148 582.257778c-5.30963 0-10.619259-1.517037-15.265185-4.456297L243.768889 417.374815c-13.274074-8.438519-17.161481-26.074074-8.628148-39.253334 8.438519-13.274074 26.074074-17.161481 39.253333-8.628148l237.131852 151.324445 413.392593-229.262222c13.748148-7.585185 31.004444-2.654815 38.684444 11.093333 7.585185 13.748148 2.654815 31.099259-11.093333 38.684444L524.231111 578.74963c-4.266667 2.37037-9.102222 3.508148-13.842963 3.508148z"
                p-id="24832"></path>
            </svg>
            <span class="font-s-13">{{ siteInfo.mailbox }}</span>
          </li>
          <li>
            <svg t="1726495684031" class="icon icon-size-16 mr-2 svg-translateY-3-5 icon-theme-1"
                 viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="10254">
              <path
                d="M817.8 109.7H207.5c-12.9 0-22.7-10.6-22.7-22.7 0-12.9 10.6-22.7 22.7-22.7h610.2c12.9 0 22.7 10.6 22.7 22.7 0.8 12.1-9.8 22.7-22.6 22.7zM848 962.2H178c-13.6 0-25-11.4-25-25s11.4-25 25-25h670c13.6 0 25 11.4 25 25s-11.3 25-25 25z"
                p-id="10255"></path>
              <path
                d="M753.4 653.3l-0.8-1.5C714 584.4 662.5 540.5 608 514c54.5-27.3 105.2-70.4 144.6-137.8l0.8-1.5C792 308 817.8 217.2 822.3 96h-44.7c-3.8 112.1-27.3 195.3-62.8 256.7l-0.8 0.8c-43.9 78-93.1 119.6-156.7 139.3l-3 0.8h-0.8c-1.5 0.8-2.3 1.5-3.8 2.3l-0.8 0.8-1.5 1.5-1.5 1.5v0.8c-0.8 0.8-1.5 2.3-2.3 3.8l-0.8 0.8-1.5 5.3v8.3l1.5 5.3 0.8 0.8c0.8 1.5 1.5 2.3 2.3 3.8v0.8l1.5 1.5v-0.8l1.5 0.8 0.8 0.8c0.8 0.8 2.3 1.5 3.8 2.3h0.8l3 1.5c64.4 20.4 112.8 61.3 157.5 138.6l0.8 1.5c18.9 31.8 33.3 70.4 44.7 115.1 2.3 12.9 10.6 40.9 12.1 77.2-35.6-31.8-134-54.5-252.1-56V461.7c1.5 0 2.3-0.8 3.8-1.5v-0.8l6.1-2.3h1.5c43.2-13.6 77.2-34.8 106-62.8 30.3-30.3 53.8-67.4 69.7-109 4.5-11.4-0.8-24.2-12.9-28.8-11.4-4.5-24.2 0.8-28.8 12.1v0.8c-13.6 34.8-33.3 67.4-59.1 93.1-23.6 22.7-51.6 40.9-87.2 51.5l-0.8 0.8-2.3 0.8-3.8 0.8-2.3-0.8-1.5-0.8-2.3-0.8c-34.8-11.4-64.4-28.8-87.1-51.5-26.5-25.7-45.4-57.5-59.1-93.1-4.5-11.4-17.4-17.4-28-12.9-11.4 4.5-16.7 16.7-12.9 28.8 15.9 41.6 38.6 78.7 69.7 109 28 28 62.8 49.2 105.2 62.8h0.8l6.1 2.3 1.5 0.8c0.8 0.8 1.5 0.8 3 1.5v349.8c-110.5 0-206.7 18.9-252.1 45.4 1.5-18.2 5.3-37.9 12.1-66.6 11.4-45.4 26.5-83.3 44.7-115.1l0.8-1.5c44.7-77.2 93.1-118.1 158.2-138.6l2.3-1.5h0.8c1.5-0.8 2.3-1.5 3.8-2.3l0.8-0.8 1.5-0.8v-0.8l1.5-1.5v-0.8c0.8-0.8 1.5-2.3 2.3-3.8v-0.8c0.8-1.5 1.5-3.8 1.5-5.3v-7.6c0-1.5-0.8-3.8-1.5-5.3v-0.8c-0.8-1.5-1.5-3-2.3-3.8v-0.8l-1.5-1.5-1.5-1.5-2.3 0.8c-1.5-0.8-2.3-1.5-3.8-2.3h-0.8l-2.3-0.8c-64.4-19.7-113.6-61.3-158.2-139.3l-0.8-0.8C274 291.7 250.5 208.4 246 96.3h-44.7c4.5 121.1 30.3 211.2 68.9 278.6l0.8 1.5c38.6 67.4 90.1 110.5 144.6 137.8C361.1 540.7 310.4 584.6 271 652l-0.8 1.5C234 717.6 208.3 802.4 203 915.2h617.8c-5.3-113.5-30.3-198.3-67.4-261.9z"
                p-id="10256"></path>
            </svg>
            <span class="font-s-13">小站已经运行了：<span>{{ siteOperationTime }}</span></span>
          </li>
        </ul>
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
        this.$API("/white/configure/sidebar/list", this.$get(), {type: 1, status: 0}).then(res => {
          this.sidebarList = res.data;
        }).finally(() => this.sidebarDialog = false);
      })
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
  border-radius: 2px;
}

</style>
