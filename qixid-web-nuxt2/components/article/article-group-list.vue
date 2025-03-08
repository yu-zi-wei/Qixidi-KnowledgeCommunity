<template>
  <div class="article-list-admin">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-skeleton :rows="6" animated v-if="loading"/>
      <el-tab-pane label="推荐" name="推荐"></el-tab-pane>
      <el-tab-pane label="最新" name="最新" v-if="last"></el-tab-pane>
    </el-tabs>
    <div v-if="!loading">
      <div v-for="(item,index) in articleList" :key="index" class="article-list-li flex-space-between">
        <div style="padding: 16px">
          <div class="mb-15 font-s-13">
            <nuxt-link class="mr-4 hover-cl font-bold" :to="`/user_home/article?uuid=`+$base64.encode(item.userId)"
                       target="_blank" rel="noopener">
              {{ item.nickname }}
            </nuxt-link>
            |
            <span class="color-grey" v-if="item.occupation!=null">{{ item.occupation }} |</span>
            <span class="color-grey font-bold-300" :title="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')">
                                        {{ $utils.reckonTime(item.createTime) }}</span>
          </div>
          <nuxt-link :to="`/article/article-details/`+$base64.encode(item.id)" rel="noopener" target="_blank">
            <h1 class="font-s-16 mb-15 cursor-pointer font-bold text-underline-hover" v-text="item.articleTitle"></h1>
          </nuxt-link>
          <p class="text-indent-13 overflow-nowrap-2 font-s-14 line-height-24 font-bold-300">
            {{ item.articleAbstract }}
          </p>
          <div class="mt-15 font-s-14 flex-left">
            <div class="mr-20" title="浏览">
              <svg t="1741407328107" class="icon-theme-1 svg-translateY-2 icon-size-14" viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="30371">
                <path
                  d="M512 153.6c-123.477333 0-235.178667 51.9168-323.089067 137.557333C117.230933 360.96 68.266667 448.938667 68.266667 505.173333v13.6704c0 56.234667 48.981333 144.196267 120.644266 214.016C276.804267 818.466133 388.522667 870.4 512 870.4c123.613867 0 235.246933-51.234133 322.7648-135.970133 71.799467-69.5296 120.490667-157.320533 120.968533-215.296V505.173333c-0.477867-58.2656-49.169067-146.056533-120.968533-215.586133C747.246933 204.8512 635.5968 153.6 512 153.6z m0 68.266667c104.6528 0 199.799467 43.6736 275.268267 116.753066 59.886933 57.992533 99.8912 130.133333 100.1984 166.843734v13.380266c-0.3072 36.420267-40.311467 108.544-100.181334 166.536534C711.7824 758.459733 616.635733 802.133333 512 802.133333c-104.3456 0-199.560533-44.2368-275.456-118.186666C176.810667 625.7664 136.533333 553.4208 136.533333 518.843733V505.173333c0-34.577067 40.277333-106.922667 100.010667-165.12C312.439467 266.120533 407.6544 221.866667 512 221.866667z"
                  p-id="30372"></path>
                <path
                  d="M512 341.333333c-94.2592 0-170.666667 76.407467-170.666667 170.666667s76.407467 170.666667 170.666667 170.666667 170.666667-76.407467 170.666667-170.666667-76.407467-170.666667-170.666667-170.666667z m0 68.266667a102.4 102.4 0 1 1 0 204.8 102.4 102.4 0 0 1 0-204.8z"
                  p-id="30373"></path>
              </svg>
              <span class="font-s-13 color-grey-2 font-bold-300"
                    v-text="item.numberTimes>0?item.numberTimes:'--'"></span>
            </div>
            <div class="mr-20" title="点赞">
              <svg t="1700403032641"
                   class="icon-theme-1 icon-size-14 svg-translateY-2"
                   viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="7591">
                <path
                  d="M190.193225 471.411583c14.446014 0 26.139334-11.718903 26.139334-26.13831 0-14.44499-11.69332-26.164916-26.139334-26.164916-0.271176 0-0.490164 0.149403-0.73678 0.149403l-62.496379 0.146333c-1.425466-0.195451-2.90005-0.295735-4.373611-0.295735-19.677155 0-35.621289 16.141632-35.621289 36.114522L86.622358 888.550075c0 19.949354 15.96767 35.597753 35.670407 35.597753 1.916653 0 3.808746 0.292666 5.649674 0l61.022819 0.022513c0.099261 0 0.148379 0.048095 0.24764 0.048095 0.097214 0 0.146333-0.048095 0.24457-0.048095l0.73678 0 0-0.148379c13.413498-0.540306 24.174586-11.422144 24.174586-24.960485 0-13.55983-10.760065-24.441669-24.174586-24.981974l0-0.393973-50.949392 0 1.450025-402.275993L190.193225 471.409536z"
                  p-id="7592"></path>
                <path
                  d="M926.52241 433.948343c-19.283182-31.445176-47.339168-44.172035-81.289398-45.546336-1.77032-0.246617-3.536546-0.39295-5.380544-0.39295l-205.447139-0.688685c13.462616-39.059598 22.698978-85.58933 22.698978-129.317251 0-28.349675-3.193739-55.962569-9.041934-82.542948l-0.490164 0.049119c-10.638291-46.578852-51.736315-81.31498-100.966553-81.31498-57.264215 0-95.466282 48.15065-95.466282 106.126063 0 3.241834-0.294712 6.387477 0 9.532097-2.996241 108.386546-91.240027 195.548698-196.23636 207.513194l0 54.881958-0.785899 222.227314 0 229.744521 10.709923 0 500.025271 0.222057 8.746198-0.243547c19.35686 0.049119 30.239721-4.817726 47.803749-16.116049 16.682961-10.761088 29.236881-25.50079 37.490869-42.156122 2.260483-3.341095 4.028757-7.075139 5.106298-11.20111l77.018118-344.324116c1.056052-4.053316 1.348718-8.181333 1.056052-12.160971C943.643346 476.446249 938.781618 453.944769 926.52241 433.948343zM893.82573 486.837924l-82.983993 367.783411-0.099261-0.049119c-2.555196 6.141884-6.879688 11.596106-12.872169 15.427364-4.177136 2.727111-8.773827 4.351098-13.414521 4.964058-1.49812-0.195451-3.046383 0-4.620227 0l-477.028511-0.540306-0.171915-407.408897c89.323375-40.266076 154.841577-79.670527 188.596356-173.661202 0.072655 0.024559 0.124843 0.049119 0.195451 0.072655 2.99931-9.137101 6.313799-20.73423 8.697079-33.164331 5.551436-29.185716 5.258771-58.123792 5.258771-58.123792-4.937452-37.98001 25.940812-52.965306 44.364417-52.965306 25.304316 0.860601 50.263777 33.656541 50.263777 52.326762 0 0 5.600555 27.563776 5.649674 57.190537 0.048095 37.366026-4.6673 56.847729-4.6673 56.847729l-0.466628 0c-5.872754 30.879288-16.214287 60.138682-30.464849 86.964654l0.36839 0.342808c-2.358721 4.815679-3.709485 10.220782-3.709485 15.943111 0 19.922748 19.088754 21.742187 38.765909 21.742187l238.761895 0.270153c0 0 14.666024 0.465604 14.690584 0.465604l0 0.100284c12.132318-0.638543 24.221658 5.207605 31.100322 16.409738 5.504364 9.016351 6.437619 19.6045 3.486404 28.988218L893.82573 486.837924z"
                  p-id="7593"></path>
                <path
                  d="M264.827039 924.31872c0.319272 0.024559 0.441045 0.024559 0.295735-0.024559 0.243547-0.048095 0.367367-0.074701-0.295735-0.074701s-0.539282 0.026606-0.271176 0.074701C264.43409 924.343279 264.532327 924.343279 264.827039 924.31872z"
                  p-id="7594"></path>
              </svg>
              <span class="font-s-13 color-grey-2 font-bold-300"
                    v-text="item.likeTimes>0?item.likeTimes:'--'"></span>
            </div>
            <div class="mr-20" title="评论">
              <svg t="1741407164890" class="icon-theme-1 icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="27498">
                <path
                  d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                  p-id="27499"></path>
              </svg>
              <span class="font-s-13 color-grey-2 font-bold-300"
                    v-text="item.commentTimes>0?item.commentTimes:'--'"></span>
            </div>
          </div>
        </div>
      </div>
      <div v-show="articleList.length==0" style="margin: auto;max-width: 200px">
        <div style="text-align: center;margin-top: 10px">
          <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="40">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-14 color-grey">暂无文章</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "articleGroupList",
  props: {
    // 内容（0：全部，1：关注，2：分类查询,3:最新的）
    type: Number,
    //id
    id: String,
    //是否展示最新
    last: {
      type: Boolean,
      default: true,
    }
  },
  data() {
    return {
      activeName: '推荐',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createTime: null,
        sortType: 1,
        groupingId: null,
      },
      total: 0,
      articleList: [],
      loading: true,
      loadingBottom: true,
      scrollLoading: true,
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.getArticleList();
    }
  },
  methods: {
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if ((scrollTop + clientHeight + clientHeight) >= scrollHeight) {
        if (!this.scrollLoading) return;
        this.load()
      }
    },
    load() {
      if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
        this.scrollLoading = false;
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        this.loadingBottom = true;
        switch (this.type) {
          case 1:
            this.$API("/white/article/follow/list", this.$get(), this.queryParams).then(res => {
              res.data.records.forEach(item => {
                this.articleList.push(item)
              })
              this.total = res.data.total;
            }).finally(() => this.scrollLoading = true);
            break
          case 2:
            this.$API("/white/article/label/list", this.$get(), this.queryParams).then(res => {
              res.data.records.forEach(item => {
                this.articleList.push(item)
              })
              this.total = res.data.total;
            }).finally(() => this.scrollLoading = true);
            break
          case 3:
            this.queryParams.createTime = 1;
            this.$API("/white/article/sort", this.$get(), this.queryParams).then(res => {
              res.rows.forEach(item => {
                this.articleList.push(item)
              })
              this.total = res.data.total;
            }).finally(() => this.scrollLoading = true);
            break
          default:
            break
        }
      } else {
        this.loadingBottom = false;
      }
    },
    handleClick(tab) {
      if (tab.label == '推荐') {//推荐
        this.queryParams.sortType = 1;
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 20;
      } else if (tab.label == '最新') {//最新
        this.queryParams.sortType = 2;
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 20;
      }
      this.getArticleList();
    },
    getArticleList() {
      this.loading = true;
      this.queryParams.groupingId = this.$route.params.id;
      switch (this.type) {
        case 1:
          this.$API("/white/article/follow/list", this.$get(), this.queryParams).then(res => {
            this.articleList = res.data.records;
            this.total = res.data.total;
          }).finally(() => this.loading = false);
          break
        case 2:
          this.$API("/white/article/label/list", this.$get(), this.queryParams).then(res => {
            this.articleList = res.data.records;
            this.total = res.data.total;
          }).finally(() => this.loading = false);
          break
        case 3:
          this.queryParams.createTime = 1;
          console.log(" this.queryParams.createTime:", this.queryParams.createTime)
          this.$API("/white/article/sort", this.$get(), this.queryParams).then(res => {
            this.articleList = res.rows;
            this.total = res.total;
          }).finally(() => this.loading = false);
          break
        default:
          break
      }
    }
  },
  mounted() {
    this.getArticleList();
    window.addEventListener('scroll', this.getData, true);
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style scoped>
@import url("components/css/pc/article-group-list.css");

</style>
