<template>
  <div>
    <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
    <div v-if="loading" style="width: 100%;"></div>
    <ul class="content" v-if="specialUserList.length>0 && !loading">
      <li v-for="item of specialUserList" class="contentItem">
        <div class="overflow-hidden">
          <div class="fl-left">
            <el-image v-if="item.cover" :src="item.cover" fit="cover" style="height: 80px;width: 100px"></el-image>
            <el-image v-else src="/img/shu.jpg" fit="cover" style="height: 80px;width: 100px"></el-image>
          </div>
          <div class="overflow-hidden">
            <div class="fl-left left-details-cl">
              <div @click="specialIndex(item)">
                <p class="details-1 text-underline-hover">{{ item.specialName }}</p>
                <p class="details-2 color-grey">{{ item.specialIntroduce }}</p>
              </div>
              <div class="details-3 color-grey-2">
                <span v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d}')"></span>
                <span style="margin: 0 4px">.</span>
                <span>文章数:</span>
                <span v-text="item.includedCount"></span>
                <span v-if="currentUser" class="fl-right">
               <el-dropdown trigger="click">
                <span class="left-loc-cl" title="更多"><i class="el-icon-more"></i></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="edit(item)">编辑</el-dropdown-item>
                  <el-dropdown-item @click.native="deletes(item)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
              </div>
              <hr class="hr_gradient"/>
            </div>
          </div>
        </div>
      </li>
    </ul>
    <div v-if="specialUserList.length==0 && !loading" style="text-align: center;margin-right: 40px;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="60">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-14 color-grey-2">暂无数据</div>
    </div>
    <el-dialog :title="tips.title" :visible.sync="dialogVisible" width="30%">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="专栏名称:" prop="specialName">
          <el-input maxlength="20" show-word-limit v-model="form.specialName" placeholder="专栏名称"/>
        </el-form-item>
        <el-form-item label="简介:">
          <el-input type="textarea" :rows="4" maxlength="200" show-word-limit v-model="form.specialIntroduce"
                    placeholder="专栏简介"/>
        </el-form-item>
        <el-form-item label="封面:">
          <imageUpload v-model="value" :limit="1"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button :loading="buttonLoading" type="primary" @click="updates">{{ tips.button }}</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "userSpecial",
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      tips: {
        button: '更新专栏',
        title: '更 新',
      },
      // 表单参数
      form: {},
      rules: {
        specialName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
      },
      loading: true,
      specialUserList: [],
      uuid: null,
      keyword: null,
      currentUser: false,
      value: [],
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.isCurrentUser();
    }
  },
  methods: {
    specialIndex(item) {
      let routeInfo = this.$router.resolve({
        path: "/external_info/special",
        query: {id: this.$base64.encode(item.id), uuid: this.$base64.encode(item.uid)},
      });
      window.open(routeInfo.href, '_blank');
    },
    deletes(item) {
      this.$modal.confirm('确认要删除《' + item.specialName + '》专栏吗？').then(() => {
        this.loading = true;
        return this.$APi("/white/delete/special/" + item.id, "get");
      }).then(() => {
        this.specialListUids();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => this.loading = false)
    },
    edit(item) {
      this.reset();
      this.form = item;
      this.value = this.form.cover == null ? [] : [{url: this.form.cover,}];
      this.dialogVisible = true;
    },
    updates() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.value.length > 0) {
            this.form.cover = this.value[0].url;
          }
          this.$API("/special/information", "put", null, this.form).then(response => {
            this.$modal.msg("更新成功");
            this.specialListUids();
          }).finally(() => {
            this.buttonLoading = false;
            this.dialogVisible = false;
          });
        }
      });
    },
    reset() {
      this.form = {
        id: undefined,
        specialName: undefined,
        specialIntroduce: undefined,
        state: undefined,
        uid: undefined,
        cover: undefined,
        createTime: undefined,
        updateId: undefined,
        updateTime: undefined
      };
      this.value = [];
    },
    isCurrentUser() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null || res.data == null) {
          this.currentUser = false;
          return;
        }
        if (res.data.uuid == uuid) {
          this.currentUser = true;
        }
      }).finally(() => this.specialListUids())
    },
    specialListUids() {
      this.uuid = this.$base64.decode(this.$route.query.uuid)
      let queryParams = {
        uid: this.uuid,
        specialName: this.keyword,
      }
      this.$API("/white/special/list", "get", queryParams).then(res => {
        this.specialUserList = res.data;
        this.loading = false;
      })
    }
  },
  mounted() {
    this.isCurrentUser();
  }
}
</script>

<style scoped>
.hr_gradient {
  border: 0;
  height: 1px;
  background: #dcdfe6;
  margin-top: 6px;
  margin-bottom: 10px;
}

.left-loc-cl {
  cursor: pointer;
  margin-right: 26px;
  font-size: 16px;
}

.left-loc-cl:hover {
  color: #1890ff;
}

.left-details-cl {
  color: #5a5e66;
  width: 100%;
  margin-left: 10px;
  cursor: pointer;
}

.details-1 {
  font-size: 18px;
  color: #2f3542;
  margin-bottom: 12px;
}

.details-2 {
  white-space: nowrap;
  overflow: hidden;
  font-size: 14px;
  text-overflow: ellipsis;
  margin-bottom: 12px;
}

.details-3 {
  font-size: 14px;
}

.content {
  height: calc(100vh - 300px);
  background-color: #fefefe;
  overflow: scroll;
  overflow-x: hidden;
}

.contentItem {
  margin: 10px 0;
  padding: 4px 0;
}

.content::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: #ced6e0;
}

.content::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 2px;
}

.content::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}
</style>
