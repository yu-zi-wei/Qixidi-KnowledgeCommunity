<template>
  <div class="mt-30">
    <div class="background-color-fefefe" style="width: 100%;padding: 15px 20px">
      <div class="font-s-18 font-bold color-stand-out mb-10 flex-space-between">
        <div></div>
        <div>
          <svg t="1731231591693" class="icon-size-24 icon-theme-stand-out svg-translateY-5" viewBox="0 0 1024 1024"
               version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="9129" width="200" height="200">
            <path
              d="M457.102222 713.130667a54.869333 54.869333 0 1 0 109.738667 0 54.869333 54.869333 0 0 0-109.738667 0z m82.289778-128a9.159111 9.159111 0 0 0 9.159111-9.130667V265.102222A9.159111 9.159111 0 0 0 539.392 256h-54.840889a9.159111 9.159111 0 0 0-9.159111 9.130667V576c0 5.034667 4.124444 9.130667 9.159111 9.130667h54.840889z m457.159111-214.840889h-67.783111c-2.986667 0-5.688889 1.365333-7.424 3.754666l-121.742222 167.793778-57.002667-78.648889a9.102222 9.102222 0 0 0-7.424-3.754666h-67.783111a9.187556 9.187556 0 0 0-7.424 14.506666l124.785778 172.231111a18.375111 18.375111 0 0 0 29.724444 0l189.496889-261.376a9.187556 9.187556 0 0 0-7.424-14.506666z m-50.289778 349.696h-73.386666a18.688 18.688 0 0 0-15.530667 8.590222 402.545778 402.545778 0 0 1-56.917333 71.082667A405.731556 405.731556 0 0 1 512.426667 918.755556 406.129778 406.129778 0 0 1 224.426667 799.658667a403.512889 403.512889 0 0 1-87.324445-129.365334A403.086222 403.086222 0 0 1 105.102222 512c0-54.869333 10.752-108.231111 32-158.293333a403.484444 403.484444 0 0 1 87.324445-129.365334A405.731556 405.731556 0 0 1 512.426667 105.244444c54.983111 0 108.344889 10.752 158.520889 31.886223a406.755556 406.755556 0 0 1 129.479111 87.210666c21.703111 21.703111 40.675556 45.511111 56.888889 71.111111a18.261333 18.261333 0 0 0 15.559111 8.533334h73.386666a9.187556 9.187556 0 0 0 8.220445-13.255111C874.012444 130.872889 709.347556 20.565333 518.485333 18.289778 245.560889 15.075556 18.830222 238.279111 18.261333 511.089778c-0.568889 273.038222 220.899556 494.648889 494.165334 494.648889 193.365333 0 360.675556-110.876444 441.941333-272.497778a9.159111 9.159111 0 0 0-8.106667-13.255111z"
              fill-opacity=".65" p-id="9130"></path>
          </svg>
          Issues
        </div>
        <div>
          <el-button type="primary" plain size="medium" @click="addFeedbackWindow">Issues
            <i class="el-icon-plus el-icon--right"></i></el-button>
        </div>
      </div>
      <el-tabs v-if="issuesLoading" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane :label="'全部'+'('+feedbackStatusSumVo.allData+')'" name="-1"></el-tab-pane>
        <el-tab-pane :label="'待处理'+'('+feedbackStatusSumVo.toBeProcessed+')'" name="1"></el-tab-pane>
        <el-tab-pane :label="'进行中'+'('+feedbackStatusSumVo.underWay+')'" name="2"></el-tab-pane>
        <el-tab-pane :label="'已完成'+'('+feedbackStatusSumVo.completed+')'" name="3"></el-tab-pane>
        <el-tab-pane :label="'已关闭'+'('+feedbackStatusSumVo.closed+')'" name="4"></el-tab-pane>
      </el-tabs>
      <div class="issues-div">
        <el-skeleton v-if="loading" :rows="5" animated/>
        <div v-else="!loading" v-for="(item,index) in feedbackList" :key="index" class="issues-item">

          <div class="font-s-16 font-bold mb-20 mt-10 flex-space-between">
            <div>
              <span class="text-underline-hover cursor-pointer" @click="viewInfo(item)">
            {{ item.feedbackTitle }}
            </span>
              <svg v-if="userInfo && userInfo.uuid==item.uid && item.status==1" t="1731225888795"
                   class="icon-theme-1 icon-size-16 svg-translateY-3 ml-6 icon-hover cursor-pointer"
                   @click="editingViewInfo(item)"
                   viewBox="0 0 1024 1024"
                   version="1.1" xmlns="http://www.w3.org/2000/svg"
                   p-id="6361">
                <path
                  d="M853.319 551.563c0-18.85 15.28-34.132 34.131-34.132s34.132 15.28 34.132 34.132V802.1c0 65.223-48.815 119.462-110.928 119.462H196.279c-62.112 0-110.928-54.24-110.928-119.462V204.791c0-65.221 48.816-119.462 110.928-119.462H465.83c18.85 0 34.133 15.281 34.133 34.133s-15.281 34.132-34.133 34.132H196.28c-22.715 0-42.665 22.167-42.665 51.198V802.1c0 29.031 19.95 51.198 42.664 51.198h614.375c22.714 0 42.665-22.167 42.665-51.198V551.563z m-303.01-4.109c-12.523 14.09-34.097 15.358-48.186 2.835s-15.358-34.098-2.835-48.186l341.32-383.985c12.522-14.089 34.097-15.358 48.186-2.834s15.357 34.097 2.834 48.186L550.31 547.454z"
                  p-id="6362"></path>
              </svg>
            </div>
            <div v-if="userInfo && userInfo.uuid==item.uid" class="mr-10">
              <svg t="1731237344567" class="icon-theme-1 icon-size-16 svg-translateY-3 cursor-pointer icon-hover"
                   viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="10132" width="200" height="200"
                   @click="deleteFeedback(item.id)">
                <path
                  d="M592.7 343.4c-19.3 0-35-15.7-35-35v-65.1h-93.5v65.1c0 19.3-15.7 35-35 35s-35-15.7-35-35v-71.9c0-34.9 28.4-63.3 63.3-63.3h107c34.9 0 63.3 28.4 63.3 63.3v71.9c-0.1 19.3-15.8 35-35.1 35z"
                  p-id="10133"></path>
                <path
                  d="M428.8 346.3H247.7c-19.3 0-35-15.7-35-35s15.7-35 35-35h181.2c19.3 0 35 15.7 35 35s-15.7 35-35.1 35zM774 346.3H592.9c-19.3 0-35-15.7-35-35s15.7-35 35-35H774c19.3 0 35 15.7 35 35s-15.6 35-35 35zM707.2 854.4h-384c-18.7 0-34.1-14.7-35-33.4l-18.1-403.9c-0.9-19.3 14.1-35.7 33.4-36.5 19.3-0.9 35.7 14.1 36.5 33.4l16.6 370.5h317L690.2 414c0.9-19.3 17.2-34.3 36.5-33.4 19.3 0.9 34.3 17.2 33.4 36.5L742 821c-0.7 18.6-16.1 33.4-34.8 33.4z"
                  p-id="10134"></path>
                <path
                  d="M454.1 723.3c-13.8 0-25-11.2-25-25V410.8c0-13.8 11.2-25 25-25s25 11.2 25 25v287.5c0 13.8-11.2 25-25 25zM579.9 723.3c-13.8 0-25-11.2-25-25V410.8c0-13.8 11.2-25 25-25s25 11.2 25 25v287.5c0 13.8-11.2 25-25 25z"
                  p-id="10135"></path>
              </svg>

            </div>
          </div>
          <div class="flex-direction-row font-s-14 color-grey-2 mr-10">
            <div class="cursor-pointer hover-cl">
              <el-tag :type="getTagType(item.status)[1]" size="mini" effect="plain" class="mr-5">
                {{ getTagType(item.status)[0] }}
              </el-tag>
              <svg t="1731222270753" class="icon icon-hover icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="1628">
                <path
                  d="M651.296021 545.977859c63.557548-42.814108 105.358583-115.235534 105.358583-197.532889 0-133.333728-110.364597-241.9761-245.813498-241.9761-135.451972 0-244.160859 108.642372-244.160859 240.322437 0 82.297354 41.821501 154.739247 105.35756 197.530842-143.80522 55.979966-244.135276 191.069687-244.135276 350.748433l0 0 68.561515 0 0 0c0-158.026106 122.06815-288.197818 277.581016-306.303174 1.674129 0 43.476187-3.284813 78.595033 0l3.350304 0 0 0C708.17445 610.18009 825.215096 738.574319 825.215096 894.946762l0 0 70.239737 0 0 0C893.779681 736.921679 793.447579 600.303138 651.296021 545.977859L651.296021 545.977859zM510.841106 527.85613c-102.008278 0-183.930079-80.644715-183.930079-181.063799 0-100.419084 81.921801-181.062775 183.930079-181.062775 102.007255 0 183.929056 80.643692 183.929056 181.062775C694.770162 447.211415 612.848361 527.85613 510.841106 527.85613L510.841106 527.85613zM510.841106 527.85613"
                  p-id="1629"></path>
              </svg>
              <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(item.uid)"
                         target="_blank">
                {{ item.nickname }}
              </nuxt-link>
            </div>
            <div class="font-s-13" :title="'创建数据：'+item.createTime">
              {{ $utils.reckonTime(item.updateTime) }}
            </div>
          </div>
        </div>
        <div v-if="!loading && feedbackList.length==0" style="text-align: center;margin-top: 10px">
          <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="60">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-14 color-grey-2">暂无Issues</div>
        </div>
        <el-pagination class="mt-10"
                       v-if="!loading && feedbackList.length>0"
                       @current-change="getList"
                       :current-page.sync="queryParams.pageNum"
                       :page-size="queryParams.pageSize"
                       :total="total"
                       layout="prev, pager, next">
        </el-pagination>
      </div>
      <!--      Issues详情-->
      <el-dialog
        title="Issues详情"
        v-if="dialogVisible"
        :visible.sync="dialogVisible"
        width="50%">
        <div class="font-bold-s mb-10 font-s-18 color-black">{{ feedbackInfo.feedbackTitle }}</div>
        <hr class="hr-item mb-10"/>
        <vditor-preview :id="'issues-info'+feedbackInfo.id" :content="feedbackInfo.feedbackContent"></vditor-preview>
        <div class="flex-direction-row mt-20">
          <div>
            <div class="font-s-13">
              <span class="font-bold-s">最新状态：</span>
              <svg t="1731222270753" class="icon icon-hover icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="1628">
                <path
                  d="M651.296021 545.977859c63.557548-42.814108 105.358583-115.235534 105.358583-197.532889 0-133.333728-110.364597-241.9761-245.813498-241.9761-135.451972 0-244.160859 108.642372-244.160859 240.322437 0 82.297354 41.821501 154.739247 105.35756 197.530842-143.80522 55.979966-244.135276 191.069687-244.135276 350.748433l0 0 68.561515 0 0 0c0-158.026106 122.06815-288.197818 277.581016-306.303174 1.674129 0 43.476187-3.284813 78.595033 0l3.350304 0 0 0C708.17445 610.18009 825.215096 738.574319 825.215096 894.946762l0 0 70.239737 0 0 0C893.779681 736.921679 793.447579 600.303138 651.296021 545.977859L651.296021 545.977859zM510.841106 527.85613c-102.008278 0-183.930079-80.644715-183.930079-181.063799 0-100.419084 81.921801-181.062775 183.930079-181.062775 102.007255 0 183.929056 80.643692 183.929056 181.062775C694.770162 447.211415 612.848361 527.85613 510.841106 527.85613L510.841106 527.85613zM510.841106 527.85613"
                  p-id="1629"></path>
              </svg>
              <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(feedbackInfo.updateBy)"
                         target="_blank">
              <span class="cursor-pointer hover-cl mr-2">
              {{ feedbackInfo.nickname }}
              </span>
              </nuxt-link>
              |
              <span :title="feedbackInfo.updateTime" class="ml-2">  {{
                  $utils.reckonTime(feedbackInfo.updateTime)
                }}</span>
              <el-tag :type="getTagType(feedbackInfo.status)[1]" size="mini" effect="plain" class="ml-5">
                {{ getTagType(feedbackInfo.status)[0] }}
              </el-tag>
            </div>
          </div>
          <div v-if="userInfo && userInfo.roleId==0">
            <el-select v-model="feedbackInfo.status" placeholder="请选择状态" size="medium">
              <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-button size="medium" type="primary" @click="updateFeedbackInfoStatus()">更新状态</el-button>
          </div>
        </div>
      </el-dialog>

      <!--      Issues编辑-->
      <el-dialog
        title="Issues编辑"
        v-if="editingDialogVisible"
        :visible.sync="editingDialogVisible"
        width="50%">
        <el-input placeholder="请输入标题"
                  v-model="feedbackInfo.feedbackTitle"
                  clearable>
        </el-input>
        <div class="mt-10">
          <vditor-md :vditor-id="'issues-editing'"
                     :outline="false"
                     :content="addFeedbackInfo.feedbackContent"
                     :mdContent.sync="addFeedbackInfo.feedbackContent"></vditor-md>
        </div>
        <div slot="footer">
          <el-button size="medium" type="primary" @click="updateFeedbackInfo()">更 新</el-button>
        </div>
      </el-dialog>
      <!--    Issues新建  -->
      <el-dialog
        title="Issues新建"
        v-if="addDialogVisible"
        :visible.sync="addDialogVisible"
        width="50%">
        <el-input placeholder="请输入标题"
                  v-model="addFeedbackInfo.feedbackTitle"
                  clearable>
        </el-input>
        <div class="mt-10">
          <vditor-md
            :vditor-id="'issues-add'"
            :outline="false"
            :content="addFeedbackInfo.feedbackContent"
            :mdContent.sync="addFeedbackInfo.feedbackContent"></vditor-md>
        </div>
        <div slot="footer">
          <el-button size="medium" type="primary" @click="addFeedback">新 建</el-button>
        </div>
      </el-dialog>
    </div>
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>
import VditorMd from "../../components/Vditor-md.vue";
import VditorPreview from "../../components/Vditor-preview.vue";

export default {
  components: {VditorPreview, VditorMd},
  data() {
    return {
      statusOptions: [
        {
          value: 1,
          label: '待处理'
        },
        {
          value: 2,
          label: '进行中'
        },
        {
          value: 3,
          label: '已完成'
        },
        {
          value: 4,
          label: '已关闭'
        }
      ],
      loginDialog: false,
      loading: true,
      issuesLoading: false,
      dialogVisible: false,
      editingDialogVisible: false,
      addDialogVisible: false,
      feedbackList: [],
      feedbackInfo: {},
      addFeedbackInfo: {},
      feedbackStatusSumVo: {},
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        status: 1
      },
      total: 0,
      activeName: "1",
      userInfo: {},
    }
  },
  methods: {
    deleteFeedback(id) {
      if (this.userInfo == null) {
        this.loginDialog = true;
      } else {
        this.$modal.confirm('确认要删除该Issues吗？').then(() => {
          this.$API("/frontDesk/feedback/delete/" + id, "delete").then(res => {
            if (res.code == 200) {
              this.$message.success("删除成功");
              this.getList();
            } else {
              this.$message.error("删除失败");
            }
          })
        });
      }
    },
    addFeedback() {
      if (this.addFeedbackInfo.feedbackTitle == null || this.addFeedbackInfo.feedbackTitle == "") {
        this.$message.warning("标题不能为空");
        return;
      }
      this.$API("/frontDesk/feedback/add", this.$post(), null, this.addFeedbackInfo).then(res => {
        if (res.code == 200) {
          this.$message.success("新建成功");
          this.addDialogVisible = false;
          this.getList();
        }
      })
    },
    addFeedbackWindow() {
      console.log("this.userInfo" + this.userInfo)
      if (this.userInfo == null) {
        this.loginDialog = true;
      } else {
        this.addFeedbackInfo = {};
        this.addDialogVisible = true;
      }
    },
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    updateFeedbackInfoStatus() {
      this.$API(`/frontDesk/feedback/update/status/${this.feedbackInfo.id}/${this.feedbackInfo.status}`, this.$get()).then(res => {
        if (res.code == 200) {
          this.$message.success("更新成功");
          this.getList();
        } else {
          this.$message.error("更新失败");
        }
        this.dialogVisible = false;
      })
    },
    updateFeedbackInfo() {
      this.$API("/frontDesk/feedback/update", this.$post(), null, this.feedbackInfo).then(res => {
        if (res.code == 200) {
          this.$message.success("更新成功");
          this.editingDialogVisible = false;
          this.getList();
        }
      });
    },
    editingViewInfo(item) {
      if (this.userInfo == null) {
        this.loginDialog = true;
      } else {
        this.$API("/white/feedback/byId/" + item.id, "get").then(res => {
          this.feedbackInfo = res.data;
          this.editingDialogVisible = true;
        })
      }
    },
    viewInfo(item) {
      this.$API("/white/feedback/byId/" + item.id, "get").then(res => {
        this.feedbackInfo = res.data;
        this.dialogVisible = true;
      })
    },

    handleClick(tab) {
      this.queryParams.status = parseInt(tab._props.name);
      this.getList();
    },
    getTagType(status) {
      let res = [];
      switch (status) {
        case 1:
          res = ["待处理", "warning"];
          break
        case 2:
          res = ["进行中", ""];
          break
        case 3:
          res = ["已完成", "success"];
          break
        case 4:
          res = ["已关闭", "info"];
          break
        default :
          res = ["未知状态", ""]
          break
      }
      return res;
    },

    getList() {
      this.loading = true;
      this.$API("/white/feedback/list", this.$get(), this.queryParams).then(res => {
        this.feedbackList = res.rows;
        this.total = res.total;
        this.loading = false;
      })
    },
    getBasicsUsers() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
        this.$API("/white/feedback/status/sum", "get").then(res => {
          this.feedbackStatusSumVo = res.data;
        }).finally(() => {
          this.issuesLoading = true;
        })
        this.getList();
      });
    },
  },
  mounted() {
    this.getBasicsUsers();
  }
}
</script>

<style scoped>
.issues-item {
  border-bottom: 1px solid #bdc3c7;
  padding: 10px 0;
  margin-bottom: 10px;
}

.issues-div {
  max-height: 75vh;
  overflow: auto;
}

.issues-div::-webkit-scrollbar {
  width: 4px;
  height: 4px;
  background-color: #ced6e0;
}

.issues-div::-webkit-scrollbar-track {
  background: #FFFFFF;
  border-radius: 2px;
}

.issues-div::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}

.el-button--medium{
  padding: 10px 20px;
}

</style>
