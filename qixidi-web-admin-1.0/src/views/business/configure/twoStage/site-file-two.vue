<template>
  <div style="margin: 20px 40px 0 40px;">
    <div style="height: 50px;">
      <el-row>
        <el-col :span="22">
          <el-input
            class="articleTitleInput"
            style="float: left;width: 100%;font-size: 16px"
            maxlength="30"
            prefix-icon="el-icon-edit el-input__icon"
            show-word-limit
            size="medium"
            placeholder="请输入标题"
            v-model="siteFileInfo.title"
            clearable>
          </el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" plain style="margin-left: 20px" size="medium" @click="saveInfo">保 存</el-button>
        </el-col>
      </el-row>
    </div>
    <MdEditor :mdContent.sync="siteFileInfo.content"
              :theme.sync="siteFileInfo.theme" v-if="longState" :content="siteFileInfo.content"
              height="400px"></MdEditor>
  </div>
</template>

<script>
import {getSiteFile, addSiteFile, updateSiteFile} from "@/api/business/configure/siteFile";

export default {
  name: "siteFileTwo",
  data() {
    return {
      siteFileInfo: {
        content: null,
        theme: null,
      },
      operationType: 1,
      longState: false,
    }
  },
  methods: {
    getInfo() {
      let id = this.$route.query.id
      if (id != null) {
        this.operationType = 2;
        getSiteFile(id).then(res => {
          this.siteFileInfo = res.data;
          this.longState = true;
          console.log("this.siteFileInfo:", this.siteFileInfo)
        });
      } else {
        this.longState = true;
      }
    },
    saveInfo() {
      console.log("this.siteFileInfo:", this.siteFileInfo)
      if (this.operationType === 2) {
        updateSiteFile(this.siteFileInfo).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("更新成功");
            this.$router.back();
          }
        });
      } else {
        addSiteFile(this.siteFileInfo).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("保存成功");
            this.$router.back();
          }
        });
      }
    },
  },
  mounted() {
    this.getInfo();
  },
}
</script>

<style scoped>

</style>
