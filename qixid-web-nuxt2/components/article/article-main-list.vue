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
