<template>
  <div class="flex-1">
    <div class="flex-grow-1">
      <el-skeleton class="mt-10 _module_explicit-padding-lf-20" style="width: 100%" :rows="8" animated
                   v-show="initLoading"/>
      <div>
        <div v-for="(item, index) of listInformationList" :key="item.id"
             class="article-item"
             :ref="`articleItem${index}`">
          <div class=" flex-space-between align-items-center">
            <div class="flex-8" style="padding: 0 15px">
              <div>
                <h1 class="mb-10">
                  <nuxt-link class="text-underline-hover font-s-16"
                             :to="`/article-details/`+$base64.encode(item.id)"
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
                  <div class="svg-translateY-3 font-s-13 hover-cl ml-15 font-bold-300" title="分类">
                    <nuxt-link :to="`/external_info/label-group-info?data=`+item.groupingId" target="_blank">
                      # {{ item.groupingName }}
                    </nuxt-link>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="item.articleCover" class="article-cover-div mt-15 _module_hiding">
              <lazy-image
                v-if="item.articleCover!=null && item.articleCover!=''"
                :src="item.articleCover"
                :alt="item.articleTitle"
                image-class="article-cover-img"
              />
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils'
import LazyImage from '~/components/lazy-image.vue'

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
          this.triggerNewItemAnimations(startIndex, res.rows.length);
        }).finally(() => this.scrollLoading = true)
      }
    },
    listInformation() {
      this.$API("/white/article/recommend/list", this.$get(), this.queryParams).then(res => {
        this.listInformationList = res.rows
        this.total = res.total;
        // 数据加载完成后触发动画
        this.triggerArticleAnimations();
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

    // 触发文章项目动画
    triggerArticleAnimations() {
      this.animator.triggerAllItemsAnimation(this.listInformationList, 'articleItem');
    },

    // 为新增的项目触发动画
    triggerNewItemAnimations(startIndex, count) {
      this.animator.triggerNewItemsAnimation(startIndex, count, 'articleItem');
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
  background: #005aa7; /* fallback for old browsers */
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
