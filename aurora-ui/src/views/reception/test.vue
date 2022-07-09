<template>
   <div>
     <el-row :gutter="20">
       <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>
       <el-col :span="16"><div class="grid-content bg-purple"></div></el-col>
       <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>
     </el-row>
     <h2  @click="isLogin">isLogin</h2>
   </div>
</template>

<script>
import { isLogin } from "@/api/login";
export default {
    name: "test",
  methods:{
    isLogin(){
      isLogin().then(res=>{
        const user = this.$store.state.user.justAuth;
        console.log("res:"+JSON.stringify(res))
        console.log("user:"+JSON.stringify(user))
      })
    }
  },
}
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  &:last-child {
     margin-bottom: 0;
   }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
