<template>
  <div class="mb-40">
    <el-skeleton style="padding: 20px 10px" :rows="6" animated v-if="initLoading"/>
    <div v-show="!initLoading">
      <ul>
        <li v-for="item of listInformationList" :key="item.id" class="search-data-li">
          <!--          用户信息-->
          <el-row :gutter="20" class="mb-15">
            <el-col :span="20" class="font-s-12">
                <span class="font-bold cursor-pointer hover-cl">
                  <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(item.userId)" target="_blank">
                    {{ item.nickname }}
                  </nuxt-link>
                  </span>
              <span class="color-grey-3 ml-2 mr-2">|</span>
              <span class="">{{ item.occupation == null ? '职业-~-' : item.occupation }}</span>
              <span class="color-grey-3 ml-2 mr-2">|</span>
              <span class="font-bold-300">{{ $utils.parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mb-10">
            <nuxt-link :to="`/article/article-details/`+$base64.encode(item.id)" target="_blank">
              <el-col :span=" item.articleCover==null?24:20" class="cursor-pointer" title="查看文章">
                <h1 class="line-height-24 font-s-16 mb-10 text-underline-hover font-bold-s">
                  {{ item.articleTitle }}
                </h1>
                <div class="font-s-14 line-height-26 abstract-cl-2 font-bold-300">
                  {{ item.articleAbstract }}
                </div>
                <div class="mt-15 font-s-14 flex-left font-bold-300">
                  <div class="mr-20" title="浏览">
                    <svg t="1741407328107" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="30371">
                      <path
                        d="M512 153.6c-123.477333 0-235.178667 51.9168-323.089067 137.557333C117.230933 360.96 68.266667 448.938667 68.266667 505.173333v13.6704c0 56.234667 48.981333 144.196267 120.644266 214.016C276.804267 818.466133 388.522667 870.4 512 870.4c123.613867 0 235.246933-51.234133 322.7648-135.970133 71.799467-69.5296 120.490667-157.320533 120.968533-215.296V505.173333c-0.477867-58.2656-49.169067-146.056533-120.968533-215.586133C747.246933 204.8512 635.5968 153.6 512 153.6z m0 68.266667c104.6528 0 199.799467 43.6736 275.268267 116.753066 59.886933 57.992533 99.8912 130.133333 100.1984 166.843734v13.380266c-0.3072 36.420267-40.311467 108.544-100.181334 166.536534C711.7824 758.459733 616.635733 802.133333 512 802.133333c-104.3456 0-199.560533-44.2368-275.456-118.186666C176.810667 625.7664 136.533333 553.4208 136.533333 518.843733V505.173333c0-34.577067 40.277333-106.922667 100.010667-165.12C312.439467 266.120533 407.6544 221.866667 512 221.866667z"
                        p-id="30372"></path>
                      <path
                        d="M512 341.333333c-94.2592 0-170.666667 76.407467-170.666667 170.666667s76.407467 170.666667 170.666667 170.666667 170.666667-76.407467 170.666667-170.666667-76.407467-170.666667-170.666667-170.666667z m0 68.266667a102.4 102.4 0 1 1 0 204.8 102.4 102.4 0 0 1 0-204.8z"
                        p-id="30373"></path>
                    </svg>
                    <span class="font-s-13 color-grey"
                          v-text="item.numberTimes>0?item.numberTimes:'--'"></span>
                  </div>
                  <div class="mr-20" title="点赞">
                    <svg t="1741407060928" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="25461">
                      <path
                        d="M757.76 852.906667c36.906667-0.021333 72.832-30.208 79.296-66.56l51.093333-287.04c10.069333-56.469333-27.093333-100.522667-84.373333-100.096l-10.261333 0.085333a19972.266667 19972.266667 0 0 1-52.842667 0.362667 3552.853333 3552.853333 0 0 1-56.746667 0l-30.997333-0.426667 11.498667-28.8c10.24-25.642667 21.76-95.744 21.504-128.021333-0.618667-73.045333-31.36-114.858667-69.290667-114.410667-46.613333 0.554667-69.461333 23.466667-69.333333 91.136 0.213333 112.661333-102.144 226.112-225.130667 225.109333a1214.08 1214.08 0 0 0-20.629333 0l-3.52 0.042667c-0.192 0 0.64 409.109333 0.64 409.109333 0-0.085333 459.093333-0.490667 459.093333-0.490666z m-17.301333-495.914667a15332.288 15332.288 0 0 0 52.693333-0.362667l10.282667-0.085333c84.010667-0.618667 141.44 67.52 126.72 150.250667L879.061333 793.813333c-10.090667 56.661333-63.68 101.696-121.258666 101.76l-458.922667 0.384A42.666667 42.666667 0 0 1 256 853.546667l-0.853333-409.173334a42.624 42.624 0 0 1 42.346666-42.730666l3.669334-0.042667c5.909333-0.064 13.12-0.064 21.333333 0 98.176 0.789333 182.293333-92.437333 182.144-182.378667C504.469333 128.021333 546.24 86.186667 616.106667 85.333333c65.173333-0.768 111.68 62.506667 112.448 156.714667 0.256 28.48-6.848 78.826667-15.701334 115.050667 8.021333 0 17.28-0.042667 27.584-0.106667zM170.666667 448v405.333333h23.466666a21.333333 21.333333 0 0 1 0 42.666667H154.837333A26.709333 26.709333 0 0 1 128 869.333333v-437.333333c0-14.784 12.074667-26.666667 26.773333-26.666667h38.912a21.333333 21.333333 0 0 1 0 42.666667H170.666667z"
                        p-id="25462"></path>
                    </svg>
                    <span class="font-s-13 color-grey"
                          v-text="item.likeTimes>0?item.likeTimes:'--'"></span>
                  </div>
                  <div class="mr-20" title="评论">
                    <svg t="1741407164890" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="27498">
                      <path
                        d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                        p-id="27499"></path>
                    </svg>
                    <span class="font-s-13 color-grey"
                          v-text="item.commentTimes>0?item.commentTimes:'--'"></span>
                  </div>
                </div>
              </el-col>
              <el-col :span="3" v-if="item.articleCover" class="mt-20">
                <el-image :src="item.articleCover" fit="cover"
                          style="width: 130px;height: 90px;border-radius: 2px"></el-image>
              </el-col>
            </nuxt-link>
          </el-row>
        </li>
      </ul>
      <div v-show="listInformationList.length==0" style="margin: auto;max-width: 200px">
        <div class="text-center pt-20">
          <svg t="1666708559980" class="icon icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="60">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-14 color-grey-2">暂无相关文章</div>
        </div>
      </div>
      <div v-show="listInformationList.length>20" class="pb-10">
        <div class="border-ts-class" v-if="loading">加载中</div>
        <div class="border-ts-class" v-if="!loading">没有更多了...</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "articleSearchList",
  props: {
    //请求参数
    queryParams: {
      type: Object,
      default: () => ({
        pageNum: 1,
        pageSize: 20
      })
    },
    //重新加载触发（父级页面累加即可）
    reload: {
      type: Number,
      default: 0
    },
  },
  data() {
    return {
      listInformationList: [],
      initLoading: true,
      total: 0,
      loading: false,
      scrollLoading: true,
    }
  },
  watch: {
    reload() {
      this.articleWhiteList();
    },
  },
  methods: {
    articleWhiteList() {
      this.$API('/white/article/list', 'get', this.queryParams).then(res => {
        this.listInformationList = res.rows;
        this.total = res.total;
        this.initLoading = false;
      })
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        this.load()
      }
    },
    load() {
      if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
        this.scrollLoading = false;
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        this.loading = true;
        this.$API('/white/article/list', 'get', this.queryParams).then(res => {
          res.rows.forEach(item => {
            this.listInformationList.push(item)
          })
          this.total = res.total;
        }).finally(() => this.scrollLoading = true)
      } else {
        this.loading = false;
      }
    },
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  },
  mounted() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
    this.articleWhiteList();
  }

}
</script>

<style scoped>
.abstract-cl-2 {
  /*超出的文本隐藏*/
  overflow: hidden;
  /*  溢出用省略号显示*/
  text-overflow: ellipsis;
  /*  默认不换行*/
  /*white-space: nowrap;*/
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

.search-data-li {
  background-color: #FFFFFF;
  padding: 15px 10px 20px 10px;
  margin-bottom: 20px;
  border-bottom: 1px solid #dcdfe6;
  transition: .3s;
}

.search-data-li:hover {
  border-bottom: 1px solid var(--hover-color);
}


.border-ts-class {
  text-align: center;
  font-size: 13px;
  color: #8a919f;
}
</style>
