<template>
  <div class="module-main-2">
    <div class="label-group-info-title">
      <el-skeleton style="padding: 20px 10px" :rows="3" animated v-if="initialLoading"/>
      <div v-show="!initialLoading" class="margin-auto">
        <p class="font-s-32 font-bold text-center">{{ labelGroupInfo.groupingName }}</p>
        <div class="font-s-13 line-height-24 color-fefefe text-center mt-6">
          <span>文章数：{{ labelGroupInfo.articleNumber }}</span>
        </div>
      </div>
    </div>
    <div>
      <el-skeleton style="padding: 20px 10px" :rows="6" animated v-if="initialLoading"/>
      <div v-show="!initialLoading">
        <ul>
          <li v-for="item of listInformationList" :key="item.id" class="search-data-li">
            <div style="padding: 0 10px ">
              <!--          用户信息-->
              <el-row :gutter="20" class="mb-10">
                <el-col :span="20" class="font-s-12 color-grey">
                <span class="font-bold cursor-pointer hover-cl">
                  <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(item.userId)" target="_blank">
                    {{ item.nickname }}
                  </nuxt-link>
                  </span>&nbsp;|
                  <span class="ml-6">{{ item.occupation == null ? '职业-~-' : item.occupation }}</span>&nbsp;|
                  <span class="ml-6">{{ $utils.parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="20" class="mb-10">
                <nuxt-link :to="`/article/article-details/`+$base64.encode(item.id)" target="_blank">
                  <el-col :span=" item.articleCover==null?24:20" class="cursor-pointer" title="查看文章">
                    <p class="font-bold line-height-24 font-s-16 mb-10 hover-cl">{{ item.articleTitle }}</p>
                    <p class="font-s-13 line-height-24 color-grey abstract-cl-2 text-indent-13">{{
                        item.articleAbstract
                      }}</p>
                  </el-col>
                  <el-col :span="3" v-if="item.articleCover">
                    <el-image :src="item.articleCover" fit="cover" style="width: 100px;height: 60px"></el-image>
                  </el-col>
                </nuxt-link>
              </el-row>
              <div class="mt-10 font-s-14 flex-left">
                <div class="mr-20">
                  <svg t="1709733858303" class="icon icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="2788">
                    <path
                      d="M204 411.3c3-3.6 9.5-10.3 19.3-19.2 16.7-15.2 36.5-30.4 59.4-44.6 65.4-40.7 141.7-65 228.4-65 86.7 0 163.1 24.4 228.5 65 22.9 14.2 42.7 29.4 59.4 44.6 9.8 9 16.3 15.7 19.3 19.2 8.2 9.7 22.8 11 32.7 2.9 9.8-8.1 11.2-22.5 3-32.2-4.2-5-12.1-13.3-23.5-23.6-18.7-17-40.7-33.9-66.1-49.7-72.4-45-157.2-72.1-253.2-72.1-96.1 0-180.8 27.1-253.2 72.1-25.4 15.8-47.5 32.7-66.1 49.7-11.4 10.3-19.3 18.6-23.5 23.6-8.2 9.7-6.9 24.1 3 32.2 9.8 8.2 24.4 6.8 32.6-2.9z m0 0"
                      p-id="2789"></path>
                    <path
                      d="M371.9 511c0 75.7 62.4 137.1 139.3 137.1S650.5 586.7 650.5 511s-62.4-137.1-139.3-137.1S371.9 435.2 371.9 511z m232.2 0c0 50.5-41.6 91.4-92.9 91.4-51.3 0-92.9-40.9-92.9-91.4 0-50.5 41.6-91.4 92.9-91.4 51.3 0 92.9 40.9 92.9 91.4z m0 0"
                      p-id="2790"></path>
                    <path
                      d="M818.4 614.4c-3 3.6-9.5 10.3-19.3 19.2-16.7 15.2-36.5 30.4-59.4 44.6-65.4 40.7-141.7 65-228.5 65-86.7 0-163.1-24.4-228.4-65-22.9-14.2-42.8-29.4-59.4-44.6-9.8-8.9-16.3-15.7-19.3-19.2-8.2-9.7-22.8-11-32.7-2.9-9.8 8.1-11.2 22.5-3 32.2 4.3 5 12.1 13.3 23.5 23.6 18.7 17 40.7 33.9 66.1 49.7 72.4 45 157.2 72.1 253.2 72.1 96.1 0 180.8-27 253.3-72.1 25.4-15.8 47.4-32.7 66.1-49.7 11.4-10.3 19.2-18.6 23.5-23.6 8.2-9.7 6.9-24.1-3-32.2-9.8-8.1-24.5-6.8-32.7 2.9z m0 0"
                      p-id="2791"></path>
                  </svg>

                  <span class="font-s-13 color-grey"
                        v-text="item.numberTimes>0?item.numberTimes:'浏览'"></span>
                </div>
                <div class="mr-20">
                  <svg t="1697947657508" class="icon icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg"
                       p-id="1484">
                    <path
                      d="M857.28 344.992h-264.832c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-71.808-153.792-140.544-143.808-60.608 8.8-89.536 59.904-89.536 125.536v59.296c0 76.064-58.208 140.928-132.224 148.064l-117.728-0.192A67.36 67.36 0 0 0 64 483.04V872c0 37.216 30.144 67.36 67.36 67.36h652.192a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824zM128 872V483.04c0-1.856 1.504-3.36 3.36-3.36H208v395.68H131.36A3.36 3.36 0 0 1 128 872z m767.328-417.088l-73.728 388.96a38.72 38.72 0 0 1-38.048 31.488H272V476.864a213.312 213.312 0 0 0 173.312-209.088V208.512c0-37.568 12.064-58.912 34.72-62.176 27.04-3.936 67.36 38.336 67.36 80.48 0 37.312-9.504 84-28.864 139.712a32 32 0 0 0 30.24 42.496h308.512a38.72 38.72 0 0 1 38.048 45.888z"
                      p-id="1485"></path>
                  </svg>
                  <span class="font-s-13 color-grey"
                        v-text="item.likeTimes>0?item.likeTimes:'赞'"></span>
                </div>
                <div class="mr-20">
                  <svg t="1697955184702" class="icon icon-size-16 svg-translateY-4" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg"
                       p-id="2570">
                    <path
                      d="M850.879104 96.41591l-676.303067 0c-60.681034 0-110.049418 49.367361-110.049418 110.049418l0 446.200388c0 60.681034 49.367361 110.049418 110.049418 110.049418l90.307795 0L396.936381 931.129846c3.793396 4.838192 9.598612 7.66354 15.746636 7.66354s11.952216-2.825348 15.746636-7.66354l132.052548-168.414711 290.396903 0c60.681034 0 110.049418-49.367361 110.049418-110.049418L960.928522 206.465329C960.928522 145.784294 911.561162 96.41591 850.879104 96.41591zM920.91111 652.665717c0 38.614459-31.416524 70.030983-70.030983 70.030983L550.744419 722.6967c-6.147 0-11.952216 2.825348-15.745612 7.66354L412.683017 886.356107l-122.31579-155.995867c-3.792373-4.838192-9.597589-7.66354-15.745612-7.66354l-100.045577 0c-38.614459 0-70.030983-31.416524-70.030983-70.030983L104.545054 206.465329c0-38.614459 31.416524-70.030983 70.030983-70.030983l676.303067 0c38.614459 0 70.030983 31.416524 70.030983 70.030983L920.910087 652.665717z"
                      p-id="2571"></path>
                    <path
                      d="M272.621051 344.526731c-44.132126 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.903721 80.036871 80.035848 80.036871s80.035848-35.904745 80.035848-80.036871C352.655875 380.430452 316.752154 344.526731 272.621051 344.526731zM272.621051 464.582037c-22.065552 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.952884-40.017412 40.017412-40.017412 22.065552 0 40.017412 17.951861 40.017412 40.017412C312.638463 446.629153 294.686602 464.582037 272.621051 464.582037z"
                      p-id="2572"></path>
                    <path
                      d="M512.727571 344.526731c-44.132126 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.903721 80.036871 80.035848 80.036871 44.132126 0 80.035848-35.904745 80.035848-80.036871C592.763418 380.430452 556.859697 344.526731 512.727571 344.526731zM512.727571 464.582037c-22.065552 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.951861-40.017412 40.017412-40.017412 22.065552 0 40.017412 17.951861 40.017412 40.017412C552.746006 446.629153 534.793122 464.582037 512.727571 464.582037z"
                      p-id="2573"></path>
                    <path
                      d="M752.836137 344.526731c-44.131103 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.904745 80.036871 80.035848 80.036871s80.035848-35.904745 80.035848-80.036871C832.871985 380.430452 796.96724 344.526731 752.836137 344.526731zM752.836137 464.582037c-22.066575 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.951861-40.017412 40.017412-40.017412s40.017412 17.951861 40.017412 40.017412C792.853549 446.629153 774.902712 464.582037 752.836137 464.582037z"
                      p-id="2574"></path>
                  </svg>
                  <span class="font-s-13 color-grey"
                        v-text="item.commentTimes>0?item.commentTimes:'评论'"></span>
                </div>
              </div>
            </div>
          </li>
        </ul>
        <div v-show="listInformationList.length==0" style="margin: auto;max-width: 200px">
          <div class="text-center pt-20">
            <svg t="1666708559980" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="2698" width="60" height="60">
              <path
                d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
                fill="#1890ff" p-id="2699"></path>
            </svg>
            <div class="font-s-14 color-grey">暂无相关文章</div>
          </div>
        </div>
        <div v-show="listInformationList.length>20" class="pb-10">
          <div class="border-ts-class" v-if="loading">加载中</div>
          <div class="border-ts-class" v-if="!loading">没有更多了...</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "labelGroupInfo",
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        groupingId: this.$route.query.data,
      },
      labelGroupInfo: {},
      userInfo: {},
      listInformationList: [],
      loading: false,
      total: 0,
      scrollLoading: true,
      initialLoading: true,
      loginDialog: false,
      followLoading: false,
    }
  },
  methods: {
    LabelGroupingInfos() {
      this.$API("/white/label/grouping/info/" + this.queryParams.groupingId, "get").then(res => {
        this.labelGroupInfo = res.data;
        this.$API('/white/article/list', 'get', this.queryParams).then(res => {
          this.listInformationList = res.rows;
          this.total = res.total;
          this.initialLoading = false;
        })
      })
    },
    load() {
      if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
        this.scrollLoading = false;
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        this.loading = true;
        this.$API('/white/article/list', 'get', this.queryParams).then(res => {
          res.records.forEach(item => {
            this.listInformationList.push(item)
          })
          this.total = res.total;
        }).finally(() => this.scrollLoading = true)
      } else {
        this.loading = false;
      }
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
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  },
  mounted() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
    this.LabelGroupingInfos();
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
  background-color: #fefefe;
  padding: 10px;
  margin-bottom: 20px;
  border-bottom: 1px solid #dcdfe6;
}

.border-ts-class {
  text-align: center;
  font-size: 13px;
  color: #8a919f;
}

.label-group-info-title {
  background: rgb(141, 46, 205, 0.3);
  background: linear-gradient(90deg, rgba(141, 46, 205, 0.3) 0%, rgba(29, 227, 253, 0.3) 50%, rgba(252, 69, 249, 0.3) 100%);
  padding: 40px 0;
  margin-bottom: 20px;
  margin-top: 20px;
  border-radius: 4px;
}
</style>
