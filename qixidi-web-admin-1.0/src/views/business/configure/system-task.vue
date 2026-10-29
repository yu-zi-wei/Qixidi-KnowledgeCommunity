<template>
  <div class="app-container">
    <el-row :gutter="20" v-if="loading">
      <el-col :span="4" v-for="(item,index) in dataList" :key="index" class="mb-20">
        <el-card class="box-card cursor-pointer">
          <div class="flex-space-between mb-10">
            <div class="text-underline-hover"> {{ item.taskName }}</div>
            <div>
              <svg t="1731829822670" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="8071" width="24" height="24" @click="runTask(item.taskUrl)">
                <path
                  d="M512 96C282.624 96 96 282.624 96 512s186.624 416 416 416 416-186.624 416-416S741.376 96 512 96z m0 768c-194.08 0-352-157.92-352-352s157.92-352 352-352 352 157.92 352 352-157.92 352-352 352z"
                  p-id="8072" fill="#27ae60"></path>
                <path
                  d="M466.816 324.96A32 32 0 0 0 416 350.848v339.776a32 32 0 0 0 50.816 25.856l233.6-169.888a32 32 0 0 0 0-51.776l-233.6-169.856z"
                  p-id="8073" fill="#27ae60"></path>
              </svg>
            </div>
          </div>
          <div class="flex-space-between align-items-center font-s-14">
            <div style="color:#636e72 ">
              <svg t="1731829560708" class="icon svg-translateY-5" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="4328" width="20" height="20">
                <path
                  d="M546.688 494.68416V370.3552a34.68288 34.68288 0 0 0-34.69312-34.67776 34.66752 34.66752 0 0 0-34.6624 34.67776v153.0624l148.85888 148.85376a34.56 34.56 0 0 0 24.51456 10.15808 34.56 34.56 0 0 0 24.50944-10.15808 34.65216 34.65216 0 0 0 0-49.02912l-128.52736-128.55808z"
                  fill="#2B3B94" p-id="4329"></path>
                <path
                  d="M835.10272 306.97472l-73.35936 15.7952c38.94784 52.06016 62.35136 116.41344 62.35136 186.28096 0 172.08832-140.01152 312.09472-312.09984 312.09472-172.0576 0-312.07936-140.0064-312.07936-312.09472 0-172.06272 140.02176-312.07936 312.07936-312.07936 17.45408 0 34.54976 1.5616 51.18976 4.3776l-18.66752 67.74784 193.31072-50.29376-140.2368-142.2592-15.85664 57.51808a378.4192 378.4192 0 0 0-69.74464-6.44608C301.3376 127.616 130.56 298.3936 130.56 509.05088c0 210.688 170.7776 381.45024 381.43488 381.45024 210.688 0 381.45024-170.76224 381.45024-381.45024 0-74.33216-21.4784-143.50848-58.3424-202.07616z"
                  fill="#2B3B94" p-id="4330"></path>
              </svg>
              {{ item.taskExecutionTime }}
            </div>
            <div class="svg-translateY-4" style="color:#ffa502 " title="已运行次数">
              {{ item.taskExecutionSum }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {systemTaskList} from "../../../api/systemTask";
import request from '@/utils/request'

export default {
  name: "systemTask",
  data() {
    return {
      dataList: [],
      loading: false,
    };
  },
  methods: {
    runTask(taskUrl) {
      this.$modal.confirm('确定要手动执行一次当前任务吗？').then(() => {
        return request({url: taskUrl, method: 'get'}).then(res => {
          this.$message.success('执行成功');
          this.systemTaskListApi();
        });
      })
    },
    systemTaskListApi() {
      systemTaskList().then(res => {
        this.dataList = res.data;
      }).finally(() => {
        this.loading = true
      });
    }
  },
  mounted() {
    this.systemTaskListApi();
  }
}
</script>

<style scoped lang="scss">

</style>
