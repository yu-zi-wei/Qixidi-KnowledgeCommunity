<template>
  <div style="background-color: #fefefe;padding: 20px 25px;border-radius: 4px">
    <template>
      <el-tabs v-model="activeName">
        <el-tab-pane label="反馈中心" name="反馈中心">
          <div class="mb-10">
            <div class="mb-20 font-s-14 color-grey5" style="line-height: 1.5">
              <div class="font-bold mb-8">反馈内容</div>
              1、网站bug反馈，指出bug产生页面，bug产生频率，描述bug细节。<br/>
              2、用户体验优化反馈，给出优化页面，并希望留下您的优化建议。<br/>
              3、数据异常反馈，指出数据异常页面，如果记得产生数据异常的流程请一并描述出来。
            </div>
            <div class="text-center">
              <el-button size="medium" type="primary" @click="$router.push('/issues')">前往反馈</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </template>
  </div>
</template>

<script>

export default {
  name: "feedback",
  data() {
    return {
      activeName: "反馈中心",
    }
  },

}
</script>

<style scoped>

</style>
