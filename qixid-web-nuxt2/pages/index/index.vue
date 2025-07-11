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
            text-color="rgba(0, 0, 0, 0.5)"
            :active-text-color="themeColor"
            class="el-menu-demo">
            <el-menu-item v-for="(item,index) in sidebarList" :index="item.route" :title="item.sidebarName"
                          :key="index">
              <nuxt-link :to="item.route">
                <!--              <i v-if="item.sidebarIcon!=null" :class="$route.path==item.route?'icon-theme':''"-->
                <!--                 v-html="item.sidebarIcon"></i>-->
                <span class="font-s-14">{{ item.sidebarName }}</span>
              </nuxt-link>
            </el-menu-item>
            <el-menu-item>
              <span class="color-grey-3">|</span>
            </el-menu-item>
            <el-menu-item v-for="(item,index) in labelGroupingList" :index="'/popular-group/'+item.id"
                          :title="'分类：'+item.groupingName" :key="index">
              <nuxt-link :to="'/popular-group/'+item.id" class="font-s-14">
                <span class="font-s-14">{{ item.groupingName }}</span>
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
                <p class="font-bold mb-8" v-if="isReport">连续签到<span class="color-stand-out ml-4">
                  {{ ctnFatalism }}天</span>
                </p>
                <p class="font-bold mb-8" v-if="!isReport">{{ $utils.obtainTimePeriod(new Date()) }}好！</p>
                <p class="font-s-14 line-height-16 color-grey-2">点亮在{{ websiteName }}的每一天</p>
              </div>
              <el-button size="medium" @click="signIn">
                {{ isReport ? '已签到' : '去签到' }}
              </el-button>
            </div>
          </div>
          <!--                用户信息-->
          <div class="auroora-card mb-15 overflow-hidden border-1-e6e6e6 border-radius-10"
               v-show="!positionCssRight && false">
            <div class="fl-right flex-left" v-if="userInfo!=null">
              <div style="background-color: #FC625D" class="aurora-dot mr-6"></div>
              <div style="background-color: #FDBC40" class="aurora-dot mr-6"></div>
              <div style="background-color: #35CD4B" class="aurora-dot"></div>
            </div>
            <el-skeleton :rows="4" animated v-if="userInfoLoading"/>
            <div v-if="!userInfoLoading">
              <div v-if="userInfo==null" class="flex-define justify-content-center cursor-pointer"
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
                      <p class="flex-center mb-6 ">
                        <countTo :startVal='0' :endVal='userInfo.followCount' :duration='2000'></countTo>
                      </p>
                      <p class="color-grey-2 hover-cl">关注</p>
                    </nuxt-link>
                  </div>
                  <div class="cursor-pointer hover-cl">
                    <nuxt-link :to="`/user_home/collection?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                               rel="noopener">
                      <p class="flex-center mb-6 ">
                        <countTo :startVal='0' :endVal='userInfo.collectionCount' :duration='2000'></countTo>
                      </p>
                      <p class="color-grey-2 hover-cl">收藏</p>
                    </nuxt-link>
                  </div>
                  <div class="cursor-pointer hover-cl">
                    <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                               rel="noopener">
                      <p class="flex-center mb-6 ">
                        <countTo :startVal='0' :endVal='userInfo.articleCount' :duration='2000'></countTo>
                      </p>
                      <p class="color-grey-2 hover-cl">文章</p>
                    </nuxt-link>
                  </div>
                  <div class="cursor-pointer hover-cl">
                    <nuxt-link :to="`/dictum/space/content-list?uuid=`+$base64.encode(userInfo.uuid)" target="_blank"
                               rel="noopener">
                      <p class="flex-center mb-6">
                        <countTo :startVal='0' :endVal='userInfo.dictumCount' :duration='2000'></countTo>
                      </p>
                      <p class="color-grey-2 hover-cl">名言</p>
                    </nuxt-link>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!--            站点总数据-->
          <div class="website-data mt-10" v-show="positionCssRight">
            <p class="font-s-14 color-grey mb-10">
              <svg t="1746426533762" class="icon icon-size-16 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="3564">
                <path
                  d="M921.6 0a102.4 102.4 0 0 0-102.4 102.4v819.2a102.4 102.4 0 0 0 204.8 0V102.4A102.4 102.4 0 0 0 921.6 0zM204.8 230.4a102.4 102.4 0 0 0-204.8 0v691.2a102.4 102.4 0 0 0 204.8 0V230.4z m409.6 268.8a102.4 102.4 0 0 0-204.8 0v422.4a102.4 102.4 0 0 0 204.8 0V499.2z"
                  fill="#6F8DBB" p-id="3565"></path>
              </svg>
              <span class="ml-4">站点数据</span>
            </p>
            <hr class="hr-item mb-10"/>
            <el-skeleton :rows="3" animated v-if="!statDataInfoLoading"/>
            <div v-if="statDataInfoLoading" style="padding: 2px">
              <div class="flex-space-between text-center line-height-20"
                   v-if="statDataInfoLoading">
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.userCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">用户数</div>
                </div>
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.specialCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">专栏数</div>
                </div>
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.labelCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">标签数</div>
                </div>
              </div>
              <div class="flex-space-between text-center line-height-20 mt-20">
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.articleCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">文章数</div>
                </div>
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.dictumCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">名言数</div>
                </div>
                <div>
                  <h4 class="color-theme font-s-15">
                    <countTo :startVal='0' :endVal='statDataInfoVo.timeNotesCount' :duration='2000'></countTo>
                  </h4>
                  <div class="color-grey-2 font-s-14">小记数</div>
                </div>
              </div>
            </div>
          </div>
          <!--        精选文章-->
          <div class="selected-articles-info mt-5">
            <div class="flex-space-between align-items-center mb-10">
              <div class="font-bold  ml-10">精选文章</div>
              <div>
                <el-button @click="selectedArticleListApi()"
                           type="text" :loading="selectedArticleLoading" icon="el-icon-refresh">
                  换一换
                </el-button>
              </div>
            </div>
            <el-skeleton :rows="6" animated v-if="selectedArticleLoading"/>
            <ul v-show="!selectedArticleLoading">
              <li v-for="(item,index) in selectedArticleList" :key="index"
                  class="recommend-article-item flex-left align-items-center font-s-14"
                  :title="item.articleTitle">
                <div class="flex-1 mr-6 text-center">
                  <h1 v-if="index<=2" class="font-s-14"
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
          <div class="mt-10 ml-10 line-height-30 font-s-13" style="padding: 0 10px;color: #747d8c"
               v-if="!siteInfoLoading">
            <a href="https://beian.miit.gov.cn" class="hover-cl" target="_blank"
               rel="nofollow">{{ siteInfo.filings }}</a>
            .
            <span>反馈邮箱：{{ siteInfo.mailbox }}</span>
            .
            <a href="https://gitee.com/yu-zi-wei/qixidi" target="_blank"
               class="text-underline-hover">开源地址：<span style="color: #ee5a24">gitee.com</span></a>
            <div>小站已经运行了：{{ siteOperationTime }}</div>
            <div>
              © {{ $utils.parseTime(new Date(), '{y}') }} 栖息地
            </div>
            <!--              订阅rss-->
            <div title="订阅RSS">
              <nuxt-link class="text-underline-hover" to="rss.xml" rel="noopener" target="_blank">
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
