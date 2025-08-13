<template>
  <div class="module-main">
    <div style="width: 30%;margin-left: 26px" class="mt-30 _module_hiding">
      <el-input placeholder="标签关键字" v-model="labelName" @keyup.enter.native="labelSearch">
        <el-button slot="append" icon="el-icon-search" @click="labelSearch"></el-button>
      </el-input>
    </div>
    <el-skeleton :rows="12" class="mt-20 _module_explicit-padding-lf-20" animated v-if="loading"/>
    <div v-if="!loading" class="flex-space-around flex-wrap-wrap">
      <div v-for="(item,index) in dateList" :key="index" class="label-item" :ref="`labelItem${index}`">
        <div class="flex-space-around">
          <div v-html="item.labelCover"></div>
          <div>
            <div class="mb-15" :title="item.articleNumber+'篇文章'">
              <nuxt-link class="font-bold cursor-pointer mb-8 hover-cl" target="_blank"
                         :to="'/external_info/label-info?data='+item.id+'&type='+2">
                {{ item.labelName }}
              </nuxt-link>
              <svg v-if="item.articleNumber!=0" t="1741580677085" class="icon icon-size-16 svg-translateY-2"
                   viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="7300">
                <path
                  d="M859.9104 609.92512l0 45.6c-0.67968 2.21952-1.5104 4.4352-1.9648 6.70464-4.66048 24.09984-7.28448 48.82944-14.31552 72.22016-20.84992 69.02016-59.92064 126.53952-114.6944 173.50016-42.24512 36.2496-89.7856 62.36544-144.1344 75.22048-17.87008 4.23552-36.19456 6.73024-54.32064 10.0352l-45.5744 0c-2.21952-0.6848-4.49024-1.72032-6.75456-1.87008-48.12544-2.9952-93.72544-15.52512-136.50048-37.38496-80.86528-41.18528-139.19488-102.5152-165.83552-190.74048-5.67424-18.8544-8.03968-38.62016-11.9744-57.97504l0-43.50976c1.7152-10.69056 3.2-21.47456 5.21984-32.16 8.61952-46.68544 29.36576-88.0256 56.83968-126.19008 25.91488-35.92064 53.44-70.70464 78.016-107.53536 26.56896-39.95008 39.424-84.2944 31.88992-132.9152-1.4848-9.60512-2.87488-19.20896-4.33536-28.76416 0.98048-0.25088 1.9648-0.45056 2.9504-0.73088 59.31008 62.16064 68.96512 138.46528 60.49408 220.92032 2.17088-2.31936 3.98592-3.93472 5.37088-5.79968 50.33984-68.08448 71.96416-143.29984 55.55456-227.54688-10.42944-53.58976-32.99456-101.76512-70.32448-141.81504C369.3056 61.84576 349.69472 47.65568 331.61984 32l18.65472 0c1.536 0.62976 2.976 1.7152 4.53504 1.86496 32.82048 2.81984 63.65056 12.95488 93.02016 27.2 67.17056 32.51584 121.62048 80.58496 167.17056 139.22048 66.9504 86.27968 110.48448 181.99424 119.10528 292.19968 3.30496 42.06976-0.9856 82.95552-12.19968 123.2896-4.23552 15.27552-10.21056 30.04544-15.68 45.94944 21.72544-9.25056 38.24-23.38944 50.9952-41.7152 38.04032-54.77504 48.67456-115.85536 40.05504-183.38048 2.80064 3.24992 4.23552 4.53504 5.21472 6.14528 22.91456 36.19968 40.05504 74.81472 49.0048 116.78464C855.05024 576.17024 857.11488 593.1648 859.9104 609.92512M501.56544 529.61536c-0.85504 0.60544-1.79072 1.2352-2.67008 1.84064-1.18528 16.64-2.06976 33.30048-3.68 49.93536-2.37056 25.38496-8.44544 49.85984-20.32 72.62464-14.52032 27.87968-38.7904 45.21984-65.69088 59.01056-29.00992 14.9696-47.28448 36.34944-49.65504 70.10048-2.46912 34.70976 7.96544 63.86944 35.94496 85.20064 26.21568 19.96032 56.84096 26.4704 89.3056 25.38496 51.82976-1.6896 90.4448-26.32064 105.92512-78.1952 11.11552-37.23008 9.30048-74.71488 1.86496-112.19456-10.16064-51.37536-28.76544-99.26528-60.60032-141.2352C523.04512 550.36032 511.7504 540.40448 501.56544 529.61536"
                  fill="#74b9ff" p-id="7301"></path>
              </svg>
              <span class="font-s-14 color-grey" v-if="item.articleNumber!=0">{{ item.articleNumber }}</span>
            </div>
            <span v-if="item.followNumber!=0" title="关注数">
            <svg t="1750495522316" class="icon-theme-2 svg-translateY-2 icon-size-14"
                 viewBox="0 0 1058 1024"
                 version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="7215">
              <path
                d="M529.163442 1024l-15.439957-10.2014C492.769258 1000.288638 0.069204 672.465267 0.069204 339.403339A332.786214 332.786214 0 0 1 324.308299 0a319.276252 319.276252 0 0 1 204.855143 75.545504A318.173398 318.173398 0 0 1 733.191444 0a332.510501 332.510501 0 0 1 325.066236 339.954766 575.965536 575.965536 0 0 1-122.692515 311.28056A1500.984383 1500.984383 0 0 1 747.804261 854.711901a2063.71567 2063.71567 0 0 1-203.200862 159.638126zM324.584013 55.142703A277.643511 277.643511 0 0 0 55.211907 338.851912c0 273.232095 390.961766 560.801292 473.675822 618.425417a2059.579968 2059.579968 0 0 0 182.522347-144.749596 1448.323102 1448.323102 0 0 0 181.143781-195.480883 518.617124 518.617124 0 0 0 110.285406-279.297792A277.367798 277.367798 0 0 0 732.640017 55.142703a261.927841 261.927841 0 0 0-183.900915 77.475498l-19.57566 19.851374-19.57566-19.851374A262.754981 262.754981 0 0 0 324.584013 55.142703z"
                p-id="7216"></path>
            </svg>
            <span class="font-s-13 color-grey-2">{{ item.followNumber }}</span>
            </span>
            <el-button type="primary" plain size="mini" v-if="!item.isFollow" :loading="item.buttonLoading"
                       @click="followClick(item)">关注
            </el-button>
            <el-button type="primary" size="mini" v-if="item.isFollow" :loading="item.buttonLoading"
                       @click="followClick(item)">取关
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils'

export default {
  head: {
    title: `标签 - ${process.env.PROJECT_NAME}`,
  },
  name: "labelIndex",
  data() {
    return {
      dateList: [],
      loading: true,
      labelName: null,
      loginDialog: false,
      userInfo: null,
      animator: null, // 动画器实例
    }
  },
  methods: {
    labelSearch() {
      this.loading = true; // 搜索时显示loading状态
      this.getDate();
    },
    followClick(item) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      item.buttonLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API("/user/follow/cancel", "post", null, {targetId: item.id, type: 2,})
          .finally(() => {
            item.buttonLoading = false;
            this.getDate(); // 重新获取数据，会触发动画
          });
      } else {
        item.isFollow = true;
        this.$API("/user/follow/add", "post", null, {targetId: item.id, type: 2,})
          .finally(() => {
            item.buttonLoading = false;
            this.getDate(); // 重新获取数据，会触发动画
          });
      }
    },
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    getBasicsUsers() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
      });
    },
    getDate() {
      this.$API("/white/dictum/system/label", this.$get(), {"label": this.labelName}).then(res => {
        this.dateList = res.data;
      }).finally(() => {
        this.loading = false;
        // 确保loading完成并且DOM更新后再触发动画
        this.$nextTick(() => {
          this.animator.triggerAllItemsAnimation(this.dateList, 'labelItem');
        });
      });
    }
  },
  mounted() {
    // 初始化动画器
    this.animator = createAnimator(this, 'commonList')
    this.getDate();
    this.getBasicsUsers();
  }
}
</script>

<style scoped>
.label-item {
  background-color: #FFFFFF;
  padding: 20px 4px;
  margin: 20px;
  border-radius: 4px;
  width: 200px;
  text-align: center;
  cursor: pointer;
  border: 1px solid #e2e2e5;
  transition: all 0.2s linear 0s;
}

.label-item:hover {
  border: 1px solid #95a5a6;
}

.el-button--mini {
  padding: 7px 15px;
}

</style>
