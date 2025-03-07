<template>
  <div>
    <el-card class="box-card">
      <div>
        <div class="flex-space-between mb-20">
          <div>数据概括</div>
          <div>
            <div class="color-grey mr-10 font-s-14" v-if="!userDateLoading">
              统计时间：{{ $utils.parseTime(countUseInfo.updateTime, '{y}-{m}-{d}') }}
              <span> | 查看更多</span>
            </div>
          </div>
        </div>
        <div>
          <!--          基本数据-->
          <div class="mb-40">
            <el-skeleton class="mt-10" v-if="userDateLoading" :rows="6" animated/>
            <div v-show="!userDateLoading">
              <el-row class="font-s-14">
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="文章总数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">
                      <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(countUseInfo.uuid)"
                                 target="_blank">
                        文章总数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.articleCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1" style="height: 1px">
                </el-col>
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="专栏数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">
                      <nuxt-link :to="'/user_home/special?uuid='+$base64.encode(countUseInfo.uuid)"
                                 target="_blank">
                        专栏数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.specialColumnCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1" style="height: 1px">
                </el-col>
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="收藏夹数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">
                      <nuxt-link :to="'/user_home/collection?uuid='+$base64.encode(countUseInfo.uuid)"
                                 target="_blank">
                        收藏夹数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.collectionCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1" style="height: 1px">
                </el-col>
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title=" 关注数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">
                      <nuxt-link :to="'/user_home/follow?uuid='+$base64.encode(countUseInfo.uuid)"
                                 target="_blank">
                        关注数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.followCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
              <el-row class="font-s-14 mt-20">
                <!--                <el-col :span="5">-->
                <!--                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="总获赞数">-->
                <!--                    <div class="cursor-pointer hover-color">总获赞数</div>-->
                <!--                    <div class="sum-data-cl"-->
                <!--                         v-text="countUseInfo.fansFabulousCount==0?'&#45;&#45;':countUseInfo.fansFabulousCount"></div>-->
                <!--                  </el-card>-->
                <!--                </el-col>-->
                <!--                <el-col :span="1" style="height: 1px">-->
                <!--                </el-col>-->
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="总获评论数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">总获评论数</div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.fansCommentCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1" style="height: 1px"/>
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="专辑总数">
                    <div class="cursor-pointer hover-cl font-s-4 mb-12">
                      <nuxt-link to="/user_admin/dictum/album-manage">
                        专辑总数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.albumCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1" style="height: 1px"/>
                <el-col :span="5">
                  <el-card shadow="hover" class="ba-color-grey-2 text-center" title="名言总数">
                    <div class="cursor-pointer hover-cl  font-s-4 mb-12">
                      <nuxt-link :to="'/dictum/space/content-list?uuid='+$base64.encode(countUseInfo.uuid)"
                                 target="_blank">
                        名言总数
                      </nuxt-link>
                    </div>
                    <div class="sum-data-cl">
                      <countTo :startVal='0' :endVal='countUseInfo.dictumCount' :duration='2000'></countTo>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </div>
          <el-divider></el-divider>
          <!--          提交记录图-->
          <div v-if="submissionLoading" class="mb-30">
            <submission-chart :profile="submissionObj"></submission-chart>
          </div>
          <div class="overflow-hidden">
            <el-skeleton class="mt-10" v-if="statisticalChartLoading" :rows="6" animated/>
            <!--  统计图        -->
            <div v-show="!statisticalChartLoading">
              <!--            收藏夹统计饼图-->
              <div style="float: left;width: 40%;">
                <div class="font-s-18 font-bold ml-20 color-blue"> 收藏夹统计图</div>
                <div id="timeCollectionCensus" style="height: 320px"></div>
              </div>
              <!--            专栏-->
              <div style="float: right;width: 40%;">
                <div class="align-center font-s-18 font-bold ml-10 color-blue"> 专栏统计图</div>
                <div id="timeSpecialCensus" style="height: 320px"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import countTo from 'vue-count-to';

export default {
  name: "dataIndex",
  components: {countTo},
  data() {
    return {
      query: {
        month: this.$utils.parseTime(new Date(), '{y}-{m}'),
      },
      countUseInfo: {
        articleCount: 0,
        specialColumnCount: 0,
        collectionCount: 0,
        followCount: 0,
        fansFabulousCount: 0,
        fansCommentCount: 0,
        albumCount: 0,
        dictumCount: 0,
        updateTime: 0,
      },
      statisticalChartLoading: true,
      userDateLoading: true,
      submissionObj: {},
      submissionLoading: false,
    }
  },
  methods: {
    routeJumpUuid(url, id) {
      let routeInfo = this.$router.resolve({
        path: url,
        query: {uuid: this.$base64.encode(id)},
      });
      window.open(routeInfo.href, '_blank');
    },
    CountUserCensusInfo() {
      this.$API("/user/census/count/user", this.$get(), this.query).then(res => {
        this.countUseInfo = res.data;
        this.$API("/user/census/submission", "get").then(res => {
          this.submissionObj = res.data.reduce((acc, item) => {
            acc[item.dateTimes] = item.censusSum;
            return acc;
          }, {});
          this.submissionLoading = true;
        });
      }).finally(() => this.userDateLoading = false);
    },
    //统计图
    CensusList() {
      //收藏夹统计
      import('@antv/g2plot').then(({Pie}) => {
        this.$API("/user/census/collection", "get", this.query).then(res => {
          if (res.data.length != 0) {
            const piePlot = new Pie('timeCollectionCensus', {
              appendPadding: 10,
              data: res.data,
              angleField: 'censusSum',
              colorField: 'title',
              radius: 1,
              innerRadius: 0.64,
              meta: {
                censusSum: {
                  formatter: (v) => `文章 ${v}`,
                },
              },
              label: {
                type: 'inner',
                offset: '-50%',
                autoRotate: false,
                style: {textAlign: 'center'},
              },
              // 添加 中心统计文本 交互
              interactions: [
                {type: 'element-selected'},
                {type: 'element-active'},
                {
                  type: 'pie-statistic-active',
                  cfg: {
                    start: [
                      {trigger: 'element:mouseenter', action: 'pie-statistic:change'},
                      {trigger: 'legend-item:mouseenter', action: 'pie-statistic:change'},
                    ],
                    end: [
                      {trigger: 'element:mouseleave', action: 'pie-statistic:reset'},
                      {trigger: 'legend-item:mouseleave', action: 'pie-statistic:reset'},
                    ],
                  },
                },
              ],
            });
            piePlot.render();
          }
          //专栏统计
          this.$API("/user/census/special", "get", this.query).then(res => {
            if (res.data.length == 0) {
              return;
            }
            const piePlot = new Pie('timeSpecialCensus', {
              appendPadding: 10,
              data: res.data,
              angleField: 'censusSum',
              colorField: 'title',
              radius: 0.8,
              meta: {
                censusSum: {
                  formatter: (v) => `${v}篇`,
                },
              },
              label: {
                type: 'outer',
              },
              interactions: [{type: 'element-active'}],
            });
            piePlot.render();
          });
        }).catch((error) => {
          // 处理导入发生错误的情况
        });
        // 按时间统计用户文章数据
        // timeArticleCensus(this.query).then(res => {
        //   const bar = new Bar('container', {
        //     data: res.data,
        //     xField: 'dateTimes',
        //     yField: 'censusSum',
        //     seriesField: 'dateTimes',
        //     legend: {
        //       position: 'top-left',
        //     },
        //   });
        //   bar.render();
        // })
      }).finally(() => this.statisticalChartLoading = false);
    },
  },
  mounted() {
    this.CountUserCensusInfo();
    this.CensusList();
  }
}
</script>

<style scoped>
.sum-data-cl {
  font-size: 32px;
  color: #1d2129;
  font-weight: bold;
  margin-top: 6px;
}

.el-card.is-always-shadow {
  box-shadow: none;
  border-style: none;
}
</style>
