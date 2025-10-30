<template>
  <div class="home-top-index">
    <el-tabs v-model="special">
      <el-tab-pane label="专 栏" name="special">
        <el-row>
          <el-col :span="10">
            <el-input v-model="queryParams.specialName" placeholder="请输入标题关键字"
                      suffix-icon="el-icon-el-input__icon el-icon-search"></el-input>
          </el-col>
          <el-col :span="3" class="fl-right">
            <el-button type="primary" size="small" plain @click="add">创建专栏
              <i class="el-icon-plus el-icon--right"></i></el-button>
          </el-col>
        </el-row>
        <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
        <ul class="content" v-if="specialUserList.length>0 && !loading">
          <li v-for="(item,index) in specialUserList" class="contentItem" :key="index"
              :ref="`userAdminSpecialItem${index}`">
            <div class="flex-space-between">
              <div class="flex-left">
                <div>
                  <el-image v-if="item.cover" :src="item.cover" fit="cover"
                            style="height: 80px;width: 100px;border-radius: 2px"></el-image>
                  <el-image v-else src="/img/shu.jpg" fit="cover"
                            style="height: 80px;width: 100px;border-radius: 2px"></el-image>
                </div>
                <div class="ml-10">
                  <div @click="specialIndex(item)">
                    <p class="font-s-18 font-bold-s mb-12 text-underline-hover cursor-pointer">{{
                        item.specialName
                      }}</p>
                    <p class="font-s-16 mb-12 overflow-nowrap-1">
                      {{ item.specialIntroduce ? item.specialIntroduce : '--' }}
                    </p>
                  </div>
                  <div class="font-s-14 color-grey">
                    <span v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d}')"></span>
                    <span>.</span>
                    <span>{{ item.includedCount }}篇文章</span>
                  </div>
                </div>
              </div>
              <div>
                <el-dropdown trigger="click">
                  <span class="left-loc-cl" title="更多"><i class="el-icon-more"></i></span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="administration(item)">管理内容</el-dropdown-item>
                    <el-dropdown-item @click.native="edit(item)">更新简介</el-dropdown-item>
                    <el-dropdown-item @click.native="deletes(item)">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
            <hr class="hr-item"/>
          </li>
        </ul>
        <div v-if="specialUserList.length==0 && !loading" class="text-center mt-30">
          <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2698" width="40" height="40">
            <path
              d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
              p-id="2699"></path>
          </svg>
          <div class="font-s-12 color-grey-2">暂无数据</div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :title="tips.title" :visible.sync="dialogVisible" width="30%">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="专栏名称:" prop="specialName">
          <el-input maxlength="20" show-word-limit v-model="form.specialName" placeholder="专栏名称"/>
        </el-form-item>
        <el-form-item label="简介:" prop="specialIntroduce">
          <div class="border-all-1-DCDFE6 border-radius-4">
            <el-input type="textarea" :rows="4" maxlength="200" show-word-limit v-model="form.specialIntroduce"
                      placeholder="专栏简介"/>
          </div>
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
import {createAnimator} from '~/plugins/animationUtils'

export default {
  name: "specialManage.vue",
  data() {
    return {
      special: 'special',
      dialogVisible: false,
      buttonLoading: false,
      tips: {
        button: '更新专栏',
        title: '更 新',
        isAdd: false,
      },
      // 表单参数
      form: {},
      rules: {
        specialName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
        specialIntroduce: [
          {required: true, message: "名称简介不能为空", trigger: "blur"}
        ],
      },
      cover: "",
      loading: true,
      specialUserList: [],
      queryParams: {
        uid: null,
        specialName: null,
      },
      value: [],
      debounceTimer: null,//防抖
      debounceTime: 400,//防抖时间
      animator: null, // 动画器实例
    }
  },
  watch: {
    'queryParams.specialName'() {
      this.specialListUidsDebounceTime();
    },
  },
  methods: {
    specialIndex(item) {
      let routeInfo = this.$router.resolve({
        path: "/external_info/special",
        query: {id: this.$base64.encode(item.id), uuid: this.$base64.encode(item.uid)},
      });
      window.open(routeInfo.href, '_blank');
    },
    administration(item) {
      this.$router.push({
        path: '/user_admin/special/special-administration',
        query: {id: this.$base64.encode(item.id), title: item.specialName}
      })
    },
    deletes(item) {
      this.$modal.confirm('确认要删除《' + item.specialName + '》专栏吗？').then(() => {
        this.loading = true;
        return this.$API("/white/delete/special/" + item.id, "delete");
      }).then(() => {
        this.specialListUids();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => this.loading = false)
    },
    add() {
      this.reset();
      this.tips.isAdd = true;
      this.tips.button = "添 加";
      this.tips.title = "添加专栏";
      this.dialogVisible = true;
    },
    edit(item) {
      this.reset();
      this.tips.isAdd = false;
      this.tips.button = "更 新";
      this.tips.title = "更新专栏";
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
          if (this.tips.isAdd) {
            this.$API("/special/information", "post", null, this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msg("添加成功");
                this.specialListUids();
              }
            }).finally(() => {
              this.buttonLoading = false;
              this.dialogVisible = false;
            });
          } else {
            this.$API("/special/information", "put", null, this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msg("更新成功");
                this.specialListUids();
              }
            }).finally(() => {
              this.buttonLoading = false;
              this.dialogVisible = false;
            });
          }
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
      this.value = []
    },
    isCurrentUser() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null) {
          this.$router.push('/');
          return;
        }
        this.queryParams.uid = res.data.uuid;
        this.specialListUids()
      });
    },
    specialListUidsDebounceTime() {
      this.loading = true;
      clearTimeout(this.debounceTimer);
      this.debounceTimer = setTimeout(() => {
        this.specialListUids();
      }, this.debounceTime);
    },
    specialListUids() {
      this.$API("/white/special/list", "get", this.queryParams).then(res => {
        this.specialUserList = res.data;
        this.loading = false;
        this.animator.triggerAllItemsAnimation(this.specialUserList, 'userAdminSpecialItem');
      })
    }
  },
  mounted() {
    this.animator = createAnimator(this, 'commonList')
    this.isCurrentUser();
  }
}
</script>

<style scoped>
.home-top-index {
  background-color: #FFFFFF;
  border-radius: 4px;
  padding: 10px;
  min-height: 100px;
}

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
  color: var(--theme-color);
}

.content {
  margin-top: 30px;
  background-color: #FFFFFF;
}

.contentItem {
  margin: 15px 0;
  padding: 4px 0;
}
</style>
