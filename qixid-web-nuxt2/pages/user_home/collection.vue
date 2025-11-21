<template>
  <div>
    <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
    <div class="overflow-hidden" v-if="currentUser && !loading">
      <el-button type="text" class="fl-right mr-4" icon="el-icon-plus" @click="add">新建收藏夹</el-button>
    </div>
    <div v-if="collectionUserLoading">
      <ul v-show="!loading">
        <li v-for="(item,index) in collectionUserList" :key="index" class="contentItem"
            :ref="`userHomeCollectionItem${index}`">
          <div class="flex-space-between">
            <h2 class="font-s-18 text-underline-hover cursor-pointer" @click="specialIndex(item)">
              <svg t="1736501888880" class="icon-theme-1 svg-translateY-6 mr-10" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="6429" width="26" height="26">
                <path
                  d="M856.8 82.8H386.1c-40.4-43.2-135.5-67-188.9-67-94.9 0-175.8 70.5-188.9 164.4-5.6 16-8.3 31.3-8.3 46.7v603.7c0 98.7 80.3 179.2 179.2 179.2h695.7c82 0 148.8-66.7 148.8-148.8V249.8c0-92.2-74.8-167-166.9-167z m0 30.4c60 0 110.5 39.2 128.7 93.2H627.2l-183.5-93.2h413.1z m136.6 747.7c0 65.3-53.1 118.4-118.4 118.4H179.3c-82 0.1-148.9-66.7-148.9-148.8V226.8c0-12.3 2.3-24.7 7-38l0.8-3.2c10.5-79.4 78.7-139.5 159.1-139.5 56.2 0 142.9 26.7 170.1 61.2l4.6 5.8h2.6l242.3 120.3h374.9c0.6 5.3 1.6 10.6 1.6 16.1v611.4z m0 0"
                  p-id="6430"></path>
                <path
                  d="M201.3 842.6h791v27.8h-791v-27.8z m-92.9 0h30.3v30.3h-30.3v-30.3z m258-260L346.1 693.8c-1.9 10.7 2.3 21.3 11 27.8 8.8 6.5 20.1 7.4 29.8 2.5l98.7-51.6 99.5 53.6c4.3 2.3 8.9 3.4 13.5 3.4 5.7 0 11.5-1.8 16.4-5.3 8.8-6.3 13.3-16.8 11.6-27.6L608.1 586.8l81.8-78c7.8-7.5 10.7-18.6 7.5-29-3.3-10.4-12-17.8-22.7-19.5L564.5 444l-49-101.9c-4.7-9.8-14.4-15.9-25.2-16.1-9.7-0.5-20.7 5.8-25.6 15.5L415.1 441.1l-112 15.1c-10.7 1.5-19.6 8.8-23.1 19.1s-0.8 21.4 6.9 29.1l79.5 78.2z m52.8-111.3c9.2-1.2 17.2-6.9 21.5-15.3l49.2-97.2 47.2 98.3c4.1 8.4 12 14.3 21.2 15.7L666.1 489.6l-78.8 75.2c-6.7 6.4-9.9 15.7-8.4 25l18.5 108.3-97.3-52.3c-4.2-2.3-8.8-3.4-13.4-3.4-4.4 0-8.9 1-13 3.1l-97 49.7 19.6-107.3c1.7-9.2-1.3-18.6-7.9-25.1l-77.3-77 108.1-14.5z m0 0"
                  p-id="6431"></path>
              </svg>
              {{ item.collectionName }}
            </h2>
            <div v-if="currentUser">
              <el-dropdown trigger="click">
                <span class="left-loc-cl" title="更多"><i class="el-icon-more"></i></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="edit(item)">编辑</el-dropdown-item>
                  <el-dropdown-item @click.native="deletes(item)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <div class="font-s-14 color-grey mt-15">
            <svg t="1741416645564" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="38483">
              <path
                d="M512 64C264.946 64 64 264.946 64 512s200.946 448 448 448 448-200.946 448-448S759.054 64 512 64z m0 831.828c-211.613 0-383.828-172.215-383.828-383.828S300.216 128.172 512 128.172c211.785 0 383.828 172.044 383.828 383.828 0 211.785-172.215 383.828-383.828 383.828z"
                p-id="38484"></path>
              <path
                d="M672.172 512h-160V288.173c0-17.72-14.28-32.172-32-32.172s-32 14.451-32 32.172v256c0 17.719 14.28 31.999 32 31.999h192c17.72 0 32.173-14.28 32.173-31.999C704.172 526.28 689.721 512 672.172 512z"
                p-id="38485"></path>
            </svg>
            <span>{{ $utils.reckonTime(item.createTime) }}</span>
            <span>.</span>
            <span>{{ item.includedCount }} 篇文章</span>
          </div>
          <hr class="hr_gradient"/>
        </li>
      </ul>
    </div>
    <div v-else style="text-align: center;margin-right: 40px;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="50">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-14 color-grey-2">暂无数据</div>
    </div>
    <el-dialog :title="tips.title" :visible.sync="dialogVisible" width="30%">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称:" prop="collectionName">
          <el-input maxlength="20" show-word-limit v-model="form.collectionName" placeholder="收藏夹名称"/>
        </el-form-item>
        <el-form-item label="简介:">
          <div class="border-all-1-DCDFE6 border-radius-4">
            <el-input type="textarea" :rows="4" maxlength="200" show-word-limit v-model="form.collectionIntroduce"
                      placeholder="收藏夹简介"/>
          </div>
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
  name: "userCollection",
  data() {
    return {
      loading: true,
      dialogVisible: false,
      collectionUserLoading: true,
      collectionUserList: [],
      uuid: null,
      currentUser: false,
      form: {
        collectionName: undefined,
        collectionIntroduce: undefined,
      },
      tips: {
        isAdd: true,
        button: '添加',
        title: '添加',
      },
      buttonLoading: false,
      rules: {
        collectionName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
      },
      animator: null, // 动画器实例
    }
  },
  methods: {
    reset() {
      this.form = {
        id: undefined,
        collectionName: undefined,
        collectionIntroduce: undefined,
        collectionCover: undefined,
        status: 0,
        uid: undefined,
        createTime: undefined,
        updateId: undefined,
        updateTime: undefined
      };
    },
    specialIndex(item) {
      let routeInfo = this.$router.resolve({
        path: "/external_info/collection-article-list",
        query: {
          id: this.$base64.encode(item.id),
          uuid: this.$base64.encode(item.uid)
        },
      });
      window.open(routeInfo.href, '_blank');
    },
    updates() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.tips.isAdd) {
            this.$API("/frontDesk/add/collection", "post", null, this.form).then(response => {
              this.$modal.msg("添加成功");
              this.collectionListUids();
            }).finally(() => {
              this.buttonLoading = false;
              this.dialogVisible = false;
              this.collectionUserLoading = true;
            });
          } else {
            this.$API("/business/collection/information", "put", null, this.form).then(response => {
              this.$modal.msg("更新成功");
              this.collectionListUids();
            }).finally(() => {
              this.buttonLoading = false;
              this.dialogVisible = false;
              this.collectionUserLoading = true;
            });
          }
        }
      });
    },
    deletes(item) {
      this.$modal.confirm('确认要删除《' + item.collectionName + '》收藏夹吗？').then(() => {
        this.loading = true;
        return this.$API("/frontDesk/delete/collection/" + item.id, "delete");
      }).then(() => {
        this.collectionListUids();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => {
        this.loading = false
        this.collectionUserLoading = true;
      })
    },
    add() {
      this.reset();
      this.tips.isAdd = true;
      this.tips.button = "添加";
      this.tips.title = "添加收藏夹";
      this.dialogVisible = true;
    },
    edit(item) {
      this.reset();
      this.tips.isAdd = false;
      this.tips.button = "更新";
      this.tips.title = "更新收藏夹";
      this.form = item;
      this.dialogVisible = true;
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
      }).finally(() => this.collectionListUids())
    },
    collectionListUids() {
      this.uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/white/collection/list/" + this.uuid, "get").then(res => {
        this.collectionUserList = res.data;
        this.loading = false;
        if (this.collectionUserList.length == 0) {
          this.collectionUserLoading = false;
        }
        this.animator.triggerAllItemsAnimation(this.collectionUserList, 'userHomeCollectionItem');
      })
    },
  },
  mounted() {
    this.animator = createAnimator(this, 'commonList')
    this.isCurrentUser();
  }
}
</script>

<style scoped>
.hr_gradient {
  border: 0;
  height: 1px;
  background: #dcdfe6;
  margin-top: 12px;
  margin-bottom: 10px;
}

.left-loc-cl {
  cursor: pointer;
  margin-right: 32px;
  font-size: 16px;
}

.left-loc-cl:hover {
  color: #1890ff;
}

.contentItem {
  margin: 10px 0;
  padding: 4px 0;
}
</style>
