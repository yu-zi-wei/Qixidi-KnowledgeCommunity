<template>
  <div>
    <navigation-bar-module :userInfo.sync="userInfo" :isConceal="false"></navigation-bar-module>
   <div class="mt-30">
     <div class="module-main">
       <div class="top-cl-user-info">
         <div class="cursor-pointer" @click="$router.push('/user_home/article?uuid='+$base64.encode(userInfo.uuid))">
           <i class="el-icon-arrow-left mr-6"></i>返回个人主页
         </div>
       </div>
       <div class="flex-left">
         <div class="mr-20 flex-2 padding-6 background-color-fefefe" style="height: 60vh">
           <el-menu :default-active="$route.path"
                    :router="true"
                    class="el-menu-vertical-demo">
             <el-menu-item index="/user_info/settings">
               <i class="el-icon-menu"></i>
               <span slot="title">个人信息</span>
             </el-menu-item>
             <el-menu-item index="/user_info/account-number">
               <i class="el-icon-document"></i>
               <span slot="title">账号资料</span>
             </el-menu-item>
           </el-menu>
         </div>
         <div class="background-color-fefefe flex-8">
           <nuxt-child/>
         </div>
       </div>
     </div>
   </div>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      userInfo: null,
    }
  },
  methods: {
    routeJumpUuid(url) {
      if (this.userInfo != null) {
        this.$router.push({
          path: url,
          query: {uuid: this.$base64.encode(this.userInfo.uuid)}
        })
      } else {
        this.$router.push('/');
      }
    },
  },
}
</script>

<style scoped>
.top-cl-user-info {
  color: #747d8c;
  background-color: #fefefe;
  padding: 0 10px;
  font-size: 14px;
  border-radius: 4px;
  line-height: 46px;
  border-color: #1f2d3d;
  margin-bottom: 20px;
}

</style>
