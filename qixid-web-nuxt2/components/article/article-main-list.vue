<template>
  <div class="flex-1">
    <div class="flex-grow-1">
      <el-carousel height="100px" class="mb-15" style="border-radius: 2px" v-if="false">
        <el-carousel-item v-for="(item,index) in 4" :key="index">
          <nuxt-link v-if="item==1" :to="`/sign_in/daily`">
            <div class="lbt-cl daily-check-in">
              每日签到
              <svg t="1681714957552" class="icon icon-size-24 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="3120">
                <path
                  d="M802.5 180h-83v-62.3c0-11.4-9.3-20.8-20.8-20.8s-20.8 9.3-20.8 20.8V180H346v-62.3c0-11.4-9.3-20.8-20.8-20.8s-20.8 9.3-20.8 20.8V180h-83C153 180 97 236 97 304.5v498C97 871 153 927 221.5 927h581C871 927 927 871 927 802.5v-498C927 236 871 180 802.5 180z m83 622.5c0 45.8-37.2 83-83 83h-581c-45.8 0-83-37.2-83-83V429h747v373.5z m0-415h-747v-83c0-45.8 37.2-83 83-83h83v62.3c0 11.4 9.3 20.8 20.8 20.8s20.8-9.3 20.8-20.8v-62.3h332v62.3c0 11.4 9.3 20.8 20.8 20.8s20.8-9.3 20.8-20.8v-62.3h83c45.8 0 83 37.2 83 83v83z"
                  p-id="3121" fill="#ffffff"></path>
                <path
                  d="M438.6 782.5c8.1 8.1 21.3 8.1 29.3 0l205.4-205.4c8.1-8.1 8.1-21.3 0-29.3-8.1-8.1-21.3-8.1-29.3 0L453.3 738.5l-73.4-73.4c-8.1-8.1-21.3-8.1-29.3 0-8.1 8.1-8.1 21.3 0 29.3l88 88.1z"
                  p-id="3122" fill="#ffffff"></path>
              </svg>
              <p class="font-s-20 mt-6 color-fefefe">{{ $utils.obtainTimePeriod(new Date()) }}好！签到赢好礼</p>
            </div>
          </nuxt-link>
          <nuxt-link v-if="item==2" :to="`/article/publish-article`" target="_blank">
            <div class="lbt-cl article-release">
              文章发布
              <svg t="1681714073198" class="icon icon-size-24 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="2724">
                <path
                  d="M864.2 235.1L713.7 754.7 532.9 669s-22.9 0-28.6 16c-5.8 16 8.1 27.2 8.1 27.2l183.5 87c26.5 12.6 54.6-0.2 62.7-28.3l163.9-565.8c8.2-28.2-7.5-42-34.3-31.3L119.7 483c-27.1 10.9-27.7 29.9-0.9 42.6l208.1 98.6s20.5 5 28.3-12.9c7.9-17.9-7.9-30.3-7.9-30.3l-157.6-74.7 674.5-271.2z m-431 593.8l6.6-128.6c1.5-23.2 17.8-57.5 34.9-73.6l378-353.5 10.6-37.6-70.6 28.3L442 591.8c-25.9 24.3-47.5 70-49.8 105.3L385 828.8s6.9 18.2 23.9 18.2 24.3-18.1 24.3-18.1z m0 0"
                  fill="#FFFFFF" p-id="2725"></path>
              </svg>
              <p class="font-s-20 mt-6 color-fefefe">来激发你的创作灵感吧↗　</p>
            </div>
          </nuxt-link>
          <nuxt-link v-if="item==3" :to="`/dictum`">
            <div class="lbt-cl famous-words-square">
              名言广场
              <svg t="1682671548681" class="icon icon-size-24 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="5385">
                <path
                  d="M176.056576 768c-15.36 0-29.184-4.096-40.96-12.8-17.92-13.312-28.672-38.4-31.744-74.24-9.216-98.816 36.864-260.608 142.848-378.88C453.048576 71.68 699.320576-13.824 999.352576 40.448c12.288 2.048 22.016 11.776 24.576 23.552 2.56 12.288-2.56 24.576-12.8 31.744-284.16 190.464-385.024 341.504-474.112 475.136-16.896 25.088-32.256 48.64-49.152 72.192-6.656 9.216-17.408 13.824-28.16 12.8-31.744-3.584-88.576 29.184-107.008 39.424l-1.024 0.512C280.504576 736.256 220.600576 768 176.056576 768z m-3.584-60.416z m-0.512-0.512c2.56 0.512 15.872 2.56 54.784-13.824 30.72-13.312 64.512-32.768 94.208-50.176l1.024-0.512c37.376-21.504 83.968-45.056 125.44-47.616 12.8-18.432 25.6-37.888 38.912-57.856C571.320576 409.6 667.064576 266.24 912.824576 89.6 669.112576 66.048 465.336576 148.992 291.256576 343.04c-134.656 150.016-139.776 339.456-119.296 364.032z"
                  fill="#FFFFFF" p-id="5386"></path>
                <path
                  d="M30.648576 980.992c-4.096 0-8.192-1.024-12.288-2.56-15.36-6.656-22.528-24.576-15.872-39.936 155.648-358.4 485.888-620.032 500.224-630.784 13.312-10.24 32.256-8.192 43.008 5.12 10.24 13.312 8.192 32.256-5.12 43.008-3.072 2.56-332.288 263.168-481.792 607.232-5.12 11.264-16.384 17.92-28.16 17.92z"
                  fill="#FFFFFF" p-id="5387"></path>
                <path
                  d="M331.192576 537.6h-1.536c-16.896-1.024-29.696-14.848-29.184-31.744l11.776-253.952c1.024-16.896 14.848-29.696 31.744-29.184 16.896 1.024 29.696 14.848 29.184 31.744l-11.776 253.952c-0.512 16.384-13.824 29.184-30.208 29.184zM905.656576 999.424c-3.584 0-7.68-0.512-11.264-2.048-44.032-17.92-68.608-46.592-88.064-69.12-23.04-26.624-35.84-41.984-71.68-40.448-31.232 1.024-50.688 20.992-72.704 44.544-27.648 28.672-61.952 64.512-120.32 51.712-47.616-10.24-71.68-39.936-91.648-63.488-20.992-25.6-35.328-42.496-74.24-42.496h-0.512c-68.096 0.512-142.336 62.464-165.376 86.528-11.776 12.288-30.72 12.8-43.008 1.024-12.288-11.776-12.8-30.72-1.024-43.008 4.096-4.096 101.376-104.448 208.896-104.96h1.024c67.584 0 97.28 35.84 121.344 64.512 17.408 20.992 30.72 37.376 57.856 43.008 23.04 5.12 35.84-5.632 63.488-34.304 26.112-27.136 58.88-61.44 114.688-63.488 65.536-2.56 94.208 31.744 120.32 61.44 17.92 20.48 34.304 39.936 65.024 52.736 15.36 6.144 23.04 24.064 16.896 39.936-6.144 10.752-17.408 17.92-29.696 17.92z"
                  fill="#FFFFFF" p-id="5388"></path>
              </svg>
              <p class="font-s-20 mt-6 color-fefefe">诗和远方值得一生追求!</p>
            </div>
          </nuxt-link>
          <nuxt-link v-if="item==4" :to="`/tool`">
            <div class="lbt-cl common-tool">
              常用工具
              <svg t="1681714501743" class="icon icon-size-24 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="4963">
                <path
                  d="M944.140673 718.412117 621.322359 452.362738c17.037025-37.017078 26.009374-77.727269 26.009374-118.662587 0-156.206668-127.07007-283.313577-283.313577-283.313577-28.04473 0-55.711859 3.996011-82.250282 12.062733l-30.853705 9.348925 175.299515 175.355797c7.463995 7.388271 11.496845 17.265222 11.496845 27.819602s-4.03285 20.430308-11.496845 27.894304l-92.992951 93.030813c-14.815427 14.776542-40.879036 14.852266-55.712883 0L102.189915 220.615607l-9.329483 30.758538c-8.066723 26.612101-12.156878 54.281277-12.156878 82.324984 0 156.131967 127.10691 283.239899 283.314601 283.239899 41.011043 0 81.684394-8.894577 118.662587-25.934672L623.357715 763.344468c0.678452 2.712785 1.811252 5.276167 3.618411 7.537673 1.88493 2.411933 4.297886 4.222161 6.935969 5.429663l113.687272 139.242298c14.175861 16.512069 34.529421 26.086122 55.789631 26.086122 19.525706 0 38.035268-7.841595 52.169173-21.937638l93.02979-93.030813c14.927991-14.927991 22.691815-34.45472 21.86396-54.958706C969.696722 751.207034 960.462406 732.358757 944.140673 718.412117M921.297408 799.380196l-93.030813 93.030813c-14.399965 14.32424-38.373982 13.269211-51.076282-1.658779l-53.714366-65.739237 64.685231-52.16815c8.329712-6.710842 9.572006-18.922978 2.938935-27.215852-6.710842-8.292873-18.847254-9.574053-27.140127-2.939959l-64.910359 52.393277-28.271904-34.6778 58.126862-46.89403c8.292873-6.708795 9.612938-18.845207 2.865257-27.139104-6.634094-8.367575-18.810415-9.650801-27.102265-2.939959l-58.31208 47.043433L492.783845 542.377868l-13.94664 7.388271c-35.132148 18.771529-74.786287 28.572756-114.818025 28.572756-134.947482 0-244.714468-109.692285-244.714468-244.639766 0-12.966313 0.998747-25.7822 2.996241-38.44766l127.917368 127.935788c29.345352 29.40061 80.930217 29.40061 110.295012 0l92.992951-93.030813c14.737656-14.70184 22.806425-34.302247 22.806425-55.184857 0-20.806885-8.068769-40.48404-22.806425-55.110155L325.607334 91.925642c12.664438-1.960655 25.51921-2.940982 38.410821-2.940982 134.947482 0 244.714468 109.691261 244.714468 244.713444 0 39.957037-9.874905 79.687924-28.572756 114.742301l-7.388271 13.947663L919.336754 747.9652c7.728008 6.634094 12.215206 15.60542 12.552897 25.25622C932.30409 782.870174 928.497391 792.145421 921.297408 799.380196"
                  fill="#ffffff" p-id="4964"></path>
              </svg>
              <p class="font-s-20 mt-6 color-fefefe">优秀的开源项目、提前下班的工具神器</p>
            </div>
          </nuxt-link>
        </el-carousel-item>
      </el-carousel>
      <el-skeleton class="mt-10" style="width: 100%" :rows="16" animated v-show="!scrollLoading"/>
      <div v-for="item of listInformationList" :key="item.id" class="article-item">
        <div class=" flex-space-between align-items-center">
          <div class="flex-8" style="padding: 0 15px">
            <div>
              <h1 class="mb-10">
                <nuxt-link class="text-underline-hover font-s-16"
                           :to="`/article/article-details/`+$base64.encode(item.id)"
                           rel="noopener" target="_blank">
                  {{ item.articleTitle }}
                </nuxt-link>
              </h1>
              <p class="font-s-14 line-height-26 overflow-nowrap-2 color-grey">
                {{ item.articleAbstract }}
              </p>
            </div>
            <div class="flex-space-between mt-15 mr-15 color-grey-2">
              <div class="article-top font-s-13">
                <div class="svg-translateY-2 font-bold-300">
                  <nuxt-link class="hover-cl"
                             :to="`/user_home/article?uuid=`+$base64.encode(item.userId)"
                             target="_blank">
                    {{ item.nickname }}
                  </nuxt-link>
                  <span class="ml-4 mr-4">|</span>
                  <span class="font-bold-300"
                        :title="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')">
                    {{ $utils.reckonTime(item.createTime, '{y}-{m}-{d}') }}</span>
                </div>
                <div class="ml-15">
                  <div v-if="item.type==1" class="font-s-12 font-bold-300">
                    <svg t="1719973854018" class="icon-theme-1 icon-size-14 svg-translateY-3" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6365">
                      <path
                        d="M511.3 955.2c-59.8 0-117.9-11.7-172.6-34.9-52.8-22.3-100.2-54.3-140.9-95s-72.7-88.1-95-140.9c-23.1-54.7-34.9-112.8-34.9-172.6s11.7-117.9 34.9-172.6c22.3-52.8 54.3-100.2 95-140.9s88.1-72.7 140.9-95c54.7-23.1 112.8-34.9 172.6-34.9s117.9 11.7 172.6 34.9c52.8 22.3 100.2 54.3 140.9 95s72.7 88.1 95 140.9c23.1 54.7 34.9 112.8 34.9 172.6S943 629.7 919.9 684.4c-22.3 52.8-54.3 100.2-95 140.9s-88.1 72.7-140.9 95c-54.7 23.2-112.8 34.9-172.7 34.9z m0-822.8c-209.2 0-379.4 170.2-379.4 379.4s170.2 379.4 379.4 379.4S890.8 721 890.8 511.8 720.5 132.4 511.3 132.4z"
                        p-id="6366"></path>
                      <path
                        d="M511.3 773.8c-70 0-135.8-27.3-185.3-76.7s-76.7-115.3-76.7-185.3S276.6 376 326 326.5c49.5-49.5 115.3-76.7 185.3-76.7v64c-109.2 0-198 88.8-198 198s88.8 198 198 198 198-88.8 198-198h64c0 70-27.3 135.8-76.7 185.3-49.5 49.4-115.3 76.7-185.3 76.7z"
                        p-id="6367"></path>
                    </svg>
                    原创
                  </div>
                  <div v-if="item.type==2" class="font-s-12">
                    <svg t="1719973895599" class="icon-theme-1 icon-size-14 svg-translateY-3" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="7373">
                      <path
                        d="M512 102.4v54.613333A354.986667 354.986667 0 1 0 866.986667 512h54.613333A409.6 409.6 0 1 1 512 102.4z m224.682667 98.048l128.213333 124.501333-142.293333 118.741334-32.810667-39.338667L775.466667 332.8H716.8a179.2 179.2 0 0 0-178.986667 170.24L537.6 512v128.341333h-51.2V512a230.4 230.4 0 0 1 221.141333-230.229333L716.8 281.6h29.994667L701.013333 237.141333l35.669334-36.693333z"
                        fill="#333333" p-id="7374"></path>
                    </svg>
                    转载
                  </div>
                </div>
                <!--              <div class="svg-translateY-3 ml-15 font-bold-300">-->
                <!--                <el-tag :type="randomType()" size="small" effect="plain" class="cursor-pointer" title="分类">-->
                <!--                  <nuxt-link :to="`/external_info/label-group-info?data=`+item.groupingId" target="_blank">-->
                <!--                    {{ item.groupingName }}-->
                <!--                  </nuxt-link>-->
                <!--                </el-tag>-->
                <!--              </div>-->
                <div class="svg-translateY-3 font-s-13 hover-cl ml-15 font-bold-300" title="分类">
                  <nuxt-link :to="`/external_info/label-group-info?data=`+item.groupingId" target="_blank">
                    # {{ item.groupingName }}
                  </nuxt-link>
                </div>
              </div>
            </div>
          </div>

          <div v-if="item.articleCover" class="article-cover-div mt-15">
            <el-image class="article-cover-img" v-if="item.articleCover!=null && item.articleCover!=''"
                      :src="item.articleCover" fit="cover"></el-image>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "articleMainList",
  props: {
    uid: {
      type: String,
    },
    type: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      listInformationList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        articleTitle: null,
      },
      total: 0,
      scrollLoading: true,
    }
  },
  methods: {
    // randomType() {
    //   let types = ["", "success", "warning", "info", "danger"];
    //   let randomIndex = Math.floor(Math.random() * types.length);
    //   return types[randomIndex];
    // },
    load() {
      if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        this.$API("/white/article/recommend/list", this.$get(), this.queryParams).then(res => {
          res.rows.forEach(item => {
            this.listInformationList.push(item)
          })
          this.total = res.total;
        }).finally(() => this.scrollLoading = true)
      }
    },
    listInformation() {
      this.scrollLoading = false;
      this.$API("/white/article/recommend/list", this.$get(), this.queryParams).then(res => {
        this.listInformationList = res.rows
        this.total = res.total;
      }).finally(() => this.scrollLoading = true);
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if ((scrollTop + clientHeight + clientHeight) >= scrollHeight) {
        if (!this.scrollLoading) return;
        this.load()
      }
    },
  },
  mounted() {
    this.listInformation();
    window.addEventListener('scroll', this.getData, true);
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style scoped>
@import url("components/css/pc/article-index-list.css");

.daily-check-in {
  background: #74ebd5; /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, rgb(116, 235, 213), rgb(172, 182, 229)); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, rgb(116, 235, 213), rgb(172, 182, 229)); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}

.article-release {
  background: #3494e6;
  background: -webkit-linear-gradient(to right, rgb(52, 148, 230), rgb(236, 110, 173));
  background: linear-gradient(to right, rgb(52, 148, 230), rgb(236, 110, 173));
}

.famous-words-square {
  background: #05aa7; /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, rgb(0, 90, 167), rgb(255, 253, 228)); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, rgb(0, 90, 167), rgb(255, 253, 228)); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}

.common-tool {
  background: #ffd89b;
  background: -webkit-linear-gradient(to right, rgb(255, 216, 155), rgb(25, 84, 123));
  background: linear-gradient(to right, rgb(255, 216, 155), rgb(25, 84, 123));
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
