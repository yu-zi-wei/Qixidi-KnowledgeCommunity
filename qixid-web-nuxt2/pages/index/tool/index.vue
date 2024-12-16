<template>
  <div>
    <div class="flex-space-between">
      <div style="width: 150px">
        <el-skeleton class="mt-10" :rows="6" animated v-if="toolArray.length==0"/>
        <div style="height: 80vh" class="tool-left-cl" v-show="toolArray.length>0">
          <!--          <div style="width: 100%;padding:10px 10px 0 30px;">-->
          <!--            <div class="cursor-pointer" title="工具申请" @click="addToolVisible=true">-->
          <!--              <span class="font-bold mr-2 color-blue">工具申请</span>-->
          <!--              <svg t="1683376523798" class="icon svg-text-center-24-f4" viewBox="0 0 1024 1024" version="1.1"-->
          <!--                   xmlns="http://www.w3.org/2000/svg"-->
          <!--                   p-id="14337" width="24" height="24">-->
          <!--                <path-->
          <!--                  d="M417.12 549.92h189.76a33.44 33.44 0 0 1 37.92 37.92v75.84a33.44 33.44 0 0 1-37.92 37.92H417.12a33.44 33.44 0 0 1-37.92-37.92v-75.84a33.44 33.44 0 0 1 37.92-37.92z"-->
          <!--                  fill="#1890ff" opacity=".15" p-id="14338"></path>-->
          <!--                <path-->
          <!--                  d="M360.32 644.8h-208a56.96 56.96 0 0 1-56.8-56.96v-304a56.8 56.8 0 0 1 56.8-56.96h208v-75.2a37.92 37.92 0 0 1 37.92-37.92h227.52a37.92 37.92 0 0 1 37.92 37.92v75.84h208a56.8 56.8 0 0 1 56.8 56.96v304a56.96 56.96 0 0 1-56.8 56.96h-208v18.88a56.8 56.8 0 0 1-56.8 55.68H417.12a56.8 56.8 0 0 1-56.8-56.96z m0-37.92v-19.04a56.8 56.8 0 0 1 56.8-56.8h189.76a56.8 56.8 0 0 1 56.8 56.8v19.04h208a18.88 18.88 0 0 0 18.88-19.04v-304a18.88 18.88 0 0 0-18.88-19.04h-720a18.88 18.88 0 0 0-18.88 19.68v304a18.88 18.88 0 0 0 18.88 19.04z m37.92-379.36h227.52v-75.84H398.24z m18.88 341.44a18.88 18.88 0 0 0-18.88 18.88v75.84a18.88 18.88 0 0 0 18.88 19.04h189.76a18.88 18.88 0 0 0 18.88-19.04v-75.84a18.88 18.88 0 0 0-18.88-18.88z m455.2 116.8a19.04 19.04 0 0 1 37.92 0V816A94.72 94.72 0 0 1 816 910.24H208A94.72 94.72 0 0 1 113.76 816V684a19.04 19.04 0 0 1 37.92 0v132A56.96 56.96 0 0 0 208 872.32h608A56.96 56.96 0 0 0 872.32 816z"-->
          <!--                  fill="#1890ff" p-id="14339"></path>-->
          <!--              </svg>-->
          <!--            </div>-->
          <!--          </div>-->
          <el-menu
            :default-active="$route.path+'?tool='+$route.query.tool"
            :router="true"
            class="el-menu-vertical-demo tool-list-left">
            <div v-for="item of toolArray">
              <el-menu-item :index="'/tool?tool='+item.id" @click="toolClick(item.id)">
                <template slot="title">
                  <span v-text="item.toolName"></span>
                </template>
              </el-menu-item>
            </div>
          </el-menu>
        </div>
      </div>
      <div style="width: 1100px">
        <div class="mb-10">
          <el-input placeholder="关键字（Enter 检索）" suffix-icon="el-input__icon el-icon-search"
                    v-model="query.toolName" @keyup.enter.native="isSearch = true"></el-input>
        </div>
        <div>
          <toolListAssembly :id="id" :isSearch="isSearch" :toolName="query.toolName"/>
        </div>
      </div>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="addToolVisible"
      width="30%">
    </el-dialog>
  </div>
</template>

<script>
import toolListAssembly from './tool-list'

export default {
  head: {
    title: `工具 - ${process.env.PROJECT_NAME}`,
  },
  components: {
    toolListAssembly
  },
  name: "tool",
  data() {
    return {
      toolArray: [],
      id: 1,
      query: {
        toolName: null,
      },
      isSearch: false,
      addToolVisible: false,
    }
  },
  methods: {
    toolClick(id) {
      this.query.toolName = null;
      this.isSearch = false;
      this.id = id;
    },
    toolLists() {
      this.$API("/white/configure/tool/list", "get").then(res => {
        this.toolArray = res;
        this.$router.push({
          path: '/tool',
          query: {tool: this.id}
        })
      })
    }
  },
  mounted() {
    this.toolLists();
  }
};
</script>

<style scoped>
.tool-left-cl {
  background-color: #fefefe;
  border-radius: 4px;
  box-shadow: 0 2px 6px 0 #dfe6e9;
}

.el-card.is-always-shadow {
  box-shadow: none;
  border-style: none;
}

.tool-list-left {
  padding: 10px;
  font-weight: 700;
  letter-spacing: 2px;
}
</style>
