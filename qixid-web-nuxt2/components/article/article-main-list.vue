<template>
  <div class="flex-1">
    <div class="flex-grow-1">
      <el-skeleton class="mt-10 _module_explicit-padding-lf-20" style="width: 100%" :rows="8" animated
                   v-show="initLoading"/>
      <div>
        <div v-for="(item, index) of listInformationList" :key="item.id"
             class="article-item"
             :ref="`articleItem${index}`">
          <div class="flex-space-between align-items-center">
            <div class="flex-8" style="padding: 0 15px">
              <div class="flex-space-between mb-15 mr-15 color-grey-2">
                <div class="article-top font-s-13">
                  <div class="">
                    <nuxt-link class="hover-cl color-grey"
                               :to="`/user_home/article?uuid=`+$base64.encode(item.userId)"
                               target="_blank">
                      {{ item.nickname }}
                    </nuxt-link>
                    <span class="ml-4 mr-4 color-grey-3">/</span>
                    <span class=""
                          :title="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')">
                    {{ $utils.reckonTime(item.createTime, '{y}-{m}-{d}') }}</span>
                  </div>
                  <div class=" hover-cl ml-12 font-bold-300" title="分类">
                    <nuxt-link :to="`/external_info/label-group-info?data=`+item.groupingId" target="_blank">
                      #{{ item.groupingName }}
                    </nuxt-link>
                  </div>
                </div>
              </div>
              <h1 class="mb-10">
                <nuxt-link class="text-underline-hover font-s-16"
                           :to="`/article-details/`+$base64.encode(item.id)"
                           rel="noopener" target="_blank">
                  {{ item.articleTitle }}
                </nuxt-link>
              </h1>
              <p class="font-s-14 line-height-28 overflow-nowrap-2 color-grey mb-10">
                {{ item.articleAbstract }}
              </p>
            </div>
            <div v-if="item.articleCover" class="article-cover-wrapper mt-15 _module_hiding">
              <lazy-image
                v-if="item.articleCover!=null && item.articleCover!=''"
                :src="item.articleCover"
                image-class="article-cover-img"
                :width="158"
                :height="98"
              />
            </div>
          </div>
          <div class="mt-5 ml-15 font-s-14 flex-left color-grey-2">
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
              <span class="font-s-13"
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
              <span class="font-s-13"
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
              <span class="font-s-13"
                    v-text="item.commentTimes>0?item.commentTimes:'--'"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils';
import LazyImage from '~/components/lazy-image.vue';

export default {
  name: "articleMainList",
  components: {
    LazyImage
  },
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
      initLoading: true,
      animator: null, // 动画器实例
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
        this.scrollLoading = false;
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        const startIndex = this.listInformationList.length; // 记录新增前的索引
        this.$API("/white/article/recommend/list", this.$get(), this.queryParams).then(res => {
          res.rows.forEach(item => {
            this.listInformationList.push(item)
          })
          this.total = res.total;
          // 为新增的项目触发动画
          this.animator.triggerNewItemsAnimation(startIndex, res.rows.length, 'articleItem');
        }).finally(() => this.scrollLoading = true)
      }
    },
    listInformation() {
      this.$API("/white/article/recommend/list", this.$get(), this.queryParams).then(res => {
        this.listInformationList = res.rows
        this.total = res.total;
        // 数据加载完成后触发动画
        this.animator.triggerAllItemsAnimation(this.listInformationList, 'articleItem');
      }).finally(() => this.initLoading = false);
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
    // 初始化动画器
    this.animator = createAnimator(this, 'mainArticle');
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
