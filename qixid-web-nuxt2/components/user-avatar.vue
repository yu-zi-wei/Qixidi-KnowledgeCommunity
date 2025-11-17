<template>
  <div>
    <div @click="toggleShow" class="cursor-pointer" title="点击上传">
      <el-avatar :size="100" :src="avatarUrl"></el-avatar>
      <p class="mb-2 mt-4 font-s-16 line-height-24">我的头像</p>
      <div style="width: 200px;margin: auto" class="font-s-14 color-grey line-height-18">支持jpg、png、jpeg 格式大小 5M
        以内的图片
      </div>
    </div>
    <client-only>
      <avatar-upload field="img"
                     @src-fileSet="beforeUpload"
                     @crop-success="cropSuccess"
                     @crop-upload-success="cropUploadSuccess"
                     @crop-upload-fail="cropUploadFail"
                     v-model="show"
                     :width="300"
                     :height="200"
                     :headers="headers"
                     url="/api/front-desk/user/update/avatar"
                     :noRotate="false"
                     :withCredentials="true"
                     :preview="'图形展示'"
                     img-format="png"></avatar-upload>
    </client-only>
  </div>
</template>

<script>
export default {
  name: "user-avatar",
  props: {
    avatarUrl: "",
    default: true,
  },
  data() {
    return {
      show: false,
      headers: {
        Authorization: "Bearer " + this.$store.state.token,
      },
      imgDataUrl: ''
    }
  },
  methods: {
    beforeUpload(fileName, fileType, fileSize) {
      console.log("前置校验")
      // if (file.size > 1024 * 124) {
      //   // 如果文件大小超过 1MB，阻止上传
      //   this.$message.error('文件大小不能超过1MB')
      //   return false
      // }
      // if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
      //   // 如果文件类型不是 JPEG 或 PNG，阻止上传
      //   this.$message.error('只能上传 JPEG 或 PNG 格式的图片')
      //   return false
      // }
      return false;
    },
    toggleShow() {
      this.show = !this.show;
    },
    changeAvatarUrl(url) {
      this.$emit('update:avatarUrl', url)
    },
    cropSuccess(imgDataUrl, field) {
      // 裁剪完成
      console.log('-------- crop success --------');
      this.imgDataUrl = imgDataUrl;
    },
    cropUploadSuccess(jsonData, field) {
      // 服务器返回成功
      if (jsonData.code != 200) {
        this.$modal.msgError("头像上传失败");
        return;
      }
      if (jsonData.code == 200) {
        this.$modal.msgSuccess("头像更新成功");
        this.changeAvatarUrl(jsonData.data.imgUrl);
      }
    },
    cropUploadFail(status, field) {
      // 服务器返回失败
    }
  }
}
</script>

<style scoped>

</style>
