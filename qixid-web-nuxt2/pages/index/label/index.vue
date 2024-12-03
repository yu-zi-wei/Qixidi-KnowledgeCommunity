<template>
  <div class="module-main">
    <div style="width: 30%;margin-left: 26px">
      <el-input placeholder="标签关键字" v-model="labelName" @keyup.enter.native="labelSearch">
        <el-button slot="append" icon="el-icon-search" @click="labelSearch"></el-button>
      </el-input>
    </div>
    <el-skeleton :rows="12" class="mt-20" animated v-if="loading"/>
    <div v-if="!loading" class="flex-space-around flex-wrap-wrap">
      <div v-for="(item,index) in dateList" :key="index" class="label-item">
        <div class="flex-space-around">
          <div v-html="item.labelCover" class=""></div>
          <div>
            <div class="mb-6">
              <nuxt-link class="font-bold cursor-pointer mb-8 hover-cl" target="_blank"
                         :to="'/external_info/label-info?data='+item.id+'&type='+2">
                {{ item.labelName }}
              </nuxt-link>
              <svg v-if="item.followNumber!=0" t="1717332963282" class="icon icon-size-18 svg-translateY-2"
                   viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="142624">
                <path
                  d="M710.287 320.707c-87.785 26.446-103.857 101.388-98.198 148.99-62.617-77.737-60.05-167.163-60.05-295.997C351.224 253.674 397.88 484.262 391.9 554.283c-50.513-43.664-60.055-147.987-60.055-147.987-53.328 28.965-80.076 106.364-80.076 169.138 0 151.81 116.513 274.867 260.23 274.867 143.714 0 260.23-123.059 260.23-274.867 0-90.22-62.713-131.85-61.957-254.727h0.015m0 0"
                  p-id="142625" fill="#d81e06"></path>
              </svg>
              <span class="font-s-14 color-grey" v-if="item.followNumber!=0">{{ item.followNumber }}</span>
            </div>
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
    }
  },
  methods: {
    labelSearch() {
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
            this.getDate()
            item.buttonLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API("/user/follow/add", "post", null, {targetId: item.id, type: 2,})
          .finally(() => {
            this.getDate()
            item.buttonLoading = false;
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
      }).finally(() => this.loading = false);
    }
  },
  mounted() {
    this.getDate();
    this.getBasicsUsers();
  }
}
</script>

<style scoped>
.label-item {
  background-color: #fefefe;
  padding: 20px 0px;
  margin: 20px;
  border-radius: 4px;
  width: 200px;
  text-align: center;
  cursor: pointer;
  border: 1px solid #fefefe;
}

.label-item:hover {
  border: 1px solid #a4b0be;
}
</style>
