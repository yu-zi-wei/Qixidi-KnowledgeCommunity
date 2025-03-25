<template>
  <div class="home-top-index">
    <div>
      <div class="cursor-pointer mb-20">
        <router-link to="/user_admin/special/special-manage">
          <i class="el-icon-arrow-left"></i>返回
        </router-link>
        <hr class="hr_gradient"/>
      </div>
      <el-transfer
        filterable
        filter-placeholder="请输入标题关键字"
        v-model="value"
        :props="{
         key: 'id',
         label: 'articleTitle',
         pinyin: 'articleTitle'
         }"
        :titles="['文章列表','专栏：'+ $route.query.title || '专栏']"
        :button-texts="['移除专栏', '添加到专栏']"
        @change="handleChange"
        :data="data">
        <el-button class="transfer-footer fl-right mt-4 mr-10" type="primary" slot="right-footer" size="small"
                   :loading="buttonLoading" @click="updateSpecial">
          保 存 <i class="el-icon-check"></i>
        </el-button>
      </el-transfer>
    </div>
  </div>
</template>

<script>

export default {
  name: "specialAdministration",
  data() {
    return {
      buttonLoading: false,
      queryParams: {
        userId: null,
        auditState: null,
      },
      data: [],
      value: [],
      specialId: null,
    };
  },
  methods: {
    isCurrentUser() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null || res.data == null) {
          this.$router.push('/');
          return;
        }
        this.queryParams.userId = res.data.uuid;
        this.getArticleInfoLists();
        this.selectSpecials();
      });
    },
    selectSpecials() {
      let id = this.$base64.decode(this.$route.query.id)
      this.specialId = id;
      let qurey = {
        id: id,
        uid: this.queryParams.userId,
      }
      this.$API("/white/select/special", "get", qurey).then(res => {
        if (res.data.length > 0) {
          res.data.forEach(item => {
            this.value.push(item.id);
          })
        }
      })
    },
    getArticleInfoLists() {
      this.$API("/white/article/user/list", "get", this.queryParams).then(res => {
        this.data = res.data.records;
      })
    },
    updateSpecial() {
      this.buttonLoading = true;
      console.log("value：", this.value)
      this.$API("/white/update/special/" + this.specialId + "/" + this.queryParams.userId, "post", null, this.value).then(res => {
        if (res.code == 200) {
          this.$modal.msgSuccess("保存成功");
        }
      }).finally(() => this.buttonLoading = false);
    },
    handleChange(value) {
      // console.log(value);
      // console.log(this.value);
    }
  },
  mounted() {
    this.isCurrentUser();
  }
}
</script>

<style>
.hr_gradient {
  border: 0;
  height: 1px;
  background: #e5e6eb;
  margin: 10px 0 20px 10px;
}

.home-top-index {
  background-color: #FFFFFF;
  border-radius: 4px;
  padding: 20px 10px 40px 20px;
  min-height: 100px;
}

.el-transfer-panel__body {
  height: 400px;
}

.el-transfer-panel__list.is-filterable {
  height: 360px;
}

.el-transfer-panel {
  width: 320px;
}
</style>
