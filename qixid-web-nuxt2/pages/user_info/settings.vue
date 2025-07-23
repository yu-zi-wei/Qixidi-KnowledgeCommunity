<template>
  <div>
    <div style="font-size: 22px;font-weight: bold; margin: 15px 0 0 10px">个人资料</div>
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="15">
        <el-divider></el-divider>
        <el-form ref="form" :model="form" :rules="rules" label-width="90px" v-if="form!=null">
          <el-form-item label="用户名 " prop="nickname">
            <el-input maxlength="24" show-word-limit v-model="form.nickname" placeholder="用户名"/>
          </el-form-item>
          <el-form-item label="职位 ">
            <el-input maxlength="20" show-word-limit v-model="form.occupation" placeholder="职位"/>
          </el-form-item>
          <el-form-item label="公司 ">
            <el-input maxlength="40" show-word-limit v-model="form.company" placeholder="公司"/>
          </el-form-item>
          <el-form-item label="个人主页 ">
            <el-input maxlength="60" show-word-limit v-model="form.homepage" placeholder="个人主页"/>
          </el-form-item>
          <el-form-item label="个人介绍">
            <div class="border-all-1-DCDFE6 border-radius-4">
              <el-input type="textarea" :rows="6" maxlength="200" show-word-limit v-model="form.introduce"
                        placeholder="个人介绍"/>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="buttonLoading" @click="uploadInfos">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="8">
        <div class="text-center">
          <user-avatar :avatarUrl.sync="form.avatar"></user-avatar>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import UserAvatar from "../../components/user-avatar";

export default {
  name: "settings",
  components: {UserAvatar},
  data() {
    return {
      buttonLoading: false,
      loading: true,
      form: {},
      rules: {
        nickname: [
          {required: true, message: "用户名不能为空", trigger: "blur"}
        ],
      },
      value: null,
    }
  },
  methods: {
    uploadInfos() {
      this.buttonLoading = true;
      this.$API("/update/user/info", "put", null, this.form).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess("保存成功")
          this.frontDeskUsers();
        }
      }).finally(() => this.buttonLoading = false)
    },
    frontDeskUsers() {
      this.loading = true;
      this.$API("/front-desk/user/info", "get").then(res => {
        this.form = res.data;
      }).finally(() => this.loading = false)
    }
  },
  mounted() {
    this.frontDeskUsers();
  }
}
</script>

<style scoped>

</style>
